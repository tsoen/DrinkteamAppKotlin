package drinkteam.layouts

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.drawerLayout

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class MainActivityLayout : AnkoComponent<MainActivity>
{
	lateinit var drawerLayout: DrawerLayout
	lateinit var fragmentContainer: FrameLayout
	lateinit var myDrawer: ListView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		drawerLayout {
			drawerLayout = this
			
			fragmentContainer = frameLayout {
				id = View.generateViewId()
			}.lparams(width = matchParent, height = matchParent)
			
			myDrawer = listView {
				backgroundColor = Color.parseColor("#333333")
				choiceMode = ListView.CHOICE_MODE_SINGLE
				//divider = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.divider_horizontal_textfield)
				divider = ColorDrawable(Color.parseColor("#AAAAAA"))
				dividerHeight = dip(1)
			}.lparams(width = dip(240), height = matchParent) {
				gravity = Gravity.START
			}
		}
	}
}
