package drinkteam

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.widget.ImageView
import drinkteam.Utils.debug
import java.net.URL
import android.graphics.drawable.BitmapDrawable

/**
 * Created by Timothée on 05/06/2017.
 *
 */
class DownloadImageTask(private val bmImage: View) : AsyncTask<String, Void, Any>()
{
	private lateinit var mode: String
	
	override fun doInBackground(vararg urls: String): Any
	{
		this.mode = urls[0]
		val urldisplay = urls[1]
		
		try
		{
			if (mode == Config.STR_USER_PROFILE)
			{
				val `in` = URL(urldisplay).openStream()
				return BitmapFactory.decodeStream(`in`)
			}
			else if (mode == Config.STR_USER_COVER)
			{
				val `is` = URL(urldisplay).openStream()
				return Drawable.createFromStream(`is`, "src name")
			}
		}
		catch (e: Exception)
		{
			debug("image download errorm: $e")
		}
		
		throw Exception()
	}
	
	override fun onPostExecute(result: Any)
	{
		if (mode == Config.STR_USER_PROFILE)
		{
			val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), result as Bitmap)
			roundedBitmapDrawable.cornerRadius = 200f
			roundedBitmapDrawable.setAntiAlias(true)
			(bmImage as ImageView).setImageDrawable(roundedBitmapDrawable)
		}
		else if (mode == Config.STR_USER_COVER)
		{
			bmImage.background = result as Drawable
			bmImage.background.setColorFilter(0x7f000000, PorterDuff.Mode.DARKEN)
			
			if(bmImage.height < (result as BitmapDrawable).bitmap.height)
			{
				bmImage.layoutParams.height = result.bitmap.height
			}
		}
	}
}
