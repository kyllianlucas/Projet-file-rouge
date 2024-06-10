package com.doranco.site.controller;

import com.doranco.site.model.ArticleCommande;
import com.doranco.site.service.ArticleCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/article-commande")
public class ArticleCommandeController {

    @Autowired
    private ArticleCommandeService articleCommandeService;

    @GetMapping("/all")
    public String getAllArticlesCommande(Model model) {
        List<ArticleCommande> articlesCommande = articleCommandeService.getAllArticlesCommande();
        model.addAttribute("articlesCommande", articlesCommande);
        return "articlesCommande";
    }

    @GetMapping("/{id}")
    public String getArticleCommandeById(@PathVariable("id") Long id, Model model) {
        ArticleCommande articleCommande = articleCommandeService.getArticleCommandeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article commande ID: " + id));
        model.addAttribute("articleCommande", articleCommande);
        return "articleCommande";
    }

    @PostMapping("/add")
    public String addArticleCommande(@Valid @ModelAttribute("articleCommande") ArticleCommande articleCommande) {
        articleCommandeService.saveArticleCommande(articleCommande);
        return "redirect:/article-commande/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteArticleCommande(@PathVariable("id") Long id) {
        articleCommandeService.deleteArticleCommande(id);
        return "redirect:/article-commande/all";
    }
}
