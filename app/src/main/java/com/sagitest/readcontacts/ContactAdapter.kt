package com.sagitest.readcontacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_child.view.*

class ContactAdapter(items: List<ContactDTO>, ctx: Context, val listener: OnItemCLickListener) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var list = items
    private var context = ctx

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        val name = v.tv_name!!
        val number = v.tv_number!!
        val profile = v.iv_profile!!

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemCLickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.contact_child, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.number.text = list[position].number
        if (list[position].image != null)
            holder.profile.setImageBitmap(list[position].image)
        else
            holder.profile.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.mipmap.ic_launcher_round
                )
            )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}