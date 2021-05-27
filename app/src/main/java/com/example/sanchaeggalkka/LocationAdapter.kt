package com.example.sanchaeggalkka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sanchaeggalkka.db.Loc

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    var data = listOf<Loc>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locName: TextView = view.findViewById(R.id.loc_name)
        val check: ImageView = view.findViewById(R.id.check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.locName.text = item.lcName

        if (item.current == 1) {
            holder.check.visibility = View.VISIBLE
        } else {
            holder.check.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = data.size
}