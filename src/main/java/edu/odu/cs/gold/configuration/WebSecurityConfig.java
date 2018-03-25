package edu.odu.cs.gold.configuration;

import edu.odu.cs.gold.model.RoleType;
import edu.odu.cs.gold.repository.UserRepository;
import edu.odu.cs.gold.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.lang.Object.*;
import java.util.List;
import java.util.ArrayList;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.*;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;
    private User.UserBuilder user;
    private RoleType roleType;


    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection< Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role:roles) {

            authorities.add(new SimpleGrantedAuthority(roleType.getAccessLevelName()));
            role.getClass().getResourceAsStream(roleType.getAccessLevelName())

                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::);
        }

        return authorities;
    }

    @Bean
    public UserDetailsService userDetailsService(UserDetails userDetails) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(userDetails);


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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.authorizeRequests().antMatchers("/**").permitAll();

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
                        "/user/register/**",
                        "/user/confirm/**")
                .permitAll()
                .antMatchers("/settings/**")
                .hasRole("admin")

                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .httpBasic();

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
