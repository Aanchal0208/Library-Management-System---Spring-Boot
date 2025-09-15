package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.entity.Books;
import com.entity.Users;
import com.repository.BooksRepository;
import com.service.BooksService;
import com.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	private final BooksRepository booksRepository;

	@Autowired
	private BooksService booksService;
	
	@Autowired
	private UserService userService;

	HomeController(BooksRepository booksRepository) {
		this.booksRepository = booksRepository;
	}
	@ModelAttribute
	public void addUserToModel(Model model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String email = null;

	    if (auth != null && auth.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) auth.getPrincipal();
	        email = userDetails.getUsername();
	    }

	    if (email != null) {
	        Users user = userService.findByEmail(email);
	        if (user != null) {
	            model.addAttribute("user", user);
	        }
	    }
	}

	@GetMapping({"/", "/index"})
	public String index(Model m) {
		long totalbooks = booksService.totalbooks();
	    long totalregisteres = userService.totalregisteres();
	    m.addAttribute("tb", totalbooks);
	    m.addAttribute("totalregisteres", totalregisteres);
	    return "index";
	}

	@GetMapping("/openaddbook")
	public String openaddbook() {
		return "books";
	}

	@PostMapping("/savebook")
	public String savebook(List<Books> book, RedirectAttributes redirectAttributes,
			@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
		String image = imageFile.getOriginalFilename();
		if (imageFile != null && !imageFile.isEmpty()) {
			for (Books book1 : book) {book1.setImageName(image);}
			File savefile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + "books" + File.separator
					+ imageFile.getOriginalFilename());
			Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} else {
			for (Books book1 : book) {book1.setImageName("default.png");}
		}
		List<Books> saveBooks = booksService.saveBooks(book);
		if (!ObjectUtils.isEmpty(saveBooks)) {
			redirectAttributes.addFlashAttribute("succmsg", "Book Successfully Stored.");
		} else {
			redirectAttributes.addFlashAttribute("succmsg", "Book Failed to Stored.");
		}
		return "redirect:/openaddbook";
	}

	@GetMapping("/viewbooks")
	public String viewbooks(Model m) {
		List<Books> getallBooks = booksService.getallBooks();
		m.addAttribute("books", getallBooks);
		return "viewBooks";
	}

	@GetMapping("/deletebook/{id}")
	public String deletebook(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
		boolean deleteBook = booksService.deleteBook(id);
		System.out.println(id);
		if (deleteBook) {
			redirectAttributes.addFlashAttribute("succmsg", "Delete Successfully");
		} else {
			redirectAttributes.addFlashAttribute("errormsg", "Delete Failed");
		}
		return "redirect:/viewbooks";
	}

	@PostMapping("/editBook")
	public String updatebooks(Books book, RedirectAttributes redirectAttributes,
			@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
		String image = imageFile.getOriginalFilename();
		if (imageFile != null && !imageFile.isEmpty()) {
			book.setImageName(image);
			File savefile = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(savefile.getAbsolutePath() + File.separator + "books" + File.separator
					+ imageFile.getOriginalFilename());
			Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} else {
			Books findbyId = booksService.findByBookId(book.getId()).orElse(null);
			if (findbyId != null) {
				book.setImageName(findbyId.getImageName());
	        }
		}
		Books updateBooks = booksService.saveBooks(book);
		 if(!ObjectUtils.isEmpty(updateBooks)) {
		 redirectAttributes.addFlashAttribute("succmsg", "Update Successfully"); 
		 }
		 else
		 { 
			 redirectAttributes.addFlashAttribute("errormsg", "Update Failed"); 
		 }
		 
		return "redirect:/updatebooks/"+book.getId();
	}

	@GetMapping("/updatebooks/{id}")
	public String editbook(@PathVariable("id") long id, Model m) {
		Optional<Books> findbyBookId = booksService.findByBookId(id);
		Books books = findbyBookId.get();
		m.addAttribute("book", books);
		return "updatebooks";
	}
	
	@GetMapping("/search")
	public String openSearchPage(
	        @RequestParam(value = "query", required = false, defaultValue = "") String query,
	        Model m,
	        @PageableDefault(size = 10, sort = "title") Pageable pageable) {

	    Page<Books> resultPage;

	    if (query == null || query.trim().isEmpty()) {
	        List<Books> allBooks = booksService.getallBooks();

	        int start = (int) pageable.getOffset();
	        int end = Math.min((start + pageable.getPageSize()), allBooks.size());

	        List<Books> paginatedList = allBooks.subList(start, end);

	        resultPage = new PageImpl<>(paginatedList, pageable, allBooks.size());
	    } else {
	        resultPage = booksService.getseacrhBooks(query, pageable);
	    }
	    m.addAttribute("countBooks", booksRepository.count());
	    m.addAttribute("books", resultPage.getContent());
	    m.addAttribute("currentPage", resultPage.getNumber());
	    m.addAttribute("totalPages", resultPage.getTotalPages());
	    m.addAttribute("totalItems", resultPage.getTotalElements());
	    m.addAttribute("searchQuery", query);

	    return "searchbooks";
	}

	
	@GetMapping("/books/download")
	public void downloadExcel(HttpServletResponse response) throws IOException {
	    List<Books> allBooks = booksService.getallBooks();

	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Books");

	    // Header row
	    Row header = sheet.createRow(0);
	    header.createCell(0).setCellValue("ID");
	    header.createCell(1).setCellValue("Title");
	    header.createCell(2).setCellValue("Author");
	    header.createCell(3).setCellValue("Price");
	    header.createCell(4).setCellValue("ISBN");
	    header.createCell(5).setCellValue("Description");

	    // Data rows
	    int rowIdx = 1;
	    for (Books book : allBooks) {
	        Row row = sheet.createRow(rowIdx++);
	        row.createCell(0).setCellValue(book.getId());
	        row.createCell(1).setCellValue(book.getTitle());
	        row.createCell(2).setCellValue(book.getAuthor());
	        row.createCell(3).setCellValue(book.getPrice());
	        row.createCell(4).setCellValue(book.getIsbn());
	        row.createCell(5).setCellValue(book.getDescription());
	    }

	    // Set response headers
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=books.xlsx");

	    // Write workbook to response output stream
	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
	
	@GetMapping("/filtersearch")
	public String filterSearch(
	        @RequestParam(value = "title", defaultValue = "") String title,
	        @RequestParam(value = "author", defaultValue = "") String author,
	        @RequestParam(value = "isbn", defaultValue = "") String isbn,
	        @RequestParam(value = "price", defaultValue = "0-999999") String price,
	        @PageableDefault(size = 10, sort = "title") Pageable pageable,
	        Model model) {

	    // Parse price
	    String[] priceRange = price.split("-");
	    int minPrice = Integer.parseInt(priceRange[0]);
	    int maxPrice = Integer.parseInt(priceRange[1]);

	    Page<Books> filteredBooks = booksService.getFilteredBooks(title, author, isbn, minPrice, maxPrice, pageable);

	    // These methods work only on Page<Books>
	    model.addAttribute("books", filteredBooks.getContent());
	    model.addAttribute("currentPage", filteredBooks.getNumber());
	    model.addAttribute("totalPages", filteredBooks.getTotalPages());
	    model.addAttribute("totalItems", filteredBooks.getTotalElements());

	    // Send search params back to view for pagination links
	    model.addAttribute("title", title);
	    model.addAttribute("author", author);
	    model.addAttribute("isbn", isbn);
	    model.addAttribute("price", price);

	    return "/filter_books";
	}






}
