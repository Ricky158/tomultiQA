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

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.Objects;

public class AchievementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingAchievement();
        //initialize TabLayout function.
        //thanks to: https://segmentfault.com/a/1190000008753052 !
        TabLayout AchievementSelectLayout = findViewById(R.id.AchievementSelectLayout);
        //set default selected Tab, to prevent from NPE.
        Objects.requireNonNull(AchievementSelectLayout.getTabAt(0)).select();
        //initializing MainView.
        PrintAchievement();
        //set Tab selected listener.
        AchievementSelectLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//get Current Selected Tab in TabLayout.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                PrintAchievement();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                PrintAchievement();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TabPosition = tab.getPosition();
                PrintAchievement();
            }
        });
        SetLanguageHint();
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
        if (item.getItemId() == R.id.action_TimerHistory) {//if setting icon in Menu be touched.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.AchievementListTran),
                    ValueLib.ACHIEVEMENT_HELP,
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
    }

    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    "Some of Text in this Page has not translated yet,\n" +
                            "you need to have the language skill to read Chinese.",
                    getString(R.string.ConfirmWordTran));
        }
    }//end of hint function.


    //Achievement system.
    /**
     * 0 means Normal Class Achievement is selected.
     * 1 means Hard Class Achievement is selected.
     * default value is 0. (Normal Class)
     */
    int TabPosition = 0;

    /**
     * Store the Content of Normal Class Achievement situation.<br/>
     * Each Element in this array represents a kind of Achievement Content.<br/>
     * The order of Achievement Content is "Level"-"Point"-"Mastery"-"Tourney".<br/>
     * By editing specific element in array, and use method like Array.toString() to show to user.
     */
    String[] NormalContent = new String[]{"","","",""};

    //Max Achievement is max number of Achievement in same type.
    int UserLevel = 1;
    static int LevelMaxAchievement = 36;

    long UserPointGotten = 0;
    static int PointMaxAchievement = 21;

    int UserMastery = 0;
    static int MasteryMaxAchievement = 15;

    long UserTourneyPt = 0;
    static int TourneyMaxAchievement = 19;

    String HardAchievementDetail = "";

    //lv.3 method, main method.
    private void InitializingAchievement(){
        UserLevel = SupportLib.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
        UserPointGotten = SupportLib.getLongData(this,"RecordDataFile","PointGotten",0);
        UserMastery = SupportLib.getIntData(this,"AlchemyMasteryFile","MasteryLevel",0);
        UserTourneyPt = SupportLib.getLongData(this,"TourneyDataFile","MaxPtRecord",0);
        LoadNormalAchievement();
        LoadHardAchievement();
    }

    //lv.2 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadNormalAchievement(){
        //1.1 load Level Achievement.
        AddLevelContent(1,2,"??????????????????");
        AddLevelContent(2,5,"?????????");
        AddLevelContent(3,8,"????????????");
        AddLevelContent(4,12,"????????????");
        AddLevelContent(5,15,"????????????");
        AddLevelContent(6,18,"??????????????????");
        AddLevelContent(7,21,"???????????????????????????");
        AddLevelContent(8,25,"??????????????????");
        AddLevelContent(9,30,"??????????????????????????????");
        AddLevelContent(10,35,"????????????");
        AddLevelContent(11,40,"????????????");
        AddLevelContent(12,45,"??????");
        AddLevelContent(13,50,"?????????");
        AddLevelContent(14,55,"????????????");
        AddLevelContent(15,60,"??????");
        AddLevelContent(16,65,"????????????????????????");
        AddLevelContent(17,70,"??????");
        AddLevelContent(18,75,"???????????????");
        AddLevelContent(19,80,"????????????");
        AddLevelContent(19,85,"?????????????????????");
        AddLevelContent(20,90,"???????????????????????????");
        AddLevelContent(21,95,"???????????????????????????");
        AddLevelContent(22,100,"??????????????????");
        AddLevelContent(23,110,"????????????");
        AddLevelContent(24,120,"?????????");
        AddLevelContent(25,130,"??????");
        AddLevelContent(26,140,"????????????");
        AddLevelContent(27,150,"????????????");
        AddLevelContent(28,160,"????????????");
        AddLevelContent(29,180,"????????????");
        AddLevelContent(30,200,"??????");
        AddLevelContent(31,220,"??????");
        AddLevelContent(32,240,"????????????");
        AddLevelContent(33,260,"??????");
        AddLevelContent(34,280,"??????");
        AddLevelContent(35,299,"??????");
        AddLevelContent(36,300,"???????????????????????????");
        //1.2 load Point Achievement.
        AddPointContent(1,10000,"????????????");
        AddPointContent(2,30000,"????????????");
        AddPointContent(3,100000,"????????????");
        AddPointContent(4,200000,"????????????");
        AddPointContent(5,350000,"????????????");
        AddPointContent(6,500000,"????????????");
        AddPointContent(7,750000,"????????????");
        AddPointContent(8,1000000,"????????????");
        AddPointContent(9,1500000,"????????????");
        AddPointContent(10,2500000,"????????????");
        AddPointContent(11,4000000,"????????????");
        AddPointContent(12,6000000,"????????????");
        AddPointContent(14,8000000,"????????????");
        AddPointContent(15,10000000,"????????????");
        AddPointContent(16,12500000,"????????????");
        AddPointContent(17,15000000,"????????????");
        AddPointContent(18,17500000,"????????????");
        AddPointContent(19,20000000,"????????????");
        AddPointContent(20,30000000,"????????????");
        AddPointContent(21,50000000,"????????????");
        //1.3 load Mastery Achievement.
        AddMasteryContent(1,1,"??????");
        AddMasteryContent(2,2,"??????");
        AddMasteryContent(3,3,"??????");
        AddMasteryContent(4,4,"??????");
        AddMasteryContent(5,5,"??????");
        AddMasteryContent(6,6,"??????");
        AddMasteryContent(7,7,"??????");
        AddMasteryContent(8,8,"??????");
        AddMasteryContent(9,9,"??????");
        AddMasteryContent(10,10,"??????");
        AddMasteryContent(11,11,"??????");
        AddMasteryContent(12,12,"??????");
        AddMasteryContent(13,13,"??????");
        AddMasteryContent(14,14,"??????");
        AddMasteryContent(15,15,"??????");
        //1.4 load Tourney Achievement.
        AddTourneyContent(1,1,"??????");
        AddTourneyContent(2,5000,"??????");
        AddTourneyContent(3,10000,"??????");
        AddTourneyContent(4,20000,"??????");
        AddTourneyContent(5,30000,"??????");
        AddTourneyContent(6,40000,"??????");
        AddTourneyContent(7,50000,"??????");
        AddTourneyContent(8,60000,"??????");
        AddTourneyContent(9,70000,"??????");
        AddTourneyContent(10,80000,"??????");
        AddTourneyContent(11,100000,"??????");
        AddTourneyContent(12,120000,"??????");
        AddTourneyContent(13,150000,"??????");
        AddTourneyContent(14,180000,"??????");
        AddTourneyContent(15,210000,"??????");
        AddTourneyContent(16,240000,"??????");
        AddTourneyContent(17,270000,"??????");
        AddTourneyContent(18,300000,"??????");
        AddTourneyContent(19,350000,"??????");
    }

    //lv.1 method, main method of Unlock Million Master Achievement.
    public void UnlockMMAchievement(View view){
        ResourceIO resourceIO = new ResourceIO(this);
        if(!SupportLib.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false) && resourceIO.UserPoint >= 1000000){
            resourceIO.CostPoint(1000000);
            resourceIO.ApplyChanges(this);
            SupportLib.saveBooleanData(this,"LimitedGoodsFile", "MillionMasterGot",true);
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You unlocked Million Master Achievement, it cost you 1,000,000 points.",
                    getString(R.string.ConfirmWordTran)
            );
            LoadHardAchievement();
        }
    }

    //lv.1 method, sub method of ReloadButtonMethod() and InitializingAchievement() method.
    private void LoadHardAchievement(){
        //0.preparation.
        HardAchievementDetail = getString(R.string.UnlockHardAchievementWordTran) + "\n\n";
        //1.load data.
        if(SupportLib.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false)){
            AddHardDetail("Million Master","?????????????????????");
        }else{
            AddHardDetail("Million Master","????????????????????????????????????????????????????????????");
        }

        if(SupportLib.getIntData(this,"BattleDataProfile","CRTalentLevel",0) >= 1000){
            AddHardDetail(getString(R.string.RightHitToTargetTran),"????????????????????????????????????");
        }

        if(SupportLib.getIntData(this,"BattleDataProfile","CDTalentLevel",0) >= 750){
            AddHardDetail(getString(R.string.FireBladeTran),"???????????????????????????????????????");
        }

        if(SupportLib.getIntData(this,"BattleDataProfile","UserConflictFloor",0) >= 20){
            AddHardDetail(getString(R.string.EndlessPathTran),"???????????????????????????????????????????????????????????????");
        }

        if(SupportLib.getIntData(this,"RecordDataFile","RightAnswering",0) >= 1000){
            AddHardDetail(getString(R.string.Is1000PercentTran),"????????????1000????????????");
        }

        if(SupportLib.getIntData(this,"BattleDataProfile","BossDeadTime",0) >= 1000){
            AddHardDetail(getString(R.string.AlwayFightTran),"??????1000???boss?????????");
        }

        if(SupportLib.getIntData(this,"RecordDataFile","BattleFail",0) >= 21){
            AddHardDetail(getString(R.string.AlwayDieTran),"boss??????????????????21??????");
        }
    }

    //lv.1 method, main method to display Achievement Content to user.
    @SuppressLint("SetTextI18n")
    private void PrintAchievement(){
        TextView AchievementMainView = findViewById(R.id.AchievementMainView);
        if(TabPosition == 0){
            //0.set initial value even user have no normal achievement.
            StringBuilder NormalGotten = new StringBuilder(getString(R.string.NormalAchievementStartTran)).append("\n\n");
            //1.through all Array to combine achievement text.
            int Id = 0;
            while (Id < NormalContent.length){
                if(!NormalContent[Id].equals("")){
                    NormalGotten.append(NormalContent[Id]).append("\n\n");
                }
                //even user doesn't get specific achievement, but we also need to print next achievement.
                Id = Id + 1;
            }
            //2.show to user.
            AchievementMainView.setText(NormalGotten.toString());
        }else if(TabPosition == 1){
            AchievementMainView.setText(HardAchievementDetail);
        }
    }

    //lv.1 method, sub method of LoadHardAchievement() method.
    private void AddHardDetail(String Name, String HowToGet){
        HardAchievementDetail = HardAchievementDetail + Name + "\n" + HowToGet + "\n\n";
    }

    //lv.1 method, sub method of PrintAchievement() method.
    @SuppressLint("SetTextI18n")
    private void AddLevelContent(int AchievementID, int RequireLevel, String Achievement){
        if(UserLevel >= RequireLevel){
            NormalContent[0] =
                    Achievement + " (" + AchievementID + " / " + LevelMaxAchievement + ")\n>"
                            + getString(R.string.CompletedWordTran) + " Lv." + RequireLevel;
        }
    }

    @SuppressLint("SetTextI18n")
    private void AddPointContent(int AchievementID, int RequirePoint, String Achievement){
        if(UserPointGotten >= RequirePoint){
            NormalContent[1] = Achievement + " (" + AchievementID + " / " + PointMaxAchievement + ")\n>" +
                    getString(R.string.GetWordTran) + " " + SupportLib.ReturnKiloIntString(RequirePoint) + " " +  getString(R.string.PointWordTran);
        }
    }

    @SuppressLint("SetTextI18n")
    private void AddMasteryContent(int AchievementID, int RequireMastery, String Achievement){
        if(UserMastery >= RequireMastery){
            NormalContent[2] = Achievement + " (" + AchievementID + " / " + MasteryMaxAchievement + ")\n>" +
                    getString(R.string.GetWordTran) + " Lv." + RequireMastery + " " + getString(R.string.MasteryWordTran);
        }
    }

    @SuppressLint("SetTextI18n")
    private void AddTourneyContent(int AchievementID, long RequirePt, String Achievement){
        if(UserTourneyPt >= RequirePt){
            NormalContent[3] = Achievement + " (" + AchievementID + " / " + TourneyMaxAchievement + ")\n>" +
                    getString(R.string.GetWordTran) + " " + RequirePt + " Pt";
        }
    }//end of Achievement system.
}