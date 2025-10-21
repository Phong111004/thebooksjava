package com.example.bookreading.repository;

import com.example.bookreading.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category")
    @Override
    @NonNull
    List<Book> findAll();

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.category.categoryId = :categoryId")
    List<Book> findByCategoryId(Long categoryId);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.title LIKE %:query% OR b.author LIKE %:query%")
    List<Book> searchBooks(@Param("query") String query);

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.category WHERE b.bookId = :id")
    Optional<Book> findByIdWithCategory(@Param("id") Long id);
}
