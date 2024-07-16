package springApp.repository;

import org.springframework.stereotype.Repository;
import springApp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
