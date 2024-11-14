package pe.com.nttdata.service;

import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerResponse> findAll();

    Mono<Void> create(CustomerRequest newCustomerRequest);

    Mono<CustomerResponse> findById(String id);

    Mono<Void> update(String id, CustomerRequest customerRequest);

    Mono<Void> delete(String id);
}
