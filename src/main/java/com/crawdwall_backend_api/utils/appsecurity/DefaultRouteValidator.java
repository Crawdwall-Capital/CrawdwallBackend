package com.crawdwall_backend_api.utils.appsecurity;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class DefaultRouteValidator {

    private static final Logger log = LoggerFactory.getLogger(DefaultRouteValidator.class);

    // Nominee-secured endpoints
    public static final List<Pattern> APPUSER_ENDPOINTS = List.of(

            Pattern.compile("/api/v1/utilities/private.*"),
            Pattern.compile("/api/v1/utilities/private($|\\?.)")
         

    );

    // Admin-secured endpoints
    public static final List<Pattern> ADMIN_ENDPOINTS = List.of(
            Pattern.compile("/api/v1/admin/private.*"),
            Pattern.compile("/api/v1/admin/private($|\\?.)"),
            Pattern.compile("/api/v1/role/admin/private($|\\?.)"),
            Pattern.compile("/api/v1/role/admin/private.*"),
            Pattern.compile("/api/v1/role/admin/private/.*"),
            Pattern.compile("/api/v1/admin/public.*"),
            Pattern.compile("/api/v1/admin/public($|\\?.)")



    );



    public DefaultRouteValidator() {
        log.info("[DRV-INIT] Admin patterns={}, Nominee patterns={}", ADMIN_ENDPOINTS.size(), APPUSER_ENDPOINTS.size());
    }

    public Predicate<HttpServletRequest> isAdminSecured = req -> {
        String uri = req.getRequestURI();
        if (log.isDebugEnabled()) {
            log.info("[DRV-A1] check admin uri={}", uri);
        }
        boolean match = ADMIN_ENDPOINTS.stream().anyMatch(p -> {
            boolean m = p.matcher(uri).matches();
            if (m && log.isDebugEnabled()) {
                log.info("[DRV-A2] matched admin pattern={}", p.pattern());
            }
            return m;
        });
        if (log.isDebugEnabled()) {
            log.info("[DRV-A3] admin match={}", match);
        }
        return match;
    };

    public Predicate<HttpServletRequest> isNomineeSecured = req -> {
        String uri = req.getRequestURI();
        if (log.isDebugEnabled()) {
            log.info("[DRV-N1] check nominee uri={}", uri);
        }
        boolean match = APPUSER_ENDPOINTS.stream().anyMatch(p -> {
            boolean m = p.matcher(uri).matches();
            if (m && log.isDebugEnabled()) {
                log.info("[DRV-N2] matched nominee pattern={}", p.pattern());
            }
            return m;
        });
        if (log.isDebugEnabled()) {
            log.info("[DRV-N3] nominee match={}", match);
        }
        return match;
    };


}
