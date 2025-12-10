package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.IdDocument;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    Customer toCustomer(ApiCustomerRequest customerRequest);

    ApiCustomerResponse toApiCustomerResponse(Customer customer);

    List<ApiCustomerResponse> toApiCustomerResponses(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateCustomerFromRequest(ApiCustomerRequest apiCustomerRequest, @MappingTarget Customer customer);
}
