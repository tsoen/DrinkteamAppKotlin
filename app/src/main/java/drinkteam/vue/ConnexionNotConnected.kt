package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.layouts.ConnexionNotConnectedLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 25/05/2017.
 *
 * Page affichée si l'utilisateur n'est pas déjà connecté
 * Propose la connexion à un compte existant ou la création d'un nouveau compte
 */
class ConnexionNotConnected : Fragment()
{
	private lateinit var layout: ConnexionNotConnectedLayout
	
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
		this.layout = ConnexionNotConnectedLayout()
		return layout.createView(AnkoContext.create(context as ConnexionActivity, activity as ConnexionActivity))
	}
	
	/**
	 * Ajout des éléments après la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		
		// bouton de création de compte //
		val newAccount = layout.btnNewAccount
		
		// listener de click sur le bouton //
		newAccount.onClick {
			
			// affiche la page de création de compte //
			(activity as ConnexionActivity).replaceFragment(activity as ConnexionActivity, ConnexionRegister(), null, true)
		}
		
		// bouton de connexion à un compte existant //
		val connexion = layout.btnConnection
		
		// listener de click sur le bouton //
		connexion.onClick {
			
			// affiche la page de connexion à un compte existant //
			(activity as ConnexionActivity).replaceFragment(activity as ConnexionActivity, ConnexionLogin(), null, true)
		}
	}
}
