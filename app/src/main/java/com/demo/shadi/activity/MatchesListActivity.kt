package com.demo.shadi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.shadi.R
import com.demo.shadi.adapter.MatchesListAdapter
import com.demo.shadi.databinding.ActivityMatchesListBinding
import com.demo.shadi.interfaces.MatchesItemClick
import com.demo.shadi.model.Result
import com.demo.shadi.model.STATUS
import com.demo.shadi.viewmodel.MatchesViewModel

class MatchesListActivity : AppCompatActivity(), MatchesItemClick {

    lateinit var viewBinding: ActivityMatchesListBinding
    lateinit var matchesListAdapter: MatchesListAdapter
    lateinit var matchesViewModel: MatchesViewModel
    private lateinit var matchesList: PagedList<Result>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_matches_list)
        initView()
    }

    /**
     * Initialise view and set adapter
     */
    private fun initView() {
        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel::class.java)
        viewBinding.rvMatches.layoutManager = LinearLayoutManager(this)
        matchesListAdapter = MatchesListAdapter(this)
        viewBinding.rvMatches.adapter = matchesListAdapter
        matchesViewModel.getState()
            .observe(this,
                Observer { networkState -> matchesListAdapter.setNetworkState(networkState) })
        fetchMatchesData()
    }

    /**
     * Fetch Matches List data
     */
    private fun fetchMatchesData() {
        matchesViewModel.getMatchesLiveData().observe(this, Observer {
            this.matchesList = it
            matchesListAdapter.submitList(this.matchesList)
        })

    }

    /**
     * On Item Click for "Accept" or "Decline"
     */
    override fun onItemClick(position: Int, status: STATUS) {
        if (position >= 0 && matchesList.isNotEmpty()) {
            matchesList[position]?.isAccepted = status == STATUS.ACCPET
            matchesListAdapter.notifyDataSetChanged()
        }
    }
}
