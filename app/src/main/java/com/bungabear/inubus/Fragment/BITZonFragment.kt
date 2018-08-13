package com.bungabear.inubus.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bungabear.inubus.R

/**
 * Created by Minjae Son on 2018-08-13.
 */

class BITZonFragment : Fragment(){
    private lateinit var mContext : Context

    companion object {
        fun newInstance(context: Context): BITZonFragment {
            val fragment = BITZonFragment()
            fragment.mContext = context
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bitzon, container, false)
        return view
    }
}