package com.picpay.desafio.android.ui.fragment


import android.widget.CheckBox
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.*
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.ui.adapter.LocalUserAdapter
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
class FavoritesFragmentTest {

    private val context =
        InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Inject
    @Named("provideTestUser")
    lateinit var user: User

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

    @Test
    fun shouldPassWhenFavoriteIsNotChecked(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        fragment.apply {
            val users = arrayListOf(user)
            adapter.submitList(users)
        }

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RemoteUserAdapter.ViewHolder>(
                0, recyclerChildAction<CheckBox>(R.id.check_favorite) { isChecked = false }
            )
        )

        delay(2000)
        onView(withId(R.id.recycler)).check(
            matches(atPosition(0, hasDescendant(isNotChecked())))
        )
    }

    @Test
    fun shouldPassWhenRecyclerViewHasItem(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        fragment.apply {
            val users = arrayListOf(user)
            adapter.submitList(users)
        }

        delay(2000)
        onView(withText(user.name)).check(matches(isDisplayed()))
        onView(withText("@${user.username}")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenClickOnArrowBackButton() {
        val navController = Mockito.mock(NavController::class.java)

        fragment.apply {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.button_arrow)).perform(ViewActions.pressBack())
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun shouldPassWhenUseOnBackPressed() {
        val navController = Mockito.mock(NavController::class.java)

        fragment.apply {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun shouldPassWhenClickOnRecyclerViewItem(): Unit = runBlocking {
        onView(withId(R.id.include_empty)).perform(setViewGroupVisibility(false))
        onView(withId(R.id.include_list)).perform(setViewGroupVisibility(true))

        val navController = Mockito.mock(NavController::class.java)

        fragment.apply {
            Navigation.setViewNavController(requireView(), navController)
            val users = arrayListOf(user)
            adapter.submitList(users)
        }

        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<LocalUserAdapter.ViewHolder>(
                0, click()
            )
        )

        delay(2000)
        Mockito.verify(navController).navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(user)
        )
    }
}
