package com.crowncement.crowncement_complain_management.Fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.ImagePathUtils
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainAdapter
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Model.Data
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.data.Model.RequestDetails
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.UpdateSeenStatViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.frag_history.*
import kotlinx.android.synthetic.main.frag_history.view.*
import kotlinx.android.synthetic.main.take_action.*
import java.io.File
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class Frag_history : Fragment() {


    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    lateinit var logUpdateSeenViewModel: UpdateSeenStatViewModel

    lateinit var cardFile: java.util.ArrayList<File>

    lateinit var listForOpen : ArrayList<GetComplainData>
    lateinit var listForDone:ArrayList<GetComplainData>

    lateinit var listToSendAdapter:ArrayList<GetComplainData>

    lateinit var hiscompdata : GetComplainData

    lateinit var loadingAnim: Dialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.frag_history, container, false)
        ComplainViewModelFactory()
        setupViewModel()
        setImg()

        val currentDateTime = LocalDateTime.now()
        val currentYear = currentDateTime.year.toString()
        val currentMonth = currentDateTime.monthValue.toString()
        var user_id: String? = Utility.getValueByKey(requireActivity(),"username")

        if (user_id != null) {
            getComplainList(user_id)
           // getComplainList(user_id,currentYear,currentMonth)
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
                .into(rootView.h_userimg)

        } else {
            rootView.h_userimg.setBackgroundResource(R.drawable.human)
        }
    }



    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
                .get(
                    ComplainViewModel::class.java
                )

        logUpdateSeenViewModel = ViewModelProviders.of(requireActivity(), UpdateSeenStatViewModelFactory())
            .get(
                UpdateSeenStatViewModel::class.java
            )

    }



    private fun getComplainList(
        user_id: String
      //  curr_yr: String,
       // curr_mon: String

    ) {
        loadingAnim = Utility.baseLoadingAnimation(
            requireActivity(),
            Dialog(requireContext()),
            "P l e a s e    w a i t"
        )
        loadingAnim.show()

        logViewModel.getSavedcomplain(user_id)
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


                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG)
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

        listToSendAdapter = ArrayList()
        listForOpen = ArrayList()
        listForDone = ArrayList()
        for (values in items) {
            //  if(values.visitCard.toString().isNotEmpty()&& values.visitOutTime.equals(blank)){

            if (values.trnStatus.equals("Open")) {
                listForOpen.add(values)

            } else if (values.trnStatus.equals("Done")) {
                listForDone.add(values)
            }


        }



        listToSendAdapter = listForOpen

        items.sortByDescending {
            it.reqNo
        }


        if(items.size>0){

        val comlogAdapter = ComplainAdapter(items)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        history_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        history_recyclerView.layoutManager = manager
        history_recyclerView.adapter = comlogAdapter
        //added new
        comlogAdapter.setOnItemClickListener(object :
            ComplainAdapter.OnAdapterItemClickListener {

            override fun OnClick(v: View?, position: Int) {

                val userData: GetComplainData = items.get(position)
                Utility.savehisCompInfo(userData, requireActivity())

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
                var back: ImageView = v.findViewById(R.id.rback)
                back.setOnClickListener {
                    dialog.dismiss()
                }


            }
        })

    }
        ////////////


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



    fun show(view: View){
       // view.hide_details.setVisibility(View.GONE)
        view.details_more.setOnClickListener {
            view.details_more.setVisibility(View.GONE)
            view.hide_details.setVisibility(View.VISIBLE)
            view.hide2_details.setVisibility(View.VISIBLE)
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



    fun setvalue(view: View){
        hiscompdata = Utility.getsavehisCompInfo(requireActivity())!!

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




    }











