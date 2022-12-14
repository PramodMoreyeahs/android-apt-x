package com.apt_x.app.views.customview;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.apt_x.app.R;


/**
 *
 * <p>
 * CustomProgressDialog displays 'ProgressDialog' widget with given custom view.
 */
public class CustomProgressDialog extends Dialog {

    private Context mContext;

    public CustomProgressDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mContext = context;
    }


    public void show(String message) {

        if (!((Activity) mContext).isFinishing()) {
            super.show();
            View view = LayoutInflater.from(mContext).inflate(R.layout.loader_layout, null);
           AppCompatTextView loaderMsg = (AppCompatTextView) view.findViewById(R.id.loader_msg);
            loaderMsg.setText(message);

            setContentView(view);
        }
    }

//    @Override
//    public void show() {
//        if (!((Activity) mContext).isFinishing()) {
//            super.show();
//            View view = LayoutInflater.from(mContext).inflate(R.layout.loader_layout, null);
//            setContentView(view);
//        }
//    }
}
