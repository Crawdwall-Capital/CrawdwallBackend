package com.crawdwall_backend_api.utils.appsecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ApiSecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiSecurityFilter.class);

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Value("${application.security.api-key}")
    private String apiKey;

    // Public endpoints (regex)
    private static final List<Pattern> PUBLIC_PATTERNS = Arrays.asList(
            Pattern.compile("^/api/v1/admin/public($|/.*)"),
            Pattern.compile("^/api/v1/company/public($|/.*)"),
            Pattern.compile("^/api/v1/company/public/?$"),
            Pattern.compile("^/api/v1/utilities/public($|/.*)"),
            Pattern.compile("^/swagger-ui\\.html$"),
            Pattern.compile("^/swagger-ui/?$"),
            Pattern.compile("^/swagger-ui/.*$"),
            Pattern.compile("^/v3/api-docs($|/.*)$"),
            Pattern.compile("^/api-docs($|/.*)$")
    );

    // ======================= doFilterInternal ======================= // [M01]
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        log.info("[M01] ENTER {} {}", req.getMethod(), req.getRequestURI());

        try {
            // 1) Public? pass without any validation
            if (isPublicEndpoint(req.getRequestURI())) {
                log.info("[M01-PUB] public endpoint detected: {} -> passing through", req.getRequestURI());
                chain.doFilter(req, res);
                log.info("[M01-PUB-END] Request completed for: {}", req.getRequestURI());
                return;
            }

            // 2) Private endpoints require API key
            validateApiKey(req);
            log.info("[M01-AK] API key validated successfully");

            // 3) JWT validation for private endpoints
            String h = req.getHeader("Authorization");
            if (h == null || !h.startsWith("Bearer ")) {
                log.warn("[M01-TOKEN] Missing or invalid Authorization header");
                sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Missing token");
                return;
            }

            String token = h.substring(7);
            if (!jwtService.isTokenValid(token)) {
                log.warn("[M01-TOKEN] Invalid or expired token");
                sendErrorResponse(res, HttpStatus.UNAUTHORIZED, "Session expired or invalid please login again");
                return;
            }

            // 4) Extract claims and set authentication
            Claims claims = jwtService.extractAllClaims(token);
            String email = claims.getSubject();
            @SuppressWarnings("unchecked")
            var authList = (List<Object>) claims.get("auth");
            var authorities = (authList == null ? List.<org.springframework.security.core.authority.SimpleGrantedAuthority>of()
                    : authList.stream().map(Object::toString)
                    .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).toList());

            var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    email, null, authorities);
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[M01-AUTH] SecurityContext set: principal={} authorities={}", email, authorities.size());

            // 5) Continue
            chain.doFilter(req, res);
            log.info("[M01-END] Filtered request successfully for principal={}", email);

        } catch (SecurityException se) {
            log.warn("[M01-ERR] SecurityException: {}", se.getMessage());
            sendErrorResponse(res, HttpStatus.UNAUTHORIZED, se.getMessage());
        } catch (Exception ex) {
            log.error("[M01-EX] Exception: {}: {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            sendErrorResponse(res, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    // ======================= isPublicEndpoint ======================= // [M02]
    private boolean isPublicEndpoint(String path) {
        boolean match = PUBLIC_PATTERNS.stream().anyMatch(p -> p.matcher(path).matches());
        log.info("[M02] isPublicEndpoint check: path={} isPublic={}", path, match);
        return match;
    }

    // ======================= validateApiKey ========================= // [M03]
    private void validateApiKey(HttpServletRequest req) {
        // Check if this is a public endpoint before validating API key
        if (isPublicEndpoint(req.getRequestURI())) {
            log.info("[M03] Public endpoint detected: {} -> skipping API key validation", req.getRequestURI());
            return;
        }

        String provided = req.getHeader("X-API-Key");
        log.info("[M03] Checking X-API-Key header for private endpoint - present={}", (provided != null));
        if (provided == null || provided.isBlank()) {
            log.warn("[M03] Missing or blank API key!");
            throw new SecurityException("Request could not be authenticated.");
        }
        if (!provided.equals(apiKey)) {
            log.warn("[M03] API key mismatch!");
            throw new SecurityException("Request could not be authenticated.");
        }
        log.info("[M03] API key validated OK");
    }

    // ======================= validateJwtToken ======================= // [M04]
    private void validateJwtToken(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        log.info("[M04] validateJwtToken: Authorization header present={}", (authHeader != null));
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[M04] Missing or invalid Authorization header");
            throw new SecurityException("Authentication is required");
        }

        String token = authHeader.substring(7);
        log.info("[M04] Token preview={}", preview(token));

        if (!jwtService.isTokenValid(token)) {
            log.warn("[M04] Token is not valid");
            throw new SecurityException("Request could not be authenticated.");
        }

        setUserAttributes(req, token);
        log.info("[M04] validateJwtToken OK");
    }

    // ======================= setUserAttributes ====================== // [M05]
    private void setUserAttributes(HttpServletRequest req, String token) {
        Claims claims = jwtService.extractAllClaims(token);
        if (claims == null) {
            log.warn("[M05] Claims extracted from token are null");
            throw new SecurityException("Invalid token payload");
        }

        Object userId = claims.get("userId");
        String email = claims.getSubject();
        Object userRole = claims.get("userRole");

        req.setAttribute("userId", userId);
        req.setAttribute("email", email);
        req.setAttribute("userRole", userRole);

        if (claims.get("clientId") != null) {
            req.setAttribute("clientId", claims.get("clientId"));
        }

        log.info("[M05] Set user attributes: userId={}, email={}, userRole={}", userId, email, userRole);
    }

    // ======================= sendErrorResponse ====================== // [M06]
    private void sendErrorResponse(HttpServletResponse res, HttpStatus status, String msg)
            throws IOException {
        log.info("[M06] sendErrorResponse: status={}, msg={}", status.value(), msg);
        res.setStatus(status.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(res.getWriter(), new ApiResponse(false, msg, null));
    }

    // ======================= preview (helper) ======================= // [M07]
    private String preview(String token) {
        if (token == null) return "null";
        int keep = Math.min(6, token.length());
        return token.substring(0, keep) + "...len=" + token.length();
    }


}
