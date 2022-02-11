package in.vineetks.shoppingbee.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Autowired
    @Qualifier("loginService")
    UserDetailsService userDetailsService;
     
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
    
    @Override
   	protected void configure(HttpSecurity http) throws Exception {
   		http.csrf().disable().authorizeRequests()
   			.antMatchers("/admin/**","/static/shoppingbee.com/admin.html/**").access("hasRole('ADMIN')")
   			.antMatchers("/current/**","/static/shoppingbee.com/cart.html/**","/static/shoppingbee.com/account.html/**").access("hasRole('ADMIN') or hasRole('USER')")
   			.antMatchers("/**").permitAll()
	   		.and().formLogin().loginPage("/static/shoppingbee.com/login.html")
	   		.loginProcessingUrl("/login")
	   		.failureUrl("/static/shoppingbee.com/login.html?error=badCredentials")
	   		.defaultSuccessUrl("/static/shoppingbee.com/index.html")
	   		.usernameParameter("username")
	   		.passwordParameter("password")
	   		.and().exceptionHandling().accessDeniedPage("/accessDenied");
   	}
}