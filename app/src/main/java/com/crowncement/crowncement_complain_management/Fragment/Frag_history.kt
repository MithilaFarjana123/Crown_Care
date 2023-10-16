package com.crowncement.crowncement_complain_management.Fragment

import android.app.Activity
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
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
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
import com.crowncement.crowncement_complain_management.data.Model.Data
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import com.crowncement.crowncement_complain_management.data.Model.GetComplainResponse
import com.crowncement.crowncement_complain_management.data.Model.RequestDetails
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.ComplainViewModelFactory
import com.crowncement.crowncement_complain_management.ui.viewmodelfactory.UpdateSeenStatViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import kotlinx.android.synthetic.main.frag_history.*
import kotlinx.android.synthetic.main.frag_history.view.*
import kotlinx.android.synthetic.main.take_action.*
import java.io.File
import java.time.LocalDateTime


class Frag_history : Fragment() {


    lateinit var rootView: View
    lateinit var logViewModel: ComplainViewModel
    lateinit var logUpdateSeenViewModel: UpdateSeenStatViewModel

    lateinit var cardFile: java.util.ArrayList<File>

    lateinit var listForOpen : ArrayList<GetComplainData>
    lateinit var listForDone:ArrayList<GetComplainData>

    lateinit var listToSendAdapter:ArrayList<GetComplainData>




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
        listForDone= ArrayList()
        for (values in items){
            //  if(values.visitCard.toString().isNotEmpty()&& values.visitOutTime.equals(blank)){

            if(values.trnStatus.equals("Open")){
                listForOpen.add(values)

            }else if (values.trnStatus.equals("Done")){
                listForDone.add(values)
            }


        }



        listToSendAdapter = listForOpen


        rootView.h_open.setOnClickListener {
            listToSendAdapter = listForOpen

        }

        rootView.h_Done.setOnClickListener {

            listToSendAdapter = listForDone

        }



        val comlogAdapter = ComplainAdapter(items)
        val rLayoutmanager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        history_recyclerView.layoutManager = rLayoutmanager
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        history_recyclerView.layoutManager = manager
        history_recyclerView.adapter = comlogAdapter
////////////added new
        comlogAdapter.setOnItemClickListener(object :
            ComplainAdapter.OnAdapterItemClickListener {

            override fun OnClick(v: View?, position: Int) {

                val userData: GetComplainData = items.get(position)
                Utility.savehisCompInfo(userData, requireActivity())


                val activity = view?.context as AppCompatActivity
                val demofragment = Frag_details()

                // Create a Bundle to hold the position value
                val bundle = Bundle()
                bundle.putInt("h_position", position) // Assuming adapterPosition holds the position

              //  val userData: GetComplainData = listToSendAdapter.get(position)
               // Utility.saveCompInfo(userData, requireActivity())

                // Attach the Bundle to the fragment
                demofragment.arguments = bundle

                // Replace the current fragment with the destination fragment
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.history_fragment, demofragment)
                    .addToBackStack(null)
                    .commit()

            }


        })

        ////////////


    }







    }











