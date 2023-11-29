package com.alkilerprueba.amg.examapk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import io.github.kexanie.library.MathView;

public class ExplanationActivity extends AppCompatActivity {

    public static String text;
    public static void launch(Activity activity, String text) {
        Intent intent = getLaunchIntent(activity);
        activity.startActivityForResult(intent, 1);
        ExplanationActivity.text = text;
    }

    public static Intent getLaunchIntent(Context context ) {
        Intent intent = new Intent(context, ExplanationActivity.class);
        return intent;
    }

    SharedPreferences preference;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

        MathView expl = (MathView) findViewById(R.id.explanation);
        expl.setText(ExplanationActivity.text);

        preference = getPreferences(MODE_PRIVATE);
        edit = preference.edit();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onResume() {
        super.onResume();
        int times = this.preference.getInt("TIMES", 1);
        this.edit.putInt("TIMES", times + 1);
        this.edit.commit();

    }

}
