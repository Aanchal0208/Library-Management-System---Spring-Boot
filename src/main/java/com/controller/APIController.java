package com.controller;

import com.entity.Books;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.BooksService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class APIController {

    @Autowired
    private BooksService booksService;

    // ✅ 1. Add new book (POST)
    @PostMapping("/add")
    public ResponseEntity<List<Books>> addBook(@RequestBody List<Books> book) {
        System.out.println("Received books: " + book); // log
        List<Books> savedBooks = booksService.saveBooks(book);
        return ResponseEntity.ok(savedBooks);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBooks(@RequestParam("booksJson") String booksJson,
                                      @RequestParam("imageFile") MultipartFile[] imageFiles) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Books> bookList = objectMapper.readValue(booksJson, new TypeReference<List<Books>>() {});
            
            if (imageFiles != null && imageFiles.length == bookList.size()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                for (int i = 0; i < bookList.size(); i++) {
                    MultipartFile imageFile = imageFiles[i];
                    String imageName = imageFile.getOriginalFilename();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "books" + File.separator + imageName);
                    Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    bookList.get(i).setImageName(imageName);
                }
            } else {
                // Set default image if files not provided
                for (Books book : bookList) {
                    book.setImageName("default.png");
                }
            }

            List<Books> savedBooks = booksService.saveBooks(bookList);
            return ResponseEntity.ok(savedBooks);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



    // ✅ 2. Update existing book (PUT)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @RequestBody Books updatedBook) {
        Optional<Books> existingBookOpt = booksService.findByBookId(id);
        if (existingBookOpt.isEmpty()) {
            System.out.println("Book not found: " + id);
            return ResponseEntity.notFound().build();
        }

        Books existingBook = existingBookOpt.get();
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setDescription(updatedBook.getDescription());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setImageName(updatedBook.getImageName());

        Books saved = booksService.saveBooks(existingBook);
        System.out.println("Book updated: " + saved.getTitle());
        return ResponseEntity.ok(saved);
    }

    // ✅ 3. Get all books (GET)
    @GetMapping("/all")
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getallBooks());
    }

    // ✅ 4. Get book by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") Long id) {
        Optional<Books> book = booksService.findByBookId(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ 5. Delete book (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        boolean deleted = booksService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok("Book deleted successfully.");
        } else {
            return ResponseEntity.status(500).body("Book deletion failed.");
        }
    }
}
