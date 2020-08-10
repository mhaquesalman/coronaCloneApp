package com.salman.coronacloneapp

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment

class CustomDialog : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Network connection")
            .setMessage("Please check internet connection !")
            .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                
            })
            .setNegativeButton("Settings", DialogInterface.OnClickListener { dialogInterface, i ->
                val intent = Intent(Settings.ACTION_SETTINGS)
                //intent.setData(Uri.parse("package:" + activity!!.packageName))
                startActivity(intent)
            })
        return builder.create()
    }
}