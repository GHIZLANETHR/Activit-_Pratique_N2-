package ma.ghizlane.activitepratique2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author pc
 **/
@Configuration
public class KeycloakConfig {

    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) {
                OidcUser user = super.loadUser(userRequest);

                Collection<GrantedAuthority> mappedAuthorities = user.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getAuthority().toUpperCase()))
                        .collect(Collectors.toList());

                return new org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser(
                        mappedAuthorities,
                        user.getIdToken(),
                        user.getUserInfo()
                );
            }
        };
    }
}