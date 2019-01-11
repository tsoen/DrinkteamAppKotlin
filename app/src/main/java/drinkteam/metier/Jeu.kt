package drinkteam.metier

/**
 * Created by Timothée on 26/06/2016.
 *
 * Représente les caractéristiques d'un Jeu
 */
class Jeu
{
	var code: Long = 0
	var nom: String = ""
	var joueurs: Int = 0
	var regles: String = ""
	var anecdote: String = ""
	var image: String = ""
	
	/**
	 * Constructeur neutre
	 * Entre autres utilisé par Firebase pour créé un Jeu à partir des données dans la base
	 * (créé un Cocktail vide et utilise les setters)
	 */
	constructor()
	
	constructor(code: Long, nom: String, joueurs: Int, regles: String, anecdote: String = "", image: String = "")
	{
		this.code = code
		this.nom = nom
		this.joueurs = joueurs
		this.regles = regles
		this.anecdote = anecdote
		this.image = image
	}
}

