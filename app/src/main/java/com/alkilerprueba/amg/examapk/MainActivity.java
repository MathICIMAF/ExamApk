package com.alkilerprueba.amg.examapk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.EigenvalueDecomposition;
import Jama.LUDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import io.github.kexanie.library.MathView;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> mathTexts;
    private RecyclerView.LayoutManager lManager;
    ArrayList<Exercise> itemsLim;
    ArrayList<Exercise> itemsDerivates;
    ArrayList<Exercise> itemsIntegrals;
    ArrayList<Exercise> test;
    SharedPreferences preference;
    SharedPreferences.Editor edit;
    public static String TOTALLim = "TotalLim";
    public static String TOTALDer = "TotalDer";
    public static String TOTALIteg = "TotalIteg";
    public static String RLim = "RLim";
    public static String RDer = "RDer";
    public static String RIteg = "RIteg";
    private InterstitialAd mInterstitialAd;

    int limPercent,derPercent,integPercent,totalPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize (this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete( InitializationStatus initializationStatus ) {

                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed
               // Toast.makeText (MainActivity.this, "AdMob Sdk Initialize "+ initializationStatus.toString(), Toast.LENGTH_LONG).show();

            }
        });

        ((AdView)findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

        preference = getPreferences(MODE_PRIVATE);
        edit = preference.edit();

        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter));

        mathTexts = new ArrayList<>();

        String[] values = getResources().getStringArray(R.array.exercises);
        itemsLim = new ArrayList<>();
        itemsDerivates = new ArrayList<>();
        itemsIntegrals = new ArrayList<>();
        test = new ArrayList<>();

        for (int i = 0; i < values.length; i ++){
            Exercise ex = new Exercise(values[i],i);
            if(ex.getTopic() == 1)
                itemsLim.add(ex);
            else if(ex.getTopic() == 2)
                itemsDerivates.add(ex);
            else if (ex.getTopic() == 3)
                itemsIntegrals.add(ex);
        }

        String[] topics = getResources().getStringArray(R.array.topics);

        Button limits = (Button)findViewById(R.id.limits);

        limits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic(itemsLim,1);
            }
        });
        Button derivates = (Button)findViewById(R.id.derivates);
        derivates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic(itemsDerivates,1);
            }
        });
        Button integrals = (Button)findViewById(R.id.integrals);
        integrals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic(itemsIntegrals,1);
            }
        });
        Button mix = (Button)findViewById(R.id.mix);
        mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTopic(itemsIntegrals,2);
            }
        });


    }

    void selectedTopic(List<Exercise> exercises, int type){
        final ProgressDialog[] progressDialog = new ProgressDialog[1];
        progressDialog[0] = new ProgressDialog(this);
        progressDialog[0].setTitle(getString(R.string.generating));
        progressDialog[0].setMessage(getString(R.string.please));
        progressDialog[0].setCancelable(false);
        progressDialog[0].setIndeterminate(true);
        new MyTask(progressDialog[0],exercises,this, type).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /*if (id == R.id.action_share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "https://play.google.com/store/apps/details?id=com.amg.benefits";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,R.string.share);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            return true;
        }
        */

        if (id == R.id.action_results){
            getResults();
            ResultsActivity.launch(this,limPercent,derPercent,integPercent,totalPercent);
        }
        return super.onOptionsItemSelected(item);
    }

    void getResults(){

        float totalLimits,totalDerivs, totalIntegs,
                rlimts, rderivs,rintegs;

        totalLimits = preference.getInt(TOTALLim,0);
        totalDerivs = preference.getInt(TOTALDer,0);
        totalIntegs = preference.getInt(TOTALIteg,0);

        rlimts = preference.getInt(RLim,0);
        rderivs = preference.getInt(RDer,0);
        rintegs = preference.getInt(RIteg,0);


        float total = totalDerivs+totalIntegs+totalLimits;
        float rtotal = rlimts+rderivs+rintegs;

        limPercent = Math.round((rlimts/totalLimits)*100);
        derPercent = Math.round((rderivs/totalDerivs)*100);
        integPercent = Math.round((rintegs/totalIntegs)*100);
        totalPercent = Math.round((rtotal/total)*100);

    }


    class MyTask extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        List<Exercise> exercises;
        Activity context;
        int type;
        public MyTask(ProgressDialog progressDialog, List<Exercise> exercises, Activity context, int type) {
            this.progressDialog = progressDialog;
            this.exercises = exercises;
            this.context = context;
            this.type = type;
        }

        protected String doInBackground(String... params) {
            if(type == 1)
                generateTest();
            else
                generateMixTest();
           return null;
        }

        public void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        public void onPostExecute(String unused) {
            TestActivity.preference = preference;
            TestActivity.edit = edit;
            TestActivity.launch(context,test);
            progressDialog.dismiss();
        }

        void generateTest(){
            List<Integer> index = generateIndex(7,exercises.size());
            for (int i = 0; i < index.size(); i++) {
                Exercise ex = exercises.get(index.get(i));
                ex.initializePositions();
                ex.setSelected(-1);
                ex.setViewAnswer(false);
                test.add(ex);
            }
        }

        void generateMixTest(){
            int N = 9;
            ArrayList<Exercise> totals = new ArrayList<>();
            totals.addAll(itemsLim);
            totals.addAll(itemsDerivates);
            totals.addAll(itemsIntegrals);
            List<Integer> indexTotal = generateIndex(N,totals.size());

            for (int i = 0; i < indexTotal.size(); i++) {
                Exercise ex = totals.get(indexTotal.get(i));
                ex.initializePositions();
                ex.setSelected(-1);
                ex.setViewAnswer(false);
                test.add(ex);
            }
        }

        List<Integer> generateIndex(int N,int Total){
            Random random = new Random();
            ArrayList<Integer> index = new ArrayList<>();
            index.add(random.nextInt(Total));
            int temp = 1;
            while (temp < N){
                int value = random.nextInt(Total);
                boolean itis = false;
                for (int i = 0; i < index.size(); i++){
                    if(value == index.get(i)){
                        itis = true;
                        break;
                    }
                }
                if (!itis) {
                    index.add(value);
                    temp++;
                }
                try {
                    Thread.sleep(89);
                }
                catch (Exception e){

                }
            }
            return index;
        }
    }

    @Override // android.support.v4.app.FragmentActivity
    public void onResume() {
        super.onResume();
        int times = this.preference.getInt("TIMES", 1);
        this.edit.putInt("TIMES", times + 1);
        this.edit.commit();

//        ((AdView)findViewById(R.id.adView1)).loadAd(new AdRequest.Builder().build());

        if (times % 5 == 0) {
            requestNewInterstitial();
            new Handler().postDelayed(new Runnable() {
                /* class com.example.adrianvelizanido.tema.MainActivity.AnonymousClass1 */

                public void run() {
                    if (MainActivity.this.mInterstitialAd.isLoaded()) {
                    }
                }
            }, 2500);
            showInterstitial();
        }
    }

    private void requestNewInterstitial() {
        this.mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void showInterstitial() {
        if (this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
        }
        requestNewInterstitial();
    }
}
