package com.tontine.customer.repository;

import com.tontine.customer.models.IdCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdCardRepository extends JpaRepository<IdCard, UUID> {
    List<IdCard> findByCustomerId(UUID customerId);
}
