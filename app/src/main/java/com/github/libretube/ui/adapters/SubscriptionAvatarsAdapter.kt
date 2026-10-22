package com.github.libretube.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.libretube.api.obj.Subscription
import com.github.libretube.databinding.ItemSubscriptionAvatarBinding
import com.github.libretube.helpers.ImageHelper
import com.github.libretube.helpers.NavigationHelper
import com.github.libretube.ui.adapters.callbacks.DiffUtilItemCallback

/**
 * Horizontal row of subscribed channel avatars shown at the top of the home screen
 */
class SubscriptionAvatarsAdapter :
    ListAdapter<Subscription, SubscriptionAvatarsAdapter.ViewHolder>(
        DiffUtilItemCallback<Subscription>(areItemsTheSame = { a, b -> a.url == b.url })
    ) {

    class ViewHolder(val binding: ItemSubscriptionAvatarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubscriptionAvatarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subscription = getItem(position)
        holder.binding.apply {
            name.text = subscription.name
            ImageHelper.loadImage(subscription.avatar, avatar, true)
            root.setOnClickListener {
                NavigationHelper.navigateChannel(root.context, subscription.url)
            }
        }
    }
}
