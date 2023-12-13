package com.fpoly.sdeliverydriver.ui.chat.room

import android.annotation.SuppressLint
import android.os.Build
import android.text.util.Linkify
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fpoly.sdeliverydriver.R
import com.fpoly.sdeliverydriver.data.model.Message
import com.fpoly.sdeliverydriver.data.model.MessageType
import com.fpoly.sdeliverydriver.data.model.Room
import com.fpoly.sdeliverydriver.databinding.ItemChatErrorBinding
import com.fpoly.sdeliverydriver.databinding.ItemChatMeBinding
import com.fpoly.sdeliverydriver.databinding.ItemChatYouBinding
import com.fpoly.sdeliverydriver.ultis.StringUltis
import com.fpoly.sdeliverydriver.ultis.convertToDateFormat
import com.fpoly.sdeliverydriver.ultis.convertToStringFormat
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@SuppressLint("SetTextI18n")
class RoomChatAdapter(
    private val onCallBack: IOnClickLisstenner
) : RecyclerView.Adapter<RoomChatAdapter.ViewHolder>() {

    interface IOnClickLisstenner{
        fun onClickItem(message: Message)
        fun onLongClickItem(message: Message)
        fun onClickItemCall()
        fun onClickItemJoinCall(message: Message)
    }

    companion object{
        const val TYPE_ME = 0
        const val TYPE_YOU = 1
    }

    var currentRoom: Room? = null
    var messages: ArrayList<Message> = ArrayList()

    fun setDataRoom(data: Room?){
        if (data == null) return
        currentRoom = data
        notifyDataSetChanged()
    }

    fun setDataMessage(data: ArrayList<Message>?){
        if (data.isNullOrEmpty()) return
        messages = data
        notifyDataSetChanged()
    }

    fun addData(data: Message?): Int? {
        if (data == null) return null

        val indexFind = messages.indexOfFirst { it._id == data._id }
        if (indexFind != -1) {
            messages[indexFind] = data
            notifyItemChanged(indexFind)
        } else {
            messages.add(data)
            notifyItemInserted(messages.size - 1)
        }

        return messages.size - 1
    }

    inner class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(message: Message, position: Int){
            when(binding){
                is ItemChatMeBinding ->{
                    handChatMe(binding, message, position)
                }
                is ItemChatYouBinding ->{
                    handChatYou(binding, message, position)
                }
                is ItemChatErrorBinding ->{

                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (messages[position].userIdSend?._id == currentRoom?.userUserId?._id) TYPE_ME else TYPE_YOU
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == TYPE_ME)return ViewHolder(ItemChatMeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return ViewHolder(ItemChatYouBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        if (currentRoom != null) return messages.size
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(messages[position], position)
    }


    private fun handChatMe(binding: ItemChatMeBinding, message: Message, position: Int) {

        binding.tvTime.text = handleShowTimeMessage(message.createdAt)
        binding.tvTime.isVisible = checkTimeStamp(position)

        if (message.type == MessageType.TYPE_TEXT){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = true
            binding.layoutCall.root.isVisible = false

            binding.imgMassage.setImageResource(0)
            binding.tvMessage.text = message.message

            if (Patterns.WEB_URL.matcher(message.message.toString()).matches()){
                Linkify.addLinks(binding.tvMessage, Linkify.WEB_URLS)
                binding.tvMessage.setLinkTextColor(binding.root.context.resources.getColor(R.color.white))
            }

        }else if(message.type == MessageType.TYPE_IMAGE && !message.images.isNullOrEmpty()){
            binding.imgMassage.isVisible = true
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = false

            Glide.with(binding.root.context)
                .asBitmap()
                .load(message.images[0].url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imgMassage)

        }else if (message.type == MessageType.TYPE_CALLING){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = true

            binding.layoutCall.root.setBackgroundResource(R.drawable.background_circle_select)
            binding.layoutCall.tvCallTitle.text = "Đang gọi"
            binding.layoutCall.tvCallTime.text = "Tham gia"

            binding.layoutCall.root.setOnClickListener{
                onCallBack.onClickItemJoinCall(message)
            }

        }else if (message.type == MessageType.TYPE_CALLED){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = true

            binding.layoutCall.root.setBackgroundResource(R.drawable.background_circle)
            binding.layoutCall.tvCallTitle.text = "Cuộc gọi thoại"
            binding.layoutCall.tvCallTime.text = handleBetweenTimeCall(message.createdAt!!, message.updatedAt!!)

            binding.layoutCall.root.setOnClickListener{
                onCallBack.onClickItemCall()
            }
        }else{

        }

        if (!message.images.isNullOrEmpty() && message.images.size > 1) {
            binding.tvCountImages.isVisible = true
            binding.tvCountImages.text = "+${message.images.size - 1}"
        }else binding.tvCountImages.isVisible = false

        binding.imgMassage.setOnClickListener{ onCallBack.onClickItem(message) }
        binding.tvMessage.setOnClickListener{
            onCallBack.onClickItem(message)
            binding.tvTime.isVisible = !binding.tvTime.isVisible
        }
        binding.tvMessage.setOnLongClickListener{ onCallBack.onLongClickItem(message)
            true }
//        binding.imgMassage.setOnLongClickListener{ onCallBack.onLongClickItem(message)
//            true }
    }


    private fun handChatYou(binding: ItemChatYouBinding, message: Message, position: Int) {
        if ( position + 1 < messages.size && messages[position].userIdSend?._id == messages[position + 1].userIdSend?._id){
            binding.imgAvatar.isVisible = false
            binding.imgAvatar.setImageResource(0)
        }else{
            binding.imgAvatar.isVisible = true
            binding.imgAvatar.setImageResource(R.mipmap.ic_launcher)
            Glide.with(binding.root.context).load(message.userIdSend?.avatar?.url).placeholder(R.mipmap.ic_launcher).into(binding.imgAvatar)
        }

        binding.tvTime.text = handleShowTimeMessage(message.createdAt)
        binding.tvTime.isVisible = checkTimeStamp(position)

        if (message.type == MessageType.TYPE_TEXT){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = true
            binding.layoutCall.root.isVisible = false

            binding.imgMassage.setImageResource(0)
            binding.tvMessage.text = message.message

            if (Patterns.WEB_URL.matcher(message.message.toString()).matches()){
                Linkify.addLinks(binding.tvMessage, Linkify.WEB_URLS)
                binding.tvMessage.setLinkTextColor(binding.root.context.resources.getColor(R.color.grey_black))
            }

        }else if(message.type == MessageType.TYPE_IMAGE && !message.images.isNullOrEmpty()){
            binding.imgMassage.isVisible = true
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = false

            Glide.with(binding.root.context)
                .asBitmap()
                .load(message.images[0].url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imgMassage)

        }else if (message.type == MessageType.TYPE_CALLING){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = true

            binding.layoutCall.root.setBackgroundResource(R.drawable.background_circle_select)
            binding.layoutCall.tvCallTitle.text = "Đang gọi"
            binding.layoutCall.tvCallTime.text = "Tham gia"

            binding.layoutCall.root.setOnClickListener{
                onCallBack.onClickItemJoinCall(message)
            }

        }else if (message.type == MessageType.TYPE_CALLED){
            binding.imgMassage.isVisible = false
            binding.tvMessage.isVisible = false
            binding.layoutCall.root.isVisible = true

            binding.layoutCall.root.setBackgroundResource(R.drawable.background_circle)
            binding.layoutCall.tvCallTitle.text = "Cuộc gọi thoại"
            binding.layoutCall.tvCallTime.text = handleBetweenTimeCall(message.createdAt!!, message.updatedAt!!)

            binding.layoutCall.root.setOnClickListener{
                onCallBack.onClickItemCall()
            }

        }else{

        }

        if (!message.images.isNullOrEmpty() && message.images.size > 1) {
            binding.tvCountImages.isVisible = true
            binding.tvCountImages.text = "+${message.images.size - 1}"
        }else binding.tvCountImages.isVisible = false

        binding.imgMassage.setOnClickListener{ onCallBack.onClickItem(message) }
        binding.tvMessage.setOnClickListener{
            onCallBack.onClickItem(message)
            binding.tvTime.isVisible = !binding.tvTime.isVisible
        }
        binding.tvMessage.setOnLongClickListener{ onCallBack.onLongClickItem(message)
            true }
//        binding.imgMassage.setOnLongClickListener{ onCallBack.onLongClickItem(message)
//            true }
    }

    private fun checkTimeStamp(position: Int): Boolean {
        return if (position == 0) true else 3600000 < (
                messages.get(position).createdAt!!.convertToDateFormat(StringUltis.dateIso8601Format)!!.time -
                        messages.get(position - 1).createdAt!!.convertToDateFormat(StringUltis.dateIso8601Format)!!.time
                )
    }

    private fun handleShowTimeMessage(time: String?): String?{
        val dateSend = time?.convertToDateFormat(StringUltis.dateIso8601Format)

        if (dateSend == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return time?.convertToStringFormat(StringUltis.dateIso8601Format, StringUltis.dateTimeHourFormat)
        }

        val localDateTodayToCheck: LocalDate = Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val localDateToCheck: LocalDate = dateSend.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val isToday = localDateToCheck.dayOfMonth == localDateTodayToCheck.dayOfMonth

        if (isToday){
            return time.convertToStringFormat(StringUltis.dateIso8601Format, StringUltis.dateTimeHourFormat)
        }else{
            return time.convertToStringFormat(StringUltis.dateIso8601Format, StringUltis.dateTimeDateFormat2)
        }
    }

    private fun handleBetweenTimeCall(strDate1: String, strDate2: String): String{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date1 = strDate1.convertToDateFormat(StringUltis.dateIso8601Format)!!
            val date2 = strDate2.convertToDateFormat(StringUltis.dateIso8601Format)!!
            val duration = Duration.between(date1.toInstant(), date2.toInstant())
            val minutes = duration.toMinutes()
            return "$minutes phút"
        } else {
            return ""
        }
    }
}