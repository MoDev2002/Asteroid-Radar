package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.detail.DetailFragment
import com.udacity.asteroidradar.repository.Asteroid

class MainFragment : Fragment() {
    private val binding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setting the view model and initializing it
        val application = requireNotNull(this.activity).application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //connecting the adapter to the view model and recycler view
        val adapter = AsteroidRecyclerAdapter(OnClickListener{viewModel.displayDetails(it)})
        viewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.asteroidRecycler.adapter = adapter

        // click listener navigation event
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (it != null) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailsCompleted()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when(item.itemId) {
            R.id.show_week_asteroids -> viewModel.getWeekAsteroids()
            R.id.show_today_asteroids -> viewModel.getTodayAsteroids()
            R.id.show_all_asteroids -> viewModel.getSavedAsteroids()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}
