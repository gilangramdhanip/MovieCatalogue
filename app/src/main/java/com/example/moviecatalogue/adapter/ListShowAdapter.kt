package com.example.moviecatalogue.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.moviecatalogue.activity.DetailTvShowActivity
import com.example.moviecatalogue.R
import com.example.moviecatalogue.model.Moviews
import com.example.moviecatalogue.model.TvShow
import kotlinx.android.synthetic.main.item_row_hero.view.*

class ListShowAdapter (private val listShow: ArrayList<TvShow>): RecyclerView.Adapter<ListShowAdapter.ListViewHolder>(), Filterable {

    internal var filterListResult: ArrayList<TvShow>

    init{
        this.filterListResult = listShow
    }

    fun setData(items: ArrayList<TvShow>){
        listShow.clear()
        listShow.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(show: TvShow) {
            with(itemView){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w185${show.poster_path}")
                    .apply(RequestOptions().override(200, 250))
                    .into(img_item_photo)
//                img_item_photo.setImageResource(show.photo)
                tv_item_name.text = show.name
                tv_item_description.text = show.overview
                tv_rating.text = show.vote_average.toString()

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailTvShowActivity::class.java)
                    intent.putExtra(DetailTvShowActivity.EXTRA_SHOW, show)
                    context.startActivity(intent)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_hero, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = filterListResult.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(filterListResult[position])
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun goToDetailShow(data: TvShow)
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty())
                    filterListResult = listShow
                else {
                    val resultlist = ArrayList<TvShow>()
                    for (row in listShow) {
                        if (row.name.toLowerCase().contains(charSearch.toLowerCase()))
                            resultlist.add(row)
                    }
                    filterListResult = resultlist
                }
                val filterResult = Filter.FilterResults()
                filterResult.values = filterListResult
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterListResult = results!!.values as ArrayList<TvShow>
                notifyDataSetChanged()
            }

        }
    }


}