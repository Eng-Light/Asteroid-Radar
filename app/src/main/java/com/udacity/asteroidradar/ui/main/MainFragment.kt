package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment(), MenuProvider {

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(
            this, MainViewModel.Factory(requireNotNull(this.activity).application)
        )[MainViewModel::class.java]

        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        viewModel.weekAsteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.asteroidRecycler.adapter = adapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val adapter = AsteroidAdapter(AsteroidAdapter.AsteroidListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        // Handle the menu selection
        when (menuItem.itemId) {
            R.id.show_weak_asteroids -> {
                viewModel.weekAsteroids.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                binding.asteroidRecycler.adapter = adapter
            }
            R.id.show_saved_asteroids -> {
                viewModel.asteroidsList.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                binding.asteroidRecycler.adapter = adapter
            }
            R.id.show_today_asteroids -> {
                viewModel.todayAsteroids.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                }
                binding.asteroidRecycler.adapter = adapter
            }
        }
        return false
    }
}
