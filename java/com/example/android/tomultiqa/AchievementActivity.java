package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingAchievement();
        SetLanguageHint();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadNormalAchievement();
        LoadHardAchievement();
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backpack_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_BackPackHelp) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.AchievementListTran),
                    ValueClass.ACHIEVEMENT_HELP,
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
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
    static int LevelMaxAchievement = 36;

    int UserPoint;
    long UserPointGotten = 0;
    static int PointMaxAchievement = 21;

    int UserMastery = 0;
    static int MasteryMaxAchievement = 15;

    int UserTourneyPt = 0;
    static int TourneyMaxAchievement = 19;

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
        //1.1 load Level Achievement.
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
        //1.2 load Point Achievement.
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
        //1.3 load Mastery Achievement.
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
        //1.4 load Tourney Achievement.
        PrintTourneyAchievement(1,1,"开幕");
        PrintTourneyAchievement(2,5000,"直击");
        PrintTourneyAchievement(3,10000,"较量");
        PrintTourneyAchievement(4,20000,"力战");
        PrintTourneyAchievement(5,30000,"奋勇");
        PrintTourneyAchievement(6,40000,"演武");
        PrintTourneyAchievement(7,50000,"冲撞");
        PrintTourneyAchievement(8,60000,"搏斗");
        PrintTourneyAchievement(9,70000,"巧思");
        PrintTourneyAchievement(10,80000,"艰险");
        PrintTourneyAchievement(11,100000,"化难");
        PrintTourneyAchievement(12,120000,"定势");
        PrintTourneyAchievement(13,150000,"有余");
        PrintTourneyAchievement(14,180000,"道法");
        PrintTourneyAchievement(15,210000,"无常");
        PrintTourneyAchievement(16,240000,"飘摇");
        PrintTourneyAchievement(17,270000,"刚柔");
        PrintTourneyAchievement(18,300000,"并济");
        PrintTourneyAchievement(19,350000,"无双");
    }

    //lv.1 method, main method of Unlock Million Master Achievement.
    public void UnlockMMAchievement(View view){
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        if(!SupportClass.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false) && UserPoint >= 1000000){
            UserPoint = UserPoint - 1000000;
            SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
            SupportClass.saveBooleanData(this,"LimitedGoodsFile", "MillionMasterGot",true);
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You unlocked Million Master Achievement, it cost you 1,000,000 points.",
                    getString(R.string.ConfirmWordTran)
            );
            LoadHardAchievement();
        }else{
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    "Not enough Point to unlock or you have unlocked it.",
                    getString(R.string.ConfirmWordTran));
        }
    }

    //lv.1 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadHardAchievement(){
        //0.preparation.
        HardAchievementDetail = getString(R.string.UnlockHardAchievementWordTran) + "\n";
        //1.load data.
        TextView HardAchievement1View = findViewById(R.id.HardAchievement1View);
        if(SupportClass.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false)){
            AddHardDetail(HardAchievement1View,"点击购买获得。");
        }else{
            AddHardDetail(HardAchievement1View,"提示：探索一下周围，你会发现这个成就的。");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0) >= 1000){
            TextView HardAchievement2View = findViewById(R.id.HardAchievement2View);
            AddHardDetail(HardAchievement2View,"暴击率天赋升至满级获得。");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0) >= 750){
            TextView HardAchievement3View = findViewById(R.id.HardAchievement3View);
            AddHardDetail(HardAchievement3View,"暴击伤害天赋升至满级获得。");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0) >= 20){
            TextView HardAchievement4View = findViewById(R.id.HardAchievement4View);
            AddHardDetail(HardAchievement4View,"“敌临境”通关所有层数获得（不含彩蛋层）。");
        }
        if(SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0) >= 1000){
            TextView HardAchievement5View = findViewById(R.id.HardAchievement5View);
            AddHardDetail(HardAchievement5View,"回答正确1000题获得。");
        }
        if(SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0) >= 1000){
            TextView HardAchievement6View = findViewById(R.id.HardAchievement6View);
            AddHardDetail(HardAchievement6View,"战胜1000只boss获得。");
        }
        if(SupportClass.getIntData(this,"RecordDataFile","BattleFail",0) >= 21){
            TextView HardAchievement7View = findViewById(R.id.HardAchievement7View);
            AddHardDetail(HardAchievement7View,"boss战斗累计失败21场。");
        }
    }

    //lv.1 method, sub method of LoadHardAchievement() method.
    private void AddHardDetail(TextView NeedToChange, String HowToGet){
        HardAchievementDetail = HardAchievementDetail + NeedToChange.getText().toString() + "\n" + HowToGet + "\n";
        NeedToChange.setTextColor(Color.RED);
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void PrintLevelAchievement(int AchievementID, int RequireLevel, String Achievement){
        TextView LevelAchievementView = findViewById(R.id.LevelAchievementView);
        TextView LevelConditionView = findViewById(R.id.LevelConditionView);
        if(UserLevel >= RequireLevel){
            LevelAchievementView.setText(Achievement + " (" + AchievementID + " / " + LevelMaxAchievement + ")");
            LevelConditionView.setText("--" + getString(R.string.CompletedWordTran) + " Lv." + RequireLevel + " --");
        }
    }

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
    }

    @SuppressLint("SetTextI18n")
    private void PrintTourneyAchievement(int AchievementID, int RequirePt, String Achievement){
        TextView TourneyAchievementView = findViewById(R.id.TourneyAchievementView);
        TextView TourneyConditionView = findViewById(R.id.TourneyConditionView);
        if(UserTourneyPt == RequirePt){
            TourneyAchievementView.setText(Achievement + " (" + AchievementID + " / " + TourneyMaxAchievement + ")");
            TourneyConditionView.setText("--" + getString(R.string.GetWordTran) + " " + RequirePt + " Pt--");
        }
    }//end of Achievement system.
}