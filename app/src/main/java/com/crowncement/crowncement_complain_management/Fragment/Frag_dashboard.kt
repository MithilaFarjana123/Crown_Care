package com.crowncement.crowncement_complain_management.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
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
import com.crowncement.crowncement_complain_management.data.Adapter.Dasgbord_OngoingAdapter
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.android.synthetic.main.frag_dashboard.*
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_history.*
import java.util.*



class Frag_dashboard : Fragment() {

    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.frag_dashboard, container, false)
        ComplainViewModelFactory()
        setupViewModel()
        setValue()
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
        val comlogAdapter = Dasgbord_OngoingAdapter(items)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        dashbord_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dashbord_recyclerView.layoutManager = manager
        dashbord_recyclerView.adapter = comlogAdapter
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