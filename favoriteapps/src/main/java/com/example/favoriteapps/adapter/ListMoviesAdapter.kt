package com.example.favoriteapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.favoriteapps.R
import com.example.favoriteapps.activity.DetailMoviesActivity
import com.example.favoriteapps.model.Moviews
import kotlinx.android.synthetic.main.item_row_hero.view.*

class ListMoviesAdapter(private val listMoviews: ArrayList<Moviews>) : RecyclerView.Adapter<ListMoviesAdapter.ListViewHolder>(), Filterable {

    internal var filterListResult: ArrayList<Moviews>

    init{
        this.filterListResult = listMoviews
    }

    fun setData(items: ArrayList<Moviews>){
        filterListResult.clear()
        filterListResult.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(moviews: Moviews) {
            with(itemView){
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w185${moviews.poster_path}")
                    .apply(RequestOptions().override(200, 250))
                    .into(img_item_photo)
                tv_item_name.text = moviews.title
                tv_item_description.text = moviews.overview
//                tv_rating.text = moviews.vote_average.toString()

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailMoviesActivity::class.java)
                    intent.putExtra(DetailMoviesActivity.EXTRA_FILM, moviews)
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

    internal var movie = arrayListOf<Moviews>()


    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun goToDetailMovie(data: Moviews)
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if(charSearch.isEmpty())
                    filterListResult = listMoviews
                else {
                    val resultlist = ArrayList<Moviews>()
                    for (row in listMoviews) {
                        if (row.title.toLowerCase().contains(charSearch.toLowerCase()))
                            resultlist.add(row)
                    }
                    filterListResult = resultlist
                }
                val filterResult = Filter.FilterResults()
                filterResult.values = filterListResult
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
               filterListResult = results!!.values as ArrayList<Moviews>
                notifyDataSetChanged()
            }

        }
    }


}
