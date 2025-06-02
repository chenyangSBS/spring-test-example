package edu.sbs.cs.service;

import edu.sbs.cs.entity.Book;
import edu.sbs.cs.exception.BookNotFoundException;
import edu.sbs.cs.repository.BookRepository;
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
//        when(...).thenReturn(...)：Mockito 的 API，用于 stub（模拟）bookRepository.findById(1L)的行为，使其返回一个包含testBook的Optional对象。

        Book found = bookService.getBookById(1L);
//        调用BookService的getBookById方法，传入 ID 1L，并将结果存储在found变量中。

        assertNotNull(found);
//        assertNotNull(found)：确保返回值不为null（即服务正确处理了存在的 ID）。
//        assertNull(found); // 需要显式断言返回null

        assertEquals("测试图书", found.getTitle());
//        assertEquals("测试图书", found.getTitle())：验证返回的书籍标题与testBook的标题一致，确保返回的是正确的对象。

        verify(bookRepository).findById(1L);
//        verify(...)：Mockito 的 API，用于验证bookRepository.findById(1L)方法是否被调用 ** exactly once（恰好一次）**。这确保了BookService正确地委托查询操作给 Repository。

    }

    @Test
    void getBookById_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });
        // 断言调用方法时抛出异常（假设服务在ID不存在时抛出异常）
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