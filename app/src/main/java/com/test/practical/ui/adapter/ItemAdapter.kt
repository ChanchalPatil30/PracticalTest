package com.test.practical.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.practical.databinding.AdapterItemBinding
import com.test.practical.model.Items

class ItemAdapter : RecyclerView.Adapter<MainViewHolder>() {
    var itemsList = mutableListOf<Items>()

    var selected = -1;
    fun setItems(items: List<Items>) {
        this.itemsList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        if(selected == position){
            holder.binding.fullData.visibility = View.VISIBLE
        } else{
            holder.binding.fullData.visibility = View.GONE
        }

        val items = itemsList[position]
            holder.binding.fullName.text = items.fullName
            holder.binding.description.text = items.description
            holder.binding.languege.text = items.language
            holder.binding.stared.text = items.stargazersCount.toString()
            holder.binding.watchers.text = items.forksCount.toString()
            Glide.with(holder.itemView.context).load(items.owner?.avatarUrl).into(holder.binding.tabImage)

        holder.binding.tap.setOnClickListener(View.OnClickListener {
            selected = if(selected == position){
                -1
            } else {
                position
            }
            notifyDataSetChanged()
        })

    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}

class MainViewHolder(val binding: AdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {

}