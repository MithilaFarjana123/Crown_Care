package com.crowncement.crowncement_complain_management.data.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.API.Endpoint
import com.crowncement.crowncement_complain_management.data.Model.GetEmpName

class EmpInfoAdapter(

    context: Context,
    private val items: List<GetEmpName>
) : ArrayAdapter<GetEmpName>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.emp_item, parent, false
        )

        val txtEmpName = view.findViewById<TextView>(R.id.txtEmpName)
        val txtDesignation = view.findViewById<TextView>(R.id.txtDesignation)
        val emImage = view.findViewById<ImageView>(R.id.emImage)

        txtEmpName.text = item?.empName
        txtDesignation.text = item?.empDesig


        if (item!!.empImg.toString().isNotEmpty()) {
            Glide
                .with(context)
                //  .load(Endpoint.IMAGE_BASE_URL + "/da/docs/x880022/" + item!!.empImg.toString())
                .load(Endpoint.IMAGE_BASE_URL + item!!.empImg.toString())
                .error(R.drawable.human)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(emImage)

        }




        return view
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<GetEmpName>()
            // val filteredList = mutableListOf<String>()
            if (constraint == null || constraint.isEmpty()) {

                filteredList.addAll(items)

//                for (value in items ){
//                    filteredList.add(value.empName.toString())
//                }

            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in items) {
                    if (item.empName!!.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            return FilterResults().apply {
                values = filteredList
                count = filteredList.size
            }
        }


        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results?.values as List<GetEmpName>)
            notifyDataSetChanged()
        }
    }

}