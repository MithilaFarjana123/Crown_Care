package com.crowncement.crowncement_complain_management.data.Adapter

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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.Fragment.Frag_notificationDetails
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.common.API.Endpoint.IMAGE_BASE_URL
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.RequestDetails
import kotlinx.android.synthetic.main.frag_notification.view.*
import kotlinx.android.synthetic.main.notification_design.view.*
import kotlinx.android.synthetic.main.notification_item_design.view.*
import org.w3c.dom.Text
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*



class NotificationAdapter (val notificationList : ArrayList<RequestDetails>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {


    lateinit var mContext: Context
    private lateinit var onClickListener: OnAdapterItemClickListener

    interface OnAdapterItemClickListener {
        fun CancleItem(v: View?, position: Int)
        fun OnClick(v: View?, position: Int)

      //  fun move(v: View?, position: Int)
    }


    fun setOnItemClickListener(listener: OnAdapterItemClickListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.notification_item_design, parent, false)

        return MyViewHolder(view,onClickListener)

    }



    override fun getItemCount(): Int {
        return notificationList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        val i = position
        var visitorposition = notificationList[position]

        holder.n_title.text = notificationList[position].reqTitle
        holder.n_u_name.text = notificationList[position].reqEmpName
        holder.n_u_catagory.text = notificationList[position].reqCat

        holder.n_u_dep.text = notificationList[position].deptNam

        var img = notificationList[position].compImage.toString()

        if (img.isNotEmpty()) {
            //ToDo Image
            Glide
                .with(mContext)
                //  .load(IMAGE_BASE_URL + img)
                .load(Endpoint.IMAGE_BASE_URL + img)
                .error(R.drawable.human)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.human)
                //  .transform(RoundedCorners(30,30.0))
                .into(holder.itemView.n_img)
        }


//for resolve date


        val startDateStr = notificationList[position].trnDate.toString()
        val endDateStr = notificationList[position].expectedResolvDate.toString()

        //  holder.itemView.setBackgroundResource(R.color.colorSelected)
        val percentageRemaining = calculatePercentageRemaining(startDateStr, endDateStr)
        if (percentageRemaining >= 67) {

            holder.itemView.cardLay.setBackgroundColor(Color.parseColor("#E1F2E8"))
        } else if (percentageRemaining >= 34) {
            holder.itemView.n_sideLay.setBackgroundColor(Color.parseColor("#f39c12"))
            holder.itemView.cardLay.setBackgroundColor(Color.parseColor("#F8F3E5"))
        }else if(percentageRemaining >= 0){

            holder.itemView.n_sideLay.setBackgroundColor(Color.parseColor("#c0392b"))
            holder.itemView.cardLay.setBackgroundColor(Color.parseColor("#FAEFEE"))
           // println("Error in date parsing or calculation.")
        }else{

            holder.itemView.n_sideLay.setBackgroundColor(Color.parseColor("#c0392b"))
            holder.itemView.cardLay.setBackgroundColor(Color.parseColor("#FAEFEE"))
        }


    }


    fun calculateRemainingDays(startDateStr: String, endDateStr: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        try {
            val startDate = dateFormat.parse(startDateStr)
            val endDate = dateFormat.parse(endDateStr)

            val currentTimeMillis = System.currentTimeMillis()
            val currentDateTime = Date(currentTimeMillis)

            if (startDate.before(currentDateTime)) {
                startDate.time = currentDateTime.time
            }

            val difference = endDate.time - startDate.time
            val remainingDays = (difference / (1000 * 60 * 60 * 24)).toInt()

            return remainingDays
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }

    fun calculatePercentageRemaining(startDateStr: String, endDateStr: String): Double {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        try {
            val startDate = dateFormat.parse(startDateStr)
            val endDate = dateFormat.parse(endDateStr)

            val currentTimeMillis = System.currentTimeMillis()
            val currentDateTime = Date(currentTimeMillis)

            if (startDate.before(currentDateTime)) {
                startDate.time = currentDateTime.time
            }

            val difference = endDate.time - startDate.time
            val remainingDays = (difference / (1000 * 60 * 60 * 24)).toInt()

            val totalDays = calculateRemainingDays(startDateStr, endDateStr)

            if (totalDays >= 0) {
                val percentageRemaining = (remainingDays.toDouble() / totalDays.toDouble()) * 100
                return percentageRemaining
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1.0
    }



    class MyViewHolder(itemView: View,listener: NotificationAdapter.OnAdapterItemClickListener) : RecyclerView.ViewHolder(itemView){

        var n_img=itemView.findViewById<ImageView>(R.id.n_img)
        var n_title = itemView.findViewById<TextView>(R.id.n_title)
        var n_u_name = itemView.findViewById<TextView>(R.id.n_u_name)
        var n_u_catagory = itemView.findViewById<TextView>(R.id.n_u_catagory)
        var n_u_dep = itemView.findViewById<TextView>(R.id.n_u_dep)
        var n_sideLay = itemView.findViewById<LinearLayout>(R.id.n_sideLay)

        init {
            itemView.setOnClickListener {
                listener.CancleItem(itemView, adapterPosition)
                listener.OnClick(itemView, adapterPosition)
               // itemView.setVisibility(View.GONE)

            }
        }





    }


}







