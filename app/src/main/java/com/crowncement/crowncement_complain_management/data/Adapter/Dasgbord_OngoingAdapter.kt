package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData

class Dasgbord_OngoingAdapter(val complainList : List<GetComplainData>) : RecyclerView.Adapter<Dasgbord_OngoingAdapter.MyViewHolder>() {


    lateinit var mContext: Context




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Dasgbord_OngoingAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val View = inflater.inflate(R.layout.dashboardchild_underway, parent, false)

        return Dasgbord_OngoingAdapter.MyViewHolder(View)
    }

    override fun getItemCount(): Int {
        return complainList.size
    }

    override fun onBindViewHolder(holder: Dasgbord_OngoingAdapter.MyViewHolder, position: Int) {
        mContext = holder.itemView.context
    }



    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

    }



}