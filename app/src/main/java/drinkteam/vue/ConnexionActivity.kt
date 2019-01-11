package drinkteam.vue

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.GoogleAuthProvider
import drinkteam.Config
import drinkteam.Utils
import drinkteam.layouts.ConnexionActivityLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by Timothée on 24/05/2017.
 *
 * Activité au lancement de l'application dans tous les cas
 * Si un utilisateur est déjà connecté, redirige automatiquement vers MainActivity
 */
class ConnexionActivity : AppCompatActivity()
{
	lateinit var layout: ConnexionActivityLayout
	
	/**
	 * Création de l'activité
	 * @param savedInstanceState Etat sauvegardé de l'application si elle a été mise de côté
	 */
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		// si un utilisateur est déjà connecté, redirection vers la page principale //
		if (Utils.mAuth.currentUser != null)
		{
			// affichage de la page principale MainActivity //
			startActivity<MainActivity>()
			
			// destruction de cette activité pour ne pas la garder active en background //
			this.finish()
		}
		// sinon afichage des boutons de connexion / création de compte //
		else
		{
			// associe le layout à l'activité //
			layout = ConnexionActivityLayout()
			layout.setContentView(this)
			
			// affiche la page avec les boutons de connexion / création de compte //
			replaceFragment(this, ConnexionNotConnected(), null, false)
			
			// cache le titre de l'app //
			val actionBar = supportActionBar
			actionBar?.hide()
			
			// permissions d'accès de l'application aux infos du compte google //
			val googleReadPermissions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(Config.SERVER_CLIENT_ID).requestEmail().build()
			
			// définition du point d'entrée de connexion à google //
			Utils.mGoogleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this) { }.addApi(Auth.GOOGLE_SIGN_IN_API, googleReadPermissions).build()
			
			// définition du gestionnaire d'appels au bouton de connexion facebook //
			Utils.callbackManager = CallbackManager.Factory.create()
		}
	}
	
	/**
	 * Appelée lorsqu'une activité se termine
	 * Cela peut être cette activité ou les activités de connexion générées par facebook et google
	 * @param requestCode Permet d'identifier quelle activité a généré le résultat - startActivityForResult(requestCode)
	 * @param resultCode Code retourné par l'activité fermée
	 * @param data Données supplémentaires
	 */
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
	{
		super.onActivityResult(requestCode, resultCode, data)
		
		// si fenêtre de connexion google //
		if (requestCode == Config.GOOGLE_SIGN_IN || requestCode == Config.GOOGLE_CREATE)
		{
			// résultat de la connexion à google //
			val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
			
			// connexion à google réussie //
			if (result.isSuccess)
			{
				// connexion à firebase avec le compte google connecté //
				firebaseAuthWithGoogle(result.signInAccount, requestCode)
			}
			// echec connexion à google //
			else
			{
				toast("Echec connexion à google")
			}
		}
		// si fenêtre de connexion facebook //
		else
		{
			// voir la partie callBack de LOGIN FACEBOOK dans ConnexionLogin ou ConnexionRegister //
			Utils.callbackManager.onActivityResult(requestCode, resultCode, data)
		}
	}
	
	/**
	 * Connexion à firebase avec un compte google
	 * @param account Compte google connecté
	 * @param code Création de compte ou simple connexion
	 */
	private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?, code: Int)
	{
		// ConnexionActivity //
		val context = this
		
		// récupération du jeton d'accès à google //
		val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
		
		// connexion à Firebase à partir du jeton d'accès à google//
		Utils.mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
			
			// si connexion réussie //
			if (task.isSuccessful)
			{
				// enregistre l'utilisateur dans la base de données ou le connecte à l'application en fonction du code //
				if (code == Config.GOOGLE_CREATE)
				{
					Utils.registerUser(context)
				}
				else if (code == Config.GOOGLE_SIGN_IN)
				{
					Utils.loginUser(context)
				}
			}
			// si erreur //
			else
			{
				toast("Authentication failed.")
			}
		}
	}
}
