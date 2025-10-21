package com.example.bookreading.controller;

import com.example.bookreading.entity.Book;
import com.example.bookreading.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String q) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Book> books = bookRepository.searchBooks(q.trim());
        return ResponseEntity.ok(books);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long id) {
        List<Book> books = bookRepository.findByCategoryId(id);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findByIdWithCategory(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Note: Add, update, delete endpoints can be added if needed, but not in original Node.js
    // For now, focus on read operations to preserve functionality
}
