package pe.com.nttdata.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pe.com.nttdata.model.Customer;
import pe.com.nttdata.model.request.CustomerRequest;
import pe.com.nttdata.model.response.CustomerResponse;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper( CustomerMapper.class );

    Customer toCustomer(CustomerRequest customerRequest );

    CustomerResponse toCustomerResponse(Customer customer);
}
