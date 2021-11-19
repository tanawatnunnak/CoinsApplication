package com.tanawatnunnak.coinsapplication.ui.coin_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tanawatnunnak.coinsapplication.databinding.ItemLeftCoinBinding
import com.tanawatnunnak.coinsapplication.databinding.ItemLoadingBinding
import com.tanawatnunnak.coinsapplication.databinding.ItemRightCoinBinding
import com.tanawatnunnak.coinsapplication.extention.htmlToString
import com.tanawatnunnak.coinsapplication.extention.loadSvg

class CoinAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList: ArrayList<CoinBaseItem> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CoinType.LEFT_TYPE -> {
                val binding = ItemLeftCoinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LeftCoinViewHolder(binding)
            }
            CoinType.RIGHT_TYPE -> {
                val binding = ItemRightCoinBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RightCoinViewHolder(binding)
            }
            CoinType.LOADING_TYPE -> {
                val binding = ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }
            else -> throw Exception("type error")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LeftCoinViewHolder -> {
                val item = itemList[position] as CoinBaseItem.LeftCoinItem
                holder.bind(item)
            }
            is RightCoinViewHolder -> {
                val item = itemList[position] as CoinBaseItem.RightCoinItem
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].type
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(itemList: ArrayList<CoinBaseItem>) {
        val lastPosition = this.itemList.size
        this.itemList = itemList
        if (itemList.size > lastPosition) {
            notifyItemChanged(lastPosition)
        } else {
            notifyDataSetChanged()
        }
    }

    fun addLoading() {
        itemList.add(CoinBaseItem.LoadingItem(-1))
        notifyItemChanged(itemList.size - 1)
    }

    fun deleteLoading() {
        if (itemList.isNotEmpty()) {
            val lastPosition = itemList.size - 1
            val loadingItem = itemList[lastPosition]
            if (loadingItem is CoinBaseItem.LoadingItem) {
                itemList.removeAt(lastPosition)
                notifyItemRemoved(lastPosition)
            }
        }
    }

    inner class LeftCoinViewHolder(private val binding: ItemLeftCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: CoinBaseItem.LeftCoinItem) {
            binding.apply {
                if (item.imageUrl.endsWith(".svg")) {
                    itemLeftCoinIv.loadSvg(item.imageUrl)
                } else {
                    itemLeftCoinIv.load(item.imageUrl)
                }
                itemLeftCoinNameTv.text = item.name
                itemLeftCoinDescriptionTv.text = item.description.htmlToString()
            }
        }
    }

    inner class RightCoinViewHolder(private val binding: ItemRightCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: CoinBaseItem.RightCoinItem) {
            binding.apply {
                if (item.imageUrl.endsWith(".svg")) {
                    itemRightCoinIv.loadSvg(item.imageUrl)
                } else {
                    itemRightCoinIv.load(item.imageUrl)
                }
                itemRightCoinNameTv.text = item.name
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)
}