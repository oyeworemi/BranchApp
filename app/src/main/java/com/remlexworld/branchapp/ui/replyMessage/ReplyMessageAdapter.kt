package com.remlexworld.branchapp.ui.replyMessage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remlexworld.branchapp.R
import com.remlexworld.branchapp.model.Message
import com.remlexworld.branchapp.util.AppUtils
import kotlinx.android.synthetic.main.item_message.view.*

class ReplyMessageAdapter(private val context: Context, private val list: ArrayList<Message>) :
        RecyclerView.Adapter<ReplyMessageAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: Message) {

            itemView.text_view_user_id.text = message.user_id
            itemView.text_view_body.text = message.body
            itemView.text_view_date.text = AppUtils.formatDate(message.timestamp)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MovieViewHolder(context, view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<Message>) {
        list.clear()
        val sortedList = newList.sortedBy { message -> message.timestamp }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }
}