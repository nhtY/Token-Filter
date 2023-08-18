package authentication.filter;

import authentication.error.TokenNotFoundError;
import authentication.error.UnauthorizedUser;
import authentication.repository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class AuthorizationFilter extends GenericFilterBean {

    private final IUserRepository userRepo;

    private final HashMap<String, List<String>> allowedUriAndMethods;

    @Autowired
    public AuthorizationFilter(IUserRepository userRepo) {
        this.userRepo = userRepo;
        this.allowedUriAndMethods = new HashMap<>();
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter() method is started --> Authorization filter.");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        log.info("Logging Request: {} : {}", req.getMethod(), req.getRequestURI());
        log.info("Logging Request: from {}, remote host: {}, remote address {}",
                req.getHeader("origin"), req.getRemoteHost(), req.getRemoteAddr());

        String myTokenHeader = req.getHeader("my-token");
        String uri = req.getRequestURI();
        String httpMethod = req.getMethod();

        if("OPTIONS".equals(httpMethod)) {
//            res.addHeader("Access-Control-Allow-Headers", "*");
//            res.addHeader("Access-Control-Allow-Methods", "*");
//            res.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            filterChain.doFilter(req, res);
            return;
        }

        log.info("Logging Request: token: {}", myTokenHeader);

        boolean isPassed = false;
        boolean isAdmin = false;

        // Admin or not, but allowed for some uri and methods
        if (isAllowed(uri, httpMethod)) {
            log.info("Logging Request: USER is allowed for this request.");
            isPassed = true;
        }

        // is this admin
        try {
            if (userRepo.isAdmin(myTokenHeader)) {
                log.info("Logging Request: token belongs to an ADMIN");
                isAdmin = true;
            }
        } catch (Exception err) {
            log.info("Logging Request: error caught {}", err.getMessage());
        }

        if (isPassed || isAdmin) {

            filterChain.doFilter(req, servletResponse);

            log.info("doFilter() method is ended --> Authorization filter.");
            return;
        }

        throw new UnauthorizedUser();
    }

    private boolean isAllowed(String reqUri, String reqMethod) {

        boolean uriMatchFound = false;
        boolean methodMatch = false;

        for (String uri: this.allowedUriAndMethods.keySet()) {
            uriMatchFound = reqUri.matches(uri);

            if (uriMatchFound) {
                methodMatch = this.allowedUriAndMethods.get(uri)
                        .stream().filter(method -> method.equals(reqMethod)).findFirst().isPresent();
                break;
            }
        }
        // TODO ANY request de olmalı...
        return uriMatchFound && methodMatch;

    }

    /**
     * You can add allowed uri and the http methods for that uri
     * @param uri asd/xyz/ means asd/xyz/* and asd/xyz means exactly asd/xyz
     * @param methods GET POST PATCH DELETE etc.
     * @return the same instance so that you can call the method consecutively.
     */
    public AuthorizationFilter addAllowedUriAndMethods(String uri, List<String> methods) {
        if (uri.endsWith("/")) {
            // exact uri or sub-uri's
            this.allowedUriAndMethods.put(uri.substring(0, uri.length() - 1).concat("(.*)"), methods);
        }else {
            // exact uri
            this.allowedUriAndMethods.put(uri, methods);
        }

        return this;
    }
}
