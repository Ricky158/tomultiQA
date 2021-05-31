package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TourneyActivity extends AppCompatActivity {
    //name: https://cn.bing.com/dict/search?q=tourney&FORM=BDVSP6&mkt=zh-cn !

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourney);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.

        //initializing MaxEvaluation number.
        TextView MaxEvaluationView = findViewById(R.id.MaxEvaluationView);
        MaxEvaluationView.setText(SupportClass.getLongData(this,"TourneyDataFile","MaxPtRecord",0) + "");
    }

    //Tourney system.
    long PtValue = 0;
    int BossHP = 1000;
    int BossTurn = 10;
    double PtIndex = 0;
    //record the Abilities which boss need to have.
    ArrayList<String> AbilityList = new ArrayList<>();

    //lv.3 method, main method of start battle.
    @SuppressLint("SetTextI18n")
    public void StartTourney(View view){
        //2.Set information needed.
        if(BossHP > 1 && BossTurn > 0 && PtValue > 0){
            //"Start"mode: put data in intent and start MainActivity.
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("Name",getString(R.string.PastNameTran));
            i.putExtra("Level", SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1) );
            i.putExtra("HP",BossHP);
            i.putExtra("SD",0);
            i.putExtra("ModeNumber",1);
            i.putExtra("Turn",BossTurn);
            i.putExtra("EXPReward",0);
            i.putExtra("PointReward",0);
            i.putExtra("MaterialReward",0);
            i.putExtra("BossAbility",AbilityList);
            i.putExtra("BossPtValue",PtValue);
            //if this state is 1, it means Boss have information transporting from SquareAbility.
            i.putExtra("BossState",3);
            startActivity(i);
            finish();
        }
    }


    //lv.2 method. pop up a dialog to let user edit Boss HP value.
    public void ShowBossHPInput(View view){
        final EditText InputView = new EditText(this);
        final TextView BossHPView = findViewById(R.id.BossHPView);
        //set this EditText only accept Number input.
        InputView.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle("Input Boss HP Number")
                .setView(InputView)
                .setPositiveButton(getString(R.string.ConfirmWordTran), new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String InputText = InputView.getText().toString();
                        //1. get the number in String form, and transform it to int form.
                        //thanks to: https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java?rq=1 !
                        int NumberInText = Integer.parseInt(InputText);//store the exact Number in Code in int form.
                        try {
                            //2. get the first number in String, and transform it to int form.
                            if(NumberInText > 0 && !InputText.isEmpty()){
                                BossHP = NumberInText;
                            }else if(NumberInText <= 0){
                                SupportClass.CreateOnlyTextDialog(TourneyActivity.this,
                                        getString(R.string.ErrorWordTran),
                                        "Input Fail.\n--Not excepted Input.\n(1 ~ 200,000,000)",
                                        getString(R.string.ConfirmWordTran),
                                        "Nothing",
                                        true);
                            }else{
                                SupportClass.CreateOnlyTextDialog(TourneyActivity.this,
                                        getString(R.string.ErrorWordTran),
                                        "Input Fail.\n--Data Casting Error.",
                                        getString(R.string.ConfirmWordTran),
                                        "Nothing",
                                        true);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            BossHP = 1000;
                            //3.try to catch the error, if there are no number in Resource Code String.
                            //this method will give this variable a default value, to prevent it from crash.
                        }
                        BossHPView.setText(BossHP + "");
                        CalculateTotalPt();
                        Toast.makeText(getApplicationContext(),"Set Value Completed.",Toast.LENGTH_LONG).show();
                        //end of after input confirm.
                    }
                }).setNegativeButton(getString(R.string.CancelWordTran),null).show();
    }

    //lv.2 method, main method.
    @SuppressLint("SetTextI18n")
    public void AddBossTurn(View view){
        TextView BossTurnView = findViewById(R.id.BossTurnView);
        if(BossTurn < 1000){
            BossTurn = BossTurn + 1;
        }
        BossTurnView.setText(BossTurn + "");
        CalculateTotalPt();
    }

    //lv.2 method, main method.
    @SuppressLint("SetTextI18n")
    public void MinusBossTurn(View view){
        TextView BossTurnView = findViewById(R.id.BossTurnView);
        if(BossTurn > 1){
            BossTurn = BossTurn - 1;
        }
        BossTurnView.setText(BossTurn + "");
        CalculateTotalPt();
    }

    //lv.2 method, main method.
    public void ChangeChoice(View view){
        //0.preparation.
        CheckBox AnyCheckBox = findViewById(view.getId());
        String CheckBoxName = AnyCheckBox.getText().toString();

        boolean IsChecked = AnyCheckBox.isChecked();
        //1.CheckBox Data Collection.
        //PtValue is percent(%) number.
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.TrialAbilityTran),30);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.RushAbilityTran),17);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FragileAbilityTran),-70);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.WeakSpotAbilityTran),-25);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.SlowAbilityTran),-17);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.CorrosionAbilityTran),25);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.CurseAbilityTran),60);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.DiversionAbilityTran),25);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.RecoverAbilityTran),85);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.LastHurtAbilityTran),-105);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ShieldAbilityTran),35);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FastStepAbilityTran),100);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ProudAbilityTran),36);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.FearAbilityTran),8);
        ChangeAbilityList(CheckBoxName,IsChecked,getString(R.string.ReTimeAbilityTran),-33);

        //2.after the Boss Ability Chosen, calculate the PtValue.
        CalculateTotalPt();
    }

    //lv.2 method, sub method of ChangeChoice() and CancelAllAbility() method.
    private void ChangeAbilitySub(View view){

    }

    //lv.1 method, sub method of ChangeChoice() method.
    private void ChangeAbilityList(String CheckBoxName, boolean IsChecked, String AbilityName, double PtValue){
        if(CheckBoxName.equals(AbilityName) && IsChecked){
            AbilityList.add(AbilityName);
            PtIndex = PtIndex + PtValue / 10;
        }else if(CheckBoxName.equals(AbilityName)){
            //!IsChecked is not necessary.
            AbilityList.remove(AbilityName);
            PtIndex = PtIndex - PtValue / 10;
        }
    }

    //lv.1 method, calculate TotalPtValue, sub method.
    @SuppressLint("SetTextI18n")
    private void CalculateTotalPt(){
        TextView BossAbilityView = findViewById(R.id.BossAbilityView);
        TextView EvaluationValueView = findViewById(R.id.EvaluationValueView);
        //old formula: BasicPtValue = 100+（bossHP比例数值）-【（boss回合数- 10）*（bossHP比例 / boss回合数）】+【（bossHP比例 / boss回合数）*（能力评价加成）】
        if(PtIndex == 0){
            PtIndex = 1;
        }
        PtValue = (int) ( BossHP / BossTurn * PtIndex);
        BossAbilityView.setText(AbilityList.toString());
        EvaluationValueView.setText(SupportClass.ReturnKiloLongString(PtValue) + "");
    }//end of Tourney system.
}

