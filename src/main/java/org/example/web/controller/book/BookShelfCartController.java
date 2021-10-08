package org.example.web.controller.book;

import org.example.app.entity.book.Book;
import org.example.app.entity.enums.Book2UserTypeEnum;
import org.example.app.service.book.BookService;
import org.example.app.service.book.BooksRatingAndPopularityService;
import org.example.app.service.book.CartService;
import org.example.app.service.book.PostponedService;
import org.example.app.service.cookie.CookieType;
import org.example.app.service.cookie.ICookieService;
import org.example.web.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/books")
public class BookShelfCartController {

    private final BookService bookService;
    private final ICookieService cookieService;
    private final CartService cartService;
    private final PostponedService postponedService;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;

    @Autowired
    public BookShelfCartController(BookService bookService, ICookieService cookieService, CartService cartService,
                                   PostponedService postponedService, BooksRatingAndPopularityService booksRatingAndPopularityService) {
        this.bookService = bookService;
        this.cookieService = cookieService;
        this.cartService = cartService;
        this.postponedService = postponedService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    }

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @ModelAttribute(name = "booksPostponed")
    public List<Book> booksPostponed() {
        return new ArrayList<>();
    }

    @ResponseBody
    @PostMapping("/changeBookStatus/{slug}")
    public void saveNewBookImage(@PathVariable("slug") String slug, @RequestParam("status") Book2UserTypeEnum status,
                                 @CookieValue(name = "cartContents", required = false) String cartContents,
                                 @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                 @RequestParam("booksIds") String bookIds,
                                 HttpServletResponse response, Model model) {
        switch (status) {
            case CART:
                System.out.println("CART");
                cartService.addCartCookie(cartContents, bookIds, response, model);
                break;
            case KEPT:
                System.out.println("KEPT");
                postponedService.addCartCookie(postponedContents, slug, response, model);
                postponedService.incrementNumberUsersBookPostponed(postponedContents, slug);
                break;

            default: throw new RuntimeException("Wrong action is provided!");
        }
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        boolean isCookieContentEmpty = cookieService.isCookieContentEmpty(cartContents);
        model.addAttribute("isCartEmpty", isCookieContentEmpty);
        if (!isCookieContentEmpty) {
            String[] cookieSlugs = cookieService.getBooksSlugsFromCookie(cartContents);
            List<Book> booksFromCookieSlugs = bookService.getBooksBySlugIn(cookieSlugs);
            List<BookDto> books = booksFromCookieSlugs.stream().map(book ->
                    new BookDto(book.getAuthors(),
                            book.getTitle(), book.getDiscount(), book.getPrice(),
                            booksRatingAndPopularityService.countBookRating(book.getSlug()),
                            book.getImage(), book.getSlug())).collect(Collectors.toList());
            model.addAttribute("bookCart", books);
        }
        return "cart";
    }

    @ResponseBody
    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug, @CookieValue(name =
            "cartContents", required = false) String cartContents, HttpServletResponse response) {
        cookieService.removeBookFromCookie(CookieType.CART, cartContents, slug, response);
        return "redirect:/books/cart";
    }

    @ResponseBody
    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug, @CookieValue(name =
            "postponedContents", required = false) String postponedContents, HttpServletResponse response) {
        cookieService.removeBookFromCookie(CookieType.POSTPONED, postponedContents, slug, response);
        return "redirect:/books/postponed";
    }

    @GetMapping("/postponed")
    public String handlePostponedRequest(@CookieValue(value = "postponedContents", required = false) String postponedContents,
                                         Model model) {
        boolean isCookieContentEmpty = cookieService.isCookieContentEmpty(postponedContents);
        model.addAttribute("isPostponedEmpty", isCookieContentEmpty);
        if (!isCookieContentEmpty) {
            String[] cookieSlugs = cookieService.getBooksSlugsFromCookie(postponedContents);
            List<Book> booksFromCookieSlugs = bookService.getBooksBySlugIn(cookieSlugs);
            model.addAttribute("booksPostponed", booksFromCookieSlugs);
            model.addAttribute("slugsPostponed", String.join(", ", cookieSlugs));
        }
        return "postponed";
    }
}
