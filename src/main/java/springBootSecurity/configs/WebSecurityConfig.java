package springBootSecurity.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import springBootSecurity.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final SuccessUserHandler successUserHandler;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, @Lazy UserService userService) {
        this.successUserHandler = successUserHandler;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorization -> authorization
                .requestMatchers("/formRegistration", "/index").not().fullyAuthenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/").permitAll().anyRequest().authenticated())
                .formLogin(form -> form.successHandler(successUserHandler).permitAll())
                .logout(LogoutConfigurer::permitAll)
                .userDetailsService(userDetailsService());
        return http.build();
    }

    // аутентификация inMemory
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}