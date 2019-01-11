package drinkteam

import android.content.Context
import android.os.Bundle
import android.support.v17.leanback.widget.HorizontalGridView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ListView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import drinkteam.Config.TAG
import drinkteam.controleur.CocktailDAO
import drinkteam.controleur.JeuDAO
import drinkteam.controleur.SuccesDAO
import drinkteam.metier.User
import drinkteam.vue.MainActivity
import drinkteam.vue.SuccesDemandesListViewAdapter
import drinkteam.vue.SuccesHorizontalGridViewAdapter
import drinkteam.vue.SuccesListViewAdapter
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.net.URL
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Timothée on 05/07/2017.
 *
 * Définit des attributs et méthodes utilisés à plusieurs endroits
 * et qui ne sont pas spécifiques à une classe
 *
 * - Référence aux bases de données
 * - Données sur les succès
 * - Références Firebase
 * - Connexion et création de compte
 * - Navigation dans les fragments
 * - Algorithme de recherche
 */
object Utils
{
	//region ////////// DONNES LOCALES //////////
	
	// base de données des Succès //
	lateinit var dbSucces: SuccesDAO
	
	// base de données des Cocktails //
	lateinit var dbCocktails: CocktailDAO
	
	// base de données des Jeux //
	lateinit var dbJeux: JeuDAO
	
	// données sur les succes obtenus par un utilisateur //
	lateinit var listDataSuccesAnyUser: SparseArray<List<String>>
	
	// liste des grades obtenus pour chaque succès d'un utilisateur //
	lateinit var niveauxSuccesAnyUser: List<String>
	
	var weeklyCocktailID: Long = 0
	
	//endregion
	
	//region ////////// FIREBASE //////////
	
	// reference à la base de données Firebase //
	var firebaseDB: DatabaseReference = FirebaseDatabase.getInstance().reference
	
	// reference au cloud storage de Firebase //
	var storage = FirebaseStorage.getInstance().reference
	
	//endregion
	
	/*
	 * Utilitaires de connexion à l'application et de création de compte
	 */
	//region ////////// CONNEXION //////////
	
	// détecte la connexion à Firebase via facebook //
	var authListener: AuthStateListener? = null
	
	// point d'entrée principal pour la connexion à Google//
	lateinit var mGoogleApiClient: GoogleApiClient
	
	// gère les appels du bouton de connexion à Facebook //
	lateinit var callbackManager: CallbackManager
	
	// point d'entrée à l'outil d'authentification Firebase //
	val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
	
	fun storeFileByStream(url: String, storageNode: StorageReference)
	{
		Thread(Runnable {
			try
			{
				val stream = URL(url).openStream()
				storageNode.putStream(stream)
			}
			catch (e: Exception)
			{
				debug("storeFileByStream : $e")
			}
		}).start()
	}
	
