package com.botscrew;

import com.botscrew.entity.Author;
import com.botscrew.entity.Book;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Console Application
 * Contains main func for user interaction
 *
 * @author Michael Rudyy
 * @version 1.9
 */


public class ConsoleApplication {
    /**
     * Scanner that read input data
     */
    static Scanner sc = new Scanner(System.in);
    /**
     * Set up session ( for connection to data base )
     */
    static Session session = DataBaseUtil.
            getSessionFactory().
            openSession();

    /**
     * Main func
     * Run Application
     */
    static void run() {
        session.beginTransaction();

        boolean statusRun = true;

        greeting();

        do {
            System.out.print(" -> ");
            String command = sc.nextLine();
            command = command.trim();
            switch (command) {
                case "add": {
                    addBook(createBook());
                    break;
                }
                case "remove": {
                    removeBook();
                    break;
                }
                case "edit": {
                    editBook();
                    break;
                }
                case "get": {
                    showBooks(selectBooks());
                    break;
                }
                case "commit": {
                    session.getTransaction().commit();
                    System.out.println(" Commited");
                    break;
                }case "end":{
                    statusRun = false;
                    break;
                }
            }
        } while (statusRun);

        DataBaseUtil.shutDown();
    }

    /**
     * Greeting
     * Show simple instructions how to use the application
     */
    private static void greeting(){
        System.out.println("    Hello! \n " +
                "   My program can add , edit , show and remove books \n" +
                "   Commands : \n" +
                "               - add\n" +
                "               - edit\n" +
                "               - remove\n" +
                "               - get\n" +
                "               - commit\n" +
                "   Try it!" );
    }

    /**
     * Create Book
     * Method that create book
     * @return new book
     */
    private static Book createBook() {
        Book book = new Book();

        if (setTitle(book)) {
            setAuthors(book);
            setYear(book);
            setGenre(book);
            return book;
        } else {
            return null;
        }
    }

