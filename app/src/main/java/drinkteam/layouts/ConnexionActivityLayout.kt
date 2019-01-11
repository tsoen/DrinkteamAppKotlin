package drinkteam.layouts

import android.view.View.generateViewId
import android.widget.FrameLayout
import drinkteam.vue.ConnexionActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class ConnexionActivityLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var fragmentContainer: FrameLayout
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		
		frameLayout {
			id = generateViewId()
			fragmentContainer = this
		}
	}
}
