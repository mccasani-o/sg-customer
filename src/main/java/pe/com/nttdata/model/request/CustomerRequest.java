package pe.com.nttdata.model.request;

import lombok.Data;

@Data
public class CustomerRequest {
    private Integer clientType;


    private Integer documentType;


    private String documentNumber;


    private String name;


    private String lastName;


    private String email;
}
