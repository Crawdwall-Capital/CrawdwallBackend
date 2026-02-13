package com.crawdwall_backend_api.company.companyKycOne;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyKycOneRepository extends MongoRepository<CompanyKycOne, String> {
    CompanyKycOne findByCompanyId(String companyId);
}
