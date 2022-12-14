package com.apt_x.app.views.base

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.apt_x.app.R
import com.apt_x.app.application.MyApp
import com.apt_x.app.privacy.PrivacyPolicy
import com.apt_x.app.views.activity.home.LogOutTimerUtil

/**
 * Created by shivanivani on 22/4/21
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener,
    LogOutTimerUtil.LogOutListener {
    /**
     * this method is responsible to initialize the views
     */
    val navHostFragmentDelegate: NavHostFragment get() = supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment

    var context: Context = this@BaseActivity
    var activity: Activity = this@BaseActivity

    abstract fun initializeViews()

    /**
     * init custom action bar override it on need
     */
    open fun setCustomActionBar() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun replaceFragment(addBackStack: Boolean, fragment: Fragment, container: Int) {
        val manager = supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.replace(container, fragment)
        val stack = fragment.javaClass.name
        if (addBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

        //Scroll Fragment
    }

    override fun onResume() {
        super.onResume()
        MyApp.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        MyApp.activityPaused()
    }

    //    override fun onStart() {
//        super.onStart()
//        MyApp.activityStart()
//
//    }
//
//    override fun onUserInteraction() {
//        super.onUserInteraction()
//        MyApp.activityInteraction()
//    }
    override fun onStart() {
        super.onStart()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Log.e(ContentValues.TAG, "OnStart () &&& Starting timer")
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        LogOutTimerUtil.startLogoutTimer(this, this)
        Log.e(ContentValues.TAG, "User interacting with screen")
    }


    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //
    //    }
    //
    fun getCurrentFragment(): Fragment {
        return navHostFragmentDelegate.childFragmentManager.fragments[0];
    }

    fun customTextView(view: TextView) {

        val spanTxt = SpannableStringBuilder(
            getString(R.string.i_agree_policy_sign_up)
        )

        spanTxt.append("\n")

        spanTxt.append(getText(R.string.privacyPolicy))

        val clickSpanTerms: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ds.linkColor // you can use custom color
                ds.color = resources.getColor(R.color.blue)

                // ds.bgColor = Color.BLUE
                ds.isUnderlineText = false // this remove the underline
            }

            override fun onClick(textView: View) {
                startActivity(
                    Intent(view.context, PrivacyPolicy::class.java)
                        .putExtra("type", "privacy")
                )
            }
        }

        spanTxt.setSpan(
            clickSpanTerms,
            spanTxt.length - getText(R.string.privacyPolicy).length,
            spanTxt.length,
            0
        )
        spanTxt.setSpan(resources.getColor(R.color.blue), 52, spanTxt.length, 0)


        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)

    }

    fun customTextView2(view: TextView) {


        val spanTxt = SpannableStringBuilder(
            getString(R.string.foreign_exchange_rates_can_go_up_as_well_as_down_when_you_confirm_this_transaction_we_secure_your_exchange_rate_as_soon_as_we_received_your_order_you_are_liable_for_any_close_out_cost_incurred_by_you_if_you_cancel_your_transaction_when_you_confirm_this_transaction_no_details_can_be_changed_and_you_are_entering_into_a_foreign_exchange_transaction_with_us_as_incorporated_into_our_terms_and_conditions)
        )

        spanTxt.append("\n")

        spanTxt.append(getText(R.string.termsandcondition))

        val clickSpanTerms: ClickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ds.linkColor // you can use custom color
                ds.color = resources.getColor(R.color.blue)
                // ds.bgColor = Color.BLUE
                ds.isUnderlineText = false // this remove the underline
            }

            override fun onClick(textView: View) {
                startActivity(
                    Intent(view.context, PrivacyPolicy::class.java)
                        .putExtra("type", "")
                )
            }
        }

        spanTxt.setSpan(
            clickSpanTerms,
            spanTxt.length - getText(R.string.termsandcondition).length,
            spanTxt.length,
            0
        )
        spanTxt.setSpan(resources.getColor(R.color.blue), 52, spanTxt.length, 0)
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)

    }

    override fun doLogout() {
        activity.runOnUiThread(Runnable {
            AlertDialog.Builder(context)
                .setTitle("Timeout")
                .setMessage("Sorry this Session Timeout")
                .setNeutralButton(
                    "OK"
                ) { dialog, which -> finish() }
                .show()
                .getButton(AlertDialog.BUTTON_NEUTRAL)
                .setTextColor(ContextCompat.getColor(context, R.color.blue))
        })
    }
    //abstract override fun onClick(v: View)
}