package com.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.Books;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
	
long count();

public Page<Books> findByTitleContainingIgnoreCase(String query, Pageable pageable);

@Query("SELECT b FROM Books b WHERE " +
        "LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) AND " +
        "LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')) AND " +
        "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :isbn, '%')) AND " +
        "b.price BETWEEN :minPrice AND :maxPrice")
 Page<Books> filterBooks(@Param("title") String title,
                         @Param("author") String author,
                         @Param("isbn") String isbn,
                         @Param("minPrice") int minPrice,
                         @Param("maxPrice") int maxPrice,
                         Pageable pageable);




}
