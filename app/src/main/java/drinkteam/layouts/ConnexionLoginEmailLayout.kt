package drinkteam.layouts

import android.text.InputType
import android.widget.Button
import android.widget.EditText
import drinkteam.vue.ConnexionActivity
import org.jetbrains.anko.*

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class ConnexionLoginEmailLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var textLoginEmailEmail: EditText
	lateinit var textLoginEmailPassword: EditText
	lateinit var btnLoginEmail: Button
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		verticalLayout {
			
			textLoginEmailEmail = editText {
				inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
				hint = "Adresse email"
			}.lparams(width = matchParent)
			
			textLoginEmailPassword = editText {
				inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
				hint = "Choisir un mot de passe"
			}.lparams(width = matchParent)
			
			btnLoginEmail = button {
			}.lparams(width = matchParent)
		}
	}
}
