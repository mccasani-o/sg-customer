package pe.com.nttdata.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductResponse {

    private String id;

    private String productType;

    private Double balance;

    private Integer limitMnthlyMovements;

    private Integer dayMovement;

}
