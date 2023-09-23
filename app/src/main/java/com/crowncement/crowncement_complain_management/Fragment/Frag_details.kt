package com.crowncement.crowncement_complain_management.Fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainSolve_Adapter
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.FollwAct
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import kotlinx.android.synthetic.main.frag_details.view.*
import java.time.LocalDateTime


class Frag_details : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    var dataReceived=0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.frag_details, container, false)
        dataReceived = arguments?.getInt("position")!!
        setupViewModel()
        val currentDateTime = LocalDateTime.now()
        val currentYear = currentDateTime.year.toString()
        val currentMonth = currentDateTime.monthValue.toString()

        getComplainList("E00-005445",currentYear,currentMonth)



    //todo action taken
        val recyclerView: RecyclerView = rootView.findViewById(R.id.RVsolution)
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Complain>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..3) {
            data.add(Complain("Item " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = ComplainSolve_Adapter(data)

        // Setting the Adapter with the recyclerview
        recyclerView.adapter = adapter



        return rootView
    }



    private fun setupViewModel() {
        logViewModel =
            ViewModelProviders.of(requireActivity(), ComplainViewModelFactory())
                .get(
                    ComplainViewModel::class.java
                )

    }



    private fun getComplainList(
        user_id: String,
        curr_yr: String,
        curr_mon: String

    ) {
        logViewModel.getSavedcomplain(user_id, curr_yr,curr_mon)
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

                successLogList1(res.data.get(dataReceived))
            } else {


            }
        }
    }

    private fun successLogList1(res: GetComplainData) {

        rootView.di_num.text= Utility.changeDateFormat(
            res.trnDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )



    }

}