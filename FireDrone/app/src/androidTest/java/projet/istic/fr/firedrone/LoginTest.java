package projet.istic.fr.firedrone;


import android.content.ComponentName;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import projet.istic.fr.firedrone.LoginActivity;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivity = new ActivityTestRule<>(LoginActivity.class);


    @Test
    public void login1(){

        Espresso.onView(ViewMatchers.withId(R.id.loginField)).perform(typeText("toto"),closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.passField)).perform(typeText("titi"),closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.loginField))
                .check(ViewAssertions.matches(ViewMatchers.withText("toto")));

        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());


    }

    @Test
    public void loginToMain(){
        Intents.init();//Initializes Intents and begins recording intents.

        Espresso.onView(ViewMatchers.withId(R.id.loginField)).perform(typeText("tanguy"),closeSoftKeyboard());//test account


        Espresso.onView(ViewMatchers.withId(R.id.passField)).perform(typeText("tanguy"),closeSoftKeyboard());//test account



        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());


        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));

        Intents.release();

    }


}