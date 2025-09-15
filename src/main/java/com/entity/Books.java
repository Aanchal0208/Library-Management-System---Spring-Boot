package com.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books", indexes = {
	    @Index(name = "idx_title", columnList = "title"),
	    @Index(name = "idx_author", columnList = "author"),
	    @Index(name = "idx_isbn", columnList = "isbn"),
	    @Index(name = "idx_price", columnList = "price")
})
public class Books {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long id;
private String title;
private String description;
private String author;
private int price;
private String isbn;
private String imageName;
public Books() {
	super();
	// TODO Auto-generated constructor stub
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public String getIsbn() {
	return isbn;
}
public void setIsbn(String isbn) {
	this.isbn = isbn;
}
public String getImageName() {
	return imageName;
}
public void setImageName(String imageName) {
	this.imageName = imageName;
}

}
