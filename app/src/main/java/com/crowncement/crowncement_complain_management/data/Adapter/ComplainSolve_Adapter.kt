package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.FollwAct

class ComplainSolve_Adapter(val complainList: ArrayList<FollwAct>) : RecyclerView.Adapter<ComplainSolve_Adapter.MyViewHolder>() {
    lateinit var mContext: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainSolve_Adapter.MyViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val View = inflater.inflate(R.layout.conversation_item, parent, false)

         return MyViewHolder(View)



    }



    override fun onBindViewHolder(holder: ComplainSolve_Adapter.MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        var visitorposition = complainList[position]
        holder.hf_reportingBName.text=complainList[position].repToName
        var feedbackDet = complainList[position].feedbackDet
        var actionDet = complainList[position].feedbackDet
        var feedback_date = complainList[position].feedbackDate
        if(!feedbackDet.equals("")){
            holder.hf_reprtingBCom.text=complainList[position].feedbackDet
        }else if (!actionDet.equals("")){
            holder.hf_reprtingBCom.text=complainList[position].actionDet
        }else {
            holder.hf_reprtingBCom.visibility = View.GONE
        }


        if(!feedbackDet.equals("")){
            feedback_date= "Feedback Date : "+ Utility.changeDateFormat(
                feedback_date,
                "yyyy-MM-dd",
                "MMM dd,yyyy"
            )
            holder.hf_feedback_date.text = feedback_date
        }



    }

    override fun getItemCount(): Int {
        return complainList.size
    }


    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val hf_reportingBName = itemView.findViewById<TextView>(R.id.hf_reportingBName)
      //  val hf_replyDate = itemView.findViewById<TextView>(R.id.hf_replyDate)
        val hf_reprtingBCom = itemView.findViewById<TextView>(R.id.hf_reprtingBCom)
        val hf_feedback_date = itemView.findViewById<TextView>(R.id.hf_feedback_date)


    }

}