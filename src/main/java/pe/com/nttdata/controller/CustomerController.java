package pe.com.nttdata.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public Flux<CustomerResponse> getAll() {
        return this.customerService.findAll();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> insert(@RequestBody CustomerRequest newCustomerRequest) {
        return this.customerService.create(newCustomerRequest).then();
    }

    @GetMapping("/{id}")
    public Mono<CustomerResponse> getUserById(@PathVariable String id) {
        return this.customerService.findById(id);


    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<Void> update(@PathVariable String id, @RequestBody CustomerRequest customerRequest) {
        return customerService.update(id, customerRequest);


    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return this.customerService.delete(id);
    }
}
