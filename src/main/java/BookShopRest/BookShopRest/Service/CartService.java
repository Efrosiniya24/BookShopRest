package BookShopRest.BookShopRest.Service;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Model.Cart;
import BookShopRest.BookShopRest.Model.User;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import BookShopRest.BookShopRest.repositories.CartRepository;
import BookShopRest.BookShopRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;

    @Transactional
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

    @Transactional
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

    public Set<Books> getBooksInCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<Books> books = user.getCart().getBooks();
        if (books == null) {
            throw new IllegalArgumentException("Cart is empty");
        }
        log.info("Books in cart: {}", books);
        return books;
    }

    public Books getBookInCartByName(String userEmail, String bookName) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = user.getCart();
        if (cart == null) {
            throw new IllegalArgumentException("Cart is empty");
        }
        return cart.getBooks().stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found in cart"));
    }

}
