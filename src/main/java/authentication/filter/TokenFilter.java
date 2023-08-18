package authentication.filter;

import authentication.error.TokenNotFoundError;
import authentication.repository.ITokenRepository;
import authentication.utils.TokenUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TokenFilter implements Filter {

    private final TokenUtil tokenUtil;
    private final ITokenRepository tokenRepo;

    @Autowired
    public TokenFilter(TokenUtil tokenUtil, ITokenRepository tokenRepo) {
        this.tokenUtil = tokenUtil;
        this.tokenRepo = tokenRepo;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init() method has been invoked.");
        log.info("Filter name is: {}", filterConfig.getFilterName());
        log.info("ServletContext name is {}", filterConfig.getServletContext());
        log.info("init() method is ended.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter() method is started --> token filter.");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        // HttpServletResponse res  = (HttpServletResponse) servletResponse; uygulamanın requeste verdiği response'a dair bir işlemimiz olursa kullanırız.
        // veya uygulama response vermeden önce burada response'a veri ekleyip ardından doFilter diyerek gerisini uygulamaya bırakabiliriz.
        log.info("Logging Request: {} : {}", req.getMethod(), req.getRequestURI());
        log.info("Logging Request: from {}, req is: {}", req.getHeader("origin"), req);

        // check if my-token header exists, expired or exists in DB:
        String myTokenHeader = req.getHeader("my-token");

        log.info("TOKEN in REQUEST: {}", myTokenHeader);

        // Browser sends option requests to check if cors is allowed. That is not the business of this filter. Let it go.
        // CorsConfig deals with this issue.
        if("OPTIONS".equals(req.getMethod())) {
            // the following 3 headers must be included in response so that browser understands what requests are allowed for cors.
//            res.addHeader("Access-Control-Allow-Headers", "*");
//            res.addHeader("Access-Control-Allow-Methods", "*");
//            res.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            filterChain.doFilter(req, res);
            return;
        }

        if (myTokenHeader == null ||
                tokenUtil.isTokenExpired(myTokenHeader) ||
                tokenRepo.findToken(myTokenHeader).orElse(null) == null) {

            log.info("expire? {}", tokenUtil.isTokenExpired(myTokenHeader));
            log.info("exists? {}", tokenRepo.findToken(myTokenHeader).orElse(null) == null);

            log.warn("Logging Request: my-token header not found or invalid.");

            throw new ServletException("servlet exception");
            //throw new TokenNotFoundError();
        }

        filterChain.doFilter(req, servletResponse);
        log.info("Response: {}, {}", res.getHeader("Access-Control-Allow-Origin"), res.getHeaderNames());
        log.info("doFilter() method is ended --> token filter.");
    }

    @Override
    public void destroy() {
        log.info("destroy() method is invoked.");
    }
}
