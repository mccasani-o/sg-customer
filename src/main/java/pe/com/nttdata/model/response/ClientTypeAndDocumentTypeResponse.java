package pe.com.nttdata.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientTypeAndDocumentTypeResponse {
    private int clientType;
    private int documentType;



}
