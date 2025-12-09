package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.models.Customer;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    default OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    Customer toCustomer(ApiCustomerRequest customerRequest);
    ApiCustomerResponse toApiResponse(Customer customer);
    List<ApiCustomerResponse> toApiResponses(List<Customer> customers);
}
