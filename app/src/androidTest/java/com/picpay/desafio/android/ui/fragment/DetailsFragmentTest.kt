package com.picpay.desafio.android.ui.fragment


import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named


@ExperimentalPagingApi
@HiltAndroidTest
@MediumTest
class DetailsFragmentTest {

    private val context =
        InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    @Inject
    @Named("provideTestUser")
    lateinit var user: User

    private lateinit var fragment: DetailsFragment

    @Before
    fun setup() {
        hiltRule.inject()
        user.favorite = true

        launchFragmentInHiltContainer<DetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundleOf("detailsArgs" to user)
        ) { fragment = this }
    }

    @Test
    fun shouldPassWhenHasUser() {
        val navController = Mockito.mock(NavController::class.java)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navController.setGraph(R.navigation.main_graph)
        }

        fragment.run { Navigation.setViewNavController(requireView(), navController) }

        onView(withText(user.name)).check(matches(isDisplayed()))
        onView(withText("@${user.username}")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenHasCharAtOnCircleImage() {
        val navController = Mockito.mock(NavController::class.java)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navController.setGraph(R.navigation.main_graph)
        }

        fragment.run { Navigation.setViewNavController(requireView(), navController) }

        onView(withText("K")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldPassWhenClickShareButton() {
        val navController = Mockito.mock(NavController::class.java)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navController.setGraph(R.navigation.main_graph)
        }

        fragment.run { Navigation.setViewNavController(requireView(), navController) }

        onView(withId(R.id.button_share)).perform(click())

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.click(0, 500)
    }

    @Test
    fun shouldPassWhenClickFavoriteButton() {
        val navController = Mockito.mock(NavController::class.java)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navController.setGraph(R.navigation.main_graph)
        }

        fragment.run { Navigation.setViewNavController(requireView(), navController) }

        onView(withId(R.id.button_favorite)).perform(click())
            .check(matches(isNotChecked()))
    }
}
