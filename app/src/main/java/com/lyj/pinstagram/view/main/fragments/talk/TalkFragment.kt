package com.lyj.pinstagram.view.main.fragments.talk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lyj.api.socket.SocketContract
import com.lyj.api.socket.TalkMessage
import com.lyj.core.base.BaseFragment
import com.lyj.core.extension.android.fromStartToStopScope
import com.lyj.core.extension.lang.plusAssign
import com.lyj.core.extension.lang.testTag
import com.lyj.core.rx.DisposableFunction
import com.lyj.core.rx.plusAssign
import com.lyj.pinstagram.R
import com.lyj.pinstagram.databinding.TalkFragmentBinding
import com.lyj.pinstagram.view.main.MainActivityViewModel
import com.lyj.pinstagram.view.main.fragments.talk.adapter.TalkAdapter
import com.lyj.pinstagram.view.main.fragments.talk.adapter.TalkAdapterViewModel
import com.lyj.pinstagram.view.main.fragments.talk.adapter.plusAssign
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class TalkFragment() : BaseFragment<TalkFragmentViewModel, TalkFragmentBinding>(
    R.layout.talk_fragment,
    { layoutInflater, viewGroup ->
        TalkFragmentBinding.inflate(
            layoutInflater,
            viewGroup,
            false
        )
    }
) {

    private val mainViewModel: MainActivityViewModel by activityViewModels()

    override val viewModel: TalkFragmentViewModel by viewModels()

    private var adapterViewModel: TalkAdapterViewModel? = null

    private lateinit var socketContact: SocketContract

    private var currentText: String = ""

    private val inputMethodManager: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectSocket()
        observeObservable()
        observeLiveData()
    }

    private fun connectSocket() {
        socketContact = viewModel.createSocket(lifecycle)
        socketContact.connect().observeOn(AndroidSchedulers.mainThread()).subscribe({

        }, {
            Toast.makeText(requireContext(), R.string.talk_socket_warning, Toast.LENGTH_LONG).show()
            it.printStackTrace()
        })
    }

    private fun observeLiveData() {
        mainViewModel
            .currentAuthData
            .observe(viewLifecycleOwner) {
                Log.d(testTag, "auth Changed ${it} ${adapterViewModel}")
                if (adapterViewModel == null) {
                    adapterViewModel = TalkAdapterViewModel(mutableListOf(), requireContext(),scopes)
                }
                adapterViewModel!!.authData = it
                binding.talkRecyclerView.adapter?.notifyDataSetChanged()
            }
    }

    private fun observeObservable() {
        fromStartToStopScope += observeBtnSend()
        fromStartToStopScope += observeEditTextChange()
        fromStartToStopScope += observeSayObserver()
        fromStartToStopScope += observeGetAllMessage()
    }

    private fun observeSayObserver(): DisposableFunction = {
        socketContact
            .getSayObserver()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lifecycleScope.launch(Dispatchers.Main) {
                    adapterViewModel += it
                    binding.talkRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
    }

    private fun observeBtnSend(): DisposableFunction = {
        val contact: TalkSendContact = (activity as? TalkSendContact) ?: throw NotImplementedError()
        contact
            .btnSendObserver
            .throttleFirst(1, TimeUnit.SECONDS)
            .filter {
                val auth = mainViewModel.currentAuthData.value
                if (auth != null && auth.isValidated) {
                    true
                } else {
                    Toast.makeText(requireContext(), R.string.main_needs_auth, Toast.LENGTH_LONG)
                        .show()
                    false
                }
            }
            .filter {
                currentText.isNotBlank()
            }
            .map { mainViewModel.currentAuthData.value!! }
            .subscribe {
                val message = TalkMessage.withAuth(it, currentText)
                if (message != null) {
                    socketContact.sendMessage(message)

                    contact.clearText()
                    currentText = ""
                    inputMethodManager.hideSoftInputFromWindow(
                        requireActivity().currentFocus!!.windowToken,
                        0
                    )
                }
            }
    }

    private fun observeEditTextChange(): DisposableFunction = {
        val contact: TalkSendContact = (activity as? TalkSendContact) ?: throw NotImplementedError()
        contact
            .editTextObserver
            .subscribe { currentText = it }
    }

    private fun observeGetAllMessage(): DisposableFunction = {
        viewModel
            .getAllTalkMessage()
            .retry(3)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isOk && it.data != null && it.data!!.isNotEmpty()) {
                    if (adapterViewModel != null) {
                        adapterViewModel?.items = it.data!!.toMutableList()
                    } else {
                        adapterViewModel =
                            TalkAdapterViewModel(it.data!!.toMutableList(), requireContext(),scopes)
                    }
                    binding.talkRecyclerView.apply {
                        adapter = TalkAdapter(adapterViewModel!!)
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter?.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.talk_api_warning, Toast.LENGTH_LONG)
                        .show()
                }
            }, {
                Toast.makeText(requireContext(), R.string.talk_api_warning, Toast.LENGTH_LONG)
                    .show()
                it.printStackTrace()
            })
    }
}

interface TalkSendContact {
    val editTextObserver: Observable<String>
    val btnSendObserver: Observable<Unit>
    val clearText: () -> Unit
}