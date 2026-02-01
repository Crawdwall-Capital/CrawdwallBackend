package com.crawdwall_backend_api.admin;


import com.crawdwall_backend_api.admin.request.AdminCreateRequest;
import com.crawdwall_backend_api.admin.request.AdminFilterRequest;
import com.crawdwall_backend_api.admin.response.AdminAuthResponse;
import com.crawdwall_backend_api.admin.response.AdminResponse;
import com.crawdwall_backend_api.rolepermissionmgnt.Role;
import com.crawdwall_backend_api.rolepermissionmgnt.RoleRepository;
import com.crawdwall_backend_api.rolepermissionmgnt.RoleService;
import com.crawdwall_backend_api.rolepermissionmgnt.reponse.RoleResponse;
import com.crawdwall_backend_api.userauthmgt.user.User;
import com.crawdwall_backend_api.userauthmgt.user.UserService;
import com.crawdwall_backend_api.userauthmgt.user.UserType;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserCreateRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserResetPasswordRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserCreateResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserResponse;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.PaginatedData;
import com.crawdwall_backend_api.utils.RefinedPagination;
import com.crawdwall_backend_api.utils.UtilsService;

import com.crawdwall_backend_api.utils.appsecurity.JwtService;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import com.crawdwall_backend_api.utils.exception.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final UtilsService utilsService;
    private final MongoTemplate mongoTemplate;
    @Value("${application.superAdmin.emailAddress}")
    private String superAdminEmailAddress;
    @Value("${application.superAdmin.password}")
    private String superAdminPassword;
    @Value("${application.superAdmin.firstName}")
    private String superAdminFirstName;
    @Value("${application.superAdmin.lastName}")
    private String superAdminLastName;
    @Value("${application.superAdmin.phoneNumber}")
    private String superAdminPhoneNumber;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;


    public AdminService(RoleRepository roleRepository, AdminRepository adminRepository, UserService userService, RoleService roleService, UtilsService utilsService, MongoTemplate mongoTemplate, JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.utilsService = utilsService;
        this.mongoTemplate = mongoTemplate;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }
    // service/auth


    public AdminAuthResponse authenticateAdmin(UserAuthRequest request) {
        UserResponse user = userService.authenticateUser(request, UserType.ADMIN);
        Admin admin = adminRepository.findByUserId(user.userId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));
        if (!admin.isActive()) throw new UnauthorizedException("Admin access disabled");

        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

        if(admin.isDefault()){
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.emailAddress(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = generateJwtForAdmin(admin, user, authorities);
            String refresh = generateRefreshTokenForAdmin(admin, user);

            return AdminAuthResponse.builder()
                    .token(token)
                    .refreshToken(refresh)
                    .adminId(admin.getId())
                    .userId(user.userId())
                    .build();
        }

        try {
            Role role = roleService.findRoleById(admin.getRoleId());
            if (role != null) {
//                authorities = AuthorityMapper.toAuthorities(role);
            } else {
                log.warn("Role is null for admin with id: {}", admin.getId());
            }
        } catch (ResourceNotFoundException ex) {
            log.warn("Role not found for admin with id: {} - proceeding without role", admin.getId());
            // continue with empty authorities
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.emailAddress(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = generateJwtForAdmin(admin, user, authorities);
        String refresh = generateRefreshTokenForAdmin(admin, user);

        return AdminAuthResponse.builder()
                .token(token)
                .refreshToken(refresh)
                .adminId(admin.getId())
                .userId(user.userId())
                .build();
    }

    private String generateRefreshTokenForAdmin(Admin admin, UserResponse user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId());
        claims.put("userId", user.userId());
        claims.put("email", user.emailAddress());
        claims.put("userType", user.userType());
        claims.put("roleId", admin.getRoleId());
        return jwtService.generateRefreshToken(user.emailAddress(), claims);
    }

    private String generateJwtForAdmin(Admin admin, UserResponse user,
                                       Collection<? extends GrantedAuthority> auths) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", admin.getId());
        claims.put("userId", user.userId());
        claims.put("email", user.emailAddress());
        claims.put("userType", user.userType());
        claims.put("roleId", admin.getRoleId());
        claims.put("auth", auths.stream().map(GrantedAuthority::getAuthority).toList()); // e.g. ["VIEW_NOMINEE_DIRECTOR_LIST", ...]
        return jwtService.generateToken(claims, user.emailAddress());
    }


    public void inviteAdmin(AdminCreateRequest request) {
    RoleResponse role = roleService.getRoleById(request.roleId());
        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .emailAddress(request.emailAddress())
                .userType(UserType.ADMIN)
                .password(generateAdminPassword())
                .build();

        UserCreateResponse userCreateResponse = userService.createUser(userCreateRequest);

        Admin admin = Admin.builder()
                .userId(userCreateResponse.userId())
                .roleId(role.id())
                .isActive(false)
                .isVerified(false)
                .isDefault(false)
                .build();

        adminRepository.save(admin);

    }

    public PaginatedData fetchAllAdmin(int page, int size) {
        try {
            long count = adminRepository.countByIsDefaultFalseAndIsDeletedFalse();
            RefinedPagination refinedPagination = utilsService.refinePageNumber(page, size, count);
            Page<Admin> admins = adminRepository.findByIsDefaultFalseAndIsDeletedFalse(PageRequest.of(refinedPagination.page(), refinedPagination.size(), Sort.by(Sort.Order.desc("createdAt"))));
            List<UserResponse> userResponses = userService.getAllUsersByType(UserType.ADMIN);
            List<Role> roles = roleService.getAllRoles(false);
            List<AdminResponse> adminResponses = admins.getContent()
                    .stream().map(admin -> buildAdminResponse(admin,
                            getUserResponse(userResponses, admin.getUserId()), getRoleResponse(roles, admin.getRoleId()).getName()))
                    .toList();
            return PaginatedData.builder()
                    .totalPage(admins.getTotalPages())
                    .numberOfElements(admins.getNumberOfElements())
                    .totalElements(admins.getTotalElements())
                    .data(adminResponses)
                    .build();
        } catch (IllegalArgumentException e) {
            log.error("Error fetching all admins", e);
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_FETCHING_ADMINS_DETAILS, true, true);
        }
    }

    public AdminResponse getAdminById(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ADMIN_NOT_FOUND, true, false));
        UserResponse userResponse = userService.getUserById(admin.getUserId());
        RoleResponse role = roleService.getRoleById(admin.getRoleId());
        return buildAdminResponse(admin, userResponse, role.name());
    }

    private UserResponse getUserResponse(List<UserResponse> userResponses, String userId) {
        return userResponses.stream().filter(user -> user.userId().equals(userId)).findFirst().orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));
    }

    private Role getRoleResponse(List<Role> roles, String roleId) {
        return roles.stream().filter(role -> role.getId().equals(roleId)).findFirst().orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ROLE_NOT_FOUND));
    }


