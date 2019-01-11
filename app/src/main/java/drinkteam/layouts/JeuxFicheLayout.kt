package drinkteam.layouts

import android.view.Gravity
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class JeuxFicheLayout : AnkoComponent<MainActivity>
{
	lateinit var nomJeuFiche: TextView
	lateinit var joueursJeuFiche: TextView
	lateinit var reglesJeuFiche: WebView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			scrollView {
				
				verticalLayout {
					
					nomJeuFiche = textView {
						textSize = 16f
						textAlignment = View.TEXT_ALIGNMENT_CENTER
					}.lparams(width = matchParent) {
						topMargin = dip(5)
						bottomMargin = dip(10)
					}
					
					joueursJeuFiche = textView {
						textSize = 10f
					}.lparams(width = matchParent) {
						gravity = Gravity.START
						leftMargin = dip(5)
						topMargin = dip(5)
						bottomMargin = dip(5)
						textAlignment = View.TEXT_ALIGNMENT_VIEW_START
					}
					
					reglesJeuFiche = webView {
						//textSize = 10f //pt // TODO
					}.lparams(width = matchParent) {
						gravity = Gravity.START
						leftMargin = dip(5)
						topMargin = dip(5)
						bottomMargin = dip(5)
						textAlignment = View.TEXT_ALIGNMENT_VIEW_START
					}
					
				}.lparams(width = matchParent, height = matchParent)
			}.lparams(width = matchParent, height = matchParent)
		}
	}
}
