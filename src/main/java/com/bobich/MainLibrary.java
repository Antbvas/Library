package com.bobich;

import com.bobich.service.LibraryService;

public class MainLibrary {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        try {
//            service.addAuthor("Лев", "Толстой");
//            service.addBook("Война и мир", 8, 1869);
//            service.addReader("Semen", "Semenov", "semen@mail.com");
//
//            service.addAuthorAndBook("Stephen", "King", "The Shining", 1977);
//            service.addAuthorAndBook("Stephen", "King", "It", 1986);
//
//            service.addAuthorAndBook("Лев", "Толстой", "Война и мир", 1869);
//            service.addAuthorAndBook("Лев", "Толстой", "Анна Каренина", 1877);

            System.out.println("Books: " + service.getAllBooks());
            System.out.println("Readers: " + service.getAllReaders());
            System.out.println("Authors: " + service.getAllAuthors());


            System.out.println("Поиск по автору 'Лев': " + service.searchBooks("Лев"));
            System.out.println("Поиск по книге 'It': " + service.searchBooks("It"));

//            service.linkReaderToBook(3,9);
            System.out.println("Список книг читателя с ид=3: " + service.getBooksByReader(3));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
