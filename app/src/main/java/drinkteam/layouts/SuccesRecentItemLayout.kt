package drinkteam.layouts

import android.view.View.generateViewId
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class SuccesRecentItemLayout : AnkoComponent<MainActivity>
{
	lateinit var succesImagesLayout: RelativeLayout
	lateinit var iconNiveau: ImageView
	lateinit var iconSucces: ImageView
	lateinit var nomSucces: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			succesImagesLayout = relativeLayout {
				
				iconNiveau = imageView {
					id = generateViewId()
				}.lparams(width = dip(80), height = dip(70)) {
					centerInParent()
				}
				
				iconSucces = imageView {
				}.lparams(width = dip(25), height = dip(25)) {
					centerInParent()
				}
				
				nomSucces = textView {
				}.lparams {
					below(iconNiveau)
					centerHorizontally()
					topMargin = dip(20)
				}
				
			}.lparams {
				marginEnd = dip(5)
				marginStart = dip(5)
			}
		}
	}
}
