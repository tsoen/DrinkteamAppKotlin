package drinkteam.controleur

import android.content.Context
import drinkteam.metier.Succes
import org.jetbrains.anko.db.*

/**
 * Created by Timothée on 26/06/2016.
 *
 * Permet de manipuler la base de données temporaire des Succes
 * Pour notre application seuls le stockage et la récupération devraient être utilisés
 *
 * Elle extend DAOBase qui représente concretement la base de données
 */
class SuccesDAO

/**
 * Constructeur
 * @param context Contexte de l'activité
 */
	(context: Context)
{
	private var databaseInstance = context.dataBaseHandler
	
	/**
	 * Définit les colonnes de la table des Succes dans la base de données
	 * Modifier ici nécessite aussi de modifier dans la création de la table dans DataBaseHandler
	 */
	companion object
	{
		const val TABLE_NAME = "tableSucces"
		const val CODECATEGORIE = "CodeCategorie"
		const val CODESUCCES = "CodeSucces"
		const val NOM = "Nom"
		const val DESCRIPTION = "Description"
		const val PALIER = "Palier"
		const val FACTEUR = "Facteur"
		const val ANECDOTE = "Anecdote"
		const val IMAGE = "Image"
	}
	
	/**
	 * Retourne la liste des succes de la base
	 * @return Liste de succes
	 */
	val allSucces: List<Succes> = databaseInstance.use {
		select(SuccesDAO.TABLE_NAME).exec { parseList(classParser()) }
	}
	
	/**
	 * Compte le nombre de succes de la base
	 * Evite de faire getAllSucces().size(), léger gain de temps
	 * @return (int) Nombre de succes
	 */
	val succesCount: Int = databaseInstance.use {
		select(SuccesDAO.TABLE_NAME).exec { count }
	}
	
	/**
	 * Ajoute un succes à la base
	 * @param succes Succes à ajouter
	 */
	fun addSucces(succes: Succes)
	{
		databaseInstance.use {
			insert(TABLE_NAME,
				CODECATEGORIE to succes.codecategorie,
				CODESUCCES to succes.codesucces,
				NOM to succes.nom,
				DESCRIPTION to succes.description,
				PALIER to succes.palier,
				FACTEUR to succes.facteur,
				ANECDOTE to succes.anecdote,
				IMAGE to succes.image
			)
		}
	}
	
	/**
	 * Retourne un succes de la base
	 * @param codeCategorie ID de la catégorie du succes
	 * @param codeSucces ID du succes
	 * @return Succes
	 */
	fun getSucces(codeCategorie: Long, codeSucces: Long): Succes
	{
		return databaseInstance.use {
			
			select(TABLE_NAME).whereArgs("($CODECATEGORIE = {codeCat}) and ($CODESUCCES = {codeSuc})", "codeCat" to codeCategorie, "codeSuc" to codeSucces).exec {
				parseSingle(classParser())
			}
		}
	}
	
	/**
	 * Supprime un succes de la base
	 * @param codeCategorie ID de la catégorie du succes
	 * @param codeSucces ID du succes
	 */
	fun deleteSucces(codeCategorie: Long, codeSucces: Long)
	{
		databaseInstance.use {
			
			delete(TABLE_NAME, "($CODECATEGORIE = {codeCat}) and ($CODESUCCES = {codeSuc})", "codeCat" to codeCategorie, "codeSuc" to codeSucces)
		}
	}
	
	/**
	 * Remplace un succes par un nouveau avec le même ID
	 * @param succes Nouveau succes
	 */
	fun updateSucces(succes: Succes)
	{
		/*
		val value = ContentValues()
		value.put(NOM, succes.nom)
		value.put(DESCRIPTION, succes.description)
		value.put(PALIER, succes.palier)
		value.put(FACTEUR, succes.facteur)
		value.put(ANECDOTE, succes.anecdote)
		value.put(IMAGE, succes.image)
		
		this.databaseInstance.update(TABLE_NAME, value, "($CODECATEGORIE,$CODESUCCES) = ?", arrayOf(succes.codecategorie.toString(), succes.codesucces.toString()))
		*/
	}
}