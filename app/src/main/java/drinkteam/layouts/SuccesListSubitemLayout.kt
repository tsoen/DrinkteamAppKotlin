package drinkteam.layouts

import android.view.View.generateViewId
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.style

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class SuccesListSubitemLayout : AnkoComponent<MainActivity>
{
	lateinit var palierSucces: TextView
	lateinit var anecdoteSucces: TextView
	lateinit var nouvelleDemandeSucces: Button
	lateinit var separateur: ImageView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			
			palierSucces = textView {
				id = generateViewId()
			}.lparams(width = matchParent){
				leftMargin = dip(5)
				rightMargin = dip(5)
			}
			
			anecdoteSucces = textView {
				id = generateViewId()
			}.lparams {
				below(palierSucces)
				alignStart(palierSucces.id)
			}
			
			nouvelleDemandeSucces = button("Faire une demande") {
				id = generateViewId()
				minHeight = 0
				minimumHeight = 0
				textSize = 10f
				setPadding(dip(10), dip(10), dip(10), dip(10))
			}.lparams(width = wrapContent, height = wrapContent) {
				below(anecdoteSucces)
				marginStart = dip(-3)
				alignStart(palierSucces.id)
			}
			
			separateur = imageView {
				imageResource = android.R.drawable.divider_horizontal_textfield
				scaleType = ImageView.ScaleType.FIT_XY
			}.lparams(width = matchParent) {
				below(nouvelleDemandeSucces)
				topMargin = dip(5)
			}
		}
	}
}
