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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.FileUtility
import com.crowncement.crowncement_complain_management.common.ImagePathUtils
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Adapter.EmpInfoAdapter
import com.crowncement.crowncement_complain_management.data.Model.*
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.UpdateSeenStatViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.escalate.*
import kotlinx.android.synthetic.main.escalate.view.*
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.frag_genarate_complain.*
import kotlinx.android.synthetic.main.frag_genarate_complain.view.*
import kotlinx.android.synthetic.main.frag_notification_details.view.*
import kotlinx.android.synthetic.main.take_action.*
import kotlinx.android.synthetic.main.take_action.view.*
import kotlinx.android.synthetic.main.take_action.view.imgAddDoc
import java.io.File
import java.io.IOException
import java.util.*

class Frag_notificationDetails : Fragment() {

    lateinit var rootView: View
    lateinit var loadingAnim: Dialog

    lateinit var logViewModel: UpdateSeenStatViewModel

    lateinit var logImgViewModel: ComplainViewModel

    lateinit var departmentList: ArrayList<DepartmentData>
    lateinit var empnameList: ArrayList<GetEmpName>


    lateinit var txtDepartment: AutoCompleteTextView
    lateinit var txtEscperson:AutoCompleteTextView
    lateinit var takeAction : TextView
    lateinit var escalate:TextView
    lateinit var actionTaken : String
    lateinit var rq_trn_no : String
    lateinit var rq_trn_row: String
    lateinit var party_code :String
    lateinit var id : String
    lateinit var compdata : RequestDetails

    private var selectedDepartment: String = ""


    lateinit var finalFile: File
    var currentPhotoPath = ""
    var currentPhotoIDPath = ""
    var fileType = ""
    var emp = " "
    lateinit var attachFile: File
    lateinit var pdfFile: File

    var result = false

    var extension = ""

    lateinit var setActionImage :ImageView

    private val permissionCode = 101
    val REQUEST_CODE = 200

    lateinit var cardFile: ArrayList<File>
    var allempolye: java.util.ArrayList<GetEmpName> = java.util.ArrayList()
    lateinit var employeNameitems: java.util.ArrayList<String>

    var dataReceived=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        rootView = inflater.inflate(R.layout.frag_notification_details, container, false)

        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireActivity()),
            "P l e a s e    w a i t"
        )
        initiate()
        setupViewModel()
        cardFile = ArrayList()
        dataReceived = arguments?.getInt("position")!!
        id = Utility.getValueByKey(requireActivity(),"username").toString()
        compdata = Utility.getsaveCompInfo(requireActivity())!!
        var title = compdata.reqCat + " Details"
        rootView.toolbar_title.text = title.toString()
        action()

        return rootView

    }


     fun action(){


        var rq_trn_no =compdata.reqNo.toString()
        var position = compdata.follwAct.size-1
         var user_id = compdata.follwAct.get(position).repTo.toString()
      //  var rq_trn_row : Int = compdata.follwAct.get(position).actRow!!
           var rq_trn_row : String = compdata.follwAct.get(position).actRow.toString()


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
            var solution_det = v.txtActionComment.text.toString()


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

/*
                        SaveUpdateActionData("E00-005445","23091100016","1","2023-09-01",
                        "test purpose","","feedback","jpg")

 */
                    }else if( saveUIValidation(v).equals(true)){
                        var solution_det = v.txtActionComment.text.toString()

                        SaveUpdateActionData(user_id,rq_trn_no,rq_trn_row,"",
                            "",solution_det,actionTaken,extension)
                        dialog.hide()

                    }

                }else{

                    Toast.makeText(requireContext(), "Please select your action", Toast.LENGTH_SHORT).show()                }
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


                // emp =parent.getItemAtPosition(position).
            }
            EscActionCancel.setOnClickListener { dialog.hide() }
            btnEscActSub.setOnClickListener {
                if(saveUIEscValidation(v).equals(true)){
                    var esc_remarks = v.txtEscReason.text.toString()


                    Toast.makeText(requireContext(), "Emp : "+emp, Toast.LENGTH_LONG)
                        .show()
                    SaveEscalateActionData(user_id,rq_trn_no,rq_trn_row,emp,esc_remarks)
                    dialog.hide()

                }

            }
        }

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


    fun eventClickListener() {
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


            // emp =getEmpIDByName(parent.getItemAtPosition(position).toString())
           // emp = getEmpIDByName(txtmeet.text.toString())
          //  Log.d("emp select", emp)


            // emp =parent.getItemAtPosition(position).
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







    fun initiate(){

        takeAction = rootView!!.findViewById<TextView>(R.id.takeAction)
        escalate= rootView!!.findViewById<TextView>(R.id.escalate)
    }

    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), UpdateSeenStatViewModelFactory())
                .get(
                    UpdateSeenStatViewModel::class.java
                )

        logImgViewModel =  ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
            .get(
                ComplainViewModel::class.java
            )

    }


//todo UpdateActionData

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


        logViewModel.SaveUpdateActionData(user_id,rq_trn_no,
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
                                successLogList(res)

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



    private fun successLogList(res: UpdateActionResponce) {

        var rq_trn_no =compdata.reqNo.toString()
        var position = compdata.follwAct.size-1
        var rq_trn_row = compdata.follwAct.get(position).actRow.toString()
        var party_code =rq_trn_no+"_"+ rq_trn_row

       /* Toast.makeText(requireContext(), "Feedback done", Toast.LENGTH_LONG)
            .show()

        */

     //   Utility.commonToast(requireActivity(),"Feedback done",res.message.toString(),1)





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




    }


    //todo Escalate

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


        logViewModel.SaveEscActionData(user_id,rq_trn_no,
            rq_trn_row,esc_to,esc_remark
        )
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            val b = res.code
                            val c = res.message

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
                            cardFile.add(attachFile)
                            setImage(attachFile, setActionImage)
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



    //todo img upload
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
                                    loading.dismiss()
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



}


