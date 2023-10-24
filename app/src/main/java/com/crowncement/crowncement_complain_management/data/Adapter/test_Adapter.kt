package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.Fragment.Frag_details
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint.IMAGE_BASE_URL
import com.crowncement.crowncement_complain_management.common.Utility
import com.crowncement.crowncement_complain_management.data.Model.Complain
import com.crowncement.crowncement_complain_management.data.Model.GetComplainData
import kotlinx.android.synthetic.main.dashboardchild_underway.view.imgComplain

class test_Adapter(val complainList : List<GetComplainData>) : RecyclerView.Adapter<Dasgbord_OngoingAdapter.MyViewHolder>() {


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


        var selectedDate =complainList[position].trnDate

        holder.do_comSubDate.text= Utility.changeDateFormat(
            selectedDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )
/*
        holder.txtExpectedResolveDate.text= Utility.changeDateFormat(
            complainList[position].expectedResolvDate,
            "yyyy-MM-dd",
            "MMM dd,yyyy"
        )

 */

        holder.do_comTitle.text = complainList[position].reqTitle
        val p = complainList[position].follwAct.size-1
        holder.do_comHandleby.text=complainList[position].follwAct[p].repToName


        if (complainList[position].reqImg!="") {

            Glide
                .with(mContext)
                .load(IMAGE_BASE_URL+complainList[position].reqImg)
                .error(R.drawable.complain)
                .centerInside()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.complain)
                .into(holder.itemView.imgComplain)
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val activity = view?.context as AppCompatActivity
                val demofragment = Frag_details()

                // Create a Bundle to hold the position value
                val bundle = Bundle()
                bundle.putInt("position", holder.adapterPosition) // Assuming adapterPosition holds the position

                // Attach the Bundle to the fragment
                demofragment.arguments = bundle

                // Replace the current fragment with the destination fragment
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.dashbord_fragment, demofragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

    }



    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val do_comSubDate = itemView.findViewById<TextView>(R.id.do_comSubDate)
        val do_comTitle = itemView.findViewById<TextView>(R.id.do_comTitle)
        val do_comHandleby = itemView.findViewById<TextView>(R.id.do_comHandleby)
        val txtExpectedResolveDate = itemView.findViewById<TextView>(R.id.txtExpectedResolveDate)

    }



}