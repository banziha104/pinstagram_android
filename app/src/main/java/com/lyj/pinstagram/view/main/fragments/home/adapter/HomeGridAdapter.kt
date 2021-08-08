package com.lyj.pinstagram.view.main.fragments.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lyj.pinstagram.R
import com.lyj.pinstagram.view.main.fragments.home.HomeFragment


class HomeGridAdapter(private val viewModel : HomeGridViewModel) : RecyclerView.Adapter<HomeGridAdapter.HomeGridViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_grid,parent,false)
        return HomeGridViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.itemCount

    override fun onBindViewHolder(holder: HomeGridViewHolder, position: Int) {
        holder.apply {
            viewModel.items[position].let { item ->
                container.layoutParams.height = viewModel.height
                Glide
                    .with(viewModel.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView)

                container.setOnClickListener {
                    viewModel.onItemClick(item)
                }
            }
        }
    }


    inner class HomeGridViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageView : ImageView = view.findViewById<ImageView>(R.id.homeItemImageView)
        val container : ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.homeItemContainer)
    }
}