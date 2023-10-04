package com.crowncement.crowncement_complain_management.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.common.Utility.getCurrentDate
import com.crowncement.crowncement_complain_management.common.Utility.getValueByKey
import com.crowncement.crowncement_complain_management.common.Utility.storeKeyValue
import com.crowncement.crowncement_complain_management.data.Model.AppVersionResponse
import com.crowncement.crowncement_complain_management.data.Model.Data
import com.crowncement.crowncement_complain_management.ui.Receiver.ConnectivityReceiver
import com.crowncement.crowncement_complain_management.ui.viewmodel.SplashViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.SplashViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.act_splash_screen.*
import kotlinx.android.synthetic.main.layout_no_internet.*
import kotlinx.android.synthetic.main.loading_dialogg.*
import java.util.*


class Act_Splash_Screen : AppCompatActivity() {

    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var splashViewModel: SplashViewModel


    var PERMISSION_ALL = 1
    private val permissionCode = 101

    private val TAG = "Act_Splash_Screen"

    lateinit var alertD: AlertDialog

    var isUpdateDialogShowing = false

     var PERMISSIONS = arrayOf(
         Manifest.permission.CAMERA,
         Manifest.permission.READ_EXTERNAL_STORAGE,
         Manifest.permission.WRITE_EXTERNAL_STORAGE
         )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash_screen)
      //  getFCMToke()
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        alertD = AlertDialog.Builder(this).create()
        //val locPermission: String = getValueByKey(this, "location_dialog")!!
        try {
                appPermission()

        } catch (e: Exception) {
         //   appPermission()
        }
        //FirebaseApp.initializeApp(this)

       // getFCMToke()
       // animation()

//privious
//        SplashViewModelFactory()
//        if (checkPermissions()) {
//            animation()
//        }else{
//            requestPermissions()
//            animation()
//
//        }


    }








    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(

                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissionCode
        )
    }

    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun animation(){
        val leftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        lottieAnimationView = findViewById(R.id.lottie)
        lottie.startAnimation(leftAnimation)



        // Delay for a few seconds and then launch the main activity
        Handler().postDelayed({
            val intent = Intent(this, Act_Login::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }



    @SuppressLint("ServiceCast")
    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    private fun appPermission() {
        try {
            if (!hasPermissions(this, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
                regularTask()
            } else {
                regularTask()
            }
        } catch (e: Exception) {
            val error = e.message

        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun regularTask() {
        setContentView(R.layout.act_splash_screen)


                // TODO  GET-----> FCM TOKEN FOR PUSH NOTIFICATION
                getFCMToke()
                SplashViewModelFactory()
                setupViewModel()



                //todo --- Set current app version
                val cV = getAppVersion()
                txtAppVersionNo.text = cV

                //TODO UPDATE DIALOGUE CHECK
                getLatestAppVersion("CrownCare", getCurrentDate("yyyy-MM-dd").toString())

/*
        btnRefresh.setOnClickListener {
            this.startActivity(Intent(this, Act_Splash_Screen::class.java))
            overridePendingTransition(0, 0)
        }

 */
    }



    private fun getAppVersion(): String {
        val pInfo: PackageInfo = this.packageManager.getPackageInfo(packageName, 0)
        return pInfo.versionName
    }

    private fun setupViewModel() {
        splashViewModel =
            ViewModelProviders.of(this, SplashViewModelFactory()).get(SplashViewModel::class.java)
    }



    private fun getLatestAppVersion(appName: String, dateTime: String) {

        splashViewModel.getAppVersion(appName, dateTime)
            ?.observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.responseData?.let { res ->
                            if (res.code.equals("200") && res.data.size > 0) {
                                getAppVersionInfo(res)
                            }else{
                                Toast.makeText(this,"error",Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    Status.LOADING -> {

                    }

                    Status.ERROR -> {
                        Utility.getBaseMessage(
                            this,
                            "Error",
                            "Can not communicate with server, please try again after some time",
                            R.drawable.error_white,
                            0
                        )
                    }
                }
            }
    }

    private fun getAppVersionInfo(res: AppVersionResponse) {

     //  storeKeyValue(this, "download_url", res.data[0].version_url.toString())
        val latestV = res.data[0].versionNo
        val currentV = getAppVersion()
        //Show version name
        txtAppVersionNo.text = currentV
        if (latestV == currentV) {
            transferActivity()
        } else {
            updateDialog(currentV, latestV.toString(), res.data[0].version_url.toString())
        }

    }

    private fun getFCMToke() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            storeKeyValue(this, "fcm_token", token)
            Log.e(TAG, "token: $token")
        })
    }




    private fun updateDialog(currentV: String, updateV: String, url: String) {
        if (!isUpdateDialogShowing) {
            isUpdateDialogShowing = true
            val v: View = layoutInflater.inflate(R.layout.update_dialog, null)
            val updateDialog = BottomSheetDialog(this)
            Objects.requireNonNull(updateDialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            val btnUpdateApp = v.findViewById<Button>(R.id.btnUpdateApp)
            val btnUpdatePlayStore = v.findViewById<Button>(R.id.btnUpdatePlayStore)

            val currentVersion = v.findViewById<TextView>(R.id.txtCurrent)
            val updatedVersion = v.findViewById<TextView>(R.id.txtLatest)

            currentVersion.text = currentV
            updatedVersion.text = updateV

            updateDialog.setContentView(v)
            updateDialog.setCancelable(false)
            updateDialog.show()

            btnUpdateApp.setOnClickListener {

                if (url != "") {
                    updateDialog.dismiss()
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }

            btnUpdatePlayStore.setOnClickListener {

                val appPackageName = packageName
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
        }

    }




    private fun transferActivity() {
        val handler = Handler()
        handler.postDelayed({
            try {

                val userINfo: Data = Utility.getUserInfo(this)!!
                val notificationClick = intent.getStringExtra("send_to")

                if (notificationClick != null && userINfo.userId.toString() != "") {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("send_to", notificationClick)
                    startActivity(intent)
                } else {
                    Utility.sendActivity(this, "Act_Login")
                }

            } catch (e: Exception) {
                Log.e(TAG, "transferActivity: " + e.message)
                Utility.sendActivity(this, "Act_Login")
            }

        }, 2000)
    }



}