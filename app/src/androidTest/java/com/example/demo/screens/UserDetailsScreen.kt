package com.example.demo.screens

import com.example.demo.R
import com.example.demo.presentation.ui.users.details.UserDetailsFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.text.KSnackbar
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar

object UserDetailsScreen : KScreen<UserDetailsScreen>() {

    override val layoutId: Int = R.layout.fragment_user_details
    override val viewClass: Class<*> = UserDetailsFragment::class.java

    val toolbar = KToolbar { withId(R.id.toolbar) }

    val avatar = KImageView { withId(R.id.image_avatar) }

    val login = KTextView { withId(R.id.text_login) }

    val followers = KTextView { withId(R.id.text_followers) }

    val following = KTextView { withId(R.id.text_following) }

    val progress = KProgressBar { withId(R.id.progress) }

    val snackbar = KSnackbar()
}