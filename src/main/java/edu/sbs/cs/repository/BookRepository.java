package edu.sbs.cs.repository;

import edu.sbs.cs.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByAuthor(String author);

    @Query("SELECT DISTINCT b.author FROM Book b")
    List<String> findDistinctAuthors(); // 正确写法

    @Query("SELECT DISTINCT b.publisher FROM Book b WHERE b.publisher IS NOT NULL")
    List<String> findAllPublishers();

    @Query("SELECT DISTINCT b.author FROM Book b WHERE b.publisher = :publisher")
    List<String> findAuthorsByPublisher(String publisher);

    List<Book> findAllByAuthor(String author);
}