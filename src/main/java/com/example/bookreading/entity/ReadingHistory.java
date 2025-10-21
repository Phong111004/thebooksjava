package com.example.bookreading.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ReadingHistory")
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BorrowID")
    private Long id;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "BookID", nullable = false)
    private Long bookId;

    @Column(name = "Progress", precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;

    @Column(name = "LastPage")
    private Integer lastPage = 0;

    @Column(name = "LastReadAt")
    private LocalDateTime lastReadAt = LocalDateTime.now();

    // ✅ THÊM @JsonIgnore VÀO 2 DÒNG NÀY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    @JsonIgnore  // ← THÊM DÒNG NÀY
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookID", insertable = false, updatable = false)
    @JsonIgnore  // ← THÊM DÒNG NÀY
    private Book book;

    // Constructors
    public ReadingHistory() {}

    public ReadingHistory(Long userId, Long bookId, BigDecimal progress, Integer lastPage) {
        this.userId = userId;
        this.bookId = bookId;
        this.progress = progress;
        this.lastPage = lastPage;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public BigDecimal getProgress() { return progress; }
    public void setProgress(BigDecimal progress) { this.progress = progress; }

    public Integer getLastPage() { return lastPage; }
    public void setLastPage(Integer lastPage) { this.lastPage = lastPage; }

    public LocalDateTime getLastReadAt() { return lastReadAt; }
    public void setLastReadAt(LocalDateTime lastReadAt) { this.lastReadAt = lastReadAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}