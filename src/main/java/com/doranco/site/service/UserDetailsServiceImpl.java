package com.doranco.site.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;
import com.doranco.site.config.UserInfoConfig;
import com.doranco.site.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service // Ce nom doit correspondre à ce qui est utilisé dans le filtre JWT
public class UserDetailsServiceImpl  implements UserDetailsService {

	@Autowired
	private UtilisateurRepository repoUtilisateur;

	@Override
	public UserDetails loadUserByUsername(String nomUtilisateur) throws UsernameNotFoundException {
		Optional<Utilisateur> utilisateur = repoUtilisateur.findByEmail(nomUtilisateur);
		
		return utilisateur.map(UserInfoConfig::new).orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", nomUtilisateur));
	}
}