package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.Utils
import drinkteam.layouts.ConnexionRegisterEmailLayout
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Timothée on 04/07/2017.
 *
 * Interface de création de compte pour les utilisateurs utilisant un système Email-Password classique
 */
class ConnexionRegisterEmail : Fragment()
{
	private lateinit var layout: ConnexionRegisterEmailLayout
	
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
		this.layout = ConnexionRegisterEmailLayout()
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
		val txtEmail = layout.textRegisterEmailEmail
		
		// champ de texte pour entrer son mot de passe //
		val txtPassword = layout.textRegisterEmailPassword
		
		// bouton de connexion //
		val btnRegister = layout.btnRegisterEmail
		
		// listener de click //
		btnRegister.onClick {
			
			// méthode de firebase pour se créer un compte avec email-password //
			if (txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty())
			{
				Utils.mAuth.createUserWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString()).addOnCompleteListener(activity!!) { task ->
					
					// si création dans firebase réussie //
					if (task.isSuccessful)
					{
						// enregistrement dans la base de données et accès à l'application //
						Utils.registerUser(activity!!)
					}
					// si erreur //
					else
					{
						// If sign in fails, display a message to the user.
						toast("Une erreur est survenue. Reessayez plus tard.")
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
