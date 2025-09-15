package com.serviceimpl;

import java.util.List;
import java.util.Optional;

import com.entity.Books;
import com.repository.BooksRepository;
import com.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	private BooksRepository booksRepository;
	
	@Override
	public List<Books> saveBooks(List<Books> books) {
	    return booksRepository.saveAll(books);
	}

	@Override
	public List<Books> getallBooks() {
		return booksRepository.findAll();
	}

	@Override
	public Optional<Books> findByBookId(long id) {
		return booksRepository.findById(id);
	}

	@Override
	public boolean updateBooks(long id) {
		Optional<Books> findbyId = booksRepository.findById(id);
		Books books = findbyId.get();
		if(!ObjectUtils.isEmpty(books)) {
			booksRepository.save(books);
			return true;
		}
		return false;
	}

	@Transactional
	@Override
	public boolean deleteBook(long id) {
		Optional<Books> findbyId = booksRepository.findById(id);
		if(findbyId.isPresent()) {
			Books books = findbyId.get();
			booksRepository.delete(books);
			return true;
		}
		return false;
	}

	@Override
	public long totalbooks() {
		return booksRepository.count();
	}

	@Override
	public Page<Books> getseacrhBooks(String query, Pageable pageable) {
		return booksRepository.findByTitleContainingIgnoreCase(query, pageable);
	}

	@Override
	public Books saveBooks(Books book) {
		return booksRepository.save(book);
	}

	@Override
	public Page<Books> getFilteredBooks(String title, String author, String isbn, int minPrice, int maxPrice, Pageable pageable) {
	    return booksRepository.filterBooks(title, author, isbn, minPrice, maxPrice, pageable);
	}


}
