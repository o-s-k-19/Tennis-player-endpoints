package com.tennis.player.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tennis.player.model.Country;
import com.tennis.player.model.Player;


public class PlayerUtilities {

	
	/**
	 * Cette méthode renvoie le pays qui a le plus grand ratio de parties gagnées.
	 * le ratio est determiné en faisant la somme des points des joueurs par pays puis divisé par le nombre.
	 */
	public static Optional<Entry<Country, Double>> getCountryWithBigRatio(List<Player> players) {
		
		Map<Country, Integer> pointsByCountry = new HashMap<>();
		Map<Country, Integer> playersByCountry = new HashMap<>();
		Map<Country, Double> ratioByCountry = new HashMap<>();
		for (Player player : players) {
			Country country = player.getCountry();
			int points = player.getPoints();
			pointsByCountry.put(country, pointsByCountry.getOrDefault(country, 0) + points);
			playersByCountry.put(country, playersByCountry.getOrDefault(country, 0) + 1);
		}
		pointsByCountry.entrySet().forEach(entry -> {
			ratioByCountry.put(entry.getKey(), 
			(double)(entry.getValue()/playersByCountry.get(entry.getKey()))); 
			});
		
		return ratioByCountry.entrySet()
		.stream()
		.max(Map.Entry.comparingByValue());
	}
	
	/**
	 * Cette méthode calcul l'indice moyen corporelle (imc).
	 * Le calcul de l'imc se fait suivant la formule : poids (kg) / taille(en mettre) ** 2
	 * 1 kg = 1000g
	 * 1 m = 100 cm
	 *
	 * */
	public static double getPlayersIMC(List<Player> players) {
		
		double sumOfIMC = players.stream()
				.mapToDouble(p -> calculateIMC(p))
				.sum();
		
		// renvoie la valeur arrondie de la moyenne
		return Math.round((sumOfIMC / players.size())*100.0)/100.0;
	}
	
	private static double calculateIMC(Player p) {
		
		// le poid convertit en kg
		double weight = (double) p.getWeight() / 1000;
		
		// la taille convertit en m
		double height = (double)p.getHeight() / 100;
			
		return weight / Math.pow(height, 2);
		
	}
	
	/**
	 * Cette méthode renvoie la taille médiane des joueurs
	 * Pour calculer la médiane : 
	 * On classe les valeurs de la série statistique dans l'ordre croissant : 
	 * Si le nombre de valeurs est impair, la médiane est la valeur du milieu. 
	 * S'il est pair, la médiane est la demi-somme des deux valeurs du milieu.
	 *
	 * */
	public static double getPlayersHeightMedian(List<Player> players) {
		
		List<Integer> listOfPlayersHeight = players.stream()
				.map(p -> p.getHeight())
				.collect(Collectors.toList());
		
		// trie dans l'ordre croissant 
		Collections.sort(listOfPlayersHeight);
		
		// pair ou impair :
		int sizeOfList = listOfPlayersHeight.size();
		int middleValue = sizeOfList / 2 ;
		
		double result;
		
		// si pair
		if(sizeOfList % 2 == 0) {
			result = ((double)listOfPlayersHeight.get(middleValue) + (double)listOfPlayersHeight.get(middleValue+1))/2.0;
		}else {
			result = listOfPlayersHeight.get(middleValue);
		}
		
		return result;
	}

	
}
