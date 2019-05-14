package hu.elte.szgy.szalain.tudor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class TudorWebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.GET,"/","/extjs/**").permitAll()
                .antMatchers(HttpMethod.GET,"/user/register").permitAll()
                .antMatchers(HttpMethod.POST,"/user/register").permitAll()
                .antMatchers(HttpMethod.GET,"/user/self").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.PUT,"/user/self").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.GET,"/question/all").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.GET,"/question/all/**").hasAnyRole("TUDOR","ADMIN")
                .antMatchers(HttpMethod.GET,"/question/new").hasRole("UGYFEL")
                .antMatchers(HttpMethod.POST,"/question/new").hasRole("UGYFEL")
                .antMatchers(HttpMethod.POST,"/question/new/default").hasRole("UGYFEL")
                .antMatchers(HttpMethod.DELETE,"question/delete/**").hasAnyRole("UGYFEL","ADMIN")
                .antMatchers(HttpMethod.GET,"/answer/all/**").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.POST,"/answer/new/**").hasRole("TUDOR")
                .antMatchers(HttpMethod.GET,"/faq/all").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.GET,"/rating/**").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.POST,"/rating/**").hasAnyRole("UGYFEL","TUDOR","ADMIN")
                .antMatchers(HttpMethod.GET,"/report/all").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/report/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/report/new/**").hasRole("TUDOR")
                .antMatchers(HttpMethod.DELETE,"/user/delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/vote/**").hasRole("TUDOR")
                .antMatchers(HttpMethod.POST,"/vote/new/**").hasRole("TUDOR")
                .antMatchers(HttpMethod.POST,"/vote/**").hasRole("TUDOR")
                .and()
            .csrf().disable()
            .formLogin()
                .loginPage("/login")
                .successForwardUrl( "/user/dispatch" )
                .permitAll()
                .and()
            .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

//  SIMPLE USERSERVICE TO BE USED FOR TESTING ONLY     
     /*UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
*/
		return new TudorUserService();
    }
}
