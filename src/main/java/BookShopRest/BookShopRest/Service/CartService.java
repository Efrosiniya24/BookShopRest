package BookShopRest.BookShopRest.Service;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Model.Cart;
import BookShopRest.BookShopRest.Model.Order;
import BookShopRest.BookShopRest.Model.User;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import BookShopRest.BookShopRest.repositories.CartRepository;
import BookShopRest.BookShopRest.repositories.OrderRepository;
import BookShopRest.BookShopRest.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BooksRepository booksRepository;
    private final OrderRepository orderRepository;

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


    @Transactional
    public Order placeOrder(String userEmail, List<Long> bookIds) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Cart cart = user.getCart();
        if (cart == null || cart.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Set<Books> booksToOrder = new HashSet<>();
        for (Long bookId : bookIds) {
            Books book = cart.getBooks().stream()
                    .filter(b -> b.getId().equals(bookId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Book with ID " + bookId + " not found in cart"));
            booksToOrder.add(book);
        }

        Order order = new Order();
        order.setUser(user);
        order.setCart(cart);
        order.setBooks(booksToOrder);
        order.setTotalPrice(booksToOrder.stream().mapToDouble(Books::getPrice).sum());
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);

        cart.getBooks().removeAll(booksToOrder);
        cart.setTotalPrice(cart.getBooks().stream().mapToDouble(Books::getPrice).sum());
        cartRepository.save(cart);

        return order;
    }
}
