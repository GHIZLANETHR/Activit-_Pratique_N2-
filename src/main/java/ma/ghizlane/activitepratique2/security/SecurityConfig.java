package ma.ghizlane.activitepratique2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author pc
 **/

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        PasswordEncoder passwordEncoder=passwordEncoder();
        UserDetails user=
                User.withUsername("user")
                        .password(passwordEncoder.encode("1234"))
                        .roles("USER")
                        .build();

        UserDetails admin=
                User.withUsername("admin")
                        .password(passwordEncoder.encode("1234"))
                        .roles("USER","ADMIN")
                        .build();

        return new InMemoryUserDetailsManager(user,admin);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/webjars/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/delete", "/formProducts").hasRole("ADMIN")
                        .requestMatchers("/index").hasRole("USER")
                        .anyRequest().authenticated()
                )
//                .formLogin(Customizer.withDefaults());
                .formLogin(form->form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout->logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
        );

        return http.build();
    }
}
