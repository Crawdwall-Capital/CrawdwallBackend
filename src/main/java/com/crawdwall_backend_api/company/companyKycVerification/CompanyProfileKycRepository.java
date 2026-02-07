package com.crawdwall_backend_api.company.companyKycVerification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyProfileKycRepository extends MongoRepository<CompanyProfileKyc, String> {
    
    Optional<CompanyProfileKyc> findByCompanyId(String companyId);
    
    boolean existsByCompanyId(String companyId);
    
    void deleteByCompanyId(String companyId);
}