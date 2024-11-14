package pe.com.nttdata.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import pe.com.nttdata.model.Customer;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer,String> {
}
