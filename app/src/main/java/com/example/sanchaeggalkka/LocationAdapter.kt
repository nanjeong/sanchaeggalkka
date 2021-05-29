package com.example.sanchaeggalkka

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sanchaeggalkka.databinding.LocationItemViewBinding
import com.example.sanchaeggalkka.db.Loc

class LocationAdapter(val clickListener: LocationListener) : ListAdapter<Loc, LocationAdapter.ViewHolder>(LocationDiffCallback()) {

    class ViewHolder(val binding: LocationItemViewBinding, nx: Int, ny: Int) : RecyclerView.ViewHolder(binding.root) {
        val nx = nx
        val ny = ny
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.location_item_view, parent, false)
        val binding = LocationItemViewBinding.inflate(layoutInflater, parent, false)

        val spX = view.context.getSharedPreferences("currentLocationX", Context.MODE_PRIVATE)
        val spY = view.context.getSharedPreferences("currentLocationY", Context.MODE_PRIVATE)

        val nx = spX.getInt("nx", 60)
        val ny = spY.getInt("ny", 127)

        return ViewHolder(binding, nx, ny)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.location = item
        holder.binding.clickListener = clickListener
        holder.binding.locName.text = item.lcName
        if (holder.nx == item.x && holder.ny == item.y) {
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