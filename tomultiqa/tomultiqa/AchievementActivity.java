package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

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
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    "Some of Text in this Page has not translated yet,\n" +
                            "you need to have the language skill to read Chinese.",
                    getString(R.string.ConfirmWordTran));
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
    }

    public void ShowHardDetail(View view){
        SupportClass.CreateNoticeDialog(this,getString(R.string.HardAchievementDetailWordTran),
                HardAchievementDetail,
                getString(R.string.ConfirmWordTran));
    }

    //lv.2 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadNormalAchievement(){
        //1.load Level Achievement.
        PrintLevelAchievement(1,2,"冒险，启程！");
        PrintLevelAchievement(2,5,"初心者");
        PrintLevelAchievement(3,8,"小试牛刀");
        PrintLevelAchievement(4,12,"初见端倪");
        PrintLevelAchievement(5,15,"小有成就");
        PrintLevelAchievement(6,18,"村里最好的剑");
        PrintLevelAchievement(7,21,"出发，向更远的世界");
        PrintLevelAchievement(8,25,"带着希望前行");
        PrintLevelAchievement(9,30,"新的境遇，挑战，和你");
        PrintLevelAchievement(10,35,"强者过招");
        PrintLevelAchievement(11,40,"未料不敌");
        PrintLevelAchievement(12,45,"再起");
        PrintLevelAchievement(13,50,"修行中");
        PrintLevelAchievement(14,55,"漫漫前路");
        PrintLevelAchievement(15,60,"跋涉");
        PrintLevelAchievement(16,65,"回忆着大家的期待");
        PrintLevelAchievement(17,70,"再遇");
        PrintLevelAchievement(18,75,"和生活对拼");
        PrintLevelAchievement(19,80,"不相上下");
        PrintLevelAchievement(19,85,"和所有人的羁绊");
        PrintLevelAchievement(20,90,"不惧，直面，出鞘！");
        PrintLevelAchievement(21,95,"刀落，回望，终焉。");
        PrintLevelAchievement(22,100,"做自己的勇者");
        PrintLevelAchievement(23,110,"新世界？");
        PrintLevelAchievement(24,120,"见往昔");
        PrintLevelAchievement(25,130,"向上");
        PrintLevelAchievement(26,140,"迎面直击");
        PrintLevelAchievement(27,150,"面向内心");
        PrintLevelAchievement(28,160,"无止无休");
        PrintLevelAchievement(29,180,"生平选择");
        PrintLevelAchievement(30,200,"现实");
        PrintLevelAchievement(31,220,"疲惫");
        PrintLevelAchievement(32,240,"丢盔弃甲");
        PrintLevelAchievement(33,260,"后退");
        PrintLevelAchievement(34,280,"黑幕");
        PrintLevelAchievement(35,299,"退场");
        PrintLevelAchievement(36,300,"花火，幻觉，和人世");
        //2.load Point Achievement.
        PrintPointAchievement(1,10000,"白手起家");
        PrintPointAchievement(2,30000,"勉强维生");
        PrintPointAchievement(3,100000,"第一桶金");
        PrintPointAchievement(4,200000,"小有所成");
        PrintPointAchievement(5,350000,"小康生活");
        PrintPointAchievement(6,500000,"手留余裕");
        PrintPointAchievement(7,750000,"出手阔绰");
        PrintPointAchievement(8,1000000,"小富为安");
        PrintPointAchievement(9,1500000,"富甲一方");
        PrintPointAchievement(10,2500000,"地方贵族");
        PrintPointAchievement(11,4000000,"氏族骄傲");
        PrintPointAchievement(12,6000000,"金榜一位");
        PrintPointAchievement(14,8000000,"皇亲国戚");
        PrintPointAchievement(15,10000000,"官爵加身");
        PrintPointAchievement(16,12500000,"锦衣玉食");
        PrintPointAchievement(17,15000000,"众国所倾");
        PrintPointAchievement(18,17500000,"大陆闻名");
        PrintPointAchievement(19,20000000,"尘世尽欲");
        PrintPointAchievement(20,30000000,"财脉系身");
        PrintPointAchievement(21,50000000,"轮回皆知");
        //3.load Mastery Achievement.
        PrintMasteryAchievement(1,1,"始习");
        PrintMasteryAchievement(2,2,"灵根");
        PrintMasteryAchievement(3,3,"记诵");
        PrintMasteryAchievement(4,4,"背默");
        PrintMasteryAchievement(5,5,"有成");
        PrintMasteryAchievement(6,6,"烂熟");
        PrintMasteryAchievement(7,7,"品获");
        PrintMasteryAchievement(8,8,"信己");
        PrintMasteryAchievement(9,9,"遍籍");
        PrintMasteryAchievement(10,10,"研古");
        PrintMasteryAchievement(11,11,"寻真");
        PrintMasteryAchievement(12,12,"灼见");
        PrintMasteryAchievement(13,13,"亲思");
        PrintMasteryAchievement(14,14,"历卷");
        PrintMasteryAchievement(15,15,"仙眼");
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
            AddHardDetail(getString(R.string.RightHitToTargetTran) + "\n暴击率天赋升至满级获得。\n");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) >= 750){
            TextView HardAchievement3View = findViewById(R.id.HardAchievement3View);
            HardAchievement3View.setTextColor(Color.RED);
            AddHardDetail(getString(R.string.FireBladeTran) + "\n暴击伤害天赋升至满级获得。\n");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0) >= 20){
            TextView HardAchievement4View = findViewById(R.id.HardAchievement4View);
            HardAchievement4View.setTextColor(Color.RED);
            AddHardDetail(getString(R.string.EndlessPathTran) + "\n“敌临境”通关所有层数获得（不含彩蛋层）。\n");
        }
    }

    //lv.1 method, sub method of LoadHardAchievement() method.
    private void AddHardDetail(String AddText){
        HardAchievementDetail = HardAchievementDetail + AddText;
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void PrintLevelAchievement(int AchievementID, int RequireLevel, String Achievement){
        TextView LevelAchievementView = findViewById(R.id.LevelAchievementView);
        TextView LevelConditionView = findViewById(R.id.LevelConditionView);
        if(UserLevel >= RequireLevel){
            LevelAchievementView.setText(Achievement + " (" + AchievementID + " / " + LevelMaxAchievement + ")");
            LevelConditionView.setText("--" + getString(R.string.CompletedWordTran) + "Lv." + RequireLevel + "--");
        }
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void PrintPointAchievement(int AchievementID, int RequirePoint, String Achievement){
        TextView PointAchievementView = findViewById(R.id.PointAchievementView);
        TextView PointConditionView = findViewById(R.id.PointConditionView);
        if(UserPointGotten >= RequirePoint){
            PointAchievementView.setText(Achievement + " (" + AchievementID + " / " + PointMaxAchievement + ")");
            PointConditionView.setText("--" + getString(R.string.GetWordTran) + " " + SupportClass.ReturnKiloIntString(RequirePoint) + " " +  getString(R.string.PointWordTran) + "--");
        }
    }

    @SuppressLint("SetTextI18n")
    private void PrintMasteryAchievement(int AchievementID, int RequireMastery, String Achievement){
        TextView MasteryAchievementView = findViewById(R.id.MasteryAchievementView);
        TextView MasteryConditionView = findViewById(R.id.MasteryConditionView);
        if(UserMastery == RequireMastery){
            MasteryAchievementView.setText(Achievement + " (" + AchievementID + " / " + MasteryMaxAchievement + ")");
            MasteryConditionView.setText("--" + getString(R.string.GetWordTran) + " Lv." + RequireMastery + " " + getString(R.string.MasteryWordTran) + "--");
        }
    }//end of Achievement system.

}