package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.FollwAct
import kotlinx.android.synthetic.main.conversation_item.view.*
import kotlinx.android.synthetic.main.frag_history.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ComplainSolve_Adapter(val complainList: ArrayList<FollwAct>) : RecyclerView.Adapter<ComplainSolve_Adapter.MyViewHolder>() {
    lateinit var mContext: Context

//    private lateinit var onClickListener: OnAdapterItemClickListener
//    interface OnAdapterItemClickListener {
//
//        fun OnSendComment(v: View?, position: Int)
//
//    }


//    fun setOnItemClickListener(listener: OnAdapterItemClickListener) {
//        onClickListener = listener
//    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplainSolve_Adapter.MyViewHolder {

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val View = inflater.inflate(R.layout.conversation_item, parent, false)

        return MyViewHolder(View)
        // return MyViewHolder(View,onClickListener)

    }



    override fun onBindViewHolder(holder: ComplainSolve_Adapter.MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        var visitorposition = complainList[position]
        holder.hf_reportingBName.text=complainList[position].repToName
        var feedbackDet = complainList[position].feedbackDet
        var actionDet = complainList[position].actionDet
        var feedback_date = complainList[position].feedbackDate
        if(!feedbackDet.equals("")){
            holder.hf_reprtingBCom.text=complainList[position].feedbackDet
        }else{
            holder.hf_reprtingBCom.visibility = View.GONE
        }

        if (!actionDet.equals("")){
            holder.hf_reprtingBaction.text=complainList[position].actionDet
        }else{
            holder.hf_reprtingBaction.visibility = View.GONE
        }


        if(!feedbackDet.equals("")){
            feedback_date= " Approximate Resolve Date : "+ Utility.changeDateFormat(
                feedback_date,
                "yyyy-MM-dd",
                "MMM dd,yyyy"
            )
            holder.hf_feedback_date.text = feedback_date
        }else if(
            (complainList.get(position).actStatus.equals("Done"))||
            (complainList.get(position).actStatus.equals("Forwarded"))
                ){
            holder.hf_feedback_date.visibility= View.GONE
        }

        else{
            holder.hf_feedback_date.visibility= View.GONE
        }


        var replyTime = complainList.get(position).actTime
            holder.seen_time.text = Utility.changeDateFormat(
                replyTime,
                "yyyy-MM-dd hh:mm:ss",
                "hh:mm:ss a"
            )

        holder.seen_date.text = Utility.changeDateFormat(
            replyTime,
            "yyyy-MM-dd hh:mm:ss",
            "MMM dd,yyyy"
        ).toString()



      //  var documentimg =  complainList[position].follImg.toString()
            if(complainList[position].follImg!="") {

                val imgS=Endpoint.IMAGE_BASE_URL + complainList[position].follImg
                Glide
                    .with(mContext)
                    .load(imgS)
                   // .load(Endpoint.IMAGE_BASE_URL + "/da/docs/x880022/care/follimg/" + documentimg)
                    .error(R.drawable.document)
                    .fitCenter()
                   // .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    // .placeholder(R.drawable.baseline_no_img)
                    //  .transform(RoundedCorners(30,30.0))
                    .into(holder.hf_document_img)
            }else{
                holder.hf_doc.visibility = View.GONE

            }

        if(complainList.get(position).actStatus.equals("")){
            holder.conversation_item.visibility = View.GONE
        }






    }

    override fun getItemCount(): Int {
        return complainList.size
    }


    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

       // class MyViewHolder (itemView: View,listener: ComplainSolve_Adapter.OnAdapterItemClickListener) : RecyclerView.ViewHolder(itemView){

        val hf_reportingBName = itemView.findViewById<TextView>(R.id.hf_reportingBName)
      //  val hf_replyDate = itemView.findViewById<TextView>(R.id.hf_replyDate)
        val hf_reprtingBCom = itemView.findViewById<TextView>(R.id.hf_reprtingBCom)
        val hf_feedback_date = itemView.findViewById<TextView>(R.id.hf_feedback_date)
        var hf_doc = itemView.findViewById<CardView>(R.id.hf_doc)
        var hf_document_img = itemView.findViewById<ImageView>(R.id.hf_doc_img)
        var seen_time = itemView.findViewById<TextView>(R.id.seen_time)
        var seen_date = itemView.findViewById<TextView>(R.id.seen_date)
        var hf_reprtingBaction =itemView.findViewById<TextView>(R.id.hf_reprtingBaction)
        var conversation_item = itemView.findViewById<LinearLayout>(R.id.conversation_item)
        var users_reply = itemView.findViewById<LinearLayout>(R.id.users_reply)
        var hf_send = itemView.findViewById<ImageView>(R.id.hf_send)
        var hf_Comment = itemView.findViewById<EditText>(R.id.hf_Comment)




        /*
        init {
            itemView.setOnClickListener {
                listener.OnSendComment(itemView, adapterPosition)

                itemView.users_reply.setVisibility(View.GONE);
            }
        }

         */



    }

}