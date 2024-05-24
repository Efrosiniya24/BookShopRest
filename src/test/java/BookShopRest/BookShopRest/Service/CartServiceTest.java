package BookShopRest.BookShopRest.Service;

import BookShopRest.BookShopRest.Model.Books;
import BookShopRest.BookShopRest.Model.Cart;
import BookShopRest.BookShopRest.Model.User;
import BookShopRest.BookShopRest.repositories.BooksRepository;
import BookShopRest.BookShopRest.repositories.CartRepository;
import BookShopRest.BookShopRest.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BooksRepository booksRepository;

    @BeforeEach
    void setUp() {
        // Не нужно для JUnit5 с MockitoExtension
    }

    @Test
    void addBookToCart_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.addBookToCart(1L, "user@example.com"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userRepository, cartRepository, booksRepository);
    }

    @Test
    void addBookToCart_BookNotFound() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(booksRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.addBookToCart(1L, "user@example.com"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(booksRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository, cartRepository, booksRepository);
    }

    @Test
    void addBookToCart_Success() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Books book = new Books();
        book.setPrice(29.99);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(booksRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addBookToCart(1L, "user@example.com");

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(booksRepository, times(1)).findById(anyLong());
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertTrue(cart.getBooks().contains(book));
    }

    @Test
    void deleteBookFromCart_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.deleteBookFromCart(1L, "user@example.com"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verifyNoMoreInteractions(userRepository, cartRepository, booksRepository);
    }

    @Test
    void deleteBookFromCart_BookNotFound() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(booksRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cartService.deleteBookFromCart(1L, "user@example.com"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(booksRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(userRepository, cartRepository, booksRepository);
    }

    @Test
    void deleteBookFromCart_Success() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Books book = new Books();
        book.setPrice(29.99);
        cart.getBooks().add(book);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(booksRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.deleteBookFromCart(1L, "user@example.com");

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(booksRepository, times(1)).findById(anyLong());
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertTrue(!cart.getBooks().contains(book));
    }

    @Test
    void getBooksInCart_Success() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Books book = new Books();
        book.setName("Test Book");
        cart.getBooks().add(book);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Set<Books> booksInCart = cartService.getBooksInCart("user@example.com");

        verify(userRepository, times(1)).findByEmail(anyString());
        assertTrue(booksInCart.contains(book));
    }

    @Test
    void getBookInCartByName_Success() {
        User user = new User();
        Cart cart = new Cart();
        user.setCart(cart);
        Books book = new Books();
        book.setName("Test Book");
        cart.getBooks().add(book);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Books foundBook = cartService.getBookInCartByName("user@example.com", "Test Book");

        verify(userRepository, times(1)).findByEmail(anyString());
        assertTrue(foundBook.getName().equals("Test Book"));
    }
}
