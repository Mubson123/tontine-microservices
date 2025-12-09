package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.IdDocument;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    default OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toCustomer(ApiCustomerRequest customerRequest);

    ApiCustomerResponse toApiCustomerResponse(Customer customer);

    List<ApiCustomerResponse> toApiCustomerResponses(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCustomerFromRequest(ApiCustomerRequest apiCustomerRequest, @MappingTarget Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    IdDocument toIdDocument(ApiIdDocumentRequest idCardRequest);

    @Mapping(target = "customerId", source = "customer.id")
    ApiIdDocumentResponse toApiIdDocumentResponse(IdDocument idDocument);

    List<ApiIdDocumentResponse> toApiIdDocumentResponses(List<IdDocument> idDocuments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateDocumentFromRequest(ApiIdDocumentRequest apiIdDocumentRequest, @MappingTarget IdDocument idDocument);
}
