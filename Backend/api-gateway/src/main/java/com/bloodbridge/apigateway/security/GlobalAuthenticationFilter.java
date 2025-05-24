package com.bloodbridge.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.lang.NonNull;

@Component
public class GlobalAuthenticationFilter implements WebFilter {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        String method = request.getMethod().name();
        String authHeader = request.getHeaders().getFirst("Authorization");
        
        // Allow public endpoints
        if (path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange.getResponse());
        }
        
        String token = authHeader.substring(7);
        
        String role;
        try {
            // First validate the token
            boolean isValid = jwtUtils.validateJwtToken(token);
            if (!isValid) {
                return unauthorized(exchange.getResponse());
            }
            
            role = jwtUtils.getRoleFromToken(token);
        } catch (Exception e) {
            return unauthorized(exchange.getResponse());
        }
        
        // Role-based restrictions
        if (path.startsWith("/api/donors")) {
            // Only ADMIN can approve or reject donors
            if (method.equals("PUT") && (path.matches(".*/approve$") || path.matches(".*/reject$"))) {
                if (!role.equals("ADMIN")) {
                    return forbidden(exchange.getResponse());
                }
            } else if (method.equals("PUT") && path.matches(".*/\\d+$")) {
                // Regular updates can be done by USER
                if (!role.equals("USER")) {
                    return forbidden(exchange.getResponse());
                }
            } else if (method.equals("DELETE")) {
                if (!role.equals("USER")) {
                    return forbidden(exchange.getResponse());
                }
            } else if (method.equals("GET")) {                
                if (!role.equals("USER") && !role.equals("ADMIN")) {
                    return forbidden(exchange.getResponse());
                }
            } else if (method.equals("POST")) {
                if (!role.equals("USER")) {
                    return forbidden(exchange.getResponse());
                }
            }
        } else if (path.startsWith("/api/requests")) {
            // Only ADMIN can approve or reject requests
            if (method.equals("PUT") && (path.matches(".*/approve$") || path.matches(".*/reject$"))) {
                if (!role.equals("ADMIN")) {
                    return forbidden(exchange.getResponse());
                }
            } else if (method.equals("PUT") || method.equals("POST") || method.equals("DELETE") || method.equals("GET")) {
                // Regular operations on requests can be done by USER
                if (!role.equals("USER")) {
                    return forbidden(exchange.getResponse());
                }
            }
        } else if (path.startsWith("/api/inventory")) {
            if (!role.equals("ADMIN")) {
                return forbidden(exchange.getResponse());
            }
        } else if (path.startsWith("/api/notifications")) {
            if (!role.equals("USER") && !role.equals("ADMIN")) {
                return forbidden(exchange.getResponse());
            }        }
        
        return chain.filter(exchange);
    }
    
    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> forbidden(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
