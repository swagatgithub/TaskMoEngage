package com.example.taskmoengage

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskmoengage.databinding.ParticularNewsItemLayoutBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class NewsArticleAdapter : androidx.recyclerview.widget.ListAdapter<ArticlesItem , NewsArticleAdapter.NewsArticleViewHolder>(NewsArticleDiffCallBack)
{
    object NewsArticleDiffCallBack : DiffUtil.ItemCallback<ArticlesItem>()
    {
        override fun areItemsTheSame(
            oldItem: ArticlesItem,
            newItem: ArticlesItem
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: ArticlesItem,
            newItem: ArticlesItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    class NewsArticleViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val newsArticleImage : ShapeableImageView = view.findViewById(R.id.newsArticleImage)
        val newsArticleTitle : MaterialTextView = view.findViewById(R.id.newsArticleTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        return NewsArticleViewHolder(ParticularNewsItemLayoutBinding.inflate(LayoutInflater.from(parent.context) , parent , false).root)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int)
    {
        Glide.with(holder.itemView).load(getItem(position).urlToImage).placeholder(R.drawable.baseline_insert_photo_24).into(holder.newsArticleImage)

        holder.newsArticleTitle.text = getItem(position).title

        holder.newsArticleTitle.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).url))
                startActivity(holder.newsArticleTitle.context , browserIntent , null)
            }
            catch (exception : Exception)
            {
                exception.printStackTrace()
            }
        }
    }

}