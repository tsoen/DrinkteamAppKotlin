package drinkteam.layouts

import android.widget.GridView
import android.widget.LinearLayout
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class JeuxPageLayout : AnkoComponent<MainActivity>
{
	lateinit var jeuPageLayout: LinearLayout
	lateinit var gridviewJeu: GridView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			jeuPageLayout = this
			
			gridviewJeu = gridView {
				//android:numColumns = 1 //not support attribute TODO
			}.lparams(width = matchParent, height = matchParent)
		}
	}
}
