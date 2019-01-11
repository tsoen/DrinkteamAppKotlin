package drinkteam.layouts

import android.widget.Button
import com.drinkteam.R
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class DialogPhotoLayout : AnkoComponent<MainActivity>
{
	lateinit var takePic: Button
	lateinit var fromAlbum: Button
	lateinit var updatePicture: Button
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			takePic = button {
				text = resources.getString(R.string.takePic)
			}.lparams(width = matchParent, height = dip(50))
			
			fromAlbum = button {
				text = resources.getString(R.string.fromAlbum)
			}.lparams(width = matchParent, height = dip(50))
			
			updatePicture = button {
			}.lparams(width = matchParent, height = dip(50))
		}
	}
}
