package com.crawdwall_backend_api.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {

    long countByIsDefaultFalseAndIsDeletedFalse();

    Page<Admin> findByIsDefaultFalseAndIsDeletedFalse(PageRequest pageRequest);

    Optional<Admin> findByUserId(String s);

    Optional<Admin> findByIsDefaultTrue();
}
