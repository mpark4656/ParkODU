package edu.odu.cs.gold.configuration;

import edu.odu.cs.gold.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password("awesome8").roles("ADMIN").build());
        manager.createUser(User.withUsername("user").password("awesome8").roles("USER").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    public UserService userService() {
        return new UserService();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
        /*
        http.authorizeRequests()
                .antMatchers("/",
                        "/index",
                        "/login",
                        "/css/**",
                        "/fonts/**",
                        "/img/**",
                        "/js/**",
                        "/analytics/**",
                        "/floor/**",
                        "/garage/**",
                        "/newuser/**")
                .permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/confirm").permitAll()
                .antMatchers("/settings/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .httpBasic();
        */
        http.csrf().disable(); // Required for Spring Security to work
    }

    // Session Control
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter(){
        return new ConcurrentSessionFilter(sessionRegistry());
    }

    @Bean
    public ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy(){
        return new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy(){
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }
}
