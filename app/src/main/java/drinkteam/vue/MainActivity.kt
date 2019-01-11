package drinkteam.vue

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.drinkteam.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import drinkteam.Config
import drinkteam.Utils
import drinkteam.Utils.debug
import drinkteam.controleur.CocktailDAO
import drinkteam.controleur.JeuDAO
import drinkteam.controleur.SuccesDAO
import drinkteam.layouts.ActionbarLayout
import drinkteam.layouts.AmisSearchDialogLayout
import drinkteam.layouts.MainActivityLayout
import drinkteam.metier.Cocktail
import drinkteam.metier.Jeu
import drinkteam.metier.Succes
import drinkteam.replaceFragment
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.setContentView
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.Map.Entry

/**
 * Conteneur principal de l'application
 * Cette activité gère :
 * - l'ActionBar qui contient le titre de l'application et les boutons associés
 * - le menu de navigation entre les différentes pages. Ces pages sont représentées sous forme de
 * Fragments qui occupent tous la même zone de l'activité
 * - la récupération et l'initialisation des bases de données de Jeux, Cocktails et Succès
 */
class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback
{
	//region //////////////////// ATTRIBUTS ////////////////////
	
	// menu de navigation //
	private lateinit var drawerLayout: DrawerLayout
	
	// bouton sur l'ActionBar qui permet d'ouvrir le menu //
	private lateinit var drawerButton: ActionBarDrawerToggle
	
	// liste qui va contenir les différentes lignes du menu //
	private lateinit var drawerItems: ListView
	
	// zone de texte du titre de l'actionBar (nécessaire pour centrer le texte) //
	private lateinit var titreActionBar: TextView
	
	lateinit var layout: MainActivityLayout
	
	lateinit var actionbarLayout: ActionbarLayout
	
	//endregion
	
	/**
	 * Ouverture de l'activitié / application
	 * @param savedInstanceState Etat sauvegardé de l'application si elle a été mise de côté
	 */
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		// on défini la Vue qui constitue l'activité //
		layout = MainActivityLayout()
		layout.setContentView(this)
		
		//region //////////////////// BASES DE DONNEES ////////////////////
		
		// base de données des Cocktails //
		Utils.dbCocktails = CocktailDAO(this)
		
		// base de données des Jeux //
		Utils.dbJeux = JeuDAO(this)
		
		// base de données des Succès //
		Utils.dbSucces = SuccesDAO(this)
		
		// récupération des Cocktails de Firebase //
		Utils.firebaseDB.child(Config.DB_NODE_COCKTAILS).addListenerForSingleValueEvent(object : ValueEventListener
		{
			/**
			 * Détecte un changement de donnée dans la base à l'emplacement défini
			 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
			 * @param dataSnapshot Contient les données présentes à cet emplacement
			 */
			override fun onDataChange(dataSnapshot: DataSnapshot)
			{
				
				for (cocktailSnapshot in dataSnapshot.children)
				{
					Utils.dbCocktails.addCocktail(cocktailSnapshot.getValue(Cocktail::class.java) as Cocktail)
				}
				
				Utils.generateWeeklyCocktail(this@MainActivity)
			}
			
			/**
			 * Arrêt de la récupération des données
			 * @param firebaseError Erreur éventuelle
			 */
			override fun onCancelled(firebaseError: DatabaseError)
			{
				debug("Init. Cocktails: $firebaseError")
			}
		})
		
		// récupération des Jeux de Firebase //
		Utils.firebaseDB.child(Config.DB_NODE_JEUX).addListenerForSingleValueEvent(object : ValueEventListener
		{
			/**
			 * Détecte un changement de donnée dans la base à l'emplacement défini
			 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
			 * @param dataSnapshot Contient les données présentes à cet emplacement
			 */
			override fun onDataChange(dataSnapshot: DataSnapshot)
			{
				for (jeuSnapshot in dataSnapshot.children)
				{
					Utils.dbJeux.addJeu(jeuSnapshot.getValue(Jeu::class.java) as Jeu)
				}
			}
			
			/**
			 * Arrêt de la récupération des données
			 * @param firebaseError Erreur éventuelle
			 */
			override fun onCancelled(firebaseError: DatabaseError)
			{
				debug("Init. Cocktails: $firebaseError")
			}
		})
		
