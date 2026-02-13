package com.crawdwall_backend_api.financing;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancingRepository extends MongoRepository<Financing, String> {
    
    List<Financing> findByCompanyId(String companyId);
    
    Optional<Financing> findByIdAndCompanyId(String id, String companyId);
}
