package com.example.zhidachen.mysmartusc_28;

import android.app.Activity;
import android.app.Fragment;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddDuplicateKeywordTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    public String keywordToBeTested = "aisngaiianoidsfn";

    @Before
    public void init() {
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, new DashboardActivity()).addToBackStack(null).commit();
    }

    @Test
    public void AddDuplicate() {

        // Step 1: add keyword
        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.to_add_keyword)).perform(click());
        // add/type keyword and close keyboard
        onView(withId(R.id.user_input)).perform(typeText(keywordToBeTested), closeSoftKeyboard());
        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.add_keyword)).perform(click());

        // Step 2: check duplicated keyword
        // add/type keyword (that already exists) and close keyboard
        onView(withId(R.id.user_input)).perform(typeText(keywordToBeTested), closeSoftKeyboard());
        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.add_keyword)).perform(click());
        // check if "Keyword already exists" appears
        onView(withText("Keyword already exists")).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}