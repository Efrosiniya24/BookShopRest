package BookShopRest.BookShopRest.controllers;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BookShop/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addBookToCart(@RequestBody Map<String, Long> request, Authentication authentication) {
        cartService.addBookToCart(request.get("bookId"), authentication.getName());
        return ResponseEntity.ok("Book added to cart");
    }

    @DeleteMapping("/deleteFromCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteFromCart(@RequestBody Map<String, Long> request, Authentication authentication) {
        cartService.deleteBookFromCart(request.get("bookId"), authentication.getName());
        return ResponseEntity.ok("Book deleted from cart");
    }

    @GetMapping("/viewCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Set<Books>> viewCart(Authentication authentication) {
        Set<Books> booksInCart = cartService.getBooksInCart(authentication.getName());
        return ResponseEntity.ok(booksInCart);
    }

    @GetMapping("/viewBookInCart")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Books> viewBookInCart(@RequestParam String bookName, Authentication authentication) {
        Books book = cartService.getBookInCartByName(bookName, authentication.getName());
        return ResponseEntity.ok(book);
    }
}
