package com.example.factorynews

import android.os.Bundle
import androidx.appcompat.app.ActionBar

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_article.*
import java.util.ArrayList

class ArticleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val intent = intent
        val articles = intent.getParcelableArrayListExtra<Article>("articles")
        val currentItemIndex = intent.getIntExtra("currentItemIndex",0)

        val actionBar: ActionBar? = supportActionBar

        actionBar?.title = articles?.get(currentItemIndex)?.title
        view_pager.adapter = ViewPagerAdapter(articles as ArrayList<Article>, actionBar)
        view_pager.setCurrentItem(currentItemIndex,false)

    }
}