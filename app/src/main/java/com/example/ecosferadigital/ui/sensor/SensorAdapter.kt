package com.example.ecosferadigital.ui.sensor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosferadigital.databinding.ItemSensorBinding
import com.example.ecosferadigital.models.Sensor

class SensorAdapter(
    private val sensors: List<Sensor>,
    private val onEdit: (Sensor) -> Unit,
    private val onDelete: (Sensor) -> Unit
) : ListAdapter<Sensor, SensorAdapter.SensorViewHolder>(SensorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        val binding = ItemSensorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SensorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensor = getItem(position)
        holder.bind(sensor)
    }

    inner class SensorViewHolder(private val binding: ItemSensorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sensor: Sensor) {
            binding.tvSensorName.text = sensor.name
            binding.btnEdit.setOnClickListener { onEdit(sensor) }
            binding.btnDelete.setOnClickListener { onDelete(sensor) }
        }
    }

    class SensorDiffCallback : DiffUtil.ItemCallback<Sensor>() {
        override fun areItemsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem == newItem
        }
    }
}
