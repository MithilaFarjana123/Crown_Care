package com.crowncement.crowncement_complain_management.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.FileUtility
import com.crowncement.crowncement_complain_management.common.ImagePathUtils
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.common.Utility.saveCompInfo
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Adapter.EmpInfoAdapter
import com.crowncement.crowncement_complain_management.data.Adapter.NotificationAdapter
import com.crowncement.crowncement_complain_management.data.Adapter.NotificationComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Model.*
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSolverViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainSolverViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.UpdateSeenStatViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.escalate.*
import kotlinx.android.synthetic.main.escalate.view.*
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.frag_details.view.d_userImg
import kotlinx.android.synthetic.main.frag_genarate_complain.view.*
import kotlinx.android.synthetic.main.frag_notification.*
import kotlinx.android.synthetic.main.frag_notification.view.*
import kotlinx.android.synthetic.main.frag_notification_details.view.*
import kotlinx.android.synthetic.main.notification_details.view.*
import kotlinx.android.synthetic.main.take_action.view.*
import kotlinx.android.synthetic.main.take_action.view.imgAddDoc
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Frag_notification : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainSolverViewModel
    lateinit var logUpdateSeenViewModel: UpdateSeenStatViewModel
//    lateinit var emp_id : String

    lateinit var loadingAnim: Dialog


    lateinit var setActionImage :ImageView

    private val permissionCode = 101
    val REQUEST_CODE = 200

    lateinit var cardFile: ArrayList<File>
    var allempolye: java.util.ArrayList<GetEmpName> = java.util.ArrayList()
    lateinit var employeNameitems: java.util.ArrayList<String>

    lateinit var logImgViewModel: ComplainViewModel

    lateinit var departmentList: ArrayList<DepartmentData>
    lateinit var empnameList: ArrayList<GetEmpName>
    lateinit var pdfFile: File


    lateinit var txtDepartment: AutoCompleteTextView
    lateinit var txtEscperson:AutoCompleteTextView
    lateinit var takeAction : CardView
    lateinit var escalate:CardView
  //  lateinit var radio_Feedback:RadioButton
    lateinit var actionTaken : String

    lateinit var user_id : String
    lateinit var rq_trn_no : String
    lateinit var rq_trn_row : String
    lateinit var solution_det : String



    lateinit var id : String
    lateinit var compdata : RequestDetails

    private var selectedDepartment: String = ""
    var emp = " "
    var fileType = ""
    lateinit var attachFile: File
    var extension = ""
    var result = false
    var currentPhotoIDPath = ""





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.frag_notification, container, false)
     //   (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ComplainSolverViewModelFactory()
        setupViewModel()
        cardFile = ArrayList()
        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireContext()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()
        setImg()
        extension= " "
        solution_det = " "
        id = Utility.getValueByKey(requireActivity(),"username").toString()

        //
        if (id != null) {
            getComplainSolverDataList(id)
         //   getComplainSolverDataList("E00-005445")
          //  getComplainSolverDataList("E11-001795")
        }



        return rootView


    }




    fun setImg(){
        var img = Utility.getValueByKey(requireActivity(), "user_img").toString()

        if (img.isNotEmpty()) {
            Glide
                .with(requireActivity())
                .load(Endpoint.IMAGE_BASE_URL + img)
                // .load(Endpoint.IMAGE_BASE_URL + "/da/docs/x880022/" + img)
                .error(R.drawable.human)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                // .placeholder(R.drawable.baseline_no_img)
                //  .transform(RoundedCorners(30,30.0))
                .into(rootView.n_userImg)

        } else {
            rootView.n_userImg.setBackgroundResource(R.drawable.human)
        }
    }




    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainSolverViewModelFactory())
                .get(
                    ComplainSolverViewModel::class.java
                )

        logUpdateSeenViewModel = ViewModelProviders.of(requireActivity(), UpdateSeenStatViewModelFactory())
            .get(
                UpdateSeenStatViewModel::class.java
            )

        logImgViewModel =  ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
            .get(
                ComplainViewModel::class.java
            )



    }


    private fun getComplainSolverDataList(
        user_id: String

    ) {
        logViewModel.getComSolverData(user_id)
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->

                            successLogList(res)
                            if (res.code == "200") {
                                loadingAnim.dismiss()
                            }

                        }

                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        loadingAnim.dismiss()
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

                prepareLogRV(res.data)
            } else {


            }
        }
    }


    private fun prepareLogRV(items: java.util.ArrayList<RequestDetails>) {

        items.sortByDescending {
            it.reqNo
        }

        if (items.size>0){

        val notificationlogAdapter = NotificationAdapter(items)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        notification_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        notification_recyclerView.layoutManager = manager
        notification_recyclerView.adapter = notificationlogAdapter


        notificationlogAdapter.setOnItemClickListener(object :
            NotificationAdapter.OnAdapterItemClickListener {

            override fun CancleItem(v: View?, position: Int) {
                // val adapter = NotificationAdapter(data)


            }

            override fun OnClick(v: View?, position: Int) {
                val userData: RequestDetails = items.get(position)
                // saveCompInfo(userData,requireActivity())

//todo check id
                UpdateSeenStatData(id, items[position].reqNo.toString())
                //   UpdateSeenStatData("E00-005445",items[position].reqNo.toString())
                //   UpdateSeenStatData("E11-001795",items[position].reqNo.toString())


                val v: View = layoutInflater.inflate(R.layout.notification_details, null)
                val dialog = Dialog(v.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                Objects.requireNonNull(dialog.window)
                    ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

                hide(v)
                show(v)

                setImg(v)
                setvalue(v, userData)
                action(v, userData)


                dialog.setContentView(v)
                dialog.setCancelable(true)
                dialog.show()

                var back: ImageView = v.findViewById(R.id.ndback)
                back.setOnClickListener {
                    dialog.dismiss()
                }


            }

        })
    }

    }


    fun hide(view: View){
        view.nt_details.setVisibility(View.GONE)
        view.nt_details_goback.setVisibility(View.GONE)
        // rootView.action_taken.setVisibility(View.GONE)
        //   rootView.action_RecyclerView.setVisibility(View.GONE)
        view.nt_details_more.setOnClickListener {
            view.nt_details.setVisibility(View.VISIBLE)
            view.nt_details_goback.setVisibility(View.VISIBLE)
            view.nt_details_more.setVisibility(View.GONE)

        }
    }



    fun show(view: View){
        // view.hide_details.setVisibility(View.GONE)
        view.nt_details_goback.setOnClickListener {
            view.nt_details_goback.setVisibility(View.GONE)
            view.nt_details.setVisibility(View.GONE)
            view.nt_details_more.setVisibility(View.VISIBLE)

        }

    }




    fun setImg(view: View){
        var img = Utility.getValueByKey(requireActivity(), "user_img").toString()

        if (img.isNotEmpty()) {
            Glide
                .with(requireActivity())
                .load(Endpoint.IMAGE_BASE_URL + img)
                // .load(Endpoint.IMAGE_BASE_URL + "/da/docs/x880022/" + img)
                .error(R.drawable.human)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                // .placeholder(R.drawable.baseline_no_img)
                //  .transform(RoundedCorners(30,30.0))
                .into(view.d_userImg)

        } else {
            view.d_userImg.setBackgroundResource(R.drawable.human)
        }
    }



    fun setvalue(v: View,userData: RequestDetails){
        var data = userData
        var catagory = data.reqCat
        v.nd_toolbar_dtitle.text = catagory+" Details"

        v.nd_reqid.text=data.reqNo
        v.nd_item_title.text = data.reqTitle
        v.nd_sob_date.text=Utility.changeDateFormat(
            data.trnDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
        v.nd_oc_date.text=  "Occurence Date : "+Utility.changeDateFormat(
            data.reqDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )

        v.nd_oc_expResolvedby.text = Utility.changeDateFormat(
            data.expectedResolvDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )

        v.nd_comp_name.text= data.compName.toString()
        v.nd_oc_num.text = data.compMob.toString()
        v.nd_oc_email.text=data.compEmail.toString()
        v.nd_oc_type.text = data.reqType.toString()
        v.nd_oc_details.text = data.reqDet.toString()


        var documentImg = data.reqImg.toString()
        if (documentImg.isNotEmpty()){
            Glide
                .with(requireActivity())
                .load(Endpoint.IMAGE_BASE_URL + documentImg)
                // .load(Endpoint.IMAGE_BASE_URL + "/da/docs/x880022/" + documentImg)
                .error(R.drawable.document)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                // .placeholder(R.drawable.baseline_no_img)
                //  .transform(RoundedCorners(30,30.0))
                .into(v.nd_oc_img)


        }else{
            //  rootView.d_oc_vf_img.setVisibility(View.GONE)
            v.nd_oc_img.setVisibility(View.GONE)
        }


        //todo action taken
        var title = data.reqCat + " Details"
        val Status = data.trnStatus
        v.nd_toolbar_dtitle.text = title.toString()
        var actionTakenList = data.follwAct
        var position = data.follwAct.size-1
        var actStatus = actionTakenList.get(position).actStatus

        val action_taken: TextView = v.findViewById(R.id.action_taken)

        val recyclerView: RecyclerView = v.findViewById(R.id.RVsolution)

        recyclerView.layoutManager = LinearLayoutManager(context)

        if(!Status.equals("Open")){
            val adapter = NotificationComplainSolve_Adapter(actionTakenList)
            recyclerView.adapter = adapter


        }else{
            action_taken.visibility= View.GONE
            recyclerView.visibility = View.GONE
        }



    }




    fun action(v: View,userData: RequestDetails){
        compdata = userData
        takeAction = v.findViewById<CardView>(R.id.nd_takeAction)
        escalate= v.findViewById<CardView>(R.id.nd_escalate)
      //  radio_Feedback = v.findViewById<RadioButton>(R.id.radio_Feedback)

        rq_trn_no = ""
        rq_trn_no =compdata.reqNo.toString()
        var position = compdata.follwAct.size-1
        user_id = ""
        user_id = compdata.follwAct.get(position).repTo.toString()
        rq_trn_row = ""
        rq_trn_row  = compdata.follwAct.get(position).actRow.toString()
        var p = rq_trn_row.toInt()-1
        //  var rq_row : Int = compdata.follwAct.get(position).actRow!!
        var feedback_status : String = compdata.follwAct.get(p).actStatus.toString()
        var esc_status: String = compdata.follwAct.get(position).actStatus.toString()

        var party_code =rq_trn_no+"_"+ rq_trn_row


        if(compdata.trnStatus=="Open"){
            takeAction.setVisibility(View.VISIBLE)
            escalate.setVisibility(View.VISIBLE)
        }else if(compdata.trnStatus=="Pending"){
            takeAction.setVisibility(View.VISIBLE)
            escalate.setVisibility(View.VISIBLE)
        }
        else {
            takeAction.setVisibility(View.GONE)
            escalate.setVisibility(View.GONE)
        }

        if(esc_status=="Forwarded"){
           takeAction.setVisibility(View.GONE)
           escalate.setVisibility(View.GONE)
        }

        takeAction.setOnClickListener {l ->

            val v: View = layoutInflater.inflate(R.layout.take_action, null)
            val dialog = BottomSheetDialog(requireActivity())
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
            var pdfAddDoc = v.findViewById<ImageView>(R.id.pdfAddDoc)
            var imgAddDoc = v.findViewById<ImageView>(R.id.imgAddDoc)

            var addCameraDoc = v.findViewById<ImageView>(R.id.addCameraDoc)

            setActionImage = v.findViewById<ImageView>(R.id.setActionImage)


            if (feedback_status=="Reviewed"){
                radio_Feedback.visibility= View.GONE
            }

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
                                "yyyy-MM-dd"
                            )
                        )
                    }, year, month, day
                )
                picker.show()
            }
            actionTaken=""
            radio_Feedback.setOnClickListener {
                laytakeActDoc.visibility = View.GONE
                layoutResolvedate.setVisibility(View.VISIBLE)
                actionTaken = "feedback"

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


            var feedback_date = v.txtFrResolvedDate.text.toString()
            var feedback_det = v.txtActionComment.text.toString()
            solution_det = v.txtActionComment.text.toString()


            v.pdfAddDoc.setOnClickListener {
                openPDFFile()
            }

            v.imgAddDoc.setOnClickListener {
                openGalleryForImages()
            }
            v.addCameraDoc.setOnClickListener {
                openCameraVisitingCard()
            }


            takeActionCancel.setOnClickListener { dialog.hide() }
            btntakeActSub.setOnClickListener {
                if(actionTaken.isNotEmpty()){
                    // v.actionQ.setTextColor(R.color.black)
                    if((actionTaken=="feedback")&&saveUIValidationFeedback(v).equals(true)){
                        var feedback_date = v.txtFrResolvedDate.text.toString()
                        var feedback_det = v.txtActionComment.text.toString()

                        SaveUpdateActionData(user_id,rq_trn_no,rq_trn_row,feedback_date,
                            feedback_det,"",actionTaken,"")

                        dialog.hide()

                    }


                    /*
                    else if((actionTaken=="done")&&saveUIValidation(v).equals(true)){
                        var act = actionTaken.toString()

                        solution_det=""
                        solution_det = v.txtActionComment.text.toString()
                        if (cardFile.size > 0) {

                            actionImageUpload(
                                party_code,
                                "care",
                                "follwup/done",
                                extension,
                                cardFile,
                                loadingAnim
                            )
                        }

                        else{

                            SaveUpdateActionData(user_id,rq_trn_no,rq_trn_row,"",
                                "",solution_det,actionTaken,extension)


                        }


                        loadingAnim.dismiss()

                        dialog.hide()

                    }

                     */


                    else if(saveUIValidation(v).equals(true)){
                        solution_det=""
                        solution_det = v.txtActionComment.text.toString()
                        if (cardFile.size > 0) {
                            if(actionTaken=="Done"){
                                actionImageUpload(
                                    party_code,
                                    "care",
                                    "follwup/done",
                                    extension,
                                    cardFile,
                                    loadingAnim
                                )
                            }
                            else if((actionTaken=="Reject")){
                                actionImageUpload(
                                    party_code,
                                    "care",
                                    "follwup/reject",
                                    extension,
                                    cardFile,
                                    loadingAnim
                                )
                            }

                        }

                        else{

                            SaveUpdateActionData(user_id,rq_trn_no,rq_trn_row,"",
                                "",solution_det,actionTaken,extension)


                        }


                        loadingAnim.dismiss()

                        dialog.hide()

                    }

                }

                else{

                    Toast.makeText(requireContext(), "Please select your action", Toast.LENGTH_SHORT).show()
                }
                //   v.actionQ.setTextColor(R.color.red)
            }

        }



        escalate.setOnClickListener {l ->
            val v: View = layoutInflater.inflate(R.layout.escalate, null)
            val dialog = BottomSheetDialog(requireContext())

            Objects.requireNonNull(dialog.window)
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            var EscActionCancel = v.findViewById<ImageView>(R.id.EscActionCancel)
            var btnEscActSub = v.findViewById<MaterialButton>(R.id.btnEscActSub)
            txtDepartment = v.findViewById<AutoCompleteTextView>(R.id.txtEscDep)
            txtEscperson = v.findViewById<AutoCompleteTextView>(R.id.txtEscperson)
            getSavedDepartmentList()


            dialog.setContentView(v)
            dialog.setCancelable(true)
            dialog.show()
            // onRadioButtonClicked(v)
            //eventClickListener()
            txtDepartment.setOnItemClickListener { parent, arg1, position, arg3 ->

                if (position > 0) {
                    selectedDepartment = parent.getItemAtPosition(position).toString()
                    if (selectedDepartment.isNotEmpty()) {

                        //   txtmeet.isEnabled = true
                        getsavedEmp(selectedDepartment)

                    }
                    //     loadDepartment1(txtDep)
                    // result=true

                    // getSavedInCategorytList(selectedCategory, selectedDivision)
                } else {
                    //  txtmeet.isEnabled = false

                    selectedDepartment = ""
                }


            }


            txtEscperson.setOnItemClickListener { parent, arg1, position, arg3 ->


                //    val empObj:GetEmpName=(txtmeet.getAdapter().getItem(position).toString(), false)

                val empObj: GetEmpName = (txtEscperson.getAdapter().getItem(position) as GetEmpName)

                txtEscperson.setText(empObj.empName.toString())
                var empolyename = empObj.empName.toString()


                //   emp =getEmpIDByName(parent.getItemAtPosition(position).toString())
                emp = getEmpIDByName(empolyename)

                Log.d("emp select", emp)

            }
            EscActionCancel.setOnClickListener { dialog.hide() }
            btnEscActSub.setOnClickListener {
                if(saveUIEscValidation(v).equals(true)){
                    var esc_remarks = v.txtEscReason.text.toString()

/*
                    Toast.makeText(requireContext(), "Emp : "+emp, Toast.LENGTH_LONG)
                        .show()

 */
                    SaveEscalateActionData(user_id,rq_trn_no,rq_trn_row,emp,esc_remarks)
                    dialog.hide()

                }

            }
        }

    }



    private fun SaveUpdateActionData(
        user_id: String,
        rq_trn_no:String,
        rq_trn_row: String,
        feedback_date:String,
        feedback_det:String,
        solution_det:String,
        action_type:String,
        doc_ext: String

    ) {

        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireActivity()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()


        logUpdateSeenViewModel.SaveUpdateActionData(user_id,rq_trn_no,
            rq_trn_row,feedback_date,feedback_det,
            solution_det,action_type,doc_ext
        )
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            val b = res.code
                            val c = res.message


                            if(res.code == "200"){



                               // successActLogList(res)

                            }
                            loadingAnim.dismiss()

                        }

                    }

                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        loadingAnim.dismiss()

                        // rootView.shimmer_att_container.visibility = View.GONE
                        // rootView.shimmer_att_container.stopShimmer()

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    // loadingAnim.dismiss()

                }
            }
    }


    private fun successActLogList(res: UpdateActionResponce) {

        var rq_trn_no =compdata.reqNo.toString()
        var position = compdata.follwAct.size-1
        var rq_trn_row = compdata.follwAct.get(position).actRow.toString()
        var party_code =rq_trn_no+"_"+ rq_trn_row

        if(res.code == "200"){
            Utility.getBaseMessage(
                requireActivity(),
                "Successful",
                res.message.toString(),
                R.drawable.ic_checked_green,
                1
            )

            //    successLogList(res)

        }

/*
        if (res.code == "200") {

            if (cardFile.size > 0) {

                actionImageUpload(
                    party_code,
                    "care",
                    "follwup/reject",
                    extension,
                    cardFile,
                    loadingAnim
                )
            }else{
                Utility.commonToast(requireActivity(),"Feedback done",res.message.toString(),1)
                loadingAnim.dismiss()
            }


        }

 */




    }


    private fun actionImageUpload(
        party_code: String,
        party_type: String,
        doc_type: String,
        doc_ext:String,
        all_images: ArrayList<File>,
        loading: Dialog
    ) {
        logImgViewModel.actionImageUpload(
            party_code,
            party_type,
            doc_type,
            doc_ext,
            all_images
        )
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            try {
                                if (res.code == "200") {


                                    SaveUpdateActionData(user_id,rq_trn_no,rq_trn_row,"",
                                        "",solution_det,actionTaken,extension)

                                    loading.dismiss()


                                    /*
                                    if (party_code != "") {
                                        Utility.getBaseMessage(
                                            requireActivity(),
                                            "Updated",
                                            "Information has been updated successfully",
                                            R.drawable.ic_checked_green,
                                            1
                                        )
                                    } else {
                                        Utility.getBaseMessage(
                                            requireActivity(),
                                            "Successful",
                                            "Action taken successfully",
                                            R.drawable.ic_checked_green,
                                            1
                                        )
                                    }

                                     */

                                    // val navController =
                                    //     requireActivity().findNavController(R.id.nav_host_fragment)
                                    //  navController.navigate(R.id.nav_dashboard)
                                } else {

                                    loading.dismiss()
                                    Utility.getBaseMessage(
                                        requireActivity(),
                                        "Warning",
                                        res.message.toString(),
                                        R.drawable.error_white,
                                        0
                                    )
                                }

                            } catch (e: Exception) {
                                loading.dismiss()
                                Log.e("Action Document Add", "Error: " + e.message)
                            }

                        }
                    }
                    Status.LOADING -> {

                    }

                    Status.ERROR -> {
                        loading.dismiss()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun SaveEscalateActionData(
        user_id: String,
        rq_trn_no:String,
        rq_trn_row: String,
        esc_to:String,
        esc_remark:String

    ) {

        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireActivity()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()


        logUpdateSeenViewModel.SaveEscActionData(user_id,rq_trn_no,
            rq_trn_row,esc_to,esc_remark
        )
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            val b = res.code
                            val c = res.message

                            var rq_trn_no =compdata.reqNo.toString()
                            var position = compdata.follwAct.size-1
                            var rq_trn_row = compdata.follwAct.get(position).actRow.toString()
                            var party_code =rq_trn_no+"_"+ rq_trn_row

/*
                            if (res.code == "200") {

                                if (cardFile.size > 0) {

                                    actionImageUpload(
                                        party_code,
                                        "care",
                                        "follwup/reject",
                                        extension,
                                        cardFile,
                                        loadingAnim
                                    )
                                }else{
                                    Utility.commonToast(requireActivity(),"Feedback done",res.message.toString(),1)
                                    loadingAnim.dismiss()
                                }


                            }

 */

                            if(res.code == "200"){
                                Utility.getBaseMessage(
                                    requireActivity(),
                                    "Successful",
                                    res.message.toString(),
                                    R.drawable.ic_checked_green,
                                    1
                                )

                                //    successLogList(res)

                            }
                            loadingAnim.dismiss()

                        }

                    }

                    Status.LOADING -> {

                    }
                    Status.ERROR -> {
                        loadingAnim.dismiss()

                        // rootView.shimmer_att_container.visibility = View.GONE
                        // rootView.shimmer_att_container.stopShimmer()

                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    // loadingAnim.dismiss()

                }
            }
    }


    private fun saveUIValidationFeedback(view: View): Boolean {

        result = false

        //department
        removeUIErrorSign(view.layTakeaction)
        removeUIErrorSign(view.layoutResolvedate)


        if (view.txtActionComment.text.toString().isEmpty()||view.txtActionComment.text.toString().equals("")) {
            result = false
            setError(view.layTakeaction, "Please mention your action ")

        }else if(view.txtFrResolvedDate.text.toString().isEmpty()||view.txtFrResolvedDate.text.toString().equals("")){
            result = false
            setError(view.layoutResolvedate, "Select Date")
        }


        else {
            result = true
        }

        return result

    }

    private fun saveUIValidation(view: View): Boolean {

        result = false

        //department
        removeUIErrorSign(view.layTakeaction)


        if (view.txtActionComment.text.toString().isEmpty()||view.txtActionComment.text.toString().equals("")) {
            result = false
            setError(view.layTakeaction, "Please mention your action ")

        }


        else {
            result = true
        }

        return result

    }


    private fun saveUIEscValidation(view: View): Boolean {

        result = false

        //department
        removeUIErrorSign(view.layEscDep)
        removeUIErrorSign(view.layEscperson)
        removeUIErrorSign(view.layEscReason)

        if (view.txtEscDep.text.toString().isEmpty()||view.txtEscDep.text.toString().equals("Select Department")) {
            result = false
            setError(view.layEscDep, "Please provide Department ")
        }
        else if(view.txtEscperson.text.toString().isEmpty()){
            result = false
            setError(view.layEscperson, "Escalated person name is required ")
        }
        else if (view.txtEscReason.text.toString().isEmpty()||view.txtEscReason.text.toString().equals("")) {
            result = false
            setError(view.layEscReason, "Please mention your remarks ")

        }


        else {
            result = true
        }

        return result

    }



    private fun removeUIErrorSign(field: TextInputLayout) {
        field.isErrorEnabled = false
        field.error = null
    }

    private fun setError(field: TextInputLayout, message: String) {

        field.isErrorEnabled = true
        field.error = message
    }




    //get emp id
    fun getEmpIDByName(name: String): String {
        var res = ""
        for (item in allempolye) {
            if (item.empName == name) {
                res = item.empId.toString()
                break
            } else {
                res = ""
            }
        }
        return res
    }



    //Todo for department
    private fun getSavedDepartmentList(

    ) {
        logImgViewModel.getDepartmentData()?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    it.responseData?.let { res ->
                        departmentList = ArrayList()
                        departmentList = res.data
                        loadDepartment1(txtDepartment, res.data)
                        loadingAnim.dismiss()
                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    loadingAnim.dismiss()

                }
            }
        }
    }
    private fun loadDepartment1(ac: AutoCompleteTextView, lists: ArrayList<DepartmentData>) {

        val items: ArrayList<String> = ArrayList()
        items.add("Select Department")
        for (item in lists) {
            items.add(item.Department.toString())
        }

        ac.setText(items[0])
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        ac.setAdapter(adapter)
        adapter.notifyDataSetChanged()

    }


    //Todo load EmpName

    private fun getsavedEmp(
        dept_name: String
    ) {
        logImgViewModel.getEmpData(dept_name)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    it.responseData?.let { res ->

                        empnameList = ArrayList()
                        empnameList = res.data
                        loadEmpName(txtEscperson, res.data)

                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    loadingAnim.dismiss()

                }
            }
        }
    }


    private fun loadEmpName(
        ac: AutoCompleteTextView,
        lists: java.util.ArrayList<GetEmpName>
    ) {
        allempolye = lists

        var empiditems: java.util.ArrayList<String> = java.util.ArrayList()

        employeNameitems = ArrayList()

        for (item in lists) {
            employeNameitems.add(item.empName.toString() + " ( " + item.empDesig.toString() + " )")
            //   empiditems.add(item.empId.toString())
        }


//        val adapter = ArrayAdapter(
//            this, android.R.layout.simple_dropdown_item_1line, employeNameitems
//        )
//        ac.setAdapter(adapter)
//        adapter.notifyDataSetChanged()
        val adapter = EmpInfoAdapter(requireContext(), lists)
        ac.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }


    private fun openCameraVisitingCard() {
        fileType = "CAMERAID"


        val fileName = "photo.jpg"
        val storageDirectory: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        var imageFile: File? = null
        try {
            imageFile = File.createTempFile(fileName, ".jpg", storageDirectory)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        currentPhotoIDPath = imageFile!!.absolutePath
        val imageUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.crowncement.crowncement_complain_management.fileprovider",
            imageFile
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, 130)
    }


    private fun openGalleryForImages() {
        fileType = "ATTACH"

        if (Build.VERSION.SDK_INT < 19) {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE)

        } else { // For latest versions API LEVEL 19+
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun openPDFFile() {
        fileType = "PDF"
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        pdfIntent.putExtra(Intent.EXTRA_MIME_TYPES, ".pdf")
        pdfIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(pdfIntent, 12)
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        cardFile = ArrayList()

        if (resultCode != Activity.RESULT_CANCELED) {
            // Toast.makeText(requireContext(), fileType, Toast.LENGTH_LONG).show()
            when (requestCode) {
//
                //attach

                200 -> {

                    if (data!!.clipData != null) {
                        val count = data.clipData!!.itemCount

                        for (i in 0 until count) {
                            val imageUri: Uri = data.clipData!!.getItemAt(i).uri

                            val path: File =
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            try {
                                path.mkdirs()
                                val file =
                                    File(ImagePathUtils.getRealPath(requireContext(), imageUri)!!)
                                cardFile.add(file)
                                //  finalFile.add()
                                extension = ImagePathUtils.getRealPath(requireContext(), imageUri)
                                    .toString().split(".")[1]
                                Log.e("extension", "onActivityResult: $extension")
                                // val selectedImage =   data.clipData!!.getItemAt(i).uri as Bitmap?
                                setImage(file, setActionImage)
                               // finalFile=file
                                fileType = ""
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                    }


                    else if (data.data != null) {
                        val imagePath: String = data.data!!.path!!
                        val imgUri: Uri = data.data!!


                        val path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        try {

                            val  file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
                                createFileFromImageUri(requireContext(),imgUri)!!
                            } else{
                                path.mkdirs()
                                File(ImagePathUtils.getRealPath(requireContext(), imgUri)!!)
                            }
                            cardFile.add(file)
                           // finalFile=file
                            fileType = ""

//
                            //get file extension
                            // extension = ImagePathUtils.getRealPath(requireContext(), imgUri).toString().split(".").last()
                            extension ="jpg"
                            //Set image to view
                            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imgUri))
                            setActionImage.setImageBitmap(bitmap)

                        }

                        catch (e: Exception) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                        }
                    }

                    else {
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Please select minimum one photo ",
                            R.drawable.error_white,
                            2
                        )
                    }
                }




/*
                200 -> {
                    setActionImage.setBackgroundResource(0)

                    if (fileType == "ATTACH" && data!!.data != null) {
                        val imagePath: String = data.data!!.path!!
                        Log.e("imagePath", imagePath)
                        val imgUri: Uri = data.data!!
                        val path: File =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        try {
                            path.mkdirs()
                            attachFile =
                                File(ImagePathUtils.getRealPath(requireContext(), imgUri)!!)
                            Log.e(
                                "path",
                                "onActivityResult: ${
                                    ImagePathUtils.getRealPath(
                                        requireContext(),
                                        imgUri
                                    )!!
                                }"
                            )
                            extension =
                                ImagePathUtils.getRealPath(requireContext(), imgUri).toString()
                                    .split(".")[1]
                            //   rootView.txtPdfName.text = ""
                            setActionImage.setImageDrawable(null)
                         //   setActionImage.setImageBitmap(null)
                            cardFile.add(attachFile)
                            setImage(attachFile, setActionImage)
                           // setImage(null, setActionImage)
                            fileType = ""


                        } catch (e: Exception) {
                            fileType = ""
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        fileType = ""
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Failed to attach image",
                            R.drawable.error_white,
                            2
                        )
                    }
                }

 */


                //pdf
                12 -> {
                    if (fileType == "PDF") {
                        val uri: Uri = data!!.data!!
                        val uriString = uri.toString()
                        val myFile = FileUtility().from(requireContext().applicationContext, uri)

                        //Get File name
                        var displayName: String? = null
                        if (uriString.startsWith("content://")) {
                            var cursor: Cursor? = null
                            try {
                                cursor =
                                    requireActivity().contentResolver.query(uri, null, null, null, null)
                                if (cursor != null && cursor.moveToFirst()) {
                                    displayName =
                                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                }
                            } finally {
                                cursor!!.close()
                            }
                        } else if (uriString.startsWith("file://")) {
                            displayName = myFile!!.name
                        }
                        pdfFile = myFile!!
                        setActionImage.setImageDrawable(null)
                        extension = "pdf"
                        //rootView.txtPdfName.text = displayName
                        cardFile.add(pdfFile)
                        setActionImage.setBackgroundResource(R.drawable.ic_attached_pdf)
                        setActionImage.scaleType = ImageView.ScaleType.FIT_CENTER
                        fileType = ""
                        //setActionImage.setBackgroundResource(0)


                    } else {
                        fileType = ""
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Failed to attach pdf file",
                            R.drawable.error_white,
                            2
                        )
                    }
                }

                130 -> {
                    setActionImage.setBackgroundResource(0)

                    if (fileType == "CAMERAID") {
                        val selectedImage = BitmapFactory.decodeFile(currentPhotoIDPath)
                        val emp = Utility.getValueByKey(requireActivity(), "username").toString()
                        val imName = emp + Utility.getCurrentDate("yyyyMMdd")
                        val tempUri = getImageUri(requireContext(), selectedImage!!, imName)

                        cardFile.add(File(getRealPathFromURI(tempUri)!!))
                        extension = "jpg"
                        // rootView.txtPdfName.text = ""
                        setActionImage.setImageDrawable(null)
                        setActionImage.setImageBitmap(selectedImage)
                        fileType = ""

                    } else {
                        fileType = ""
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Failed to capture document",
                            R.drawable.error_white,
                            2
                        )
                    }
                }


                else -> {
                    fileType = ""
                    Utility.commonToast(
                        requireActivity(),
                        "Failed",
                        "failed to process image or pdf file",
                        0
                    )
                }
            }
        }
    }



    //added new
    fun createFileFromImageUri(context: Context, imageUri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = createTempImageFile(context)

        return try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun createTempImageFile(context: Context): File {
        val tempFileName = "temp_image.jpg" // Replace with your desired file name and extension
        val storageDir = context.cacheDir // Use cache directory or external storage depending on your needs
        return File.createTempFile(tempFileName, null, storageDir)
    }

// new end

    private fun setImage(imgFile: File, v: ImageView) {
        v.setImageBitmap(null)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            v.setImageBitmap(myBitmap)
        }
    }

    fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)!!
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap, imageName: String): Uri? {

        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            // inImage,
            getResizedBitmap(inImage, 800),
            imageName,
            "Action"
        )
        return Uri.parse(path)
    }






    ///


    //Todo UpdateSeenStat
    private fun UpdateSeenStatData(
        user_id: String,
        rq_trn_no: String

    ) {
        logUpdateSeenViewModel.getUpdateSeenStatData(user_id,rq_trn_no)
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->

                            successLog2List(res)

                        }

                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {


                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }


    private fun successLog2List(res: UpdateSeenStatResponce) {

        if (res.code == "200") {


        }
    }

/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press by navigating back to the dashboard fragment
                findNavController().navigate(R.id.frag_dashboard) // Replace with your destination's ID
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

 */





}