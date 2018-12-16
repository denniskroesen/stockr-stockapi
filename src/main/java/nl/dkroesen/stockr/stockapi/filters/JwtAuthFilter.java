package nl.dkroesen.stockr.stockapi.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import nl.dkroesen.stockr.stockapi.services.TokenProvider;
import nl.dkroesen.stockr.stockapi.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final UserService userDetailsService;

    public JwtAuthFilter(final TokenProvider tokenProvider, final UserService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final Optional<String> token = getAuthToken(httpServletRequest);

        if(token.isPresent()){
            final Optional<String> username = findUsername(token.get());
            if (username.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                setSecurityContext(username.get(), token.get(), httpServletRequest);
            }
        }

    }

    private void setSecurityContext(final String username, final String token, final HttpServletRequest req) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (tokenProvider.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthentication(token, SecurityContextHolder.getContext().getAuthentication(), userDetails);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            logger.info("authenticated user " + username + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private Optional<String> getAuthToken(final HttpServletRequest req) {
        final String header = req.getHeader("Authorization");
        if (header != null && header.startsWith(PREFIX)) {
            return Optional.of(header.replace(PREFIX, ""));
        }
        return Optional.empty();
    }

    private Optional<String> findUsername(final String token) {
        try {
            return tokenProvider.getUsernameFromToken(token);
        } catch (IllegalArgumentException e) {
            logger.error("an error occured during getting username from token", e);
        } catch (ExpiredJwtException e) {
            logger.warn("the token is expired and not valid anymore", e);
        } catch (SignatureException e) {
            logger.error("Authentication Failed. Username or Password not valid.");
        }
        return Optional.empty();
    }
}
