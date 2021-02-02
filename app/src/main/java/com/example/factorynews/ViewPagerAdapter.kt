package com.example.factorynews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_details.view.*
import java.util.ArrayList

class ViewPagerAdapter(private val articles: ArrayList<Article>, private val actionBar: ActionBar?): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_details, parent,false)
        return Pager2ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        val currentArticle = articles[position]
        holder.articleTitle.text = currentArticle.title
        holder.articleDescription.text = currentArticle.description
        Picasso.get().load(currentArticle.urlToImage).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }


    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.image_view_details
        val articleTitle: TextView = itemView.text_view_article_details
        val articleDescription: TextView = itemView.text_view_article_details_description
    }


    override fun onViewAttachedToWindow(holder: Pager2ViewHolder) {
        super.onViewAttachedToWindow(holder)
        actionBar?.title = holder.articleTitle.text
    }


}