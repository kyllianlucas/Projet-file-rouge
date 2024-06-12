package com.doranco.site;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.doranco.site.config.SecurityConfig;

@SpringBootTest
class EcommerceAppTests {

	@Test
	@Profile("test")
	void contextLoads() {
	}

}
