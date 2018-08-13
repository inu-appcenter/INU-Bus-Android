package com.bungabear.inubus.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bungabear.inubus.R

/**
 * Created by Minjae Son on 2018-08-13.
 */

class DestinationFragment : Fragment() {

    private lateinit var mFm: FragmentManager
    private lateinit var mContext : Context

    companion object {
        fun newInstance(fm : FragmentManager, context : Context) : DestinationFragment{
            val fragment = DestinationFragment()
            fragment.mFm = fm
            fragment.mContext = context
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_destination, container, false)
        return view
    }
}