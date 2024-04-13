package BookShopRest.BookShopRest.repositories;

import BookShopRest.BookShopRest.Model.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Books, Long> {
    List<Books> findByName(String name);
}
