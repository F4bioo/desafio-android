package com.picpay.desafio.android.ui.fragment


import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.R
import com.picpay.desafio.android.extensions.launchFragmentInHiltContainer
import com.picpay.desafio.android.extensions.setViewGroupVisibility
import com.picpay.desafio.android.extensions.waitFor
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@ExperimentalPagingApi
@HiltAndroidTest
@MediumTest
class FavoritesFragmentTest {

    private val context =
        InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    private lateinit var fragment: FavoritesFragment

    @Before
    fun setup() {
        hiltRule.inject()

        launchFragmentInHiltContainer<FavoritesFragment>(factory = fragmentFactory) {
            fragment = this
        }
    }

    @Test
    fun shouldPassWhenHasTitle() {
        onView(withText(context.getString(R.string.favorites_title))).perform()
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenHasEmptyLayout() {
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(false))

        onView(isRoot()).perform(waitFor(2000))
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(true))

        onView(withId(R.id.include_empty)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun shouldPassWhenHasEmptyLayoutDescription() {
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(false))

        onView(isRoot()).perform(waitFor(2000))
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(true))

        onView(isRoot()).perform(waitFor(3000))
        onView(withText(context.getString(R.string.favorites_empty_list))).check(matches(isDisplayed()))
    }
}
