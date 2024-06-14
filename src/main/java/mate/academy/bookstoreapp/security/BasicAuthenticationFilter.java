package mate.academy.bookstoreapp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.exceptions.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
public class BasicAuthenticationFilter extends HttpFilter {
    private static final String AUTHORIZATION_SCHEMA_BASIC = "basic";
    private static final int START_OF_TOKEN = 6;
    private final AuthenticationManager authenticationManager;

    @Override
    public void doFilter(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String url = request.getRequestURI();
        if (PublicAvailableEndpoints.getPublicEndpoints().contains(url)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getAuthentication(request);
        if (!authenticationManager.isAuthenticated(authentication)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        SecurityContextHolder.getSecurityContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null;
        }

        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHORIZATION_SCHEMA_BASIC)) {
            return null;
        }

        if (header.equalsIgnoreCase(AUTHORIZATION_SCHEMA_BASIC)) {
            throw new BadCredentialsException("Empty basic authentication token");
        }

        String token = header.substring(START_OF_TOKEN);
        String loginAndPassword = new String(Base64.getDecoder().decode(token));

        int delim = loginAndPassword.indexOf(':');
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }

        String login = loginAndPassword.substring(0, delim);
        String password = loginAndPassword.substring(delim + 1);

        return new UsernamePasswordAuthenticationToken(login, password);
    }
}
