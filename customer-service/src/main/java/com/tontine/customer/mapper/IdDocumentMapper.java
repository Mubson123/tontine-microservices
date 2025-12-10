package com.tontine.customer.mapper;

import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;
import com.tontine.customer.models.IdDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IdDocumentMapper {

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
