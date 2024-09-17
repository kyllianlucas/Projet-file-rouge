package com.doranco.site.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	Page<Article> findByProductNameLike(String keyword, Pageable pageDetails);

}
