package com.crawdwall_backend_api.company;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {
    boolean existsByCompanyName(String companyName);
    boolean existsByCompanyEmail(String companyEmail);
    boolean existsByCompanyPhone(String companyPhone);
    boolean existsByCompanyRegistrationNumber(String companyRegistrationNumber);
    Optional<Company> findById(String companyId);
    Optional<Company> findByUserId(String userId);
}
