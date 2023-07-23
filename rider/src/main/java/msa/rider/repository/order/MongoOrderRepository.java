package msa.rider.repository.order;

import msa.rider.dto.rider.RiderPartDto;
import msa.rider.entity.order.Order;
import msa.rider.entity.order.OrderStatus;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoOrderRepository implements OrderRepository {

    private final SpringDataMongoOrderRepository repository;

    public MongoOrderRepository(SpringDataMongoOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createOrder(Order order) {
        Order savedOrder = repository.save(order);
        return savedOrder.getOrderId();
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return repository.findById(orderId);
    }

    @Override
    public List<Order> findByRiderId(String riderId) {
        return repository.findByRiderId(riderId);
    }

    @Override
    public List<Order> findNewOrderListNearLocation(Point location) {
        return repository.findByStoreLocationNearAndOrderStatusIs(location, OrderStatus.ORDER_ACCEPT);
    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
        repository.findById(orderId).ifPresent(order -> {
            order.setOrderStatus(orderStatus);
            repository.save(order);
        });
    }

    @Override
    public void updateOrderInfo(String orderId, RiderPartDto riderPartDto, Point location) {
        repository.findById(orderId).ifPresent(order -> {
            order.setRiderId(riderPartDto.getRiderId());
            order.setRiderName(riderPartDto.getName());
            order.setRiderPhoneNumber(riderPartDto.getPhoneNumber());
            order.setRiderLocation(location);
            repository.save(order);
        });
    }

    @Override
    public void deleteOrder(String orderId) {
        repository.deleteById(orderId);
    }
}
