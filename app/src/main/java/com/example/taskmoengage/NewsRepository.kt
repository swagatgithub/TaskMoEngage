package com.example.taskmoengage

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class NewsRepository @Inject constructor( @ApplicationContext val context : Context)
{
     fun fetchArticle() : StaticNewsResponse
     {
         lateinit var returnValue : StaticNewsResponse

             var urlConnection: HttpsURLConnection? = null
             try {
                 val url =
                     URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
                 urlConnection = url.openConnection() as HttpsURLConnection
                 val responseCode = urlConnection.responseCode
                 if (responseCode != 200) {
                     throw IOException("Error Has Happened..$responseCode")
                 }
                 val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                 val jsonStringHolder = StringBuilder()
                 while (true) {
                     val readLine = bufferedReader.readLine() ?: break
                     jsonStringHolder.append(readLine)
                 }
                 returnValue =
                     Gson().fromJson(jsonStringHolder.toString(), StaticNewsResponse::class.java)
             } catch (throwable: Throwable) {
                 throwable.printStackTrace()
                 returnValue = StaticNewsResponse(status = context.getString(R.string.requestFailed))
             } finally {
                 urlConnection?.disconnect()
             }
         return returnValue
     }
}