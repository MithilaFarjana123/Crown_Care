package com.crowncement.crowncement_complain_management.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Model.*
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_details.view.*
import kotlinx.android.synthetic.main.frag_notification.view.*
import kotlinx.android.synthetic.main.frag_notification.view.toolbar_title
import kotlinx.android.synthetic.main.frag_notification_details.view.*
import java.io.File
import java.time.LocalDateTime


class Frag_details : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    var dataReceived=0

    lateinit var hiscompdata : GetComplainData




    lateinit var cardFile: java.util.ArrayList<File>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.frag_details, container, false)
        dataReceived = arguments?.getInt("h_position")!!

        setupViewModel()
        setImg()
        hide()
        show()
        hiscompdata = Utility.getsavehisCompInfo(requireActivity())!!

        val currentDateTime = LocalDateTime.now()
        val currentYear = currentDateTime.year.toString()
        val currentMonth = currentDateTime.monthValue.toString()



        setvalue()



    //todo action taken
        var title = hiscompdata.reqCat + " Details"
        rootView.toolbar_dtitle.text = title.toString()
        var actionTakenList = hiscompdata.follwAct
        var position = hiscompdata.follwAct.size-1
        var actStatus = actionTakenList.get(position).actStatus

        val action_taken: TextView = rootView.findViewById(R.id.action_taken)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.RVsolution)

        recyclerView.layoutManager = LinearLayoutManager(context)

        if(!actStatus.equals("")){
            val adapter = ComplainSolve_Adapter(actionTakenList)
            recyclerView.adapter = adapter
        }else{
            action_taken.visibility= View.GONE
            recyclerView.visibility = View.GONE
        }


        return rootView
    }


    fun hide(){
        rootView.hide_details.setVisibility(View.GONE)
        rootView.hide2_details.setVisibility(View.GONE)
       // rootView.action_taken.setVisibility(View.GONE)
     //   rootView.action_RecyclerView.setVisibility(View.GONE)
            rootView.details_goback.setOnClickListener {
            rootView.hide_details.setVisibility(View.GONE)
            rootView.hide2_details.setVisibility(View.GONE)
            rootView.details_more.setVisibility(View.VISIBLE)

        }
    }

    fun show(){
        rootView.details_more.setOnClickListener {
            rootView.details_more.setVisibility(View.GONE)
            rootView.hide_details.setVisibility(View.VISIBLE)
            rootView.hide2_details.setVisibility(View.VISIBLE)
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
                .into(rootView.d_userImg)

        } else {
            rootView.d_userImg.setBackgroundResource(R.drawable.human)
        }
    }


    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
                .get(
                    ComplainViewModel::class.java
                )

    }




    fun setvalue(){
        rootView.item_title.text = hiscompdata.reqCat.toString()+
                " Title : "+hiscompdata.reqTitle

        rootView.d_oc_date.text= "Occurence Date : "+Utility.changeDateFormat(
            hiscompdata.trnDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
        rootView.d_oc_expResolvDate.text=Utility.changeDateFormat(hiscompdata.expectedResolvDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
        rootView.d_oc_num.text = hiscompdata.compMob.toString()
        rootView.d_oc_email.text=hiscompdata.compEmail.toString()
        rootView.d_oc_type.text=hiscompdata.reqType.toString()
        rootView.d_oc_details.text=hiscompdata.reqDet.toString()


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
                .into(rootView.d_oc_img)


        }else{
          //  rootView.d_oc_vf_img.setVisibility(View.GONE)
            rootView.d_oc_img.setVisibility(View.GONE)
        }

    }

}