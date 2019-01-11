package drinkteam.vue

import android.content.Context
import android.support.v7.preference.DialogPreference
import android.util.AttributeSet
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dialog de Préférence contenant le DatePicker de sélection d'une date
 * En tant que préférence, la prochaine ouverture du dialog aura en mémoire la dernière date sélectionnée
 */
class PreferencesDate

/**
 * Constructeur
 * @param context Contexte de l'application
 * @param attrs Paramètres de la préférences
 */
	(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs)
{
	//region //////////////////// ATTRIBUTS ////////////////////
	
	// valeur de la date sélectionnée sous forme de chaîne //
	private var dateval: String = ""
	
	// indique la valeur actuelle de la préférence dans le menu des préférences //
	private var mSummary: CharSequence? = null
	
	val defaultDate: String
		/**
		 * La date par défaut est "date du jour -18 ans"
		 * @return Date par défaut
		 */
		get()
		{
			val formatJourMois = SimpleDateFormat("dd-MM-")
			val moisJour = formatJourMois.format(Date())
			val formatAnnee = SimpleDateFormat("yyyy")
			val annee = Integer.toString(Integer.parseInt(formatAnnee.format(Date())) - 18)
			
			return moisJour + annee
		}
	
	/**
	 * Récupère la valeur de la date enregistrée
	 * @return Valeur de la date
	 */
	var text: String
		get()
		{
			try
			{
				return sharedPreferences.getString("naissance_user", this.dateval)
			}
			catch (ex: NullPointerException)
			{
				return this.defaultDate
			}
		}
		/**
		 * Enregistre la date sélectionnée sous forme de chaîne
		 * La valeur enregistrée sera utilisée pour appeler setSummary
		 * @param text Valeur de la date
		 */
		set(text)
		{
			val wasBlocking = this.shouldDisableDependents()
			this.dateval = text
			this.persistString(text)
			val isBlocking = this.shouldDisableDependents()
			
			if (isBlocking != wasBlocking)
			{
				this.notifyDependencyChange(isBlocking)
			}
		}
	
	init
	{
		this.dateval = this.text
		
		// bouton de validation du dialog //
		this.positiveButtonText = "Set"
		
		// bouton d'annulation du dialog //
		this.negativeButtonText = "Cancel"
	}
	
	//endregion ////////////////////////////////////////
	
	//region //////////////////// GETS & SETS ////////////////////
	
	/**
	 * Renvoi l'année sélectionnée
	 * @param dateval Date complète
	 * @return Année sous forme d'entier
	 */
	fun getYear(dateval: String): Int
	{
		val pieces = dateval.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		return Integer.parseInt(pieces[2])
	}
	
	/**
	 * Renvoi le mois sélectionné
	 * @param dateval Date complète
	 * @return Mois sous forme d'entier
	 */
	fun getMonth(dateval: String): Int
	{
		val pieces = dateval.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		return Integer.parseInt(pieces[1])
	}
	
	/**
	 * Renvoi le jour sélectionné
	 * @param dateval Date complète
	 * @return Jour sous forme d'entier
	 */
	fun getDate(dateval: String): Int
	{
		val pieces = dateval.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		return Integer.parseInt(pieces[0])
	}
	
	/**
	 * Récupère la valeur affichée sur l'écran des préférences
	 * Ne pas supprimer même si No Usage Found
	 * @return Date affichée
	 */
	override fun getSummary(): CharSequence?
	{
		return this.mSummary
	}
	
	/**
	 * Défini la valeur affichée sur l'écran des préférences
	 * @param summary Date à afficher
	 */
	override fun setSummary(summary: CharSequence?)
	{
		if (summary == null && this.mSummary != null || summary != null && summary != this.mSummary)
		{
			this.mSummary = summary
			notifyChanged()
		}
	}
	
	//endregion ////////////////////////////////////////
}