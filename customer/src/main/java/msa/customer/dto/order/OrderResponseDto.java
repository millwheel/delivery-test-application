package msa.customer.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msa.customer.entity.basket.MenuInBasket;
import msa.customer.entity.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderResponseDto {
    private LocalDateTime orderTime;
    private OrderStatus orderStatus;
    private String customerPhoneNumber;
    private String customerAddress;
    private String customerAddressDetail;
    private List<MenuInBasket> menuInBasketList;
    private int totalPrice;
    private String storeName;
    private String storePhoneNumber;
    private String storeAddress;
    private String storeAddressDetail;
    private int storeOrderNumber;
}
