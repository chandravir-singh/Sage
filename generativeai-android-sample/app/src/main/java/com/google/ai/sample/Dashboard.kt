package com.google.ai.sample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton




class Dashboard : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val vrbtn=findViewById<ImageButton>(R.id.vrbtn)
        val chatbtn=findViewById<ImageButton>(R.id.chatbtn)
        val arbtn=findViewById<ImageButton>(R.id.arbtn)
        vrbtn.setOnClickListener {
            val regAc = Intent(this, VRmenu::class.java)
            startActivity(regAc)
        }
        chatbtn.setOnClickListener {
                val regAc = Intent(this, MainActivity::class.java)
                startActivity(regAc)
            }
        arbtn.setOnClickListener {
            val regAc = Intent(this, ARActivity::class.java)
            startActivity(regAc)
        }
        val callButton = findViewById<FloatingActionButton>(R.id.sosbut)
        callButton.setOnClickListener { showPopup() }
    }

    private fun showPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to call an ambulance?")
            .setPositiveButton(
                "Yes"
            ) { dialog, id -> makePhoneCall() }
            .setNegativeButton("No") { dialog, id ->
                // User cancelled the dialog
            }
        // Create the AlertDialog object and return it
        builder.create().show()
    }

    private fun makePhoneCall() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.CALL_PHONE),
                Dashboard.Companion.REQUEST_PHONE_CALL_PERMISSION
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.setData(Uri.parse("tel:7460956644")) // Replace with the desired phone number
            startActivity(callIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Dashboard.Companion.REQUEST_PHONE_CALL_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }
        }
    }

    companion object {
        private const val REQUEST_PHONE_CALL_PERMISSION = 1
    }
}
