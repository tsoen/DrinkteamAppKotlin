package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import drinkteam.Config
import drinkteam.Utils
import drinkteam.layouts.ConnexionRegisterLayout
import drinkteam.loginFacebook
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 24/05/2017.
 *
 * Interface de création de compte pour les nouveaux utilisateurs
 */
class ConnexionRegister : Fragment()
{
	private lateinit var layout: ConnexionRegisterLayout
	
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
		this.layout = ConnexionRegisterLayout()
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
		
		//region ////////// LOGIN FACEBOOK //////////
		
		// association du layout du bouton //
		val btnFacebook = layout.btnRegisterFacebook
		
		btnFacebook.onClick {
			
			btnFacebook.loginFacebook({ result ->
				// récupération du jeton d'accès à facebook //
				val accessToken = result.accessToken
				
				// définition du listener de connexion à Firebase //
				Utils.authListener = FirebaseAuth.AuthStateListener {
					
					// enregistrement et accès à l'application //
					Utils.registerUser(activity!!)
				}
				
				// ajout du listener au point d'entrée à firebase //
				Utils.mAuth.addAuthStateListener(Utils.authListener!!)
				
				// accès à firebase à partir du jeton d'accès à facebook //
				val credential = FacebookAuthProvider.getCredential(accessToken.token)
				
				// connexion à Firebase, déclenche authListener //
				Utils.mAuth.signInWithCredential(credential)
			}, activity as ConnexionActivity)
		}
		
		//endregion
		
		//region ////////// LOGIN GOOGLE //////////
		
		// association du layout du bouton //
		val btnGoogle = layout.btnRegisterGoogle
		
		// listener de click sur le bouton //
		btnGoogle.onClick {
			
			// définition de la fenêtre de sélection du compte google //
			val signInIntent = Auth.GoogleSignInApi.getSignInIntent(Utils.mGoogleApiClient)
			
			// démarrage de la fenêtre de sélection //
			// la gestion de la connexion se trouve dans ConnexionActivity.onActivityResult //
			activity!!.startActivityForResult(signInIntent, Config.GOOGLE_CREATE)
		}
		
		//endregion
		
		//region ////////// LOGIN EMAIL/PASSWORD //////////
		
		// association du layout du bouton de connexion email-password //
		val btnEmail = layout.btnRegisterEmail
		
		// listener de click sur le bouton //
		btnEmail.onClick {
			
			// interface d'enregistrement de compte avec email / mot de passe //
			(activity as ConnexionActivity).replaceFragment(activity as ConnexionActivity, ConnexionRegisterEmail(), null, true)
		}
		
		//endregion
	}
}
