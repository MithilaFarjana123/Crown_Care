package com.crowncement.crowncement_complain_management.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.crowncement.crowncement_complain_management.data.Adapter.NotificationAdapter
import com.crowncement.crowncement_complain_management.data.Model.GetActivity4AppResponse
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.RequestDetails
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSolverViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainSolverViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import kotlinx.android.synthetic.main.frag_history.*
import kotlinx.android.synthetic.main.frag_history.view.*
import kotlinx.android.synthetic.main.frag_notification.*
import kotlinx.android.synthetic.main.frag_notification.view.*


class Frag_notification : Fragment() {


    lateinit var rootView: View
    lateinit var logViewModel: ComplainSolverViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.frag_notification, container, false)

        ComplainSolverViewModelFactory()
        setupViewModel()
        setImg()

        var id = Utility.getValueByKey(requireActivity(),"username")
//
//        if (id != null) {
//            getComplainSolverDataList(id)
//        }

        getComplainSolverDataList("E11-001795")


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

                prepareLogRV(res.data)
            } else {


            }
        }
    }


    private fun prepareLogRV(items: java.util.ArrayList<RequestDetails>) {
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

                // Setting the Adapter with the recyclerview
                // recyclerNView.adapter = adapter
                // getCheckoutdone(data[position])
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



}