package com.lyj.pinstagram.view.main.fragments.talk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lyj.pinstagram.R


class TalkAdapter(val viewModel: TalkAdapterViewModel) : RecyclerView.Adapter<TalkAdapter.TalkViewHolder>(){

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
                        cardview.setCardBackgroundColor(ContextCompat.getColor(viewModel.context,R.color.white_red))
                        userName.setTextColor(ContextCompat.getColor(viewModel.context,R.color.white_gray))
                        message.setTextColor(ContextCompat.getColor(viewModel.context,R.color.white))
                    }else{
                        cardview.setCardBackgroundColor(ContextCompat.getColor(viewModel.context,R.color.cardview_background))
                        userName.setTextColor(ContextCompat.getColor(viewModel.context,R.color.black_gray))
                        message.setTextColor(ContextCompat.getColor(viewModel.context,R.color.black))
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
