package com.apt_x.app.views.activity.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import androidx.core.content.ContextCompat
import com.apt_x.app.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.apt_x.app.databinding.ActivityThankYouBinding
import com.apt_x.app.views.activity.home.HomeActivity
import com.apt_x.app.views.activity.verification.AddAddressActivity
import com.apt_x.app.views.base.BaseActivity

class ThankYouActivity : BaseActivity() {

     var type:String ?=null
    var binding: ActivityThankYouBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, R.layout.activity_thank_you)
        initializeViews()
    }

    override fun onClick(v: View?) {
        if (v === binding!!.tvContinue) {
           /* if(type.equals("P"))
            {
                startActivity(
                    Intent(this, AddAddressActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
              finish()
            }
            else
            {*/
                startActivity(Intent(this@ThankYouActivity, HomeActivity::class.java))
                finish()
           // }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun initializeViews() {
      type=intent.getStringExtra("Type")
        binding!!.tvContinue.setOnClickListener(this)
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
                .getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(context,R.color.blue))
        })
    }
}