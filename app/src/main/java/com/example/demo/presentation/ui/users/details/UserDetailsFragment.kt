package com.example.demo.presentation.ui.users.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.demo.R
import com.example.demo.databinding.FragmentUserDetailsBinding
import com.example.demo.presentation.extension.applyHorizontalWindowInsets
import com.example.demo.presentation.extension.applyWindowInsets
import com.example.demo.presentation.extension.doOnApplyWindowInsets
import com.example.demo.presentation.model.LceState
import com.example.demo.presentation.model.onContent
import com.example.demo.presentation.model.onError
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args: UserDetailsFragmentArgs by navArgs()

    private val viewModel by viewModel<UserDetailsViewModel> {
        parametersOf(args.login)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentUserDetailsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.applyWindowInsets()
            toolbar.setupWithNavController(findNavController())
            toolbar.title = args.login

            scrollView.applyHorizontalWindowInsets()

            viewModel
                .userDetailsFlow
                .onEach { lceState ->
                    progress.isVisible = lceState == LceState.Loading
                    lceState
                        .onContent { userDetails ->
                            imageAvatar.load(userDetails.avatarUrl)
                            textLogin.text = userDetails.login
                            textFollowers.text =
                                getString(R.string.user_details_followers, userDetails.followers)
                            textFollowing.text =
                                getString(R.string.user_details_following, userDetails.following)
                        }
                        .onError { throwable ->
                            Snackbar.make(
                                root,
                                throwable.localizedMessage ?: getString(R.string.general_error),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}