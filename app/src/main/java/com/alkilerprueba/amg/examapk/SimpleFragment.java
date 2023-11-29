package com.alkilerprueba.amg.examapk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import io.github.kexanie.library.MathView;

/**
 * Created by AMG on 25/12/2020.
 */
public class SimpleFragment extends Fragment {
    // Holds the fragment id passed in when created
    public static final String MESSAGE = "";
    public static final String MODE = "INDEX";
    public static int clicks = 0;
    // Our newInstance method which we call to make a new Fragment
    public static SimpleFragment newInstance(int ex)
    {
        // Create the fragment
        SimpleFragment fragment = new SimpleFragment();
// Create a bundle for our message/id
        Bundle bundle = new Bundle(1);
// Load up the Bundle
        //bundle.putString(MESSAGE, message);
        bundle.putInt(MODE,ex);
// Call setArguments ready for when onCreate is called
        fragment.setArguments(bundle);
        return fragment;
    }

    MathView[]answers;
    LinearLayout[] linearLayouts;
    int selected;
    Exercise exercise;
    ProgressBar progressBar;
    TextView textProgress;
    int ex;
    //creating Object of Rewarded Ad Callback
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Get the id from the Bundle
        //String message = getArguments().getString(MESSAGE);
        ex = getArguments().getInt(MODE);
        exercise = TestActivity.exercises.get(ex);
        progressBar = ((TestActivity)getActivity()).progress;
        textProgress = ((TestActivity)getActivity()).textProgress;
        View view ;
        view = inflater.inflate(R.layout.question_fragment,
                container, false);
        MathView question = (MathView)view.findViewById(R.id.item);
        question.setText(exercise.getText());
        ((AdView)view.findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());
        //((AdView)view.findViewById(R.id.adView1)).loadAd(new AdRequest.Builder().build());

        answers = new MathView[4];
        answers[0] = (MathView)view.findViewById(R.id.ans0);
        answers[1] = (MathView)view.findViewById(R.id.ans1);
        answers[2] = (MathView)view.findViewById(R.id.ans2);
        answers[3] = (MathView)view.findViewById(R.id.ans3);

        int[] positions = exercise.getRandomPositions();

        answers[0].setText(exercise.getOptions()[positions[0]]);
        answers[1].setText(exercise.getOptions()[positions[1]]);
        answers[2].setText(exercise.getOptions()[positions[2]]);
        answers[3].setText(exercise.getOptions()[positions[3]]);
        linearLayouts = new LinearLayout[4];
        linearLayouts[0] = (LinearLayout)view.findViewById(R.id.linear0);
        linearLayouts[1] = (LinearLayout)view.findViewById(R.id.linear1);
        linearLayouts[2] = (LinearLayout)view.findViewById(R.id.linear2);
        linearLayouts[3] = (LinearLayout)view.findViewById(R.id.linear3);

        if (exercise.getSelected() != -1){
            linearLayouts[exercise.getSelected()].setBackgroundResource(R.drawable.bar_back3);
        }
        for (int i = 0; i < linearLayouts.length; i++){
            final int pos = i;
            linearLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected(pos);
                    exercise.setSelected(pos);
                }
            });
        }
        final Button expl = (Button)view.findViewById(R.id.view);
        expl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedExplanation(ex);
            }
        });
        Button check = (Button)view.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!exercise.getViewAnswer()) {
                    clickRightAnswer();
                    expl.setEnabled(true);
                    exercise.setViewAnswer(true);
                }
            }
        });
        if (exercise.getViewAnswer())
            expl.setEnabled(true);
        return view;
    }

    public void selected(int pos){
        if(exercise.getSelected() == -1 || !exercise.getViewAnswer()) {
            this.selected = pos;
            for (int i = 0; i < answers.length; i++) {
                if (i != pos) {
                    linearLayouts[i].setBackgroundResource(R.drawable.bar_back2);
                } else {
                    linearLayouts[i].setBackgroundResource(R.drawable.bar_back3);
                }
            }
        }
    }

    void clickRightAnswer(){
        final int[] drawables = new int[2];
        drawables[0] = R.drawable.bar_back2;
        drawables[1] = R.drawable.back_right;
        int right = 0;
        for (int i = 0; i < answers.length; i++){
            if(answers[i].getText().compareTo(exercise.getOptions()[0]) == 0){
                if(i == selected) {
                    drawables[0] = R.drawable.bar_back3;
                    updateProgress();
                    updatePercent(1);
                }
                else
                    updatePercent(0);
                right = i;
                break;
            }
        }
        final int r = right;
        final Context context = getContext();
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int t = 0;
                while (true) {
                    final int temp = t;
                    try {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            linearLayouts[r].setBackgroundResource(drawables[temp%2]);

                        }
                    });
                    t++;
                }
            }
        };
        new Thread(runnable).start();
    }

    private void updateProgress() {
        final int start = progressBar.getProgress();
        int N = TestActivity.exercises.size();
        final int cant = 100/N;
        final int end = (Math.abs(100 - (start+cant)) < 5)? 100:(start+cant);
        final android.os.Handler handler = new android.os.Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                int t = start;
                while (t <= end) {
                    final int temp = t;
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            progressBar.setProgress(temp);
                            textProgress.setText(temp+"%");

                        }
                    });
                    t++;
                }
            }
        };
        new Thread(runnable).start();
    }

    void clickedExplanation(int ex){
        launchActivitity1(ex);
    }

    void launchActivitity1(int ex){
        ExplanationActivity.launch(this.getActivity(),TestActivity.exercises.get(ex).getSolution());
    }

    void updatePercent(int right){
        switch (exercise.topic){
            case 1:
                updateTopicPercent(right,MainActivity.RLim,MainActivity.TOTALLim);
                break;
            case 2:
                updateTopicPercent(right,MainActivity.RDer,MainActivity.TOTALDer);
                break;
            default:
                updateTopicPercent(right,MainActivity.RIteg,MainActivity.TOTALIteg);
                break;
        }
    }
    void updateTopicPercent(int val,String right, String total){
        SharedPreferences.Editor edit = TestActivity.edit;
        SharedPreferences preferences = TestActivity.preference;

        int t = preferences.getInt(total,0);
        int r = preferences.getInt(right,0);

        edit.putInt(right,r+val);
        edit.putInt(total,t+1);
        edit.commit();
    }
}
