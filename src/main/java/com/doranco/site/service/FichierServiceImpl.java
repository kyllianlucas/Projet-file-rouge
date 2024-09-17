package com.doranco.site.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FichierServiceImpl implements FichierService {

	@Override
	public String téléverserImage(String chemin, MultipartFile fichier) throws IOException {
		
		String nomFichierOriginal = fichier.getOriginalFilename();
		String idAléatoire = UUID.randomUUID().toString();
		String nomFichier = idAléatoire.concat(nomFichierOriginal.substring(nomFichierOriginal.lastIndexOf('.')));
		String cheminFichier = chemin + File.separator + nomFichier;
		
		File dossier = new File(chemin);
		if(!dossier.exists()) {
			dossier.mkdir();
		}
		
		Files.copy(fichier.getInputStream(), Paths.get(cheminFichier));
		
		return nomFichier;
	}

	@Override
	public InputStream obtenirRessource(String chemin, String nomFichier) throws FileNotFoundException {
		String cheminFichier = chemin + File.separator + nomFichier;
		
		InputStream fluxEntrant = new FileInputStream(cheminFichier);
		
		return fluxEntrant;
	}

}
