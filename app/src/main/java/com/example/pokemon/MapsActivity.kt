package com.example.pokemon

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.pokemon.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        loadpokymon()
        checkpermission()
    }
    val accesslocation=132
    fun checkpermission(){
        if (Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                    PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),accesslocation)
                return
            }
        }
        getuserlocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        when(requestCode){
            accesslocation->{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"PERMISSION_GRANTED",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun getuserlocation(){
        Toast.makeText(this,"location accessed",Toast.LENGTH_LONG).show()

        val mylocation =MylocationListener()
        val locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,mylocation)

        val myThread =MyThread()

        myThread.start()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("me").snippet("location").
        icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))*/
    }

    var myLocation:Location?=null
    var i=0
    var oldlocation:Location?=null
    inner class MylocationListener:LocationListener{
        constructor(){
            myLocation= Location("ME")
            myLocation!!.latitude=0.0
            myLocation!!.longitude=0.0
            oldlocation = Location("oldlocation")
            oldlocation!!.longitude=0.0
            oldlocation!!.latitude=0.0
        }
        override fun onLocationChanged(p0: Location) {
            myLocation=p0
            //Toast.makeText(baseContext,"location accessed:::"+p0!!.latitude+"\n"+p0!!.longitude,Toast.LENGTH_LONG).show()
            Toast.makeText(baseContext,"location accessed:::"+myLocation!!.latitude+"\n"+myLocation!!.longitude,Toast.LENGTH_LONG).show()
            i++;
            mMap.clear()
            val sydney = LatLng(myLocation!!.latitude, myLocation!!.longitude)
            mMap.addMarker(MarkerOptions().position(sydney).title("me").snippet("location").
            icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

            if(oldlocation!!.distanceTo(myLocation)==0f){

            }else {
                oldlocation = myLocation
                for (ss in 0..listofpokymon.size - 1) {
                    if (listofpokymon[ss].isCatch == false) {
                        val pokyloc = LatLng(
                            listofpokymon[ss].location!!.latitude,
                            listofpokymon[ss].location!!.longitude
                        )
                        mMap.addMarker(
                            MarkerOptions().position(pokyloc).title(listofpokymon[ss].name)
                                .snippet(listofpokymon[ss].des+" power "+listofpokymon[ss].power)
                                .icon(BitmapDescriptorFactory.fromResource(listofpokymon[ss].image!!))
                        )

                        if (myLocation!!.distanceTo(listofpokymon[ss].location)<20){
                            listofpokymon[ss].isCatch=true
                            power = power+listofpokymon[ss].power!!
                            Toast.makeText(applicationContext,"new power:::"+power,Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
    }

    inner class MyThread: Thread {
        constructor() : super() {

        }

        override fun run() {
            while (true) {
                try {

                    /*    mMap.clear()
                    runOnUiThread {
                    val sydney = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                    mMap.addMarker(MarkerOptions().position(sydney).title("also me").snippet("location").
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

                    Toast.makeText(baseContext,"location accessed:::"+myLocation!!.latitude+"\n"+myLocation!!.longitude,Toast.LENGTH_LONG).show()
                }


                    Thread.sleep(1000)*/
                    Thread.sleep(3000)
                }catch (e:Exception){}

                }

        }
    }
    var power:Double=0.0
    var listofpokymon=ArrayList<Pokymon>()

    fun loadpokymon(){
        listofpokymon.add(Pokymon("p1","bulbasaur",R.drawable.bulbasaur,55.0,30.703995/*-.0030002*/,30.763005/*+.004000001*/))
        listofpokymon.add(Pokymon("p2","charmander",R.drawable.charmander,70.0,30.70385+.001061,30.76303833-.0050061))
        listofpokymon.add(Pokymon("p3","squirtle",R.drawable.squirtle,20.0,30.70385-.0050102,30.76303833+.0060000602))
    }
}