package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.Fragment.Frag_notificationDetails
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.RequestDetails
import kotlinx.android.synthetic.main.notification_design.view.*
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
        val view = inflater.inflate(R.layout.notification_design, parent, false)

        return MyViewHolder(view,onClickListener)

    }



    override fun getItemCount(): Int {
        return notificationList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mContext = holder.itemView.context
        val i = position
        var visitorposition = notificationList[position]


        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
               val activity = p0?.context as AppCompatActivity
                val bundle = Bundle()
              //  bundle.putParcelableArrayList("list",notificationList)
                val demofragment = Frag_notificationDetails()
                demofragment.arguments= bundle

                activity.supportFragmentManager.beginTransaction().replace(R.id.res,demofragment)
                    .addToBackStack(null).commit()

            }


        })




    }



    class MyViewHolder(itemView: View,listener: NotificationAdapter.OnAdapterItemClickListener) : RecyclerView.ViewHolder(itemView){

       // var delete=itemView.findViewById<ImageView>(R.id.dltNotification)


        init {
            itemView.setOnClickListener {
                listener.CancleItem(itemView, adapterPosition)
                listener.OnClick(itemView, adapterPosition)
               // itemView.setVisibility(View.GONE)

            }
        }





    }


}







