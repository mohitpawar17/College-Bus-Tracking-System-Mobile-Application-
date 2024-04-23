package com.mohit.bustrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohit.bustrackerapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var database: BusTrackerDatabase
    lateinit var dao: BusDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLoginBinding.inflate(inflater)
        database = BusTrackerDatabase.getDb(requireContext())
        dao = database.userDao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.info.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToInfoFragment())
        }

//        lifecycleScope.launch {
//            dao.insertBus(Bus("Bus A", 26.7678041374F, 75.8502224307F))
//            dao.insertBus(Bus("Bus B", 26.7678041374F, 75.8502224307F))
//            dao.insertBus(Bus("Bus C", 26.7678041374F, 75.8502224307F))
//        }

        binding.button.setOnClickListener {
            when {
                binding.name.text.toString().lowercase().startsWith("bus") -> {
                    val bus = dao.getBus(binding.name.text.toString())
                    if (bus != null && binding.editTextTextPassword.text.toString() == "1234") findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDriverFragment(binding.name.text.toString()))
                }

                binding.name.text.toString().lowercase().matches(Regex("student \\w")) -> {
                    if (binding.editTextTextPassword.text.toString() == "1234") findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBusesFragment())
                }
            }
        }
    }
}