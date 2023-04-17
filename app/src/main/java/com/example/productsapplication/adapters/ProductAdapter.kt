package com.example.productsapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.productsapplication.ProductList
import com.example.productsapplication.R
import com.example.productsapplication.databinding.ActivityProductCardBinding
import com.example.productsapplication.models.Product

class ProductAdapter: RecyclerView.Adapter<ViewHolder>() {

    inner class RecyclerItemProduct(viewItem: ActivityProductCardBinding): ViewHolder(viewItem.root){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val prodCardItem =  ActivityProductCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerItemProduct(prodCardItem)
    }

    override fun getItemCount(): Int {
        return ProductList.productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prodDetailCard = holder.itemView.findViewById<ConstraintLayout>(R.id.prodDetailCard)
        val prodImage = holder.itemView.findViewById<ImageView>(R.id.prodImage)
        val rating = holder.itemView.findViewById<TextView>(R.id.rating)
        val prodTitle = holder.itemView.findViewById<TextView>(R.id.prodTitle)
        val prodDesc = holder.itemView.findViewById<TextView>(R.id.prodDescription)
        val prodPrice = holder.itemView.findViewById<TextView>(R.id.productPrice)
        val imageUrl = ProductList.productList[position].image
        prodDetailCard.visibility = View.VISIBLE
        Glide.with(holder.itemView.context).asBitmap().load(imageUrl).into(BitmapImageViewTarget(prodImage))
        rating.text = "4.4"
        prodTitle.text = ProductList.productList[position].title
        prodDesc.text = ProductList.productList[position].description
        prodPrice.text = "₹ ${ProductList.productList[position].price}"
    }
}