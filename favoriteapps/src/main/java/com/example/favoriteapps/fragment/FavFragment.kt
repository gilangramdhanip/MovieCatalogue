package com.example.favoriteapps.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.favoriteapps.R
import com.example.favoriteapps.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.fragment_fav.*

/**
 * A simple [Fragment] subclass.
 */
class FavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sectionPager = SectionsPagerAdapter(context, childFragmentManager)
        viewPager.adapter = sectionPager
        tabLayout.setupWithViewPager(viewPager)
    }


}
