package com.example.android.tomultiqa;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.example.android.tomultiqa.ConflictActivity.CONFLICT_MAX_FLOOR;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//LinearLayout divider: https://blog.csdn.net/qq_35928566/article/details/101773490 !

public class SquareActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        Thread thread = new Thread(() -> {
            LoadEvent();
            ImportAppMode();
        });
        thread.start();
        ImportExperiment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializingResourceData();
    }

    //thanks to: https://blog.csdn.net/z8711042/article/details/28903275 !
    //Override system return button method.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;//keep it here!
        }
        return super.onOptionsItemSelected(item);
    }


    //basic value of whole activity.
    ResourceIO resourceIO;
    String AppMode = "Normal";

    private void ImportAppMode(){
        SharedPreferences ChooseAppMode = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
        AppMode = ChooseAppMode.getString("AppMode", ValueLib.NORMAL_MODE);
    }

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        resourceIO = new ResourceIO(this);
        //make number into financial form.
        PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        MaterialCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserMaterial));
    }//end of basic values.


    //Import Experiment Function.
    private void ImportExperiment(){
        boolean IsItemFuncOn = SupportLib.getBooleanData(this,"EXPFuncFile","ItemSystemEnable",false);
        Button OpenBackpackButton = findViewById(R.id.OpenBackpackButton);
        Button OpenMarketButton = findViewById(R.id.OpenMarketButton);
        Button OpenCraftButton = findViewById(R.id.OpenCraftButton);
        OpenBackpackButton.setEnabled(IsItemFuncOn);
        OpenMarketButton.setEnabled(IsItemFuncOn);
        OpenCraftButton.setEnabled(IsItemFuncOn);
    }


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


    //Event system.
    int AppMonth;
    int AppDay;
    String EventName = "";
    String EventScript = "";
    
    private void LoadEvent(){
        //1.get system time.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar c = Calendar.getInstance();
            AppMonth = c.get(Calendar.MONTH) + 1;//month is based on 0.
            AppDay = c.get(Calendar.DAY_OF_MONTH);
        }else{
            Time time = new Time();
            time.setToNow();
            AppMonth = time.month + 1;//month is based on 0.
            AppDay = time.monthDay;
        }
        //2.decide which Event will be launched.
        //2.1 the Event based on specific Days.
        switch (AppDay){
            case 1:
                EventName = getString(R.string.AlchemyBottleEventTran);
                EventScript = getString(R.string.AlchemyBottleEventScriptTran);
                break;
            case 28:
                EventName = getString(R.string.ShopSaleEventTran);
                EventScript = getString(R.string.ShopSaleEventScriptTran);
                break;
            default:
                break;
        }
    }


    public void OpenTalentActivity(View view){
        Intent i = new Intent(this,TalentActivity.class);
        startActivity(i);
    }

    public void OpenShopActivity(View view) {
        Intent i = new Intent(this,ShopActivity.class);
        i.putExtra("EventName",EventName);
        i.putExtra("EventScript",EventScript);
        startActivity(i);
    }

    public void OpenMissionActivity(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.NoticeWordTran),
                "This page is not available for current version of App. You can't excess it.",
                getString(R.string.ConfirmWordTran));
        //Intent i = new Intent(this,MissionActivity.class);
        //startActivity(i);
    }

    public void OpenAlchemyActivity(View view){
        if(!AppMode.equals(ValueLib.GAME_MODE)){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    getString(R.string.NotInGameModeTran),
                    getString(R.string.ConfirmWordTran)
            );
        }else{
            Intent i = new Intent(this,AlchemyActivity.class);
            i.putExtra("EventName",EventName);
            i.putExtra("EventScript",EventScript);
            startActivity(i);
        }
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
    }

    public void OpenCraftActivity(View view){
        Intent i = new Intent(this,CraftActivity.class);
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
                .setTitle("Input Code")
                .setView(InputLayout)
                .setPositiveButton(getString(R.string.ConfirmWordTran), (dialogInterface, i) -> {
                    //after input confirmed.
                    String FormulaText = FormulaView.getText().toString();
                    int FormulaNumber = SupportLib.getInputNumber(NumberView);//store the exact Number in Code in int form.
                    //according the Code, give user resource.
                    switch (FormulaText){
                        case "greedisgood":
                            TextView PointCountingView = findViewById(R.id.PointCountInMarket);
                            resourceIO.GetPoint(FormulaNumber);
                            resourceIO.ApplyChanges(this);
                            //make number into financial form.
                            PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
                            break;
                        case "whosyourdaddy":
                            SupportLib.saveIntData(SquareActivity.this,"CheatFile","CheatATK",FormulaNumber);
                            break;
                        case "/xp":
                            SupportLib.saveIntData(SquareActivity.this,"EXPInformationStoreProfile","UserHaveEXP",FormulaNumber);
                            break;
                        case "maxrank":
                            SupportLib.saveIntData(SquareActivity.this,"ExcessDataFile","LevelLimit",300);
                            SupportLib.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessRank",10);
                            SupportLib.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessATK",5000);
                            SupportLib.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessCR",25);
                            SupportLib.saveIntData(SquareActivity.this,"ExcessDataFile","LevelExcessCD",50);
                            break;
                        case "unlockvhquest":
                            SupportLib.saveIntData(SquareActivity.this,"BattleDataProfile","UserConflictFloor",CONFLICT_MAX_FLOOR);
                            break;
                        case "goodmorning":
                            SupportLib.saveIntData(SquareActivity.this,"TimerSettingProfile","SignDay",-1);
                            Intent intent1 = new Intent(this, ListActivity.class);
                            intent1.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent1);
                            break;
                        case "resetexp":
                            ExpIO expIO = new ExpIO(this);
                            expIO.UserLevel = 1;
                            expIO.UserHaveEXP = 0;
                            expIO.ApplyChanges(this);
                            break;
                        case "WipeQuestData":
                            QuestDbHelper questDbHelper = new QuestDbHelper(this);
                            questDbHelper.DeleteEntire(questDbHelper.getWritableDatabase());
                            break;
                        case "WipeItemData":
                            ItemDbHelper itemDbHelper = new ItemDbHelper(this);
                            itemDbHelper.DeleteEntire(itemDbHelper.getWritableDatabase());
                            break;
                        default:
                            break;
                    }
                    //end of after input confirm.
                    Toast.makeText(this,"Exchange Completed.", Toast.LENGTH_SHORT).show();
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
