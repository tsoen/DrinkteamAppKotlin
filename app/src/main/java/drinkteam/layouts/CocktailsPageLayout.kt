package drinkteam.layouts

import android.widget.GridView
import android.widget.ImageView
import android.widget.Spinner
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class CocktailsPageLayout : AnkoComponent<MainActivity>
{
	lateinit var cocktailsSpinner: Spinner
	lateinit var gridviewCocktails: GridView
	lateinit var separateur: ImageView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			cocktailsSpinner = spinner {
			}.lparams(width = matchParent)
			
			separateur = imageView {
				imageResource = android.R.drawable.divider_horizontal_textfield
				scaleType = ImageView.ScaleType.FIT_XY
			}.lparams(width = matchParent)
			
			gridviewCocktails = gridView {
				//android:numColumns = 1 //not support attribute TODO
			}.lparams(width = matchParent, height = matchParent)
		}
	}
}
