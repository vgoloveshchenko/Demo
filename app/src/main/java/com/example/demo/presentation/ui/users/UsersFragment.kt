package com.example.demo.presentation.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.R
import com.example.demo.databinding.FragmentUsersBinding
import com.example.demo.presentation.extension.addPaginationScrollListener
import com.example.demo.presentation.extension.addVerticalSpaceDecoration
import com.example.demo.presentation.extension.applyHorizontalWindowInsets
import com.example.demo.presentation.extension.applyWindowInsets
import com.example.demo.presentation.model.PagingDisplayItem
import com.example.demo.presentation.paging.PagingLceState
import com.example.demo.presentation.ui.users.adapter.UserAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<UsersViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        UserAdapter(
            context = requireContext(),
            onUserClicked = {
                findNavController().navigate(UsersFragmentDirections.toDetails(it.login))
            },
            onRetryClicked = {
                viewModel.onRetry()
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUsersBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.applyWindowInsets()
            recyclerView.applyHorizontalWindowInsets()

            val linearLayoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.addVerticalSpaceDecoration(R.dimen.users_list_vertical_space)
            recyclerView.addPaginationScrollListener(linearLayoutManager, ITEMS_TO_LOAD) {
                viewModel.onLoadMore()
            }

            layoutSwipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }

            viewModel
                .usersPagingData
                .onEach { state ->
                    progress.isVisible =
                        state == PagingLceState.FirstLoading && !layoutSwipeRefresh.isRefreshing
                    layoutSwipeRefresh.isRefreshing =
                        state == PagingLceState.FirstLoading && layoutSwipeRefresh.isRefreshing

                    when (state) {
                        is PagingLceState.FirstLoadingError -> {
                            Snackbar.make(
                                view,
                                state.throwable.localizedMessage
                                    ?: getString(R.string.general_error),
                                Snackbar.LENGTH_LONG
                            ).setAction(android.R.string.ok) { viewModel.onRetry() }
                                .show()
                        }
                        is PagingLceState.Content -> {
                            adapter.submitList(
                                state.data.map { PagingDisplayItem.Content(it) }
                                    .let {
                                        if (state.hasMoreData) it.plus(PagingDisplayItem.Loading)
                                        else it
                                    }
                            )
                        }
                        is PagingLceState.ContentWithError -> {
                            adapter.submitList(
                                state.data.map { PagingDisplayItem.Content(it) }
                                    .plus(PagingDisplayItem.Error)
                            )
                        }
                        is PagingLceState.FirstLoading -> {
                            // no-op
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ITEMS_TO_LOAD = 15
    }
}