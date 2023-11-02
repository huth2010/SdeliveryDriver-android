package com.fpoly.sdeliverydriver.ui.chat.room

import android.content.ContentUris
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.Gallery
import com.fpoly.sdeliverydriver.data.model.GalleryType
import com.fpoly.sdeliverydriver.data.model.GalleryVideo
import com.fpoly.sdeliverydriver.data.model.GalleyImage
import com.fpoly.sdeliverydriver.databinding.ItemGalleryChatBinding

class GalleryBottomChatAdapter(
    private val onClick: IOnClickLisstenner
) : RecyclerView.Adapter<GalleryBottomChatAdapter.ViewHolder>() {

    interface IOnClickLisstenner{
        fun onClickSelectItem(gallery: Gallery)
        fun onClickUnSelectItem(gallery: Gallery)
        fun onLongClickItem(gallery: Gallery)
    }

    var galleries: ArrayList<Gallery> = ArrayList()

    fun setData(data: ArrayList<Gallery>?){
        if (data == null) return
        this.galleries = data
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(gallery: Gallery, position: Int){
            with(binding as ItemGalleryChatBinding){
                binding.layoutChoose.isVisible = false

                if (gallery.type == GalleryType.GALLERY_IMAGE){
                    gallery as GalleyImage

                    var uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, gallery.id)

                    Glide.with(binding.root.context)
                        .load(uri)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .signature(ObjectKey(System.currentTimeMillis()))
                        .placeholder(R.mipmap.ic_launcher)
                        .into(this.imgBody)

                    binding.imgVideo.isVisible = false
                }else if(gallery.type == GalleryType.GALLERY_VIDEO){
                    gallery as GalleryVideo

                    Glide.with(binding.root.context)
                        .load(gallery.thumb)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .signature(ObjectKey(System.currentTimeMillis()))
                        .placeholder(R.mipmap.ic_launcher)
                        .into(this.imgBody)

                    binding.imgVideo.isVisible = true
                } else {

                }

                binding.root.setOnClickListener{
                    if (!binding.layoutChoose.isVisible){
                        binding.layoutChoose.isVisible = true
                        onClick.onClickSelectItem(gallery)
                    }else{
                        binding.layoutChoose.isVisible = false
                        onClick.onClickUnSelectItem(gallery)
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemGalleryChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return galleries.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(galleries[position], position)
    }
}