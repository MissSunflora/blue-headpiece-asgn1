package com.example.lab2_nesabertanico;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    TextView    questionID;
    Button      tru_id, fals_id;
    ProgressBar progressBar;
    ImageView   photoID;

    int index_question = 0;
    int counter = 0;
    /*
    Question[] bank_questions = new Question[]{
            new Question(R.string.i, true),
            new Question(R.string.ii, true),
            new Question(R.string.iii, true),
            new Question(R.string.iv, false),
            new Question(R.string.v, true)
    };*/
    List<Question> bank_questions = Arrays.asList(
            new Question(R.string.i, true, R.drawable.tulips),
            new Question(R.string.ii, true, R.drawable.broccoli),
            new Question(R.string.iii, true, R.drawable.sunflower),
            new Question(R.string.iv, false, R.drawable.smell),
            new Question(R.string.v, false, R.drawable.orchid)
    );


    public class Question {
        private int tv_quest;
        private boolean btn_tf;
        private int img;
        public Question(int tq, boolean answer, int image) {
            tv_quest = tq;
            btn_tf = answer;
            img = image;
        }

        public int getTv_quest() {
            return tv_quest;
        }

        public boolean isAnswerCorrect() {
            return btn_tf;
        }

        public int getImg() { return img; }

    }

    final int PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / bank_questions.size());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionID      = findViewById(R.id.questionID);
        tru_id          = findViewById(R.id.tru_id);
        fals_id         = findViewById(R.id.fals_id);
        progressBar     = findViewById(R.id.progressBar);
        photoID         = findViewById(R.id.photoID);

        try{
            Collections.shuffle(bank_questions);
            updateQuestion();

            tru_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(true);
                    updateQuestion();
                }
            });

            fals_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(false);
                    updateQuestion();
                }
            });

        }catch (Exception e){
            toastMsg("Error: " + e);
            e.printStackTrace();
        }

    }

    public void updateQuestion() {

        if (counter == 5) {
            resultDialog(MainActivity.this);
        }else{
            try{
                int question = bank_questions.get(index_question).getTv_quest();
                questionID.setText(question);
                int q = bank_questions.get(index_question).getImg();
                photoID.setImageResource(q);
            }catch (Exception e){
                toastMsg("Error updateQuestion: " + e);
                e.printStackTrace();
            }
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        try{
            boolean answerIsTrue = bank_questions.get(index_question).isAnswerCorrect();

            if (userPressedTrue == answerIsTrue) {
                toastMsg("Correct");
                counter = counter + 1;
                progressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
                index_question = index_question + 1;

            } else {
                toastMsg("Incorrect");
                resultDialog(MainActivity.this);
            }


        }catch (Exception e){
            toastMsg("Error CheckAnswer: " + e);
            e.printStackTrace();
        }
    }

    private void resultDialog(Activity activity){
        new AlertDialog.Builder(activity)

                .setTitle("Result")
                .setMessage("Your score is: " +counter + " out of 5.")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (index_question <= 4){
                            toastMsg("Cancel");
                        }else{
                            counter=0;
                            index_question = 0;
                            updateQuestion();
                            progressBar.setProgress(0);
                        }
                    }
                })
                .setPositiveButton("Repeat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        counter=0;
                        index_question = 0;
                        Collections.shuffle(bank_questions);
                        updateQuestion();
                        progressBar.setProgress(0);
                    }
                })
                .show();
    }


    /**
     *this method makes it easier for me to use toast to output a message in the screen
     * @param msg
     */
    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
