package com.picpay.desafio.android

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher


fun atPosition(
    position: Int,
    itemMatcher: Matcher<View>
) = object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("Has item at position: $position.")
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
            return "Performing action on RecyclerView child item."
        }

        override fun perform(uiController: UiController, view: View) {
            view.findViewById<T>(resId).block()
        }
    }
}

fun setViewGroupVisibility(value: Boolean): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(View::class.java)
        }

        override fun getDescription(): String {
            return "Performing action Show / Hide ViewGroup"
        }

        override fun perform(uiController: UiController, view: View) {
            (view as ViewGroup).visibility = if (value) View.VISIBLE else View.GONE
        }
    }
}

fun setTextInTextView(value: String): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.allOf(isDisplayed(), isAssignableFrom(TextView::class.java))
        }

        override fun perform(uiController: UiController, view: View) {
            (view as TextView).text = value
        }

        override fun getDescription(): String {
            return "Replace text"
        }
    }
}

fun waitFor(millis: Long = 5000): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "Wait for $millis milliseconds."
        }

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadForAtLeast(millis)
        }
    }
}
