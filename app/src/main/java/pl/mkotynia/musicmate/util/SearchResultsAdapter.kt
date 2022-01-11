package pl.mkotynia.musicmate.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mkotynia.musicmate.model.User


class SearchResultsAdapter(private val onUserSelectedListener: OnUserSelectedListener) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    var usersList = mutableListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(usersList[position])
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(user: User) {
            val nameTextView = itemView.findViewById(android.R.id.text1) as TextView
            val instrumentTextView  = itemView.findViewById(android.R.id.text2) as TextView
            nameTextView.text = user.userName
            instrumentTextView.text = user.instrument

            itemView.setOnClickListener {
                onUserSelectedListener.onUserSelected(user)
            }
        }
    }

    interface OnUserSelectedListener{
        fun onUserSelected(user: User)
    }
}