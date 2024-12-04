package pe.com.nttdata.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductResponse {

    private String id;

    private String productType;

    private Double balance;

    private Integer limitMnthlyMovements;

    private String dayMovement;

    private BigDecimal limitCredit;

}
