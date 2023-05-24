package com.michael.potcastplant

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class PlantsAdapter(private var plants : Array<PlantDashboardClass>) : RecyclerView.Adapter<PlantsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return plants.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plants_dashboard, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val PlantDashboardClass = plants[position]
        holder.imageView.setImageResource(PlantDashboardClass.image_url)
        holder.textView.text = PlantDashboardClass.title

        holder.cardView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PlantInformationActivity::class.java)
            context.startActivity(intent)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view_plant)
        val textView: TextView = itemView.findViewById(R.id.text_view_title)
        val cardView: CardView = itemView.findViewById(R.id.card_view_plants)
    }
}