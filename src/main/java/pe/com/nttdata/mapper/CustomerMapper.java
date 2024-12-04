package pe.com.nttdata.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import pe.com.nttdata.model.Customer;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;
import pe.com.nttdata.model.response.ProductResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper( CustomerMapper.class );


    CustomerResponse toCustomerResponse(Customer customer);


    @Mapping(target = "clientType", source = "codeType")
    @Mapping(target = "documentType", source = "documentTypeCode")
    Customer toCustomerSave(CustomerRequest customerRequest, String codeType, String documentTypeCode);


}

