package com.picpay.desafio.android.ui.fragment


import androidx.paging.ExperimentalPagingApi
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject


@ExperimentalPagingApi
@HiltAndroidTest
@MediumTest
class DetailsFragmentTest {

    private val context =
        InstrumentationRegistry.getInstrumentation().targetContext

    private val user = User(
        "1",
        "Sandrine Spinka",
        "https://randomuser.me/api/portraits/men/1.jpg",
        "Tod86"
    )

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    private lateinit var fragment: DetailsFragment

    @Before
    fun setup() {
        hiltRule.inject()

        launchFragmentInHiltContainer<DetailsFragment>(factory = fragmentFactory) {
            fragment = this
        }
    }
}
