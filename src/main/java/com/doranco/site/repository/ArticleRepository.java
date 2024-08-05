package com.doranco.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	List<Article> findBySousCategorieId(Long sousCategorieId);

}
