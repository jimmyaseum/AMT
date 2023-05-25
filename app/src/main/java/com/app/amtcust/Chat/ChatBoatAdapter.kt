package com.app.amtcust.Chat

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ChatBoatAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
//                val chatFragment = ChatFragment()
//                return chatFragment
                val groupFragment = GroupFragment()
                return groupFragment
            }
            else -> {
                val groupFragment = GroupFragment()
                return groupFragment
            }
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}