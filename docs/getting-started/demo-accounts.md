# Demo Accounts

The system starts with a fully seeded dataset on first launch. All accounts use
**username = password** for easy testing. Click any account row on the login page
to auto-fill the credentials.

---

## Administration

| Username | Password | Name | Role |
|---|---|---|---|
| `rauan` | rauan | Rauan Assetov | Admin |
| `serdar` | serdar | Serdar Dundar | Manager |
| `bayan` | bayan | Bayan Assetova | Dean |

## Teaching Staff

| Username | Password | Name | Role | Notes |
|---|---|---|---|---|
| `zhomart` | zhomart | Zhomart Aldamuratov | Teacher, Professor, PhD | Also a Researcher; teaches 7 courses |
| `anel` | anel | Anel Yeraliyeva | Teacher, Senior Lector, MSc | Teaches 7 courses |
| `marat` | marat | Marat Ospanov | Teacher, Professor, PhD | Teaches 7 courses |

## Support Staff

| Username | Password | Name | Role |
|---|---|---|---|
| `bekasyl` | bekasyl | Bekasyl Amanshayev | TechSupport |
| `assylzhan` | assylzhan | Assylzhan Izbassar | Librarian |

## Students

| Username | Password | Name | Year | Notes |
|---|---|---|---|---|
| `dariya` | dariya | Dariya Yergaliyeva | 2 | Grade B in Discrete Math, C in OOP, D in Linear Algebra |
| `bexultan` | bexultan | Bexultan Yeraliyev | 3 | Grade A in OOP, **FX** in Discrete Math, **F (retake)** in Algorithms |
| `assel` | assel | Assel Bazhikey | 1 | **F (not admitted)** in OOP; good in other courses |
| `arman` | arman | Arman Seitkali | 2 | Grade B in Discrete Math, B in OOP, A in Stats |
| `zarina` | zarina | Zarina Bekova | 3 | Grade B in Discrete Math, C in Algorithms, C in DB Systems |
| `dias` | dias | Dias Nurmagambetov | 1 | First year, most courses in progress |
| `aizat` | aizat | Aizat Sultanova | 2 | Grade A in Discrete Math, A in Linear Algebra — Dean's list |
| `alibek` | alibek | Alibek Dzhaksybekov | 4 | Grade A in Algorithms, A in DB Systems, A in SE — Dean's list |
| `madina` | madina | Madina Ospanova | 1 | **FX** in Discrete Math (exam=11), **FX** in Linear Algebra |
| `yerlan` | yerlan | Yerlan Abenov | 2 | **F (not admitted)** in Discrete Math (att=29), D in Technical English |
| `sanzhar` | sanzhar | Sanzhar Mukhanov | 4 | Grade D in OOP, C in Algorithms, **FX** in DB Systems |
| `kamila` | kamila | Kamila Dzhunusova | 1 | Grade C in OOP, C in Linear Algebra |
| `temirlan` | temirlan | Temirlan Sabenov | 3 | Grade A in OOP, **F (not admitted)** in Algorithms, D in SE |

## Graduate Students

| Username | Password | Name | Degree | Notes |
|---|---|---|---|---|
| `nurasyl` | nurasyl | Nurasyl Dulat | Master yr1 | Active Researcher; Grade B in Algorithms, A in DB Systems |
| `aigerim` | aigerim | Aigerim Bekzhanova | Doctorate yr2 | Active Researcher; thesis defense scheduled |

---

## Pre-seeded Data Summary

| Category | Count | Notes |
|---|---|---|
| Users | 23 | All roles represented |
| Courses | 20 | Each with lessons, enrollments, some with grades |
| Lessons | ~55 | Distributed across all courses, various types and rooms |
| Grades | 30+ | Mix of A/B/C/D/FX/F/needsRetake |
| Books | 30 | 5 currently borrowed |
| News posts | 40 | Full semester narrative, comments included |
| Messages | 48 | Multi-thread conversations between various users |
| Help requests | 23 | Mix of PENDING/APPROVED/REJECTED |
| IT orders | 48 | Mix of NEW/ACCEPTED/DONE |
| Research papers | 6 | 3 journals, 3 projects |
| Researchers | 3 | zhomart, nurasyl, aigerim |

---

## Grade Reference

| Letter | Score range | Meaning |
|---|---|---|
| A | 90–100 | Excellent |
| B | 80–89 | Good |
| C | 70–79 | Satisfactory |
| D | 50–69 | Passing (minimum) |
| FX | att ≥ 30 AND exam 10–19 | Conditional fail — may retake exam without fail penalty |
| F | att < 30 OR exam < 10 OR (exam ≥ 20 AND total < 50) | Fail — counts against 3-fail limit |

---

## Resetting the Demo

To start fresh with clean seeded data:

```bash
# Stop the server
# Delete all JSON data files
rm data/*.json
# Restart — DataSeeder runs automatically on first launch
java -jar university-system.jar --server
```
