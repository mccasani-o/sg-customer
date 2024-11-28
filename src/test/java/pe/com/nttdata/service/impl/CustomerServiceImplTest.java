package pe.com.nttdata.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.nttdata.mapper.CustomerMapper;
import pe.com.nttdata.model.Customer;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.repository.CustomerRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService; // Clase que implementa tu servicio

    private static final String CUSTOMER_ID = "437f8666";

    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        this.customerRequest=CustomerRequest.builder()
                .clientType("business")
                .documentType("DNI")
                .documentNumber("12345678")
                .name("Juan")
                .lastName("Cardenas")
                .email("juan@emeal.com")
                .build();
    }


    @Test
    void create() {
        when(customerMapper.toCustomerSave(this.customerRequest, 1, 1))
                .thenReturn(this.buildCustomer());

        when(customerRepository.save(this.buildCustomer()))
                .thenReturn(Mono.empty());

        StepVerifier.create(customerService.create(customerRequest))
                .verifyComplete(); // Verifica que el flujo se complete correctamente


    }

    @Test
    void findById_Success() {

        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Mono.just(this.buildCustomer()));
        when(customerMapper.toCustomerResponse(this.buildCustomer())).thenReturn(this.buildCustomerResponse());

        // Act: Verifica el flujo con StepVerifier
        StepVerifier.create(customerService.findById(CUSTOMER_ID))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals("Juan", response.getName());
                    assertEquals("Cardenas", response.getLastName());
                    assertEquals("juan@gmail.com", response.getEmail());
                })
                .verifyComplete();

        // Verify: Asegúrate de que se llamó al repositorio y al mapper
        verify(customerRepository).findById(CUSTOMER_ID);
        verify(customerMapper).toCustomerResponse(this.buildCustomer());
    }






    @Test
    void update() {

        Mockito.when(this.customerRepository.findById(anyString()))
                .thenReturn(Mono.just(this.buildCustomer()));
        when(customerMapper.toCustomerResponse(this.buildCustomer())).thenReturn(this.buildCustomerResponse());
        Mockito.when(this.customerRepository.save(any(Customer.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.customerService.update(CUSTOMER_ID, this.customerRequest))

                .verifyComplete();
    }

    @Test
    void delete() {

        Mockito.when(this.customerRepository.findById(anyString()))
                .thenReturn(Mono.just(this.buildCustomer()));

        when(customerMapper.toCustomerResponse(this.buildCustomer())).thenReturn(this.buildCustomerResponse());

        Mockito.when(this.customerRepository.deleteById(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.customerService.delete("437f8666"))
                .verifyComplete();

    }



    private Customer buildCustomer() {
        return Customer.builder()
                .id(CUSTOMER_ID)
                .clientType(1)
                .documentType(1)
                .documentNumber("12345678")
                .name("Juan")
                .lastName("Cardenas")
                .email("juan@gmail.com").build();
    }

    private CustomerResponse buildCustomerResponse() {


        return CustomerResponse.builder()
                .id(CUSTOMER_ID)
                .clientType(1)
                .documentType(1)
                .documentNumber("12345678")
                .name("Juan")
                .lastName("Cardenas")
                .email("juan@gmail.com")
                .build();
    }
}