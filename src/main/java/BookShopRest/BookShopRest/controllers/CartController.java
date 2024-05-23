package BookShopRest.BookShopRest.controllers;

import BookShopRest.BookShopRest.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}
