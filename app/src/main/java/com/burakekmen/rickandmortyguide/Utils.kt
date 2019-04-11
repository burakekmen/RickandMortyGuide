package com.burakekmen.rickandmortyguide

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import cn.pedant.SweetAlert.SweetAlertDialog
import com.burakekmen.rickandmortyguide.enums.SharedPreferenceNameEnum
import com.burakekmen.rickandmortyguide.ui.BaseActivity
import com.burakekmen.rickandmortyguide.ui.activity.PolicyActivity


class Utils(context: Context) {


    var context = context
    private var dialog: SweetAlertDialog? = null

    fun hideStatusBar() {

//        (context as Activity).window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//                (context as Activity).actionBar?.hide()
        (context as Activity).actionBar?.hide()
    }


    fun isOnline(): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }


    fun waitDialogShow() {
        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        dialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialog!!.titleText = "Loading"
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun waitDialogHide() {
        if (dialog != null) {
            if (dialog!!.isShowing)
                dialog!!.dismiss()
        }
    }


    fun internetConnectionWarningShow() {
        dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        dialog!!.titleText = "Uppss.."
        dialog!!.contentText = "Please check your internet connection!"
        dialog!!.cancelText = "No, I will not!"
        dialog!!.confirmText = "Ok, I will check it!"
        dialog!!.setConfirmClickListener { sDialog ->
            if (isOnline()) {
                sDialog.cancel()
            } else {
                dialog!!.dismiss()
                internetConnectionWarningShow()
            }
        }
        dialog!!.showCancelButton(true)
        dialog!!.setCancelClickListener { sDialog ->
            sDialog.cancel()
            (context as Activity).finish()
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()

    }


    fun privacyPolicyShow() {

        val sharedPref = context.getSharedPreferences(
            SharedPreferenceNameEnum.RaMSharedPereference.toString(),
            Context.MODE_PRIVATE
        )

        var isConfirmPolicy = sharedPref.getBoolean("isConfirmPolicy", false)

        if(!isConfirmPolicy) {
            dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            dialog!!.titleText = "Privacy Policy"
            dialog!!.contentText = "Please read us Privacy Policy"
            dialog!!.setNeutralText("Later")
            dialog!!.confirmText = "Okey"
            dialog!!.setConfirmClickListener { sDialog ->
                if (isOnline()) {
                    context!!.startActivity(Intent(context, PolicyActivity::class.java))
                    sDialog.cancel()
                } else {
                    sDialog.cancel()
                    internetConnectionWarningShow()
                }
            }
            dialog!!.showCancelButton(false)
            dialog!!.setNeutralClickListener { sDialog ->
                //Toast.makeText(context, "Later!", Toast.LENGTH_SHORT).show()
                sDialog.cancel()
            }
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        }

    }


    fun rateUsDialogShow() {
        dialog = SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
        dialog!!.setCustomImage(context.getDrawable(R.drawable.ic_notification))
        dialog!!.titleText = "Rate Us"
        dialog!!.contentText = "Do you LIKE US?\n\nCould you give us Play Store Comment If You Like Us?"
        dialog!!.setNeutralText("Later")
        dialog!!.confirmText = "Ok! Rick!"
        dialog!!.setConfirmClickListener { sDialog ->
            if (isOnline()) {

                val sharedPref = context.getSharedPreferences(
                    SharedPreferenceNameEnum.RaMSharedPereference.toString(),
                    Context.MODE_PRIVATE
                )
                val editor = sharedPref!!.edit()
                editor.putBoolean("isRateApp", true)
                editor.putInt("totalCharacterView", 0)
                editor.apply()

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=com.burakekmen.rickandmortyguide")
                context.startActivity(intent)

                sDialog.cancel()
            } else {
                sDialog.cancel()
                internetConnectionWarningShow()
            }
        }
        dialog!!.showCancelButton(false)
        dialog!!.setNeutralClickListener { sDialog ->
            //Toast.makeText(context, "Later!", Toast.LENGTH_SHORT).show()
            sDialog.cancel()
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()

    }


    fun actionFavouriteSuccessDialogShow(message: String) {
        dialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        dialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialog!!.titleText = message
        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun actionErrorDialogShow(message: String) {
        dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        dialog!!.progressHelper.barColor = Color.parseColor("#666600")
        dialog!!.titleText = message
        dialog!!.setCancelable(true)
        dialog!!.show()
    }


    fun splashWaitSomeMunite() {
        val secondsDelayed = 1
        Handler().postDelayed({
            context.startActivity(Intent(context, BaseActivity::class.java))
            (context as Activity).finish()
        }, (secondsDelayed * 500).toLong())
    }


    fun getRandomNumber(): Int {
        return (1..30).random()
    }


}