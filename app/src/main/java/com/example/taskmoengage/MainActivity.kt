package com.example.taskmoengage

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmoengage.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    @Inject @ActivityContext lateinit var context : Context
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var activityMainBinding : ActivityMainBinding
    private var newsArticlesList : List<ArticlesItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainActivityViewModel.addressInputNotMutable.observe(this ){
            if(it.articles != null)
            {
                newsArticlesList = it.articles
                setUpAdapter(it.articles)
            }
            else
            {
                activityMainBinding.newsArticleRecyclerView.visibility = View.GONE
                activityMainBinding.progressIndicator.visibility = View.GONE
                Toast.makeText(context, it.status , Toast.LENGTH_SHORT).show()
            }
        }



        activityMainBinding.oldToNewButton.setOnClickListener {

            val result = newsArticlesList?.sortedBy {
                it?.publishedAt
            }
            setUpAdapter(result)
        }

        activityMainBinding.newToOldButton.setOnClickListener {

            val result = newsArticlesList?.sortedByDescending {
                it?.publishedAt
            }
            setUpAdapter(result)
        }
        mainActivityViewModel.startFetchingArticle()
    }

    private fun setUpAdapter(gotArticle : List<ArticlesItem?>?)
    {
        activityMainBinding.progressIndicator.visibility = View.VISIBLE
        val newsArticleAdapter = NewsArticleAdapter()
        newsArticleAdapter.submitList(gotArticle)
        activityMainBinding.newsArticleRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsArticleAdapter
        }
        activityMainBinding.progressIndicator.visibility = View.GONE
    }

}