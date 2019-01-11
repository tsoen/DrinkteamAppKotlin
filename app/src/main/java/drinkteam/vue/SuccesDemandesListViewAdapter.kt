package drinkteam.vue

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import drinkteam.Utils
import drinkteam.layouts.SuccesDemandesListItemLayout
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 14/10/2017.
 *
 */
class SuccesDemandesListViewAdapter

/**
 * Constructeur
 * @param context Contexte de l'application
 * @param userID ID de l'utilisateur
 */
	(private val context: Context, private val userID: String, private val positions: List<Int>) : BaseAdapter()
{
	// liste des Succès à afficher //
	private val listSucces = Utils.dbSucces.allSucces
	
	//region //////////////////// ACCESSEURS ////////////////////
	
	/**
	 * Retourne le nombre d'items (Succes) affichés
	 * @return Nombre de Succès affichés
	 */
	override fun getCount(): Int
	{
		return this.positions.size
	}
	
	/**
	 * Retourne l'item à la position indiquée
	 * @param i Index de l'item
	 * @return Succès
	 */
	override fun getItem(i: Int): Any
	{
		return listSucces[positions[i]]
	}
	
	/**
	 * Retourne l'ID du Succès à la position indiquée
	 * @param i Index du Succès dans la ListView
	 * @return Long
	 */
	override fun getItemId(i: Int): Long
	{
		return Integer.parseInt(this.listSucces[i].codecategorie.toString() + "" + this.listSucces[i].codesucces).toLong()
	}
	
	//endregion ////////////////////////////////////////
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	private inner class Holder
	{
		// icone du Succès //
		internal var icon: ImageView? = null
		
		// niveau atteint par l'utilisateur pour ce Succès //
		internal var niveau: ImageView? = null
		
		// nom du Succès //
		internal var nom: TextView? = null
		
		// description du Succès //
		internal var description: TextView? = null
		
		// bouton de confirmation de la demande //
		internal var buttonYes: Button? = null
		
		// bouton de refus de la demande //
		internal var buttonNo: Button? = null
	}
	
	override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View
	{
		// REUSE VIEW IF NOT NULL CODE //
		val layout = SuccesDemandesListItemLayout()
		val itemView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = Holder()
		holder.nom = layout.nomSucces
		holder.icon = layout.iconSucces
		holder.niveau = layout.iconNiveau
		holder.description = layout.descriptionSucces
		holder.buttonYes = layout.succesDemandeAcceptButton
		holder.buttonNo = layout.succesDemandeRefuseButton
		
		// nom du Succès //
		holder.nom!!.text = listSucces[positions[position]].nom
		
		// nom du fichier contenant l'image du Succès //
		val image = if (listSucces[positions[position]].image != "")
		{
			listSucces[positions[position]].image
		}
		else
		{
			"default_icon"
		}
		val iconID = this.context.resources.getIdentifier(image, "drawable", context.packageName)
		// arrondi l'image de base //
		val imageBit = BitmapFactory.decodeResource(context.resources, iconID)
		holder.icon!!.setImageBitmap(imageBit)
		val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBit)
		roundedBitmapDrawable.cornerRadius = 200f
		roundedBitmapDrawable.setAntiAlias(true)
		holder.icon!!.setImageDrawable(roundedBitmapDrawable)
		
		// image du niveau atteint par l'utilisateur pour ce Succès //
		holder.niveau!!.setImageURI(Uri.parse(Utils.niveauxSuccesAnyUser[positions[position]]))
		
		// description du Succès //
		holder.description!!.text = listSucces[positions[position]].description
		
		return itemView
	}
}
