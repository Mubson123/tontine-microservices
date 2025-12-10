package com.tontine.customer.service;

import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;

import java.util.List;
import java.util.UUID;

public interface IdDocumentService {
    List<ApiIdDocumentResponse> getAllDocuments();
    List<ApiIdDocumentResponse> getAllByCustomerId(UUID customerId);
    ApiIdDocumentResponse getById(UUID customerId, UUID idDocumentId);
    ApiIdDocumentResponse addDocument(UUID customerId, ApiIdDocumentRequest apiIdDocumentRequest);
    ApiIdDocumentResponse updateDocument(UUID customerId, UUID idDocumentId, ApiIdDocumentRequest apiIdDocumentRequest);
    void deleteDocument(UUID customerId, UUID idDocumentId);
}
