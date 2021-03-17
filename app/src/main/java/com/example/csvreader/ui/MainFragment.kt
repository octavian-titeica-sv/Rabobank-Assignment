package com.example.csvreader.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csvreader.R
import com.example.csvreader.presentation.UserState
import com.example.csvreader.presentation.viewModel.MainViewModel
import com.example.csvreader.ui.adapter.UsersAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.main_fragment.users_list_recycler_view as usersListRecyclerView

class MainFragment : DaggerFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupUsersAdapter()

        viewModel.usersState.observe(this, { state -> render(state) })
    }

    private fun render(state: UserState) {
        loading_progress_bar.visibility = if (state.loading) View.VISIBLE else View.GONE
        if (state.result.isNotEmpty()) {
            usersAdapter.users = state.result
        }
        state.error?.let { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show() }
    }

    private fun setupUsersAdapter() {
        usersListRecyclerView.apply {
            usersAdapter = UsersAdapter()
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
