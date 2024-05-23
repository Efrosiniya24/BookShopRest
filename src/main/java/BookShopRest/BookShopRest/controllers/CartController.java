package BookShopRest.BookShopRest.controllers;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Model.Cart;
import BookShopRest.BookShopRest.Model.User;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import BookShopRest.BookShopRest.repositories.CartRepository;
import BookShopRest.BookShopRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BookShop/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;

    @PostMapping("/addToCart")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> addBookToCart(@RequestBody Map<String, Long> request, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();
        Cart cart = user.getCart();
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }
        Books books = booksRepository.findById(request.get("bookId"))
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        cart.getBooks().add(books);
        cart.setTotalPrice(cart.getTotalPrice() + books.getPrice());
        cartRepository.save(cart);

        return ResponseEntity.ok("Book added to cart");
    }
}
//    @PostMapping("/addToCart")
//    public void addToCart(@RequestParam Book book, @RequestParam Cart cart){
//        log.info("Adding a book to the cart: " + cartRepository.save(cart));
//    }
//
//

