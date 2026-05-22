package infrastructure.persistence.mapper;

import domain.research.JournalName;
import domain.research.PaperId;
import domain.research.ResearchProject;
import domain.shared.Username;
import infrastructure.persistence.json.JsonObjectBuilder;
import infrastructure.persistence.json.JsonValue;
import infrastructure.persistence.orm.EntityMapper;
import infrastructure.persistence.orm.MapperHelpers;

public final class ResearchProjectMapper implements EntityMapper<ResearchProject, Integer> {

    @Override public Integer idOf(ResearchProject p) { return p.id(); }
    @Override public String idAsString(Integer id) { return id.toString(); }

    @Override public JsonValue toJson(ResearchProject p) {
        JsonObjectBuilder b = JsonObjectBuilder.create()
                .put("_id",      Integer.toString(p.id()))
                .put("id",       p.id())
                .put("journal",  p.journal().value())
                .put("topic",    p.topic())
                .put("supervisor", p.supervisor() != null ? p.supervisor().value() : "")
                .putStrings("participants",    p.participants().stream().map(Username::value).toList())
                .putStrings("publishedPapers", p.publishedPapers().stream().map(pid -> Integer.toString(pid.value())).toList());
        return b.build();
    }

    @Override public ResearchProject fromJson(JsonValue json) {
        JsonValue.JsonObject o = (JsonValue.JsonObject) json;
        String supervisorStr = MapperHelpers.readString(o, "supervisor");
        Username supervisor  = (supervisorStr != null && !supervisorStr.isBlank())
                ? new Username(supervisorStr)
                : null;
        ResearchProject p = new ResearchProject(
                MapperHelpers.readInt(o, "id"),
                new JournalName(MapperHelpers.readString(o, "journal")),
                MapperHelpers.readString(o, "topic"),
                supervisor);
        for (String u : MapperHelpers.readStrings(o, "participants")) p.addParticipant(new Username(u));
        for (String pid : MapperHelpers.readStrings(o, "publishedPapers"))
            p.recordPublication(new PaperId(Integer.parseInt(pid)));
        return p;
    }
}
