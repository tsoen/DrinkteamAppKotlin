package drinkteam.metier

/**
 * Created by Timothée on 26/06/2016.
 *
 * Représente les caractéristiques d'un Succes
 */
class Succes
{
	var codecategorie: Long = 0
	var codesucces: Long = 0
	var nom: String = ""
	var description: String? = ""
	var palier: Int = 1
	var facteur: Int = 1
	var anecdote: String = ""
	var image: String = ""
	
	/**
	 * Constructeur neutre
	 * Entre autres utilisé par Firebase pour créé un Succes à partir des données dans la base
	 * (créé un Cocktail vide et utilise les setters)
	 */
	constructor()
	
	constructor(codeCategorie: Long, codeSucces: Long, nom: String, description: String, palier: Int, facteur: Int, anecdote: String = "", image: String = "")
	{
		this.codecategorie = codeCategorie
		this.codesucces = codeSucces
		this.nom = nom
		this.description = description
		this.palier = palier
		this.facteur = facteur
		this.anecdote = anecdote
		this.image = image
	}
}

