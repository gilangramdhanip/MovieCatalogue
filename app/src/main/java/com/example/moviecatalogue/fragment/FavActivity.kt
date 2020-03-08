package com.example.moviecatalogue.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.SectionsPagerAdapter
import com.example.moviecatalogue.adapter.SectionsPagerFavAdapter
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.android.synthetic.main.activity_main.*

class FavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerFavAdapter =
            SectionsPagerFavAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerFavAdapter
        tabLayout.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f
    }
}
