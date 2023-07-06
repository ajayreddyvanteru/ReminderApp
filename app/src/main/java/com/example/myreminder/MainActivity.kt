package com.example.myreminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.userMasterFragment)

//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val broadcastIntent = Intent(this, MyBroadcastReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_IMMUTABLE )
//
//        val intervalMillis = 10000 // 20 seconds
//
//        val startTime = SystemClock.elapsedRealtime()
//        alarmManager.setRepeating(
//            AlarmManager.RTC,
//            startTime,
//            intervalMillis.toLong(),
//            pendingIntent
//        )
        val fromNotification = intent.getBooleanExtra("FROM_NOTIFICATION", false)
        if (fromNotification) {
            val serviceIntent = Intent(this, MyForegroundService::class.java)
            serviceIntent.putExtra("ARGUMENT_KEY", "Your argument value")
            ContextCompat.startForegroundService(this, serviceIntent)
        }else{
            val serviceIntent = Intent(this, MyForegroundService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }

}