//    @PostConstruct
//    private void setSuperAdmin() {
//
//        if (!userService.existsByEmailAddressIgnoreCase(superAdminEmailAddress)) {
//            log.info("Super admin not found, creating super admin");
//            UserCreateRequest userCreateRequest = UserCreateRequest.builder()
//                    .firstName(superAdminFirstName)
//                    .lastName(superAdminLastName)
//                    .emailAddress(superAdminEmailAddress)
//                    .userType(UserType.ADMIN)
//                    .password(superAdminPassword)
//                    .build();
//
//            UserCreateResponse userCreateResponse = userService.createUser(userCreateRequest);
//
//            adminRepository.save(Admin.builder()
//                    .userId(userCreateResponse.userId())
//                    .isActive(true)
//                    .isVerified(true)
//                    .isDefault(true)
//                    .roleId(roleService.getRoleByName("SUPER_ADMIN").id())
//                    .build());
//
//            log.info("Super admin created successfully");
//        }
//        log.info("Super admin found, skipping creation");
//    }


    public PaginatedData searchAdmins(String searchParam, int page, int size) {
        // normalize
        String q = searchParam == null ? "" : searchParam.trim();
        String qLower = q.toLowerCase();

        Query query = new Query()
                .addCriteria(Criteria.where("isDefault").is(false))
                .addCriteria(Criteria.where("isDeleted").is(false));

        // ---- find matching userIds by firstName/lastName/email (user doc) ----
        List<UserResponse> allAdminUsers = userService.getAllUsersByType(UserType.ADMIN);

        // full name support (e.g., "Ada Lovelace" or "Lovelace Ada")
        String[] parts = qLower.split("\\s+");
        boolean twoParts = parts.length == 2;

        Set<String> matchedUserIds = allAdminUsers.stream().filter(u -> {
            String fn = safeLower(u.firstName());
            String ln = safeLower(u.lastName());
            String em = safeLower(u.emailAddress());

            boolean hit = false;
            if (!qLower.isBlank()) {
                hit |= fn.contains(qLower) || ln.contains(qLower) || em.contains(qLower);
            }
            if (!hit && twoParts) {
                // first + last or last + first
                hit |= (fn.contains(parts[0]) && ln.contains(parts[1]))
                        || (fn.contains(parts[1]) && ln.contains(parts[0]));
            }
            return hit;
        }).map(UserResponse::userId).collect(Collectors.toSet());

        // ---- find matching roleIds by roleName (role doc) ----
        Set<String> matchedRoleIds = roleService.getAllRoles(false).stream()
                .filter(r -> safeLower(r.getName()).contains(qLower))
                .map(Role::getId)
                .collect(Collectors.toSet());

        // If nothing matches users or roles, short-circuit
        if ((matchedUserIds == null || matchedUserIds.isEmpty())
                && (matchedRoleIds == null || matchedRoleIds.isEmpty())) {
            return PaginatedData.builder()
                    .totalPage(0).numberOfElements(0).totalElements(0)
                    .data(Collections.emptyList())
                    .build();
        }

        // Build OR criteria on Admin: userId IN (…) OR roleId IN (…)
        List<Criteria> ors = new ArrayList<>();
        if (!matchedUserIds.isEmpty()) ors.add(Criteria.where("userId").in(matchedUserIds));
        if (!matchedRoleIds.isEmpty()) ors.add(Criteria.where("roleId").in(matchedRoleIds));
        if (!ors.isEmpty()) query.addCriteria(new Criteria().orOperator(ors.toArray(new Criteria[0])));

        // paging
        int pageIndex = Math.max(0, page - 1);
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Order.desc("createdAt")));

        long total = mongoTemplate.count(query, Admin.class);
        if (total == 0) {
            return PaginatedData.builder()
                    .totalPage(0).numberOfElements(0).totalElements(0)
                    .data(Collections.emptyList())
                    .build();
        }

        query.with(pageRequest);
        List<Admin> admins = mongoTemplate.find(query, Admin.class);

        // map to DTOs
       Map<String, UserResponse> userById = allAdminUsers.stream()
                .collect(Collectors.toMap(UserResponse::userId, u -> u, (a,b)->a));

       Map<String, Role> roleById = roleService.getAllRoles(false).stream()
                .collect(Collectors.toMap(Role::getId, r -> r, (a,b)->a));

        List<AdminResponse> adminResponses = admins.stream()
                .map(ad -> buildAdminResponse(
                        ad,
                        userById.get(ad.getUserId()),
                        roleById.getOrDefault(ad.getRoleId(), new Role()).getName()
                ))
                .toList();

        Page<Admin> adminPage = new PageImpl<>(admins, pageRequest, total);

        return PaginatedData.builder()
                .numberOfElements(adminPage.getNumberOfElements())
                .totalElements(adminPage.getTotalElements())
                .totalPage(adminPage.getTotalPages())
                .data(adminResponses)
                .build();
    }



    public PaginatedData filterAdmins(AdminFilterRequest request, int page, int size) {
        // Base query: only real, non-deleted admins
        Query query = new Query()
                .addCriteria(Criteria.where("isDefault").is(false))
                .addCriteria(Criteria.where("isDeleted").is(false));

        // 1) Filter by User fields (firstName, lastName, emailAddress) via userService
        boolean wantsUserFilter =
                hasText(request.firstName()) || hasText(request.lastName()) || hasText(request.emailAddress());

        Set<String> userIdsFilter = new HashSet<>();
        List<UserResponse> allAdminUsers = userService.getAllUsersByType(UserType.ADMIN); // cache once

        if (wantsUserFilter) {
            String fn = safeLower(request.firstName());
            String ln = safeLower(request.lastName());
            String em = safeLower(request.emailAddress());

            userIdsFilter = allAdminUsers.stream()
                    .filter(u -> {
                        boolean ok = true;
                        if (hasText(fn)) ok &= safeLower(u.firstName()).contains(fn);
                        if (hasText(ln)) ok &= safeLower(u.lastName()).contains(ln);
                        if (hasText(em)) ok &= safeLower(u.emailAddress()).contains(em);
                        return ok;
                    })
                    .map(UserResponse::userId)
                    .collect(Collectors.toSet());

            if (userIdsFilter.isEmpty()) {
                return PaginatedData.builder()
                        .totalPage(0).numberOfElements(0).totalElements(0)
                        .data(Collections.emptyList())
                        .build();
            }

            query.addCriteria(Criteria.where("userId").in(userIdsFilter));
        }

        // 2) Filter by roleName via roleService (map to roleIds)
        if (hasText(request.roleName())) {
            String rn = safeLower(request.roleName());
            List<Role> roles = roleService.getAllRoles(false);
            Set<String> roleIds = roles.stream()
                    .filter(r -> safeLower(r.getName()).contains(rn))
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            if (roleIds.isEmpty()) {
                return PaginatedData.builder()
                        .totalPage(0).numberOfElements(0).totalElements(0)
                        .data(Collections.emptyList())
                        .build();
            }

            query.addCriteria(Criteria.where("roleId").in(roleIds));
        }

        // 3) Page & sort
        int pageIndex = Math.max(0, page - 1);
        PageRequest pageRequest = PageRequest.of(pageIndex, size, Sort.by(Sort.Order.desc("createdAt")));

        long total = mongoTemplate.count(query, Admin.class);
        if (total == 0) {
            return PaginatedData.builder()
                    .totalPage(0).numberOfElements(0).totalElements(0)
                    .data(Collections.emptyList())
                    .build();
        }

        query.with(pageRequest);
        List<Admin> admins = mongoTemplate.find(query, Admin.class);

        // 4) Build response DTOs (reuse cached users; avoid refetch)
        //    If we had a userIdsFilter, limit the map to those; otherwise use allAdminUsers.
        Set<String> finalUserIdsFilter = userIdsFilter;
        Map<String, UserResponse> userById = (wantsUserFilter
                ? allAdminUsers.stream().filter(u -> finalUserIdsFilter.contains(u.userId()))
                : allAdminUsers.stream())
                .collect(Collectors.toMap(UserResponse::userId, u -> u));

        List<Role> allRoles = roleService.getAllRoles(false);
        Map<String, Role> roleById = allRoles.stream()
                .collect(Collectors.toMap(Role::getId, r -> r));

        List<AdminResponse> adminResponses = admins.stream()
                .map(admin -> buildAdminResponse(
                        admin,
                        userById.get(admin.getUserId()),
                        roleById.getOrDefault(admin.getRoleId(), new Role()).getName()
                ))
                .toList();

        Page<Admin> adminPage = new PageImpl<>(admins, pageRequest, total);

        return PaginatedData.builder()
                .totalPage(adminPage.getTotalPages())
                .numberOfElements(adminPage.getNumberOfElements())
                .totalElements(adminPage.getTotalElements())
                .data(adminResponses)
                .build();
    }

    // --- helpers ---
    private static boolean hasText(String s) { return s != null && !s.isBlank(); }
    private static String safeLower(String s) { return s == null ? "" : s.toLowerCase(); }


    public void changeAdminPassword(String userId, PasswordChangeRequest request) {
        userService.changeUserPassword(userId, UserType.ADMIN, request);
    }

    public void initiateResetPassword(String emailAddress) {
        userService.initiateResetPassword(emailAddress, UserType.ADMIN);
    }

    public void verifyOtp(UserVerifyOtpRequest request) {
    User user =    userService.verifyOtp(request);
    Admin admin = adminRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_ADMIN_NOT_FOUND, true, false));
    admin.setVerified(true);
    admin.setVerifiedAt(LocalDateTime.now());
    admin.setActive(true);
    adminRepository.save(admin);
    }

    public void resendOtp(String emailAddress, UserOtpType userOtpType) {
        userService.resendOtp(emailAddress, userOtpType);
    }

    public void resetPassword(UserResetPasswordRequest request) {
        userService.resetPassword(request);
    }

    private AdminResponse buildAdminResponse(Admin admin, UserResponse userResponse) {
        return AdminResponse.builder()
                .adminId(admin.getId())
                .userId(admin.getUserId())
                .firstName(userResponse.firstName())
                .lastName(userResponse.lastName())
                .emailAddress(userResponse.emailAddress())
                .phoneNumber(userResponse.phoneNumber())
                .profilePictureUrl(userResponse.profilePictureUrl())
                .createdAt(admin.getCreatedAt())
                .isVerified(admin.isVerified())
                .isActive(admin.isActive())
                .build();
    }

    private String generateAdminPassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specials = "!@#$%^&*()-_=+<>?";

        String all = upper + lower + digits + specials;
        StringBuilder sb = new StringBuilder(8);

        // Ensure at least one of each type
        sb.append(upper.charAt((int) (Math.random() * upper.length())));
        sb.append(lower.charAt((int) (Math.random() * lower.length())));
        sb.append(digits.charAt((int) (Math.random() * digits.length())));
        sb.append(specials.charAt((int) (Math.random() * specials.length())));

        for (int i = 4; i < 8; i++) {
            sb.append(all.charAt((int) (Math.random() * all.length())));
        }

        // Shuffle to avoid predictable positions
        char[] pw = sb.toString().toCharArray();
        for (int i = pw.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = pw[i];
            pw[i] = pw[j];
            pw[j] = temp;
        }
        return new String(pw);
    }

    private AdminResponse buildAdminResponse(Admin admin, UserResponse userResponse, String roleName) {
        return AdminResponse.builder()
                .adminId(admin.getId())
                .userId(admin.getUserId())
                .firstName(userResponse.firstName())
                .lastName(userResponse.lastName())
                .emailAddress(userResponse.emailAddress())
                .profilePictureUrl(userResponse.profilePictureUrl())
                .createdAt(admin.getCreatedAt())
                .isVerified(userResponse.isVerified())
                .isActive(userResponse.isActive())
                .roleName(roleName)
                .build();
    }


}
