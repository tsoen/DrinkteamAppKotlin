package drinkteam.layouts

import android.widget.Button
import drinkteam.vue.ConnexionActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.verticalLayout

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class ConnexionNotConnectedLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var btnNewAccount: Button
	lateinit var btnConnection: Button
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		verticalLayout {
			
			btnNewAccount = button {
				text = "Creer un compte"
			}
			
			btnConnection = button {
				text = "Deja utilisateur ? Connectez-vous"
			}
		}
	}
}
