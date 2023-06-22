package msa.customer.entity.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document("order")
@NoArgsConstructor
public class Order {
    @MongoId
    private String orderId;
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerAddressDetail;
    private int storeOrderNumber;
    private String storeId;
    private String storeName;
    private String storeAddress;
    private String storeAddressDetail;
    private String riderId;
    private int totalPrice;
}
