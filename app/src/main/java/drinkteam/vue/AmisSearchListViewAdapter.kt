package drinkteam.vue

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import drinkteam.Config
import drinkteam.DownloadImageTask
import drinkteam.Utils
import drinkteam.layouts.AmisSearchListItemLayout
import org.jetbrains.anko.AnkoContext
import kotlin.collections.Map.Entry

/**
 * Created by Timothée on 20/07/2017.
 *
 * Gestionnaire de la ListView qui affiche les résultats d'une recherche d'utilisateurs (Dialog de la barre de recherche)
 * Il défini le comportement de la ListView et le contenu de chaque item de la liste
 */
class AmisSearchListViewAdapter
/**
 * Constructeur
 * @param context Contexte de l'application
 * @param userArray Liste des résultats de la recherche d'utilisateurs SnapShot de firebase, score de recherche>
 */
	(private val context: Context, private val userArray: Array<Entry<DataSnapshot, Double>>) : BaseAdapter()
{
	/**
	 * Compte le nombre d'objets affichés
	 * @return Nombre de résultats de la recherche
	 */
	override fun getCount(): Int
	{
		return this.userArray.size
	}
	
	/**
	 * Récupère un objet affiché dans la ListView
	 * @param i Position dans la ListView
	 * @return DataSnapShot firebase de l'utilisateur
	 */
	override fun getItem(i: Int): Any
	{
		return this.userArray[i]
	}
	
	/**
	 * Récupère l'identifiant d'un objet affiché dans la ListView
	 * @param i Position dans la ListView
	 * @return Position dans la ListView
	 */
	override fun getItemId(i: Int): Long
	{
		return i.toLong()
	}
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	private inner class ItemHolder
	{
		// image du profil //
		internal var profile: ImageView? = null
		
		// nom de l'utilisateur //
		internal var nom: TextView? = null
		
		// titre de l'utilisateur //
		internal var titre: TextView? = null
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour afficher chaque item de la ListView
	 * @param position Position de l'item dans la ListView
	 * @param view Vue de l'item
	 * @param viewGroup Vue de la ListView
	 * @return Vue de l'item
	 */
	override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View
	{
		val layout = AmisSearchListItemLayout()
		val itemView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = ItemHolder()
		holder.profile = layout.iconProfile
		holder.nom = layout.nomProfile
		holder.titre = layout.titreProfile
		
		val userID = this.userArray[position].key.child(Config.DB_USER_ID).value!!.toString()
		
		// récupération de l'url de l'image dans le storage de firebase //
		Utils.storage.child(Config.STR_NODE_COMPTES).child(userID).child(Config.STR_USER_PROFILE).downloadUrl.addOnSuccessListener { uri ->
			
			// téléchargement et affichage de l'image de profil //
			DownloadImageTask(holder.profile as ImageView).execute(Config.STR_USER_PROFILE, uri.toString())
		}
		
		holder.nom!!.text = userArray[position].key.child(Config.DB_USER_NAME).value!!.toString()
		holder.titre!!.text = userArray[position].key.child(Config.DB_USER_TITRE).value!!.toString()
		
		return itemView
	}
}
