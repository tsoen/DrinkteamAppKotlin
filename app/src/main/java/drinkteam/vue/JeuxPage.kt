package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import drinkteam.Utils
import drinkteam.layouts.JeuxPageLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 18/06/2016.
 *
 * Page (Fragment) "Nos idées jeux" accessible depuis le menu
 * Liste les Jeux proposés par l'application
 */
class JeuxPage : Fragment()
{
	private lateinit var layout: JeuxPageLayout
	
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
		this.layout = JeuxPageLayout()
		return layout.createView(AnkoContext.create(context as MainActivity, activity as MainActivity))
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
		
		// récupère la liste des succès de la base//
		val listJeux = Utils.dbJeux.allJeux
		
		// récupère la vue de la liste des cocktails //
		val gridView = layout.gridviewJeu
		
		// donne la liste des Jeux à afficher au gestionnaire de la liste //
		val adapter = JeuxGridViewAdapter(context!!, listJeux)
		
		// auto-scroll au dernier Jeu sélectionné quand on revient d'une JeuxFiche //
		val index = gridView.firstVisiblePosition
		gridView.smoothScrollToPosition(index)
		gridView.adapter = adapter
		
		// listener de click sur un Jeu de la liste //
		gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
			
			val bundle = Bundle()
			bundle.putLong("code", id)
			
			// affiche la fiche du Jeu //
			(activity as MainActivity).replaceFragment(activity as MainActivity, JeuxFiche(), bundle, true)
		}
		
		/* The fuck was that, me ?
		gridView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					getFragmentManager().popBackStackImmediate();
					return true;
				}
				return false;
			}
		});
		*/
	}
}
