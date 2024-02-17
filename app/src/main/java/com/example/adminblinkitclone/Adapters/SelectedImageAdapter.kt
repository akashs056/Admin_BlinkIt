package com.example.adminblinkitclone.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.adminblinkitclone.databinding.SampleSelectedImageBinding

class SelectedImageAdapter(val imageUri :ArrayList<Uri>):RecyclerView.Adapter<SelectedImageAdapter.viewHolder>() {
    class viewHolder(val binding:SampleSelectedImageBinding) : ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(SampleSelectedImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return imageUri.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val  images=imageUri[position]
        holder.binding.apply {
            image.setImageURI(images)
        }
        holder.binding.remove.setOnClickListener {
            if (position<imageUri.size){
                imageUri.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

}