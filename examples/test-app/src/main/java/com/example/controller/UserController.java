package com.example.controller;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for testing SQL query visualization
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get all users - tests simple SELECT query
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user by ID - tests findById query
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get user by email - tests findByEmail query
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get users by name - tests findByName query
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getUsersByName(@PathVariable String name) {
        List<User> users = userRepository.findByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by age - tests findByAge query
     */
    @GetMapping("/age/{age}")
    public ResponseEntity<List<User>> getUsersByAge(@PathVariable Integer age) {
        List<User> users = userRepository.findByAge(age);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by status - tests findByStatus query
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable User.UserStatus status) {
        List<User> users = userRepository.findByStatus(status);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by email and status - tests complex query
     */
    @GetMapping("/email/{email}/status/{status}")
    public ResponseEntity<List<User>> getUsersByEmailAndStatus(
            @PathVariable String email, 
            @PathVariable User.UserStatus status) {
        List<User> users = userRepository.findByEmailAndStatus(email, status);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Search users by name containing - tests LIKE query
     */
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<User>> searchUsersByName(@PathVariable String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users by age range - tests BETWEEN query
     */
    @GetMapping("/age/between/{minAge}/{maxAge}")
    public ResponseEntity<List<User>> getUsersByAgeRange(
            @PathVariable Integer minAge, 
            @PathVariable Integer maxAge) {
        List<User> users = userRepository.findByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get users created between dates - tests date range query
     */
    @GetMapping("/created/between")
    public ResponseEntity<List<User>> getUsersCreatedBetween(
            @RequestParam String startDate, 
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        List<User> users = userRepository.findByCreatedAtBetween(start, end);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Create new user - tests INSERT query
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
    
    /**
     * Update user - tests UPDATE query
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }
    
    /**
     * Delete user - tests DELETE query
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Test custom JPQL query
     */
    @GetMapping("/custom/search")
    public ResponseEntity<List<User>> searchUsersCustom(
            @RequestParam String name, 
            @RequestParam User.UserStatus status) {
        List<User> users = userRepository.findByNameLikeAndStatus(name, status);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Test count query
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countUsersByStatus(@PathVariable User.UserStatus status) {
        Long count = userRepository.countByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Test native query
     */
    @GetMapping("/native/age/{minAge}/{maxAge}")
    public ResponseEntity<List<User>> getUsersByAgeRangeNative(
            @PathVariable Integer minAge, 
            @PathVariable Integer maxAge) {
        List<User> users = userRepository.findUsersByAgeRangeNative(minAge, maxAge);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Test exists queries
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> userExistsByEmail(@PathVariable String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Test complex exists query
     */
    @GetMapping("/exists/name/{name}/status/{status}")
    public ResponseEntity<Boolean> userExistsByNameAndStatus(
            @PathVariable String name, 
            @PathVariable User.UserStatus status) {
        boolean exists = userRepository.existsByNameAndStatus(name, status);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Test multiple queries in single request
     */
    @GetMapping("/test/multiple")
    public ResponseEntity<String> testMultipleQueries() {
        // Execute multiple queries to test performance monitoring
        long totalUsers = userRepository.count();
        List<User> activeUsers = userRepository.findByStatus(User.UserStatus.ACTIVE);
        List<User> recentUsers = userRepository.findByCreatedAtBetween(
            LocalDateTime.now().minusDays(7), 
            LocalDateTime.now()
        );
        
        return ResponseEntity.ok(String.format(
            "Total users: %d, Active users: %d, Recent users: %d", 
            totalUsers, activeUsers.size(), recentUsers.size()
        ));
    }
}
