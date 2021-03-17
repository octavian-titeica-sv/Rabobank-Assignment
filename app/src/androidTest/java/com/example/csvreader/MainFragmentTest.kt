package com.example.csvreader

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.csvreader.ui.MainFragment
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

const val EXPECTED_CHILD_COUNT = 3

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<MainFragment>

    @Before
    fun containerIsDisplayed() {
        fragmentScenario = launchFragmentInContainer()
    }

    @Test
    fun testOnStarted() {
        fragmentScenario.moveToState(Lifecycle.State.STARTED)

        onView(withId(R.id.users_list_recycler_view)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.users_list_recycler_view)).check(matches(hasChildCount(EXPECTED_CHILD_COUNT)))
        onView(withId(R.id.loading_progress_bar)).check(matches(not(isDisplayed())))
    }
}
