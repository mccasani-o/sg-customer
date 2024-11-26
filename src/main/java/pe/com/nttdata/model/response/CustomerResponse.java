package pe.com.nttdata.model.response;


import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private String id;

    private Integer clientType;

    private Integer documentType;

    private String documentNumber;

    private String name;

    private String lastName;

    private String email;

    private List<ProductResponse> products;
}
