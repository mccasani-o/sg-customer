package pe.com.nttdata.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.nttdata.mapper.CustomerMapper;
import pe.com.nttdata.model.Customer;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.repository.CustomerRepository;
import pe.com.nttdata.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Flux<CustomerResponse> findAll() {
        return this.customerRepository.findAll().map(this.customerMapper::toCustomerResponse);
    }



    @Override
    public Mono<Void> create(CustomerRequest customerRequest ) {
       return this.customerRepository.save(this.customerMapper.toCustomer(customerRequest)).then();

    }


    @Override
    public Mono<CustomerResponse> findById(String id) {
        return this.customerRepository.findById(id).map(this.customerMapper::toCustomerResponse);
    }

    @Override
    public Mono<Void> update(String id, CustomerRequest request) {

        return this.customerRepository.deleteById(id)
                .then(Mono.defer(() -> {
                    Customer customer = this.customerMapper.toCustomer(request);
                    return this.customerRepository.save(customer);
                }))
                .then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.customerRepository.deleteById(id);
    }


}
