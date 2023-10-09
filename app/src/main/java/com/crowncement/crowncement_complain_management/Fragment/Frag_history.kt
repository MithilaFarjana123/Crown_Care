package com.crowncement.crowncement_complain_management.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.data.Model.UpdateSeenStatResponce
import com.crowncement.crowncement_complain_management.databinding.ActivityMainBinding
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.UpdateSeenStatViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_history.*
import kotlinx.android.synthetic.main.frag_history.view.*
import java.time.LocalDateTime
import java.time.Year
import java.util.ArrayList


class Frag_history : Fragment() {


    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    lateinit var logUpdateSeenViewModel: UpdateSeenStatViewModel


    lateinit var listForOpen : ArrayList<GetComplainData>
    lateinit var listForDone:ArrayList<GetComplainData>



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
        listForDone= ArrayList()
        for (values in items){
            //  if(values.visitCard.toString().isNotEmpty()&& values.visitOutTime.equals(blank)){

            if(values.trnStatus.equals("Open")){
                listForOpen.add(values)
            }else if (values.trnStatus.equals("Done")){
                listForDone.add(values)
            }
        }





        val comlogAdapter = ComplainAdapter(listForOpen)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        history_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        history_recyclerView.layoutManager = manager
        history_recyclerView.adapter = comlogAdapter
/*
        comlogAdapter.setOnItemClickListener(object :
            ComplainAdapter.OnAdapterItemClickListener {

            override fun OnClick(v: View?, position: Int) {
                //  getCheckoutdone(listForDone[position].visitLogId.toString())
                comlogAdapter.setOnItemClickListener(object :
                    ComplainAdapter.OnAdapterItemClickListener {

                    override fun OnClick(v: View?, position: Int) {
                        //  getCheckoutdone(listForDone[position].visitLogId.toString())
                        UpdateSeenStatData(listForOpen.get(position).reqEmp.toString(),
                            listForOpen.get(position).reqNo.toString()
                        )
                    }
                })
            }
        })

 */

        rootView.h_open.setOnClickListener {
            val comlogAdapter = ComplainAdapter(listForOpen)
            val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

            history_recyclerView.layoutManager = rLayoutmanager
            val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            history_recyclerView.layoutManager = manager
            history_recyclerView.adapter = comlogAdapter
/*
            comlogAdapter.setOnItemClickListener(object :
                ComplainAdapter.OnAdapterItemClickListener {

                override fun OnClick(v: View?, position: Int) {
                    //  getCheckoutdone(listForDone[position].visitLogId.toString())
                    UpdateSeenStatData(listForOpen.get(position).reqEmp.toString(),
                    listForOpen.get(position).reqNo.toString()
                        )
                }
            })

 */

        }

        rootView.h_Done.setOnClickListener {
            val comlogAdapter = ComplainAdapter(listForDone)
            val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

            history_recyclerView.layoutManager = rLayoutmanager
            val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            history_recyclerView.layoutManager = manager
            history_recyclerView.adapter = comlogAdapter

            /*
            comlogAdapter.setOnItemClickListener(object :
                ComplainAdapter.OnAdapterItemClickListener {

                override fun OnClick(v: View?, position: Int) {
                    //  getCheckoutdone(listForDone[position].visitLogId.toString())
                    UpdateSeenStatData(listForDone.get(position).reqEmp.toString(),
                        listForDone.get(position).reqNo.toString()
                    )
                }
            })

             */

        }





    }


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

                        // rootView.shimmer_att_container.visibility = View.GONE
                        // rootView.shimmer_att_container.stopShimmer()

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


    }











