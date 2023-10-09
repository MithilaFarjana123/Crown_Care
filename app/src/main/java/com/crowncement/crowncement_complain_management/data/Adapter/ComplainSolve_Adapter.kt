package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Model.Complain

class ComplainSolve_Adapter (val complainList : List<Complain>) : RecyclerView.Adapter<ComplainSolve_Adapter.MyViewHolder>() {
    lateinit var mContext: Context
  //  private lateinit var onClickListener: OnAdapterItemClickListener


//    interface OnAdapterItemClickListener {
//
//        fun OnClick(v: View?, position: Int)
//
//    }

//    fun setOnItemClickListener(listener: ComplainSolve_Adapter.OnAdapterItemClickListener) {
//        onClickListener = listener
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainSolve_Adapter.MyViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val View = inflater.inflate(R.layout.conversation_item, parent, false)

         return MyViewHolder(View)


        // return ComplainSolve_Adapter.MyViewHolder(View,onClickListener)
    }



    override fun onBindViewHolder(holder: ComplainSolve_Adapter.MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        var visitorposition = complainList[position]
    }

    override fun getItemCount(): Int {
        return complainList.size
    }


    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

//        init {
//            itemView.setOnClickListener {
//                listener.OnClick(itemView, adapterPosition)
//
//
//            }
//        }

    }

}