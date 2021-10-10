package com.lyj.pinstagram.view.main.dialogs.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.lyj.core.extension.base.resDimen
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogSignBinding

typealias ChangeViewTypeCallBack = (SignViewType) -> Unit

class SignDialog :
DialogFragment(),
    View.OnClickListener {

    private val viewModel: SignDialogViewModel by viewModels()
    private lateinit var binding : DialogSignBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSignBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopContainerSize()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.currentViewType.observe(viewLifecycleOwner) { type ->
            childFragmentManager.commit {
                replace(
                    R.id.signLayout,
                    type.getFragments(viewModel.changeViewTypeCallBack){ dismiss() }
                )
            }
        }
    }

    private fun setTopContainerSize() {
        binding.signContainer.layoutParams.apply {
            width = resDimen(R.dimen.sign_dialog_width).toInt()
            height = resDimen(R.dimen.sign_dialog_height).toInt()
        }
    }

    override fun onClick(v: View) {
        dismiss()
    }
}
