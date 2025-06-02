package com.example.springtestexample.service;

import com.example.springtestexample.entity.Book;
import com.example.springtestexample.exception.BookNotFoundException;
import com.example.springtestexample.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("测试图书");
        testBook.setAuthor("测试作者");
        testBook.setPrice(29.99);
        testBook.setStock(10);
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book found = bookService.getBookById(1L);

        assertNotNull(found);
        assertEquals("测试图书", found.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
    }

    @Test
    void updateBookStock_ShouldUpdateStock_WhenQuantityIsValid() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book updated = bookService.updateBookStock(1L, 5);

        assertEquals(15, updated.getStock());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBookStock_ShouldThrowException_WhenInsufficientStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBookStock(1L, -15);
        });
    }
}