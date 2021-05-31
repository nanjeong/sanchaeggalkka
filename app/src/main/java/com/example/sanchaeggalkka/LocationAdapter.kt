package com.example.sanchaeggalkka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sanchaeggalkka.databinding.LocationItemViewBinding
import com.example.sanchaeggalkka.db.Loc

class LocationAdapter(val clickListener: LocationListener, val moreListener: MoreListener) : ListAdapter<Loc, LocationAdapter.ViewHolder>(LocationDiffCallback()) {

    class ViewHolder(val binding: LocationItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationItemViewBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.location = item
        holder.binding.clickListener = clickListener
        holder.binding.moreListener = moreListener
        holder.binding.locName.text = item.lcName

        if (item.current == 1) {
            holder.binding.check.visibility = View.VISIBLE
        } else {
            holder.binding.check.visibility = View.GONE
        }
    }
}

class LocationDiffCallback : DiffUtil.ItemCallback<Loc>() {
    override fun areItemsTheSame(oldItem: Loc, newItem: Loc): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Loc, newItem: Loc): Boolean {
        return oldItem == newItem
    }
}

class LocationListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(location: Loc) = clickListener(location.id)
}

class MoreListener(val moreClickListener: (id: Long) -> Unit) {
    fun onClick(location: Loc) = moreClickListener(location.id)
}