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
class ConnexionRegisterEmailLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var textRegisterEmailEmail: EditText
	lateinit var textRegisterEmailPassword: EditText
	lateinit var btnRegisterEmail: Button
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		verticalLayout {
			
			textRegisterEmailEmail = editText {
				inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
				hint = "Adresse email"
			}.lparams(width = matchParent)
			
			textRegisterEmailPassword = editText {
				inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
				hint = "Choisir un mot de passe"
			}.lparams(width = matchParent)
			
			btnRegisterEmail = button {
			}.lparams(width = matchParent)
		}
	}
}
