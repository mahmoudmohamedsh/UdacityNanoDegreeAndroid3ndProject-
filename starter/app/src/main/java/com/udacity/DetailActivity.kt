package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        var status = intent.getStringExtra("status")
        var name = intent.getStringExtra("name")
        //
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()

        status_string.text = status
        name_string.text = name



        custom_button.text = "ok"
        custom_button.customButColor = Color.parseColor("#F9A825")
        custom_button.setOnClickListener{
            var intent:Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }

}
