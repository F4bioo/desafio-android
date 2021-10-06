package com.picpay.desafio.android.ui.fragment

import android.widget.CheckBox
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.*
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.ui.adapter.RemoteUserAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

@ExperimentalPagingApi
@HiltAndroidTest
@MediumTest
class MainFragmentTest {

    private val context =
        InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Inject
    @Named("provideTestUser")
    lateinit var user: User

    private lateinit var fragment: MainFragment

    @Before
    fun setup() {
        hiltRule.inject()

        launchFragmentInHiltContainer<MainFragment>(factory = fragmentFactory) {
            fragment = this
        }
    }

    @Test
    fun shouldPassWhenHasTitle() {
        onView(withText(context.getString(R.string.main_title))).perform()
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenGetDayNightModeRadioButtonsDescription() {
        onView(withText(context.getString(R.string.day_mode)))
            .check(matches(isDisplayed()))

        onView(withText(context.getString(R.string.night_mode)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenDayNightModeIsChecked() {
        onView(withId(R.id.radio_day_mode)).perform(click())
        onView(withId(R.id.radio_day_mode))
            .check(matches(isChecked()))

        onView(withId(R.id.radio_night_mode)).perform(click())
        onView(withId(R.id.radio_night_mode))
            .check(matches(isChecked()))
    }

    @Test
    fun shouldPassWhenFavoriteIsChecked(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        fragment.apply {
            val users = PagingData.from(arrayListOf(user))
            adapter.submitData(users)
        }

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RemoteUserAdapter.ViewHolder>(
                0, recyclerChildAction<CheckBox>(R.id.check_favorite) { isChecked = true }
            )
        )

        onView(withId(R.id.recycler)).check(
            matches(atPosition(0, hasDescendant(isChecked())))
        )
    }

    @Test
    fun shouldPassWhenRecyclerViewHasItem(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        fragment.apply {
            val users = PagingData.from(arrayListOf(user))
            adapter.submitData(users)
        }

        onView(withText(user.name)).check(matches(isDisplayed()))
        onView(withText("@${user.username}")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenClickOnFabButton() {
        val navController = Mockito.mock(NavController::class.java)

        fragment.apply {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fab)).perform(click())

        Mockito.verify(navController).navigate(
            MainFragmentDirections.actionMainFragmentToFavoritesFragment()
        )
    }

    @Test
    fun shouldPassWhenClickOnRecyclerViewItem(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        val navController = Mockito.mock(NavController::class.java)

        fragment.apply {
            Navigation.setViewNavController(requireView(), navController)
            val users = PagingData.from(arrayListOf(user))
            adapter.submitData(users)
        }

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RemoteUserAdapter.ViewHolder>(
                0, click()
            )
        )

        delay(2000)
        Mockito.verify(navController).navigate(
            MainFragmentDirections.actionMainFragmentToDetailsFragment(user)
        )
    }
}
