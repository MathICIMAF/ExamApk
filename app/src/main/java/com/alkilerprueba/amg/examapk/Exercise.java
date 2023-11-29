package com.alkilerprueba.amg.examapk;

import java.util.Random;

/**
 * Created by AMG on 13/12/2020.
 */
public class Exercise {
    String text,solution;
    String[] options;
    int topic,pos;
    int[] randomPostions;
    private int selected;
    private boolean viewAnswer;

    public Exercise(String info, int pos){
        parseInfo(info);
        this.pos = pos;
        randomPostions = initializePositions();
        selected = -1;
        viewAnswer = false;
    }

    private void parseInfo(String info) {
        String[] items = info.split("%");
        text = items[0].split("@")[1];
        solution = items[1].split("@")[1];
        topic = Integer.parseInt(items[2].split("@")[1].trim());
        options = new String[4];
        String[] options_parse = items[3].split("@")[1].split("#");
        for (int i = 0; i < 4; i++)
            options[i] = options_parse[i];

    }

    public void setSelected(int value){
        selected = value;
    }

    public int getSelected(){
        return selected;
    }

    public int [] initializePositions(){
        int[] res = new int[4];
        for (int i = 0; i < 4; i++)
            res[i] = -1;
        Random random = new Random();
        res[0] = random.nextInt(4);
        int temp = 1;
        while (temp < 4){
            int value = random.nextInt(4);
            boolean itis = false;
            for (int i = 0; i < 4; i++){
                if(value == res[i]){
                    itis = true;
                    break;
                }
            }
            if (!itis) {
                res[temp] = value;
                temp++;
            }
            try {
                Thread.sleep(20);
            }
            catch (Exception e){

            }
        }
        return res;
    }

    public String getText(){
        return text;
    }

    public int getTopic(){
        return topic;
    }

    public int getPos(){
        return pos;
    }

    public String getSolution(){
        return solution;
    }

    public String[] getOptions(){
        return options;
    }

    public int[] getRandomPositions(){
        return randomPostions;
    }

    public void setViewAnswer(boolean value){
        viewAnswer = value;
    }

    public boolean getViewAnswer(){
        return viewAnswer;
    }
}
