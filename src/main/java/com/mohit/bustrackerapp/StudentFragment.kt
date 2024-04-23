package com.mohit.bustrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mohit.bustrackerapp.databinding.FragmentStudentBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"

class StudentFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentStudentBinding
    private val buses: MutableStateFlow<Bus?> = MutableStateFlow(null)
    private val args by navArgs<StudentFragmentArgs>()
    lateinit var database: BusTrackerDatabase
    lateinit var dao: BusDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentBinding.inflate(inflater, container, false)
        binding.root.findFragment<SupportMapFragment>().getMapAsync(this)
        database = BusTrackerDatabase.getDb(requireContext())
        dao = database.userDao()
        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch(Main) {
            val bus = dao.getBus(args.busName)!!
            val marker = MarkerOptions().title(bus.name).position(LatLng(bus.latitude.toDouble(), bus.longitude.toDouble()))
            map.addMarker(marker)

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15F))
        }
    }
}