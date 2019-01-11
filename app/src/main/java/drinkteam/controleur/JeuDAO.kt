package drinkteam.controleur

import android.content.Context
import drinkteam.metier.Jeu
import org.jetbrains.anko.db.*

/**
 * Created by Timothée on 26/06/2016.
 *
 * Permet de manipuler la base de données temporaire des Jeux
 * Pour notre application seuls le stockage et la récupération devraient être utilisés
 *
 * Elle extend DAOBase qui représente concretement la base de données
 */
class JeuDAO

/**
 * Constructeur
 * @param context Contexte de l'activité
 */
	(context: Context)
{
	private var databaseInstance = context.dataBaseHandler
	
	/**
	 * Définit les colonnes de la table des Jeux dans la base de données
	 * Modifier ici nécessite aussi de modifier dans la création de la table dans DataBaseHandler
	 */
	companion object
	{
		const val TABLE_NAME = "tableJeux"
		const val CODE = "Code"
		const val NOM = "Nom"
		const val JOUEURS = "Joueurs"
		const val REGLES = "Regles"
		const val ANECDOTE = "Anecdote"
		const val IMAGE = "Image"
	}
	
	/**
	 * Retourne la liste des jeux de la base
	 * @return Liste de jeux
	 */
	val allJeux: List<Jeu> = databaseInstance.use {
		select(JeuDAO.TABLE_NAME).exec { parseList(classParser()) }
	}
	
	/**
	 * Compte le nombre de jeux de la base
	 * Evite de faire getAllJeux().size(), léger gain de temps
	 * @return (int) Nombre de jeux
	 */
	val jeuxCount: Int = databaseInstance.use {
		select(JeuDAO.TABLE_NAME).exec { count }
	}
	
	/**
	 * Ajoute un jeu à la base
	 * @param jeu Jeu à ajouter
	 */
	fun addJeu(jeu: Jeu)
	{
		databaseInstance.use {
			insert(TABLE_NAME,
				CODE to jeu.code,
				NOM to jeu.nom,
				JOUEURS to jeu.joueurs,
				REGLES to jeu.regles,
				ANECDOTE to jeu.anecdote,
				IMAGE to jeu.image
			)
		}
	}
	
	/**
	 * Retourne un jeu de la base
	 * @param id ID du jeu
	 * @return Jeu
	 */
	fun getJeu(id: Long): Jeu
	{
		return databaseInstance.use {
			
			select(TABLE_NAME).whereArgs("$CODE = {code}", "code" to id).exec { parseSingle(classParser()) }
		}
	}
	
	/**
	 * Supprime un jeu de la base
	 * @param id ID du jeu
	 */
	fun deleteJeu(id: Long)
	{
		databaseInstance.use {
			
			delete(TABLE_NAME, "$CODE = {code}", "code" to id)
		}
	}
	
	/**
	 * Remplace un jeu par un nouveau avec le même ID
	 * @param jeu Nouveau jeu
	 */
	fun updateJeu(jeu: Jeu)
	{
		/*
		databaseInstance.use {
			
			val value = ContentValues()
			value.put(NOM, jeu.nom)
			value.put(JOUEURS, jeu.joueurs)
			value.put(REGLES, jeu.regles)
			value.put(ANECDOTE, jeu.anecdote)
			value.put(IMAGE, jeu.image)
			
			this.databaseInstance.update(TABLE_NAME, value, CODE + " = ?", arrayOf(jeu.code.toString()))
		}
		*/
	}
}