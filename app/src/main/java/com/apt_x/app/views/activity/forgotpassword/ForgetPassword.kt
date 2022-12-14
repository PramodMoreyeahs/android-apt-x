package com.apt_x.app.views.activity.forgotpassword

import android.app.Activity
import android.app.AlertDialog
import androidx.core.content.ContextCompat
import com.apt_x.app.R
import android.os.Bundle
import android.view.View
import com.apt_x.app.views.base.BaseActivity

class ForgetPassword : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
    }

    override fun initializeViews() {
    }

    override fun doLogout() {
        Alertdialog()
    }

    fun Alertdialog() {
        activity.runOnUiThread(Runnable {
            AlertDialog.Builder(context)
                .setTitle("Timeout")
                .setMessage("Sorry this Session Timeout")
                .setNeutralButton(
                    "OK"
                ) { dialog, which -> finish() }
                .show()
                .getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context,
                    R.color.blue))
        })
    }
}