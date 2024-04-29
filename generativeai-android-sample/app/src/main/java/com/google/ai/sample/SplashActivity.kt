package com.google.ai.sample

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : Activity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(auth.currentUser==null){1
            var item = Intent(this, LoginActivity::class.java)
            startActivity(item)

            finish()
        }else{
                var item = Intent(this, Dashboard::class.java)
                startActivity(item)
                finish()
            }},1000)
    }
}