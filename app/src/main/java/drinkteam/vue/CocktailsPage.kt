package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import drinkteam.Utils
import drinkteam.layouts.CocktailsPageLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 18/06/2016.
 *
 * Page (Fragment) "Les Recettes !" accessible depuis le menu
 * Liste les Cocktails proposés par l'application
 */
class CocktailsPage : Fragment()
{
	private lateinit var layout: CocktailsPageLayout
	
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
		this.layout = CocktailsPageLayout()
		return layout.createView(AnkoContext.create(context as MainActivity, context as MainActivity))
	}
	
	/**
	 * Ajout des éléments après la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		// titre de l'actionBar //
		(activity as MainActivity).setTitreActionBar(arguments!!.getString("titre_actionbar"))
		
		// récupère la liste des cocktails de la base //
		val listAllCocktails = Utils.dbCocktails.allCocktails
		
		// récupère la vue de la liste des cocktails //
		val gridView = layout.gridviewCocktails
		
		val listAlcools = ArrayList(Utils.dbCocktails.distinctAlcool)
		// sélecteur global //
		listAlcools.add(0, "Tous")
		
		// créer la liste déroulante avec les options de tri //
		val dropDownList = layout.cocktailsSpinner
		
		// envoi la liste des alcools à afficher dans le spinner //
		val dropDownListAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listAlcools)
		dropDownList.adapter = dropDownListAdapter
		
		// affiche la liste des cocktails en fonction du tri choisi appelé aussi une fois à la création du fragment //
		dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
		{
			/**
			 * Met à jour la liste des cocktails affichés quand on sélectionne un paramètre de tri
			 * @param parentView Vue du spinner
			 * @param selectedItemView Vue de l'item sélectionné dans le spinner
			 * @param position Position de l'item sélectionné dans le spinner
			 * @param id ID de l'item sélectionné dans le spinner (== position)
			 */
			override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long)
			{
				// par defaut (option "Tous"), tous les cocktails sont affiches //
				var cocktailsToDisplay = ArrayList(listAllCocktails)
				
				// tri si alcool selectionne //
				if (position != 0)
				{
					cocktailsToDisplay = ArrayList(listAllCocktails.filter
					{
						it.alcool == listAlcools[position]
					})
				}
				
				// envoi la liste des cocktails à afficher dans la GridView du fragment //
				val adapter = CocktailsGridViewAdapter(activity!!, cocktailsToDisplay)
				gridView.adapter = adapter
			}
			
			override fun onNothingSelected(parentView: AdapterView<*>)
			{
			}
		}
		
		// affiche automatiquement la dernière position affichée lorsque l'on revient d'une CocktailsFiche //
		gridView.smoothScrollToPosition(gridView.firstVisiblePosition)
		
		// détecte les cliques et affiche la fiche d'un cocktail //
		gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
			
			// passage de l'ID du cocktail qui a été cliqué //
			val bundle = Bundle()
			bundle.putLong("code", id)
			
			// remplace le fragment actuel par le nouveau fragment //
			(activity as MainActivity).replaceFragment(activity as MainActivity, CocktailsFiche(), bundle, true)
		}
		
		// revient à la liste complète quand on appuie sur retour depuis une fiche de cocktail //
		gridView.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
			
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				fragmentManager!!.popBackStackImmediate()
				return@OnKeyListener true
			}
			false
		})
	}
}
