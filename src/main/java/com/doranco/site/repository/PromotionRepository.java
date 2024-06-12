package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

}
