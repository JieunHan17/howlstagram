package com.example.howlstagram.navigation

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howlstagram.R
import com.example.howlstagram.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_detail_view.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewFragment : Fragment() {
    var firestore: FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =
            LayoutInflater.from(activity).inflate(R.layout.fragment_detail_view, container, false)
//        return inflater.inflate(R.layout.fragment_detail_view, container, false)
        view.detailviewfragment_recyclerview.adapter = DetailViewRecylerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecylerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener {
                querySnapshot, firebaseFirestoreException -> contentDTOs.clear()
                contentUidList.clear()
                for(snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as CustomViewHolder).itemView

            viewHolder.detailviewitem_profile_textview.text = contentDTOs[position].userId
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).into(viewHolder.detailviewitem_imageview_content)

            viewHolder.detailviewitem_explain_textview.text = contentDTOs[position].explain
            viewHolder.detailviewitem_favoritecounter_textview.text = "Likes " + contentDTOs[position].favoriteCount

            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).into(viewHolder.detailviewitem_profile_image)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

    }
}