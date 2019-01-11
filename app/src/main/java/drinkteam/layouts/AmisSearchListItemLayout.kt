package drinkteam.layouts

import android.graphics.Typeface
import android.support.v4.content.ContextCompat.getColor
import android.view.View.generateViewId
import android.widget.ImageView
import android.widget.TextView
import com.drinkteam.R
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class AmisSearchListItemLayout : AnkoComponent<MainActivity>
{
	lateinit var iconProfile: ImageView
	lateinit var nomProfile: TextView
	lateinit var titreProfile: TextView
	lateinit var separateur: ImageView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		relativeLayout {
			backgroundColor = getColor(context, R.color.white)
			
			iconProfile = imageView {
				id = generateViewId()
			}.lparams {
				//centerVertically()
				topMargin = dip(4)
				bottomMargin = dip(3)
				marginEnd = dip(15)
				marginStart = dip(15)
			}
			
			nomProfile = textView {
				id = generateViewId()
				textColor = getColor(context, R.color.black).opaque
				typeface = Typeface.DEFAULT_BOLD
				textSize = 15f
			}.lparams {
				endOf(iconProfile)
			}
			
			titreProfile = textView {
				id = generateViewId()
				textColor = getColor(context, R.color.black).opaque
				textSize = 10f
			}.lparams {
				below(nomProfile)
				alignStart(nomProfile.id)
			}
			
			separateur = imageView {
				imageResource = android.R.drawable.divider_horizontal_textfield
				scaleType = ImageView.ScaleType.FIT_XY
			}.lparams(width = matchParent) {
				below(iconProfile)
			}
		}
	}
}
