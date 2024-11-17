package pe.com.nttdata.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiErrorResponse {
    private String code;
    private String message;
}