		// récupération des Succès de Firebase puis chargement de la page d'accueil //
		Utils.firebaseDB.child(Config.DB_NODE_SUCCES).addListenerForSingleValueEvent(object : ValueEventListener
		{
			/**
			 * Détecte un changement de donnée dans la base à l'emplacement défini
			 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
			 * @param dataSnapshot Contient les données présentes à cet emplacement
			 */
			override fun onDataChange(dataSnapshot: DataSnapshot)
			{
				for (succesSnapshot in dataSnapshot.children)
				{
					Utils.dbSucces.addSucces(succesSnapshot.getValue(Succes::class.java) as Succes)
				}
				
				// on attend que tous les succès aient été initialisés pour charger la page d'accueil //
				chargementAccueil(savedInstanceState)
			}
			
			/**
			 * Arrêt de la récupération des données
			 * @param firebaseError Erreur éventuelle
			 */
			override fun onCancelled(firebaseError: DatabaseError)
			{
				debug("Init. Cocktails: $firebaseError")
			}
		})
		
		//endregion
		
		//region //////////////////// ACTIONBAR ////////////////////
		
		// sélectionne la Vue de l'actionBar, qui contient la zone d'affichage du titre //
		actionbarLayout = ActionbarLayout()
		val viewActionBar = actionbarLayout.createView(AnkoContext.create(this, this))
		
		// défini le texte de la zone d'affichage du titre //
		this.titreActionBar = actionbarLayout.titreActionBar
		
		// permet de centrer la zone de texte qui contient le titre de l'actionBar //
		val params = ActionBar.LayoutParams(
			ActionBar.LayoutParams.WRAP_CONTENT,
			ActionBar.LayoutParams.WRAP_CONTENT,
			Gravity.CENTER
		)
		
		val actionBar = supportActionBar
		
		// créer une vue personnalisée avec les paramètres définis précedemment //
		actionBar!!.setCustomView(viewActionBar, params)
		
		// applique la vue personnalisée à notre actionBar
		actionBar.setDisplayShowCustomEnabled(true)
		
		// empêche l'affichage d'un titre automatique //
		actionBar.setDisplayShowTitleEnabled(false)
		
		// TODO: check if necessary
		// clique sur le titre de l'actionBar :
		// false -> remonter en haut de la page; true -> remonter d'un niveau
		actionBar.setDisplayHomeAsUpEnabled(true)
		// boutton "home" de l'actionBar ?
		actionBar.setHomeButtonEnabled(true)
		
		//endregion
		
		//region //////////////////// MENU DE NAVIGATION ////////////////////
		
		// définition de la vue du Menu principal //
		this.drawerLayout = layout.drawerLayout
		
		// récupèration de la liste des titres du menu //
		val drawerItemsList = resources.getStringArray(R.array.items)
		
		// définition de la Vue qui contient la liste des pages et on lui ajoute les titres //
		this.drawerItems = layout.myDrawer
		this.drawerItems.adapter = ArrayAdapter(this, R.layout.general_menu_item, drawerItemsList)
		
		// listener de click sur les onglets //
		this.drawerItems.onItemClickListener = MyDrawerItemClickListener()
		
		// création du bouton sur l'actionBar qui va ouvrir le menu drawerLayout //
		this.drawerButton = ActionBarDrawerToggle(this, this.drawerLayout, R.string.ouverture, R.string.fermeture)
		
		// on précise au Menu qu'il doit s'ouvrir quand on appuie sur le bouton en haut à gauche //
		this.drawerLayout.addDrawerListener(this.drawerButton)
		this.drawerButton.syncState()
		
