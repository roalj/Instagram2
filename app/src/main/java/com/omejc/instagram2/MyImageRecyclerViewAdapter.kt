package com.omejc.instagram2

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.omejc.instagram2.ImageFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_image.view.*


class MyImageRecyclerViewAdapter(
    private val mValues: List<Image>,
    private val mListener: OnListFragmentInteractionListener?,
    private val context: Context

) : RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Image
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.title.text = item.title
        holder.mContentView.text = item.desciption


        val getJsonLoad  = ImageUploadRequest.getJsonLoadRequest(item.mongoId, holder.image)
        ImageUploadRequest.makeRequest(getJsonLoad, context)

        holder.image.contentDescription = item.desciption

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.item_number
        val mContentView: TextView = mView.content
        val image: ImageView = mView.image

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

}
