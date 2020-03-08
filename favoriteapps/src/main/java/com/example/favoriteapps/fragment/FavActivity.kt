package com.example.favoriteapps.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favoriteapps.R
import com.example.favoriteapps.adapter.SectionsPagerFavAdapter
import kotlinx.android.synthetic.main.activity_fav.*
import kotlinx.android.synthetic.main.activity_main.*

class FavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R
            .layout.activity_fav)

        val sectionsPagerFavAdapter =
            SectionsPagerFavAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerFavAdapter
        tabLayout.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f
    }
}
