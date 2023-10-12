package com.crowncement.crowncement_complain_management.Fragment

import android.app.Activity
import android.app.DatePickerDialog
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.FileUtility
import com.crowncement.crowncement_complain_management.common.ImagePathUtils
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
import java.io.File
import java.io.IOException
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


    lateinit var finalFile: File
    var currentPhotoPath = ""
    var currentPhotoIDPath = ""
    var fileType = ""
    lateinit var attachFile: File
    lateinit var pdfFile: File

    var extension = ""

    lateinit var setActionImage :ImageView

    private val permissionCode = 101
    val REQUEST_CODE = 200

    lateinit var cardFile: ArrayList<File>

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

            EscActionCancel.setOnClickListener { dialog.hide() }
            btnEscActSub.setOnClickListener { dialog.hide() }
        }

        return rootView

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
                            "Failed to capture visiting card",
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
            "Engineer"
        )
        return Uri.parse(path)
    }



}