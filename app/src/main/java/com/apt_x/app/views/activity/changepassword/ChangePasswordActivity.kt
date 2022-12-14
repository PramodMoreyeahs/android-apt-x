package com.apt_x.app.views.activity.changepassword

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.apt_x.app.R
import com.apt_x.app.interfaces.DialogClickListener
import com.apt_x.app.model.bean.ChangePasswordBean
import com.apt_x.app.utils.AppUtils
import com.apt_x.app.utils.LocaleHelper
import com.apt_x.app.utils.Utils
import com.apt_x.app.views.base.BaseActivity
import com.apt_x.app.views.customview.CustomActionBar

class ChangePasswordActivity : BaseActivity(),DialogClickListener {


    private var viewModel: ChangePasswordViewModel? = null


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setCustomActionBar()
        initializeViews()
    }
    override fun setCustomActionBar() {
        super.setCustomActionBar()
        val customActionBar = CustomActionBar(this)
        customActionBar.setActionbar(getString(R.string.change_password), true, false, this)
    }
    var response_observer: Observer<ChangePasswordBean> =
        Observer { bean ->
            if (bean == null) {
                Utils.showAlert(
                    context,
                    "",
                    getString(R.string.server_error),
                    getString(R.string.ok),
                    "",
                    AppUtils.dialog_ok_click,
                    this@ChangePasswordActivity
                )
                return@Observer
            }
            if (bean.code == AppUtils.STATUS_SUCCESS) {
                Utils.showAlert(
                    context,
                    "",
                    bean.message,
                    getString(R.string.ok),
                    "",
                    AppUtils.dialog_request_succes,
                    this@ChangePasswordActivity
                )
            } else if (bean.code == AppUtils.STATUS_FAIL) {
                Utils.showAlert(
                    context,
                    "",
                    bean.message,
                    getString(R.string.ok),
                    "",
                    AppUtils.dialog_ok_click,
                    this@ChangePasswordActivity
                )
                return@Observer
            }
        }


    var observer: Observer<*> =
        Observer<Int?> { value ->
            when (value) {
                AppUtils.empty_old_password -> Utils.showToast(
                    context,
                    getString(R.string.enter_old_password)
                )
                AppUtils.empty_password -> Utils.showToast(
                    context,
                    getString(R.string.enter_password)
                )
                AppUtils.empty_confirm_password -> Utils.showToast(
                    context,
                    getString(R.string.enter_confirm_password)
                )
                AppUtils.match_confirm_password -> Utils.showToast(
                    context,
                    getString(R.string.confirm_password_match)
                )
                AppUtils.SERVER_ERROR -> Utils.showToast(context, getString(R.string.server_error))
                AppUtils.NO_INTERNET -> {
                    Utils.hideProgressDialog()
                    Utils.showToast(context, getString(R.string.internet_connection))
                }
            }
        }


    override fun onClick(v: View?) {
//        when (v!!.getId()) {
//            R.id.tv_submit -> if (viewModel.isValidate(binding)) {
//                Utils.showDialog(context, getString(R.string.please_wait))
//                viewModel.doChangePassword(binding, context)
//            }
//            R.id.left_iv -> finish()
//        }
//
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

    override fun initializeViews() {

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
//        binding.tvSubmit.setOnClickListener(this)
//        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
//        binding.setViewModel(viewModel)
//        viewModel.validator.observe(this, observer)
//
//        viewModel.response_validator.observe(this, response_observer)

    }

    override fun onDialogClick(which: Int, requestCode: Int) {
        when (which) {
            AppUtils.dialog_request_succes -> finish()
        }
    }
}