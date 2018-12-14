package com.example.cesarguerrero.movies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MovieFrame : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_frame)
        var infoPass= intent.extras
        var myNewFragment = BlankFragment()
        var loadFragment= getSupportFragmentManager().beginTransaction() //this is a fragment transaction
        loadFragment.replace(R.id.fragment_location, myNewFragment)//this will load the fragment on the screen
        myNewFragment.arguments=infoPass
        loadFragment.commit()
    }
}
