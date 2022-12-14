package com.apt_x.app.authsdk.verifyAucant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.apt_x.app.R

class FacialMatchFailedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_not_match)
        val image = findViewById<ImageView>(R.id.ivCapture)
        image.setImageBitmap(MainActivity.image)
    }

    fun retry(view: View) {
        finish()
    }

    override fun onBackPressed() {
        val facialMatchFailedIntent = Intent(
            this@FacialMatchFailedActivity,
            MainActivity::class.java
        )
        startActivity(facialMatchFailedIntent)
        finish()
    }
}
