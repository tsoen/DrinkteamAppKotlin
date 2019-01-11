package drinkteam.metier

/**
 * Created by Timothée on 26/06/2016.
 *
 * Représente les caractéristiques d'un Cocktail
 */
class Cocktail
{
	var code: Long = 0
	var nom: String = ""
	var recette: String = ""
	var alcool: String = ""
	var anecdote: String = ""
	var image: String = ""
	
	/**
	 * Constructeur neutre
	 * Entre autres utilisé par Firebase pour créé un Cocktail à partir des données dans la base (créé un Cocktail vide et utilise les setters)
	 */
	constructor()
	
	constructor(code: Long, nom: String, recette: String, alcool: String = "", anecdote: String = "", image: String = "")
	{
		this.code = code
		this.nom = nom
		this.recette = recette
		this.alcool = alcool
		this.anecdote = anecdote
		this.image = image
	}
}

