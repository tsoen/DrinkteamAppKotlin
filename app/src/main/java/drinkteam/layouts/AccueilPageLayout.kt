package drinkteam.layouts

import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.HorizontalGridView
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getColor
import android.view.View.generateViewId
import android.widget.Button
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.RelativeLayout
import android.widget.TextView
import com.drinkteam.R
import drinkteam.horizontalGridView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

class AccueilPageLayout : AnkoComponent<MainActivity>
{
	lateinit var accueilProfileLayout: RelativeLayout
	lateinit var accueilProfilePic: ImageView
	lateinit var accueilProfileName: TextView
	lateinit var accueilProfileTitre: TextView
	
	lateinit var accueilSuccesTitre: TextView
	lateinit var accueilSuccesButton: Button
	lateinit var accueilSuccesList: HorizontalGridView
	
	lateinit var accueilCocktailTitre: TextView
	lateinit var accueilCocktailLogo: ImageView
	lateinit var weeklyCocktailName: TextView
	lateinit var cocktailFicheButton: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			accueilProfileLayout = relativeLayout {
				topPadding = dip(20)
				rightPadding = dip(0)
				bottomPadding = dip(20)
				leftPadding = dip(40)
				
				accueilProfilePic = imageView {
					id = generateViewId()
					scaleType = ScaleType.FIT_XY
				}.lparams(width = dip(60), height = dip(60)) {
					centerVertically()
				}
				
				accueilProfileName = textView {
					id = generateViewId()
					textColor = getColor(context, R.color.white).opaque
					typeface = Typeface.DEFAULT_BOLD
					textSize = 20f
				}.lparams(width = wrapContent, height = wrapContent){
					endOf(accueilProfilePic)
					sameTop(accueilProfilePic)
					marginStart = dip(10)
				}
				
				accueilProfileTitre = textView {
					textColor = getColor(context, R.color.white).opaque
					textSize = 15f
				}.lparams(width = wrapContent, height = wrapContent) {
					below(accueilProfileName)
					sameStart(accueilProfileName)
				}
			}.lparams(width = matchParent, height = wrapContent)
			
			relativeLayout {
				
				accueilSuccesTitre = textView("Derniers succès") {
					id = generateViewId()
					textSize = 20f
				}.lparams(width = wrapContent, height = wrapContent){
					marginStart = dip(5)
				}
				
				accueilSuccesButton = button("Tout voir") {
					padding = dip(0)
					textSize = 10f
				}.lparams(width = wrapContent, height = wrapContent) {
					sameTop(accueilSuccesTitre)
					sameBottom(accueilSuccesTitre)
					alignParentEnd()
				}
				
				frameLayout {
					accueilSuccesList = horizontalGridView().lparams(width = wrapContent, height = dip(110))
				}.lparams {
					below(accueilSuccesTitre)
				}
			}.lparams(width = wrapContent, height = wrapContent) {
				topMargin = dip(5)
			}
			
			relativeLayout {
				topPadding = dip(5)
				rightPadding = dip(10)
				bottomPadding = dip(5)
				leftPadding = dip(10)
				
				accueilCocktailTitre = textView("Cocktail de la semaine") {
					id = generateViewId()
					textSize = 20f
				}.lparams(width = wrapContent, height = wrapContent){
					topMargin = dip(5)
					bottomMargin = dip(5)
				}
				
				accueilCocktailLogo = imageView {
				}.lparams(width = wrapContent, height = wrapContent) {
					below(accueilCocktailTitre)
					centerVertically()
					topMargin = dip(10)
				}
				
				relativeLayout {
					
					weeklyCocktailName = textView {
						id = generateViewId()
						typeface = Typeface.DEFAULT_BOLD
						textSize = 15f
					}.lparams(width = wrapContent, height = wrapContent) {
						centerHorizontally()
					}
					
					cocktailFicheButton = button("Voir la fiche complète") {
					}.lparams(width = wrapContent, height = wrapContent) {
						below(weeklyCocktailName)
						centerHorizontally()
					}
				}.lparams(width = matchParent, height = wrapContent) {
					below(accueilCocktailTitre)
				}
			}.lparams(width = matchParent, height = wrapContent)
		}
	}
}
