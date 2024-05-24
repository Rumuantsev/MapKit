package com.example.mapkit

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer

private lateinit var mapView: MapView
lateinit var KemerovoButton: FloatingActionButton
lateinit var MyLocButton: FloatingActionButton
lateinit var userLocationLayer: UserLocationLayer
lateinit var trafficButton: ToggleButton
lateinit var mapObjectCollection: MapObjectCollection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("7d99dc25-6192-4cf0-a18e-fc891a177002")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        KemerovoButton = findViewById(R.id.Kemerovo_btn)
        MyLocButton = findViewById(R.id.MyLoc_btn)
        trafficButton = findViewById(R.id.toggleButton)

        userLocationLayer = MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true

        mapObjectCollection = mapView.map.mapObjects.addCollection()
        mapView.map.addInputListener(inputListener)

        KemerovoButton.setOnClickListener() {
            toKemerovo()
        }

        MyLocButton.setOnClickListener() {
            toMyLocationPhone()
        }

        var mapKit = MapKitFactory.getInstance()
        var trafficLayer = mapKit.createTrafficLayer(mapView.mapWindow)

        trafficButton.setOnCheckedChangeListener { buttonView, isChecked ->
            trafficLayer.isTrafficVisible = isChecked
        }

        locationPermission()

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    fun locationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                0
            )
        }
    }

    fun toKemerovo() {
        val kemerovoCoordinates = Point(55.354722, 86.0861)
        mapView.map.move(
            CameraPosition(
                kemerovoCoordinates,
                16.0f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 3f),
            null
        )
    }

    fun toMyLocationPhone() {
        mapView.map.move(
            CameraPosition(
                userLocationLayer.cameraPosition()!!.target,
                16f,
                0f,
                0f),
            Animation(Animation.Type.SMOOTH, 2f),
            null
        )
    }

    fun toMyLocationEmulation() {
        val currentLocation = Point(55.351926, 86.093097)

        mapView.map.move(
            CameraPosition(
                currentLocation,
                15.0f,
                0.0f,
                0.0f
            ),
            Animation(Animation.Type.SMOOTH, 2f),
            null
        )
    }

    fun addMark(point: Point) {
        mapObjectCollection.addPlacemark(point)
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            addMark(point)
        }

        override fun onMapLongTap(map: Map, point: Point) {
            addMark(point)
        }
    }
}