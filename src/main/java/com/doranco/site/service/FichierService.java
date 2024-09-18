package com.doranco.site.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;


public interface FichierService {
    
    String televerserImage(String chemin, MultipartFile fichier) throws IOException;
    
    InputStream obtenirRessource(String chemin, String nomFichier) throws FileNotFoundException;

}