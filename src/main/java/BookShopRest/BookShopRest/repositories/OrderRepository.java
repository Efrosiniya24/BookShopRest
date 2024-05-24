package BookShopRest.BookShopRest.repositories;

import BookShopRest.BookShopRest.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
