package sol.auth.security.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import sol.auth.core.entity.User;
import sol.auth.core.service.AuthorizationService;
import sol.auth.core.service.UserService;
import sol.auth.security.principal.AuthUserPrincipal;

public class AuthUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final AuthorizationService authorizationService;

    public AuthUserDetailsService(UserService userService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found"));

        Set<String> permissions = authorizationService.getPermissions(user).stream()
                .map(x -> x.getPermissionName()).collect(Collectors.toSet());

         Collection<GrantedAuthority> authorities =
            permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            return new AuthUserPrincipal(user, authorities);           
    }

}
