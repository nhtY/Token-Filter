package authentication.config;

import authentication.filter.AuthorizationFilter;
import authentication.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FilterBeanConfig {

    private final AuthorizationFilter authorizationFilter;

    private final TokenFilter tokenFilter;

    @Autowired
    public FilterBeanConfig( AuthorizationFilter authorizationFilter, TokenFilter tokenFilter) {
        this.authorizationFilter = authorizationFilter;
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public FilterRegistrationBean<TokenFilter> filterTokenBean() {
        FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(tokenFilter);
        registrationBean.addUrlPatterns("/api/profile/*", "/api/user/*"); // "/api/profile/*" "/api/user/*"
        registrationBean.setOrder(2);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> adminFilter() {
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/api/user/admin/*", "/api/products/*");

        // we have some exemptions (not admin but can do some operations):
        registrationBean.getFilter()
                .addAllowedUriAndMethods("/api/products/", Arrays.asList("GET"));

        registrationBean.setOrder(3);

        return registrationBean;
    }
}
