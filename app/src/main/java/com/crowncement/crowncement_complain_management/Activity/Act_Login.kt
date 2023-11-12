package com.crowncement.crowncement_complain_management.Activity

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.common.Utility.baseLoadingAnimation
import com.crowncement.crowncement_complain_management.common.Utility.saveUserInfo
import com.crowncement.crowncement_complain_management.data.Model.Data
import com.crowncement.crowncement_complain_management.ui.viewmodel.LoginViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.LoginViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class Act_Login : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loadingAnim: Dialog

   // private val REQUEST_BACKGROUND_LOCATION = 66
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingAnim = baseLoadingAnimation(this, Dialog(this), "P l e a s e    w a i t")

        //requestBackgroundLocationPermission()
        regularTask()
    }


    private fun regularTask() {
        loadingAnim = baseLoadingAnimation(this, Dialog(this), "P l e a s e    w a i t")


        LoginViewModelFactory()

        setupViewModel()

        checkAutoLogin()

        txtRegister.setOnClickListener {
            Utility.sendActivity(this, "Act_Forget_Password")
        }

        btnLogin.setOnClickListener {

            //Demo user login for google play store

            if (txtEMPID.text.toString() == "E00-000000" && txtPass.text.toString() == "demo1234") {

                Utility.sendActivity(this, "MainActivity")
            }

            // Crown cement user

            else {
                val fcmToken = Utility.getValueByKey(this, "fcm_token").toString()
                Utility.storeKeyValue(this, "username", txtEMPID.text.toString())
                if (cbRemember.isChecked) {
                    val pass = txtPass.text.toString()
                    val device = getAndroidDeviceId()

                    Utility.storeKeyValue(this, "pass", pass)
                    Utility.storeKeyValue(this, "deviceId", device)

                }
                if (loginUIValidation()) {
                    loadingAnim.show()
                    getUserLogin(
                        txtEMPID.text.toString(),
                        txtPass.text.toString(),
                        getAndroidDeviceId(),
                        fcmToken
                    )
                }
            }
        }

        if (txtEMPID.text.isNullOrEmpty()) {
            txtEMPID.setText("E")
            txtEMPID.setSelection(txtEMPID.length())
        }

        //TODO EMP ID AUTO TEXT
        txtEMPID.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s.toString().isEmpty()) {
                    txtEMPID.setText("E")
                    txtEMPID.setSelection(txtEMPID.length())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s.toString().length == 3) {
                    val givenText = txtEMPID.text.toString() + "-"
                    txtEMPID.setText(givenText)
                    txtEMPID.setSelection(txtEMPID.length())
                }
            }
        })
    }

/*
    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                REQUEST_BACKGROUND_LOCATION
            )
        } else {
            regularTask()
        }
    }

 */



    private fun checkAutoLogin() {
        loadingAnim = baseLoadingAnimation(this, Dialog(this), "P l e a s e    w a i t")

        val username: String = Utility.getValueByKey(this, "username")!!
        val pass: String = Utility.getValueByKey(this, "pass")!!
        val deviceId: String = Utility.getValueByKey(this, "deviceId")!!
        val fcm = Utility.getValueByKey(this, "fcm_token").toString()

        if (username.isNotBlank() && pass.isNotBlank() && deviceId.isNotBlank() && fcm.isNotBlank()) {
            txtEMPID.setText(username)
            txtPass.setText(pass)
            cbRemember.isChecked = true
            loadingAnim.show()
            getUserLogin(username, pass, deviceId, fcm)
        }
    }

    private fun getAndroidDeviceId(): String {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }


    private fun setupViewModel() {
        viewModel =
            ViewModelProviders.of(this, LoginViewModelFactory()).get(LoginViewModel::class.java)
             viewModel =  ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun getUserLogin(userName: String, pass: String, deviceId: String, fcm: String) {

        viewModel.getUserInfo(userName, pass, deviceId, fcm).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.responseData?.let { res ->

                        loadingAnim.dismiss()
                        if (res.message.equals("Success Message")) {
                            val userData: Data? = res.data
                            saveUserInfo(userData, this)
                            var user_name= res.data?.userName.toString()
                            Utility.storeKeyValue(this,"user_name",user_name)
                            var user_desig= res.data?.userDesig.toString()
                            Utility.storeKeyValue(this,"user_desig",user_desig)
                            var user_location= res.data?.userLocation.toString()
                            Utility.storeKeyValue(this,"user_location",user_location)
                            var user_dept= res.data?.userDept.toString()
                            Utility.storeKeyValue(this,"user_dept",user_dept)
                            var user_number = res.data?.userMobile.toString()
                            Utility.storeKeyValue(this,"user_number",user_number)
                            var user_email = res.data?.userEmail.toString()
                            Utility.storeKeyValue(this,"user_email",user_email)


                            var img = res.data?.userProfileImagePath.toString()
                            Utility.storeKeyValue(this,"user_img",img)



                            Utility.sendActivity(this, "MainActivity")
                        } else {
                            Utility.commonToast(this, "Login failed", res.message.toString(), 2)
                        }
                    }
                }

                Status.LOADING -> {

                }
                Status.ERROR -> {
                    loadingAnim.dismiss()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loginUIValidation(): Boolean {

        layoutEmpId.error = null
        laypass.error = null

        return when {
            txtEMPID.text.isNullOrBlank() -> {
                layoutEmpId.error = "Please, provide your employee id"
                false
            }
            txtPass.text!!.isNullOrBlank() -> {
                laypass.error = "Password is required"
                false
            }
            else -> true
        }
    }


/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            REQUEST_BACKGROUND_LOCATION -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    regularTask()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                    //requestBackgroundLocationPermission()
                }
                return
            }

        }

    }

 */






}