package com.tontine.customer.repository;

import com.tontine.customer.models.IdDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdDocumentRepository extends JpaRepository<IdDocument, UUID> {
    List<IdDocument> findByCustomerId(UUID customerId);
    boolean existsByDocumentType(String documentType);
    Optional<IdDocument> findByIdAndCustomerId(UUID idDocumentId, UUID customerId);
}
