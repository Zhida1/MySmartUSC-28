package com.example.zhidachen.mysmartusc_28;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

public class AddContentCheckSender {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private int areasforcheck;

    @Before
    public void init() {
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, new DashboardActivity()).addToBackStack(null).commit();
    }

    @Test
    public void AddConCheckSend() {

        // click on type keyword to be directed to Preference/Add Keyword page
        onView(withId(R.id.to_add_keyword)).perform(click());

//        click spinner and choose first item
        onView(withId(R.id.check_area)).perform(click());
//        areasforcheck = R.array.areasforcheck;
//        onData(allOf(is(instanceOf(String.class)), is("Sender"))).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.user_input)).perform(typeText("career"), closeSoftKeyboard());
        onView(withId(R.id.add_keyword)).perform(click());

        // click on subject
        onView(withId(R.id.check_area)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.user_input)).perform(typeText("career"), closeSoftKeyboard());
        onView(withId(R.id.remove_keyword)).perform(click());

        onView(withText("Keyword does not exist")).inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }
}
