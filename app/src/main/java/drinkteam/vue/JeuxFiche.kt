package drinkteam.vue

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import drinkteam.Utils
import drinkteam.layouts.JeuxFicheLayout
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 16/05/2017.
 *
 * Page (Fragment) de détails d'un jeu
 * Accessible par click d'un jeu dans la liste de JeuxPage
 */
class JeuxFiche : Fragment()
{
	private lateinit var layout: JeuxFicheLayout
	
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
		this.layout = JeuxFicheLayout()
		return layout.createView(AnkoContext.create(context as MainActivity, activity as MainActivity))
	}
	
	/**
	 * Ajout des éléments après la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		
		// code du jeu //
		val code = arguments!!.getLong("code")
		
		// nom du jeu //
		val nom = layout.nomJeuFiche
		nom.text = Utils.dbJeux.getJeu(code).nom
		
		// nombre de joueurs //
		val joueurs = layout.joueursJeuFiche
		joueurs.text = "Joueurs: " + Utils.dbJeux.getJeu(code).joueurs
		
		//TextView regles = (TextView) view.findViewById(R.id.regles_jeu_fiche);
		//on utilise du texte formaté en html
		//regles.setText("Règles: \n" + Html.fromHtml(Utils.dbJeux.getJeu(code).getRegles()));
		
		val regles = layout.reglesJeuFiche
		val settings = regles.settings
		settings.defaultTextEncodingName = "utf-8"
		regles.setBackgroundColor(Color.TRANSPARENT)
		regles.loadData(Utils.dbJeux.getJeu(code).regles, "text/html; charset=utf-8", "utf-8")
	}
}
