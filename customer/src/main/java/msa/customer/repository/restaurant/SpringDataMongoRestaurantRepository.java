package msa.customer.repository.restaurant;

import msa.customer.DAO.FoodKindType;
import msa.customer.DAO.Restaurant;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataMongoRestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByLocationNearAndFoodKindIs(Point location, Distance distance, FoodKindType foodKind);
}
