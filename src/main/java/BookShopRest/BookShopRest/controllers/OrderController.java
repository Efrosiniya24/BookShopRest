package BookShopRest.BookShopRest.controllers;

import BookShopRest.BookShopRest.Model.Order;
import BookShopRest.BookShopRest.Service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/BookShop/order")
public class OrderController {

    private final CartService cartService;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> placeOrder(@RequestBody Map<String, List<Long>> request, Authentication authentication) {
        List<Long> bookIds = request.get("bookIds");
        Order order = cartService.placeOrder(authentication.getName(), bookIds);
        return ResponseEntity.ok(order);
    }
}
