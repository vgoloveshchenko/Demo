package com.example.demo.screens

import android.view.View
import com.example.demo.R
import com.example.demo.presentation.ui.users.UsersFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.swiperefresh.KSwipeRefreshLayout
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object UsersScreen : KScreen<UsersScreen>() {

    override val layoutId: Int = R.layout.fragment_users
    override val viewClass: Class<*> = UsersFragment::class.java

    val toolbar = KToolbar { withId(R.id.toolbar) }

    val swipeRefreshLayout = KSwipeRefreshLayout { withId(R.id.layout_swipe_refresh) }

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.recycler_view) },
        itemTypeBuilder = {
            itemType(UsersScreen::UserItem)
            itemType(UsersScreen::LoadingItem)
            itemType(UsersScreen::ErrorItem)
        }
    )

    val progress = KProgressBar { withId(R.id.progress) }

    class UserItem(parent: Matcher<View>) : KRecyclerItem<UserItem>(parent) {
        val avatar = KImageView(parent) { withId(R.id.image_avatar) }
        val name = KTextView(parent) { withId(R.id.text_name) }
    }

    class LoadingItem(parent: Matcher<View>) : KRecyclerItem<LoadingItem>(parent) {
        val progress = KProgressBar { withId(R.id.progress) }
    }

    class ErrorItem(parent: Matcher<View>) : KRecyclerItem<ErrorItem>(parent) {
        val title = KTextView(parent) { withId(R.id.text_title) }
        val button = KTextView(parent) { withId(R.id.button) }
    }
}