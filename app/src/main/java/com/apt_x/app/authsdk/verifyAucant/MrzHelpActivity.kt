package com.apt_x.app.authsdk.verifyAucant

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.apt_x.app.R

class MrzHelpActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mrz_help)

        val mrzHelpScreen : ConstraintLayout = findViewById(R.id.main_mrz_help_layout)
        findViewById<ImageView>(R.id.main_mrz_image)?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        mrzHelpScreen.setOnClickListener {
            showMrzCaptureCamera()
        }
    }

    private fun showMrzCaptureCamera() {
        val result = Intent()
        result.putExtra("Confirmed", true)
        this@MrzHelpActivity.setResult(Constants.REQUEST_HELP_MRZ, result)
        this@MrzHelpActivity.finish()
    }
}