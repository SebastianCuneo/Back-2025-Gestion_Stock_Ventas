package uy.edu.ucu.security.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uy.edu.ucu.security.services.IJWTUtilityService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collections;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final IJWTUtilityService jwtService;

    public JWTAuthorizationFilter(IJWTUtilityService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            // 1) Parseo y verificación del JWT usando tu servicio
            JWTClaimsSet claims = jwtService.parseJWT(token);

            // 2) Construcción de la autenticación Spring
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            claims.getSubject(),    // usuario (subject)
                            null,                  // credenciales (ya validadas)
                            Collections.emptyList()// roles/authorities
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JOSEException | ParseException e) {
            // Token inválido o mal formado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Problema al validar la firma del token
            throw new ServletException("Error validando la firma del JWT", e);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
