package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SquareActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        InitializingResourceData();
        ImportAppMode();
    }

    //basic value of whole activity.
    int UserPoint;
    int PointRecord;
    int UserMaterial;
    String AppMode = "Normal";

    private void ImportAppMode(){
        SharedPreferences ChooseAppMode = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
        Button ConflictEntryButton = findViewById(R.id.ConflictEntryButton);
        Button DailyEntryButton = findViewById(R.id.DailyEntryButton);
        AppMode = ChooseAppMode.getString("AppMode", "Normal");
        assert AppMode != null;
        if(AppMode.equals("Normal")){
            ConflictEntryButton.setClickable(false);
            DailyEntryButton.setClickable(false);
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.HintWordTran),
                    "App detected that you don`t enable Game Mode yet.\n"+
                            "In current Mode, [Conflict] and [Daily] function are not available.\n"+
                            "You can go to Setting, and enable it to open these function",
                    "Know",
                    "Nothing",
                    true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountingView);
        TextView MaterialCountingView = findViewById(R.id.MaterialCountingView);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        PointCountingView.setText( UserPoint + "");
        MaterialCountingView.setText( UserMaterial + "");
    }//end of basic values.


    //Resource system from MainActivity.
    //basic operation of resource, just do it automatically.
    @SuppressLint("SetTextI18n")
    private void GetPoint(int addNumber){
        TextView PointCountingView = findViewById(R.id.PointCountingView);
        UserPoint = UserPoint + addNumber;
        PointRecord = PointRecord + addNumber;
        SupportClass.saveLongData(this,"RecordDataFile","PointGotten",PointRecord);
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        PointCountingView.setText(UserPoint + "");
    }//end of Resource system.

    //Function Layout management system.
    //OpenFunctionLayout method is abandoned. I just leave it for Learning Purpose.
//    public void OpenFunctionLayout(View view){
//        //0.preparation: listing all Function Layout in this activity.
//        ConstraintLayout MissionLayout = findViewById(R.id.MissionLayout);
//
//        //1.close all Function Layout before management, to do it easily.
//        MissionLayout.setVisibility(View.GONE);
//
//        //2.confirm that which button has been touched, and return it`s Text to use in next step as recognizing identifier.
//        //thanks to: https://stackoverflow.com/questions/3412180/how-to-determine-which-button-pressed-on-android !
//        Button AnyButtonView = findViewById(view.getId());
//        String ButtonText = AnyButtonView.getText().toString();
//
//        //3.open certain Layout.
//        if (ButtonText.equals(getString(R.string.ExchangeTitleTran))){
//            MissionLayout.setVisibility(View.VISIBLE);
//        }
//    }
    public void OpenConflictActivity(View view){
        Intent i = new Intent(this, ConflictActivity.class);
        startActivity(i);
    }

    public void OpenTalentActivity(View view){
        Intent i = new Intent(this,TalentActivity.class);
        startActivity(i);
    }

    public void OpenDailyActivity(View view){
        Intent i = new Intent(this,DailyActivity.class);
        startActivity(i);
    }

    public void OpenShopActivity(View view) {
        Intent i = new Intent(this,ShopActivity.class);
        startActivity(i);
    }

    public void OpenMissionActivity(View view){
        Intent i = new Intent(this,MissionActivity.class);
        startActivity(i);
    }

    public void OpenAlchemyActivity(View view){
        Intent i = new Intent(this,AlchemyActivity.class);
        startActivity(i);
    }

    public void OpenTourneyActivity(View view){
        Intent i = new Intent(this,TourneyActivity.class);
        startActivity(i);
    }

    public void OpenTimerActivity(View view){
        Intent i = new Intent(this,TimerActivity.class);
        startActivity(i);
    }
    //end of Function Layout management system.


    //Exchange Resource system. show a dialog with input function.
    //thanks to:
    //https://blog.csdn.net/feiqinbushizheng/article/details/78837711 !
    //https://stackoverflow.com/questions/12876624/multiple-edittext-objects-in-alertdialog !
    //https://stackoverflow.com/questions/6261456/layout-orientation-in-code !
    public void ShowResourceCodeInput(View view){
        //0.preparation.
        final LinearLayout InputLayout = new LinearLayout(this);
        final EditText FormulaView = new EditText(this);
        final EditText NumberView = new EditText(this);
        InputLayout.setOrientation(LinearLayout.VERTICAL);
        InputLayout.addView(FormulaView);
        InputLayout.addView(NumberView);
        FormulaView.setInputType(InputType.TYPE_CLASS_TEXT);
        FormulaView.setHint(getString(R.string.InputFormulaWordTran));
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        NumberView.setHint(getString(R.string.InputFormulaNumberWordTran));

        new AlertDialog.Builder(this)
                .setTitle("Input Resource Code")
                .setView(InputLayout)
                .setPositiveButton(getString(R.string.ConfirmWordTran), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //after input confirmed.
                        String FormulaText = FormulaView.getText().toString();
                        int FormulaNumber;//store the exact Number in Code in int form.
                        //try to catch the error, if there are no number in Resource Code String, this method will give this variable a default value, to prevent it from crash.
                        try {
                            FormulaNumber = Integer.parseInt(NumberView.getText().toString());
                        }
                        catch (NumberFormatException e)
                        {
                            FormulaNumber = 0;
                        }
                        //according the Code, give user resource.
                        if(FormulaText.contains("greedisgood")){
                            GetPoint(FormulaNumber);
                        }
                        //end of after input confirm.
                        Toast.makeText(getApplicationContext(),"Exchange Completed.", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }//end of Exchange Resource system.


    //basic support system.
    public void GoToMainPage(View view){
        Intent i = new Intent(this, MainActivity.class);
        //if this state is 1, it means Boss have information transporting from SquareAbility.
        i.putExtra("BossState",1);
        startActivity(i);
    }//end of basic support system.


    //website collection.
    //after input confirmed.
    //1.search all number in Resource Code String, and return the result as List.
    //thanks to: https://stackoverflow.com/questions/13440506/find-all-numbers-in-the-string !
    //2.get the number in String List, and transform it to int form.
    //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
    //2.1 get the first number in String List, and transform it to int form.
    //3.try to catch the error, if there are no number in Resource Code String, this method will give this variable a default value, to prevent it from crash.
    //4.according the Code, give user resource.
 }