	/**
	 * Enregistre l'utilisateur dans la base de données firebase et le redirige vers l'application
	 * @param context Informations globales sur l'environnement de l'applicaiotn
	 */
	fun registerUser(context: Context)
	{
		// récupération de l'utilisateur firebase connecté //
		val user = mAuth.currentUser
		
		// si il y a bien un utilisateur connecté //
		if (user != null)
		{
			// objet représentant l'utilisateur créé //
			val newUser = User()
			
			// récupération des informations de l'utilisateur firebase //
			// toujours à la fois sur firebase et un autre provider //
			for (profile in user.providerData)
			{
				// provider d'authentification //
				val providerId = profile.providerId
				
				// les informations générales sont centralisées dans le profil firebase //
				if (providerId == "firebase")
				{
					newUser.id = profile.uid
					newUser.name = profile.displayName
					newUser.email = profile.email
					
					// titre de base à la création du compte //
					newUser.titre = "Débutant"
					
					// enregistrement asynchrone de la photo de profile dans le storage firebase //
					Thread(Runnable
					{
						try
						{
							// enregistrement de la photo de profile sous forme de flux de données //
							val stream = URL(user.photoUrl!!.toString()).openStream()
							storage.child(Config.STR_NODE_COMPTES).child(newUser.id!!).child(
								Config.STR_USER_PROFILE).putStream(stream)
						}
						catch (e: Exception)
						{
							debug("registerUser Profile: $e")
						}
					}).start()
				}
				// données propres à l'autre provider //
				else
				{
					// enregistre l'autre provider d'identification (facebook.com) //
					newUser.provider = providerId
					
					// identifiant de l'utilisateur propre à ce provider //
					newUser.providerid = profile.uid
					
					if (providerId == "facebook.com")
					{
						// requête pour obtenir la photo de couverture facebook //
						val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { `object`, _ ->
							try
							{
								// `object` Données récupérées par la requête encodées en JSON //
								val url = `object`.getJSONObject("cover").getString("source")
								
								// enregistrement asynchrone de la photo de couverture dans le storage firebase //
								Thread(Runnable {
									try
									{
										val stream = URL(url).openStream()
										storage.child(Config.STR_NODE_COMPTES).child(newUser.id!!).child(
											Config.STR_USER_COVER).putStream(stream)
									}
									catch (e: Exception)
									{
										debug("registerUser Couverture: $e")
									}
								}).start()
							}
							catch (ex: Exception)
							{
								debug("registerUser couverture request: $ex")
							}
						}
						val parameters = Bundle()
						
						// indique qu'on souhaite récupérer la photo de couverture //
						parameters.putString("fields", "cover")
						request.parameters = parameters
						request.executeAsync()
					}
				}
			}
			
			// tentative de création du compte dans la base de données //
			// si un conflit existe (un autre utilisateur s'enregistre en même temps), une transaction se relance jusqu'à réussite //
			firebaseDB.child(Config.DB_NODE_COMPTES).child(newUser.id!!).runTransaction(object : Transaction.Handler
			{
				/**
				 * Une fois la transaction terminée
				 * @param databaseError Null ou contient l'erreur survenue
				 * @param committed True si succès, false si erreur survenue
				 * @param dataSnapshot Nouvelles données présentes à l'endroit spécifié
				 */
				override fun onComplete(databaseError: DatabaseError?, committed: Boolean, dataSnapshot: DataSnapshot?)
				{
					// si l'enregistrement dans la base a fonctionné //
					if (databaseError == null)
					{
						// ouverture de l'activité principale de l'application //
						context.startActivity<MainActivity>()
					}
				}
				
				/**
				 * Tentative de création du compte
				 * @param currentData Données existantes à l'endroit spécifié
				 * @return Résultat de la transaction
				 */
				override fun doTransaction(currentData: MutableData): Transaction.Result
				{
					// création du compte si il n'existe pas de données avec cette ID //
					if (currentData.value == null)
					{
						// enregistrement automatique des données à partir d'un object User //
						currentData.value = newUser
						
						return Transaction.success(currentData)
					}
					
					return Transaction.success(currentData)
				}
			})
			
			// supprime le listener pour ne pas le déclencher à la déconnexion //
			if (authListener != null)
			{
				mAuth.removeAuthStateListener(authListener as AuthStateListener)
			}
		}
	}
	
