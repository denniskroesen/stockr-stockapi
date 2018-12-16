package nl.dkroesen.stockr.stockapi.services;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final int TOKEN_EXPIRATION_TIME = 5 * 60 * 60 * 1000;
    private static final String SIGNING_KEY = "stockr-signature";
    private static final String SCOPES = "scopes";
    private static final String DELIMITER = ",";

    public String generateToken(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(DELIMITER));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(SCOPES, authorities)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return getUsernameFromToken(token).get().equals(userDetails.getUsername()) &&
                tokenIsNotExpired(token);
    }

    private boolean tokenIsNotExpired(String token) {
        final Date expiration = getAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token, Authentication authentication, UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();
        final List<String> scopes = Arrays.asList(claims.get(SCOPES).toString().split(DELIMITER));

        final Collection<? extends GrantedAuthority> authorities =
                scopes.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public Optional<String> getUsernameFromToken(String token) {
        final Claims allClaims = getAllClaims(token);
        return Optional.of(allClaims.getSubject());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
