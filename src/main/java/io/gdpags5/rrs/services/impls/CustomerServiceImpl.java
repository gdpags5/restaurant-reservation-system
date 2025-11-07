package io.gdpags5.rrs.services.impls;

import io.gdpags5.rrs.dtos.CustomerDTO;
import io.gdpags5.rrs.entities.Customer;
import io.gdpags5.rrs.respositories.CustomerRepository;
import io.gdpags5.rrs.services.CustomerService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO saveOrUpdate(CustomerDTO dto) {
        Customer customerToBeSavedOrUpdated = dto.id() != null
                ? customerRepository.getReferenceById(dto.id())
                : new Customer();
        customerToBeSavedOrUpdated.setFullName(dto.fullName());
        customerToBeSavedOrUpdated.setPhoneNumber(Long.valueOf(dto.phoneNumber()));
        customerToBeSavedOrUpdated.setEmail(dto.email());
        customerToBeSavedOrUpdated.setDateAndTimeUpdated(LocalDateTime.now());
        if (dto.id() == null) customerToBeSavedOrUpdated.setDateAndTimeCreated(LocalDateTime.now());

        Customer savedOrUpdatedCustomer = customerRepository.save(customerToBeSavedOrUpdated);
        return new CustomerDTO(savedOrUpdatedCustomer.getId(),
                savedOrUpdatedCustomer.getFullName(),
                savedOrUpdatedCustomer.getPhoneNumber().toString(),
                savedOrUpdatedCustomer.getEmail(),
                savedOrUpdatedCustomer.getDateAndTimeUpdated());
    }

    @Override
    public Optional<CustomerDTO> findById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            return optionalCustomer.map(customer -> new CustomerDTO(customer.getId(),
                    customer.getFullName(),
                    customer.getPhoneNumber().toString(),
                    customer.getEmail(),
                    customer.getDateAndTimeUpdated()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(CustomerDTO dto) {
        customerRepository.deleteById(dto.id());
    }

    @Override
    public List<CustomerDTO> findAll() {
        return customerRepository.findAll().stream().map(customer -> new CustomerDTO(customer.getId(),
                customer.getFullName(),
                customer.getPhoneNumber().toString(),
                customer.getEmail(),
                customer.getDateAndTimeUpdated())).toList();
    }
}
