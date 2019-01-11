package drinkteam.vue

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.drinkteam.R
import drinkteam.Utils
import drinkteam.Utils.debug
import drinkteam.layouts.SuccesRecentItemLayout
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.coroutines.experimental.Ref
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Timothée on 17/06/2017.
 *
 * Gestionnaire de la GridView qui affiche la liste des Succès de la page de profil d'un utilisateur
 * (AmisProfil) ou sur la page d'accueil de l'application (AccueilPage)
 * Il défini le comportement de la GridView et le contenu de chaque item de la liste
 */
class SuccesHorizontalGridViewAdapter
/**
 * Constructeur
 * @param context Contexte de l'application
 */
	(private val context: Context, private val userID: String) : RecyclerView.Adapter<SuccesHorizontalGridViewAdapter.ItemHolder>()
{
	// liste de tous les Succès de l'application //
	private val listSucces = Utils.dbSucces.allSucces
	
	lateinit var layout: SuccesRecentItemLayout
	
	/**
	 * Compte le nombre d'items affichés
	 * @return Nombre de Succès affichés
	 */
	override fun getItemCount(): Int
	{
		return this.listSucces.size
	}
	
	/**
	 * Renvoi l'ID du Succès sélectionné
	 * @param i Index du Succès dans la liste
	 * @return ID
	 */
	override fun getItemId(i: Int): Long
	{
		return Integer.parseInt(this.listSucces[i].codecategorie.toString() + "" + this.listSucces[i].codesucces).toLong()
	}
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	class ItemHolder
	/**
	 * Constructeur
	 * @param view Vue de l'item
	 */
	constructor(view: View, layout: SuccesRecentItemLayout) : RecyclerView.ViewHolder(view)
	{
		// icon du succès //
		internal var icon = layout.iconSucces
		
		// niveau atteint par l'utilisateur pour ce succès //
		internal var niveau = layout.iconNiveau
		
		// nom du succès //
		internal var nom = layout.nomSucces
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour créer chaque item de la GridView
	 * @param parent Vue de la GridView
	 * @param viewType -
	 * @return Vue de l'item
	 */
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder
	{
		layout = SuccesRecentItemLayout()
		val view = layout.createView(AnkoContext.create(context as MainActivity, context))
		return ItemHolder(view, layout)
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour afficher chaque item de la GridView
	 * @param holder Vue de l'item
	 * @param position Position de l'item dans la GridView
	 */
	override fun onBindViewHolder(holder: ItemHolder, position: Int)
	{
		try
		{
			// nom du succès //
			holder.nom.text = listSucces[position].nom
			
			// nom du fichier contenant l'image du Succès //
			val image = if (listSucces[position].image != "")
			{
				listSucces[position].image
			}
			else
			{
				"default_icon"
			}
			val iconID = context.resources.getIdentifier(image, "drawable", context.packageName)
			// arrondi l'image de base //
			val imageBit = BitmapFactory.decodeResource(context.resources, iconID)
			holder.icon.setImageBitmap(imageBit)
			val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBit)
			roundedBitmapDrawable.cornerRadius = 200f
			roundedBitmapDrawable.setAntiAlias(true)
			holder.icon.setImageDrawable(roundedBitmapDrawable)
			
			// image du niveau atteint par l'utilisateur pour ce succès //
			holder.niveau.setImageURI(Uri.parse(Utils.niveauxSuccesAnyUser[position]))
			
			val refUI: Ref<SuccesHorizontalGridViewAdapter> = this.asReference()
			holder.itemView.onClick {
				
				val activity = refUI().context as MainActivity
				val bundle = Bundle()
				if (userID == Utils.mAuth.currentUser!!.uid)
				{
					bundle.putString("titre_actionbar", activity.resources.getString(R.string.Mes_succes))
				}
				else
				{
					val currentUserName = activity.actionbarLayout.titreActionBar
					bundle.putString("titre_actionbar", currentUserName.text.toString())
				}
				
				bundle.putString("id", userID)
				val succesID = Integer.parseInt(listSucces[position].codecategorie.toString() + "" + listSucces[position].codesucces)
				bundle.putInt("succesID", succesID)
				activity.replaceFragment(activity, SuccesPage(), bundle, true)
			}
		}
		catch (ex: Exception)
		{
			debug("$ex")
		}
	}
}
