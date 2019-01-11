package drinkteam.layouts

import android.graphics.Typeface
import android.support.v17.leanback.widget.HorizontalGridView
import android.support.v4.content.ContextCompat.getColor
import android.view.View.generateViewId
import android.widget.*
import android.widget.ImageView.ScaleType
import com.drinkteam.R
import drinkteam.horizontalGridView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

class AmisProfilLayout : AnkoComponent<MainActivity>
{
	lateinit var profileHeader: RelativeLayout
	lateinit var profilePic: ImageView
	lateinit var profileName: TextView
	lateinit var profileTitre: TextView
	lateinit var profileAmbiance: TextView
	
	lateinit var profileTextAmis: TextView
	lateinit var profileAmisList: HorizontalGridView
	
	lateinit var profileSuccesTitre: TextView
	lateinit var profileSuccesButton: Button
	lateinit var profileSuccesList: HorizontalGridView
	
	lateinit var profileDemandesTitre: TextView
	lateinit var profileDemandesList: ListView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		scrollView {
			isFillViewport = true
			
			verticalLayout {
				
				profileHeader = relativeLayout {
					
					profilePic = imageView {
						id = generateViewId()
						scaleType = ScaleType.FIT_XY
					}.lparams(width = dip(100), height = dip(100)) {
						centerHorizontally()
						topMargin = dip(20)
						bottomMargin = dip(10)
					}
					
					profileName = textView {
						id = generateViewId()
						textColor = getColor(context, R.color.white).opaque
						typeface = Typeface.DEFAULT_BOLD
						textSize = 20f
					}.lparams(width = wrapContent, height = wrapContent) {
						below(profilePic)
						centerHorizontally()
					}
					
					profileTitre = textView {
						id = generateViewId()
						textColor = getColor(context, R.color.white).opaque
						textSize = 15f
					}.lparams(width = wrapContent, height = wrapContent) {
						below(profileName)
						centerHorizontally()
					}
					
					profileAmbiance = textView {
						textColor = getColor(context, R.color.white).opaque
						textSize = 15f
					}.lparams(width = wrapContent, height = wrapContent) {
						below(profileTitre)
						centerHorizontally()
					}
				}.lparams(width = matchParent, height = wrapContent)
				
				relativeLayout {
					
					profileTextAmis = textView("Amis en commun") {
						textSize = 20f
					}.lparams(width = wrapContent, height = wrapContent) {
						topMargin = dip(10)
						marginStart = dip(5)
					}
					
					frameLayout {
						profileAmisList = horizontalGridView().lparams(width = wrapContent, height = dip(110))
					}
				}.lparams(width = matchParent, height = wrapContent)
				
				relativeLayout {
					
					profileSuccesTitre = textView("Derniers succès") {
						id = generateViewId()
						textSize = 20f
					}.lparams(width = wrapContent, height = wrapContent) {
						topMargin = dip(10)
						marginStart = dip(5)
					}
					
					profileSuccesButton = button("Tout voir") {
						padding = dip(0)
						textSize = 10f
					}.lparams(width = wrapContent, height = wrapContent) {
						sameTop(profileSuccesTitre)
						sameBottom(profileSuccesTitre)
						alignParentEnd()
					}
					
					frameLayout {
						profileSuccesList = horizontalGridView().lparams(width = wrapContent, height = dip(110))
					}.lparams {
						below(profileSuccesTitre)
					}
				}.lparams(width = wrapContent, height = wrapContent)
				
				profileDemandesTitre = textView("Demandes en attente") {
					textSize = 20f
				}.lparams(width = wrapContent, height = wrapContent) {
					topMargin = dip(10)
					marginStart = dip(5)
				}
				
				profileDemandesList = listView {
				}.lparams(width = matchParent, height = wrapContent)
			}.lparams(width = matchParent, height = wrapContent)
		}
	}
}
