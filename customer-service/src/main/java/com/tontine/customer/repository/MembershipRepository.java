package com.tontine.customer.repository;

import com.tontine.customer.models.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    Optional<Membership> findByIdAndTontineId(UUID membershipId, UUID tontineId);
    List<Membership> findAllByTontineId(UUID tontineId);
}
