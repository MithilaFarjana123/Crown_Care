package com.crowncement.crowncement_complain_management.Fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.crowncement.crowncement_complain_management.Activity.MainActivity

import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.ImagePathUtils
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.DepartmentData
import com.crowncement.crowncement_complain_management.data.Model.InCategory
import com.crowncement.crowncement_complain_management.data.Model.Title
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSaveViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainSaveViewModelfactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.frag_genarate_complain.*
import kotlinx.android.synthetic.main.frag_genarate_complain.view.*


import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Frag_genarate_complain : Fragment() {

    lateinit var rootView: View
    lateinit var finalFile: File
    lateinit var datetext : TextInputEditText
    lateinit var Expdatetext : TextInputEditText
    lateinit var clickimg : ImageView
    lateinit var imgAddGalary: ImageView
    lateinit var imgDocument : ImageView

    var result = false
    lateinit var txtcomtilte : TextInputEditText
    lateinit var txtpriority : AutoCompleteTextView
    lateinit var txtDepartment: AutoCompleteTextView
    lateinit var txtcat : AutoCompleteTextView
    lateinit var txtComCat : AutoCompleteTextView



    lateinit var logViewModel: ComplainViewModel
    lateinit var logsaveNewComModel:ComplainSaveViewModel
    lateinit var loadingAnim: Dialog

    lateinit var priorityList: ArrayList<String>
    lateinit var categoryList: ArrayList<String>
    lateinit var departmentList: ArrayList<DepartmentData>
    lateinit var InCategoryList: ArrayList<InCategory>
    lateinit var TitleList:ArrayList<Title>
    lateinit var cardFile: ArrayList<File>


    private var selectedDepartment: String = ""
    private var selectedCategory: String = ""
    private var selectedTitle: String = ""
    var fileType = ""
    var currentPhotoPath = ""
    var InCat=""
    var extension = ""
    var currentPhotoIDPath = ""

    lateinit var attachFile: File

    private val permissionCode = 101
    val REQUEST_CODE = 200
    var firstTime = 0

    var currentInquiryDate = ""
   // var user_number = ""
 //   var user_name = ""
   // var user_email = ""
    var inputfor = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.frag_genarate_complain, container, false)

        finalFile = File("")
        cardFile = ArrayList()
        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireActivity()),
            "P l e a s e    w a i t"
        )
      //  loadingAnim.show()
        var user_number = Utility.getValueByKey(requireActivity(),"user_number").toString()
        var user_name = Utility.getValueByKey(requireActivity(),"user_name").toString()
        var user_email = Utility.getValueByKey(requireActivity(),"user_email").toString()
        radioSelect(user_number,user_name,user_email)

        ComplainViewModelFactory()
        ComplainSaveViewModelfactory()
        setupViewModel()
        initiate()

        var UserDepartment = Utility.getValueByKey(requireActivity(),"user_dept").toString()
        var gc_userdepartment = rootView.findViewById<AutoCompleteTextView>(R.id.gc_userdepartment)
        gc_userdepartment.setText(UserDepartment)
        purposeCategory(txtpriority)
       // clickimage()
        getSavedDepartmentList()
       //  loadDepartment1(txtDepartment)
        onClickEventListener()
        date()


        return rootView
    }

    fun radioSelect(user_number:String,user_name:String,user_email:String){
        var self = rootView.findViewById<RadioButton>(R.id.self)
        var other = rootView.findViewById<RadioButton>(R.id.Other)
        self.setOnClickListener {
            inputfor = "Self"
            rootView.txtcomplainername.setText(user_name)
            rootView.txtnum.setText(user_number)
            rootView.txtEmail.setText(user_email)
        }
        other.setOnClickListener {
            inputfor = "Other"
        }


    }



    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
                .get(
                    ComplainViewModel::class.java
                )

        logsaveNewComModel=ViewModelProviders.of(requireActivity(), ComplainSaveViewModelfactory())
            .get(
                ComplainSaveViewModel::class.java
            )
    }




