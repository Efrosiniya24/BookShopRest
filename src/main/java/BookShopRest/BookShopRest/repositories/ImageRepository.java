package BookShopRest.BookShopRest.repositories;

import BookShopRest.BookShopRest.Model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

}