package com.tontine.oauth.repository;

import com.tontine.oauth.models.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface TokensRepository extends JpaRepository<Tokens, UUID> {
    Optional<Tokens> findByEmail(String email);
}
