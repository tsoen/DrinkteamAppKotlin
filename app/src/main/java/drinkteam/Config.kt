package drinkteam

/**
 * Created by Timothée on 05/07/2017.
 *
 * Définit des constantes utilisées à plusieurs endroits dans l'application
 * Cela permet d'éviter les fautes de frappes, de faciliter leur modification, et centralise leur déclaration
 */
object Config
{
	const val TAG = "myapp"
	const val SQLITE_DB_NAME = "DrinkTeamDB"
	const val SQLITE_DB_VERSION = 1
	
	// mode de requête google pour la connexion à un compte existant //
	const val GOOGLE_SIGN_IN = 1000
	
	// mode de requête google pour la création d'un compte //
	const val GOOGLE_CREATE = 1001
	
	// id web client google //
	const val SERVER_CLIENT_ID = "124880078835-d7vjd8tiagkg9kuhmvmdf2nhan4uhpli.apps.googleusercontent.com"
	
	// chemin de stockage des ressources image //
	const val path = "android.resource://com.drinkteam/drawable/"
	const val PICK_IMAGE_PROFILE = 2000
	const val PICK_IMAGE_COVER = 2001
	const val CAPTURE_IMAGE_PROFILE = 2002
	const val CAPTURE_IMAGE_COVER = 2003
	
	//region ////////// DATABASE NODE NAMES //////////
	
	const val DB_NODE_COCKTAILS = "Cocktails"
	const val DB_NODE_COMPTES = "Comptes"
	const val DB_USER_ID = "id"
	const val DB_USER_NAME = "name"
	const val DB_USER_PROVIDER = "provider"
	const val DB_USER_PROVIDERID = "providerid"
	const val DB_USER_TITRE = "titre"
	const val DB_NODE_SCORES = "ComptesSucces"
	const val DB_NODE_JEUX = "Jeux"
	const val DB_NODE_SUCCES = "Succes"
	const val DB_SUCCES_SCORE = "Score"
	const val DB_SUCCES_DATE = "Date"
	const val DB_NODE_DEMANDES = "SuccesDemandes"
	const val DB_DEMANDE_SCORE = "Score"
	const val DB_DEMANDE_DATE = "Date"
	
	//endregion
	
	//region ////////// STORAGE NODE NAMES //////////
	
	const val STR_NODE_COMPTES = "Comptes"
	const val STR_USER_PROFILE = "profile"
	const val STR_USER_COVER = "cover"
	
	// nom du fichier dans le storage firebase pour la photo de couverture par défaut //
	const val STR_DEFAULT_COVER = "defaultCover.png"
	
	// nom du fichier dans le storage firebase pour la photo de profile par défaut //
	const val STR_DEFAULT_PROFILE = "defaultProfile.png"
	
	//endregion
}
