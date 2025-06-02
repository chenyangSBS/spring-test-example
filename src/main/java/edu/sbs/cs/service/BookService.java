package edu.sbs.cs.service;


import edu.sbs.cs.entity.Book;
import edu.sbs.cs.exception.BookNotFoundException;
import edu.sbs.cs.model.PublisherTree;
import edu.sbs.cs.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<String> getAllAuthors() {
        return bookRepository.findDistinctAuthors();
    }

    public List<PublisherTree> getPublisherAuthorTree() {
        List<PublisherTree> tree = new ArrayList<>();

        // 获取所有出版商
        List<String> publishers = bookRepository.findAllPublishers();

        // 为每个出版商查找对应的作者
        for (String publisher : publishers) {
            List<String> authors = bookRepository.findAuthorsByPublisher(publisher);
            PublisherTree node = new PublisherTree(publisher);
            node.setAuthors(authors);
            tree.add(node);
        }

        return tree;
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthor(author);
    }
}