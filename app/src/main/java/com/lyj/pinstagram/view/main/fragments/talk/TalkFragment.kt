package com.lyj.pinstagram.view.main.fragments.talk

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.user.UserFragment

class TalkFragment : Fragment() {

    companion object{
        val instance : TalkFragment by lazy { TalkFragment() }
    }

    private val viewModel: TalkFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.talk_fragment, container, false)
    }

}