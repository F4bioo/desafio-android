package com.picpay.desafio.android.utils.extensions

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher

fun atPosition(
    position: Int,
    itemMatcher: Matcher<View>
) = object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("Has item at position: $position: ")
        itemMatcher.describeTo(description)
    }

    override fun matchesSafely(item: RecyclerView?): Boolean {
        val viewHolder = item?.findViewHolderForAdapterPosition(position) ?: return false
        return itemMatcher.matches(viewHolder.itemView)
    }
}

fun <T : View> recyclerChildAction(@IdRes resId: Int, block: T.() -> Unit): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.any(View::class.java)
        }

        override fun getDescription(): String {
            return "Performing action on RecyclerView child item"
        }

        override fun perform(uiController: UiController, view: View) {
            view.findViewById<T>(resId).block()
        }
    }
}
