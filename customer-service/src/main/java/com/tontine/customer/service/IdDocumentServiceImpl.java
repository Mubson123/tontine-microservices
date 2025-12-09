package com.tontine.customer.service;

import com.tontine.customer.exception.IdDocumentAlreadyExistsException;
import com.tontine.customer.exception.IdDocumentExpiredException;
import com.tontine.customer.exception.IdDocumentNotFoundException;
import com.tontine.customer.mapper.CustomerMapper;
import com.tontine.customer.model.ApiIdDocumentRequest;
import com.tontine.customer.model.ApiIdDocumentResponse;
import com.tontine.customer.models.IdDocument;
import com.tontine.customer.models.utils.DocumentType;
import com.tontine.customer.repository.IdDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdDocumentServiceImpl implements IdDocumentService {

    private static final String DOCUMENT_NOT_FOUND = "Identification document for Customer %s not found";
    private final IdDocumentRepository idDocumentRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<ApiIdDocumentResponse> getAllDocuments() {
        List<IdDocument> idDocuments = idDocumentRepository.findAll();
        return customerMapper.toApiIdDocumentResponses(idDocuments);
    }

    @Override
    public List<ApiIdDocumentResponse> getAllByCustomerId(UUID customerId) {
        List<IdDocument> idDocuments = idDocumentRepository.findByCustomerId(customerId);
        return customerMapper.toApiIdDocumentResponses(idDocuments);
    }

    @Override
    public ApiIdDocumentResponse getById(UUID customerId, UUID idDocumentId) {
        IdDocument idDocument = idDocumentRepository.findByIdAndCustomerId(idDocumentId, customerId)
                .orElseThrow(() -> new IdDocumentNotFoundException(DOCUMENT_NOT_FOUND.formatted(customerId)));
        return customerMapper.toApiIdDocumentResponse(idDocument);
    }

    @Override
    @Transactional
    public ApiIdDocumentResponse createDocument(UUID customerId, ApiIdDocumentRequest apiIdDocumentRequest) {
        boolean exists = idDocumentRepository.existsByDocumentType(apiIdDocumentRequest.getDocumentType().name());
        if (exists) {
            throw new IdDocumentAlreadyExistsException(
                    "Identification document of type %s already exists"
                            .formatted(apiIdDocumentRequest.getDocumentType().name()));
        }
        IdDocument idDocument = customerMapper.toIdDocument(apiIdDocumentRequest);
        idDocument.setCustomerId(customerId);
        idDocument = idDocumentRepository.save(idDocument);
        return customerMapper.toApiIdDocumentResponse(idDocument);
    }

    @Override
    @Transactional
    public ApiIdDocumentResponse updateDocument(UUID customerId, UUID idDocumentId, ApiIdDocumentRequest apiIdDocumentRequest) {
        IdDocument idDocument = idDocumentRepository.findByIdAndCustomerId(idDocumentId, customerId)
                .orElseThrow(() -> new IdDocumentNotFoundException(DOCUMENT_NOT_FOUND.formatted(customerId)));
        LocalDate expiryDateCurrentDocument = idDocument.getExpiryDate();
        if (expiryDateCurrentDocument != null && expiryDateCurrentDocument.isBefore(LocalDate.now())) {
            throw new IdDocumentExpiredException(
                    "Cannot update expired identification document for Customer %s".formatted(customerId));
        }
        customerMapper.updateDocumentFromRequest(apiIdDocumentRequest, idDocument);
        idDocument = idDocumentRepository.save(idDocument);
        return customerMapper.toApiIdDocumentResponse(idDocument);
    }

    @Override
    @Transactional
    public void deleteDocument(UUID customerId, UUID idDocumentId) {
        IdDocument idDocument = idDocumentRepository.findByIdAndCustomerId(idDocumentId, customerId)
                .orElseThrow(() -> new IdDocumentNotFoundException(DOCUMENT_NOT_FOUND.formatted(customerId)));
        idDocumentRepository.delete(idDocument);
    }
}
