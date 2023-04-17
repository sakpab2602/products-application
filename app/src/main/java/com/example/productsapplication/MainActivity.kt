package com.example.productsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productsapplication.adapters.CategoryAdapter
import com.example.productsapplication.adapters.ProductAdapter

import com.example.productsapplication.databinding.ActivityMainBinding
import com.example.productsapplication.models.Category
import com.example.productsapplication.models.Product
import com.example.productsapplication.services.ApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //product recycler view variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    //category recycler view variables
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing the product recycler view
        recyclerView = binding.productRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //initializing the category recycler view
        categoryRecyclerView = binding.categoryRecyclerView
        categoryRecyclerView.setHasFixedSize(true)
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        var state = 0
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("MainActivityScrollState", newState.toString())
                state = newState
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                Log.d("MainActivityOnScrolled","$dx, $dy")
                if (dy > 0 && (state == 0 || state == 2)) binding.appBar.visibility = View.GONE
                else if (dy < -10) binding.appBar.visibility = View.VISIBLE
            }
        })

        val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        binding.dataLoader.visibility = View.VISIBLE
        GlobalScope.launch {
            val productResult = apiService.getProducts()
            val categoryResult = apiService.getCategories()
            if (productResult.isSuccessful) {
                ProductList.productList = productResult.body()!!
//                Log.d("MainActivity: ", productList.toString())
                runOnUiThread(Runnable {
                    productAdapter = ProductAdapter()
                    recyclerView.adapter = productAdapter
                    binding.dataLoader.visibility = View.GONE
                })
            }

            if (categoryResult.isSuccessful) {
                val res = categoryResult.body()!!
                CategoryList.categoryList.add(Category("All", true, true))
                for (i in 0 until res.size) {
                    CategoryList.categoryList.add(Category(res[i], false, false))
                }
                runOnUiThread(Runnable {
                    categoryAdapter = CategoryAdapter(this@MainActivity, productAdapter,binding)
                    categoryRecyclerView.adapter = categoryAdapter
                    binding.dataLoader.visibility = View.GONE
                })
            }
        }
    }
}