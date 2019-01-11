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
class CocktailsListItemLayout : AnkoComponent<MainActivity>
{
	lateinit var cocktailLineLayout: RelativeLayout
	lateinit var iconCocktail: ImageView
	lateinit var nomCocktail: TextView
	lateinit var alcoolCocktail: TextView
	lateinit var separateur: ImageView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			cocktailLineLayout = this
			
			iconCocktail = imageView {
				id = generateViewId()
			}.lparams {
				marginEnd = dip(10)
				marginStart = dip(5)
				centerVertically()
			}
			
			nomCocktail = textView {
				id = generateViewId()
				textSize = 20f
			}.lparams {
				rightOf(iconCocktail)
			}
			
			alcoolCocktail = textView {
				id = generateViewId()
			}.lparams {
				below(nomCocktail)
				rightOf(iconCocktail)
			}
			
			separateur = imageView {
				imageResource = android.R.drawable.divider_horizontal_textfield
				scaleType = ImageView.ScaleType.FIT_XY
			}.lparams(width = matchParent) {
				below(alcoolCocktail)
				topMargin = dip(5)
			}
		}
	}
}
