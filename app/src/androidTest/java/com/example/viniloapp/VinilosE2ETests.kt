package com.example.viniloapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import org.hamcrest.CoreMatchers.containsString
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
        Thread.sleep(5000)
        onView(withId(R.id.navigation_albums))
            .perform(click())

        onView(withId(R.id.albums_recycler_view))
            .check(matches(hasDescendant(withText("Buscando América Prueba"))));

        Thread.sleep(5000)
        onView(withId(R.id.albums_recycler_view))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Don gato"))
                )
            )
        onView(withText("Don gato"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun listCollectors() {
        Thread.sleep(5000)
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
        Thread.sleep(5000)
        onView(withId(R.id.navigation_collectors))
            .perform(click())

        Thread.sleep(5000)
        onView(withId(R.id.collectors_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Jose Ortegon")),
                    click()
                )
            )

        Thread.sleep(5000)
        onView(withId(R.id.collector_detail_name))
            .check(matches(withText("Name: Jose Ortegon")))

        onView(withId(R.id.collector_correo))
            .check(matches(withText("Email: jose@gmail.com")))

        onView(withId(R.id.collector_telefono))
            .check(matches(withText("Phone: 12345678")))
    }

    @Test
    fun createAlbumFormFieldsExist() {
        Thread.sleep(5000)
        // Navegar a la sección de álbumes
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(2000)
        // Oprimir el botón de crear álbum
        onView(withId(R.id.button_add_album)).perform(click())
        Thread.sleep(2000)
        // Validar que los campos requeridos existen en el formulario
        onView(withId(R.id.editTextAlbumName)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextAlbumCover)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextAlbumReleaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextAlbumDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextAlbumGenre)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextAlbumRecordLabel)).check(matches(isDisplayed()))
    }

    @Test
    fun detalleAlbum(){
        Thread.sleep(5000)
        // Navegar a la sección de álbumes
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.albums_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Buscando América Prueba")),
                    click()
                )
            )
        Thread.sleep(5000)
        onView(withId(R.id.album_record_label))
            .check(matches(withText("Sello discográfico: Elektra")))

        onView(withId(R.id.album_release_date))
            .check(matches(withText("Fecha de lanzamiento: 1984-08-01T00:00:00.000Z")))

        onView(withId(R.id.album_genre))
            .check(matches(withText("Género: Salsa")))

    }
}