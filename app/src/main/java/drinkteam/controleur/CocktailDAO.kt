package drinkteam.controleur

import android.content.Context
import drinkteam.metier.Cocktail
import org.jetbrains.anko.db.*

/**
 * Created by Timothée on 26/06/2016.
 *
 * Permet de manipuler la base de données temporaire des Cocktails
 * Pour notre application seuls le stockage et la récupération devraient être utilisés
 *
 * Elle extend DAOBase qui représente concretement la base de données
 */
class CocktailDAO

/**
 * Constructeur
 * @param context Contexte de l'activité
 */
	(context: Context)
{
	private var databaseInstance = context.dataBaseHandler
	
	/**
	 * Définit les colonnes de la table des Cocktails dans la base de données
	 * Modifier ici nécessite aussi de modifier dans la création de la table dans DataBaseHandler
	 */
	companion object
	{
		const val TABLE_NAME = "tableCocktails"
		const val CODE = "Code"
		const val NOM = "Nom"
		const val RECETTE = "Recette"
		const val ALCOOL = "Alcool"
		const val ANECDOTE = "Anecdote"
		const val IMAGE = "Image"
	}
	
	/**
	 * Retourne la liste des cocktails de la base
	 * @return Liste de cocktails
	 */
	val allCocktails: List<Cocktail> = databaseInstance.use {
		select(CocktailDAO.TABLE_NAME).exec { parseList(classParser()) }
	}
	
	val distinctAlcool: List<String> = databaseInstance.use {
		select(CocktailDAO.TABLE_NAME, CocktailDAO.ALCOOL).distinct().exec { parseList(classParser()) }
	}
	
	/**
	 * Compte le nombre de cocktails de la base
	 * Evite de faire getAllCocktails().size(), léger gain de temps
	 * @return (int) Nombre de cocktails
	 */
	val cocktailCount: Int = databaseInstance.use {
		select(CocktailDAO.TABLE_NAME).exec { count }
	}
	
	/**
	 * Ajoute un cocktail à la base
	 * @param cocktail Cocktail à ajouter
	 */
	fun addCocktail(cocktail: Cocktail)
	{
		databaseInstance.use {
			insert(TABLE_NAME,
				CODE to cocktail.code,
				NOM to cocktail.nom,
				RECETTE to cocktail.recette,
				ALCOOL to cocktail.alcool,
				ANECDOTE to cocktail.anecdote,
				IMAGE to cocktail.image
			)
		}
	}
	
	/**
	 * Retourne un cocktail de la base
	 * @param id ID du cocktail à récupérer
	 * @return Cocktail
	 */
	fun getCocktail(id: Long): Cocktail
	{
		return databaseInstance.use {
			
			select(TABLE_NAME).whereArgs("$CODE = {code}", "code" to id).exec { parseSingle(classParser()) }
		}
	}
	
	/**
	 * Supprime un cocktail de la base
	 * @param id ID du cocktail
	 */
	fun deleteCocktail(id: Long)
	{
		databaseInstance.use {
			
			delete(TABLE_NAME, "$CODE = {code}", "code" to id)
		}
	}
	
	/**
	 * Remplace un cocktail par un nouveau avec le même ID
	 * @param cocktail Nouveau cocktail
	 */
	fun updateCocktail(cocktail: Cocktail)
	{
		/*
		val value = ContentValues()
		value.put(NOM, cocktail.nom)
		value.put(RECETTE, cocktail.recette)
		value.put(ALCOOL, cocktail.alcool)
		value.put(ANECDOTE, cocktail.anecdote)
		value.put(IMAGE, cocktail.image)
		
		this.database.update(TABLE_NAME, value, CODE + " = ?", arrayOf(cocktail.code.toString()))
		*/
	}
}