//Todo datepicker
    fun date() {
    var picker: DatePickerDialog


    datetext.setOnClickListener {
        //    layoutProbDate.isErrorEnabled = false
        //  layoutProbDate.isErrorEnabled = false
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]
        picker = DatePickerDialog(
            requireActivity(),
            { view1: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                val monthNumber = monthOfYear + 1
                val selectedDate = "$year1-$monthNumber-$dayOfMonth"

                datetext.setText(
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


    Expdatetext.setOnClickListener {
        //    layoutProbDate.isErrorEnabled = false
        //  layoutProbDate.isErrorEnabled = false
        val cldr = Calendar.getInstance()
        val day = cldr[Calendar.DAY_OF_MONTH]
        val month = cldr[Calendar.MONTH]
        val year = cldr[Calendar.YEAR]
        picker = DatePickerDialog(
            requireActivity(),
            { view1: DatePicker?, year1: Int, monthOfYear: Int, dayOfMonth: Int ->
                val monthNumber = monthOfYear + 1
                val selectedDate = "$year1-$monthNumber-$dayOfMonth"

                Expdatetext.setText(
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

}

    //Todo initiate
     fun initiate(){
        datetext = rootView.findViewById<TextInputEditText>(R.id.txtComdate)
        Expdatetext = rootView.findViewById<TextInputEditText>(R.id.txtExpdate)
        txtcomtilte=rootView.findViewById<TextInputEditText>(R.id.txtcomtilte)
        txtDepartment = rootView.findViewById<AutoCompleteTextView>(R.id.txtDep)
        txtpriority = rootView.findViewById<AutoCompleteTextView>(R.id.txtpriority)
        txtcat = rootView.findViewById<AutoCompleteTextView>(R.id.txtcat)
        txtComCat = rootView.findViewById<AutoCompleteTextView>(R.id.txtComCat)
        clickimg = rootView.findViewById<ImageView>(R.id.imgAddDoc)
        imgAddGalary=rootView.findViewById<ImageView>(R.id.imgAddGalary)
        imgDocument =rootView.findViewById<ImageView>(R.id.imgDocument)
    }


    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }





    ///doc camera
    private fun openCameraVisitingCard() {
        fileType = "CAMERAID"


        val fileName = "photo.png"
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

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(

                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissionCode
        )
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //images = ArrayList()
        if (resultCode != Activity.RESULT_CANCELED) {
            // Toast.makeText(requireContext(), fileType, Toast.LENGTH_LONG).show()
            when (requestCode) {
                //camera profile picture
                /*
                0 -> {
                    if (fileType == "CAMERA") {
                        val selectedImage = BitmapFactory.decodeFile(currentPhotoPath)

                        val imName = "" + Utility.getCurrentDate("yyyyMMdd")
                        val tempUri = getImageUri(requireContext(), selectedImage!!, imName)
                        finalFile = File(getRealPathFromURI(tempUri)!!)
                        imgDocument.setImageBitmap(selectedImage)
                        fileType = ""
                    } else {
                        fileType = ""
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Failed to capture image",
                            R.drawable.error_white,
                            2
                        )
                    }
                }

                 */

                //camera visiting card
                130 -> {
                    if (fileType == "CAMERAID") {
                        val selectedImage = BitmapFactory.decodeFile(currentPhotoIDPath)
                        val emp = Utility.getValueByKey(requireActivity(), "username").toString()
                        val imName = emp + Utility.getCurrentDate("yyyyMMdd")
                        val tempUri = getImageUri(requireContext(), selectedImage!!, imName)

                        cardFile.add(File(getRealPathFromURI(tempUri)!!))
                        extension = "png"
                        rootView.txtDocName.text = ""
                        rootView.imgDocument.setImageDrawable(null)
                        rootView.imgDocument.setImageBitmap(selectedImage)
                        finalFile=File(getRealPathFromURI(tempUri)!!)
                        fileType = ""

                    } else {
                        fileType = ""
                        Utility.getBaseMessage(
                            requireActivity(),
                            "Failed",
                            "Failed to capture visiting card",
                            R.drawable.error_white,
                            2
                        )
                    }
                }

            //attach from galary
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
                                setImage(file, rootView.imgDocument)
                                finalFile=file
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
                            finalFile=file
                            fileType = ""

//
                            //get file extension
                            // extension = ImagePathUtils.getRealPath(requireContext(), imgUri).toString().split(".").last()
                            extension ="jpg"
                            //Set image to view
                            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imgUri))
                            rootView.imgDocument.setImageBitmap(bitmap)

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
                            rootView.txtDocName.text = ""
                            rootView.imgDocument.setImageDrawable(null)
                            cardFile.add(attachFile)
                            setImage(attachFile, rootView.imgDocument)
                            finalFile=attachFile
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

    fun getImageUri(inContext: Context, inImage: Bitmap, imageName: String): Uri? {

        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            // inImage,
            getResizedBitmap(inImage, 800),
            imageName,
            "Visitor"

        )

        return Uri.parse(path)
    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
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


    //TODO Spinner priority
    private fun purposeCategory(ac: AutoCompleteTextView) {

        priorityList = ArrayList()
        priorityList.add("Select Your Priority")
        priorityList.add("High")
        priorityList.add("Medium")
        priorityList.add("Low")

        ac.setText(priorityList[0])

        for (item in priorityList) {
            val pos = priorityList.indexOf(item)
            ac.setText(priorityList[pos])
            // break
        }
        ac.setText(priorityList[0])
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorityList)
        ac.setAdapter(adapter)
        adapter.notifyDataSetChanged()

    }





    //Todo spiner category
    private fun category(ac: AutoCompleteTextView) {

        categoryList = ArrayList()
        categoryList.add("Select Your Category")
        categoryList.add("Inquiry")
        categoryList.add("Incident")

        ac.setText(categoryList[0])

        for (item in categoryList) {
            val pos = categoryList.indexOf(item)
            ac.setText(categoryList[pos])
            // break
        }
        ac.setText(categoryList[0])
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryList)
        ac.setAdapter(adapter)
        adapter.notifyDataSetChanged()

    }


    //Todo for department
    private fun getSavedDepartmentList(

    ) {
        logViewModel.getDepartmentData()?.observe(requireActivity()) {
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
      //  private fun loadDepartment1(ac: AutoCompleteTextView) {

        val items: ArrayList<String> = ArrayList()
        items.add("Select Department")
      //  var UserDepartment = Utility.getValueByKey(requireActivity(),"user_dept").toString()
     //   items.add(UserDepartment)
      //  selectedDepartment = UserDepartment

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


    private fun onClickEventListener() {
       // layoutComdate.visibility = View.GONE
        category(txtcat)
       // selectedDepartment = rootView.txtde.text.toString()


        rootView.txtcat.setOnItemClickListener { parent, arg1, position, arg3 ->
            if (position > 0) {

                selectedCategory = parent.getItemAtPosition(position).toString()
                if(selectedCategory.equals("Inquiry")){
                    layoutComdate.visibility = View.GONE
                }else if(selectedCategory.equals("Incident")){
                    layoutComdate.visibility = View.VISIBLE
                }
//                if(selectedDepartment.isNotEmpty()&&selectedCategory.isNotEmpty()){
//                        getSavedInCategorytList(selectedCategory, selectedDepartment)
//                    }
            } else {
                selectedCategory = ""
            }
        }

        rootView.txtDep.setOnItemClickListener { parent, arg1, position, arg3 ->
            if (position > 0) {

                selectedDepartment = parent.getItemAtPosition(position).toString()
               // category(txtcat)
               // result=true
                if(selectedDepartment.isNotEmpty()&&selectedCategory.isNotEmpty()){
                    getSavedInCategorytList(selectedCategory, selectedDepartment)
                }

             //   getSavedInCategorytList(selectedCategory, selectedDepartment)
            } else {
                selectedDepartment = ""
            }

        }


        rootView.txtComCat.setOnItemClickListener { parent, arg1, position, arg3 ->
            if (position > 0) {

                selectedTitle = parent.getItemAtPosition(position).toString()
                if(selectedDepartment.isNotEmpty()&&selectedCategory.isNotEmpty()
                    &&selectedTitle.isNotEmpty()){
                  //  getSavedTitleList(selectedCategory, selectedDepartment,selectedTitle)
                }
            } else {
                selectedTitle = ""
            }
        }

        clickimg.setOnClickListener{
          //  requestPermissions()
           // checkPermissions()

           // openCamera()
            openCameraVisitingCard()
        }

        imgAddGalary.setOnClickListener {
            openGalleryForImages()
        }

        //save
        rootView.btnAddNew.setOnClickListener {
          //  val user_id="E00-005445"
            var user_id = Utility.getValueByKey(requireActivity(),"username")

            val dept_code=txtDep.text.toString()
            val req_cat=txtcat.text.toString()
            val req_type=txtComCat.text.toString()
            val req_title=txtcomtilte.text.toString()
            val req_det=txtcomsum.text.toString()

            var occ_date = rootView.txtComdate.text.toString()
            var comp_mob=rootView.txtnum.text.toString()
            var comp_email=rootView.txtEmail.text.toString()
            var req_prior=rootView.txtpriority.text.toString()
            var comp_name = rootView.txtcomplainername.text.toString()
            var exp_solve_date = rootView.txtExpdate.text.toString()



           // saveUIValidation()

            val dialog = BottomSheetDialog(requireContext())
            Objects.requireNonNull(dialog.window)//**
                ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            if(saveUIValidation().equals(true)) {

                if(occ_date.equals("")){
                    occ_date = "2999-12-31"
                }else{

                    val dt = Utility.changeDateFormat(
                        txtComdate.text.toString(),
                        "dd-MMM-yyyy",
                        "yyyy-MM-dd"

                    )
                    occ_date= dt.toString()
                }

                if(exp_solve_date.equals("")){
                    exp_solve_date = "2999-12-31"
                }else{

                    val dcxp = Utility.changeDateFormat(
                        txtExpdate.text.toString(),
                        "dd-MMM-yyyy",
                        "yyyy-MM-dd"

                    )
                    exp_solve_date= dcxp.toString()
                }


                if(cardFile.size > 0){
                    if(selectedCategory.equals("Inquiry")){
                        //need to add currentdate
                        val cldr = Calendar.getInstance()
                        val day = cldr[Calendar.DAY_OF_MONTH]
                        val month = cldr[Calendar.MONTH]
                        val year = cldr[Calendar.YEAR]
                        val inquirydate = "$year-$month-$day"

                        saveNewComplainImg(
                            user_id.toString(), dept_code, req_cat, req_type,
                            req_title, req_det, inquirydate, comp_mob, comp_email, req_prior,"png"
                            ,finalFile,comp_name,exp_solve_date,dialog)

                    }else if(selectedCategory.equals("Incident")){

                        saveNewComplainImg(
                        user_id.toString(), dept_code, req_cat, req_type,
                        req_title, req_det, occ_date, comp_mob, comp_email, req_prior,"png"
                                ,finalFile,comp_name,exp_solve_date,dialog)}
                }else {

                    if(selectedCategory.equals("Inquiry")){
                        //need to add currentdate
                        val cldr = Calendar.getInstance()
                        val day = cldr[Calendar.DAY_OF_MONTH]
                        val month = cldr[Calendar.MONTH]
                        val year = cldr[Calendar.YEAR]
                        val inquirydate = "$year-$month-$day"
                        saveNewComplain(
                            user_id.toString(), dept_code, req_cat, req_type,
                            req_title, req_det, inquirydate, comp_mob, comp_email, req_prior, comp_name,
                            exp_solve_date, dialog
                        )


                }else if(selectedCategory.equals("Incident")){
                    saveNewComplain(
                        user_id.toString(), dept_code, req_cat, req_type,
                        req_title, req_det, occ_date, comp_mob, comp_email, req_prior, comp_name,
                        exp_solve_date, dialog
                    )
                }

                }

            }
            else{
                saveUIValidation()
            }



        }

    }

//todo department End

//Todo Incident/Inquiry Category

    private fun getSavedInCategorytList(
        doc_cat:String,dept:String
    ) {
        logViewModel.getInCategoryData(doc_cat,dept)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    it.responseData?.let { res ->
                        InCategoryList = ArrayList()
                        InCategoryList = res.data
                        loadInCategory(txtComCat, res.data)
                        loadingAnim.dismiss()
                        //  successSavedList(res)
                    }


                }
                Status.LOADING -> {


                }
                Status.ERROR -> {
                    loadingAnim.dismiss()
                    //  Toast.makeText(this.applicationContext, "New Visitor", Toast.LENGTH_LONG)
                    //    .show()

                }
            }
        }
    }


    private fun loadInCategory(ac: AutoCompleteTextView, lists: ArrayList<InCategory>) {
        loadingAnim.dismiss()
        val items: ArrayList<String> = ArrayList()
        items.add("Select your category")
        for (item in lists) {
            items.add(item.requestType.toString())
        }

        ac.setText(items[0])
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        ac.setAdapter(adapter)
        adapter.notifyDataSetChanged()
       // categoryname = ac.text.toString()
       // getSavedInCategorytList(categoryname,departmentName)
    }


//Todo Incident/Inquiry Category end


//Todo title

    private fun getSavedTitleList(
        doc_cat:String,dept:String,trn_parent:String
    ) {
        logViewModel.getTitleData(doc_cat,dept,trn_parent)?.observe(requireActivity()) {
            when (it.status) {
                Status.SUCCESS -> {

                    it.responseData?.let { res ->
                        TitleList = ArrayList()
                        TitleList = res.data
                      //  loadtitle(txtcomtilte, res.data)
                        loadingAnim.dismiss()
                        //  successSavedList(res)
                    }

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    loadingAnim.dismiss()
                    //  Toast.makeText(this.applicationContext, "New Visitor", Toast.LENGTH_LONG)
                    //    .show()

                }
            }
        }
    }

    private fun loadtitle(ac: AutoCompleteTextView, lists: ArrayList<Title>) {

        val items: ArrayList<String> = ArrayList()
        items.add("Select your Title")
        for (item in lists) {
            items.add(item.requestTitle.toString())
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

//  Todo title end



    
//Todo validation start
    private fun saveUIValidation(): Boolean {

        result = false

        //department
        removeUIErrorSign(rootView.layoutComDep)
        //category
        removeUIErrorSign(rootView.layoutComtype)
        //inquery
        removeUIErrorSign(rootView.layoutComCat)
        //title
        removeUIErrorSign(rootView.layoutComtitle)
        //Details
        removeUIErrorSign(rootView.layoutComSum)
        //Date
        removeUIErrorSign(rootView.layoutComdate)
        //Number
        removeUIErrorSign(rootView.layoutComNum)
        //Email
        removeUIErrorSign(rootView.layoutComEmail)
        //Priority
        removeUIErrorSign(rootView.layoutComPriority)
        //complainers name
        removeUIErrorSign(rootView.layoutComName)
        //expected resolve date
        removeUIErrorSign(rootView.layoutExpdate)

        if (txtDep.text.toString().isEmpty()||txtDep.text.toString().equals("Select Department")) {
            result = false
            setError(rootView.layoutComDep, "Please provide Department ")

        }
        else if (txtcat.text.toString().isEmpty()||txtcat.text.toString().equals("Select Your Category")) {
            result = false
            setError(rootView.layoutComtype, "Provide provide category")
        } else if (txtComCat.text.toString().isEmpty()||txtComCat.text.toString().equals("Select your category")) {
            result = false
            setError(rootView.layoutComCat, "Provide provide Inquiry/Incident Category")
//start
        } else if (txtcomtilte.text.toString().isEmpty()||txtcomtilte.text.toString().equals("Select your Title")) {
            result = false
            setError(rootView.layoutComtitle, "Your title id is required")
        }else if (txtcomsum.text.toString().isEmpty()) {
            result = false
            setError(rootView.layoutComSum, "Your Details id is required")
        }else if (txtcomplainername.text.toString().isEmpty()) {
            result = false
            setError(rootView.layoutComName, "Complainers name is required")
        }
        /*
        else if (txtComdate.text.toString().isEmpty()) {
            result = false
            setError(layoutComdate, "Date id is required")
        }

         */

        else if (txtnum.text.toString()
                .isEmpty() || txtnum.text.toString().length != 11){
            result = false
            setError(rootView.layoutComNum, "Invalid mobile number")
        }else if (txtEmail.text.toString().isEmpty()) {
            result = false
            setError(rootView.layoutComEmail, "Email address id is required")
        }else if (txtpriority.text.toString().isEmpty()||txtpriority.text.toString().equals("Select Your Priority")) {
            result = false
            setError(rootView.layoutComPriority, "Set your priority please")
        }

        /*       else if (!finalFile.isFile) {
                   result = false
                   Utility.getBaseMessage(
                       this,
                       "Visitor's image is required",
                       "Please capture visitor's image",
                       R.drawable.error_white,
                       0
                   )
             }

         */
        else if (txtExpdate.text.toString().isEmpty()) {
            result = false
            setError(layoutExpdate, "Expected Resolve Date is required")
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
//Todo validation End


    //Todo Save new complain

    private fun saveNewComplain(user_id:String,dept_code:String, req_cat:String,req_type:String,
                                req_title:String,req_det:String, occ_date:String,comp_mob:String,
                                comp_email:String,req_prior:String,comp_name:String,
                                exp_solve_date:String,dialog: Dialog) {
        dialog.dismiss()
        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireContext()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()

        logsaveNewComModel.getNewComplainData(user_id,dept_code,req_cat,req_type,
            req_title,req_det,occ_date,comp_mob,comp_email,req_prior,comp_name,exp_solve_date

        )?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {

                    it.responseData?.let { res ->

                        if (res.code == "200") {
                            /*
                            Utility.getBaseMessage(
                                requireActivity(),
                                "Successful",
                                "Request submitted successfully",
                                R.drawable.ic_checked_green,
                                1
                            )

                             */
                            Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                            loadingAnim.dismiss()
                            Utility.sendActivity(requireActivity(),"MainActivity")

                          //  val intent = Intent(requireContext(), Frag_history::class.java)
                           // startActivity(intent)

                            }


                         else {
                            loadingAnim.dismiss()

                            Utility.getBaseMessage(
                                requireActivity(),
                                "Error",
                                res.message.toString(),
                                R.drawable.error_white,
                                0
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    loadingAnim.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    //Todo Save new complain end


    //Todo Save new complain with Img
    private fun saveNewComplainImg(
        user_id:String,
        dept_code:String,
        req_cat:String,
        req_type:String,
        req_title:String,
        req_det:String,
        occ_date:String,
        comp_mob:String,
        comp_email:String,
        req_prior:String,
        doc_ext: String,
        all_images: File,
        comp_name: String,
        exp_solve_date:String,
        dialog: Dialog) {
        dialog.dismiss()
        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireContext()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()

        logsaveNewComModel.getNewComplainImgData(user_id,dept_code,req_cat,req_type,
            req_title,req_det,occ_date,comp_mob,comp_email,req_prior,doc_ext,
            all_images,comp_name,exp_solve_date

        )?.observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {

                }
                Status.SUCCESS -> {

                    it.responseData?.let { res ->

                        if (res.code == "200") {

                            Toast.makeText(requireContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show()


                            Utility.sendActivity(requireActivity(),"MainActivity")
                            loadingAnim.dismiss()


                        }

                        //   loadingAnim.dismiss()
                        else {
                            loadingAnim.dismiss()

                            Utility.getBaseMessage(
                                requireActivity(),
                                "Error",
                                res.message.toString(),
                                R.drawable.error_white,
                                0
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    loadingAnim.dismiss()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }





}







