package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.doranco.site.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
