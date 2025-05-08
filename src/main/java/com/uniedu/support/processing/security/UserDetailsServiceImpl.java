package sasdevs.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import sasdevs.backend.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(u -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    u.getRoles().forEach(role ->
                            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().toString()))
                    );

                    return new User(u.getEmail(), u.getPassword(), authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException(email + " was not found!"));
    }

}
