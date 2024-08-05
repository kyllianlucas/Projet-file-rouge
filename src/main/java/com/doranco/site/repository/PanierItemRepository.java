package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Item;

public interface PanierItemRepository extends JpaRepository<Item,Long>{

}
