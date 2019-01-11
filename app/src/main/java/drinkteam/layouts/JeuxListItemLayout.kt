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
class JeuxListItemLayout : AnkoComponent<MainActivity>
{
	lateinit var jeuLineLayout: RelativeLayout
	lateinit var nomJeu: TextView
	lateinit var iconJeu: ImageView
	lateinit var joueursJeu: TextView
	lateinit var separateur: ImageView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			jeuLineLayout = this
			topPadding = dip(5)
			
			nomJeu = textView {
				id = generateViewId()
				textSize = 20f
				topPadding = 0
			}.lparams {
				marginStart = dip(5)
				
			}
			
			iconJeu = imageView {
			}
			
			joueursJeu = textView {
				id = generateViewId()
			}.lparams {
				below(nomJeu)
				alignStart(nomJeu.id)
			}
			
			separateur = imageView {
				imageResource = android.R.drawable.divider_horizontal_textfield
				scaleType = ImageView.ScaleType.FIT_XY
				topPadding = dip(5)
			}.lparams(width = matchParent) {
				below(joueursJeu)
			}
		}
	}
}
