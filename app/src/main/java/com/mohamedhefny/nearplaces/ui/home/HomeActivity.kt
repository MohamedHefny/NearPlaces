package com.mohamedhefny.nearplaces.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mohamedhefny.nearplaces.R
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homePlacesAdapter: HomePlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homePlacesAdapter = HomePlacesAdapter(ArrayList())

        home_recycler.adapter = homePlacesAdapter

        observePlaces()
    }

    /**
     * This method is to observe places data and bind it to recyclerView
     */
    private fun observePlaces() {
        homeViewModel.getPlaces().observe(this, Observer {

            home_progressbar.visibility = View.GONE
            home_progress_tip.visibility = View.GONE
            home_state_info_img.visibility = View.GONE

            if (it != null)
                if (it.isNotEmpty())
                    homePlacesAdapter.setPlacesList(it)
                else
                    setPlacesDataError(R.string.data_not_found, R.drawable.ic_no_data_found)
            else //Places data is null
                setPlacesDataError(R.string.something_wrong, R.drawable.ic_cloud_off)
        })
    }

    /**
     * Handel error, and update a view to the user
     *
     * @param errorMsg is a string resource id of error message
     * @param errorImg is a drawable resource id of error image
     */
    private fun setPlacesDataError(errorMsg: Int, errorImg: Int) {
        home_progress_tip.setText(errorMsg)
        home_state_info_img.setImageResource(errorImg)
        home_progress_tip.visibility = View.VISIBLE
        home_state_info_img.visibility = View.VISIBLE
    }

}