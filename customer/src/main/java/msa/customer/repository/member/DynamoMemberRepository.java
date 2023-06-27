package msa.customer.repository.member;

import msa.customer.dto.customer.CustomerRequestDto;
import msa.customer.entity.member.Customer;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DynamoMemberRepository implements MemberRepository {

    private final SpringDataDynamoMemberRepository repository;

    public DynamoMemberRepository(SpringDataDynamoMemberRepository repository) {
        this.repository = repository;
    }
    @Override
    public String create(Customer customer) {
        Customer savedCustomer = repository.save(customer);
        return savedCustomer.getCustomerId();
    }

    @Override
    public Optional<Customer> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void update(String customerId, CustomerRequestDto data) {
        repository.findById(customerId).ifPresent(customer -> {
            if (data.getName() != null) customer.setName(data.getName());
            if (data.getPhoneNumber() != null) customer.setPhoneNumber(data.getPhoneNumber());
            repository.save(customer);
        });
    }

    @Override
    public void update(String customerId, CustomerRequestDto data, Point location) {
        repository.findById(customerId).ifPresent(customer -> {
            if (data.getName() != null) customer.setName(data.getName());
            if (data.getPhoneNumber() != null) customer.setPhoneNumber(data.getPhoneNumber());
            customer.setAddress(data.getAddress());
            customer.setLocation(location);
            if (data.getAddressDetail() != null) customer.setAddressDetail(data.getAddressDetail());
            repository.save(customer);
        });
    }

}
