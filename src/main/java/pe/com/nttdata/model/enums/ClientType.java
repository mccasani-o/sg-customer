package pe.com.nttdata.model.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import pe.com.nttdata.exception.CustomerException;

import java.util.Arrays;

@Getter
public enum ClientType {

    BUSINESS(1,"BUSINESS"),
    PERSONAL(2,"PERSONAL");

    int codeType;
    String nameType;

    ClientType(int codeType, String nameType) {
        this.codeType = codeType;
        this.nameType = nameType;
    }



    public static ClientType findCustomerType(String nameType) {
        return Arrays.stream(values()).filter(t -> t.getNameType().equalsIgnoreCase(nameType))
                .findAny()
                .orElseThrow(()->  new CustomerException("Tipo cliente es invalido...","400", HttpStatus.BAD_REQUEST));
    }
}
