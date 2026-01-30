package com.crawdwall_backend_api.rolepermissionmgnt;


import com.crawdwall_backend_api.rolepermissionmgnt.reponse.AllPermissionResponse;
import com.crawdwall_backend_api.rolepermissionmgnt.reponse.RoleResponse;
import com.crawdwall_backend_api.rolepermissionmgnt.request.RoleCreateRequest;
import com.crawdwall_backend_api.rolepermissionmgnt.request.RolePermissionsUpdateRequest;
import com.crawdwall_backend_api.rolepermissionmgnt.request.RoleUpdateRequest;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.PaginatedData;
import com.crawdwall_backend_api.utils.RefinedPagination;
import com.crawdwall_backend_api.utils.UtilsService;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final UtilsService utilsService;

    public RoleResponse createRole(RoleCreateRequest request) {
        if (roleRepository.existsByName(request.name())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_ROLE_NAME_ALREADY_EXISTS);
        }
        Role role = Role.builder()
                .name(request.name())
                .description(request.description())
                .businessManagementPermissions(request.businessManagementPermissions())
                .nomineeDirectorManagementPermissions(request.nomineeDirectorManagementPermissions())
                .paymentManagementPermissions(request.paymentManagementPermissions())
                .isActive(true)
                .isDeleted(false)
                .build();

        Role savedRole = roleRepository.save(role);

        return RoleResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .description(savedRole.getDescription())
                .businessManagementPermissions(savedRole.getBusinessManagementPermissions())
                .nomineeDirectorManagementPermissions(savedRole.getNomineeDirectorManagementPermissions())
                .paymentManagementPermissions(savedRole.getPaymentManagementPermissions())
                .active(savedRole.isActive())
                .isDeleted(savedRole.isDeleted())
                .createdAt(savedRole.getCreatedAt())
                .updatedAt(savedRole.getUpdatedAt())
                .build();   
    }

    // UPDATE
    public RoleResponse updateRole(String id, RoleUpdateRequest request) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

        // Check name uniqueness if name is being updated
        if (request.name() != null && !request.name().equals(role.getName())) {
            if (roleRepository.existsByName(request.name())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_ROLE_NAME_ALREADY_EXISTS);
            }
            role.setName(request.name());
        }

        if (request.description() != null) {
            role.setDescription(request.description());
        }
        if (request.businessManagementPermissions() != null) {
            role.setBusinessManagementPermissions(request.businessManagementPermissions());
        }
        if (request.nomineeDirectorManagementPermissions() != null) {
            role.setNomineeDirectorManagementPermissions(request.nomineeDirectorManagementPermissions());
        }
        if (request.paymentManagementPermissions() != null) {
            role.setPaymentManagementPermissions(request.paymentManagementPermissions());
        }


        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }

    public void deleteRole(String id) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

        role.setActive(false);
        roleRepository.save(role);
    }

    public RoleResponse toggleRoleActivation(String id, boolean active) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

        role.setActive(active);
        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }

 


    

    // GET ALL PERMISSIONS FOR A ROLE
    public RoleResponse getAllPermissions(String roleId) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .businessManagementPermissions(role.getBusinessManagementPermissions())
                .nomineeDirectorManagementPermissions(role.getNomineeDirectorManagementPermissions())
                .paymentManagementPermissions(role.getPaymentManagementPermissions())
                .build();
    }


    public RoleResponse updateAllPermissions(String roleId, RolePermissionsUpdateRequest request) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

        if (request.businessManagementPermissions() != null) {
            role.setBusinessManagementPermissions(request.businessManagementPermissions());
        }
        if (request.nomineeDirectorManagementPermissions() != null) {
            role.setNomineeDirectorManagementPermissions(request.nomineeDirectorManagementPermissions());
        }
        if (request.paymentManagementPermissions() != null) {
            role.setPaymentManagementPermissions(request.paymentManagementPermissions());
        }

        Role updatedRole = roleRepository.save(role);
        return mapToResponse(updatedRole);
    }


    public RoleResponse getRoleById(String id) {
        Role role = roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));
        return mapToResponse(role);
    }
    public Role findRoleById(String id) {
        return roleRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));

    }

    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findByNameIgnoreCaseAndIsDeletedFalse(name)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));
        return mapToResponse(role);
    }

    private RoleResponse mapToResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .businessManagementPermissions(role.getBusinessManagementPermissions())
                .nomineeDirectorManagementPermissions(role.getNomineeDirectorManagementPermissions())
                .paymentManagementPermissions(role.getPaymentManagementPermissions())
                .active(role.isActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    public PaginatedData fetchAllRole(int page, int size) {
        try {
            long count = roleRepository.countByIsDeletedFalse();
            RefinedPagination refinedPagination = utilsService.refinePageNumber(page, size, count);
            Page<Role> roles = roleRepository.findAllByIsDeletedFalse(PageRequest.of(refinedPagination.page(), refinedPagination.size(), Sort.by(Sort.Order.desc("createdAt"))));

            Set<RoleResponse> roleResponses = roles.getContent()
                    .stream().map(this::mapToResponse)
                    .collect(Collectors.toSet());
            return PaginatedData.builder()
                    .totalPage(roles.getTotalPages())
                    .numberOfElements(roles.getNumberOfElements())
                    .totalElements(roles.getTotalElements())
                    .data(roleResponses)
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Error fetching all roles", e);
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_FETCHING_ROLES_DETAILS, true, true);
        }
    }

//        public AllPermissionResponse getAllPermissions() {
//            return AllPermissionResponse.builder()
//                    .businessManagementPermissions(Set.of(BusinessManagement.values()))
//                    .nomineeDirectorManagementPermissions(Set.of(NomineeDirectorManagement.values()))
//                    .paymentManagementPermissions(Set.of(PaymentManagement.values()))
//                    .build();
//        }

    public List<Role> getAllRolesByIdsAndIsDeleted(List<String> list) {
            return roleRepository.findAllByIdInAndIsDeletedFalse(list);
    }

    public List<Role> getAllRoles(boolean b) {
        return roleRepository.findAllByIsDeleted(b);
    }
}