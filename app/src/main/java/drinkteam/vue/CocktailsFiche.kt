package drinkteam.vue

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.Utils
import drinkteam.layouts.CocktailsFicheLayout
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 17/05/2017.
 *
 * Page (Fragment) de détails d'un Cocktail
 */
class CocktailsFiche : Fragment()
{
	private lateinit var layout: CocktailsFicheLayout
	
	/**
	 * Création de la vue à partir du layout dédié
	 * @param inflater Objet utilisé pour remplir la vue
	 * @param container Vue parente qui contient la vue
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 * @return Vue créée
	 */
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		// associe le layout au fragment //
		this.layout = CocktailsFicheLayout()
		return layout.createView(AnkoContext.create(context as MainActivity, context as MainActivity))
	}
	
	/**
	 * Ajout des éléments après la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		
		// code du cocktail //
		val code = arguments!!.getLong("code")
		
		// nom du cocktail //
		val nom = layout.nomCocktailFiche
		nom.text = Utils.dbCocktails.getCocktail(code).nom
		
		// vue de l'image de l'icone du cocktail //
		val icon = layout.iconCocktailFiche
		// TODO sélection de l'image du cocktail dans les ressources de l'appli //
		val iconID = context!!.resources.getIdentifier("default_icon", "drawable", context!!.packageName)
		val imageBit = BitmapFactory.decodeResource(context!!.resources, iconID)
		icon.setImageBitmap(Bitmap.createScaledBitmap(imageBit, 120, 120, false))
		
		// recette du cocktail //
		val recette = layout.recetteCocktailFiche
		recette.text = "Recette: \n" + Utils.dbCocktails!!.getCocktail(code).recette
		
		// anecdote du cocktail //
		val anecdote = layout.anecdoteCocktailFiche
		anecdote.text = Utils.dbCocktails.getCocktail(code).anecdote
	}
}
