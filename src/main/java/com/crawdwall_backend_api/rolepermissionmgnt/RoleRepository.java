package com.crawdwall_backend_api.rolepermissionmgnt;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;
import java.util.Optional;


public interface RoleRepository extends MongoRepository<Role, String> {
    
    boolean existsByName(String name);

    List<Role> findAllByIsDeleted(boolean isDeleted);




    Optional<Role> findByIdAndIsDeletedFalse(String id);
    Optional<Role> findByNameIgnoreCaseAndIsDeletedFalse(String name);
    @Query("{ 'name': { $regex: ?0, $options: 'i' }, 'deleted': false }")
    List<Role> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    long countByIsDeletedFalse();

    Page<Role> findAllByIsDeletedFalse(PageRequest createdAt);

    List<Role> findAllByIdInAndIsDeletedFalse(List<String> list);
}
