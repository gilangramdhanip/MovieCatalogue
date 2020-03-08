package com.example.favoriteapps.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.favoriteapps.R
import com.example.favoriteapps.fragment.FavMovie
import com.example.favoriteapps.fragment.FavTvShow

class SectionsPagerFavAdapter(private val mContext: Context?, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FavMovie()
            1 -> fragment = FavTvShow()
        }
        return fragment as Fragment
    }

    @Nullable

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext!!.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

}