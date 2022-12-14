package com.apt_x.app.views.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apt_x.app.R
import com.apt_x.app.databinding.ActivityAboutBinding
import com.apt_x.app.databinding.ActivitySupportBinding
import com.apt_x.app.model.ChangePasswordResponse
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls
import com.apt_x.app.utils.Utils
import com.apt_x.app.views.activity.changepassword.ChangePasswordViewModel
import com.apt_x.app.views.activity.home.HomeActivity
import com.google.gson.JsonObject

class AboutActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_about)
        initViews()
    }

    private fun initViews() {
        binding.ivBack.setOnClickListener { onBackPressed() }



    }

}