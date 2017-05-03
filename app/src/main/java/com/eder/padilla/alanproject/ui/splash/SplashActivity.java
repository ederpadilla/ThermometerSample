package com.eder.padilla.alanproject.ui.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.daimajia.androidanimations.library.Techniques;
import com.eder.padilla.alanproject.R;
import com.eder.padilla.alanproject.ui.main.Main2Activity;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashActivity extends AwesomeSplash {
    @Override
    public void initSplash(ConfigSplash configSplash) {

			/* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2300); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_firewall); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        configSplash.setTitleSplash("Banco de sangre");


        /***Customize Title
        configSplash.setTitleSplash("My Awesome App");
        configSplash.setTitleTextColor(R.color.Wheat);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/**/

    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
        //transit to another activity here
        //or do whatever you want
    }
}


