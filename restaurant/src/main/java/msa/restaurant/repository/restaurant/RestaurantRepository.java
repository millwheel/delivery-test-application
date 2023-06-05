package msa.restaurant.repository.restaurant;

import msa.restaurant.DAO.FoodKindType;
import msa.restaurant.DAO.Menu;
import msa.restaurant.DAO.Store;
import msa.restaurant.DTO.RestaurantForm;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    String create(Store store);
    void update(String restaurantId, RestaurantForm data);
    Optional<Store> findById(String restaurantId);
    List<Store> findAll();
    void updateName(String restaurantId, String name);
    void updateFoodKind(String restaurantId, FoodKindType foodKind);
    void updatePhoneNumber(String restaurantId, String phoneNumber);
    void updateAddress(String restaurantId, String address);
    void updateAddressDetail(String restaurantId, String addressDetail);
    void updateLocation(String restaurantId, Point location);
    void updateIntroduction(String restaurantId, String introduction);
    void updateMenuList(String restaurantId, List<Menu> menuList);
    void updateOpenStatus(String restaurantId, boolean open);
    void deleteAll();
}
