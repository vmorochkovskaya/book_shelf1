package org.example.web.controller;

import org.apache.log4j.Logger;
import org.example.app.entity.book.Book;
import org.example.app.service.book.BookService;
import org.example.web.dto.BooksPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private final BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String books() {
        logger.info("got book shelf");
        return "index";
    }

    @GetMapping("/books/recent-view")
    public String booksRecent() {
        logger.info("got book shelf");
        return "books/recent";
    }

    @GetMapping("/books/author")
    public String booksOfAuthor() {
        return "books/author";
    }

    @GetMapping("/books/popular-view")
    public String booksPopular(){
        return "books/popular";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getBooksRecommendedPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/books/recent")
    @ResponseBody
    public BooksPageDto getBooksRecentPage(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit,
                                           @RequestParam(value = "from", required = false) String from,
                                           @RequestParam(value = "to", required = false) String to) {
        return new BooksPageDto(bookService.getPageOfRecentBooksSortedByPubBaseDesc(offset, limit, from, to).getContent());
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getBooksPopular2Page(@RequestParam("offset") Integer offset,
                                             @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/postponed")
    public String postponed(){
        return "postponed";
    }

    @GetMapping("/search")
    public String search(){
        return "search/index";
    }

    @ModelAttribute("bookList")
    public List<Book> bookList(){
        return bookService.getAllBooks();
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 6).getContent();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("popularBooksSeparatePage")
    public List<Book> popularBooksSeparatePage() {
        return bookService.getPageOfPopularBooks(0, 20).getContent();
    }

    @ModelAttribute("recentBooksSeparatePage")
    public List<Book> recentBooksSeparatePage() {
        return bookService.getPageOfRecentBooksSortedByPubBaseDesc(0, 20).getContent();
    }
}
