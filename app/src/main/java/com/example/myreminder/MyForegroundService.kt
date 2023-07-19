package com.example.myreminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

//Work even phone is locked
class MyForegroundService : Service() {
    private val notificationId = 1
    private val channelId = "my_channel_id"
    private var isMusicPlaying = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.mothersong)
        mediaPlayer.isLooping = true
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the service in the foreground with a notification
        startForeground(notificationId, createNotification())
        val argumentValue = intent?.getStringExtra("ARGUMENT_KEY")
        if (argumentValue != null) {
            stopMusicPlayback()
        }
        val context = this
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                val db = MyDatabaseHelper(context)
                val formatter = SimpleDateFormat("d/M")
                val str: String = formatter.format(Date())
                var ampm=""
                var hour = Calendar.getInstance().get(Calendar.HOUR).toString()
                if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 12 && Calendar.getInstance().get(Calendar.MINUTE)>=0 ) {
                    ampm = "PM"
                } else {
                    ampm = "AM"
                }
                if (hour == "0") {
                    hour="12"
                }
                var min = Calendar.getInstance().get(Calendar.MINUTE).toString()
                var sec = Calendar.getInstance().get(Calendar.SECOND).toString()

                eventReminder(context,db,str,ampm,hour,min,sec)

                customReminder(context,db,ampm,hour,min,sec)
            }
        }, 0, 1000) // 20 seconds
        return START_STICKY
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun customReminder(
        context: MyForegroundService,
        db: MyDatabaseHelper,
        ampm: String,
        hour: String,
        min: String,
        sec: String
    ){
        val result = db.getUsers1()
        for (i in result) {
            if (hour == i.hour && min.toInt() == i.min.toInt() && ampm==i.ampm && LocalDate.now().dayOfWeek.toString()==i.weekdays && sec=="1") {
                showCustomNotification(context, i.name,i.Discription)
                startMusicPlayback()
            } else if (hour == i.hour && min.toInt() == i.min.toInt() && ampm==i.ampm && "DAILY"==i.weekdays && sec=="1") {
                showCustomNotification(context, i.name,i.Discription)
                startMusicPlayback()
            }
            val weekdays= arrayOf("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY")
            if (hour == i.hour && min.toInt() == i.min.toInt() && ampm==i.ampm && LocalDate.now().dayOfWeek.toString() in weekdays && "MONDAY-FRIDAY"==i.weekdays  && sec=="1") {
                showCustomNotification(context, i.name,i.Discription)
                startMusicPlayback()
            }
        }
    }
    fun eventReminder(
        context: MyForegroundService,
        db: MyDatabaseHelper,
        str: String,
        ampm: String,
        hour: String,
        min: String,
        sec: String
    ) {
        val result = db.getUsers()
        for (i in result) {
            var mnth = ""
            when (i.mnth) {
                "January" -> mnth = "1"
                "February" -> mnth = "2"
                "March" -> mnth = "3"
                "April" -> mnth = "4"
                "May" -> mnth = "5"
                "June" -> mnth = "6"
                "July" -> mnth = "7"
                "Augest" -> mnth = "8"
                "september" -> mnth = "9"
                "October" -> mnth = "10"
                "November" -> mnth = "11"
                "December" -> mnth = "12"
                else -> mnth = "Month not exist"
            }
            if (str == i.days + "/" + mnth && hour == i.hour && min.toInt() == i.min.toInt() && ampm==i.ampm && sec=="1") {
                showCustomNotification(context, i.name,i.Discription)
                startMusicPlayback()
            }
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("FROM_NOTIFICATION", true)
        intent.action = "STOP_FOREGROUND_SERVICE"
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotification(): Notification {
        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "My Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("My App")
            .setContentText("Foreground Service is running")
            .setSmallIcon(R.drawable.ic_menu_24)

        return notificationBuilder.build()
    }

    fun showCustomNotification(context: Context, name: String, discription: String) {
        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "custom_notification_channel"
            val channelName = "Custom Notification Channel"
            val channelDescription = "Channel for custom notifications"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableLights(true)
                lightColor = Color.GREEN
            }
            notificationManager.createNotificationChannel(channel)
        }
        // Create the custom notification
        val notificationBuilder = NotificationCompat.Builder(context, "custom_notification_channel")
            .setSmallIcon(R.drawable.ic_icon_add)
            .setContentTitle(name)
            .setContentText(discription)
            .setColor(Color.RED)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Remove the notification when clicked
            .setOngoing(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText("Ajay you got notification."))
            .addAction(R.drawable.ico_delete, "Ajay today,"+name+" "+discription+"", null)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        val pendingIntent = createPendingIntent()
        notificationBuilder.setContentIntent(pendingIntent)
        // Show the notification
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(1, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    private fun startMusicPlayback() {
        if (!isMusicPlaying) {
            try {
                mediaPlayer.start()
                isMusicPlaying = true
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopMusicPlayback() {
        if (isMusicPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, R.raw.mothersong)
            mediaPlayer.isLooping = true
            isMusicPlaying = false
        }
    }
}
