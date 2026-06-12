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
        String secret = securityProperties.jwt().secret();

        secret = secret.replace("\"", "");
        byte[] keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        byte[] padded = new byte[32];
        System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
        return Keys.hmacShaKeyFor(padded);
    }

}