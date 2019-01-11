package drinkteam.vue

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageException
import drinkteam.Config
import drinkteam.DownloadImageTask
import drinkteam.Utils
import drinkteam.Utils.debug
import drinkteam.Utils.weeklyCocktailID
import drinkteam.layouts.AccueilPageLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 18/06/2016.
 *
 * Page (Fragment) "Accueil" accessible depuis le menu et affichée à l'ouverture de l'application
 */
class AccueilPage : Fragment()
{
	private lateinit var layout: AccueilPageLayout
	
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
		this.layout = AccueilPageLayout()
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
		
		try
		{
			val user = Utils.mAuth.currentUser
			if (user != null)
			{
				val bundle = arguments as Bundle
				
				// titre de l'actionBar //
				(activity as MainActivity).setTitreActionBar(bundle.getString("titre_actionbar"))
				
				// récupération des données de l'utilisateurs //
				Utils.firebaseDB.child(Config.DB_NODE_COMPTES).child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener
				{
					/**
					 * Détecte un changement de donnée dans la base à l'emplacement défini
					 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
					 * @param dataSnapshot Contient les données présentes à cet emplacement
					 */
					override fun onDataChange(dataSnapshot: DataSnapshot)
					{
						// si des données existent pour cet utilisateur //
						if (dataSnapshot.exists())
						{
							// titre de l'utilisateur //
							val titre = layout.accueilProfileTitre
							titre.text = dataSnapshot.child(Config.DB_USER_TITRE).value!!.toString()
							
							// création des paramètres à transmettre si on souhaite afficher la page de profil détaillée de l'utilisateur //
							bundle.putString("id", dataSnapshot.child(Config.DB_USER_ID).value!!.toString())
							bundle.putString("name", dataSnapshot.child(Config.DB_USER_NAME).value!!.toString())
							bundle.putString("provider", dataSnapshot.child(Config.DB_USER_PROVIDER).value!!.toString())
							bundle.putString("providerid", dataSnapshot.child(Config.DB_USER_PROVIDERID).value!!.toString())
							bundle.putString("titre", dataSnapshot.child(Config.DB_USER_TITRE).value!!.toString())
						}
					}
					
					/**
					 * Arrêt de la récupération des données
					 * @param databaseError Erreur éventuelle
					 */
					override fun onCancelled(databaseError: DatabaseError)
					{
					}
				})
				
				// vue contenant la photo de couverture - RelativeLayout dont on va set le background //
				val background = layout.accueilProfileLayout
				
				// listener de clicks sur la photo de couverture //
				background.onClick {
					
					// affiche du profil détaillé de l'utilisateur //
					(activity as MainActivity).replaceFragment(activity as MainActivity, AmisProfil(), bundle, true)
				}
				
				// nom de l'utilisateur //
				val name = layout.accueilProfileName
				name.text = user.displayName
				
				//region ////////// IMAGE DE PROFIL //////////
				
				// photo de profil de l'utilisateur //
				val profile = layout.accueilProfilePic
				
				// télécharge et affiche la photo dans le vue indiquée //
				
				// récupération de l'url de l'image dans le storage de firebase //
				Utils.storage.child(Config.STR_NODE_COMPTES).child(user.uid).child(Config.STR_USER_PROFILE).downloadUrl.addOnSuccessListener { uri ->
					
					// téléchargement et affichage de l'image de profil //
					DownloadImageTask(profile).execute(Config.STR_USER_PROFILE, uri.toString())
				}.addOnFailureListener {
					
					// si l'erreur provient du storage de firebase //
					if (it is StorageException && it.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND)
					{
						// récupération de l'image de profil par défaut //
						Utils.storage.child(Config.STR_NODE_COMPTES).child(Config.STR_DEFAULT_PROFILE).downloadUrl.addOnSuccessListener { uri ->
							
							// téléchargement et affichage de l'image de profil par défaut //
							DownloadImageTask(profile).execute(Config.STR_USER_PROFILE, uri.toString())
						}.addOnFailureListener { exception ->
							
							debug("AmisProfil profile notFound: $exception")
						}
					}
					else
					{
						debug("AmisProfil profile onFailure: $it")
					}
				}
				
				//endregion
				
				//region ////////// IMAGE DE COUVERTURE //////////
				
				// récupération de la photo de couverture dans le storage firebase //
				Utils.storage.child(Config.STR_NODE_COMPTES).child(user.uid).child(Config.STR_USER_COVER).downloadUrl.addOnSuccessListener { uri ->
					/**
					 * Réussite de récupération des données
					 * @param uri Uri de téléchargement du fichier
					 */
					DownloadImageTask(background).execute(Config.STR_USER_COVER, uri.toString())
				}.addOnFailureListener {
					
					// si l'erreur provient du storage firebase //
					if (it is StorageException && it.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND)
					{
						// récupération de la photo de couverture par défaut //
						Utils.storage.child(Config.STR_NODE_COMPTES).child(Config.STR_DEFAULT_COVER).downloadUrl.addOnSuccessListener { uri ->
							
							DownloadImageTask(background).execute(Config.STR_USER_COVER, uri.toString())
						}.addOnFailureListener { exception ->
							
							debug("Accueil background notFound: $exception")
						}
					}
					else
					{
						debug("Accueil background onFailure: $it")
					}
				}
				
				//endregion
				
				bundle.putString("id", user.uid)
				
				val buttonAllSucces = layout.accueilSuccesButton
				buttonAllSucces.onClick {
					
					(activity as MainActivity).replaceFragment(activity as MainActivity, SuccesPage(), bundle, true)
				}
				
				// succes obtenus //
				val horizontalGridView = layout.accueilSuccesList
				Utils.setUserNiveauSucces(bundle, horizontalGridView)
				
				// cocktail de la semaine //
				val cocktailName = layout.weeklyCocktailName
				cocktailName.text = Utils.dbCocktails.getCocktail(weeklyCocktailID).nom
				
				val cocktailLogo = layout.accueilCocktailLogo
				// TODO sélection de l'image du cocktail dans les ressources de l'appli //
				val iconID = context!!.resources.getIdentifier("default_icon", "drawable", context!!.packageName)
				val imageBit = BitmapFactory.decodeResource(context!!.resources, iconID)
				cocktailLogo.setImageBitmap(Bitmap.createScaledBitmap(imageBit, 120, 120, false))
				
				val cocktailFicheButton = layout.cocktailFicheButton
				cocktailFicheButton.onClick {
					
					val newBundle = Bundle()
					newBundle.putLong("code", Utils.dbCocktails.getCocktail(weeklyCocktailID).code)
					(activity as MainActivity).replaceFragment(activity as MainActivity, CocktailsFiche(), newBundle, true)
				}
			}
			else
			{
				debug("AccueilPage: no user logged in")
			}
		}
		catch (ex: Exception)
		{
			debug("changeUI: $ex")
		}
	}
}
