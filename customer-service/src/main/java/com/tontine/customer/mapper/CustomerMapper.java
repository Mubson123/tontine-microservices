package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiCustomerRequest;
import com.tontine.customer.model.ApiCustomerResponse;
import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;
import com.tontine.customer.models.Customer;
import com.tontine.customer.models.IdDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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

    Customer toCustomer(ApiCustomerRequest customerRequest);

    ApiCustomerResponse toApiCustomerResponse(Customer customer);

    List<ApiCustomerResponse> toApiCustomerResponses(List<Customer> customers);

    void updateCustomerFromRequest(ApiCustomerRequest apiCustomerRequest, @MappingTarget Customer customer);

    IdDocument toIdDocument(ApiIdDocumentRequest idCardRequest);

    ApiIdDocumentResponse toApiIdDocumentResponse(IdDocument idDocument);

    List<ApiIdDocumentResponse> toApiIdDocumentResponses(List<IdDocument> idDocuments);

    void updateDocumentFromRequest(ApiIdDocumentRequest apiIdDocumentRequest, @MappingTarget IdDocument idDocument);
}
