package pe.com.nttdata.model.request;

import lombok.Data;

@Data
public class CustomerRequest {

    private String clientType;


    private String documentType;


    private String documentNumber;


    private String name;


    private String lastName;


    private String email;
}
