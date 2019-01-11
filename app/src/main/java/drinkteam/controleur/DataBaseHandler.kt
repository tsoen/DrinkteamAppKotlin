package drinkteam.controleur

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import drinkteam.Config.SQLITE_DB_NAME
import drinkteam.Config.SQLITE_DB_VERSION
import org.jetbrains.anko.db.*

/**
 * Created by timot on 10/02/2018.
 *
 */
class DataBaseHandler(context: Context) : ManagedSQLiteOpenHelper(context, SQLITE_DB_NAME, null, SQLITE_DB_VERSION)
{
	companion object
	{
		private var instance: DataBaseHandler? = null
		
		@Synchronized
		fun getInstance(context: Context): DataBaseHandler
		{
			if (instance == null)
			{
				instance = DataBaseHandler(context.applicationContext)
			}
			
			return instance!!
		}
	}
	
	/**
	 * Initialise les tables de la base de données
	 * @param db Base de données SQLLite qui va contenir les tables
	 */
	override fun onCreate(db: SQLiteDatabase)
	{
		// création table cocktails //
		db.createTable(CocktailDAO.TABLE_NAME, true,
			CocktailDAO.CODE to INTEGER + PRIMARY_KEY,
			CocktailDAO.NOM to TEXT + NOT_NULL,
			CocktailDAO.RECETTE to TEXT + NOT_NULL,
			CocktailDAO.ALCOOL to TEXT,
			CocktailDAO.ANECDOTE to TEXT,
			CocktailDAO.IMAGE to TEXT
		)
		
		// création table jeux //
		db.createTable(JeuDAO.TABLE_NAME, true,
			JeuDAO.CODE to INTEGER + PRIMARY_KEY,
			JeuDAO.NOM to TEXT + NOT_NULL,
			JeuDAO.JOUEURS to INTEGER + NOT_NULL,
			JeuDAO.REGLES to TEXT + NOT_NULL,
			JeuDAO.ANECDOTE to TEXT,
			JeuDAO.IMAGE to TEXT
		)
		
		// création table succès //
		// PRIMARY KEY composee pas disponible atm //
		db.execSQL("CREATE TABLE " + SuccesDAO.TABLE_NAME + " (" +
				SuccesDAO.CODECATEGORIE + " INTEGER NOT NULL, " +
				SuccesDAO.CODESUCCES + " INTEGER NOT NULL, " +
				SuccesDAO.NOM + " TEXT NOT NULL, " +
				SuccesDAO.DESCRIPTION + " TEXT NOT NULL, " +
				SuccesDAO.PALIER + " INTEGER NOT NULL, " +
				SuccesDAO.FACTEUR + " INTEGER NOT NULL, " +
				SuccesDAO.ANECDOTE + " TEXT, " +
				SuccesDAO.IMAGE + " TEXT, " +
				"PRIMARY KEY (" + SuccesDAO.CODECATEGORIE + "," + SuccesDAO.CODESUCCES + "));"
		)
	}
	
	/**
	 * Comportement lors de l'upgrade de la base
	 * Appelée si le constructeur détecte une nouvelle version de la base
	 * @param db Base de données SQLLite
	 * @param oldVersion Numéro de l'ancienne version
	 * @param newVersion Numéro de la nouvelle version
	 */
	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
	{
		// supprime les tables existantes //
		db.dropTable("tableCocktails", true)
		db.dropTable("tableJeux", true)
		db.dropTable("tableSucces", true)
		
		// recree les tables //
		onCreate(db)
	}
}

// Access property for Context
val Context.dataBaseHandler: DataBaseHandler
	get() = DataBaseHandler.getInstance(applicationContext)