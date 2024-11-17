package pe.com.nttdata.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import pe.com.nttdata.exception.CustomerException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentType {

    DNI(1, "DNI", 8, 8 ),
    RUC(2, "RUC", 11, 11),
    CE(3, "CE", 20, 15);

    final int documentTypeCode;
    final String documentTypeName;
    final int maxDocumentNumber;
    final int minDocumentNumber;

    public static DocumentType findDocumentTypeName(String documentTypeName) {
        return Arrays.stream(values()).filter(t -> t.getDocumentTypeName().equalsIgnoreCase(documentTypeName))
                .findAny()
                .orElseThrow(()->  new CustomerException("Nombre tipo documento es invalido ","400", HttpStatus.BAD_REQUEST));

    }


}
