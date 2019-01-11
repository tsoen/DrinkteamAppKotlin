package drinkteam.vue

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import drinkteam.layouts.JeuxListItemLayout
import drinkteam.metier.Jeu
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 14/05/2017.
 *
 * Gestionnaire de la GridView qui affiche la liste des Jeux (JeuxPage)
 * Il défini le comportement de la GridView et le contenu de chaque item de la liste
 */
class JeuxGridViewAdapter

/**
 * Constructeur
 * @param context Contexte de l'application
 * @param listJeux Liste des Jeux à afficher
 */
	(private val context: Context, private val listJeux: List<Jeu>) : BaseAdapter()
{
	/**
	 * Compte le nombre d'items affichés
	 * @return Nombre de Jeux affichés
	 */
	override fun getCount(): Int
	{
		return this.listJeux.size
	}
	
	/**
	 * Récupère un Jeu affiché dans la GridView
	 * @param i Position dans la GridView
	 * @return Jeu
	 */
	override fun getItem(i: Int): Any
	{
		return this.listJeux[i]
	}
	
	/**
	 * Renvoi l'ID du jeu sélectionné
	 * @param i Index du jeu dans la liste
	 * @return Code
	 */
	override fun getItemId(i: Int): Long
	{
		return this.listJeux[i].code
	}
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	private inner class ItemHolder
	{
		// nom du Jeu //
		internal var nom: TextView? = null
		
		// nombre de joueurs pour ce Jeu //
		internal var joueurs: TextView? = null
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour afficher chaque item de la GridView
	 * @param position Position de l'item dans la GridView
	 * @param view Vue de l'item
	 * @param viewGroup Vue de la GridView
	 * @return Vue de l'item
	 */
	override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View
	{
		val layout = JeuxListItemLayout()
		val gridView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = ItemHolder()
		
		// nom du Jeu //
		holder.nom = layout.nomJeu
		holder.nom!!.text = listJeux[position].nom
		
		// nombre de joueurs pour ce Jeu //
		holder.joueurs = layout.joueursJeu
		holder.joueurs!!.text = Integer.toString(listJeux[position].joueurs) + " joueurs"
		
		return gridView
	}
}
