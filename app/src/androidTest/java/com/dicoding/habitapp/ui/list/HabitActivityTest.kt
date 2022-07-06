package com.dicoding.habitapp.ui.list

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.add.AddHabitActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@LargeTest
@RunWith(AndroidJUnit4::class)
class HabitActivityTest {
    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HabitListActivity::class.java)

    @Rule
    @JvmField
    val taskActivityRule = IntentsTestRule(HabitListActivity::class.java)

    @Before
    fun setUp(){
        ActivityScenario.launch(HabitListActivity::class.java)
    }

    @Test
    fun openAddHabit() {
        onView(withId(R.id.fab)).perform(ViewActions.click())
        intended(hasComponent(AddHabitActivity::class.java.name))
    }

}