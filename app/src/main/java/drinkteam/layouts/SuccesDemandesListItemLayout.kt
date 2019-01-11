package drinkteam.layouts

import android.view.Gravity
import android.view.View.generateViewId
import android.widget.Button
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
class SuccesDemandesListItemLayout : AnkoComponent<MainActivity>
{
	lateinit var succesLineLayout: RelativeLayout
	lateinit var succesImagesLayout: RelativeLayout
	lateinit var iconNiveau: ImageView
	lateinit var iconSucces: ImageView
	lateinit var succesTextLayout: RelativeLayout
	lateinit var nomSucces: TextView
	lateinit var descriptionSucces: TextView
	lateinit var succesDemandeAcceptButton: Button
	lateinit var succesDemandeRefuseButton: Button
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			succesLineLayout = this
			
			succesImagesLayout = relativeLayout {
				id = generateViewId()
				
				iconNiveau = imageView {
				}.lparams(width = dip(80), height = dip(70)) {
					centerInParent()
				}
				
				iconSucces = imageView {
				}.lparams(width = dip(25), height = dip(25)) {
					centerInParent()
				}
			}.lparams(width = wrapContent, height = matchParent){
				centerVertically()
			}
			
			succesTextLayout = relativeLayout {
				
				nomSucces = textView {
					id = generateViewId()
					textSize = 15f
				}
				
				descriptionSucces = textView {
					id = generateViewId()
				}.lparams {
					below(nomSucces)
				}
				
				succesDemandeAcceptButton = button("OK") {
					id = generateViewId()
					padding = dip(5)
					minHeight = 0
					minWidth = 0
				}.lparams {
					sameTop(descriptionSucces)
					alignParentEnd()
				}
				
				succesDemandeRefuseButton = button("KO") {
					padding = dip(5)
					minHeight = 0
					minWidth = 0
				}.lparams {
					below(succesDemandeAcceptButton)
					alignParentEnd()
				}
				
			}.lparams(width = matchParent, height = matchParent) {
				marginStart = 0
				endOf(succesImagesLayout)
			}
		}
	}
}
