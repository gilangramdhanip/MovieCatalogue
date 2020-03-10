package com.example.moviecatalogue.receiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.moviecatalogue.BuildConfig
import com.example.moviecatalogue.R
import com.example.moviecatalogue.activity.MainActivity
import com.example.moviecatalogue.api.DataRepository
import com.example.moviecatalogue.model.MovieResponse
import com.example.moviecatalogue.model.Release
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Notif : BroadcastReceiver() {

    private val API_KEY: String = BuildConfig.TMDB_API_KEY
    private val DATE_FORMAT = "yyyy-MM-dd"
    private val notifRelease = ArrayList<Release>()
    private val apiService = DataRepository.create()
    private var todayNotifId = 0

    companion object{
        const val TYPE_DAILY_REMINDER = "DailyReminder"
        const val TYPE_TODAY_RELEASE = "TodayRelease"
        const val EXTRA_TYPE = "type"

        val CHANNEL_ID = "channel_01"
        val CHANNEL_NAME: CharSequence = "gilangmc"
        private const val NOTIFICATION_REQUEST_CODE = 200
        var MAX_NOTIFICATION= 0
        private const val GROUP_KEY_DAILY_RELEASE = "group_key_daily_release"
        private const val ID_REMINDER = 100
        private const val ID_TODAY_RELEASE = 101

    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)

        val notifId = if(type.equals(TYPE_DAILY_REMINDER, ignoreCase = true)) ID_REMINDER else ID_TODAY_RELEASE


        when(type){
            TYPE_DAILY_REMINDER ->{
                dailyReleaseNotification(context, notifId)
            }

            TYPE_TODAY_RELEASE ->{
                dailyNotification(context, notifId)
            }
        }

    }

    fun setDailyReminder(context: Context, type: String){
        Log.d("Notifikasi","Tes setting")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notif::class.java)
        val putExtra = intent.putExtra(EXTRA_TYPE, type)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 2)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun setTodayRelease(context: Context, type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notif::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 2)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_TODAY_RELEASE, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }



    private fun dailyReleaseNotification(context: Context, notifId: Int){
        val date = Date()
        val parser = SimpleDateFormat(DATE_FORMAT)
        val formattedDate = parser.format(date)


        val messageMovie = context.resources.getString(R.string.release_content)

        apiService.getTodayRelease(API_KEY, formattedDate, formattedDate, context.getString(R.string.language)).enqueue(object :
            Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("FailureLog", t.message)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                val data = response.body()
                Log.d("ResponseLog", response.toString())
                if(data?.results!= null){
                    MAX_NOTIFICATION = data?.results?.size -1
                }
                data?.results?.forEach {
                    notifRelease.add(Release(todayNotifId, it.title, messageMovie))
                    popNotif(context, notifId)
                    todayNotifId++
                }
            }
        })

    }

    private fun popNotif(context: Context, notifId: Int) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context,
            NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mBuilder: NotificationCompat.Builder

        if(todayNotifId< MAX_NOTIFICATION){
            mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(notifRelease[todayNotifId].title)
                .setContentText(notifRelease[todayNotifId].desc)
                .setGroup(GROUP_KEY_DAILY_RELEASE)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.bell)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.bell))
                .setAutoCancel(true)
        }else{
            val inboxStyle = NotificationCompat.InboxStyle()
                .addLine(notifRelease[todayNotifId].title + " "+ context.resources.getString(R.string.is_released))
                .setBigContentTitle("$todayNotifId "+ context.resources.getString(R.string.released_today))
                .setSummaryText(context.resources.getString(R.string.release_content))
            mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("$todayNotifId new movie released")
                .setGroup(GROUP_KEY_DAILY_RELEASE)
                .setGroupSummary(true)
                .setSmallIcon(R.drawable.bell)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.bell))
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        mNotificationManager.notify(todayNotifId, notification)

    }

    fun dailyNotification(context: Context, notifId: Int){

        Log.d("Notification", "Tes Daily")
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.resources.getString(R.string.notif_title))
            .setContentText(context.resources.getString(R.string.notif_content))
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.bell)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.bell))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)

            mBuilder.setChannelId(CHANNEL_ID)

            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()

        mNotificationManager.notify(notifId, notification)
    }


    fun cancelReminder(context: Context, type: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notif::class.java)
        val requestCode = if (type.equals(TYPE_DAILY_REMINDER, ignoreCase = true)) ID_REMINDER else ID_TODAY_RELEASE
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}
