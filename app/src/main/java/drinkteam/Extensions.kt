package drinkteam

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v17.leanback.widget.HorizontalGridView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.ViewManager
import android.widget.Button
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.SignInButton
import com.google.android.gms.maps.MapView
import drinkteam.vue.ConnexionActivity
import drinkteam.vue.MainActivity
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by timot on 01/02/2018.
 *
 */

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit, addToBackStack: Boolean)
{
	val fragmentTransaction = beginTransaction()
	fragmentTransaction.func()
	if (addToBackStack)
	{
		fragmentTransaction.addToBackStack(null)
	}
	fragmentTransaction.commit()
}

fun AppCompatActivity.replaceFragment(context: Context, fragment: Fragment, bundle: Bundle?, addToBackStack: Boolean)
{
	fragment.arguments = bundle
	if (context is MainActivity)
	{
		supportFragmentManager.inTransaction({ replace(context.layout.fragmentContainer.id, fragment) }, addToBackStack)
	}
	else if (context is ConnexionActivity)
	{
		supportFragmentManager.inTransaction({ replace(context.layout.fragmentContainer.id, fragment) }, addToBackStack)
	}
}

fun Button.loginFacebook(func: (result: LoginResult) -> Unit, activity: Activity)
{
	LoginManager.getInstance().registerCallback(Utils.callbackManager, object : FacebookCallback<LoginResult>
	{
		/**
		 * Si succès de la connexion à facebook
		 * @param result Objet représentant le résultat de la connexion
		 */
		override fun onSuccess(result: LoginResult)
		{
			func(result)
		}
		
		/**
		 * Annulation de la connexion à facebook
		 */
		override fun onCancel()
		{
		}
		
		/**
		 * Erreur lors de la connexion à facebook
		 * @param error Object décrivant l'erreur de connexion
		 */
		override fun onError(error: FacebookException)
		{
			context.toast("Error occurred while logging in. Please try again.")
		}
	})
	
	LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_friends"))
}

fun ViewManager.mapView() = mapView {}
inline fun ViewManager.mapView(init: MapView.() -> Unit): MapView
{
	return ankoView({ MapView(it) }, theme = 0, init = init)
}

fun ViewManager.horizontalGridView() = horizontalGridView {}
inline fun ViewManager.horizontalGridView(init: HorizontalGridView.() -> Unit): HorizontalGridView
{
	return ankoView({ HorizontalGridView(it) }, theme = 0, init = init)
}

fun ViewManager.loginButton() = loginButton {}
inline fun ViewManager.loginButton(init: LoginButton.() -> Unit): LoginButton
{
	return ankoView({ LoginButton(it) }, theme = 0, init = init)
}

fun ViewManager.signInButton() = signInButton {}
inline fun ViewManager.signInButton(init: SignInButton.() -> Unit): SignInButton
{
	return ankoView({ SignInButton(it) }, theme = 0, init = init)
}