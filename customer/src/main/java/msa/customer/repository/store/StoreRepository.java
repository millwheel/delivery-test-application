package msa.customer.repository.store;

import msa.customer.entity.FoodKindType;
import msa.customer.entity.Menu;
import msa.customer.entity.Store;
import msa.customer.dto.StoreSqsDto;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    String create(Store store);
    Optional<Store> findById(String id);
    List<Store> findStoreNear(Point location, FoodKindType foodKind);
    void update(StoreSqsDto data);
    void updateMenuList(String id, List<Menu> menuList);
    void updateOpenStatus(String id, boolean open);
    void deleteById(String id);
}
