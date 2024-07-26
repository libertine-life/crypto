package com.epam.xm.crypto.repository;

import com.epam.xm.crypto.entity.CryptoSymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link CryptoSymbolEntity}.
 */
@Repository
public interface CryptoSymbolRepository extends JpaRepository<CryptoSymbolEntity, String> {
}
