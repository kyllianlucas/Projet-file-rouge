package com.doranco.site;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.doranco.site.repository.RoleRepository;
import com.doranco.site.model.Role;
import com.doranco.site.config.AppConfig;

@SpringBootApplication
public class EcommerceApp implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepo;

	 @Bean
	 public RestTemplate restTemplate() {
	      return new RestTemplate();
	 }
	
	public static void main(String[] args) {
		SpringApplication.run(EcommerceApp.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role adminRole = new Role();
			adminRole.setIdRole(AppConfig.ID_ADMIN);
			adminRole.setNomRole("ADMIN");

			Role userRole = new Role();
			userRole.setIdRole(AppConfig.ID_UTILISATEUR);
			userRole.setNomRole("USER");

			List<Role> roles = List.of(adminRole, userRole);

			List<Role> savedRoles = roleRepo.saveAll(roles);

			savedRoles.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}