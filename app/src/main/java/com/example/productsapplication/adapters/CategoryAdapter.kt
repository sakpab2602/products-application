package com.example.productsapplication.adapters


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.provider.Settings.System.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.productsapplication.*
import com.example.productsapplication.databinding.ActivityMainBinding
import com.example.productsapplication.databinding.ActivityProductCardBinding
import com.example.productsapplication.databinding.CategoryButtonBinding
import com.example.productsapplication.models.Category
import com.example.productsapplication.models.Product
import com.example.productsapplication.services.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryAdapter(private val mActivity: Activity, private val productAdapter: ProductAdapter, private val binding: ActivityMainBinding): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class RecyclerItemCategoryButton(viewItem: CategoryButtonBinding): RecyclerView.ViewHolder(viewItem.root), View.OnClickListener{
        private val category = viewItem.categoryName.text.toString()
        private val apiService: ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        private val categoryButton = viewItem.categoryButton

        init {
            categoryButton.setOnClickListener(this)
        }

        @OptIn(DelicateCoroutinesApi::class)
        override fun onClick(view: View?) {

            when(view?.id){
                (R.id.categoryButton)->{
                    Log.d("CategoryAdapterOnClick",CategoryList.categoryList[adapterPosition].toString())
                    if(!CategoryList.categoryList[adapterPosition].isSelected){

                        for(i in 0 until CategoryList.categoryList.size){
                            if(CategoryList.categoryList[i].isSelected){
                                CategoryList.categoryList[i].isSelected=false
                                notifyItemChanged(i)
                            }
                        }
                        CategoryList.categoryList[adapterPosition].isSelected=true
                        notifyItemChanged(adapterPosition)
                        Log.d("CategoryAdapter",category)
                        binding.categoryProductLoader.visibility=View.VISIBLE
                        binding.loaderDullBackground.visibility=View.VISIBLE
                        GlobalScope.launch {

                                if(CategoryList.categoryList[adapterPosition].isAllCategory){
                                    val productRes = apiService.getProducts()
                                    if(productRes.isSuccessful){
                                        ProductList.productList = productRes.body()!!
                                    }
                                }else{
                                    val categoryRes = apiService.getCategoryProducts(CategoryList.categoryList[adapterPosition].categoryName)
                                    if(categoryRes.isSuccessful){
                                        ProductList.productList = categoryRes.body()!!
                                    }
                                }


                            mActivity.runOnUiThread(Runnable {
                                Log.d("CategoryAdapterProductList",ProductList.productList.toString())
                                productAdapter.notifyDataSetChanged()
                                binding.categoryProductLoader.visibility=View.GONE
                                binding.loaderDullBackground.visibility=View.GONE
                            })
                        }
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val categoryButtonItem =  CategoryButtonBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerItemCategoryButton(categoryButtonItem)
    }

    override fun getItemCount(): Int {
        return CategoryList.categoryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val categoryButtonHolder = (holder as RecyclerItemCategoryButton)
        val categoryButtonBorder = categoryButtonHolder.itemView.findViewById<FrameLayout>(R.id.categoryButtonBorder)
        val categoryName = categoryButtonHolder.itemView.findViewById<TextView>(R.id.categoryName)
        categoryName.text = CategoryList.categoryList[position].categoryName
        Log.d("CategoryAdapter",categoryName.text.toString())
        if(CategoryList.categoryList[position].isSelected) categoryButtonBorder.setBackgroundResource(R.drawable.category_button_selected)
        else categoryButtonBorder.setBackgroundResource(R.drawable.category_button_border)
    }
}