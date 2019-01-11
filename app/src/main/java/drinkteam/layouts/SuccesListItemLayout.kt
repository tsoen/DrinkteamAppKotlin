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
class SuccesListItemLayout : AnkoComponent<MainActivity>
{
	lateinit var succesLineLayout: RelativeLayout
	lateinit var succesImagesLayout: RelativeLayout
	lateinit var iconNiveau: ImageView
	lateinit var iconSucces: ImageView
	lateinit var nomSucces: TextView
	lateinit var descriptionSucces: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			succesLineLayout = this
			padding = dip(5)
			
			succesImagesLayout = relativeLayout {
				id = generateViewId()
				
				iconNiveau = imageView {
				}.lparams(width = dip(100), height = dip(80))
				
				iconSucces = imageView {
				}.lparams(width = dip(30), height = dip(30)) {
					centerInParent()
					topMargin = dip(20)
				}
			}
			
			relativeLayout {
				
				nomSucces = textView {
					id = generateViewId()
					textSize = 20f
				}
				
				descriptionSucces = textView {
				}.lparams {
					below(nomSucces)
				}
				
			}.lparams(width = matchParent, height = matchParent) {
				endOf(succesImagesLayout)
				marginStart = dip(10)
			}
		}
	}
}
