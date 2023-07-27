package msa.restaurant.controller;

import jakarta.servlet.http.HttpServletResponse;
import msa.restaurant.dto.order.OrderResponseDto;
import msa.restaurant.entity.order.Order;
import msa.restaurant.entity.order.OrderStatus;
import msa.restaurant.service.store.StoreService;
import msa.restaurant.sqs.SendingMessageConverter;
import msa.restaurant.sqs.SqsService;
import msa.restaurant.service.order.OrderService;
import msa.restaurant.sse.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;

@RestController
@RequestMapping("/restaurant/store/{storeId}/order")
public class OrderController {

    private final OrderService orderService;
    private final SseService sseService;
    private final SendingMessageConverter sendingMessageConverter;
    private final SqsService sqsService;

    @Autowired
    public OrderController(OrderService orderService, SseService sseService, SendingMessageConverter sendingMessageConverter, SqsService sqsService) {
        this.orderService = orderService;
        this.sseService = sseService;
        this.sendingMessageConverter = sendingMessageConverter;
        this.sqsService = sqsService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter showOrderList(@PathVariable String storeId){
        SseEmitter sseEmitter = sseService.connect(storeId);
        sseService.showOrderList(storeId);
        return sseEmitter;
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto showOrderInfo(@PathVariable String storeId,
                                          @PathVariable String orderId){
        Optional<Order> orderOptional = orderService.getOrder(orderId);
        if (orderOptional.isEmpty()){
            throw new NullPointerException("Order doesn't exist. " + orderId + " is not correct order id.");
        }
        Order order = orderOptional.get();
        if (!order.getStoreId().equals(storeId)){
            throw new IllegalCallerException("This order doesn't belong to the store");
        }
        return new OrderResponseDto(order);
    }

    @PostMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void acceptOrder(@PathVariable String orderId,
                                  @PathVariable String storeId) {
        Optional<Order> orderOptional = orderService.getOrder(orderId);
        if (orderOptional.isEmpty()){
            throw new NullPointerException("Order doesn't exist. " + orderId + " is not correct order id.");
        }
        Order order = orderOptional.get();
        if (!order.getStoreId().equals(storeId)){
            throw new IllegalCallerException("This order doesn't belong to the store");
        }
        OrderStatus changedOrderStatus = orderService.changeOrderStatusFromManager(orderId, order.getOrderStatus());
        String messageToUpdateOrderStatus = sendingMessageConverter.createMessageToChangeOrderStatus(orderId, changedOrderStatus);
        String messageToAcceptOrder = sendingMessageConverter.createMessageToAcceptOrder(order);
        sqsService.sendToCustomer(messageToUpdateOrderStatus);
        sqsService.sendToRider(messageToAcceptOrder);
    }

    @PutMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void changeOrderStatus(@PathVariable String orderId,
                                  @PathVariable String storeId){
        Optional<Order> orderOptional = orderService.getOrder(orderId);
        if (orderOptional.isEmpty()){
            throw new NullPointerException("Order doesn't exist. " + orderId + " is not correct order id.");
        }
        Order order = orderOptional.get();
        if (!order.getStoreId().equals(storeId)){
            throw new IllegalCallerException("This order doesn't belong to the store");
        }

    }

}
