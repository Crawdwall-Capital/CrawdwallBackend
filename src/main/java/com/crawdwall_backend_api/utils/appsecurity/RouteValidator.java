package com.crawdwall_backend_api.utils.appsecurity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class RouteValidator {


    public static final List<Pattern> openApiEndpoints = List.of(
            // existing public route(s)

            Pattern.compile("^/magic/public/.*$"),
            Pattern.compile("^/api/v1/admin/public/.*$"),

            Pattern.compile("^/api/v1/utilities/public/.*$"),

            // Add more public routes as needed


            // --- Swagger UI (springdoc 2.x) ---
            // UI entry points
            Pattern.compile("^/swagger-ui\\.html$"),
            Pattern.compile("^/swagger-ui/?$"),
            Pattern.compile("^/swagger-ui/.*$"),

            // OpenAPI spec + config (default /v3)
            Pattern.compile("^/v3/api-docs$"),
            Pattern.compile("^/v3/api-docs/.*$"),
            Pattern.compile("^/v3/api-docs\\.yaml$"),
            Pattern.compile("^/v3/api-docs/swagger-config$"),

            // If you mapped docs to /api-docs in application.yml
            Pattern.compile("^/api-docs$"),
            Pattern.compile("^/api-docs/.*$")
    );


//    public Predicate<ServerHttpRequest> isSecured =
//            request -> openApiEndpoints
//                    .stream()
//                    .noneMatch(pattern -> pattern.matcher(request.getPath().toString()).matches());

    // public = matches any open endpoint
    public final Predicate<HttpServletRequest> isPublic =
            req -> openApiEndpoints.stream()
                    .anyMatch(p -> p.matcher(req.getRequestURI()).matches());

    // secured = NOT public
    public final Predicate<HttpServletRequest> isSecured =
            req -> !isPublic.test(req);
}


