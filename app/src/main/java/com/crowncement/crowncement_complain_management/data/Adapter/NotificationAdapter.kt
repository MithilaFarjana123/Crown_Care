package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

      //  holder.n_u_desig.text = notificationList[position].r

        holder.n_u_dep.text = notificationList[position].deptNam

        var img = notificationList[position].reqImg.toString()

        if (img.isNotEmpty()) {
            //ToDo Image
            Glide
                .with(mContext)
              //  .load(IMAGE_BASE_URL + img)
                .load(Endpoint.IMAGE_BASE_URL +  img)
                .error(R.drawable.human)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.human)
                //  .transform(RoundedCorners(30,30.0))
                .into(holder.itemView.n_img)
        }


        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
               val activity = p0?.context as AppCompatActivity
                val bundle = Bundle()
                bundle.putInt("position", holder.adapterPosition) // Assuming adapterPosition holds the position

                //  bundle.putParcelableArrayList("list",notificationList)
                val demofragment = Frag_notificationDetails()
                demofragment.arguments= bundle

              //  demofragment.arguments = bundle

                // Replace the current fragment with the destination fragment
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.res, demofragment)
                    .addToBackStack(null)
                    .commit()

//                activity.supportFragmentManager.beginTransaction().replace(R.id.res,demofragment)
//                    .addToBackStack(null).commit()

            }


        })




    }



    class MyViewHolder(itemView: View,listener: NotificationAdapter.OnAdapterItemClickListener) : RecyclerView.ViewHolder(itemView){

        var n_img=itemView.findViewById<ImageView>(R.id.n_img)
        var n_title = itemView.findViewById<TextView>(R.id.n_title)
        var n_u_name = itemView.findViewById<TextView>(R.id.n_u_name)
        var n_u_desig = itemView.findViewById<TextView>(R.id.n_u_desig)
        var n_u_dep = itemView.findViewById<TextView>(R.id.n_u_dep)


        init {
            itemView.setOnClickListener {
                listener.CancleItem(itemView, adapterPosition)
                listener.OnClick(itemView, adapterPosition)
               // itemView.setVisibility(View.GONE)

            }
        }





    }


}







