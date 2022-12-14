package com.apt_x.app.views.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.apt_x.app.R
import com.apt_x.app.databinding.ActivitySupportBinding
import com.apt_x.app.model.ChangePasswordResponse
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls
import com.apt_x.app.utils.Utils
import com.apt_x.app.views.activity.changepassword.ChangePasswordViewModel
import com.apt_x.app.views.activity.home.HomeActivity
import com.google.gson.JsonObject

class SupportActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySupportBinding
    var viewModel: ChangePasswordViewModel? = null
    var apiCalls: ApiCalls? = null
    var suportList: List<String> = ArrayList()
    var email:String=""
    var issue:String=""
    var detail:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_support)
        initViews()
    }

    private fun initViews() {
        viewModel=ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        apiCalls=ApiCalls.getInstance(this)
        viewModel!!.response_validator_issue.observe(this,resetObserver)
        binding.ivBack.setOnClickListener { onBackPressed() }
        binding.spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val value = parentView!!.getItemAtPosition(position).toString()
                val issue= resources.getStringArray(R.array.issue_aaray)
                if(value == issue[0]){
                    (selectedItemView as TextView).setTextColor(Color.parseColor("#676767"))
                }

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here



            }
        })
        suportList= arrayListOf("Issue type","Identification"," Close Account","Change of Address","Change of Name"," Transaction Issues")
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this@SupportActivity,
            R.layout.spinner_item,
            resources.getTextArray(R.array.issue_aaray)
        )
        ad.setDropDownViewResource(R.layout.spinner_item)
        binding.spinner.setPopupBackgroundResource(R.drawable.iv_bg)

        binding.spinner.adapter = ad

        binding.tvContinue.setOnClickListener {
            if(binding.etEmail.text.isEmpty()){
                Utils.showToast(applicationContext,getString(R.string.enter_emailid))
            }
            else if(issue.equals("Issue type") || issue.equals("Type de probleme")){
                Utils.showToast(applicationContext,getString(R.string.please_select_issue_type))
            }
            else{
                   Utils.showDialog(this,"Loading")
                    viewModel!!.sendIssue(apiCalls,binding.etEmail.text.toString(),issue,binding.etDetail.text.toString())

            }
        }

    }
    var resetObserver: Observer<JsonObject> =
        Observer { data ->
            var status=data.get("status").asBoolean
            var message=data.get("message").asString

            if (status) {
                Utils.showToast(applicationContext, message.toString())
                startActivity(Intent(this@SupportActivity,HomeActivity::class.java))
            } else {
                Utils.showToast(applicationContext,message.toString())
            }
        }
}