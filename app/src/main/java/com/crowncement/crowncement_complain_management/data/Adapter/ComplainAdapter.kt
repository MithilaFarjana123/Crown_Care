package com.crowncement.crowncement_complain_management.data.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.Fragment.Frag_details
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Utility

import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import kotlinx.android.synthetic.main.com_item.view.*
import kotlinx.android.synthetic.main.complain_item.view.*
import java.text.SimpleDateFormat
import java.util.*



class ComplainAdapter (val complainList : List<GetComplainData>) : RecyclerView.Adapter<ComplainAdapter.MyViewHolder>() {
    lateinit var mContext: Context
    private lateinit var onClickListener: OnAdapterItemClickListener


    interface OnAdapterItemClickListener {

        fun OnClick(v: View?, position: Int)

    }

    fun setOnItemClickListener(listener: OnAdapterItemClickListener) {
        onClickListener = listener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val View = inflater.inflate(R.layout.com_item, parent, false)

        return MyViewHolder(View,onClickListener)

      //  return MyViewHolder(View)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        var visitorposition = complainList[position]
        //holder.h_solver.text=complainList[position].


        val p = complainList[position].follwAct.size-1
        holder.h_solver.text=complainList[position].follwAct[p].repToName

        var seen_Status = complainList[position].follwAct[p].seenStatus
        if(seen_Status.equals("Yes")){

            holder.seen_status.setImageResource(R.drawable.seen)

        }else{
            holder.seen_status.setImageResource(R.drawable.unseen)

        }



        var selectedDate =complainList[position].trnDate

        holder.h_date.text=Utility.changeDateFormat(
            selectedDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )


        var expected_resolv_date = complainList[position].expectedResolvDate
        holder.h_exp_date.text=Utility.changeDateFormat(
            expected_resolv_date,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )

        var estemated_date_bySolver = complainList[position].follwAct[p].feedbackDate
        if(estemated_date_bySolver!="2999-12-31"&&estemated_date_bySolver!=""){
            holder.h_exres_date.text =Utility.changeDateFormat(
                estemated_date_bySolver,
                "yyyy-MM-dd",
                "MMM dd,yyyy"
            )
        }else{
            holder.layEstResolved_by.setVisibility(View.GONE)
        }


        val st = complainList[position].trnStatus
        if(st.equals("Open")){
            holder.h_status.text=st
            holder.h_status.setTextColor(Color.parseColor("#1d8348"))
        }else if (st.equals("Done")){
            holder.h_status.text=st
            holder.h_status.setTextColor(Color.parseColor("#c0392b"))
        }else{
            holder.h_status.text=st
            holder.h_status.setTextColor(R.color.red)
        }
      //  holder.h_status.text=complainList[position].trnStatus
        holder.h_title.text = complainList[position].reqTitle

        holder.item_heading.text = complainList[position].reqCat + " Info "


    }

    override fun getItemCount(): Int {
        return complainList.size
    }


 //   class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

    class MyViewHolder (itemView: View,listener: OnAdapterItemClickListener) : RecyclerView.ViewHolder(itemView){

        val h_status = itemView.findViewById<TextView>(R.id.h_status)
        val h_date =itemView.findViewById<TextView>(R.id.h_date)
        val h_title=itemView.findViewById<TextView>(R.id.h_title)
        val h_solver = itemView.findViewById<TextView>(R.id.h_solver)
        val h_exp_date = itemView.findViewById<TextView>(R.id.h_exp_date)
        val h_exres_date = itemView.findViewById<TextView>(R.id.h_exres_date)
        val item_heading = itemView.findViewById<TextView>(R.id.item_heading)
        val seen_status = itemView.findViewById<ImageView>(R.id.seen_status)

        val layEstResolved_by = itemView.findViewById<LinearLayout>(R.id.layEstResolved_by)


        init {
            itemView.setOnClickListener {
                listener.OnClick(itemView, adapterPosition)


            }
        }



    }

}




