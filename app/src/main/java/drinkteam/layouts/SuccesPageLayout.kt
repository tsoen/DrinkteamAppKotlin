package drinkteam.layouts

import android.widget.ExpandableListView
import android.widget.LinearLayout
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class SuccesPageLayout : AnkoComponent<MainActivity>
{
	lateinit var succesPageLayout: LinearLayout
	lateinit var expandListviewSucces: ExpandableListView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			succesPageLayout = this
			
			expandListviewSucces = expandableListView {
			}.lparams(width = matchParent, height = matchParent)
		}
	}
}