package pe.com.nttdata.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {

    private String clientType;


    private String documentType;


    private String documentNumber;


    private String name;


    private String lastName;


    private String email;
}
