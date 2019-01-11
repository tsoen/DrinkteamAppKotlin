package drinkteam.layouts

import android.view.Gravity
import android.widget.Button
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.SignInButton
import drinkteam.loginButton
import drinkteam.signInButton
import drinkteam.vue.ConnexionActivity
import org.jetbrains.anko.*

class ConnexionRegisterLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var btnRegisterFacebook: LoginButton
	lateinit var btnRegisterGoogle: SignInButton
	lateinit var btnRegisterEmail: Button
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		verticalLayout {
			
			frameLayout {
				btnRegisterFacebook = loginButton().lparams(width = wrapContent, height = wrapContent)
			}.lparams {
				gravity = Gravity.CENTER_HORIZONTAL
				topMargin = dip(30)
				bottomMargin = dip(30)
			}
			
			frameLayout {
				btnRegisterGoogle = signInButton().lparams(width = wrapContent, height = wrapContent)
			}.lparams {
				gravity = Gravity.CENTER_HORIZONTAL
			}
			
			btnRegisterEmail = button("S'inscrire par e-mail") {
			}.lparams(width = wrapContent, height = wrapContent) {
				gravity = Gravity.CENTER_HORIZONTAL
			}
		}
	}
}
