package com.example.moviecatalogue.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.R
import com.example.moviecatalogue.api.DataRepository
import com.example.moviecatalogue.model.Release
import com.example.moviecatalogue.receiver.Notif
import java.util.*

class ReminderFragment: PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var DAILY: String
    private lateinit var TODAY_RELEASE: String
    private lateinit var dailyNotifPreference: SwitchPreference
    private lateinit var todayReleaseNotifPreference: SwitchPreference
    private lateinit var notifReceiver: Notif


    private val DATE_FORMAT = "yyyy-MM-dd"
    private val stackNotif = ArrayList<Release>()
    private val apiService = DataRepository.create()
    private val API_KEY: String = "8908a1d59ebed18e66c34b99f01d57fc"
    private var todayNotifId = 0


    companion object{
        val CHANNEL_ID = "channel_01"
        val CHANNEL_NAME: CharSequence = "moviedb channel"
        const val NOTIFICATION_REQUEST_CODE = 200
        var MAX_NOTIFICATION= 0
        const val GROUP_KEY_DAILY_RELEASE = "group_key_daily_release"
        const val ID_REMINDER = 100
        const val ID_TODAY_RELEASE = 101
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_layout)

        DAILY = resources.getString(R.string.key_daily)
        TODAY_RELEASE = resources.getString(R.string.key_today_release)

        notifReceiver = Notif()

        dailyNotifPreference = findPreference<Preference>(DAILY) as SwitchPreference
        todayReleaseNotifPreference = findPreference<Preference>(TODAY_RELEASE) as SwitchPreference

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == DAILY){
            if(dailyNotifPreference.isChecked){
                notifReceiver.setDailyReminder(context!!, Notif.TYPE_DAILY_REMINDER)
            }else{
                notifReceiver.cancelReminder(context!!, Notif.TYPE_DAILY_REMINDER)
            }
        }

        if(key == TODAY_RELEASE){
            if(todayReleaseNotifPreference.isChecked){
                notifReceiver.setTodayRelease(context!!, Notif.TYPE_TODAY_RELEASE)
            }else{
                notifReceiver.cancelReminder(context!!, Notif.TYPE_TODAY_RELEASE)
            }
        }
    }


}