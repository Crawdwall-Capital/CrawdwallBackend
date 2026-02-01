package com.crawdwall_backend_api.investor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestorRepository extends MongoRepository<Investor, String> {
    
    boolean existsByInvestorEmail(String investorEmail);
    boolean existsByInvestorPhone(String investorPhone);
    boolean existsByInvestorNationalId(String investorNationalId);
    boolean existsByInvestorTaxId(String investorTaxId);
    
    Optional<Investor> findById(String investorId);
    Optional<Investor> findByUserId(String userId);
    Optional<Investor> findByInvestorEmail(String investorEmail);
    Optional<Investor> findByInvestorPhone(String investorPhone);
    Optional<Investor> findByInvestorNationalId(String investorNationalId);
}