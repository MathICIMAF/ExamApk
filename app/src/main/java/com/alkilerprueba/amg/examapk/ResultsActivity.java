package com.alkilerprueba.amg.examapk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    public static int limitsPercent, derivPercent, integPercent, mixPercent;
    public static void launch(Activity activity, int limitsPercent, int derivPercent,
                              int integPercent, int mixPercent) {

        ResultsActivity.limitsPercent = limitsPercent;
        ResultsActivity.derivPercent = derivPercent;
        ResultsActivity.integPercent = integPercent;
        ResultsActivity.mixPercent = mixPercent;
        Intent intent = getLaunchIntent(activity);
        activity.startActivityForResult(intent, 1);
    }

    public static Intent getLaunchIntent(Context context ) {
        Intent intent = new Intent(context, ResultsActivity.class);
        return intent;
    }

    ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
    TextView progressText1,progressText2,
            progressText3,progressText4;

    SharedPreferences preference;
    SharedPreferences.Editor edit;
    //private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
        preference = getPreferences(MODE_PRIVATE);
        edit = preference.edit();

        //this.mInterstitialAd = new InterstitialAd(this);
        //this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter));


        progressBar1 = (ProgressBar)findViewById(R.id.progress1);
        progressBar2 = (ProgressBar)findViewById(R.id.progress2);
        progressBar3 = (ProgressBar)findViewById(R.id.progress3);
        progressBar4 = (ProgressBar)findViewById(R.id.progress4);

        progressText1 = (TextView)findViewById(R.id.textProgress1);
        progressText2 = (TextView)findViewById(R.id.textProgress2);
        progressText3 = (TextView)findViewById(R.id.textProgress3);
        progressText4 = (TextView)findViewById(R.id.textProgress4);

        String[] topics = getResources().getStringArray(R.array.topics);
        TextView text1 = (TextView)findViewById(R.id.text1);
        TextView text2 = (TextView)findViewById(R.id.text2);
        TextView text3 = (TextView)findViewById(R.id.text3);
        TextView text4 = (TextView)findViewById(R.id.text4);

        text1.setText(topics[0]);
        text2.setText(topics[1]);
        text3.setText(topics[2]);
        text4.setText(getString(R.string.total));


        updateProgress(limitsPercent,progressBar1,progressText1);
        updateProgress(derivPercent,progressBar2,progressText2);
        updateProgress(integPercent,progressBar3,progressText3);
        updateProgress(mixPercent,progressBar4,progressText4);
    }

    private void updateProgress(int percent,final ProgressBar progressBar,final TextView progressText) {
        final int start = 0;
        final int end = percent;

        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int t = start;
                while (t <= end) {
                    final int temp = t;
                    try {
                        Thread.sleep(70);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            progressBar.setProgress(temp);
                            progressText.setText(temp+"%");

                        }
                    });
                    t++;
                }
            }
        };
        new Thread(runnable).start();
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

        //((AdView)findViewById(R.id.adView1)).loadAd(new AdRequest.Builder().build());
    }


}
