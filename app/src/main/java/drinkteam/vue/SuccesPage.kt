package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.Utils
import drinkteam.layouts.SuccesPageLayout
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 18/06/2016.
 *
 * Page (Fragment) "Mes succès" accessible depuis le menu
 * Liste les succès disponibles et les scores obtenus
 */
class SuccesPage : Fragment()
{
	private lateinit var layout: SuccesPageLayout
	
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
		this.layout = SuccesPageLayout()
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
		
		// vue de la liste des Succès //
		val listView = layout.expandListviewSucces
		
		// calcule et affiche les niveaux obtenus pour chaque succès dans la listView //
		Utils.setUserNiveauSucces(arguments!!, listView)
	}
}
