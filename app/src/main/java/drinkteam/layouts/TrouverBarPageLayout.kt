package drinkteam.layouts

import com.google.android.gms.maps.MapView
import drinkteam.mapView
import drinkteam.vue.MainActivity
import org.jetbrains.anko.*

class TrouverBarPageLayout : AnkoComponent<MainActivity>
{
	lateinit var mapView: MapView
	
	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			
			frameLayout {
				mapView = mapView().lparams(width = matchParent, height = matchParent)
			}
		}
	}
}