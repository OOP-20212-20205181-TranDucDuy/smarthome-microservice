package org.development.apigateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class ApiValidator {

    public static final List<String> openEndpoints = List.of(
            "api/auth/register",
            "api/auth/signin",
            "api/auth/verify",
            "api/auth/refresh"
    );
    public static final List<String> adminEndpoints = List.of(

    );
    public static final List<String> userEndpoints = List.of(
            "api/user/findOneById"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
    public Predicate<ServerHttpRequest> adminValidateApi =
            request -> adminEndpoints.stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));
    public Predicate<ServerHttpRequest> userValidateApi =
            request -> userEndpoints.stream()
                    .anyMatch(uri -> request.getURI().getPath().contains(uri));
}
