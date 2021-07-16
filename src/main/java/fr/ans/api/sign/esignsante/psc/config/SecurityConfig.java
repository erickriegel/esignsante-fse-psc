package fr.ans.api.sign.esignsante.psc.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Web security configuration.
     *
     * @param web WebSecurity
     */
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/**"); // turn off web security for all requests
    }
    

    /**
     * Http securityCSRF security configuration.
     * permit all and CSRF security configuration.
     *
     * @param http HttpSecurity
     */
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // storing the CSRF token in the cookie as required by swagger
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        
        //CJU TEST 
       // http.authorizeRequests().antMatchers("/").permitAll();
       //http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    }

}