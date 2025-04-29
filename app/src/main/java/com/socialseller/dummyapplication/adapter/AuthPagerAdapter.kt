package com.socialseller.dummyapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.socialseller.dummyapplication.ui.auth.AuthActivity
import com.socialseller.dummyapplication.ui.auth.LoginFragment
import com.socialseller.dummyapplication.ui.auth.SignUpFragment


class AuthPagerAdapter(fragmentActivity: AuthActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> SignUpFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
