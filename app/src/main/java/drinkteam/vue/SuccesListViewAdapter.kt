package drinkteam.vue

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import drinkteam.Config
import drinkteam.Utils
import drinkteam.Utils.mAuth
import drinkteam.layouts.SuccesListItemLayout
import drinkteam.layouts.SuccesListSubitemLayout
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.DateFormat
import java.util.*

/**
 * Created by Timothée on 28/06/2016.
 *
 * Gestionnaire de la ListView qui affiche tous les Succès possibles (Succès_Page)
 * Il défini le comportement de la ListView et le contenu de chaque item de la liste
 * Cette liste est "Expandable" : sélectionner un item créer un sous-item pour l'item choisi
 */
class SuccesListViewAdapter

/**
 * Constructeur
 * @param context Contexte de l'application
 */
	(private val context: Context, private val userID: String) : BaseExpandableListAdapter()
{
	// liste des Succès à afficher //
	private val listSucces = Utils.dbSucces.allSucces
	
	//region //////////////////// ACCESSEURS ////////////////////
	
	/**
	 * Retourne le nombre d'items (Succes) affichés
	 * @return Nombre de Succès affichés
	 */
	override fun getGroupCount(): Int
	{
		return this.listSucces.size
	}
	
	/**
	 * Retourne le nombre d'enfant de l'item à la position indiquée
	 * Chaque item ne possède qu'un seul "sous-item"
	 * @param i Index de l'item
	 * @return Entier (1)
	 */
	override fun getChildrenCount(i: Int): Int
	{
		return 1
	}
	
	/**
	 * Retourne l'item à la position indiquée
	 * @param i Index de l'item
	 * @return Succès
	 */
	override fun getGroup(i: Int): Any
	{
		return listSucces[i]
	}
	
	/**
	 * Retourne un sous-item de l'item à la position indiquée
	 * @param i Position de l'item dans la ListView
	 * @param i1 Position du sous-item dans la liste des sous-items de l'item
	 * @return -
	 */
	override fun getChild(i: Int, i1: Int): Any?
	{
		return null
	}
	
	/**
	 * Retourne l'ID du Succès à la position indiquée
	 * @param i Index du Succès dans la ListView
	 * @return Long
	 */
	override fun getGroupId(i: Int): Long
	{
		return Integer.parseInt(this.listSucces[i].codecategorie.toString() + "" + this.listSucces[i].codesucces).toLong()
	}
	
	/**
	 * Retourne l'ID du sous-item à la position indiquée
	 * @param i Position de l'item dans la ListView
	 * @param i1 Position du sous-item dans la liste des sous-items de l'item
	 * @return -
	 */
	override fun getChildId(i: Int, i1: Int): Long
	{
		return i1.toLong()
	}
	
	/**
	 * Vrai : l'ID des items ne change pas à chaque modification de la ListView (qui ne devrait pas
	 * être modifiée de toute façon)
	 * @return True
	 */
	override fun hasStableIds(): Boolean
	{
		return true
	}
	
	//endregion ////////////////////////////////////////
	
	/**
	 * Définit l'affichage de chaque item de la liste
	 */
	private inner class GroupHolder
	{
		// icone du Succès //
		internal var icon: ImageView? = null
		
		// niveau atteint par l'utilisateur pour ce Succès //
		internal var niveau: ImageView? = null
		
		// nom du Succès //
		internal var nom: TextView? = null
		
		// description du Succès //
		internal var description: TextView? = null
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour afficher chaque item de la ListView
	 * @param position Position de l'item dans la ListView
	 * @param isExpanded Indique si le sous-item est affiché ou non
	 * @param view Vue de l'item
	 * @param viewGroup Vue de la ListView
	 * @return Vue de l'item
	 */
	override fun getGroupView(position: Int, isExpanded: Boolean, view: View?, viewGroup: ViewGroup): View
	{
		val layout = SuccesListItemLayout()
		val itemView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = GroupHolder()
		holder.nom = layout.nomSucces
		holder.icon = layout.iconSucces
		holder.niveau = layout.iconNiveau
		holder.description = layout.descriptionSucces
		
		// nom du Succès //
		holder.nom!!.text = listSucces[position].nom
		
		// nom du fichier contenant l'image du Succès //
		val image: String? = if (listSucces[position].image != "")
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
		holder.icon!!.setImageBitmap(imageBit)
		val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, imageBit)
		roundedBitmapDrawable.cornerRadius = 200f
		roundedBitmapDrawable.setAntiAlias(true)
		holder.icon!!.setImageDrawable(roundedBitmapDrawable)
		
		// image du niveau atteint par l'utilisateur pour ce Succès //
		holder.niveau!!.setImageURI(Uri.parse(Utils.niveauxSuccesAnyUser[position]))
		
		// description du Succès //
		holder.description!!.text = listSucces[position].description
		
		return itemView
	}
	
	/**
	 * Défini l'affichage du sous-item pour chaque item de la liste
	 */
	private inner class ChildHolder
	{
		// score actuel et prochain palier à atteindre //
		internal var palier: TextView? = null
		
		// anecdote sur le Succès //
		internal var anecdote: TextView? = null
		
		// lancer une demande de validation //
		internal var demande: Button? = null
	}
	
	/**
	 * Méthode utilisée par le gestionnaire pour afficher sous-item item d'un item de la ListView
	 * @param groupPosition Position de l'item parent dans la ListView
	 * @param childPosition Position du sous-item si un item pouvait avoir plusieurs sous-item
	 * @param isLastChild Indique si le sous-item est le dernier des sous-item (toujours vrai ici)
	 * @param view Vue du sous-item
	 * @param viewGroup Vue de l'item parent
	 * @return Vue de l'item
	 */
	override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, view: View?, viewGroup: ViewGroup): View
	{
		val layout = SuccesListSubitemLayout()
		val itemView = layout.createView(AnkoContext.create(context as MainActivity, context))
		
		val holder = ChildHolder()
		
		// anecdote //
		holder.anecdote = layout.anecdoteSucces
		holder.anecdote!!.text = listSucces[groupPosition].anecdote
		
		// palier //
		holder.palier = layout.palierSucces
		var score = 0
		var exists = false
		if (Utils.listDataSuccesAnyUser.indexOfKey(groupPosition) >= 0)
		{
			// score calculé dans Utils.setUserNiveauSucces //
			score = Integer.parseInt(Utils.listDataSuccesAnyUser.get(groupPosition)[0])
			exists = true
		}
		
		// score à obtenir pour atteindre le premier palier //
		val premierPalier = listSucces[groupPosition].palier
		
		// facteur de multiplication des paliers à atteindre //
		val facteur = listSucces[groupPosition].facteur
		
		// calcul du score à atteindre pour le prochain palier //
		var i = 0
		var prochainPalier = 0
		while (score >= prochainPalier)
		{
			// suite numérique //
			prochainPalier = if (facteur == 1)
			{
				premierPalier + premierPalier * i
			}
			// suite géométrique //
			else
			{
				(premierPalier * Math.pow(facteur.toDouble(), i.toDouble())).toInt()
			}
			
			i++
		}
		
		// score actuel et prochain palier à atteindre //
		val stringPalier = "Prochain palier : $score / $prochainPalier"
		holder.palier!!.text = stringPalier
		
		// bouton pour demande de validation //
		if (this.userID == Utils.mAuth.currentUser!!.uid)
		{
			holder.demande = layout.nouvelleDemandeSucces
			
			// fixe la valeur des variables pour etre accessibles dans le listener //
			val tempScore = score
			val tempExist = exists
			val tempProchainPalier = prochainPalier
			
			// listener de click sur le bouton //
			holder.demande!!.onClick {
				
				// id de l'utilisateur connecté //
				val userId = mAuth.currentUser!!.uid
				
				// ID du succes (on suppose = position dans la liste //
				// TODO changer le fonctionnement des ID des Succes //
				val succesId = Integer.toString(groupPosition)
				
				// TODO supprimer partie augmentant les scores //
				// enregistrement du nouveau score //
				Utils.firebaseDB.child(Config.DB_NODE_SCORES).child(userId).child(succesId).child(Config.DB_SUCCES_SCORE).setValue(tempScore + 1)
				
				// enregistrement de la date si le score atteint un nouveau palier //
				val dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
				val dateOut = dateFormatter.format(Date())
				if (!tempExist || tempScore + 1 == tempProchainPalier)
				{
					Utils.firebaseDB.child(Config.DB_NODE_SCORES).child(userId).child(succesId).child(Config.DB_SUCCES_DATE).setValue(dateOut)
				}
				
				Utils.firebaseDB.child(Config.DB_NODE_DEMANDES).child(userId).child(succesId).child(Config.DB_DEMANDE_SCORE).setValue(0)
				Utils.firebaseDB.child(Config.DB_NODE_DEMANDES).child(userId).child(succesId).child(Config.DB_DEMANDE_DATE).setValue(dateOut)
			}
		}
		else
		{
			(itemView as ViewManager).removeView(holder.demande)
		}
		
		return itemView
	}
	
	/**
	 * Indique si le sous-item peut être cliqué
	 * @param i Position de l'item dans la ListView
	 * @param i1 Position du sous-item dans la liste des sous-items de l'item
	 * @return False
	 */
	override fun isChildSelectable(i: Int, i1: Int): Boolean
	{
		return false
	}
}
