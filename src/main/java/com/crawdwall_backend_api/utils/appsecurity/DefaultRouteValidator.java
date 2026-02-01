package com.crawdwall_backend_api.utils.appsecurity;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultRouteValidator {

    private final JwtService jwtService;

    // Endpoint patterns
    public static final List<Pattern> APP_USER_ENDPOINTS = List.of(
            Pattern.compile("/api/v1/app-user/private.*"),
            Pattern.compile("/api/v1/waitlist/private.*"),
            Pattern.compile("/api/v1/utilities/private.*")
    );

    public static final List<Pattern> THERAPIST_ENDPOINTS = List.of(
            Pattern.compile("/api/v1/therapist/private.*"),
            Pattern.compile("/api/v1/therapist/public.*"),
            Pattern.compile("/api/v1/waitlist/private.*")

    );

    public static final List<Pattern> ADMIN_ENDPOINTS = List.of(
            Pattern.compile("/api/v1/admin/private.*"),
            Pattern.compile("/api/v1/blog/admin/private.*"),
            Pattern.compile("/api/v1/role/admin/private.*"),
            Pattern.compile("/api/v1/contact-us/admin/private.*"),
            Pattern.compile("/api/v1/newsletter-emails/admin/private.*"),
            Pattern.compile("/api/v1/app-user/admin/private.*"),
            Pattern.compile("/api/v1/users/admin/private.*"),
            Pattern.compile("/api/v1/therapist/admin/private.*")
    );

        public static final List<Pattern> SUPER_ADMIN_ENDPOINTS = List.of(
            Pattern.compile("/api/v1/admin/private.*"),
            Pattern.compile("/api/v1/admin/super-admin/private.*"),
            Pattern.compile("/api/v1/blog/admin/private.*"),
            Pattern.compile("/api/v1/role/admin/private.*"),
            Pattern.compile("/api/v1/contact-us/admin/private.*"),
            Pattern.compile("/api/v1/newsletter-emails/admin/private.*"),
            Pattern.compile("/api/v1/app-user/admin/private.*"),
            Pattern.compile("/api/v1/therapist/admin/private.*"),
            Pattern.compile("/api/v1/users/admin/private.*")
    );

    // Check if endpoint requires admin role
    public boolean isAdminEndpoint(HttpServletRequest req) {
        log.info("1: isAdminEndpoint: Checking if endpoint requires admin role: {}", req.getRequestURI());
        String uri = req.getRequestURI();
        log.info("2: isAdminEndpoint: URI: {}", uri);
        boolean matches = ADMIN_ENDPOINTS.stream().anyMatch(p -> p.matcher(uri).matches());
        log.info("3: isAdminEndpoint: Matches: {}", matches);
        if (matches) {
            log.info("4: isAdminEndpoint: Endpoint requires admin role: {}", uri);
        } else {
            log.info("5: isAdminEndpoint: Admin does not have access to endpoint: {}", uri);
        }
        return matches;
    }
    // Check if endpoint requires admin role
    public boolean isSuperAdminEndpoint(HttpServletRequest req) {
        log.info("1: isSuperAdminEndpoint: Checking if endpoint requires super admin role: {}", req.getRequestURI());
        String uri = req.getRequestURI();
        log.info("2: isSuperAdminEndpoint: URI: {}", uri);
        boolean matches = SUPER_ADMIN_ENDPOINTS.stream().anyMatch(p -> p.matcher(uri).matches());
        log.info("3: isSuperAdminEndpoint: Matches: {}", matches);
        if (matches) {
            log.info("4: isSuperAdminEndpoint: Endpoint requires super admin role: {}", uri);
        } else {
            log.info("5: isSuperAdminEndpoint: Super admin does not have access to endpoint: {}", uri);
        }
        return matches;
    }

    // Check if endpoint requires app user role
    public boolean isAppUserEndpoint(HttpServletRequest req) {
        log.info("1: isAppUserEndpoint: Checking if endpoint requires app user role: {}", req.getRequestURI());
        String uri = req.getRequestURI();
        log.info("2: isAppUserEndpoint: URI: {}", uri);
        boolean matches = APP_USER_ENDPOINTS.stream().anyMatch(p -> p.matcher(uri).matches());
        log.info("3: isAppUserEndpoint: Matches: {}", matches);
        if (matches) {
            log.info("4: isAppUserEndpoint: Endpoint requires app user role: {}", uri);
        } else {
            log.info("5: isAppUserEndpoint: User does not have access to endpoint: {}", uri);
        }
        return matches;
    }


    // Validate user has correct role for endpoint
    public boolean validateUserRoleForEndpoint(HttpServletRequest req) {
        log.info("1: validateUserRoleForEndpoint: Checking if user has correct role for endpoint: {}", req.getRequestURI());
        String token = req.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            log.info("2: validateUserRoleForEndpoint: Token is null or does not start with Bearer: {}", req.getRequestURI());
            return false;
        }
        
        token = token.substring(7);
        if (!jwtService.isTokenValid(token)) {
            log.info("3: validateUserRoleForEndpoint: Token is not valid: {}", req.getRequestURI());
            return false;
        }
        
        Claims claims = jwtService.extractAllClaims(token);
        String userRole = claims.get("userType", String.class);
        log.info("4: validateUserRoleForEndpoint: User role: {}", userRole);

        switch (userRole) {
            case "ADMIN":
                return isAdminEndpoint(req);
            case "APP_USER":
                return isAppUserEndpoint(req);
            case "SUPER_ADMIN":
                return isSuperAdminEndpoint(req);
            default:
                log.info("5: validateUserRoleForEndpoint: User role is not valid: {}", req.getRequestURI());
                return false;
        }
        // Check endpoint type vs user role
        // if (isAdminEndpoint(req)) {
        //     return "ADMIN".equals(userRole);
        // } else if (isAppUserEndpoint(req)) {
        //     return "USER".equals(userRole) || "ADMIN".equals(userRole);
        // }
        
        // return true; // Public endpoints
    }

    // Predicate for filter
    public Predicate<HttpServletRequest> hasAccess = req -> {
        log.info("1: hasAccess: Checking if user has access to endpoint: {}", req.getRequestURI());
        boolean hasAccess = validateUserRoleForEndpoint(req);
        log.info("2: hasAccess: User has access: {}", hasAccess);
        return hasAccess;
    };
}