package com.apt_x.app.authsdk.verifyAucant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.apt_x.app.R

class ClassificationFailureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classification_failure)

        val image = findViewById<ImageView>(R.id.classificationErrorImage)
        image.setImageBitmap(MainActivity.image)
    }

    fun retryClassificationClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val result = Intent()
        this@ClassificationFailureActivity.setResult(Constants.REQUEST_RETRY, result)
        this@ClassificationFailureActivity.finish()
    }
}
