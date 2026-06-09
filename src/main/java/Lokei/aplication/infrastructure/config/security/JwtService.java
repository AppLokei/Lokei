package Lokei.aplication.infrastructure.config.security;

import Lokei.aplication.infrastructure.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final SecurityProperties securityProperties;

    public JwtService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String gerarToken(UsuarioAutenticado usuario) {
        Instant agora = Instant.now();
        Instant expiracao = agora.plus(securityProperties.jwt().expirationMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("uid", usuario.getId())
                .issuedAt(Date.from(agora))
                .expiration(Date.from(expiracao))
                .signWith(getKey())
                .compact();
    }

    public String extrairUsername(String token) {
        return extrairClaims(token).getSubject();
    }

    public boolean tokenValido(String token, UsuarioAutenticado usuario) {
        Claims claims = extrairClaims(token);
        return claims.getSubject().equalsIgnoreCase(usuario.getUsername()) && claims.getExpiration().after(new Date());
    }

    private Claims extrairClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(encodeIfNecessary(securityProperties.jwt().secret())));
    }

    private String encodeIfNecessary(String secret) {
        if (secret.matches("^[A-Za-z0-9+/=]+$") && secret.length() % 4 == 0) {
            return secret;
        }
        return java.util.Base64.getEncoder().encodeToString(secret.getBytes());
    }
}