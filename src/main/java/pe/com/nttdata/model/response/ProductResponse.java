package pe.com.nttdata.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonIgnoreProperties(ignoreUnknown = true)

public class ProductResponse {

    private String id;

    private String productType;

    private Double balance;

    private Integer limitMnthlyMovements;

    private Integer dayMovement;

}
