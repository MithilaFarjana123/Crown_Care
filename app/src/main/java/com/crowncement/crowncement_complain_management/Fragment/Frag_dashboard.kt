package com.crowncement.crowncement_complain_management.Fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainAdapter
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Adapter.Dasgbord_OngoingAdapter
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.data.Model.GetUserReqSumData
import com.crowncement.crowncement_complain_management.data.Model.GetUserReqSumResponce
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.android.synthetic.main.frag_dashboard.*
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.frag_history.*
import java.util.*
import kotlin.collections.ArrayList


class Frag_dashboard : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    lateinit var listForOpen : ArrayList<GetComplainData>
    lateinit var hiscompdata : GetComplainData
    lateinit var loadingAnim: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.frag_dashboard, container, false)
        ComplainViewModelFactory()
        setupViewModel()
        setValue()
        var user_id: String? = Utility.getValueByKey(requireActivity(),"username")

        if (user_id != null) {
            getComplainSummary(user_id)
        }
        // Inflate the layout for this fragment
        eventClickListener()
        return rootView

    }



    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
                .get(
                    ComplainViewModel::class.java
                )

    }




    fun eventClickListener(){
        rootView.btn_logout.setOnClickListener {
            logoutBottomSheet()
        }
    }




    fun setValue(){
        var user_id: String? = Utility.getValueByKey(requireActivity(),"username")
        var user_name: String? = Utility.getValueByKey(requireActivity(),"user_name")
        var user_desig: String? = Utility.getValueByKey(requireActivity(),"user_desig")
        var user_location: String? = Utility.getValueByKey(requireActivity(),"user_location")
        var user_dept: String? = Utility.getValueByKey(requireActivity(),"user_dept")
        rootView.txt_userName.setText(user_name)
        rootView.txt_desg_emp.setText(user_desig+" - "+user_id)

        rootView.txt_userDep.setText("Department : "+user_dept)

        rootView.txt_ofcAddress.setText("Job Location : "+user_location)
        setImg()
        if (user_id != null) {
            getComplainList(user_id)
        }

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
                .into(rootView.userImg)

        } else {
            rootView.userImg.setBackgroundResource(R.drawable.human)
        }
    }



    private fun logoutBottomSheet() {
        val v: View = layoutInflater.inflate(R.layout.confirm_logout, null)
        val dialog = BottomSheetDialog(requireContext())
        Objects.requireNonNull(dialog.window)
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val btnYes: Button = v.findViewById(R.id.btnYes)
        val btnNo: Button = v.findViewById(R.id.btnNo)
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.show()
        btnNo.setOnClickListener {

            dialog.hide()
        }
        btnYes.setOnClickListener {
            // super.onBackPressed()


            btnYes.isEnabled = false
            Utility.removePreference(requireContext(), "username")
            Utility.removePreference(requireContext(), "pass")
            Utility.removePreference(requireContext(), "UserInfo")
            requireActivity().finish()

        }


    }



    private fun getComplainList(
        user_id: String
        //  curr_yr: String,
        // curr_mon: String

    ) {
        logViewModel.getSavedcomplain(user_id)
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

    private fun successLogList(res: GetComplainResponse) {

        if (res.code == "200") {
            if (res.data.isNotEmpty()) {

                prepareLogRV(res.data)

            } else {


            }
        }
    }


    private fun prepareLogRV(items: java.util.ArrayList<GetComplainData>) {
        listForOpen = ArrayList()


        for (values in items){
            //  if(values.visitCard.toString().isNotEmpty()&& values.visitOutTime.equals(blank)){

            if(!values.trnStatus.equals("Done")&&!values.trnStatus.equals("Rejected")){
                listForOpen.add(values)
            }
        }

        listForOpen.sortByDescending {
            it.reqNo
        }

        val comlogAdapter = Dasgbord_OngoingAdapter(listForOpen)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())

        dashbord_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        dashbord_recyclerView.layoutManager = manager
        dashbord_recyclerView.adapter = comlogAdapter

        comlogAdapter.setOnItemClickListener(object :
            Dasgbord_OngoingAdapter.OnAdapterItemClickListener {

            override fun OnClick(v: View?, position: Int) {

                val userData: GetComplainData = listForOpen.get(position)
                Utility.savedasCompInfo(userData, requireActivity())

                val v: View = layoutInflater.inflate(R.layout.history_details, null)
                val dialog = Dialog(v.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                Objects.requireNonNull(dialog.window)
                    ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

                setImg(v)
                hide(v)
                show(v)
                setvalue(v)

                dialog.setContentView(v)
                dialog.setCancelable(true)
                dialog.show()
                var back : ImageView = v.findViewById(R.id.rback)
                back.setOnClickListener {
                    dialog.dismiss()
                }


            }
        })


        /*
            rootView.rvAttendance.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )

     */

        /*
            itemAdapter.setOnItemClickListener(object :
                AttendanceReportAdapter.OnAdapterItemClickListener {
                override fun OnSelectSubMenu(v: View?, position: Int) {

                }
            })

     */
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
    fun hide(view: View){
        view.hide_details.setVisibility(View.GONE)
        view.hide2_details.setVisibility(View.GONE)
        // rootView.action_taken.setVisibility(View.GONE)
        //   rootView.action_RecyclerView.setVisibility(View.GONE)
        view.details_goback.setOnClickListener {
            view.hide_details.setVisibility(View.GONE)
            view.hide2_details.setVisibility(View.GONE)
            view.details_more.setVisibility(View.VISIBLE)

        }
    }

    fun setvalue(view: View){
        hiscompdata = Utility.getsavedasCompInfo(requireActivity())!!

        view.item_req_num.text = "Request Number : "+hiscompdata.reqNo.toString()

        view.item_title.text = hiscompdata.reqCat.toString()+
                " Title : "+hiscompdata.reqTitle

        view.d_oc_date.text= "Occurence Date : "+Utility.changeDateFormat(
            hiscompdata.reqDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
        view.d_oc_expResolvDate.text=Utility.changeDateFormat(hiscompdata.expectedResolvDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
        view.d_oc_num.text = hiscompdata.compMob.toString()
        view.d_oc_email.text=hiscompdata.compEmail.toString()
        view.d_oc_type.text=hiscompdata.reqType.toString()
        view.d_oc_details.text=hiscompdata.reqDet.toString()


        var documentImg = hiscompdata.reqImg.toString()
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
                .into(view.d_oc_img)


        }else{
            //  rootView.d_oc_vf_img.setVisibility(View.GONE)
            view.d_oc_img.setVisibility(View.GONE)
        }


        //todo action taken
        var title = hiscompdata.reqCat + " Details"
        val Status = hiscompdata.trnStatus
        view.toolbar_dtitle.text = title.toString()
        var actionTakenList = hiscompdata.follwAct
        var position = hiscompdata.follwAct.size-1
        var actStatus = actionTakenList.get(position).actStatus

        val action_taken: TextView = view.findViewById(R.id.action_taken)

        val recyclerView: RecyclerView = view.findViewById(R.id.RVsolution)

        recyclerView.layoutManager = LinearLayoutManager(context)

        if(!Status.equals("Open")){
            val adapter = ComplainSolve_Adapter(actionTakenList)
            recyclerView.adapter = adapter
        }else{
            action_taken.visibility= View.GONE
            recyclerView.visibility = View.GONE
        }



    }

    fun show(view: View){
        // view.hide_details.setVisibility(View.GONE)
        view.details_more.setOnClickListener {
            view.details_more.setVisibility(View.GONE)
            view.hide_details.setVisibility(View.VISIBLE)
            view.hide2_details.setVisibility(View.VISIBLE)
        }

    }



//todo for comp summary
    private fun getComplainSummary(
        user_id: String


    ) {

    loadingAnim = Utility.baseLoadingAnimation(
        requireActivity(),
        Dialog(requireContext()),
        "P l e a s e    w a i t"
    )
    loadingAnim.show()

        logViewModel.getSavedComplainSum(user_id)
            ?.observe(requireActivity()) {
                when (it.status) {
                    Status.SUCCESS -> {

                        it.responseData?.let { res ->
                            if (res.code == "200") {
                                loadingAnim.dismiss()
                                successLogList1(res)
                            }

                        }

                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                        // rootView.shimmer_att_container.visibility = View.GONE
                        // rootView.shimmer_att_container.stopShimmer()
                        loadingAnim.dismiss()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
    }

    private fun successLogList1(res: GetUserReqSumResponce) {

        if (res.code == "200") {
            if (res.data.isNotEmpty()) {
                prepareLogRV1(res.data)



            } else {


            }
        }
    }


    private fun prepareLogRV1(items: ArrayList<GetUserReqSumData>) {


        var incidentNum : TextView = rootView.findViewById(R.id.d_incidentNum)
        var inquireyNum : TextView =  rootView.findViewById(R.id.d_inquiresNum)
        var pendingIncident : TextView = rootView.findViewById(R.id.d_penIncident)
        var PendingInquirey : TextView =  rootView.findViewById(R.id.d_penInquirey)
        var appliedNo: TextView =  rootView.findViewById(R.id.appliedNo)
        var resolvedNo: TextView =  rootView.findViewById(R.id.resolvedNo)

        var pendingNo: TextView =  rootView.findViewById(R.id.pendingNo)
        incidentNum.setText(items.get(0).incidentNo.toString())
        inquireyNum.setText(items.get(0).inquiryNo.toString())
        pendingIncident.setText("Pending "+items.get(0).incidentPendingNo.toString())
        PendingInquirey.setText("Pending "+items.get(0).inquiryPendingNo.toString())
        appliedNo.setText(items.get(0).appliedNo.toString())
        resolvedNo.setText(items.get(0).resolvedNo.toString())
        pendingNo.setText(items.get(0).pendingNo.toString())


        val progressBar = rootView.findViewById<ProgressBar>(R.id.progressBar)
        val maxProgress = 100 // Set your custom maximum progress value
        progressBar.max = maxProgress
        var progress = items.get(0).incidentNo!!.toInt() // Set your desired progress value here
        progressBar.progress = progress

        val progressBar1 = rootView.findViewById<ProgressBar>(R.id.progressBar2)
        val maxProgress1 = 100 // Set your custom maximum progress value
        progressBar.max = maxProgress1
        var progress1 = items.get(0).inquiryNo!!.toInt() // Set your desired progress value here
        progressBar1.progress = progress1

    }


}