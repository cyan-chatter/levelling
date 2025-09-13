package com.levelling.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private Date createdAt = new Date();

    private List<String> roles;

    private Currencies currencies = new Currencies();

    private Set<String> ownedItemIds = new HashSet<>();

    // --- UserDetails Methods Implementation ---

    /**
     * Returns the authorities granted to the user. We map our simple role strings
     * (e.g., "ROLE_USER") to Spring Security's GrantedAuthority objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * For this application, accounts do not expire.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * For this application, accounts are never locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * For this application, credentials (password) do not expire.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * For this application, users are enabled by default upon creation.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- Inner Currencies Class ---

    @Data
    @NoArgsConstructor
    public static class Currencies {
        private long points = 0;
        private int yellowGems = 0;
        private int royalBlueGems = 0;
        private int crimsonRedGems = 0;
        private int emeraldGreenGems = 0;
    }
}