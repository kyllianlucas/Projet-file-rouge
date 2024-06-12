package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.doranco.site.model.User;
import com.doranco.site.model.Role;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.HashSet;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setPassword("plainPassword");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName(anyString())).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user, "USER");

        assertNotNull(savedUser);
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(1, savedUser.getRoles().size());
        assertTrue(savedUser.getRoles().contains(role));

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveUserRoleNotFound() {
        User user = new User();
        user.setPassword("plainPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName(anyString())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUser(user, "INVALID_ROLE");
        });

        assertEquals("Role not found: INVALID_ROLE", exception.getMessage());

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(roleRepository, times(1)).findByName("INVALID_ROLE");
        verify(userRepository, times(0)).save(user);
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    public void testFindByEmailNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        User foundUser = userService.findByEmail("nonexistent@example.com");

        assertNull(foundUser);

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
