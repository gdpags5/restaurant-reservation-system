package io.gdpags5.rrs.respositories;

import io.gdpags5.rrs.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
