package com.doranco.site.config;

public class AppConfig {
	
	public static final String NUMERO_PAGE = "0";
	public static final String TAILLE_PAGE = "2";
	public static final String TRIER_CATEGORIES_PAR = "categoryId";
	public static final String TRIER_ARTICLE_PAR = "articleId";
	public static final String TRIER_UTILISATEURS_PAR = "userId";
	public static final String TRIER_COMMANDES_PAR = "totalAmount";
	public static final String ORDONNER_PAR = "asc";
	public static final Long ID_ADMIN = 101L;
	public static final Long ID_UTILISATEUR = 102L;
	public static final long VALIDITE_TOKEN_JWT = 5 * 60 * 60;
	public static final String[] URL_PUBLIC = { "/v3/api-docs/**", "/swagger-ui/**", "/api/inscription", "/api/connexion", "/api/produit/all" };
	public static final String[] URL_UTILISATEUR = { "/api/public/**" };
	public static final String[] URL_ADMIN = { "/api/admin/**" };
	
}
