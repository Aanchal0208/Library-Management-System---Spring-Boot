📚 Library Management System

This is a simple Library Management System project with basic security features, book management, searching, filtering, and REST API support.

-------------------------------------------------------

🚀 Features
🔐 User Security

User Register and Login

On Register, an email is sent to the backend for confirmation

Basic session security for authenticated access

📖 Book Management

Add new books

Update existing books

Delete books

View book details

🔎 Search & Filter

Search books by index

Filter books by title, author, category, etc.

Pagination for easier browsing of large book lists

🌐 API Support

REST API available to use all features

Can be integrated with frontend apps or mobile apps

--------------------------------------------------------

📌 API Examples
Method	Endpoint	Description
POST	/api/auth/register	Register new user (email goes to backend)
POST	/api/auth/login	Login user
GET	/api/books	Get all books (with pagination)
POST	/api/books	Add new book
PUT	/api/books/{id}	Update book
DELETE	/api/books/{id}	Delete book
GET	/api/books/search	Search / filter books

---------------------------------------------------------

🛠️ Tech Stack

Backend: Spring Boot (Java)

Frontend: HTML, CSS

Database: MySQL

API: REST API with Spring Boot

Version Control: Git & GitHub
