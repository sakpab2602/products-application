package com.example.productsapplication.services


import android.database.Observable
import com.example.productsapplication.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("products")
    suspend fun getProducts(): Response<ArrayList<Product>>

    @GET("products/categories")
    suspend fun getCategories(): Response<ArrayList<String>>

    @GET("products/category/{category}")
    suspend fun getCategoryProducts(@Path("category") category: String): Response<ArrayList<Product>>
}