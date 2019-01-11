package drinkteam

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.AccessToken
import com.facebook.GraphRequest
import drinkteam.Utils.debug
import drinkteam.layouts.DialogPhotoLayout
import drinkteam.vue.MainActivity
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 24/06/2016.
 *
 * Fragment de dialogue pour sélectionner une photo ou ouvrir l'appareil photo
 */
class PhotoDialog : DialogFragment()
{
	
	/**
	 * Affichage du dialog
	 * @param inflater .
	 * @param container Conteneur parent
	 * @param savedInstanceState Etat sauvegardé de l'application
	 * @return Vue du dialog
	 */
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		
		// récupère le layout qui va contenir le fragmentDialog //
		val layout = DialogPhotoLayout()
		val view = layout.createView(AnkoContext.create(context as MainActivity, activity as MainActivity))
		
		val mode = arguments!!.getString("mode")
		
		//region ////////// PRENDRE UNE PHOTO //////////
		
		// création du bouton pour prendre une photo //
		val btnPicture = layout.takePic
		
		btnPicture.onClick {
			val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
			if (takePictureIntent.resolveActivity(activity!!.packageManager) != null)
			{
				if (mode == "profile")
				{
					activity!!.startActivityForResult(takePictureIntent, Config.CAPTURE_IMAGE_PROFILE)
				}
				else if (mode == "cover")
				{
					activity!!.startActivityForResult(takePictureIntent, Config.CAPTURE_IMAGE_COVER)
				}
				
				dialog.cancel()
			}
		}
		
		//endregion
		
		//region ////////// CHOISIR UNE PHOTO //////////
		
		val btnAlbum = layout.fromAlbum
		
		btnAlbum.onClick {
			
			val intent = Intent()
			
			// Show only images, no videos or anything else
			intent.type = "image/*"
			intent.action = Intent.ACTION_PICK
			
			// Always show the chooser (if there are multiple options available)
			if (mode == "profile")
			{
				activity!!.startActivityForResult(Intent.createChooser(intent, "Select Picture"), Config.PICK_IMAGE_PROFILE)
			}
			else if (mode == "cover")
			{
				activity!!.startActivityForResult(Intent.createChooser(intent, "Select Picture"), Config.PICK_IMAGE_COVER)
			}
			
			dialog.cancel()
		}
		
		//endregion
		
		val user = Utils.mAuth.currentUser
		// récupération du provider autre que Firebase //
		val provider = user!!.providers!![0]
		val btnUpdate = layout.updatePicture
		
		//region ////////// MISE A JOUR VIA FACEBOOK //////////
		
		if (provider == "facebook.com")
		{
			btnUpdate.text = "Mettre à jour via facebook"
			btnUpdate.onClick {
				if (mode == "profile")
				{
					try
					{
						// enregistrement de la photo de profile sous forme de flux de données //
						val url = user.providerData[1].photoUrl!!.toString()
						val node = Utils.storage.child(Config.STR_NODE_COMPTES).child(user.uid)
								.child(Config.STR_USER_PROFILE)
						
						Utils.storeFileByStream(url, node)
					}
					catch (e: Exception)
					{
						debug("updateUser FacebookProfile: $e")
					}
				}
				else if (mode == "cover")
				{
					// requête pour obtenir la photo de couverture facebook //
					val request = GraphRequest.newMeRequest(
						AccessToken.getCurrentAccessToken()
					) { `object`, _ ->
						/**
						 * Une fois la récupération terminée
						 * @param object Données récupérées par la requête encodées en JSON
						 * @param response Contient l'erreur éventuelle
						 */
						try
						{
							val url = `object`.getJSONObject("cover").getString("source")
							
							val node = Utils.storage.child(Config.STR_NODE_COMPTES).child(user.uid).child(Config.STR_USER_COVER)
							
							Utils.storeFileByStream(url, node)
						}
						catch (ex: Exception)
						{
							debug("updateUser couverture request: $ex")
						}
					}
					val parameters = Bundle()
					
					// indique qu'on souhaite récupérer la photo de couverture //
					parameters.putString("fields", "cover")
					request.parameters = parameters
					request.executeAsync()
				}
				
				dialog.cancel()
			}
		}
		else if (provider == "google.com")
		{
			if (mode == "profile")
			{
				btnUpdate.text = "Mettre à jour via google"
				btnUpdate.onClick {
					try
					{
						// enregistrement de la photo de profile sous forme de flux de données //
						val url = user.providerData[1].photoUrl!!.toString()
						
						val node = Utils.storage.child(Config.STR_NODE_COMPTES).child(user.uid).child(Config.STR_USER_PROFILE)
						
						Utils.storeFileByStream(url, node)
					}
					catch (e: Exception)
					{
						debug("updateUser FacebookProfile: $e")
					}
					
					dialog.cancel()
				}
			}
			else
			{
				val viewGroup = btnUpdate.parent as ViewGroup
				viewGroup.removeView(btnUpdate)
			}
		}
		
		//endregion
		
		//region ////////// MISE A JOUR VIA GOOGLE //////////
		
		//TODO ?
		
		//endregion
		
		// création du Dialog à partie de l'instanciation et de la création de la Vue (boutons) //
		dialog.setTitle("Choisir une action")
		
		return view
	}
	
	companion object
	{
		/**
		 * Instancie un nouveau Dialog
		 * @param mode Fonction du dialog
		 * @return Dialog
		 */
		fun newInstance(mode: String): PhotoDialog
		{
			val dialog = PhotoDialog()
			val args = Bundle()
			args.putString("mode", mode)
			dialog.arguments = args
			
			return dialog
		}
	}
}
