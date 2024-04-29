package com.google.ai.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VRmenu : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vrmenu2)
        val vrcbtn=findViewById<ImageButton>(R.id.vrcbtn)
        vrcbtn.setOnClickListener{
            val launchIntent = packageManager.getLaunchIntentForPackage("com.computationalcore.vincarnival")
            launchIntent?.let { startActivity(it) }
        }
        val vrfbtn=findViewById<ImageButton>(R.id.vrfbtn)
        vrfbtn.setOnClickListener{
            val launchIntent = packageManager.getLaunchIntentForPackage("com.Fantancy.Environment")
            launchIntent?.let { startActivity(it) }
        }
    }
}