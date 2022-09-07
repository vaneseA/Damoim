package com.example.moim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.damoim.R
import com.example.damoim.fragments.BaseFragment


class AllFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all, container,false)
    }
    override fun setupEvents() {
    }

    override fun setValues() {
    }
}