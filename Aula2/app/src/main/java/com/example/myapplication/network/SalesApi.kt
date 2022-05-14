package com.example.myapplication.network

/* Imports */
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

/* Const */
private const val BASE_URL = "https://sales-provider.appspot.com"

/* Config serialize */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()) // Config to moshi accept json content
    .build() // Apply

/* Config http client */
private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS) // Set the max time to connect
    .readTimeout(60, TimeUnit.SECONDS) // Set the max time to read
    .writeTimeout(60, TimeUnit.SECONDS) // Set the max time to write
    .addInterceptor(OauthTokenInterceptor()) // Interceptor to add token
    .authenticator(OauthTokenAuthenticator()) // Verify if auth fails and do something
    .build() // Apply

/* Config retrofit */
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL) // Define the base url
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // Declare the serialize
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) // Allow the req async using CoroutineCallAdapterFactory
    .client(okHttpClient) // Define the http client
    .build() // Apply

interface SalesApiService {
    // api/products - Endpoint
    // Deferred - Async (I can wait, so in your time)
    // List<Product> - List of Product
    @GET("api/products")
    fun getProducts(): Deferred<List<Product>>

    // api/products - Endpoint
    // Deferred - Async (I can wait, so in your time)
    // Product - A specific product
    @GET("api/products/{code}")
    fun getProductByCode(
        @Path("code") code: String,
    ): Deferred<Product>

    // oauth/token - Endpoint
    // Call - Sync (I want the response, STOP the process and give me the answer)
    @POST("oauth/token")
    @FormUrlEncoded
    fun getToken(
        @Header("Authorization") basicAuthentication: String,
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<OauthTokenResponse>

}

object SalesApi {
    val retrofitService: SalesApiService by lazy {
        retrofit.create(SalesApiService::class.java)
    }
}