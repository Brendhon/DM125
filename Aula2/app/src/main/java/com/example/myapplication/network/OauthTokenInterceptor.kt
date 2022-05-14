package com.example.myapplication.network

import android.util.Log
import com.example.myapplication.utils.SharedPreferenceUtils
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "OauthTokenInterceptor"

class OauthTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        Log.i(TAG, "Fetching access token from shared preferences")

        /* Get Token */
        val accessToken = SharedPreferenceUtils.getAccessToken()

        /* Verify if token exist */
        if (accessToken !== null) {
            Log.i(TAG, "Using the existing token")

            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        return chain.proceed(request)
    }

}
