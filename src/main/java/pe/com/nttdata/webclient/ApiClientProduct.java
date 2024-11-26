package pe.com.nttdata.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pe.com.nttdata.model.response.ProductResponse;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class ApiClientProduct {

    private final WebClient webClient;

    //http://localhost:8882/api/v1/products/673955d14ee5a65f5c4251e8/customer
    public ApiClientProduct(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8882/api/v1/products").build();
    }

    public Flux<ProductResponse> findByProductId(String id) {


        return this.webClient.get()
                .uri("/customer/{id}", id)
                .retrieve()
                .bodyToFlux(ProductResponse.class)
                .map(productResponse -> ProductResponse.builder()
                        .id(productResponse.getId())
                        .productType(productResponse.getProductType())
                        .balance(productResponse.getBalance())
                        .limitMnthlyMovements(productResponse.getLimitMnthlyMovements())
                        .dayMovement(productResponse.getDayMovement())
                        .build())
                .defaultIfEmpty(ProductResponse.builder().build())
                .doOnNext(customProduct ->
                        log.info("Custom Product Response: {}", customProduct)
                )
                .doOnError(error ->
                        log.error("Error retrieving product for ID {}: {}", id, error.getMessage())
                );


    }


}
