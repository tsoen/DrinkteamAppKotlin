package drinkteam.layouts

import android.support.v4.content.ContextCompat.getColor
import android.view.Gravity
import android.widget.TextView
import com.drinkteam.R
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

class ActionbarLayout : AnkoComponent<MainActivity>
{
	lateinit var titreActionBar: TextView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			titreActionBar = textView {
				gravity = Gravity.CENTER_HORIZONTAL
				textColor = getColor(context, R.color.white).opaque
				textSize = 20f
			}.lparams(width = wrapContent, height = wrapContent)
		}
	}
}