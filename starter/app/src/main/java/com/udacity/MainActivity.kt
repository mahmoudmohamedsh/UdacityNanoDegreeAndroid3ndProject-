package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var selectedFile: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )
        custom_button.text = "Download"
        custom_button.setOnClickListener {
            var selectedToDownload = radio_group!!.checkedRadioButtonId
            if (selectedToDownload == -1)
                Toast.makeText(this, "no file selected ", Toast.LENGTH_LONG).show()
            else {
                custom_button.valueAnimator.start()
                var selected : RadioButton= findViewById(selectedToDownload);
                selectedFile = selected.text.toString()
                download()
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var title = intent?.getStringExtra("title")
            if(title == null )
                title = ""
            if (id == downloadID) {
                Log.e("recive download", "Complete")
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.sendNotification("downloaded", context, "success", selectedFile)
            } else {
                Log.e("recive download", "falure")
                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.sendNotification("downloaded", context, "Fail", selectedFile)
            }

        }
    }

    private fun download() {
        try {
            var uri = Uri.parse(URL)
            val request =
                DownloadManager.Request(uri)
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)


            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            Log.e(
                "isInitialized downloadManager",
                " is not null = > " + (request != null).toString() + " " + Uri.parse(URL).toString()
            )
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
            Log.e(
                "isInitialized downloadId",
                (downloadID != null).toString() + "  = > " + (downloadManager != null).toString()
            )
        } catch (e: Exception) {
            Log.e("download error -> ", e.toString())
        }

    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Downloading"

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }


    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
