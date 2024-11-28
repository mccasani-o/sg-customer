package pe.com.nttdata.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pe.com.nttdata.exception.CustomerException;
import pe.com.nttdata.mapper.CustomerMapper;
import pe.com.nttdata.model.Customer;
import pe.com.nttdata.model.enums.ClientType;
import pe.com.nttdata.model.enums.DocumentType;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.ClientTypeAndDocumentTypeResponse;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.model.response.ProductResponse;
import pe.com.nttdata.repository.CustomerRepository;
import pe.com.nttdata.service.CustomerService;
import pe.com.nttdata.webclient.ApiClientProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    public static final String MESSAGE = "El recurso solicitado no se encuentra";
    private final CustomerRepository customerRepository;
    private final ApiClientProduct clientProduct;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ApiClientProduct clientProduct, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.clientProduct = clientProduct;
        this.customerMapper = customerMapper;
    }

    @Override
    public Flux<CustomerResponse> findAll() {
        return this.customerRepository.findAll()
                .switchIfEmpty(subscriber -> new CustomerException("Data no encontrado","200", HttpStatus.OK))
                .doOnNext(customer -> log.info("DATA: {}", customer))
                .flatMap(customer -> this.clientProduct.findByProductId(customer.getId())
                        .collectList() // Recolecta todos los productos en una lista
                        .map(productResponses -> toCustomerResponse(customer, productResponses))
                        .defaultIfEmpty(toCustomerResponse(customer, Collections.emptyList())) // Maneja clientes sin productos
                )
                .doOnError(error -> log.error("Error fetching customers: {}", error.getMessage()));
    }


    @Override
    public Mono<Void> create(CustomerRequest customerRequest ) {
       return this.getClientTypeAndDocumentType(customerRequest)
               .flatMap(typeResponse->this.customerRepository.save(this.customerMapper.toCustomerSave(customerRequest,
                       typeResponse.getClientType(),
                       typeResponse.getDocumentType())))
               .then();

    }


    @Override
    public Mono<CustomerResponse> findById(String id) {
        return this.customerRepository.findById(id)
                .map(this.customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new CustomerException(MESSAGE, "400", HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Mono<Void> update(String id, CustomerRequest request) {
        return this.getClientTypeAndDocumentType(request)
                .flatMap(typeResponse ->
                        this.findById(id)
                                .flatMap(customerResponse->this.customerRepository.save(this.toCustomerUpdate(request, customerResponse.getId(),typeResponse)))

                ).then();
    }



    @Override
    public Mono<Void> delete(String id) {
        return this.findById(id)
                .flatMap(customerResponse->this.customerRepository.deleteById(customerResponse.getId()));
    }


    Mono<ClientTypeAndDocumentTypeResponse> getClientTypeAndDocumentType(CustomerRequest customerRequest) {
        return Mono.defer(() -> {
            ClientType clientTypeEnum = ClientType.findCustomerType(customerRequest.getClientType());
            DocumentType documentTypeEnum = DocumentType.findDocumentTypeName(customerRequest.getDocumentType());
            int documentLength = customerRequest.getDocumentNumber().length();

            switch (documentTypeEnum){
                case DNI:
                    if (documentLength < documentTypeEnum.getMinDocumentNumber() || documentLength > documentTypeEnum.getMaxDocumentNumber()) {
                        throw new CustomerException("El número de DNI debe tener exactamente 8 dígitos", "400", HttpStatus.BAD_REQUEST);
                    }
                    break;
                case RUC:
                    if (documentLength < documentTypeEnum.getMinDocumentNumber() || documentLength > documentTypeEnum.getMaxDocumentNumber()) {
                        throw new CustomerException("El número de RUC debe tener exactamente 11 dígitos", "400", HttpStatus.BAD_REQUEST);
                    }
                    break;
                case CE:
                    if (documentLength < documentTypeEnum.getMinDocumentNumber() || documentLength > documentTypeEnum.getMaxDocumentNumber()) {
                        throw new CustomerException("El número de CE debe tener entre 15 y 20 caracteres", "400", HttpStatus.BAD_REQUEST);
                    }
                    break;
                default:
                    throw new CustomerException("Número de documento no reconocido", "400", HttpStatus.BAD_REQUEST);

            }
            return Mono.just(ClientTypeAndDocumentTypeResponse.builder()
                    .clientType(clientTypeEnum.getCodeType())
                    .documentType(documentTypeEnum.getDocumentTypeCode())
                    .build());
        });
    }


    Customer toCustomerUpdate(CustomerRequest customerRequest, String id, ClientTypeAndDocumentTypeResponse typeResponse){
        return Customer.builder()
                .id(id)
                .clientType(typeResponse.getClientType())
                .documentType(typeResponse.getDocumentType())
                .documentNumber(customerRequest.getDocumentNumber())
                .name(customerRequest.getName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .build();
    }

    private CustomerResponse toCustomerResponse(Customer customer, List<ProductResponse> productResponses) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .clientType(customer.getClientType())
                .documentType(customer.getDocumentType())
                .documentNumber(customer.getDocumentNumber())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .products(productResponses) // Asigna la lista de productos
                .build();
    }

}
