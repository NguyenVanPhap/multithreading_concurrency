package com.example.repository;

import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User repository for testing SQL query visualization
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Simple query methods
    Optional<User> findByEmail(String email);
    
    List<User> findByName(String name);
    
    List<User> findByAge(Integer age);
    
    List<User> findByStatus(User.UserStatus status);
    
    // Complex query methods
    List<User> findByEmailAndStatus(String email, User.UserStatus status);
    
    List<User> findByNameContainingIgnoreCase(String name);
    
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Custom queries
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% AND u.status = :status")
    List<User> findByNameLikeAndStatus(@Param("name") String name, @Param("status") User.UserStatus status);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") User.UserStatus status);
    
    @Query("SELECT u FROM User u WHERE u.age > :minAge ORDER BY u.createdAt DESC")
    List<User> findUsersOlderThan(@Param("minAge") Integer minAge);
    
    // Native query
    @Query(value = "SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge ORDER BY created_at DESC", 
           nativeQuery = true)
    List<User> findUsersByAgeRangeNative(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    // Exists queries
    boolean existsByEmail(String email);
    
    boolean existsByNameAndStatus(String name, User.UserStatus status);
}
