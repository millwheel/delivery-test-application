package msa.customer.repository.menu;

import msa.customer.entity.Menu;
import msa.customer.entity.Store;

import java.util.Optional;

public interface MenuRepository {
    String make(Menu menu);
    Optional<Menu> findById(String id);
    void setName(String id, String name);
    void setPrice(String id, int price);
    void setDescription(String id, String description);
    void setRestaurant(String id, Store store);
    void deleteAll();
}
