package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        InitializingAchievement();
        SetLanguageHint();
    }

    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle(getString(R.string.HintWordTran));
            dialog.setMessage("Some of Text in this Page has not translated yet,\n" +
                    "you need to have the language skill to read Chinese.");
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    //set left button`s text.
                    getString(R.string.ConfirmWordTran),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }//end of hint function.


    //Achievement system.
    //Max Achievement is max number of Achievement in same type.
    int UserLevel = 1;
    int LevelMaxAchievement = 36;

    long UserPointGotten = 0;
    int PointMaxAchievement = 21;

    int UserMastery = 0;
    int MasteryMaxAchievement = 15;

    String HardAchievementDetail = "";

    //lv.3 method, main method.
    public void ReloadButtonMethod(View view){
        LoadNormalAchievement();
        LoadHardAchievement();
        //......
    }

    //lv.3 method, main method.
    private void InitializingAchievement(){
        UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
        UserPointGotten = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
        UserMastery = SupportClass.getIntData(this,"AlchemyMasteryFile","MasteryLevel",0);
        LoadNormalAchievement();
        LoadHardAchievement();
        //......
    }

    public void ShowHardDetail(View view){
        SupportClass.CreateOnlyTextDialog(this,getString(R.string.HardAchievementDetailWordTran),
                HardAchievementDetail,
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true
        );
    }

    //lv.2 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadNormalAchievement(){
        //1.load Level Achievement.
        PrintLevelAchievement(1,2,"冒险，启程！","--Completed Lv.1--");
        PrintLevelAchievement(2,5,"初心者","--Completed Lv.5--");
        PrintLevelAchievement(3,8,"小试牛刀","--Completed Lv.8--");
        PrintLevelAchievement(4,12,"初见端倪","--Completed Lv.12--");
        PrintLevelAchievement(5,15,"小有成就","--Completed Lv.18--");
        PrintLevelAchievement(6,18,"村里最好的剑","--Completed Lv.18--");
        PrintLevelAchievement(7,21,"出发，向更远的世界","--Completed Lv.21--");
        PrintLevelAchievement(8,25,"带着希望前行","--Completed Lv.25--");
        PrintLevelAchievement(9,30,"新的境遇，挑战，和你","--Completed Lv.30--");
        PrintLevelAchievement(10,35,"强者过招","--Completed Lv.35--");
        PrintLevelAchievement(11,40,"未料不敌","--Completed Lv.40--");
        PrintLevelAchievement(12,45,"再起","--Completed Lv.45--");
        PrintLevelAchievement(13,50,"修行中","--Completed Lv.50--");
        PrintLevelAchievement(14,55,"漫漫前路","--Completed Lv.55--");
        PrintLevelAchievement(15,60,"跋涉","--Completed Lv.60--");
        PrintLevelAchievement(16,65,"回忆着大家的期待","--Completed Lv.65--");
        PrintLevelAchievement(17,70,"再遇","--Completed Lv.70--");
        PrintLevelAchievement(18,75,"和生活对拼","--Completed Lv.75--");
        PrintLevelAchievement(19,80,"不相上下","--Completed Lv.80--");
        PrintLevelAchievement(19,85,"和所有人的羁绊","--Completed Lv.85--");
        PrintLevelAchievement(20,90,"不惧，直面，出鞘！","--Completed Lv.90--");
        PrintLevelAchievement(21,95,"刀落，回望，终焉。","--Completed Lv.95--");
        PrintLevelAchievement(22,100,"做自己的勇者","--Completed Lv.100--");
        PrintLevelAchievement(23,110,"新世界？","--Completed Lv.110--");
        PrintLevelAchievement(24,120,"见往昔","--Completed Lv.120--");
        PrintLevelAchievement(25,130,"向上","--Completed Lv.130--");
        PrintLevelAchievement(26,140,"迎面直击","--Completed Lv.140--");
        PrintLevelAchievement(27,150,"面向内心","--Completed Lv.150--");
        PrintLevelAchievement(28,160,"无止无休","--Completed Lv.160--");
        PrintLevelAchievement(29,180,"生平选择","--Completed Lv.180--");
        PrintLevelAchievement(30,200,"现实","--Completed Lv.200--");
        PrintLevelAchievement(31,220,"疲惫","--Completed Lv.220--");
        PrintLevelAchievement(32,240,"丢盔弃甲","--Completed Lv.240--");
        PrintLevelAchievement(33,260,"后退","--Completed Lv.260--");
        PrintLevelAchievement(34,280,"黑幕","--Completed Lv.280--");
        PrintLevelAchievement(35,299,"退场","--Completed Lv.299--");
        PrintLevelAchievement(36,300,"花火，幻觉，和人世","--Completed Lv.300--");
        //2.load Point Achievement.
        PrintPointAchievement(1,10000,"白手起家","--Have got 10k Point--");
        PrintPointAchievement(2,30000,"勉强维生","--Have got 30k Point--");
        PrintPointAchievement(3,100000,"第一桶金","--Have got 100k Point--");
        PrintPointAchievement(4,200000,"小有所成","--Have got 200k Point--");
        PrintPointAchievement(5,350000,"小康生活","--Have got 350k Point--");
        PrintPointAchievement(6,500000,"手留余裕","--Have got 500k Point--");
        PrintPointAchievement(7,750000,"出手阔绰","--Have got 750k Point--");
        PrintPointAchievement(8,1000000,"小富为安","--Have got 1m Point--");
        PrintPointAchievement(9,1500000,"富甲一方","--Have got 1.5m Point--");
        PrintPointAchievement(10,2500000,"地方贵族","--Have got 2.5m Point--");
        PrintPointAchievement(11,4000000,"氏族骄傲","--Have got 4m Point--");
        PrintPointAchievement(12,6000000,"金榜一位","--Have got 6m Point--");
        PrintPointAchievement(14,8000000,"皇亲国戚","--Have got 8m Point--");
        PrintPointAchievement(15,10000000,"官爵加身","--Have got 10m Point--");
        PrintPointAchievement(16,12500000,"锦衣玉食","--Have got 12.5m Point--");
        PrintPointAchievement(17,15000000,"众国所倾","--Have got 15m Point--");
        PrintPointAchievement(18,17500000,"大陆闻名","--Have got 17.5m Point--");
        PrintPointAchievement(19,20000000,"尘世尽欲","--Have got 20m Point--");
        PrintPointAchievement(20,30000000,"财脉系身","--Have got 30m Point--");
        PrintPointAchievement(21,50000000,"轮回皆知","--Have got 50m Point--");
        //3.load Mastery Achievement.
        PrintMasteryAchievement(1,1,"始习","--Get Lv.1 Mastery--");
        PrintMasteryAchievement(2,2,"灵根","--Get Lv.2 Mastery--");
        PrintMasteryAchievement(3,3,"记诵","--Get Lv.3 Mastery--");
        PrintMasteryAchievement(4,4,"背默","--Get Lv.4 Mastery--");
        PrintMasteryAchievement(5,5,"有成","--Get Lv.5 Mastery--");
        PrintMasteryAchievement(6,6,"烂熟","--Get Lv.6 Mastery--");
        PrintMasteryAchievement(7,7,"品获","--Get Lv.7 Mastery--");
        PrintMasteryAchievement(8,8,"信己","--Get Lv.8 Mastery--");
        PrintMasteryAchievement(9,9,"遍籍","--Get Lv.9 Mastery--");
        PrintMasteryAchievement(10,10,"研古","--Get Lv.10 Mastery--");
        PrintMasteryAchievement(11,11,"寻真","--Get Lv.11 Mastery--");
        PrintMasteryAchievement(12,12,"灼见","--Get Lv.12 Mastery--");
        PrintMasteryAchievement(13,13,"亲思","--Get Lv.13 Mastery--");
        PrintMasteryAchievement(14,14,"历卷","--Get Lv.14 Mastery--");
        PrintMasteryAchievement(15,15,"仙眼","--Get Lv.15 Mastery--");
    }

    //lv.1 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadHardAchievement(){
        //0.preparation.
        HardAchievementDetail = getString(R.string.UnlockHardAchievementWordTran) + "\n";
        //1.load data.
        if(SupportClass.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false)){
            TextView HardAchievement1View = findViewById(R.id.HardAchievement1View);
            HardAchievement1View.setTextColor(Color.RED);
            AddHardDetail("Million Master:\n商店购买获得。\n");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0) >= 1000){
            TextView HardAchievement2View = findViewById(R.id.HardAchievement2View);
            HardAchievement2View.setTextColor(Color.RED);
            AddHardDetail("Right Hit to Target:\n暴击率天赋升至满级获得。\n");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) >= 750){
            TextView HardAchievement3View = findViewById(R.id.HardAchievement3View);
            HardAchievement3View.setTextColor(Color.RED);
            AddHardDetail("Fire Blade:\n暴击伤害天赋升至满级获得。\n");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0) >= 20){
            TextView HardAchievement4View = findViewById(R.id.HardAchievement4View);
            HardAchievement4View.setTextColor(Color.RED);
            AddHardDetail("Endless Path:\n“敌临境”通关所有层数获得（不含彩蛋层）。\n");
        }
    }

    //lv.1 method, sub method of LoadHardAchievement() method.
    private void AddHardDetail(String AddText){
        HardAchievementDetail = HardAchievementDetail + AddText;
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void PrintLevelAchievement(int AchievementID, int RequireLevel, String Achievement, String Condition){
        TextView LevelAchievementView = findViewById(R.id.LevelAchievementView);
        TextView LevelConditionView = findViewById(R.id.LevelConditionView);
        if(UserLevel >= RequireLevel){
            LevelAchievementView.setText(Achievement + " (" + AchievementID + " / " + LevelMaxAchievement + ")");
            LevelConditionView.setText(Condition);
        }
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void PrintPointAchievement(int AchievementID, int RequirePoint, String Achievement, String Condition){
        TextView PointAchievementView = findViewById(R.id.PointAchievementView);
        TextView PointConditionView = findViewById(R.id.PointConditionView);
        if(UserPointGotten >= RequirePoint){
            PointAchievementView.setText(Achievement + " (" + AchievementID + " / " + PointMaxAchievement + ")");
            PointConditionView.setText(Condition);
        }
    }

    @SuppressLint("SetTextI18n")
    private void PrintMasteryAchievement(int AchievementID, int RequireMastery, String Achievement, String Condition){
        TextView MasteryAchievementView = findViewById(R.id.MasteryAchievementView);
        TextView MasteryConditionView = findViewById(R.id.MasteryConditionView);
        if(UserMastery == RequireMastery){
            MasteryAchievementView.setText(Achievement + " (" + AchievementID + " / " + MasteryMaxAchievement + ")");
            MasteryConditionView.setText(Condition);
        }
    }//end of Achievement system.

}