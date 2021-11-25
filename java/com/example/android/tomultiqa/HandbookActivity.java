package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class HandbookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handbook);
        //show language hint to user.
        SetLanguageHint();
        //initialize Ability data from AbilityIO Class.
        AbilityIO abilityIO = new AbilityIO(this);
        AbilityDataLimit = abilityIO.ReturnAbilityNumber();
        //initialize TabLayout function.
        //thanks to: https://segmentfault.com/a/1190000008753052 !
        TabLayout HandbookTypeLayout = findViewById(R.id.HandbookTypeLayout);
        //set default selected Tab, to prevent from NPE.
        Objects.requireNonNull(HandbookTypeLayout.getTabAt(0)).select();
        //initializing MainView.
        LoadHandbookText();
        //set Tab selected listener.
        HandbookTypeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//get Current Selected Tab in TabLayout.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                LoadHandbookText();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                LoadHandbookText();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                LoadHandbookText();
            }
        });
    }

    //handbook system.
    /**
     * 0 means Ability Tab is selected.<br/>
     * 1 means Formula Tab is selected.<br/>
     * default value is 0. (Ability Page)
     */
    int TabPosition = 0;

    //Boss Ability Part.
    //static: https://www.cnblogs.com/dolphin0520/p/3799052.html !
    static int CurrentAbilityNumber = 1;
    static int AbilityDataLimit;

    public void NextAbility(View view){
        if(CurrentAbilityNumber < AbilityDataLimit){
            CurrentAbilityNumber = CurrentAbilityNumber + 1;
        }else{
            CurrentAbilityNumber = 1;
        }
        LoadHandbookText();
    }

    public void BeforeAbility(View view){
        if(CurrentAbilityNumber > 1){
            CurrentAbilityNumber = CurrentAbilityNumber - 1;
        }else{
            CurrentAbilityNumber = AbilityDataLimit;
        }
        LoadHandbookText();
    }

    //this method have no usage in this version, may be the content of it can using in future. So we keep it here.
//    public void ShowAbilityHelpDialog(View view){
//        SupportLib.CreateNoticeDialog(this,
//                getString(R.string.HelpWordTran),
//                getString(R.string.AbilityHelpTextTran),
//                getString(R.string.ConfirmWordTran));
//    }

    //main method.
    @SuppressLint("SetTextI18n")
    private void LoadHandbookText(){
        TextView HandbookMainView = findViewById(R.id.HandbookMainView);
        if(TabPosition == 0){
            //change the counter UI.
            TextView AbilityShowingNumberView = findViewById(R.id.AbilityShowingNumberView);
            AbilityShowingNumberView.setText(CurrentAbilityNumber + " / " + AbilityDataLimit);
            AbilityIO abilityIO = new AbilityIO(this);
            AbilityDataLimit = abilityIO.ReturnAbilityNumber();
            //show content to user.
            //"number - 1": trans to based on 0.
            int AbilityId = CurrentAbilityNumber - 1;
            //prepare Tourney information for specific Ability to display.
            String TourneyValue = abilityIO.TourneyList.get(AbilityId).toString();
            if(abilityIO.TourneyList.get(AbilityId) > 0){
                TourneyValue = "+" + TourneyValue;
            }
            HandbookMainView.setText(
                    getString(R.string.BossAbilityWordTran) + "\n\n" +
                            abilityIO.NameList.get(AbilityId) + "\n\n" +
                            getString(R.string.EffectWordTran) + "\n\n" +
                            abilityIO.EffectList.get(AbilityId) + "\n\n" +
                            getString(R.string.AbilityEvaluateWordTran) + "\n\n" +
                            TourneyValue
            );
        }else if(TabPosition == 1){
            //show content to user.
            HandbookMainView.setText(ValueLib.GAME_FORMULA_HELP);
        }
    }//end of Boss Ability part.
    //end of handbook system.

    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    "Some of Text in this Page has not translated yet,\n" +
                            "you need to have the language skill to read Chinese.",
                    getString(R.string.ConfirmWordTran)
            );
        }
    }//end of hint function.
}