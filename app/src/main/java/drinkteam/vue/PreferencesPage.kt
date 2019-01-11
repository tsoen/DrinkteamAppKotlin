package drinkteam.vue

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceGroup
import com.drinkteam.R
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import drinkteam.PhotoDialog
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by Timothée on 24/06/2016.
 *
 * Les préférences sont des données qui sont sauvegardées dans l'application : par exemple, la langue de l'utilisateur
 */
class PreferencesPage : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener
{
	/**
	 * A la création d'un fragment de préférence
	 * @param savedInstanceState Etat sauvegardé de l'application si elle a été mise de côté
	 * @param rootKey .
	 */
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
	{
		// titre de l'actionBar //
		if (arguments!!.getString("titre_actionbar") != null)
		{
			(activity as MainActivity).setTitreActionBar(arguments!!.getString("titre_actionbar"))
		}
		
		// charge les éléments présent dans le PreferenceScreen qui a rootKey comme attribut 'rootKey' //
		
		setPreferencesFromResource(R.xml.preferences_page, rootKey)
		
		// détecte et enregistre les changements de préférences sur ce PreferenceScreen //
		preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
		
		// PreferencesPage est forcément un groupe (ou conteneur) d'autres préférences //
		val pGroup = findPreference(rootKey) as PreferenceGroup
		
		// parcourt les préférences contenues dans ce groupe //
		val pCount = pGroup.preferenceCount
		
		for (i in 0 until pCount)
		{
			// si la préférence d'index i est une ListPreference //
			if (pGroup.getPreference(i) is ListPreference)
			{
				// mise à jour du summary (indication dans le menu) de la préférence //
				val listPreference = pGroup.getPreference(i) as ListPreference
				listPreference.summary = listPreference.entry
			}
			// si la préférence d'index i est un editText //
			else if (pGroup.getPreference(i) is EditTextPreference)
			{
				val editTextPreference = pGroup.getPreference(i) as EditTextPreference
				val editText = editTextPreference.editText
				
				// par défaut, le summary correspond au 'hint' //
				val summary = editText.hint
				editTextPreference.summary = summary
			}
		}
	}
	
	/**
	 * Ouverture de l'écran des préférences, on s'assure que les summary des préférences sont à jour
	 */
	override fun onResume()
	{
		super.onResume()
		
		// vérifie toutes les préférences affichées //
		for (i in 0 until preferenceScreen.preferenceCount)
		{
			val preference = preferenceScreen.getPreference(i)
			
			// si on rencontre un groupe de Préférence //
			if (preference is PreferenceGroup)
			{
				// met à jour les summary des préférences qu'il contient //
				for (j in 0 until preference.preferenceCount)
				{
					val singlePref = preference.getPreference(j)
					updatePreference(singlePref, singlePref.key)
				}
			}
			// si on rencontre une préférence simple, on met à jour son summary //
			else
			{
				updatePreference(preference, preference.key)
			}
		}
	}
	
	/**
	 * Modification du summary d'une préférence à chaque modification
	 * @param sharedPreferences Liste des préférences de l'application
	 * @param key Clé de la préférence modifiée dans la liste des préférences
	 */
	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String)
	{
		updatePreference(findPreference(key), key)
	}
	
	/**
	 * Met à jour le summary d'une préférence
	 * @param preference L''affichage de la préférence à modifier
	 * @param key Clé de la préférence dans la liste des préférence
	 */
	private fun updatePreference(preference: Preference?, key: String)
	{
		if (preference == null)
		{
			return
		}
		
		if (preference is ListPreference)
		{
			val listPreference = findPreference(key) as ListPreference
			listPreference.summary = listPreference.entry
		}
		else if (preference is EditTextPreference)
		{
			val editTextPreference = findPreference(key) as EditTextPreference
			val editText = editTextPreference.editText
			if (editText.text.isNotEmpty())
			{
				// si on a rentré du texte dans un editText, le summary prend la valeur du texte //
				editTextPreference.summary = editTextPreference.text
			}
			// si la zone de texte est vide, le summary reprend la valeur de l'hint //
			else
			{
				editTextPreference.summary = editText.hint
			}
		}
		else if (preference is PreferencesDate)
		{
			val datePreference = preference as PreferencesDate?
			
			if (datePreference!!.text != datePreference.defaultDate)
			{
				datePreference.summary = datePreference.text
			}
		}
	}
	
	/**
	 * Comportement lors du clique sur une préférence
	 * @param preference Préférence sélectionné
	 * @return Validation
	 */
	override fun onPreferenceTreeClick(preference: Preference): Boolean
	{
		// ces préférences sont personnalisées, il faut donc gérer manuellement leur déclenchement //
		when (preference.key)
		{
			"profilPic" ->
			{
				val profileDialog = PhotoDialog.newInstance("profile")
				profileDialog.show(activity!!.supportFragmentManager, "fragment_photoDialog")
			}
			
			"backPic" ->
			{
				val coverDialog = PhotoDialog.newInstance("cover")
				coverDialog.show(activity!!.supportFragmentManager, "fragment_photoDialog")
			}
			
			"deconnexion" ->
			{
				LoginManager.getInstance().logOut()
				FirebaseAuth.getInstance().signOut()
				val mainActivity = activity
				startActivity<MainActivity>()
				mainActivity?.finish()
			}
			
			else ->
			{
			}
		}
		
		return super.onPreferenceTreeClick(preference)
	}
	
	override fun onDisplayPreferenceDialog(preference: Preference)
	{
		if (preference is PreferencesDate)
		{
			val fragment = PreferencesDateFragment.newInstance(preference)
			fragment.setTargetFragment(this, 0)
			fragment.show(fragmentManager, "android.support.v7.preference.PreferenceFragmentCompat.DIALOG")
		}
		else
		{
			super.onDisplayPreferenceDialog(preference)
		}
	}
}
