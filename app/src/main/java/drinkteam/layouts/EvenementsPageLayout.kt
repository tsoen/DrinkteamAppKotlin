package drinkteam.layouts

import android.widget.TextView
import com.drinkteam.R
import drinkteam.vue.MainActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class EvenementsPageLayout : AnkoComponent<MainActivity>
{
	lateinit var evenements: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			evenements = textView {
				text = resources.getString(R.string.Evenements)
			}
		}
	}
}
