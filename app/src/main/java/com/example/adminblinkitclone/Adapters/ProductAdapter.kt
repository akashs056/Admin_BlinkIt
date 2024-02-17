package com.example.adminblinkitclone.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.adminblinkitclone.Models.Product
import com.example.adminblinkitclone.databinding.SampleProductsBinding

class ProductAdapter:RecyclerView.Adapter<ProductAdapter.viewHolder>() {
    class viewHolder(val binding:SampleProductsBinding) :ViewHolder(binding.root){}

    val difutil =object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId==newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,difutil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(SampleProductsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val product=differ.currentList[position]
        holder.binding.apply {

            val imageList=ArrayList<SlideModel>()
            val productImage=product.productImageUris
            for (i in 0 until productImage!!.size){
                imageList.add(SlideModel(productImage[i].toString()))
            }
            imageSlider.setImageList(imageList)
            tvProductTitle.text=product.productTitle
            val quantity=product.productQuantity.toString()+product.productTUnit
            tvProductQuantity.text=quantity
            tvProductPrice.text="₹"+product.productPrice
        }

    }
}