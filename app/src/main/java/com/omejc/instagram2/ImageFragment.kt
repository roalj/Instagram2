package com.omejc.instagram2

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.GsonBuilder
import org.json.JSONArray
import java.util.*
import kotlin.concurrent.schedule


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ImageFragment.OnListFragmentInteractionListener] interface.
 */
class ImageFragment : Fragment() {

    private var columnCount = 1
    private var TAG = "ImageFragment"
    private var listener: OnListFragmentInteractionListener? = null
    private var rootView: View? = null
    private lateinit var mContext: Context
    private lateinit var listView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NukeSSLCerts.nuke()
        arguments?.let { columnCount = it.getInt(ARG_COLUMN_COUNT) }
        CatalogRequestHelper.makeRequest(context, myImageListener)
    }

    private var myImageListener: Response.Listener<JSONArray> =
        Response.Listener { response ->
            val gson = GsonBuilder().create()
            val images = gson.fromJson(response.toString(), Array<Image>::class.java).toList()
           /* Log.d(TAG, "image length:$images")
            for (name:Image in images){
                Log.d(TAG, "!!!name${name.title}")
                Log.d(TAG, name.desciption)
            }
*/
            val adapter = MyImageRecyclerViewAdapter(images, listener, mContext)
            listView.adapter = adapter
        }

    public fun refresh(){
        Timer("SettingUp", false).schedule(2000) {
            CatalogRequestHelper.makeRequest(context, myImageListener)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_list, container, false)
        this.rootView = view;

        listView = view.findViewById(R.id.list)
        mContext = view.context

        // Set the adapter
      /*  if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyImageRecyclerViewAdapter(DummyContent.ITEMS, listener)
            }
        }*/
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Image?)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
