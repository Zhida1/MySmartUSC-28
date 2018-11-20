package com.example.zhidachen.mysmartusc_28;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeleteKeywordTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init() {
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, new DashboardActivity()).addToBackStack(null).commit();
    }

    @Test
    public void DeleteKeyTest() {

        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.to_add_keyword)).perform(click());
        // add/type keyword "career" and close keyboard
        onView(withId(R.id.user_input)).perform(typeText("career"), closeSoftKeyboard());
        // click on add (keyword) button
        onView(withId(R.id.remove_keyword)).perform(click());
        // check if "Keyword has been removed" appears
        onView(withText("Keyword has been removed")).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }
}
