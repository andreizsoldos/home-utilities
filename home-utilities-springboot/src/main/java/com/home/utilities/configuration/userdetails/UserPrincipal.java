package com.home.utilities.configuration.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.utilities.entity.AccountStatus;
import com.home.utilities.entity.Gender;
import com.home.utilities.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private Long id;

    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String firstName;

    @JsonIgnore
    private String lastName;

    @JsonIgnore
    private Gender gender;

    @JsonIgnore
    private AccountStatus status;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(final User user) {
        final Set<GrantedAuthority> authorities = Stream.of(user.getRole())
              .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
              .collect(Collectors.toSet());
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword(),
              user.getFirstName(), user.getLastName(), user.getGender(), user.getStatus(), authorities);
    }

    public static UserPrincipal getCurrentUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return (status != AccountStatus.EXPIRED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return (status != AccountStatus.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (status != AccountStatus.DISABLED);
    }
}
