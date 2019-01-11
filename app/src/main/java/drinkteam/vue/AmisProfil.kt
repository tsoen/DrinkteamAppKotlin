package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.StorageException
import drinkteam.Config
import drinkteam.DownloadImageTask
import drinkteam.Utils
import drinkteam.Utils.debug
import drinkteam.layouts.AmisProfilLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 05/07/2017.
 *
 * Page (Fragment) de détails du profil d'un utilisateur
 * Accessible par la recherche d'un utilisateur ou clique sur la bannière de la page d'accueil pour l'utilisateur connecté
 */
class AmisProfil : Fragment()
{
	private lateinit var layout: AmisProfilLayout
	
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
		this.layout = AmisProfilLayout()
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
			// titre de l'actionBar //
			(activity as MainActivity).setTitreActionBar(arguments!!.getString("name"))
			
			// nom de l'utilisateur //
			val name = layout.profileName
			name.text = arguments!!.getString("name")
			
			// titre de l'utilisateur //
			val titre = layout.profileTitre
			titre.text = arguments!!.getString("titre")
			
			//region ////////// IMAGE DE PROFIL //////////
			
			// vue de l'image du profil de l'utilisateur //
			val profile = layout.profilePic
			
			// récupération de l'url de l'image dans le storage de firebase //
			Utils.storage.child(Config.STR_NODE_COMPTES).child(arguments!!.getString("id")!!).child(
				Config.STR_USER_PROFILE).downloadUrl.addOnSuccessListener { uri ->
				
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
			
			// vue de l'image du couverture de l'utilisateur //
			val background = layout.profileHeader
			
			// récupération de l'url de l'image dans le storage de firebase //
			Utils.storage.child(Config.STR_NODE_COMPTES).child(arguments!!.getString("id")!!).child(
				Config.STR_USER_COVER).downloadUrl.addOnSuccessListener { uri ->
				
				// téléchargement et affichage de l'image de couverture //
				DownloadImageTask(background).execute(Config.STR_USER_COVER, uri.toString())
			}.addOnFailureListener {
				
				// si l'erreur provient du storage de firebase //
				if (it is StorageException && it.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND)
				{
					Utils.storage.child(Config.STR_NODE_COMPTES).child(Config.STR_DEFAULT_COVER).downloadUrl.addOnSuccessListener { uri ->
						
						// téléchargement et affichage de l'image de couverture par défaut //
						DownloadImageTask(background).execute(Config.STR_USER_COVER, uri.toString())
					}.addOnFailureListener { exception ->
						
						debug("AmisProfil couverture notFound: $exception")
					}
				}
				else
				{
					debug("AmisProfil couverture onFailure: $it")
				}
			}
			
			//endregion
			
			val buttonAllSucces = layout.profileSuccesButton
			buttonAllSucces.onClick {
				
				(activity as MainActivity).replaceFragment(activity as MainActivity, SuccesPage(), arguments, true)
			}
			
			// vue qui contient la liste des derniers succès //
			val horizontalGridViewSucces = layout.profileSuccesList
			
			// calcul des niveaux et affichage des succès //
			Utils.setUserNiveauSucces(arguments!!, horizontalGridViewSucces)
			
			val demandesListView = layout.profileDemandesList
			Utils.setUserNiveauSucces(arguments!!, demandesListView)
		}
		catch (e: Exception)
		{
			debug("AmisProfil onViewCreated: $e")
		}
	}
}
