package com.crowncement.crowncement_complain_management.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Utility
import kotlinx.android.synthetic.main.act_forget_password.*

class Act_Forget_Password : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_forget_password)
        compareWithNewPass()
    }

    private fun compareWithNewPass() {

        txtConfirmPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s!!.length == txtNewPass.length()) {
                    if (s.toString() != txtNewPass.text.toString()) {
                        layconfirmpass.error = "Confirm password doesn't match with new password"
                        layconfirmpass.errorIconDrawable =
                            getDrawable(R.drawable.ic_baseline_warning)
                    } else {
                        layconfirmpass.endIconDrawable = getDrawable(R.drawable.ic_baseline_check_green)
                    }
                } else {
                    if (s.length > txtNewPass.length()) {
                        layconfirmpass.error = "Confirm password doesn't match with new password"
                        layconfirmpass.errorIconDrawable =
                            getDrawable(R.drawable.ic_baseline_warning)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                layconfirmpass.error = null
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utility.ReturnActivity(this, "Act_Login")
    }

}