package com.lyj.pinstagram.view.main.fragments.talk

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.core.base.BaseFragment
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.TalkFragmentBinding
import com.lyj.pinstagram.view.main.fragments.user.UserFragment

class TalkFragment : BaseFragment<TalkFragmentViewModel,TalkFragmentBinding>(R.layout.talk_fragment) {

    companion object{
        val instance : TalkFragment by lazy { TalkFragment() }
    }

    override val viewModel: TalkFragmentViewModel by viewModels()


}