package drinkteam.layouts

import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.ContextCompat.getDrawable
import android.text.InputType
import android.view.View.generateViewId
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.drinkteam.R
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class AmisSearchDialogLayout : AnkoComponent<MainActivity>
{
	lateinit var actionbarEditText: EditText
	lateinit var actionBarCancelSearch: Button
	lateinit var searchFriendsList: ListView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			relativeLayout {
				backgroundColor = getColor(context, R.color.black)
				
				actionBarCancelSearch = button("Annuler") {
					id = generateViewId()
					backgroundColor = getColor(context, R.color.black)
					textColor = getColor(context, R.color.white)
				}.lparams {
					centerVertically()
					alignParentEnd()
				}
				
				actionbarEditText = editText {
					backgroundResource = R.drawable.rounded_edittext
					inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
					padding = dip(5)
					hint = "Rechercher des amis"
					setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.clear_cross, 0)
				}.lparams(width = matchParent) {
					centerVertically()
					startOf(actionBarCancelSearch)
				}
			}.lparams(width = matchParent, height = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimension(0, 0.toFloat()).toInt())
			
			searchFriendsList = listView {
				dividerHeight = 0
			}.lparams(width = matchParent, height = matchParent)
		}
	}
}
