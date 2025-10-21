package com.example.bookreading.controller;

import com.example.bookreading.entity.ReadingHistory;
import com.example.bookreading.entity.User;
import com.example.bookreading.repository.ReadingHistoryRepository;
import com.example.bookreading.repository.UserRepository;
import com.example.bookreading.service.UdpBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadingHistoryRepository readingHistoryRepository;

    @Autowired
    private UdpBroadcastService udpBroadcastService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");

        if (username == null || email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username, email, and password are required"));
        }

        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            return ResponseEntity.status(409).body(Map.of("error", "Username or email already exists"));
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword, "customer");
        User savedUser = userRepository.save(user);

        udpBroadcastService.broadcastMessage("New user registered: " + username);

        return ResponseEntity.status(201).body(Map.of(
            "message", "User created successfully",
            "user", Map.of(
                "id", savedUser.getUserId(),
                "username", savedUser.getUsername(),
                "email", savedUser.getEmail(),
                "role", savedUser.getRole()
            )
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("username"); // Note: using 'username' field for email as per Node.js
        String password = loginData.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.ok(Map.of(
            "message", "Login successful",
            "user", Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");

        if (username == null || email == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and email are required"));
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<ReadingHistory>> getReadingHistory(@PathVariable Long userId) {
        List<ReadingHistory> history = readingHistoryRepository.findByUserIdOrderByLastReadAtDesc(userId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{userId}/history/{bookId}")
    public ResponseEntity<?> getReadingHistoryForBook(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<ReadingHistory> historyOpt = readingHistoryRepository.findByUserIdAndBookId(userId, bookId);
        return historyOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}/history/{bookId}")
    public ResponseEntity<?> addToHistory(@PathVariable Long userId, @PathVariable Long bookId) {
        // Find or Create logic: If it doesn't exist, create it.
        if (readingHistoryRepository.existsByUserIdAndBookId(userId, bookId)) {
            // If it already exists, just return OK. The action is idempotent.
            return ResponseEntity.ok(Map.of("message", "Book is already in history."));
        }

        ReadingHistory history = new ReadingHistory(userId, bookId, BigDecimal.ZERO, 0);
        readingHistoryRepository.save(history);
        return ResponseEntity.status(201).body(Map.of("message", "Book added to history."));
    }

    @DeleteMapping("/{userId}/history/{bookId}")
    public ResponseEntity<?> removeFromHistory(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<ReadingHistory> history = readingHistoryRepository.findByUserIdAndBookId(userId, bookId);
        // Nếu tìm thấy, xóa nó đi. Nếu không, không làm gì cả.
        if (history.isPresent()) {
            readingHistoryRepository.delete(history.get());
        }
        return ResponseEntity.ok(Map.of("message", "Book removed from history successfully"));
    }
}
