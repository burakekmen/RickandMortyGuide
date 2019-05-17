package com.burakekmen.rickandmortyguide.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.burakekmen.rickandmortyguide.R
import com.burakekmen.rickandmortyguide.Utils
import com.burakekmen.rickandmortyguide.ui.activity.PolicyActivity
import kotlinx.android.synthetic.main.fragment_settings_view.*

class SettingsFragment : Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var utils: Utils? = null
    private var flayout: View? = null


//    init {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//    }

    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        acilisAyarlariniYap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if (flayout == null)
            flayout = inflater.inflate(R.layout.fragment_settings, container, false)

        return flayout
    }


    private fun acilisAyarlariniYap() {
        utils = Utils(context!!)
        fragment_settings_view_txtVersionNumber!!.text =
            "v" + context!!.packageManager!!.getPackageInfo(context!!.packageName!!, 0).versionName

        Toast.makeText(context!!, getString(R.string.comingSoonMessage), Toast.LENGTH_SHORT).show()


        fragment_settings_view_txtPrivacyPolicy!!.setOnClickListener(this)
        fragment_settings_view_txtSendMessage!!.setOnClickListener(this)
        fragment_settings_view_txtRateUs?.setOnClickListener(this)
        fragment_settings_view_txtVersionNumber?.setOnClickListener(this)
        spinnerTanimla()
    }


    fun showDialog(view: View) {

        val alertDialog = AlertDialog.Builder(context!!)
            //set icon
            .setIcon(android.R.drawable.ic_dialog_alert)
            //set title
            .setTitle("Are you sure to Exit")
            //set message
            .setMessage("If yes then application will close")
            //set positive button
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i ->
                //set what would happen when positive button is clicked
                activity!!.finish()
            })
            //set negative button
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                //set what should happen when negative button is clicked
                Toast.makeText(context!!, "Nothing Happened", Toast.LENGTH_LONG).show()
            })
            .show()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fragment_settings_view_txtPrivacyPolicy -> {
                var intent = Intent(context!!, PolicyActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context!!.startActivity(intent)
            }
            R.id.fragment_settings_view_txtSendMessage -> {
                Toast.makeText(context!!, getString(R.string.comingSoonMessage), Toast.LENGTH_SHORT).show()
            }
            R.id.fragment_settings_view_txtRateUs -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(context!!.getString(R.string.rateUsPlayLink))
                context!!.startActivity(intent)
            }
            R.id.fragment_settings_view_txtVersionNumber -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(context!!.getString(R.string.rateUsPlayLink))
                context!!.startActivity(intent)
            }
        }
    }


    private fun spinnerTanimla() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.languagePack,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fragment_settings_view_languageSpinner!!.adapter = adapter
        }


        fragment_settings_view_languageSpinner?.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var index = position
        var languageCodeArray = resources.getStringArray(R.array.languagePackCode)

        when(index){
            0->{
                Toast.makeText(context!!, languageCodeArray[index], Toast.LENGTH_SHORT).show()
                //LocateHelper.setLocale(activity!!,languageCodeArray[index])
            }
            1->{
                Toast.makeText(context!!, languageCodeArray[index], Toast.LENGTH_SHORT).show()
                //LocateHelper.setLocale(activity!!,languageCodeArray[index])
            }
            2->{
                Toast.makeText(context!!, languageCodeArray[index], Toast.LENGTH_SHORT).show()
                //LocateHelper.setLocale(activity!!,languageCodeArray[index])
            }
            3->{
                Toast.makeText(context!!, languageCodeArray[index], Toast.LENGTH_SHORT).show()
                //LocateHelper.setLocale(activity!!,languageCodeArray[index])
            }
        }
    }


}
