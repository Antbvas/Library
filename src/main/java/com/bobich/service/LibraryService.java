package com.bobich.service;

import com.bobich.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    // Метод для добавления нового автора
    public void addAuthor(String firstName, String lastName) throws SQLException {
        String sql = "INSERT INTO authors (first_name, last_name) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.executeUpdate();
        }
    }

    //    Написать метод для добавления новой книги в таблицу books
    public void addBook(String title, int authorId, int year) throws SQLException {
        String sql = "INSERT INTO books (title, author_id, published_year) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, authorId);
            ps.setInt(3, year);
            ps.executeUpdate();
        }
    }

    //    Написать метод для добавления нового читателя в таблицу readers
    public void addReader(String firstName, String lastName, String email) throws SQLException {
        String sql = "INSERT INTO readers (first_name, last_name, email) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.executeUpdate();
        }
    }

    //   Написать метод для извлечения всех книг из таблицы books
    public List<String> getAllBooks() throws SQLException {
        List<String> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(rs.getString("title"));
            }
        }
        return books;
    }

    //    Написать метод для извлечения всех читателей из таблицы readers
    public List<String> getAllReaders() throws SQLException {
        List<String> readers = new ArrayList<>();
        String sql = "SELECT * FROM readers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                readers.add(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }
        return readers;
    }

    //    Написать метод для извлечения всех авторов из таблицы authors
    public List<String> getAllAuthors() throws SQLException {
        List<String> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                authors.add(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        }
        return authors;
    }

    //    Написать метод для обновления информации о книге по её id
    public void updateBook(int id, String title, int authorId, int year) throws SQLException {
        String sql = "UPDATE books SET title = ?, author_id = ?, published_year = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, authorId);
            ps.setInt(3, year);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    //    Написать метод для удаления книги по её id
    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    //    Написать метод, который добавляет книгу и автора в одну транзакцию, в случае ошибки откатить изменения
    public void addAuthorAndBook(String firstName, String lastName, String title, int year) throws SQLException {
        String findAuthor = "SELECT id FROM authors WHERE first_name = ? AND last_name = ?";
        String insertAuthor = "INSERT INTO authors (first_name, last_name) VALUES (?,?) RETURNING id";
        String insertBook = "INSERT INTO books (title,author_id, published_year) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psFind = conn.prepareStatement(findAuthor);
                 PreparedStatement psInsertAuthor = conn.prepareStatement(insertAuthor);
                 PreparedStatement psInsertBooks = conn.prepareStatement(insertBook)) {

                int authorID;
                //Ищем автора
                psFind.setString(1, firstName);
                psFind.setString(2, lastName);
                ResultSet rs = psFind.executeQuery();


                if (rs.next()) {
                    authorID = rs.getInt("id");
                } else {
                    //Добавляем нового автора
                    psInsertAuthor.setString(1, firstName);
                    psInsertAuthor.setString(2, lastName);
                    ResultSet authorResult = psInsertAuthor.executeQuery();
                    authorResult.next();
                    authorID = authorResult.getInt("id");
                }

                // Добавляем книгу
                psInsertBooks.setString(1, title);
                psInsertBooks.setInt(2, authorID);
                psInsertBooks.setInt(3, year);
                psInsertBooks.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    //    Написать метод для поиска книг по названию или автору
    public List<String> searchBooks(String keyword) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT b.title FROM books b " +
                "JOIN authors a ON b.author_id = a.id " +
                "WHERE b.title  ILIKE ? OR a.first_name ILIKE ? OR a.last_name ILIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(rs.getString("title"));
            }
            return result;
        }
    }

    // добавление связи между читателем и книгой
    public void linkReaderToBook(int readerId, int bookId) throws SQLException {
        String sql = "INSERT INTO readers_books(reader_id, book_id) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    // Список книг читателя
    public List<String> getBooksByReader(int readerId) throws SQLException {
        List<String> books = new ArrayList<>();
        String sql = "SELECT b.title FROM books b " +
                "JOIN readers_books rb ON b.id=rb.book_id " +
                "WHERE rb.reader_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(rs.getString("title"));
            }
        }
        return books;
    }
}

