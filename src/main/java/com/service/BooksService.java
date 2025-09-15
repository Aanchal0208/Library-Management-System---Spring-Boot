package com.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import com.entity.Books;

@Service
public interface BooksService {

	public List<Books> saveBooks(List<Books> book);
	
	public Books saveBooks(Books book);
	
	public List<Books> getallBooks();
	
	public Optional<Books> findByBookId(long id);
	
	public boolean updateBooks(long id);
	
	public boolean deleteBook(long id);
	
	public long totalbooks();
	
	public Page<Books> getseacrhBooks(String query, Pageable pageable);
	
	public Page<Books> getFilteredBooks(String title, String author, String isbn, int minPrice, int maxPrice, Pageable pageable);
}
