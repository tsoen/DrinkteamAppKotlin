package drinkteam.metier

/**
 * Created by Timothée on 07/06/2017.
 *
 * Représente les caractéristiques d'un Utilisateur
 */
class User
{
	var id: String? = null
	var provider: String? = null
	var providerid: String? = null
	var email: String? = null
	var name: String? = null
	var titre: String? = null
	var ambiance: String? = null
	
	/**
	 * Constructeur neutre
	 * Entre autres utilisé par Firebase pour créé un Utilisateur à partir des données dans la base
	 * (créé un Cocktail vide et utilise les setters)
	 */
}
