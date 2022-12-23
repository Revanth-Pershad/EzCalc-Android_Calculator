package com.example.easycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    TextView currentTV, resultTV;
    Button btnC, btnOpen, btnClose, btnDiv, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn0, btn9, btnAC, btnDot, btnAdd, btnMinus, btnMul, btnEquals;
    String currString = "",resString = "=";
    HorizontalScrollView currScroll, resScroll;
    boolean isNew = false;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    ValueAnimator animatorLargeRes = ValueAnimator.ofFloat(30, 45);
    ValueAnimator animatorLargeCurr = ValueAnimator.ofFloat(30, 45);
    ValueAnimator animatorSmallRes = ValueAnimator.ofFloat(45, 30);
    ValueAnimator animatorSmallCurr = ValueAnimator.ofFloat(45, 30);

    Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBtnId();
        setAnimationFontLargeRes(resultTV);
        setAnimationFontSmallRes(resultTV);
        setAnimationFontLargeCurr(currentTV);
        setAnimationFontSmallCurr(currentTV);
        myTimer = new Timer();

        animatorLargeCurr.start();
        animatorSmallRes.start();

    }

    void setBtnId(){
        currentTV = findViewById(R.id.text_current);
        resultTV = findViewById(R.id.text_result);
        currScroll = findViewById(R.id.currentScroll);
        resScroll = findViewById(R.id.resultScroll);
        assignListener(btn0 ,R.id.button_0);
        assignListener(btn1 ,R.id.button_1);
        assignListener(btn2 ,R.id.button_2);
        assignListener(btn3 ,R.id.button_3);
        assignListener(btn4 ,R.id.button_4);
        assignListener(btn5 ,R.id.button_5);
        assignListener(btn6 ,R.id.button_6);
        assignListener(btn7 ,R.id.button_7);
        assignListener(btn8,R.id.button_8);
        assignListener(btn9 ,R.id.button_9);
        assignListener(btnC ,R.id.button_c);
        assignListener(btnClose ,R.id.button_closed_brakcet);
        assignListener(btnOpen ,R.id.button_open_bracket);
        assignListener(btnDiv ,R.id.button_divide);
        assignListener(btnAC ,R.id.button_ac);
        assignListener(btnDot ,R.id.button_dot);
        assignListener(btnAdd,R.id.button_plus);
        assignListener(btnMinus ,R.id.button_minus);
        assignListener(btnMul ,R.id.button_multiply);
        assignListener(btnEquals ,R.id.button_equal);
    }

    void assignListener(Button btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this::onClick);
    }

    void setAnimationFontLargeRes(TextView txtView){
        animatorLargeRes.setDuration(250);
        animatorLargeRes.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                txtView.setTextSize(animatedValue);
            }
        });
    }

    void setAnimationFontLargeCurr(TextView txtView){
        animatorLargeCurr.setDuration(250);
        animatorLargeCurr.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                txtView.setTextSize(animatedValue);
            }
        });
    }

    void setAnimationFontSmallRes(TextView txtView){
        animatorSmallRes.setDuration(250);
        animatorSmallRes.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                txtView.setTextSize(animatedValue);
            }
        });
    }

    void setAnimationFontSmallCurr(TextView txtView){
        animatorSmallCurr.setDuration(250);
        animatorSmallCurr.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                txtView.setTextSize(animatedValue);
            }
        });
    }

    public void onClick(View view){
        Button btn = (Button) view;
        String btnString = (btn.getText().toString());

        if(isNew == true){
            if(Character.isDigit(btnString.charAt(0)) || btnString == "."){
                currString = "0";
            }

            animatorLargeCurr.start();
            animatorSmallRes.start();
            setCurrView(currString);
            isNew = false;
        }

        switch(btnString){
            case "AC":
                currString = "0";
                resString = "0";
                resultTV.setText(resString);
                break;

            case  "C":
                if(currString.length() < 2)break;
                currString = currString.substring(0, currString.length() - 1);
                break;

            case "=":
                setResult();
                isNew = true;
                animatorLargeRes.start();
                animatorSmallCurr.start();
                break;

            default:
                if(currString=="0")currString = btnString;
                else currString = currString + btnString;
                break;
        }

        setResult();
        setCurrView(currString);

    }

    void setResult(){
        resString = getResult(currString);
        resultTV.setText(resString);

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                resScroll.fullScroll(View.FOCUS_RIGHT);
            }
        }, 0, 250);

    }

    void setCurrView(String curr){
        currentTV.setText(curr);

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currScroll.fullScroll(View.FOCUS_RIGHT);
            }
        }, 0, 250);
    }

    String getResult(String exp){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            return String.format("%.2f", context.evaluateString(scriptable, exp, "Javascript", 1, null));
        }
        catch (Exception e){
            return "Error";
        }
    }

}