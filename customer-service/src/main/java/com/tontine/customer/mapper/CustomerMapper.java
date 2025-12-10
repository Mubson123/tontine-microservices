package com.tontine.customer.mapper;

import com.tontine.customer.model.*;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.utils.Address;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(ApiCustomerRequest customerRequest);

    ApiCustomerResponse toApiCustomerResponse(Customer customer);

    List<ApiCustomerResponse> toApiCustomerResponses(List<Customer> customers);

    void updateCustomerFromRequest(ApiCustomerRequest apiCustomerRequest, @MappingTarget Customer customer);

    Address toAddress(ApiAddress apiAddress);
}
