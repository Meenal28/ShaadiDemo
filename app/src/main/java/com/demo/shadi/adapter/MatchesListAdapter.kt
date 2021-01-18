package com.demo.shadi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.demo.shadi.GlideApp
import com.demo.shadi.R
import com.demo.shadi.databinding.MatchesListItemBinding
import com.demo.shadi.databinding.NetworkStateItemBinding
import com.demo.shadi.interfaces.MatchesItemClick
import com.demo.shadi.model.Result
import com.demo.shadi.model.STATUS
import com.demo.shadi.utils.NetworkState

class MatchesListAdapter(private val matchesItemClick: MatchesItemClick) :
    PagedListAdapter<Result, RecyclerView.ViewHolder>(Result.DIFF_CALLBACK) {

    private val TYPE_PROGRESS = 0
    private val TYPE_ITEM = 1

    private var networkState: NetworkState? = null
    private lateinit var context: Context

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        return if (viewType == TYPE_ITEM) {
            val itemBinding: MatchesListItemBinding =
                MatchesListItemBinding.inflate(layoutInflater, parent, false)
            MatchesViewHolder(itemBinding)
        } else {
            val headerBinding: NetworkStateItemBinding =
                NetworkStateItemBinding.inflate(layoutInflater, parent, false)
            NetworkStateItemViewHolder(headerBinding)
        }
    }

    override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM && holder is MatchesViewHolder) {
            getItem(position)?.let { holder.bind(it) }
        } else {
            (holder as NetworkStateItemViewHolder).bindView(networkState)
        }
    }


    /**
     * Method to check if network state row is required or not
     */
    private fun hasExtraRow(): Boolean {
        return super.getItemCount() != 0 && networkState == null || networkState?.getStatus() == NetworkState.Status.RUNNING ||
                networkState?.getStatus() == NetworkState.Status.FAILED
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) {
            TYPE_ITEM
        } else {
            TYPE_PROGRESS
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        this.networkState = newNetworkState
        notifyItemChanged(super.getItemCount())
    }

    inner class MatchesViewHolder(itemView: MatchesListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        var matchesListItemBinding: MatchesListItemBinding = itemView

        fun bind(result: Result) {
            matchesListItemBinding.result = result
            matchesListItemBinding.executePendingBindings()
            matchesListItemBinding.tvUserDetail.text = result.dob.age.toString()

            /// Show User Image
            GlideApp
                .with(itemView)
                .load(result.picture?.medium)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(matchesListItemBinding.ivUser)

            /// Locally Maintained the accepted or declines state
            if (result.isAccepted == null) {
                matchesListItemBinding.llButtons.visibility = View.VISIBLE
                matchesListItemBinding.tvStatus.visibility = View.GONE
                matchesListItemBinding.buttonAccept.setOnClickListener {
                    matchesItemClick.onItemClick(adapterPosition, STATUS.ACCPET)
                }

                matchesListItemBinding.buttonDecline.setOnClickListener {
                    matchesItemClick.onItemClick(
                        adapterPosition,
                        STATUS.DECLINE
                    )
                }
            } else if (result.isAccepted != null) {
                matchesListItemBinding.llButtons.visibility = View.GONE
                matchesListItemBinding.tvStatus.visibility = View.VISIBLE
                matchesListItemBinding.tvStatus.text =
                    if (result.isAccepted == true) context.getString(R.string.text_member_accepted) else context.getString(
                        R.string.text_member_declined
                    )
            }
        }

    }


    class NetworkStateItemViewHolder(private val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(networkState: NetworkState?) {
            binding.progressBar.visibility =
                if (networkState != null && networkState.getStatus() === NetworkState.Status.RUNNING) View.VISIBLE else View.GONE

            if (networkState != null && networkState.getStatus() === NetworkState.Status.FAILED) {
                binding.tvErrorMsg.visibility = View.VISIBLE
                binding.tvErrorMsg.text = networkState.getMsg()
            } else {
                binding.tvErrorMsg.visibility = View.GONE
            }
        }

    }
}