    /**
     * Set Title
     * Method set up book title and check if it unique , if not propose to not create new book
     * @param book
     * @return
     */
    public static boolean setTitle(Book book) {
        System.out.print(" Title -> ");
        String title = sc.nextLine();
        List<Book> booksWithSameTitle = getBooksByTitle(title);
        if (booksWithSameTitle.size() == 0) {
            book.setTitle(title);
            return true;
        } else {
            System.out.print(" There is books with same title :");
            showBooks(booksWithSameTitle);
            System.out.print(" You want : \n 1.Create new \n 2.Discard \n ->");
            int type = sc.nextInt();
            sc.nextLine();
            if (type == 1) {
                book.setTitle(title);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Set Authors
     * Method set up new and exist authors to book
     * @param book
     */
    public static void setAuthors(Book book) {
        System.out.print(" Authors -> ");
        String[] authors = sc.nextLine().split(";");
        List<Author> authorsOfBook = new ArrayList<>();
        List<Author> authorsWithSameName;
        for (String name : authors) {
            authorsWithSameName = getAuthorsByName(name);
            if (authorsWithSameName.size() == 0) {
                authorsOfBook.add(createNewAuthor(name));
            } else {
                System.out.println(" There is authors with the same name \n");
                showAuthors(authorsWithSameName);
                System.out.print("\n You want : \n 1.Create new \n 2.Use created \n -> ");
                int type = sc.nextInt();
                if (type == 1) {
                    authorsOfBook.add(createNewAuthor(name));
                } else if (type == 2) {
                    System.out.print(" Which one ? -> ");
                    authorsOfBook.add(authorsWithSameName.get(sc.nextInt()));
                } else {
                    System.out.println(" Create new Author ");
                    authorsOfBook.add(createNewAuthor(name));
                }
            }
        }
        book.setAuthors(authorsOfBook);
    }

    /**
     * Set Year
     * @param book
     */
    public static void setYear(Book book) {
        System.out.print(" Year -> ");
        int year = 0000;
        try {
            year = sc.nextInt();
        }catch (InputMismatchException e){
            System.out.println(" Invalid Year ");
        }
        sc.nextLine();
        book.setYear(year);
    }

    /**
     * Set Genre
     * @param book
     */
    public static void setGenre(Book book) {
        System.out.print(" Genre -> ");
        String genre = sc.nextLine();
        book.setGenre(genre);
    }

    /**
     * Remove Book
     * Remove book if it != null
     * If book == null method do nothing
     * @param book
     */
    private static void removeBook(Book book) {
        if(book != null){
            session.delete(book);
            System.out.println(" Removed");
        }else{
            System.out.println(" Invalid book");
        }
    }

    /**
     * Collect data for book removing
     * Method that remove Book
     * Use {@code selectBooks()} to get book
     * Redirect book to {@code removeBook(Book book)}
     */
    private static void removeBook() {
        removeBook(selectBookFormList(selectBooks()));
    }

    /**
     * Collect data for book editing
     * Method that edit Book
     * Use {@code selectBooks()} to get book
     * Redirect book to {@code editBook(Book book)}
     */
    private static void editBook() {
        editBook(selectBookFormList(selectBooks()));
    }

    /**
     * Edit Book
     * Provide tools for book editing
     * @param book
     */
    private static void editBook(Book book) {
        boolean editRun = true;
        do {
            System.out.print(" What you want to edit : \n 1.Title \n 2.Author \n 3.Year \n 4.Genre \n 5.Finish \n ->");
            int type = sc.nextInt();
            switch (type) {
                case 1: {
                    sc.nextLine();
                    setTitle(book);
                    break;
                }
                case 2: {
                    sc.nextLine();
                    setAuthors(book);
                    break;
                }
                case 3: {
                    sc.nextLine();
                    setYear(book);
                    break;
                }
                case 4: {
                    sc.nextLine();
                    setGenre(book);
                    break;
                }
                case 5: {
                    sc.nextLine();
                    editRun = false;
                    break;
                }
            }
        } while (editRun);
    }

    /**
     * Show Books
     * Method display books from books list
     * @param books
     */
    private static void showBooks(List<Book> books) {
        if (books != null) {
            for (int i = 0; i < books.size(); i++) {
                System.out.println(" " + i + "  : " + books.get(i));
            }
        }
    }

    /**
     * Get Books by Title
     * @param title
     * @return list of books with title {@code title}
     */
    private static List<Book> getBooksByTitle(String title) {
        return session.createCriteria(Book.class).add(Restrictions.eq("title", title)).list();
    }


    /**
     * Select Books
     * Provide tools for getting list of books with some argument
     * @return list of books
     */
    private static List<Book> selectBooks() {
        System.out.print(" Select by : \n 1.All \n 2.Title \n 3.Author \n 4.Genre \n 5.Year \n -> ");
        int type = Integer.parseInt(sc.nextLine());
        switch (type) {
            case 1: {
                return getBooks();
            }
            case 2: {
                System.out.print(" Title -> ");
                String title = sc.nextLine();
                return getBooksByTitle(title.trim());
            }
            case 3: {
                System.out.print(" Name -> ");
                String name = sc.nextLine();
                return getBooksByAuthor(name);
            }
            case 4: {
                System.out.print(" Genre -> ");
                String genre = sc.next().trim();
                return getBooksByGenre(genre);
            }
            case 5: {
                System.out.print(" Year -> ");
                int year = sc.nextInt();
                return getBooksByYear(year);
            }
        }
        return new ArrayList<Book>();
    }

    /**
     * Get Books by Author name
     * @param name
     * @return list of books of Authors with name {@code name}
     */
    private static List<Book> getBooksByAuthor(String name) {
        List<Book> books = new ArrayList<>();
        List<Author> authors = session.createCriteria(Author.class).add(Restrictions.eq("name", name)).list();
        for (Author author : authors) {
            books.addAll(author.getBooks());
        }
        return books;
    }

    /**
     * Get Books by Genre
     * @param genre
     * @return list of books with genre {@code genre}
     */
    private static List<Book> getBooksByGenre(String genre) {
        return session.createCriteria(Book.class).add(Restrictions.eq("genre", genre)).list();
    }

    /**
     * Get Books by Year
     * @param year
     * @return list of books with year {@code year}
     */
    private static List<Book> getBooksByYear(int year) {
        return session.createCriteria(Book.class).add(Restrictions.eq("year", year)).list();
    }

    /**
     * Select Book from List
     * Provite tools for getting a book from list of books
     * @param books
     * @return book
     */
    private static Book selectBookFormList(List<Book> books) {
        if (books.size() == 1) {
            return books.get(0);
            //session.delete(books.get(0));
        } else if (books.size() > 1) {
            showBooks(books);
            System.out.print(" Which one you want to select ? -> ");
            int selectId = sc.nextInt();
            sc.nextLine();
            if (selectId < books.size()) return books.get(selectId);
            return null;
        } else {
            System.out.println(" There is not such book");
            return new Book();
        }
    }

    /**
     * Show Authors
     * Method display authors from authors list
     * @param authors
     */
    private static void showAuthors(List<Author> authors) {
        if (authors != null) {
            for (int i = 0; i < authors.size(); i++) {
                System.out.println(" " + i + " : " + authors.get(i).toString());
            }
        }
    }

    /**
     * Get Books
     * Return All Books
     * @return list of all books
     */
    private static List<Book> getBooks() {
        return session.createCriteria(Book.class).list();
    }

    /**
     * Get Authors Id by Name
     * @param name
     * @return return list of authors is
     */
    private static List<Integer> getAuthorIdByName(String name) {
        List<Integer> authorIDs = new ArrayList<>();
        List<Author> authors = session.createCriteria(Author.class).
                add(Restrictions.eq("name", name)).
                list();

        for (Author author : authors) {
            authorIDs.add(author.getId());
        }

        return authorIDs;
    }

    /**
     * Get Authors by Name
     * Return Authors with "name"
     * @param name
     * @return list of authors
     */
    private static List<Author> getAuthorsByName(String name) {
        return session.createCriteria(Author.class).
                add(Restrictions.eq("name", name)).
                list();
    }

    /**
     * Add Book
     * If Book != null method add it to session , if not - do nothing
     * @param book
     */
    private static void addBook(Book book) {
        if (book != null){
            session.save(book);
            System.out.println(" Saved");
        }
    }

    /**
     * Add Author
     * If Author != null method add it to session , if not - do nothing
     * @param author
     */
    private static void addAuthor(Author author) {
        if (author != null) {
            session.save(author);
        }
    }

    /**
     * Create new Author using name and add it to session by {@code addAuthor}
     * @param name
     * @return new author
     */
    private static Author createNewAuthor(String name) {
        Author author = new Author(name);
        addAuthor(author);
        return author;
    }

}
