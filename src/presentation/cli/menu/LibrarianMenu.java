package presentation.cli.menu;

import application.Result;
import application.usecase.library.AddBook;
import application.usecase.library.RemoveBook;
import domain.repository.BookRepository;
import domain.user.Librarian;
import presentation.cli.Console;

import java.util.ArrayList;
import java.util.List;

public final class LibrarianMenu extends Menu {
    private final Librarian librarian;
    private final BookRepository books;
    private final AddBook addBook;
    private final RemoveBook removeBook;
    private final CommonMenuActions common;

    public LibrarianMenu(Console console, Librarian librarian, BookRepository books,
                         AddBook addBook, RemoveBook removeBook, CommonMenuActions common) {
        super(console);
        this.librarian = librarian;
        this.books = books;
        this.addBook = addBook;
        this.removeBook = removeBook;
        this.common = common;
    }

    @Override protected String title() { return "=== LIBRARIAN MENU (" + librarian.username() + ") ==="; }

    @Override protected List<MenuItem> items() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("View all books",  this::viewBooks));
        items.add(new MenuItem("Add book",        this::addInteractive));
        items.add(new MenuItem("Remove book",     this::removeInteractive));
        items.add(new MenuItem("View inbox",      common::viewInbox));
        items.add(new MenuItem("Send message",    common::sendMessageInteractive));
        items.add(new MenuItem("View news",       common::viewNews));
        items.add(new MenuItem("Publish news",    common::publishNewsInteractive));
        return items;
    }

    private void viewBooks() {
        var all = books.findAll();
        if (all.isEmpty()) { console.println("No books."); return; }
        all.forEach(b -> console.println("  " + b));
    }

    private void addInteractive() {
        String title  = console.readLine("Title:");
        String author = console.readLine("Author:");
        Result r = addBook.execute(librarian.username(), title, author);
        console.println(r.message());
    }

    private void removeInteractive() {
        viewBooks();
        String title = console.readLine("Title:");
        Result r = removeBook.execute(librarian.username(), title);
        console.println(r.message());
    }
}