	/**
	 * Redirige l'utilisateur vers l'application si son compte existe dans la base
	 * @param context Contexte de l'application
	 */
	fun loginUser(context: Context)
	{
		// récupération de l'utilisateur connecté //
		val user = mAuth.currentUser
		
		// si il y a bien un utilisateur connecté //
		if (user != null)
		{
			// vérification de l'existence du compte dans la base de données //
			firebaseDB.child(Config.DB_NODE_COMPTES).child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener
			{
				/**
				 * Essaye de récupérer les données à l'endroit précisé
				 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
				 * @param snapshot Contient les données présentes à cet emplacement
				 */
				override fun onDataChange(snapshot: DataSnapshot)
				{
					// si données (compte) existent //
					if (snapshot.exists())
					{
						// ouverture de l'activité principale de l'application //
						context.startActivity<MainActivity>()
					}
					// si le compte n'est pas enregistré dans la base de données, il faut déconnecter l'utilisateur //
					else
					{
						// déconnexion de facebook //
						LoginManager.getInstance().logOut()
						
						// déconnexion de google //
						Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { }
						
						// message d'erreur //
						context.toast("Compte inexistant")
					}
				}
				
				/**
				 * Arrêt de la récupération des données
				 * @param databaseError Erreur éventuelle
				 */
				override fun onCancelled(databaseError: DatabaseError)
				{
					debug("loginUser $databaseError")
				}
			})
			
			// supprime le listener pour ne pas le déclencher à la déconnexion à firebase //
			if (authListener != null)
			{
				mAuth.removeAuthStateListener(authListener as AuthStateListener)
			}
		}
	}
	
	//endregion
	
	/**
	 * Calcule et affiche les images des niveaux obtenus pour les succès d'un joueur
	 * TODO: les algos de détection et stockage des succès puent bien la merde
	 * @param bundle Paramètre supplémentaires
	 * @param view Vue de la liste contenant les images de niveaux
	 */
	fun setUserNiveauSucces(bundle: Bundle, view: View)
	{
		firebaseDB.child(Config.DB_NODE_SCORES).child(bundle.getString("id")!!).addListenerForSingleValueEvent(object : ValueEventListener
		{
			override fun onDataChange(dataSnapshot: DataSnapshot)
			{
				// liste de tous les succès existant //
				val listSucces = dbSucces.allSucces
				
				// liste des données des données succès-score de l'utilisateur //
				val dataSucces = SparseArray<List<String>>()
				
				// liste des grades obtenu pour chaque succès //
				val niveauSucces = ArrayList<String>()
				
				if (dataSnapshot.exists())
				{
					// récupération des données pour chaque succès obtenu //
					for (succesSnapshot in dataSnapshot.children)
					{
						val data = ArrayList<String>()
						data.add(0, succesSnapshot.child(Config.DB_SUCCES_SCORE).value!!.toString())
						data.add(1, succesSnapshot.child(Config.DB_SUCCES_DATE).value!!.toString())
						dataSucces.append(Integer.parseInt(succesSnapshot.key), data)
					}
				}
				// définition de l'image en fonction du palier atteint //
				var imagePath: String
				
				// parcours de tous les succès possibles //
				for (pos in listSucces.indices)
				{
					var score = 0
					
					// si l'utilisateur possède ce succès dans sa liste //
					if (dataSucces.indexOfKey(pos) >= 0)
					{
						// récupération du score //
						score = Integer.parseInt(dataSucces.get(pos)[0])
					}
					
					// calcul du palier atteint par l'utilisateur //
					val palier = listSucces[pos].palier
					val facteur = listSucces[pos].facteur
					var i = 0
					var prochainPalier = 0
					while (score >= prochainPalier)
					{
						prochainPalier = if (facteur == 1)
						{
							palier + palier * i
						}
						else
						{
							(palier * Math.pow(facteur.toDouble(), i.toDouble())).toInt()
						}
						
						i++
					}
					
					// définit l'image en fonction du palier atteint //
					imagePath = when (i - 1)
					{
						0 -> Config.path + "niveau_inconnu"
						1 -> Config.path + "niveau_bronze"
						2 -> Config.path + "niveau_argent"
						3 -> Config.path + "niveau_or"
						4 -> Config.path + "niveau_platine"
						5 -> Config.path + "niveau_diamant"
						6 -> Config.path + "niveau_master"
						7 -> Config.path + "niveau_challenger"
						else -> Config.path + "niveau_challenger"
					}
					
					niveauSucces.add(imagePath)
				}
				
				niveauxSuccesAnyUser = niveauSucces
				listDataSuccesAnyUser = dataSucces
				
				// HorizontalGridView de la page de profil //
				if (view is HorizontalGridView)
				{
					val adapter = SuccesHorizontalGridViewAdapter(view.context, bundle.getString("id"))
					view.adapter = adapter
				}
				else if (view is ExpandableListView)
				{
					val adapter = SuccesListViewAdapter(view.context, bundle.getString("id"))
					view.setAdapter(adapter)
					
					var pos = -1
					for (i in listSucces.indices)
					{
						val code = Integer.parseInt(listSucces[i].codecategorie.toString() + "" + listSucces[i].codesucces)
						
						if (code == bundle.getInt("succesID"))
						{
							pos = i
							break
						}
					}
					
					if (pos != -1)
					{
						view.expandGroup(pos)
					}
				}
				// ExpandableListView de la page des succès //
				else if (view is ListView)
				{
					firebaseDB.child(Config.DB_NODE_DEMANDES).child(bundle.getString("id")!!).addListenerForSingleValueEvent(object : ValueEventListener
					{
						/**
						 * Détecte un changement de donnée dans la base à l'emplacement défini
						 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
						 * @param dataSnapshot Contient les données présentes à cet emplacement
						 */
						override fun onDataChange(dataSnapshot: DataSnapshot)
						{
							val positions = ArrayList<Int>()
							for (succes in dataSnapshot.children)
							{
								positions.add(Integer.parseInt(succes.key))
							}
							
							val adapter = SuccesDemandesListViewAdapter(view.context, bundle.getString("id"), positions)
							view.adapter = adapter
							setListViewHeightBasedOnChildren(view)
						}
						
						/**
						 * Arrêt de la récupération des données
						 * @param firebaseError Erreur éventuelle
						 */
						override fun onCancelled(firebaseError: DatabaseError)
						{
							debug("searchAndDisplay: $firebaseError")
						}
					})
				}
			}
			
			override fun onCancelled(firebaseError: DatabaseError)
			{
				debug("setUserNiveauSucces: $firebaseError")
			}
		})
	}
	
	/** Method for Setting the Height of the ListView dynamically.
	 * Hack to fix the issue of not showing all the items of the ListView
	 * when placed inside a ScrollView
	 */
	private fun setListViewHeightBasedOnChildren(listView: ListView)
	{
		val listAdapter = listView.adapter
		if (listAdapter != null)
		{
			val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)
			var totalHeight = 0
			var view: View? = null
			for (i in 0 until listAdapter.count)
			{
				view = listAdapter.getView(i, view, listView)
				if (i == 0)
				{
					view!!.layoutParams = ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
				}
				
				view!!.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
				totalHeight += view.measuredHeight
			}
			val params = listView.layoutParams
			params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
			listView.layoutParams = params
		}
	}
	
	/*
	 * Fonctions utilisées pour la recherche d'utilisateurs
	 */
	//region ////////// ALGORITHME DE RECHERCHE //////////
	
	/**
	 * Calcule le nombre de paires de caractères en commun entre 2 chaînes
	 * L'ordre des paramètres n'a pas d'importance
	 * @param str1 Première chaîne pour la comparaison
	 * @param str2 Deuxième chaîne pour la comparaison
	 * @return Similarité lexicale entre 0 et 1
	 */
	fun compareStrings(str1: String, str2: String): Double
	{
		// pairs de 2 caractères qui composent la 1ère chaîne //
		val pairs1 = wordLetterPairs(str1.toUpperCase())
		
		// pairs de 2 caractères qui composent la 2ème chaîne //
		val pairs2 = wordLetterPairs(str2.toUpperCase())
		
		var similarite = 0
		val totalSize = pairs1.size + pairs2.size
		
		for (pair1 in pairs1)
		{
			for (pair2 in pairs2)
			{
				if (pair1 == pair2)
				{
					similarite++
					pairs2.remove(pair2)
					break
				}
			}
		}
		
		return 2.0 * similarite / totalSize
	}
	
	/**
	 * Découpe une chaîne de mots en liste de chaînes de 2 caractères
	 * @param str Chaîne à découper
	 * @return Liste de chaînes de 2 caractères
	 */
	private fun wordLetterPairs(str: String): ArrayList<String>
	{
		val allPairs = ArrayList<String>()
		
		// sépare les mots de la chaîne et les place dans un tableau //
		val words = str.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		
		// parcours des mots de la chaîne //
		for (word in words)
		{
			// découpe le mot en chaînes de 2 caractères //
			val pairsInWord = letterPairs(word)
			
			allPairs.addAll(Arrays.asList(*pairsInWord))
		}
		
		return allPairs
	}
	
	/**
	 * Découpe un mot en tableau de chaînes de 2 caractères
	 * @param str Mot à découper
	 * @return Tableau de chaînes de 2 caractères
	 */
	private fun letterPairs(str: String): Array<String>
	{
		val numPairs = str.length - 1
		val pairs = Array(numPairs) { "" }
		for (i in 0 until numPairs)
		{
			pairs[i] = str.substring(i, i + 2)
		}
		
		return pairs
	}
	
	//endregion
	
	fun generateWeeklyCocktail(activity: AppCompatActivity)
	{
		val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
		val currentWeek = Calendar.WEEK_OF_YEAR
		val lastWeek = sharedPref.getInt("weekNumber", -1)
		
		if (lastWeek != currentWeek)
		{
			weeklyCocktailID = ThreadLocalRandom.current().nextLong(1, (dbCocktails.allCocktails.size + 1).toLong())
			val editor = sharedPref.edit()
			editor.putInt("weekNumber", currentWeek)
			editor.putLong("weeklyCocktailID", weeklyCocktailID)
			editor.apply()
		}
		else
		{
			weeklyCocktailID = sharedPref.getLong("weeklyCocktailID", -1)
		}
	}
	
	fun debug(message: String)
	{
		Log.d(TAG, message)
	}
}
