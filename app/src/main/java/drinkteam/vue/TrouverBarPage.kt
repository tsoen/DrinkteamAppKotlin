package drinkteam.vue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import drinkteam.Utils.debug
import drinkteam.layouts.TrouverBarPageLayout
import org.jetbrains.anko.AnkoContext

/**
 * Created by Timothée on 18/06/2016.
 *
 */
class TrouverBarPage : Fragment(), OnMapReadyCallback
{
	private lateinit var layout: TrouverBarPageLayout
	private lateinit var mMapView: MapView
	private lateinit var myMap: GoogleMap
	
	/**
	 * Création de la vue à partir du layout dédié
	 * @param inflater Objet utilisé pour remplir la vue
	 * @param container Vue parente qui contient la vue
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 * @return Vue créée
	 */
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
	{
		// associe le layout au fragment //
		this.layout = TrouverBarPageLayout()
		return layout.createView(AnkoContext.create(context as MainActivity, context as MainActivity))
	}
	
	/**
	 * Ajout des éléments après la création de la vue
	 * @param view Vue créée
	 * @param savedInstanceState Ancien état de l'application si mise de côté
	 */
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		// titre de l'actionBar //
		(activity as MainActivity).setTitreActionBar(arguments!!.getString("titre_actionbar"))
		
		mMapView = layout.mapView
		
		// ----- A vérifier ------- ///
		mMapView.onCreate(savedInstanceState)
		mMapView.onResume() // needed to get the map to display immediately
		try
		{
			MapsInitializer.initialize(activity!!.applicationContext)
		}
		catch (e: Exception)
		{
			debug("$e")
		}
	}
	
	override fun onMapReady(googleMap: GoogleMap)
	{
		myMap = googleMap
		
		// For showing a move to my location button
		// TODO Permission GOOGLE (décommenter pour voir l'erreur
		//myMap.setMyLocationEnabled(true);
		val strasbourg = LatLng(48.5, 7.7)
		
		// For dropping a marker at a point on the Map
		// Marche pas lol
		googleMap.addMarker(MarkerOptions().position(strasbourg).title("Marker Title").snippet("Marker Description"))
		
		// For zooming automatically to the location of the marker
		// marche pas lol
		val cameraPosition = CameraPosition.Builder().target(strasbourg).zoom(12f).build()
		myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
		
		myMap.moveCamera(CameraUpdateFactory.newLatLng(strasbourg))
	}
}
