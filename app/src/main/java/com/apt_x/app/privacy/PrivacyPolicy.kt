package com.apt_x.app.privacy

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apt_x.app.BuildConfig
import com.apt_x.app.R
import com.apt_x.app.databinding.ActivityPrivacyPolicyBinding
import com.apt_x.app.preferences.MyPref
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import es.voghdev.pdfviewpager.library.util.FileUtil
import kotlinx.android.synthetic.main.activity_privacy_policy.*
import kotlinx.android.synthetic.main.activity_privacy_policy.view.*

@Suppress("DEPRECATION")
class PrivacyPolicy : AppCompatActivity(), DownloadFile.Listener {
    var mbinding:ActivityPrivacyPolicyBinding?=null
    private lateinit var adaptor:PDFPagerAdapter
    private lateinit var remotePDF:RemotePDFViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding=DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy)
        initViews()
    }

    private fun initViews() {
        //pdfView=mbinding?.pdfView
        mbinding?.progressBar?.visibility=View.VISIBLE
        var type=intent.getStringExtra("type")
        if(MyPref.getInstance(this).readPrefs(MyPref.LANG_TYPE_PREF).equals("fr"))
        {
            if(type.equals("privacy"))
            {
                mbinding?.tvTitle?.setText(getText(R.string.privacyPolicy))
                load_pdf("auth/getAdd?type=policyFR")
            }
            else{
                mbinding?.tvTitle?.setText(getText(R.string.terms_amp_conditions))
                load_pdf("auth/getAdd?type=termsFR")
            }
        }
        else{
            if(type.equals("privacy"))
            {
                mbinding?.tvTitle?.setText(getText(R.string.privacyPolicy))
                load_pdf("auth/getAdd?type=policyEN")
            }
            else{
                mbinding?.tvTitle?.setText(getText(R.string.terms_amp_conditions))
                load_pdf("auth/getAdd?type=termsEN")
            }
        }


        mbinding?.ivBack?.setOnClickListener { onBackPressed() }

    }

    private fun load_pdf(s: String) {
       remotePDF= RemotePDFViewPager(this,BuildConfig.BASE_URL+s,this)
        remotePDF.id=R.id.pdfViewPager

      /* // AppWebViewClients(progressBar)
        progressBar.visibility=View.VISIBLE


        *//* (((TedPermission.Builder) TedPermission.with(this).setPermissionListener(permissionlistener)).setDeniedMessage((CharSequence) getString(R.string.txt_permission_message))).setPermissions("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE").check();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(TermsAndConditionsActivity.this, new String[]{Manifest.permission.CAMERA}, 123);
            }
        }*//*
        val settings: WebSettings = pdfView?.getSettings()!!
        settings.javaScriptEnabled = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.builtInZoomControls = true
        pdfView?.pdfView?.setWebViewClient(WebViewClient())
        settings?.setSupportZoom(true)



        pdfView!!.loadUrl("https://docs.google.com/gview?embedded=true&url=${BuildConfig.BASE_URL+s}")
        print("URL ****** http://docs.google.com/gview?embedded=true&url=${BuildConfig.BASE_URL+s}")

        pdfView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                progressBar.visibility=View.GONE
                return true
            }
        })*/



    }


    override fun onSuccess(url: String?, destinationPath: String?) {
        mbinding?.progressBar?.visibility=View.GONE
        adaptor = PDFPagerAdapter(this,  FileUtil.extractFileNameFromURL(url))
        mbinding?.pdfViewPager?.adapter=adaptor
    }

    override fun onFailure(e: Exception?) {
        mbinding?.progressBar?.visibility=View.GONE
    }

    override fun onProgressUpdate(progress: Int, total: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
     //   adaptor.close()
    }


}