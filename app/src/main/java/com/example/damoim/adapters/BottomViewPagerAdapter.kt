package com.example.damoim.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.damoim.fragments.BottomTab2ChargeFragment
import com.example.damoim.fragments.BottomTab4LocationFragment
import com.example.damoim.fragments.BottomTab1MeetFragment
import com.example.damoim.fragments.BottomTab3MyPageFragment

class BottomViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 4


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BottomTab1MeetFragment()
            1 -> BottomTab2ChargeFragment()
            2 -> BottomTab3MyPageFragment()
            else -> BottomTab4LocationFragment()
        }
    }
}