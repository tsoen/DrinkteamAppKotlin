package drinkteam.vue

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import drinkteam.layouts.CocktailsListItemLayout
import drinkteam.metier.Cocktail
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 17/05/2017.
 *
 * Gestionnaire de la GridView qui affiche la liste des Cocktails (CocktailsPage)
 * Il défini le comportement de la GridView et le contenu de chaque item de la liste
 */
class CocktailsGridViewAdapter

/**
 * Constructeur
 * @param context Contexte de l'application
 * @param listeCocktails Liste des cocktails à afficher
 */
	(private val context: Context, private val listeCocktails: List<Cocktail>) : BaseAdapter()
{
	/**
	 * Compte le nombre d'items affichés
	 * @return Nombre de cocktails affichés
	 */
	override fun getCount(): Int
	{
		return this.listeCocktails.size
	}
	
	/**
	 * Récupère un Cocktail affiché dans la GridView
	 * @param i Position dans la GridView
	 * @return Cocktail
	 */
	override fun getItem(i: Int): Any
	{
		return this.listeCocktails[i]
	}
	
	/**
	 * Récupère l'identifiant d'un Cocktail affiché dans la GridView
	 * @param i Position dans la GridView
	 * @return Code du Cocktail
	 */
	override fun getItemId(i: Int): Long
	{
		return this.listeCocktails[i].code
	}
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	private inner class ItemHolder
	{
		// nom du Cocktail //
		internal var nom: TextView? = null
		
		// alcool principal du Cocktail //
		internal var alcool: TextView? = null
		
		// image du Cocktail //
		internal var icon: ImageView? = null
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
		val layout = CocktailsListItemLayout()
		val gridView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = ItemHolder()
		
		// nom du Cocktail //
		holder.nom = layout.nomCocktail
		holder.nom!!.text = listeCocktails[position].nom
		
		// alcool principale du Cocktail //
		holder.alcool = layout.alcoolCocktail
		holder.alcool!!.text = listeCocktails[position].alcool
		
		// image du Cocktail //
		holder.icon = layout.iconCocktail
		var icon = listeCocktails[position].image
		if (listeCocktails[position].image == "")
		{
			icon = "default_icon"
		}
		
		// rend l'image du Cocktail "arrondie" //
		val iconID = this.context.resources.getIdentifier(icon, "drawable", context.packageName)
		val imageBit = BitmapFactory.decodeResource(this.context.resources, iconID)
		holder.icon!!.setImageBitmap(imageBit)
		val roundedBitmap = RoundedBitmapDrawableFactory.create(context.resources, imageBit)
		roundedBitmap.cornerRadius = 200f
		roundedBitmap.setAntiAlias(true)
		holder.icon!!.setImageDrawable(roundedBitmap)
		
		return gridView
	}
}
