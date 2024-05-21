package Pegas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            http
                    .authorizeHttpRequests(request-> request
                            .anyRequest().authenticated())
                    .sessionManagement(config-> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .csrf(CsrfConfigurer::disable)
                    .oauth2ResourceServer(i-> i.jwt(jwt-> {
                        var jwtAuthenticationConverter= new JwtAuthenticationConverter();
                        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter);
                    }));

        return http.build();
    }
}
