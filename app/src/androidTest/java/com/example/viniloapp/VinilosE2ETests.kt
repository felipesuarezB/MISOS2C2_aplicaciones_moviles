package com.example.viniloapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class VinilosE2ETests {

    @get:Rule
    var mActivityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun listAlbums() {
        Thread.sleep(10000)
        onView(withId(R.id.navigation_albums))
            .perform(click())

        onView(withId(R.id.albums_recycler_view))
            .check(matches(hasDescendant(withText("From Zero"))));
    }

    @Test
    fun listCollectors() {
        Thread.sleep(10000)
        onView(withId(R.id.navigation_collectors))
            .perform(click())

        Thread.sleep(5000)
        onView(withId(R.id.collectors_recycler_view))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Gerardo"))
                )
            )

        onView(withText("Gerardo"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun viewCollector() {
        Thread.sleep(10000)
        onView(withId(R.id.navigation_collectors))
            .perform(click())

        Thread.sleep(5000)
        onView(withId(R.id.collectors_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Gerardo")),
                    click()
                )
            )

        Thread.sleep(5000)
        onView(withId(R.id.collector_detail_name))
            .check(matches(withText("Name: Gerardo")))
    }
}