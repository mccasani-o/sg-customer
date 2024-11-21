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
import pe.com.nttdata.repository.CustomerRepository;
import pe.com.nttdata.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    public static final String MESSAGE = "El recurso solicitado no se encuentra";
    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public Flux<CustomerResponse> findAll() {
        return this.customerRepository.findAll()
                .map(this.customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new CustomerException("No customers found","204", HttpStatus.NO_CONTENT)));
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


    private Mono<ClientTypeAndDocumentTypeResponse> getClientTypeAndDocumentType(CustomerRequest customerRequest) {
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

}
