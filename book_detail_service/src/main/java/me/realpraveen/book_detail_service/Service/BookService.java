package me.realpraveen.book_detail_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.realpraveen.book_detail_service.Model.Book;
import me.realpraveen.book_detail_service.Repository.BookRepository;

@Service
public class BookService {

	private BookRepository bookRepo;

	@Autowired
	public BookService(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}

	public void saveBook(Book book) {
		if (doesBookExist(book.getBookId()))
			return;
		bookRepo.save(book);
	}

	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}

	public Book getBookById(Long id) {
		return bookRepo.findById(id).orElse(null);
	}

	public List<Book> getAllBooksOfUser(Long userId) {
		return bookRepo.findByUserId(userId);
	}

	private boolean doesBookExist(Long id) {
		return bookRepo.existsById(id);
	}

}
