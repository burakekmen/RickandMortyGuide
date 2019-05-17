package com.burakekmen.rickandmortyguide

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.burakekmen.rickandmortyguide.enums.SharedPreferenceNameEnum
import com.burakekmen.rickandmortyguide.ui.BaseActivity
import com.burakekmen.rickandmortyguide.ui.activity.PolicyActivity
import com.google.android.material.snackbar.Snackbar


class Utils(var context: Context?) {


    private var dialog: SweetAlertDialog? = null

    fun hideStatusBar() {
        (context!! as Activity).actionBar?.hide()
    }


    fun isOnline(): Boolean {
        val connectivityManager = context!!
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }


    fun waitDialogShow() {
        dialog = SweetAlertDialog(context!!, SweetAlertDialog.PROGRESS_TYPE)
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
        dialog = SweetAlertDialog(context!!, SweetAlertDialog.WARNING_TYPE)
        dialog!!.titleText = context!!.getString(R.string.networkConnectionErrorTitle)
        dialog!!.contentText = context!!.getString(R.string.networkConnectionErrorContentText)
        dialog!!.cancelText = context!!.getString(R.string.networkConnectionErrorCancelText)
        dialog!!.confirmText = context!!.getString(R.string.networkConnectionErrorConfirmText)
        dialog!!.setConfirmClickListener { sDialog ->
            if (isOnline()) {
                sDialog!!.dismiss()
            } else {
                dialog!!.dismiss()
                internetConnectionWarningShow()
            }
        }
        dialog!!.showCancelButton(true)
        dialog!!.setCancelClickListener { sDialog ->
            sDialog!!.dismiss()
            (context!! as Activity).finish()
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()

    }


    fun privacyPolicyShow() {

        val sharedPref = context!!.getSharedPreferences(
            SharedPreferenceNameEnum.RaMSharedPereference.toString(),
            Context.MODE_PRIVATE
        )

        val isConfirmPolicy = sharedPref.getBoolean("isConfirmPolicy", false)

        if (!isConfirmPolicy) {
            dialog = SweetAlertDialog(context!!, SweetAlertDialog.WARNING_TYPE)
            dialog!!.titleText = context!!.getString(R.string.privacyPolicyMessageTitle)
            dialog!!.contentText = context!!.getString(R.string.privacyPolicyMessageContentText)
            dialog!!.setNeutralText(context!!.getString(R.string.privacyPolicyMessageNeutralMessage))
            dialog!!.confirmText = context!!.getString(R.string.privacyPolicyMessageConfirmText)
            dialog!!.setConfirmClickListener { sDialog ->
                if (isOnline()) {
                    var intent = Intent(context!!, PolicyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context!!.startActivity(intent)
                    sDialog!!.dismiss()
                } else {
                    sDialog!!.dismiss()
                    internetConnectionWarningShow()
                }
            }
            dialog!!.showCancelButton(false)
            dialog!!.setNeutralClickListener { sDialog ->
                sDialog!!.dismiss()
            }
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.show()
        }

    }


    fun rateUsDialogShow() {
        dialog = SweetAlertDialog(context!!, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog!!.setCustomImage(context!!.getDrawable(R.drawable.ic_notification))
        } else
            dialog!!.setCustomImage(context!!.resources.getDrawable(R.drawable.ic_notification))

        dialog!!.titleText = context!!.getString(R.string.rateUsTitleText)
        dialog!!.contentText = context!!.getString(R.string.rateUsContent)
        dialog!!.setNeutralText(context!!.getString(R.string.rateusLaterText))
        dialog!!.confirmText = context!!.getString(R.string.rateUsConfirmText)
        dialog!!.setConfirmClickListener { sDialog ->
            if (isOnline()) {

                val sharedPref = context!!.getSharedPreferences(
                    SharedPreferenceNameEnum.RaMSharedPereference.toString(),
                    Context.MODE_PRIVATE
                )
                val editor = sharedPref!!.edit()
                editor.putBoolean("isRateApp", true)
                editor.putInt("totalCharacterView", 0)
                editor.apply()

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(context!!.getString(R.string.rateUsPlayLink))
                context!!.startActivity(intent)

                sDialog!!.dismiss()
            } else {
                sDialog!!.dismiss()
                internetConnectionWarningShow()
            }
        }
        dialog!!.showCancelButton(false)
        dialog!!.setNeutralClickListener { sDialog ->
            val sharedPref = context!!.getSharedPreferences(
                SharedPreferenceNameEnum.RaMSharedPereference.toString(),
                Context.MODE_PRIVATE
            )
            val editor = sharedPref!!.edit()
            editor.putBoolean("isRateApp", false)
            editor.putInt("totalCharacterView", 0)
            editor.apply()

            sDialog!!.dismiss()
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()

    }


    fun actionFavouriteSuccessDialogShow(message: String) {
        dialog = SweetAlertDialog(context!!, SweetAlertDialog.SUCCESS_TYPE)
        dialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialog!!.titleText = message
        dialog!!.setCancelable(true)
        dialog!!.show()
    }

    fun actionErrorDialogShow(message: String) {
        dialog = SweetAlertDialog(context!!, SweetAlertDialog.WARNING_TYPE)
        dialog!!.progressHelper.barColor = Color.parseColor("#666600")
        dialog!!.titleText = message
        dialog!!.setCancelable(true)
        dialog!!.show()
    }


    fun splashWaitSomeMunite() {
        val secondsDelayed = 1
        Handler().postDelayed({
            context!!.startActivity(Intent(context!!, BaseActivity::class.java))
            (context!! as Activity).finish()
        }, (secondsDelayed * 500).toLong())
    }


    fun getRandomNumber(): Int {
        return (1..30).random()
    }


    fun showSnackBarWithMessage(view: View?, message: String) {
        val snackBar = Snackbar.make(
            view!!, // Parent view
            message, // Message to show
            Snackbar.LENGTH_SHORT // How long to display the message.
        )

        val snackBarView = snackBar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackBarView.setBackgroundColor(context!!.getColor(R.color.snackBarBackgroundColor))
        }

        val snackBarText = snackBarView.findViewById(R.id.snackbar_text) as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackBarText.setTextColor(context!!.getColor(R.color.snackBarTextColor))
        }

        snackBar.show()
    }


}