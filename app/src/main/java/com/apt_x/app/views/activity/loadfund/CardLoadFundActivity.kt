package com.apt_x.app.views.activity.loadfund

import android.app.Activity
import android.app.AlertDialog
import androidx.core.content.ContextCompat
import com.apt_x.app.R
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import com.apt_x.app.databinding.ActivityCardLoadFundBinding
import com.apt_x.app.model.AddDisburesementResponse
import com.apt_x.app.privacy.netcom.retrofit.ApiCalls
import com.apt_x.app.utils.Utils
import com.apt_x.app.views.base.BaseActivity
import io.reactivex.observers.DisposableObserver
import java.text.SimpleDateFormat
import java.util.*

class CardLoadFundActivity : BaseActivity() {
    var binding: ActivityCardLoadFundBinding? = null


    val myCalendar = Calendar.getInstance()

    var apiCalls: ApiCalls? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_card_load_fund)
        initializeViews()

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvContinue ->
                try {
                    if(binding!!.etCardNumber.text.toString().trim().isEmpty()){
                        Utils.showToast(this@CardLoadFundActivity, getString(R.string.Pleaseentercard))

                    }
                    else if(binding!!.etDateOfExpiration.text.toString().trim().isEmpty()){
                        Utils.showToast(this@CardLoadFundActivity, getString(R.string.please_enter_ex))

                    }
                    else{
                        callApiPostEvents()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            R.id.ivBack -> onBackPressed()
            R.id.etDateOfExpiration ->{
                val dialog = DatePickerDialog(
                    this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                )
                dialog.datePicker.minDate =
                    System.currentTimeMillis() - 1000 + 1000 * 60 * 60 * 24 * 1
                dialog.show ()
            }
        }
    }

    var date1 =
        DatePickerDialog.OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel1()
        }
    private fun updateLabel1() {
        val myFormat = "yyyy-MM" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding!!.etDateOfExpiration.setText(sdf.format(myCalendar.getTime()))
    }


    fun callApiPostEvents() {
        apiCalls!!.addDisburesement(
            getIntent().getStringExtra("Amount"),
            "CARD",
            "CAD",
            binding!!.etCardNumber.text.toString(),
            binding!!.etDateOfExpiration.text.toString(),
            disposableObserver
        )
    }


    var disposableObserver: DisposableObserver<AddDisburesementResponse?> =
        object : DisposableObserver<AddDisburesementResponse?>() {
            override fun onNext(getCompanyConstantResponse: AddDisburesementResponse) {
                Utils.hideProgressDialog()
                Utils.showToast(this@CardLoadFundActivity, "Success")

            }
            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                Utils.showToast(this@CardLoadFundActivity, "Failure")
                e.printStackTrace()
            }
            override fun onComplete() {}
        }

    override fun initializeViews() {
        apiCalls = ApiCalls.getInstance(this@CardLoadFundActivity)
        binding!!.llHeader.ivBack.setOnClickListener(this)
        binding!!.llHeader.tvTitle.setText(getString(R.string.load_fund))
        binding!!.tvContinue.setOnClickListener(this)
        binding!!.etDateOfExpiration.setOnClickListener(this)
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