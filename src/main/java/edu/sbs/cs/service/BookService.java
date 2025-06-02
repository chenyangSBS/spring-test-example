package com.example.springtestexample.service;

import com.example.springtestexample.entity.Book;
import com.example.springtestexample.exception.BookNotFoundException;
import com.example.springtestexample.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("未找到ID为 " + id + " 的图书"));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBookStock(Long id, Integer quantity) {
        Book book = getBookById(id);
        if (book.getStock() + quantity < 0) {
            throw new IllegalArgumentException("库存不足");
        }
        book.setStock(book.getStock() + quantity);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("未找到ID为 " + id + " 的图书");
        }
        bookRepository.deleteById(id);
    }
}