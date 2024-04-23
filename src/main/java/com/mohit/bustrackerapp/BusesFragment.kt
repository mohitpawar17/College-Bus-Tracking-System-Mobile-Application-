package com.mohit.bustrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohit.bustrackerapp.databinding.BusLayoutBinding
import com.mohit.bustrackerapp.databinding.FragmentBusesBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BusesFragment : Fragment() {
    lateinit var binding: FragmentBusesBinding
    lateinit var database: BusTrackerDatabase
    lateinit var dao: BusDao
    private val buses = MutableStateFlow(emptyList<Bus>())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBusesBinding.inflate(inflater);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = BusTrackerDatabase.getDb(requireContext())
        dao = database.userDao()

        lifecycleScope.launch {
            buses.emit(dao.getAllBuses())
        }

        lifecycleScope.launch(Main) {
            buses.collectLatest {
                for (each in it) {
                    val view = BusLayoutBinding.inflate(layoutInflater)
                    view.busName.text = each.name
                    binding.buses.addView(view.root)

                    view.root.setOnClickListener {
                        findNavController().navigate(BusesFragmentDirections.actionBusesFragmentToStudentFragment(each.name))
                    }
                }
            }
        }
    }
}