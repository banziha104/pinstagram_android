package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lyj.core.base.BaseAdapter
import com.lyj.core.extension.android.base.resColor
import com.lyj.domain.model.TalkModel
import com.lyj.pinstagram.R


class TalkAdapter(val viewModel: TalkAdapterViewModel) : BaseAdapter<TalkModel,TalkAdapter.TalkViewHolder>(viewModel) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_talk,parent,false)
        return TalkViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.itemCount

    override fun onBindViewHolder(holder: TalkViewHolder, position: Int) {
        holder.apply {
            viewModel.items[position].let{ item ->
                userName.text = item.name
                message.text = item.text
                (cardview.layoutParams as ConstraintLayout.LayoutParams).apply {
                    if (viewModel.authData != null && viewModel.authData!!.id == item.userId.toInt()){
                        startToStart = -1
                        endToEnd = cloneEndToEnd
                        cardview.setCardBackgroundColor(resColor(R.color.white_red))
                        userName.setTextColor(resColor(R.color.white_gray))
                        message.setTextColor(resColor(R.color.white))
                    }else{
                        cardview.setCardBackgroundColor(resColor(R.color.cardview_background))
                        userName.setTextColor(resColor(R.color.black_gray))
                        message.setTextColor(resColor(R.color.black))
                        endToEnd = -1
                        startToStart = cloneStartToStart
                    }
                }
            }
        }
    }

    inner class TalkViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val cardview : CardView = view.findViewById<CardView>(R.id.itemTalkCard)
        val userName : TextView = view.findViewById<TextView>(R.id.itemTalkTxtUserName)
        val message : TextView = view.findViewById<TextView>(R.id.itemTalkTxtMessage)
        val cloneStartToStart = (cardview.layoutParams as ConstraintLayout.LayoutParams).startToStart
        val cloneEndToEnd = (cardview.layoutParams as ConstraintLayout.LayoutParams).endToEnd
    }
}
