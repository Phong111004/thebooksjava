package com.example.bookreading.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookID")
    private Long bookId;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Author")
    private String author;

    @Column(name = "CategoryID")
    private Long categoryId;

    @Column(name = "ImageUrl")
    private String imageUrl;

    @Column(name = "PdfPath")
    private String pdfPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", insertable = false, updatable = false)
    private Category category;

    // Constructors
    public Book() {}

    public Book(String title, String author, Long categoryId, String imageUrl) {
        this.title = title;
        this.author = author;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}
