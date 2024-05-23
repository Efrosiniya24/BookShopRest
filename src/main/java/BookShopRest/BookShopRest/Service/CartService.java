package BookShopRest.BookShopRest.Service;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Model.Cart;
import BookShopRest.BookShopRest.Model.User;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import BookShopRest.BookShopRest.repositories.CartRepository;
import BookShopRest.BookShopRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;

    public void addBookToCart(Long bookId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
        }
        Books books = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        cart.getBooks().add(books);
        cart.setTotalPrice(cart.getTotalPrice() + books.getPrice());
        cartRepository.save(cart);
    }

    public void deleteBookFromCart(Long bookId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = user.getCart();
        Books books = booksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        cart.getBooks().remove(books);
        cart.setTotalPrice(cart.getTotalPrice() - books.getPrice());
        cartRepository.save(cart);
    }
}
