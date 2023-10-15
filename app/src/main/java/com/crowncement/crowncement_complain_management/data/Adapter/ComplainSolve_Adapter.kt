package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Model.FollwAct

class ComplainSolve_Adapter(val complainList: ArrayList<FollwAct>) : RecyclerView.Adapter<ComplainSolve_Adapter.MyViewHolder>() {
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
        holder.hf_reportingBName.text=complainList[position].repToName




    }

    override fun getItemCount(): Int {
        return complainList.size
    }


    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val hf_reportingBName = itemView.findViewById<TextView>(R.id.hf_reportingBName)
        val hf_replyDate = itemView.findViewById<TextView>(R.id.hf_replyDate)
        val hf_reprtingBCom = itemView.findViewById<TextView>(R.id.hf_reprtingBCom)


//        init {
//            itemView.setOnClickListener {
//                listener.OnClick(itemView, adapterPosition)
//
//
//            }
//        }

    }

}