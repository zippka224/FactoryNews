package com.example.factorynews

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.time.LocalDateTime



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)

        if (compareTime(this)) {
            fetchJson(this)
        }
        else {
            fetchLocalJson()
        }
    }

    // Funkcija za dohvaćanje i prikazivanje podataka s URL-a
    private fun fetchJson(context: Context) {
        println("Prošlo 5 minuta ili ne postoje lokalni podaci...")
        runOnUiThread {
            loading_spinner.visibility = View.VISIBLE
        }

        val url = "https://newsapi.org/v1/articles?source=bbc-news&sortBy=top&apiKey=6946d0c07a1c4555a4186bfcade76398"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                // Greška: rateLimited
                if (body?.contains("rateLimited") == true) {
                    // Pop-up dialog
                    showAlertDialog(context,"rateLimited")
                }
                // Request obavljen
                else {
                    // Spremi podatke u local storage
                    writeJsonDataToAsset(context, body)

                    // Spremi vrijeme kada je request obavljen
                    writeTimeToAsset(context)

                    val gson = GsonBuilder().create()
                    val newsArticles = gson.fromJson(body, News::class.java)
                    runOnUiThread {
                        recycler_view.adapter = RecyclerViewAdapter(newsArticles.articles)
                        loading_spinner.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                // Pop-up dialog
                showAlertDialog(context,"Ups, došlo je do pogreške.")
            }
        })
    }

    // Funkcija za dohvaćanje i prikazivanje lokalnih podataka
    private fun fetchLocalJson() {
        println("Nije prošlo 5 min, učitavam lokalne podatke...")
        val jsonFileString: String? = getJsonDataFromAsset(this)
        val gson = GsonBuilder().create()
        val newsArticles = gson.fromJson(jsonFileString, News::class.java)
        loading_spinner.visibility = View.GONE
        recycler_view.adapter = RecyclerViewAdapter(newsArticles.articles)
    }


    // Funkcija za učitavanje lokalnog JSON-a
    private fun getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.openFileInput("articles.json").bufferedReader().use { it.readText() }
        }
        catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }


    // Funkcija za spremanje podataka u lokalni JSON
    private fun writeJsonDataToAsset(context: Context, articles: String?) {
        context.openFileOutput("articles.json", Context.MODE_PRIVATE).use {
            it.write(articles?.toByteArray())
        }
    }

    // Funkcija za spremanje datuma i vremena u lokalni file
    private fun writeTimeToAsset(context: Context) {
        val time = LocalDateTime.now().toString()
        context.openFileOutput("time.txt", Context.MODE_PRIVATE).use {
            it.write(time.toByteArray())
        }
    }

    // Funkcija za učitavanje vremena iz lokalnog file-a i usporedba s trenutnim vremenom
    private fun compareTime(context: Context): Boolean {
        val fetchedTimeString: String

        try {
            fetchedTimeString = context.openFileInput("time.txt").bufferedReader().use { it.readText() }
        }
        catch (ioException: IOException) {
            return true
        }

        val nowTime = LocalDateTime.now().minusMinutes(5)
        val fetchedTime = LocalDateTime.parse(fetchedTimeString)
        println("nowTime - 5 minutes: $nowTime \nfetchedTime: $fetchedTime")

      return !(nowTime < fetchedTime)
    }

    // Funkcija za prikazivanje Alert Dialoga
    private fun showAlertDialog(context: Context, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        runOnUiThread {
            alertDialogBuilder.setTitle("Greška")
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton("U REDU") { _: DialogInterface, _: Int -> fetchJson(context) }
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.show()
        }
    }
}