package edu.sbs.cs.controller;


import edu.sbs.cs.entity.Book;
import edu.sbs.cs.model.AuthorInfo;
import edu.sbs.cs.model.PublisherTree;
import edu.sbs.cs.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Book> updateBookStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(bookService.updateBookStock(id, quantity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/authors")
    public ResponseEntity<List<String>> getAllAuthors() {
        return ResponseEntity.ok(bookService.getAllAuthors());
    }

    @PostMapping("/author/books")
    public ResponseEntity<List<Book>> getBooksByAuthor(@RequestBody AuthorInfo author) {
        System.out.println(author);
        return ResponseEntity.ok(bookService.getBooksByAuthor(author.getAuthor()));
    }

    @PostMapping("/publisher-tree")
    public ResponseEntity<List<PublisherTree>> getPublisherTree() {
        return ResponseEntity.ok(bookService.getPublisherAuthorTree());
    }
}