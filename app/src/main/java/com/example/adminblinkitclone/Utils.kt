package com.example.adminblinkitclone

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.adminblinkitclone.databinding.ProgressDialogBinding
import com.google.firebase.auth.FirebaseAuth

object Utils {
    private var dialog: AlertDialog?=null
    fun showDialog(context: Context, message: String){
        val progress= ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.text.text=message
        dialog= AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show()
    }
    fun hideDialog(){
        dialog?.dismiss()
    }
    fun Toast(context: Context, message:String){
        android.widget.Toast.makeText(context,message, android.widget.Toast.LENGTH_SHORT).show()
    }
    private var firebaseAuthInstance : FirebaseAuth?=null
    fun getAuthInstance(): FirebaseAuth {
        if (firebaseAuthInstance==null){
            firebaseAuthInstance= FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!
    }

    fun getCurrentUid() : String{
        return firebaseAuthInstance!!.currentUser!!.uid
    }
    fun getRandomUid():String{
        return (1..28).map { (('a'..'z') + ('0'..'9')).random() }.joinToString("")
    }
}