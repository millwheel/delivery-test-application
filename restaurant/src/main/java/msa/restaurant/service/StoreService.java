package msa.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import msa.restaurant.dto.store.StoreRequestDto;
import msa.restaurant.entity.Menu;
import msa.restaurant.entity.MenuPartInfo;
import msa.restaurant.entity.Store;
import msa.restaurant.repository.store.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final AddressService addressService;

    @Autowired
    public StoreService(StoreRepository storeRepository, AddressService addressService) {
        this.storeRepository = storeRepository;
        this.addressService = addressService;
    }

    public Optional<Store> getStore(String storeId){
        return storeRepository.findById(storeId);
    }

    public String createStore(StoreRequestDto data){
        Store store = new Store();
        store.setName(data.getName());
        store.setFoodKind(data.getFoodKind());
        store.setPhoneNumber(data.getPhoneNumber());
        store.setAddress(data.getAddress());
        Point coordinate = addressService.getCoordinate(data.getAddress());
        store.setLocation(coordinate);
        store.setAddressDetail(data.getAddressDetail());
        store.setIntroduction(data.getIntroduction());
        return storeRepository.create(store);
    }

    public void updateStore(String storeId, StoreRequestDto data){
        storeRepository.update(storeId, data);
        Point coordinate = addressService.getCoordinate(data.getAddress());
        storeRepository.updateLocation(storeId, coordinate);
    }

    public void deleteStore(String storeId){
        storeRepository.deleteById(storeId);
    }

    public void openStore(String storeId){
        storeRepository.updateOpenStatus(storeId, true);
    }

    public void closeStore(String storeId){
        storeRepository.updateOpenStatus(storeId, false);
    }

    public List<MenuPartInfo> getMenuList(String storeId){
        return storeRepository.findById(storeId).map(Store::getMenuPartInfoList).orElseGet(ArrayList::new);
    }

    public void updateMenuList(String storeId, MenuPartInfo menuPartInfo){
        List<MenuPartInfo> menuList = getMenuList(storeId);
        menuList.add(menuPartInfo);
        storeRepository.updateMenuList(storeId, menuList);
    }

    public void deleteMenuFromList(String storeId, String menuId){
        List<MenuPartInfo> menuList = getMenuList(storeId);
        menuList.removeIf(menuPartInfo -> menuPartInfo.getMenuId().equals(menuId));
        storeRepository.updateMenuList(storeId, menuList);
    }


}
