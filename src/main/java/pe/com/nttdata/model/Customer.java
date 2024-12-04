package pe.com.nttdata.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "customers")
public class Customer {

    @Id
    @JsonProperty("id")
    private String id;

    private String clientType;

    private String documentType;

    private String documentNumber;

    private String name;

    private String lastName;

    private String email;


}
