package drinkteam._old

import android.os.AsyncTask
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadWebpageTask
/**
 * Constructeur - Appelle doInBackground
 * @param callback Object qui définit l'action à réaliser une fois la tâche terminée
 */
	( // Object qui définit l'action à réaliser une fois la tâche terminée //
	private val callback: AsyncResult) : AsyncTask<String, Void, String>()
{
	/**
	 * Traitement à réaliser
	 * Appelé par DownloadWebpageTask.execute()
	 * @param urls URLs des fichiers à lire - nombre de paramètres (URLs) indéfinis
	 * @return Contenu texte du fichier
	 */
	override fun doInBackground(vararg urls: String): String
	{
		try
		{
			return downloadUrl(urls[0])
		}
		catch (e: IOException)
		{
			return "Unable to download the requested page."
		}
	}
	
	/**
	 * Appelé à la fin du traitement
	 * @param result String retournée par doInBackground()
	 */
	override fun onPostExecute(result: String)
	{
		// supprime les headers inutiles //
		val start = result.indexOf("{", result.indexOf("{") + 1)
		val end = result.lastIndexOf("}")
		val jsonResponse = result.substring(start, end)
		
		try
		{
			// parse le contenu du fichier en JSON et déclenche le traitement final //
			val table = JSONObject(jsonResponse)
			callback.onResult(table)
		}
		catch (e: JSONException)
		{
			e.printStackTrace()
		}
	}
	
	/**
	 * Se connecte à un fichier, le lit et renvoi son contenu sous forme de texte
	 * @param urlString URL du fichier à lire
	 * @return Le contenu du fichier sous forme de texte
	 * @throws IOException Exception
	 */
	@Throws(IOException::class)
	private fun downloadUrl(urlString: String): String
	{
		lateinit var inputStream: InputStream
		
		try
		{
			// connexion au fichier à l'URL donnée //
			val url = URL(urlString)
			val conn = url.openConnection() as HttpURLConnection
			// timeout pour se connecter au fichier (millisecondes) //
			conn.connectTimeout = 15000
			// timeout pour lire le fichier (millisecondes) //
			conn.readTimeout = 10000
			
			conn.requestMethod = "GET"
			conn.doInput = true
			
			conn.connect()
			inputStream = conn.inputStream
			
			return convertStreamToString(inputStream)
		}
		finally
		{
			inputStream.close()
		}
	}
	
	/**
	 * Lit un fichier et renvoi son contenu sous forme de texte
	 * @param inputStream Flux de lecture du fichier
	 * @return Le contenu du fichier sous forme de texte
	 */
	private fun convertStreamToString(inputStream: InputStream): String
	{
		val reader = BufferedReader(InputStreamReader(inputStream))
		var res = ""
		try
		{
			while (reader.readLine() != null)
			{
				res += reader.readLine() + "\n"
			}
		}
		catch (e: IOException)
		{
			e.printStackTrace()
		}
		finally
		{
			try
			{
				inputStream.close()
			}
			catch (e: IOException)
			{
				e.printStackTrace()
			}
		}
		
		return res
	}
}
