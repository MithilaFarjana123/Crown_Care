package com.crowncement.crowncement_complain_management.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.recyclerview.widget.RecyclerView
import com.crowncement.crowncement_complain_management.R
import com.crowncement.crowncement_complain_management.common.Utility
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.frag_dashboard.*
import kotlinx.android.synthetic.main.frag_dashboard.view.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Frag_dashboard : Fragment() {

    lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.frag_dashboard, container, false)

        // Inflate the layout for this fragment
        eventClickListener()
        return rootView

    }


    fun eventClickListener(){
        rootView.btn_logout.setOnClickListener {
            logoutBottomSheet()
        }
    }

    private fun logoutBottomSheet() {
        val v: View = layoutInflater.inflate(R.layout.confirm_logout, null)
        val dialog = BottomSheetDialog(requireContext())
        Objects.requireNonNull(dialog.window)
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val btnYes: Button = v.findViewById(R.id.btnYes)
        val btnNo: Button = v.findViewById(R.id.btnNo)
        dialog.setContentView(v)
        dialog.setCancelable(false)
        dialog.show()
        btnNo.setOnClickListener {

            dialog.hide()
        }
        btnYes.setOnClickListener {
            // super.onBackPressed()


            btnYes.isEnabled = false
            Utility.removePreference(requireContext(), "username")
            Utility.removePreference(requireContext(), "pass")
            Utility.removePreference(requireContext(), "UserInfo")
            // Utility.clearCache(this)
//            context?.applicationContext.finishAffinity()

            requireActivity().finish()

        }


    }




}