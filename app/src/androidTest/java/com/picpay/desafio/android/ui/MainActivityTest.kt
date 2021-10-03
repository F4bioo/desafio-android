package com.picpay.desafio.android.ui


import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.utils.RecyclerViewMatchers.atPosition
import com.picpay.desafio.android.utils.RecyclerViewMatchers.recyclerChildAction
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest : TestCase() {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun shouldPassWhenHasTitle() {
        onView(withText(context.getString(R.string.title))).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenDayNightModeIsChecked() {
        onView(withId(R.id.radio_day_mode)).perform(click())
        onView(withId(R.id.radio_day_mode)).check(matches(isChecked()))

        onView(withId(R.id.radio_night_mode)).perform(click())
        onView(withId(R.id.radio_night_mode)).check(matches(isChecked()))
    }

    @Test
    fun shouldPassWhenFavoriteIsChecked() {
        val position = 4

        onView(withId(R.id.recycler)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, recyclerChildAction<CheckBox>(R.id.check_favorite) { isChecked = true }
            )
        )

        onView(withId(R.id.recycler))
            .check(matches(atPosition(position, hasDescendant(isChecked()))))
    }

    @Test
    fun shouldPassWhenClickOnRecyclerViewItem() {
        onView(withId(R.id.recycler)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3, click()
            )
        )
    }

    @Test
    fun shouldPassWhenScrollRecyclerView() {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.adapter?.itemCount?.let { itemCount ->
            onView(withId(R.id.recycler)).perform(
                scrollToPosition<RecyclerView.ViewHolder>(itemCount.minus(1))
            )
        }
    }

    @Test
    fun shouldPassWhenRecyclerViewHasItem() {
        val user = User(
            "1",
            "Sandrine Spinka",
            "https://randomuser.me/api/portraits/men/1.jpg",
            "Tod86"
        )

        onView(withText(user.name)).check(matches(isDisplayed()))
        onView(withText("@${user.username}")).check(matches(isDisplayed()))
    }
}
