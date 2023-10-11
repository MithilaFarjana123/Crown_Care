package com.crowncement.crowncement_complain_management.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.*
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSolverViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.take_action.view.*
import java.util.*

class Frag_notificationDetails : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainSolverViewModel

    lateinit var takeAction : TextView
    lateinit var escalate:TextView
    lateinit var actionTaken : String
    lateinit var rq_trn_no : String
    lateinit var rq_trn_row: String
    lateinit var party_code :String

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
            var picker: DatePickerDialog

            Objects.requireNonNull(dialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


            val takeActionCancel = v.findViewById<ImageView>(R.id.takeActionCancel)
            val btntakeActSub = v.findViewById<MaterialButton>(R.id.btntakeActSub)
            var txtFrResolvedDate = v.findViewById<TextInputEditText>(R.id.txtFrResolvedDate)
            var laytakeActDoc = v.findViewById<CardView>(R.id.laytakeActDoc)
            laytakeActDoc.visibility = View.GONE

            var layoutResolvedate = v.findViewById<TextInputLayout>(R.id.layoutResolvedate)
            layoutResolvedate.visibility = View.GONE

            var radio_Feedback = v.findViewById<RadioButton>(R.id.radio_Feedback)
            var radio_Done = v.findViewById<RadioButton>(R.id.radio_Done)
            var radio_reject = v.findViewById<RadioButton>(R.id.radio_reject)


            dialog.setContentView(v)
            dialog.setCancelable(true)
            dialog.show()


            txtFrResolvedDate.setOnClickListener{
                layoutResolvedate.isErrorEnabled = false
                // layoutSearchT.isErrorEnabled = false
                val cldr = Calendar.getInstance()
                val day = cldr[Calendar.DAY_OF_MONTH]
                val month = cldr[Calendar.MONTH]
                val year = cldr[Calendar.YEAR]
                picker = DatePickerDialog(
                    requireContext(),
                    { view1: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val monthNumber = monthOfYear + 1
                        val selectedDate = "$year1-$monthNumber-$dayOfMonth"

                        txtFrResolvedDate.setText(
                            Utility.changeDateFormat(
                                selectedDate,
                                "yyyy-MM-dd",
                                "dd-MMM-yyyy"
                            )
                        )
                    }, year, month, day
                )
                picker.show()
            }
            radio_Feedback.setOnClickListener {
                laytakeActDoc.visibility = View.GONE
                layoutResolvedate.setVisibility(View.VISIBLE)
                actionTaken = "Feedback"

            }

            radio_Done.setOnClickListener {
                layoutResolvedate.visibility = View.GONE
                laytakeActDoc.setVisibility(View.VISIBLE)
                actionTaken = "Done"
            }

            radio_reject.setOnClickListener {
                layoutResolvedate.visibility = View.GONE
                laytakeActDoc.setVisibility(View.VISIBLE)
                actionTaken = "Reject"

            }
            /*
            UpdateAction (
                user_id:String, rq_trn_no:String, rq_trn_row:int, feedback_date:yyyy-MM-dd,
                feedback_det:String, solution_det:String, action_type:String, doc_ext: String )

             */
            var user_id: String? = Utility.getValueByKey(requireActivity(),"username")
           // rq_trn_no

            var feedback_date = v.txtFrResolvedDate.text.toString()
            var feedback_det = v.txtActionComment.text.toString()
            var solution_det = v.txtActionComment.text.toString()
          //  var doc_ext =


                takeActionCancel.setOnClickListener { dialog.hide() }
            btntakeActSub.setOnClickListener { dialog.hide()



            }

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
                /*
                R.id.radio_Yes ->
                    if (checked) {
                        // Pirates are the best
                    }
                R.id.radio_no ->
                    if (checked) {
                        // Ninjas rule
                    }

                 */
            }
        }
    }



    //todo for get data








    private fun getComplainSolverDataList(
        user_id: String

    ) {
        logViewModel.getComSolverData(user_id)
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            successLogList(res)

                        }

                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                        // rootView.shimmer_att_container.visibility = View.GONE
                        // rootView.shimmer_att_container.stopShimmer()

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }


    private fun successLogList(res: GetActivity4AppResponse) {

        if (res.code == "200") {
            if (res.data.isNotEmpty()) {

                successLogList1(res.data.get(dataReceived))
            } else {


            }
        }
    }


    private fun successLogList1(res: RequestDetails) {
        party_code = res.reqNo.toString()+"_"+(res.follwAct.size-1).toString()
        rq_trn_no = res.reqNo.toString()
        rq_trn_row = (res.follwAct.size-1).toString()

    }



}