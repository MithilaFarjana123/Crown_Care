package com.crowncement.crowncement_complain_management.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Model.Data
import com.google.gson.Gson
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Utility {


    fun getCurrentDate(format: String?): String? {
        val c = Calendar.getInstance().time
        println("Current time => $c")
        val df = SimpleDateFormat(format, Locale.getDefault())
        return df.format(c)
    }

    fun changeDateFormat(date: String?, oldFormat: String?, newFormat: String?): String? {
        val newDateString: String
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat(oldFormat)
        var d: Date? = null
        try {
            d = sdf.parse(date)
        } catch (e: ParseException) {
            Log.e(TAG, "ChangeDateFormat: " + e.message)
        }
        sdf.applyPattern(newFormat)
        newDateString = sdf.format(Objects.requireNonNull(d))
        return newDateString
    }

    fun getBaseMessage(activity: Activity, title: String, mess: String, icon: Int, status: Int) {
/*
        when (status) {
            0 -> {
                Alerter.create(activity)
                    .setTitle(title)
                    .setText(mess)
                    .setIcon(icon)
                    .setBackgroundColorRes(R.color.orange)
                    // .setIconColorFilter(0) // Optional - Removes white tint
                    // .setIconSize(38) // Optional - default is 38dp
                    .show()
            }
            1 -> {
                Alerter.create(activity)
                    .setTitle(title)
                    .setText(mess)
                    .setIcon(icon)
                    .setBackgroundColorRes(R.color.crown_icon_color)
                    // .setIconColorFilter(0) // Optional - Removes white tint
                    // .setIconSize() // Optional - default is 38dp
                    .show()
            }
            else -> {
                Alerter.create(activity)
                    .setTitle(title)
                    .setText(mess)
                    .setIcon(icon)
                    .setBackgroundColorRes(R.color.reject)
                    //.setIconColorFilter(0) // Optional - Removes white tint
                    // .setIconSize(38) // Optional - default is 38dp
                    .show()


            }
        }
        */
    }

    fun storeKeyValue(activity: Activity, key: String?, value: String?) {
        val editor =
            activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
        Log.d(key, value!!)
    }


    fun baseLoadingAnimation(activity: Activity, dialog: Dialog, mess: String): Dialog {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mView: View = inflater.inflate(R.layout.loading_dialogg, null, false)
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val message = mView.findViewById<TextView>(R.id.txtDefaultMessage)
        message.text = mess
        dialog.setContentView(mView)
        dialog.setCanceledOnTouchOutside(false)
        val window = dialog.window
        Objects.requireNonNull(window)!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window!!.setBackgroundDrawableResource(R.color.transparent)
        window.setGravity(Gravity.CENTER)
        //dialog.show();
        return dialog
    }

    fun sendActivity(from: Activity, className: String) {
        try {
            val cls = Class.forName("com.crowncement.crowncement_complain_management.Activity.$className")
            val intent = Intent(from, cls)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            from.startActivity(intent)
       //     from.overridePendingTransition(R.anim.left_in, R.anim.right_out)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "SendActivity: " + e.message)
        }
    }



    fun getValueByKey(activity: Activity, key: String?): String? {
        val prefs = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }


    fun commonToast(activity: Activity, title: String, message: String, status: Int) {
        when (status) {
            0 -> {
                MotionToast.createColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity, www.sanju.motiontoast.R.font.helveticabold)
                )

            }
            1 -> {
                MotionToast.createColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity, www.sanju.motiontoast.R.font.helveticabold)
                )
            }
            2 -> {
                MotionToast.createColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity, www.sanju.motiontoast.R.font.helveticabold)
                )
            }
            else -> {
                MotionToast.createColorToast(
                    activity,
                    title,
                    message,
                    MotionToastStyle.INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(activity, www.sanju.motiontoast.R.font.montserrat_bold)
                )
            }
        }
    }




    fun saveUserInfo(info: Data?, activity: Activity) {
        val mPrefs =
            activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(info)
        mPrefs.putString("UserInfo", json)
        mPrefs.commit()
    }


    fun getUserInfo(activity: Activity): Data? {
        val prefs = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString("UserInfo", "")
        return gson.fromJson(json, Data::class.java)
    }


    fun noInternetDialog(activity: Activity) {
        val v: View = activity.layoutInflater.inflate(R.layout.layout_no_internet, null)
        val dialog = Dialog(v.context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        val btnRefresh = v.findViewById<Button>(R.id.btnRefresh)
     //   val btnOfflineAttendance = v.findViewById<Button>(R.id.btnOfflineAttendance)
      //  val btnQuickVisit = v.findViewById<Button>(R.id.btnQuickVisit)
        //val mPlayer: MediaPlayer = MediaPlayer.create(activity, R.raw.check_internet_connection)
        // mPlayer.start()
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.show()

        btnRefresh.setOnClickListener {
            //val intent = Intent(activity, Act_Splash_Screen::class.java)
            //  activity.startActivity(intent)
            dialog.dismiss()
        }

//        btnOfflineAttendance.setOnClickListener {
//            dialog.dismiss()
//            val intent = Intent(activity, Act_Offline_Attendance::class.java)
//            activity.overridePendingTransition(0, 0)
//            activity.startActivity(intent)
//        }

//        btnQuickVisit.setOnClickListener {
//            val intent = Intent(activity, Act_Offline_QuickVisit::class.java)
//            activity.overridePendingTransition(0, 0)
//            activity.startActivity(intent)
//        }
    }


    fun removePreference(context: Context, key: String?) {
        val preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(key)
        editor.commit()
    }


}