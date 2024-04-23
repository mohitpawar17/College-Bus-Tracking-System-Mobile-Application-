package com.mohit.bustrackerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mohit.bustrackerapp.databinding.FragmentDriverBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val TAG = "DriverFragment"

class DriverFragment : Fragment() {

    lateinit var binding: FragmentDriverBinding
    lateinit var database: BusTrackerDatabase
    lateinit var dao: BusDao
    private val locationFlow: MutableStateFlow<Location> = MutableStateFlow(Location(-1F, -1F))
    private val args by navArgs<DriverFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDriverBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = BusTrackerDatabase.getDb(requireContext())
        dao = database.userDao()

        binding.busName.text = args.busName

        lifecycleScope.launch(IO) {
            locationFlow.collect {
                dao.updateLocation(Bus(args.busName, it.latitude, it.longitude))
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()

        binding.locationTrackingSwitch.setOnClickListener {
            if (binding.locationTrackingSwitch.isEnabled) {
                val locationRequest = LocationRequest.Builder(10000).build()
                LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(
                    locationRequest, object : LocationCallback() {
                        override fun onLocationResult(p0: LocationResult) {
                            Log.e(TAG, "onLocationResult: called 2")
                            p0.locations.forEach {
                                Log.e(TAG, "onLocationResult: $it")
                                lifecycleScope.launch {
                                    locationFlow.emit(Location(it.latitude.toFloat(), it.longitude.toFloat()))
                                }
                            }
                        }
                    }, Looper.getMainLooper()
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.cancel()
    }
}