		//endregion ////////////////////////////////////////
	}
	
	/**
	 * Chargement du fragment de la page d'accueil à l'ouverture de l'application
	 * @param savedInstanceState Etat sauvegardé de l'application si elle a été mise de côté
	 */
	private fun chargementAccueil(savedInstanceState: Bundle?)
	{
		// si un état est déjà en sauvegarde, ne pas recréer le fragment pour éviter une superposition
		if (savedInstanceState == null)
		{
			val bundle = Bundle()
			bundle.putString("titre_actionbar", resources.getString(R.string.app_name))
			
			// ajout de fragment dans la zone qui doit le contenir //
			replaceFragment(this, AccueilPage(), bundle, false)
		}
	}
	
	//region //////////////////// MENU ////////////////////
	
	/**
	 * Listener de cliques sur le menu
	 */
	private inner class MyDrawerItemClickListener : AdapterView.OnItemClickListener
	{
		/**
		 * Comportement à la détection d'un clique
		 * @param parent AdapterView où le clique a eu lieu
		 * @param view Vue de l'AdapterView
		 * @param pos Index de l'onglet sélectionné
		 * @param id ID de l'index
		 */
		override fun onItemClick(parent: AdapterView<*>, view: View, pos: Int, id: Long)
		{
			// créer un nouveau fragment qui correspond à l'onglet sélectionné //
			lateinit var fragment: Fragment
			
			val bundle = Bundle()
			
			when (pos)
			{
				0 ->
				{
					fragment = AccueilPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.app_name))
				}
				
				1 ->
				{
					fragment = SuccesPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Mes_succes))
					bundle.putString("id", Utils.mAuth.currentUser!!.uid)
				}
				
				2 ->
				{
					fragment = AmisPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Mes_amis))
				}
				
				3 ->
				{
					fragment = CocktailsPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Les_recettes))
				}
				
				4 ->
				{
					fragment = JeuxPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Idées_jeux))
				}
				
				5 ->
				{
					fragment = TrouverBarPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Trouver_bar))
				}
				
				6 ->
				{
					fragment = NewsPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.News))
				}
				
				7 ->
				{
					fragment = EvenementsPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Evenements))
				}
				
				8 ->
				{
					fragment = PreferencesPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.Paramètres))
				}
				
				9 ->
				{
					fragment = AboutPage()
					bundle.putString("titre_actionbar", resources.getString(R.string.AboutUs))
				}
				
				else ->
				{
				}
			}
			
			// si un fragment a bien été créé //
			replaceFragment(drawerLayout.context, fragment, bundle, true)
			
			// ferme le Menu, ce qui va redessiner l'actionBar et mettre à jour le titre //
			drawerLayout.closeDrawer(drawerItems)
		}
	}
	
	/**
	 * Appelé uniquement à la première ouverture de l'application ( onCreate )
	 * @param menu Actionbar
	 * @return Validation
	 */
	override fun onCreateOptionsMenu(menu: Menu): Boolean
	{
		//créer le menu avec les boutons en haut à droite
		menuInflater.inflate(R.menu.menu, menu)
		
		return true
	}
	
	//endregion ////////////////////////////////////////
	
	/**
	 * La page des préférences (Paramètres) est composée de plusieurs écrans au sein d'un même fragment. Cette fonction est appelée à chaque ouverture d'un des écrans
	 * (sauf l'écran de démarrage du fragment) ex: écran "Notifications" ou "Modifier le profil"
	 * On redessine le fragment on lui indiquant dans quel écran on va se trouver
	 * @param fragmentCompat Fragment de la page de préférences
	 * @param preferenceScreen L'écran de préférences ouvert
	 * @return Validation de l'ouverture
	 */
	override fun onPreferenceStartScreen(fragmentCompat: PreferenceFragmentCompat, preferenceScreen: PreferenceScreen): Boolean
	{
		// indique au fragment dans quel écran de préférence on va se trouver //
		val bundle = Bundle()
		bundle.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.key)
		
		// remplacement du fragment actuel par le nouveau fragment //
		replaceFragment(this, PreferencesPage(), bundle, true)
		
		return true
	}
	
	/**
	 * Détecte les clicks sur les boutons de l'actionBar :
	 * - le drawerButton du menu de navigation
	 * - le bouton de recherche
	 * - le bouton "écrire"
	 * @param item Bouton sélectionné
	 * @return Validation du clique
	 */
	override fun onOptionsItemSelected(item: MenuItem): Boolean
	{
		// si l'item sélectionné est le bouton d'ouverture du menu, ouvre le menu //
		if (this.drawerButton.onOptionsItemSelected(item))
		{
			return true
		}
		else
		{
			when (item.itemId)
			{
			// si bouton de recherche sélectionné //
				R.id.action_search ->
				{
					// initialisation du dialog de recherche d'utilisateurs //
					val dialog = Dialog(this, R.style.full_screen_dialog)
					val layout = AmisSearchDialogLayout()
					dialog.setContentView(layout.createView(AnkoContext.create(this, this)))
					
					// background semi-transparent //
					val background = ColorDrawable(Color.BLACK)
					background.alpha = 130
					dialog.window!!.setBackgroundDrawable(background)
					
					dialog.show()
					
					// bouton de fermeture du dialog //
					val cancelButton = layout.actionBarCancelSearch
					
					// listener de click //
					cancelButton.onClick {
						// fermeture du dialog //
						dialog.cancel()
					}
					
					// champs de recherche //
					val textSearch = layout.actionbarEditText
					
					// gestionnaire d'événements quand l'utilisateur entre du texte //
					textSearch.addTextChangedListener(object : TextWatcher
					{
						// timer pour ne pas déclencher les événements à chaque caractère //
						private var timer = Timer()
						
						// durée du timer en millisecondes //
						private val DELAY: Long = 500
						
						override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
						{
						}
						
						override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
						{
						}
						
						/**
						 * Evenement déclenché après avoir entré du texte
						 * @param editable Contenu du champ
						 */
						override fun afterTextChanged(editable: Editable)
						{
							// réinitialisation du timer à chaque fois que cette méthode est appelée (à chaque caractère entré dans le champ) //
							timer.cancel()
							timer = Timer()
							// si le timer atteint le DELAY (il n'a pas été réinitialisé à 0 avant), déclenche la recherche //
							timer.schedule(object : TimerTask()
							{
								/**
								 * Opération réalisée dans un autre thread
								 */
								override fun run()
								{
									// recherche et affiche les utilisateurs //
									searchAndDisplay(layout, dialog, editable.toString())
								}
							}, DELAY)
						}
					})
					
					textSearch.setOnTouchListener { _, motionEvent ->
						if (motionEvent.action == MotionEvent.ACTION_UP)
						{
							if (motionEvent.rawX >= textSearch.right - textSearch.compoundDrawables[2].bounds.width())
							{
								textSearch.text.clear()
							}
						}
						false
					}
					
					return true
				}
				
				R.id.action_talk ->
				{
					return true
				}
				
				else ->
				{
					return super.onOptionsItemSelected(item)
				}
			}
		}
	}
	
	/**
	 * Détection du click sur le bouton "retour" du téléphone
	 */
	override fun onBackPressed()
	{
		// TODO dialog de confirmation pour fermer l'application
		// détection de la page actuellement affichée //
		val fragment = supportFragmentManager.findFragmentById(layout.fragmentContainer.id)
		
		// si on se trouve sur la page d'acueil, on peut demander confirmation avant de fermer l'application //
		if (fragment is AccueilPage)
		{
			finishAndRemoveTask()
		}
		// sinon on garde le comportement basique de revenir à la dernière page affichée //
		else
		{
			super.onBackPressed()
		}
	}
	
	public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	{
		super.onActivityResult(requestCode, resultCode, data)
		
		if (resultCode == RESULT_OK && data != null && data.data != null)
		{
			val user = Utils.mAuth.currentUser
			val url = data.data
			
			when (requestCode)
			{
				Config.PICK_IMAGE_PROFILE ->
				{
					Thread(Runnable
					{
						try
						{
							Utils.storage.child(Config.STR_NODE_COMPTES).child(user!!.uid).child(Config.STR_USER_PROFILE).putFile(url!!)
						}
						catch (e: Exception)
						{
							debug("onActivityResult setCoverImage: $e")
						}
					}).start()
				}
				
				Config.PICK_IMAGE_COVER ->
				{
					Thread(Runnable
					{
						try
						{
							Utils.storage.child(Config.STR_NODE_COMPTES).child(user!!.uid).child(Config.STR_USER_COVER).putFile(url!!)
						}
						catch (e: Exception)
						{
							debug("onActivityResult setCoverImage: $e")
						}
					}).start()
				}
				
				else ->
				{
					try
					{
						val extras = data.extras
						val imageBitmap = extras!!.get("data") as Bitmap
						
						val imageFile = File(applicationContext.filesDir, "tempPicture.jpg")
						
						val fileOutputStream = FileOutputStream(imageFile)
						
						imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
						fileOutputStream.flush()
						fileOutputStream.close()
						
						if (requestCode == Config.CAPTURE_IMAGE_PROFILE)
						{
							
							Utils.storage.child(Config.STR_NODE_COMPTES).child(user!!.uid).child(Config.STR_USER_PROFILE).putFile(Uri.parse(imageFile.toURI().toString()))
						}
						else if (requestCode == Config.CAPTURE_IMAGE_COVER)
						{
							
							Utils.storage.child(Config.STR_NODE_COMPTES).child(user!!.uid).child(Config.STR_USER_COVER).putFile(Uri.parse(imageFile.toURI().toString()))
						}
					}
					catch (e: Exception)
					{
						debug("Error writing bitmap")
					}
				}
			}
		}
	}
	
	/**
	 * Recherche les utilisateurs correspondants au texte entré et les affiche dans le dialog
	 * @param dialog Dialog de recherche  des utilisateurs
	 * @param query La chaîne entrée par l'utilisateur
	 */
	private fun searchAndDisplay(layout: AmisSearchDialogLayout, dialog: Dialog, query: String?)
	{
		// récupère tous les comptes de la base de données //
		Utils.firebaseDB.child(Config.DB_NODE_COMPTES).addListenerForSingleValueEvent(object : ValueEventListener
		{
			/**
			 * Détecte un changement de donnée dans la base à l'emplacement défini
			 * Appelée 1 seule fois immédiatement pour un ListenerForSingleValueEvent
			 * @param dataSnapshot Contient les données présentes à cet emplacement
			 */
			override fun onDataChange(dataSnapshot: DataSnapshot)
			{
				// ListView qui va contenir les résultats de la recherche //
				val listView = layout.searchFriendsList
				
				// si du texte a bien été entré pour effectuer la recherche //
				if (query != null && query != "")
				{
					// liste qui va contenir les résultats et leur "score" (pertinence) de recherche //
					val userList = HashMap<DataSnapshot, Double>()
					
					// parcours de tous les comptes //
					for (compte in dataSnapshot.children)
					{
						// comparaison de leur nom au texte de recherche //
						// le "score" de recherche est compris entre 0 et 1 //
						val name = compte.child(Config.DB_USER_NAME).value!!.toString()
						val similarity = Utils.compareStrings(name, query)
						
						// critère de sélection pour l'affichage //
						if (similarity > 0.5)
						{
							// ajout du résultat dans la liste qui sera à afficher //
							userList[compte] = similarity
						}
					}
					
					// manipulation pour transformer la liste en tableau, ce qui va permettre de trier les résultats par score de recherche //
					val userArray = userList.entries.toTypedArray<Entry<DataSnapshot, Double>>()
					
					// tri du tableau des résultats en fonction de leur score de recherche //
					Arrays.sort(userArray) { o1, o2 ->
						// précise à l'algorithme de tri que le critère de tri est le "score" de recherche //
						(o2.value.compareTo(o1.value))
					}
					
					// donne le tableau trié des résultats au gestionnaire de la ListView //
					listView.adapter = AmisSearchListViewAdapter(this@MainActivity, userArray)
					
					// détecte les clicks sur les résultats et affiche le profil de l'utilisateur sélectionné //
					listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, position, _ ->
						
						// récupère l'utilisateur sélectionné dans la liste (AmisSearchListViewAdapter.getItemId(position)) //
						val userEntry = adapterView.getItemAtPosition(position) as Entry<DataSnapshot, Double>
						
						// récupère les données de l'utilisateur //
						val userData = userEntry.key
						
						// défini les paramètres pour l'affichage du profil de l'utilisateur //
						val bundle = Bundle()
						bundle.putString("titre_actionbar", userData.child(Config.DB_USER_NAME).value.toString())
						bundle.putString("id", userData.child(Config.DB_USER_ID).value.toString())
						bundle.putString("name", userData.child(Config.DB_USER_NAME).value.toString())
						bundle.putString("provider", userData.child(Config.DB_USER_PROVIDER).value.toString())
						bundle.putString("providerid", userData.child(Config.DB_USER_PROVIDERID).value.toString())
						bundle.putString("titre", userData.child(Config.DB_USER_TITRE).value.toString())
						
						// affiche le profil de l'utilisateur //
						replaceFragment(this@MainActivity, AmisProfil(), bundle, true)
						
						// ferme le dialog de recherche //
						dialog.cancel()
					}
				}
				// si aucun texte n'a été entré pour la recherche, supprime les résultats d'une recherche précédente //
				else
				{
					listView.adapter = null
				}
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
	
	fun setTitreActionBar(titre: String)
	{
		
		this.titreActionBar.text = titre
		
		titreActionBar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener
		{
			override fun onGlobalLayout()
			{
				// empeche le déclenchement plusieurs fois //
				titreActionBar.viewTreeObserver.removeOnGlobalLayoutListener(this)
				
				if (titreActionBar.lineCount > 1)
				{
					
					val words = titre.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
					
					val builder = StringBuilder()
					var i = 1
					for (word in words)
					{
						if (i == words.size / 2)
						{
							builder.append(word)
							builder.append("\n")
						}
						else
						{
							builder.append(word)
						}
						
						i++
					}
					
					titreActionBar.text = builder.toString()
				}
			}
		})
	}
}

