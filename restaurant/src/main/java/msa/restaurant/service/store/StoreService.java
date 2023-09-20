package msa.restaurant.service.store;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.restaurant.dto.store.OpenStatus;
import msa.restaurant.dto.store.StoreRequestDto;
import msa.restaurant.dto.store.StoreSqsDto;
import msa.restaurant.entity.store.Store;
import msa.restaurant.message_queue.SendingMessageConverter;
import msa.restaurant.message_queue.SqsService;
import msa.restaurant.repository.store.StoreRepository;
import msa.restaurant.service.AddressService;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final AddressService addressService;
    private final SendingMessageConverter sendingMessageConverter;
    private final SqsService sqsService;


    public Store getStore(String managerId, String storeId){
        return storeRepository.readStore(managerId, storeId);
    }

    public List<Store> getStoreList(String managerId){
        return storeRepository.readStoreList(managerId);
    }

    public String createStore(StoreRequestDto data, String managerId) {
        Point coordinate = addressService.getCoordinate(data.getAddress());
        Store store = Store.builder()
                .name(data.getName())
                .foodKind(data.getFoodKind())
                .phoneNumber(data.getPhoneNumber())
                .address(data.getAddress())
                .addressDetail(data.getAddressDetail())
                .introduction(data.getIntroduction())
                .managerId(managerId)
                .location(coordinate)
                .open(false)
                .build();
        Store savedStore = storeRepository.create(store);
        StoreSqsDto storeSqsDto = new StoreSqsDto(savedStore);
        String messageToCreateStore = sendingMessageConverter.createMessageToCreateStore(storeSqsDto);
        sendMessageToOtherServer(messageToCreateStore);
        return savedStore.getStoreId();
    }

    public Store updateStore(String managerId, String storeId, StoreRequestDto data){
        Point location = addressService.getCoordinate(data.getAddress());
        Store store = storeRepository.update(managerId, storeId, data, location);
        StoreSqsDto storeSqsDto = new StoreSqsDto(store);
        String messageToUpdateStore = sendingMessageConverter.createMessageToUpdateStore(storeSqsDto);
        sendMessageToOtherServer(messageToUpdateStore);
        return store;
    }

    public void deleteStore(String managerId, String storeId){
        storeRepository.delete(managerId, storeId);
        String messageToDeleteStore = sendingMessageConverter.createMessageToDeleteStore(storeId);
        sendMessageToOtherServer(messageToDeleteStore);
    }

    public void changeStoreStatus(String managerId, String storeId, OpenStatus openStatus){
        String storeStatusMessage;
        if (openStatus == OpenStatus.OPEN){
            storeStatusMessage = openStore(managerId, storeId);
            sendMessageToOtherServer(storeStatusMessage);
        }else if (openStatus == OpenStatus.CLOSE){
            storeStatusMessage = closeStore(managerId, storeId);
            sendMessageToOtherServer(storeStatusMessage);
        }
    }

    private String openStore(String managerId, String storeId){
        storeRepository.updateOpenStatus(managerId, storeId, true);
        return sendingMessageConverter.createMessageToOpenStore(storeId);
    }

    private String closeStore(String managerId, String storeId){
        storeRepository.updateOpenStatus(managerId, storeId, false);
        return sendingMessageConverter.createMessageToCloseStore(storeId);
    }

    private void sendMessageToOtherServer(String message){
        sqsService.sendToCustomer(message);
        sqsService.sendToRider(message);
    }

}
