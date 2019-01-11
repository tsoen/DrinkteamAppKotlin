package drinkteam.layouts

import android.view.Gravity
import android.widget.Button
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.SignInButton
import drinkteam.loginButton
import drinkteam.signInButton
import drinkteam.vue.ConnexionActivity
import org.jetbrains.anko.*

class ConnexionLoginLayout : AnkoComponent<ConnexionActivity>
{
	lateinit var btnLoginFacebook: LoginButton
	lateinit var btnLoginGoogle: SignInButton
	lateinit var btnLoginEmail: Button
	
	override fun createView(ui: AnkoContext<ConnexionActivity>) = with(ui) {
		verticalLayout {
			
			frameLayout {
				btnLoginFacebook = loginButton().lparams(width = wrapContent, height = wrapContent)
			}.lparams {
				gravity = Gravity.CENTER_HORIZONTAL
				topMargin = dip(30)
				bottomMargin = dip(30)
			}
			
			frameLayout {
				btnLoginGoogle = signInButton().lparams(width = wrapContent, height = wrapContent)
			}.lparams {
				gravity = Gravity.CENTER_HORIZONTAL
			}
			
			btnLoginEmail = button("Se connecter par e-mail") {
			}.lparams(width = wrapContent, height = wrapContent) {
				gravity = Gravity.CENTER_HORIZONTAL
			}
		}
	}
}