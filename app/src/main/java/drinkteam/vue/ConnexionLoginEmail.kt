package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.Utils
import drinkteam.layouts.ConnexionLoginEmailLayout
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Timothée on 04/07/2017.
 *
 * Interface de connexion pour les utilisateurs utilisant un système Email-Password classique
 */
class ConnexionLoginEmail : Fragment()
{
	private lateinit var layout: ConnexionLoginEmailLayout
	
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
		this.layout = ConnexionLoginEmailLayout()
		return layout.createView(AnkoContext.create(context as ConnexionActivity, activity as ConnexionActivity))
	}
	
	/**
	 * Ajout des éléments près la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		
		// champ de texte pour entrer son email //
		val txtEmail = layout.textLoginEmailEmail
		
		// champ de texte pour entrer son mot de passe //
		val txtPassword = layout.textLoginEmailPassword
		
		// bouton de connexion //
		val btnLogin = layout.btnLoginEmail
		
		// listener de click //
		btnLogin.onClick {
			if (txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty())
			{
				// méthode de firebase pour se connecter avec email-password //
				Utils.mAuth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString()).addOnCompleteListener(activity!!)
				{ task ->
					
					// si connexion réussie à firebase //
					if (task.isSuccessful)
					{
						// connexion à l'application //
						Utils.loginUser(activity!!)
					}
					else
					{
						// If sign in fails, display a message to the user.
						toast("Identifiants incorrects")
					}
				}
			}
			else
			{
				// TODO
			}
		}
	}
}
