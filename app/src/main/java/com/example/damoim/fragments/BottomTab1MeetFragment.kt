package com.example.damoim.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.damoim.CreateActivity
import com.example.damoim.MainActivity
import com.example.damoim.R
import com.example.damoim.adapters.ViewPagerFragmentStateAdapter
import com.example.damoim.databinding.FragmentBottomTab1MeetBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_bottom_tab1_meet.*

class BottomTab1MeetFragment : BaseFragment() {
    lateinit var binding: FragmentBottomTab1MeetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. View Binding 설정
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_tab1_meet, container, false)

        // 2. View Pager의 FragmentStateAdapter 설정
        binding.viewPager.adapter = activity?.let { ViewPagerFragmentStateAdapter(it) }

        // 3. View Pager의 Orientation 설정
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 4. TabLayout + ViewPager2 연동 (ViewPager2에 Adapter 연동 후에)
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        setupEvents()
        setValues()
        // 5. return Fragment Layout View
        return binding.root

    }
    override fun setupEvents() {
        binding.meettingAddBtn.setOnClickListener {
            startActivity(Intent(mContext,CreateActivity::class.java))
        }
    }

    override fun setValues() {

    }
    // Tab & ViewPager 연동 및 Tab title 설정
    private fun getTabTitle(position: Int): String? {
        return when (position) {
            0 -> "전체"
            1 -> "아웃도어/   여행"
            2 -> "운동/스포츠"
            else -> "인문학/책/글"
        }
    }

}