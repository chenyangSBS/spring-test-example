package com.example.springtestexample.controller;

import com.example.springtestexample.entity.Book;
import com.example.springtestexample.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        
        testBook = new Book();
        testBook.setTitle("Spring Boot测试指南");
        testBook.setAuthor("测试作者");
        testBook.setPrice(39.99);
        testBook.setStock(100);
        testBook = bookRepository.save(testBook);
    }

    @Test
    void createBook_ShouldCreateNewBook() throws Exception {
        Book newBook = new Book();
        newBook.setTitle("新书");
        newBook.setAuthor("新作者");
        newBook.setPrice(49.99);
        newBook.setStock(50);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("新书"))
                .andExpect(jsonPath("$.author").value("新作者"));
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        mockMvc.perform(get("/api/books/{id}", testBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot测试指南"));
    }

    @Test
    void updateBookStock_ShouldUpdateStock() throws Exception {
        mockMvc.perform(put("/api/books/{id}/stock", testBook.getId())
                .param("quantity", "-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(90));
    }
}