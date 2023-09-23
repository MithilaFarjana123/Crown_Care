package com.crowncement.crowncement_complain_management.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Adapter.ComplainAdapter
import com.crowncement.crowncement_complain_management.data.Adapter.NotificationAdapter
import com.crowncement.crowncement_complain_management.data.Model.Complain


class Frag_notification : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.frag_notification, container, false)

        val recyclerNView: RecyclerView = view.findViewById(R.id.notification_recyclerView)
        // this creates a vertical layout Manager
        recyclerNView.layoutManager = LinearLayoutManager(context)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Complain>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(Complain("Item " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = NotificationAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerNView.adapter = adapter
        adapter.setOnItemClickListener(object :
            NotificationAdapter.OnAdapterItemClickListener {

            override fun CancleItem(v: View?, position: Int) {
               // val adapter = NotificationAdapter(data)

                // Setting the Adapter with the recyclerview
               // recyclerNView.adapter = adapter
               // getCheckoutdone(data[position])
            }

        })


        return view


    }





}