package pe.com.nttdata.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.service.CustomerService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    WebTestClient client;

    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(new CustomerController(customerService)).build();
        customerRequest = CustomerRequest.builder()
                .clientType("PERSONAL")
                .documentType("DNI")
                .documentNumber("12345678")
                .name("Juan")
                .lastName("Cardenas")
                .email("juan@emeal.com")
                .build();
    }

    @Test
    void getAllCustomers() {
        when(this.customerService.findAll())
                .thenReturn(Flux.just(this.buildCustomerResponse()));

        client.get().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CustomerResponse.class)
                .consumeWith(response -> {
                    List<CustomerResponse> products = response.getResponseBody();
                    Assertions.assertNotNull(products);
                    assertFalse(products.isEmpty());
                });
    }

    @Test
    void insert() {
        when(this.customerService.create(any(CustomerRequest.class)))
                .thenReturn(Mono.empty());

        this.client.post().uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(this.customerRequest), CustomerRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }


    @Test
    void getCustomerById() {

        given(this.customerService.findById(anyString()))
                .willReturn(Mono.just(this.buildCustomerResponse()));

        client.get().uri("/api/v1/customers/437f8666")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerResponse.class)
                .consumeWith(response -> {
                    CustomerResponse prod = response.getResponseBody();
                    assertNotNull(prod);
                    assertEquals("437f8666", prod.getId());
                    assertEquals("Juan", prod.getName());
                });
    }


    @Test
    void updateCustomer() {

        given(this.customerService.update(any(String.class), any(CustomerRequest.class)))
                .willAnswer(invocationOnMock -> Mono.empty());

        client.put().uri("/api/v1/customers/437f8666")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(this.customerRequest), CustomerRequest.class)
                .exchange()
                .expectStatus()
                .isAccepted();
    }


    @Test
    void deleteByClientId() {
        when(this.customerService.delete(anyString()))
                .thenReturn(Mono.empty());

        client.delete().uri("/api/v1/customers/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    private CustomerResponse buildCustomerResponse() {
        return CustomerResponse.builder()
                .id("437f8666")
                .clientType("PERSONAL")
                .documentType("DNI")
                .documentNumber("12345678")
                .name("Juan")
                .lastName("Cardenas")
                .email("juan@emeal.com")
                .build();
    }
}