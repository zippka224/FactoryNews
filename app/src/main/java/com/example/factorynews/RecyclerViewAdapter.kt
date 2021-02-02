package com.example.factorynews


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_row.view.*



class RecyclerViewAdapter(private val articles: List<Article>): RecyclerView.Adapter<RecyclerViewAdapter.RowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_row, parent,false)
        return RowViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val currentArticle = articles[position]
        holder.textView.text = currentArticle.title
        Picasso.get().load(currentArticle.urlToImage).into(holder.imageView)
        holder.currentItemIndex = position
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class RowViewHolder(itemView: View, var currentItemIndex: Int? = null): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.image_view
        val textView: TextView = itemView.text_view

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ArticleActivity::class.java)
                val arrayListArticles = ArrayList(articles)
                intent.putExtra("articles", arrayListArticles)
                intent.putExtra("currentItemIndex", currentItemIndex)
                itemView.context.startActivity(intent)

            }
        }

    }


}