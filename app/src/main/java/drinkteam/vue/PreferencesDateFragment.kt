package drinkteam.vue

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.view.View
import android.widget.DatePicker

/**
 * Created by Timothée on 25/06/2016.
 *
 * Fragment contenant le Dialog de sélection d'une date
 */
class PreferencesDateFragment : PreferenceDialogFragmentCompat()
{
	private var picker: DatePicker? = null
	private var datePref: PreferencesDate? = null
	
	override fun onCreateDialogView(context: Context?): View
	{
		datePref = preferenceIsDatePreference as PreferencesDate?
		
		this.picker = DatePicker(context)
		
		return this.picker as View
	}
	
	override fun onBindDialogView(view: View)
	{
		super.onBindDialogView(view)
		
		picker!!.updateDate(lastYear, lastMonth, lastDate)
	}
	
	override fun onDialogClosed(positiveResult: Boolean)
	{
		if (positiveResult)
		{
			lastDate = picker!!.dayOfMonth
			lastMonth = picker!!.month
			lastYear = picker!!.year
			
			dateval = (lastDate.toString() + "-" + (lastMonth + 1).toString() + "-" + lastYear.toString())
			val prefs = activity!!.getSharedPreferences("prefs", Context.MODE_PRIVATE)
			val preferences = prefs.edit()
			preferences.putString("naissance_user", dateval)
			preferences.apply()
			
			datePref!!.text = dateval
		}
	}
	
	companion object
	{
		private var lastDate = 0
		private var lastMonth = 0
		private var lastYear = 0
		private var dateval = ""
		private var preferenceIsDatePreference: Preference? = null
		
		fun newInstance(preference: Preference): PreferencesDateFragment
		{
			val fragment = PreferencesDateFragment()
			
			preferenceIsDatePreference = preference
			
			dateval = (preference as PreferencesDate).text
			lastDate = preference.getDate(dateval)
			lastMonth = preference.getMonth(dateval) - 1
			lastYear = preference.getYear(dateval)
			val bundle = Bundle()
			bundle.putString("key", preference.getKey())
			fragment.arguments = bundle
			
			return fragment
		}
	}
}
