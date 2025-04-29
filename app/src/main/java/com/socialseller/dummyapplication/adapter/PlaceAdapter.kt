package com.socialseller.dummyapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialseller.clothcrew.utility.GlideHelper
import com.socialseller.dummyapplication.databinding.RowPlacesBinding
import com.socialseller.dummyapplication.room.Place

class PlaceAdapter : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    private val items = mutableListOf<Place>()

    fun submitList(data: List<Place>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class PlaceViewHolder(val binding: RowPlacesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = RowPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = items[position]
        with(holder.binding) {
            nameTv.text = place.name
            descTv.text = place.description
            GlideHelper.loadImage(profileIv, place.imageUrl)
        }
    }
}

