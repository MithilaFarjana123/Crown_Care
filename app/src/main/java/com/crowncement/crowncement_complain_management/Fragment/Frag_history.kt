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
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Status
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainAdapter
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.databinding.ActivityMainBinding
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.frag_history.*
import kotlinx.android.synthetic.main.frag_history.view.*
import java.time.LocalDateTime
import java.time.Year


class Frag_history : Fragment() {


    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.frag_history, container, false)
        ComplainViewModelFactory()
        setupViewModel()

        val currentDateTime = LocalDateTime.now()
        val currentYear = currentDateTime.year.toString()
        val currentMonth = currentDateTime.monthValue.toString()

        getComplainList("E00-005445",currentYear,currentMonth)


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

                prepareLogRV(res.data)
            } else {


            }
        }
    }


    private fun prepareLogRV(items: java.util.ArrayList<GetComplainData>) {
        val comlogAdapter = ComplainAdapter(items)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        history_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        history_recyclerView.layoutManager = manager
        history_recyclerView.adapter = comlogAdapter
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




}


