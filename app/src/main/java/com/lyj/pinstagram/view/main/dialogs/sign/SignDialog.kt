package com.lyj.pinstagram.view.main.dialogs.sign

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import com.lyj.core.base.BaseDialog
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.DialogSignBinding

typealias ChangeViewTypeCallBack = (SignViewType) -> Unit

class SignDialog(private val viewModel: SignDialogViewModel) : BaseDialog<DialogSignBinding>(
    viewModel,
    { inflater, viewGroup, _ -> DialogSignBinding.inflate(inflater, viewGroup, false) }),
    View.OnClickListener {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setTopContainerSize()
        observeLiveData()
    }
    private fun observeLiveData() {
        viewModel.currentViewType.observe(viewModel.context) { type ->
            childFragmentManager.commit {
                replace(
                    R.id.signLayout,
                    type.getFragments(viewModel.changeViewTypeCallBack)
                )
            }
        }
    }

    private fun setTopContainerSize() {
        binding.signContainer.layoutParams.apply {
            width = viewModel.getDimen(R.dimen.sign_dialog_width).toInt()
            height = viewModel.getDimen(R.dimen.sign_dialog_height).toInt()
        }
    }

    override fun onClick(v: View) {
        dismiss()
    }
}
