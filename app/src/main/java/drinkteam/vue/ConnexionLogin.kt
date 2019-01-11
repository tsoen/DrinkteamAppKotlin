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
import drinkteam.layouts.ConnexionLoginLayout
import drinkteam.loginFacebook
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 24/05/2017.
 *
 * Interface de connexion pour les utilisateurs disposant déjà d'un compte
 */
class ConnexionLogin : Fragment()
{
	private lateinit var layout: ConnexionLoginLayout
	
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
		this.layout = ConnexionLoginLayout()
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
		
		//region ////////// LOGIN FACEBOOK //////////
		
		// association du layout du bouton //
		val btnFacebook = layout.btnLoginFacebook
		
		btnFacebook.onClick {
			
			btnFacebook.loginFacebook({ result ->
				// objet représentant le jeton d'accès à facebook //
				val facebookToken = result.accessToken
				
				// définition du listener de connexion à Firebase //
				Utils.authListener = FirebaseAuth.AuthStateListener {
					/**
					 * Action après un changment de statut de connexion à firebase :
					 * - quand le listener est enregistré
					 * - quand un utilisateur se connecte
					 * - quand un utilisateur se déconnecte
					 */
					// connexion et accès à l'application //
					Utils.loginUser(activity!!)
				}
				
				// ajout du listener au point d'entrée à firebase //
				Utils.mAuth.addAuthStateListener(Utils.authListener!!)
				
				// récupération du jeton d'accès à facebook //
				val credential = FacebookAuthProvider.getCredential(facebookToken.token)
				
				// connexion à Firebase à partir du jeton d'accès à facebook, déclenche authListener //
				Utils.mAuth.signInWithCredential(credential)
			}, activity as ConnexionActivity)
		}
		
		//endregion
		
		//region ////////// LOGIN GOOGLE //////////
		
		// association du layout du bouton de connexion Google //
		val btnGoogle = layout.btnLoginGoogle
		
		// listener de click sur le bouton //
		btnGoogle.onClick {
			
			// définition de la fenêtre de sélection du compte google //
			val signInIntent = Auth.GoogleSignInApi.getSignInIntent(Utils.mGoogleApiClient)
			
			// démarrage de la fenêtre de sélection du compte //
			// la gestion de la connexion se trouve dans ConnexionActivity.onActivityResult //
			activity!!.startActivityForResult(signInIntent, Config.GOOGLE_SIGN_IN)
		}
		
		//endregion
		
		//region ////////// LOGIN EMAIL/PASSWORD //////////
		
		// association du layout du bouton de connexion email-password //
		val btnEmail = layout.btnLoginEmail
		
		// listener de click sur le bouton //
		btnEmail.onClick {
			
			// affichage d'une page personnalisée pour entrer les identifiants //
			(activity as ConnexionActivity).replaceFragment(activity as ConnexionActivity, ConnexionLoginEmail(), null, true)
		}
		
		//endregion
	}
}
