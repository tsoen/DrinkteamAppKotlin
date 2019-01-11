package drinkteam.layouts

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class CocktailsFicheLayout : AnkoComponent<MainActivity>
{
	lateinit var nomCocktailFiche: TextView
	lateinit var iconCocktailFiche: ImageView
	lateinit var recetteCocktailFiche: TextView
	lateinit var anecdoteCocktailFiche: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			nomCocktailFiche = textView {
				textSize = 16f
				textAlignment = View.TEXT_ALIGNMENT_CENTER
			}.lparams(width = matchParent) {
				topMargin = dip(5)
				bottomMargin = dip(10)
			}
			
			iconCocktailFiche = imageView {
			}.lparams {
				gravity = Gravity.CENTER_HORIZONTAL
			}
			
			recetteCocktailFiche = textView {
				textSize = 10f
			}.lparams(width = matchParent) {
				leftMargin = dip(5)
				topMargin = dip(5)
				bottomMargin = dip(5)
				textAlignment = View.TEXT_ALIGNMENT_VIEW_START
				gravity = Gravity.START
			}
			
			anecdoteCocktailFiche = textView {
				textSize = 10f
				setTypeface(typeface, Typeface.ITALIC)
			}.lparams(width = matchParent) {
				leftMargin = dip(5)
				topMargin = dip(5)
				bottomMargin = dip(5)
				textAlignment = View.TEXT_ALIGNMENT_VIEW_START
				gravity = Gravity.START
			}
		}
	}
}
