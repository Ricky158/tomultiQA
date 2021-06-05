package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SquareActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
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
        Button AlchemyEntryButton = findViewById(R.id.AlchemyEntryButton);
        AppMode = ChooseAppMode.getString("AppMode", ValueClass.NORMAL_MODE);
        assert AppMode != null;
        if(!AppMode.equals(ValueClass.GAME_MODE)){
            AlchemyEntryButton.setEnabled(false);
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.HintWordTran),
                    "App detected that you are not in [Game] Mode.\n"+
                            "In current Mode, SOME function here are not available.\n"+
                            "You can go to Setting, and enable it to open these function",
                    "Know",
                    "Nothing",
                    true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        //make number into financial form.
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
        MaterialCountingView.setText(SupportClass.ReturnKiloIntString(UserMaterial));
    }//end of basic values.


    //Resource system from MainActivity.
    //basic operation of resource, just do it automatically.
    @SuppressLint("SetTextI18n")
    private void GetPoint(int addNumber){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        UserPoint = UserPoint + addNumber;
        PointRecord = PointRecord + addNumber;
        SupportClass.saveLongData(this,"RecordDataFile","PointGotten",PointRecord);
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        //make number into financial form.
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
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


    public void OpenTalentActivity(View view){
        Intent i = new Intent(this,TalentActivity.class);
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

    public void OpenTimerActivity(View view){
        Intent i = new Intent(this,TimerActivity.class);
        startActivity(i);
    }

    public void OpenBackpackActivity(View view){
        Intent i = new Intent(this,BackpackActivity.class);
        startActivity(i);
    }

    public void OpenMarketActivity(View view){
        Intent i = new Intent(this,MarketActivity.class);
        startActivity(i);
    }//end of Function Layout management system.


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
                        if(FormulaText.contains("whosyourdaddy")){
                            SupportClass.saveIntData(SquareActivity.this,"CheatFile","CheatATK",FormulaNumber);
                        }
                        if(FormulaText.contains("/xp")){
                            SupportClass.saveIntData(SquareActivity.this,"EXPInformationStoreProfile","UserHaveEXP",FormulaNumber);
                        }
                        if(FormulaText.contains("maxrank")){
                            SupportClass.saveIntData(SquareActivity.this,"ExcessDataFile","LevelLimit",300);
                            SupportClass.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessRank",10);
                            SupportClass.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessATK",5000);
                            SupportClass.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessCR",25);
                            SupportClass.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessCD",50);
                        }
                        if(FormulaText.contains("unlockvhquest")){
                            SupportClass.saveIntData(SquareActivity.this,"BattleDataProfile","UserConflictFloor",ValueClass.CONFLICT_MAX_FLOOR);
                        }
                        //end of after input confirm.
                        Toast.makeText(getApplicationContext(),"Exchange Completed.", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }//end of Exchange Resource system.


    //website collection.
    //after input confirmed.
    //1.search all number in Resource Code String, and return the result as List.
    //thanks to: https://stackoverflow.com/questions/13440506/find-all-numbers-in-the-string !
    //2.get the number in String List, and transform it to int form.
    //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
    //2.1 get the first number in String List, and transform it to int form.
    //3.try to catch the error, if there are no number in Resource Code String, this method will give this variable a default value, to prevent it from crash.
    //4.according the Code, give user resource.
    //5.colors: https://blog.csdn.net/daichanglin/article/details/1563299 !
 }
