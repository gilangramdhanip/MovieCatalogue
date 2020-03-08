package com.example.favoriteapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favoriteapps.R


class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
