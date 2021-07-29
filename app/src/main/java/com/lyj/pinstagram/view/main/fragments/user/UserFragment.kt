package com.lyj.pinstagram.view.main.fragments.user

import androidx.fragment.app.viewModels
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.UserFragmentBinding

class UserFragment private constructor() : BaseFragment<UserFragmentViewModel,UserFragmentBinding>(R.layout.user_fragment) {

    companion object{
        val instance : UserFragment by lazy { UserFragment() }
    }
    override val viewModel: UserFragmentViewModel by viewModels()


}