package com.crowncement.crowncement_complain_management.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Adapter.NotificationAdapter
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.escalate.*
import kotlinx.android.synthetic.main.take_action.*
import kotlinx.android.synthetic.main.take_action.view.*
import java.util.*

class Frag_notificationDetails : Fragment() {

    lateinit var rootView: View
    lateinit var takeAction : TextView
    lateinit var escalate:TextView
    var dataReceived=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        rootView = inflater.inflate(R.layout.frag_notification_details, container, false)
        initiate()
        dataReceived = arguments?.getInt("position")!!


        takeAction.setOnClickListener {l ->

            val v: View = layoutInflater.inflate(R.layout.take_action, null)
            val dialog = BottomSheetDialog(requireContext())

            Objects.requireNonNull(dialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


            val takeActionCancel = v.findViewById<ImageView>(R.id.takeActionCancel)
            val btntakeActSub = v.findViewById<MaterialButton>(R.id.btntakeActSub)

            dialog.setContentView(v)
            dialog.setCancelable(true)
            dialog.show()


            takeActionCancel.setOnClickListener { dialog.hide() }
            btntakeActSub.setOnClickListener { dialog.hide() }
        }

        escalate.setOnClickListener {l ->
            val v: View = layoutInflater.inflate(R.layout.escalate, null)
            val dialog = BottomSheetDialog(requireContext())

            Objects.requireNonNull(dialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            val EscActionCancel = v.findViewById<ImageView>(R.id.EscActionCancel)
            val btnEscActSub = v.findViewById<MaterialButton>(R.id.btnEscActSub)

            dialog.setContentView(v)
            dialog.setCancelable(true)
            dialog.show()
            onRadioButtonClicked(v)


          //  val rGroup = v.findViewById<RadioGroup>(R.id.radioGroup)

            // Set a listener for the radio group to handle radio button selections
            /*
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val radioButton = v.findViewById<RadioButton>(checkedId)
                val selectedOption = radioButton.text.toString()
                // Do something with the selected option
            }

             */
            EscActionCancel.setOnClickListener { dialog.hide() }
            btnEscActSub.setOnClickListener { dialog.hide() }
        }

        return rootView

    }

    fun initiate(){

        takeAction = rootView!!.findViewById<TextView>(R.id.takeAction)
        escalate= rootView!!.findViewById<TextView>(R.id.escalate)
    }




    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_Yes ->
                    if (checked) {
                        // Pirates are the best
                    }
                R.id.radio_no ->
                    if (checked) {
                        // Ninjas rule
                    }
            }
        }
    }


}