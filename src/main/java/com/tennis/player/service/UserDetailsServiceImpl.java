package com.tennis.player.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tennis.player.model.AppUser;
import com.tennis.player.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	Optional<AppUser> optionalAppUser = appUserRepository.findByUsername(username);
	AppUser appUser = optionalAppUser
		.orElseThrow(() -> new UsernameNotFoundException("Unknown username : " + username));
	return new User(appUser.getUsername(), appUser.getPassword(), appUser.isEnabled(), true, true, true,
		getAuthorities());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
	return Collections.singletonList(new SimpleGrantedAuthority("USER"));

    }

}
