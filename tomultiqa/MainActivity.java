package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

//2021.2.4 experience:
// 1.setText() can not accept int ,but we can plus a "empty String" to transform int to String.
//2.can`t think member variable equal to global variable ,it only works in parts!
//3.if you want to use "A=B is true" in "if-else",please use "=="!
//2021.3.14 : Android icon making: https://blog.csdn.net/zuo_er_lyf/article/details/82254316 !
//2021.4.28 : if you want to add a timer to Activity, you need to implement OnChronometerTickListener() here by system, instead of add it in anywhere by yourself.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get total number of id in database according records in SharedPreference., and initializing the database in variables form.
        ChangeQuestTotalNumber();
        //get total number of id in database according records in SharedPreference.
        //initializing Mode of whole App.
        InitializingAppMode();
        //initializing EXP data, including User haveEXP and level.
        InitializingEXPInformation();
        InitializingResourceData();
        InitializingTalentData();
        //calculate User`s battle data.
        ImportUserBattleData();
        BossState = getIntent().getIntExtra("BossState",0);
        //BossState: 1 ConflictBoss, 2 DailyBoss, 3 TourneyBoss.
        if(BossState > 0){
            //don`t move ImportUserBattleData() to here,because not all Boss comes from Another Activities!
            ImportConflictBossData(false);
            if(BossState == 2){
                DailyDifficulty = getIntent().getIntExtra("DailyDifficulty",1);
            }else if(BossState == 3){
                BossPtValue = getIntent().getLongExtra("BossPtValue",100);
            }
            //ModeChangeAppearance();
        }
        ModeChangeAppearance();
        InitializingTimer();
        InitializingRecordData();
        InitializingAnsweringData();

        //timer support.thanks to:
        //https://www.twle.cn/l/yufei/android/android-basic-chronometer.html !
        //https://stackoverflow.com/questions/526524/android-get-time-of-chronometer-widget !
        Chronometer QuestTimer = findViewById(R.id.QuestTimerView);
        QuestTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                //......may be we need it future, the number will automatically update.
            }
        });
    }

    //Basic values of whole system.

    //store the Quest title text for display.
    String QuestTitleContent = "";
    //store total number of Quests in database.Following the search method to define this variable.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    int QuestTotalNumber = 0;
    //store user`s answer text for judge.
    String QuestUserAnswer ="";
    //store system`s answer for judge.
    String QuestSystemAnswer = "";
    //store Quest`s type text for display.
    String QuestType = "";
    //store Quest`s level number for display.
    int QuestLevel = 1;
    //store user`s right times.
    int UserRightTimes = 0;
    //store user`s wrong times.
    int UserWrongTimes = 0;
    //store whole App Mode.
    String AppMode ="Normal";//the end of basic values.

    @SuppressLint("SetTextI18n")
    private void InitializingAnsweringData(){
        TextView RightTimes = findViewById(R.id.RightTimes);
        TextView WrongTimes = findViewById(R.id.WrongTimes);
        UserRightTimes = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
        RightTimes.setText(UserRightTimes + "");
        UserWrongTimes = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);
        WrongTimes.setText(UserWrongTimes + "");
    }


    //EXP system.
    int UserLevel;
    int UserHaveEXP;
    int UserUpgradeEXP = 1;

    //basic calculation of EXP, define them to use less manually coding or Error.
    private void LostEXP(int MinusNumber){
        UserHaveEXP = UserHaveEXP - MinusNumber;
        CheckLevel();
    }

    private void GetEXP(int AddNumber){
        UserHaveEXP = UserHaveEXP + AddNumber;
        EXPRecord = EXPRecord + AddNumber;
        SupportClass.saveIntData(this,"RecordDataFile","EXPGotten",EXPRecord);
        CheckLevel();
    }

    @SuppressLint("SetTextI18n")
    private void CalculateUpgradeEXP(){
        //EXPLimit stand for EXP which user need to collect to upgrade.
        // it just use for double form number calculating, it is not used in actual EXP system code.
        double EXPLimit;
        //it means the number: level^1.6 + 15 - level^1.1.
        EXPLimit = Math.pow(UserLevel,1.6) + 15 - Math.pow(UserLevel,1.1) ;
        //lost the number under 0`s part,or "FLOOR".
        UserUpgradeEXP = (int) EXPLimit;
        TextView NextLevelEXPView = findViewById(R.id.NextLevelEXP);
        NextLevelEXPView.setText(UserUpgradeEXP + "");
    }

    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    //keep EXP data method.
    private void StoreEXPInformation(){
        SharedPreferences EXPInfo = getSharedPreferences("EXPInformationStoreProfile", MODE_PRIVATE);
        //获取Editor
        SharedPreferences.Editor editor= EXPInfo.edit();
        //得到Editor后，写入需要保存的数据
        editor.putInt("UserLevel", UserLevel);
        editor.putInt("UserHaveEXP",UserHaveEXP);
        editor.apply();//提交修改
    }
    //read EXP data method.
    private void InitializingEXPInformation(){
        UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
        UserHaveEXP = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserHaveEXP",0);
        CheckLevel();
    }

     //check the EXP number, and make level up or down calculation.
   @SuppressLint("SetTextI18n")
   private void CheckLevel(){
        //show current level upgrade needs.
        CalculateUpgradeEXP();
       TextView EXPrateView = findViewById(R.id.EXPrate);
       ProgressBar EXPBarView = findViewById(R.id.EXPBar);

        if (UserHaveEXP >= UserUpgradeEXP && UserLevel < 300){
            UserLevel = UserLevel + 1 ;
            UserHaveEXP = UserHaveEXP -UserUpgradeEXP ;
            //after upgrade, show higher needs to upgrade.
            CalculateUpgradeEXP();
        }else if (UserHaveEXP < 0 && UserLevel > 1 ){
            UserLevel = UserLevel - 1 ;
            UserHaveEXP = UserHaveEXP + UserUpgradeEXP;
            //after downgrade,show lower needs to upgrade.
            CalculateUpgradeEXP();
        }

        // And show the result to user.
       EXPrateView.setText(SupportClass.CalculatePercent(UserHaveEXP,UserUpgradeEXP) + "%");
       EXPBarView.setProgress(SupportClass.CalculatePercent(UserHaveEXP,UserUpgradeEXP));

        //show the EXP data to user, including level, upgrade EXP, and User haveEXP.
       TextView NowLevel = findViewById(R.id.NowLevel);
        NowLevel.setText(UserLevel + "");

       TextView NowHaveEXPView = findViewById(R.id.NowHaveEXP);
        NowHaveEXPView.setText(UserHaveEXP +"");

       TextView NextLevelEXPView = findViewById(R.id.NextLevelEXP);
        NextLevelEXPView.setText(UserUpgradeEXP + "");

        //keep the EXP data.
        StoreEXPInformation();
    }//the end of EXP system.


    //Game Resource system. Including Point and Materials.
    int UserPoint = 0;
    int UserMaterial = 0;
    int QuestCombo = 0;


    //basic operation of resource, just do it automatically.
    private void GetPoint(int addNumber){
        UserPoint = UserPoint + addNumber;
        PointRecord = PointRecord + addNumber;
        SupportClass.saveLongData(this,"RecordDataFile","PointGotten",PointRecord);
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
    }

    private void GetMaterial(int addNumber){
        UserMaterial = UserMaterial + addNumber;
        MaterialRecord = MaterialRecord + addNumber;
        SupportClass.saveIntData(this,"RecordDataFile","MaterialGotten",MaterialRecord);
        SupportClass.saveIntData(this,"BattleDataProfile","UserMaterial",UserMaterial);
    }

    @SuppressLint("SetTextI18n")
    private void ChangeQuestCombo(boolean IsQuestRight){
        TextView QuestComboShowView = findViewById(R.id.QuestComboShowView);

        if(IsQuestRight){
            QuestCombo = QuestCombo + 1;
        }else{
            QuestCombo = 0;
        }

        if(QuestCombo >= ComboRecord){
            ComboRecord = QuestCombo;
            SupportClass.saveIntData(this,"RecordDataFile","ComboGotten",ComboRecord);
        }
        QuestComboShowView.setText(QuestCombo + "");
        SupportClass.saveIntData(this,"BattleDataProfile","QuestCombo",QuestCombo);
    }

    private void InitializingResourceData(){
        QuestCombo = SupportClass.getIntData(this,"BattleDataProfile","QuestCombo",0);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
    }//end of Game Resource system.


    //Record system.
    int EXPRecord = 0;
    long PointRecord = 0;
    int MaterialRecord = 0;
    int ComboRecord = 0;

    private void InitializingRecordData(){
        EXPRecord = SupportClass.getIntData(this,"RecordDataFile","EXPGotten",0);
        PointRecord = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
        MaterialRecord = SupportClass.getIntData(this,"RecordDataFile","MaterialGotten",0);
        ComboRecord = SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0);
    }
    //end of Record system.


    //battle system.
    //value "SD" means Shield point, value "ModeNumber" means Multi Mode Boss`s Mode total number. BossState: 0 is normal boss, 1 is Conflict boss, 2 is Daily boss.

    //basic values of Battle.
    //boss data part.
    int BossState = 0;
    int BossTotalHP;
    int BossNowHP;
    int BossTotalSD;
    int BossNowSD;
    int BossEXPValue;
    int BossPointValue;
    int BossMaterialValue;
    //"Pt" is a record in Tourney function, is not equal to "Point".
    long BossPtValue;
    //user data part.
    int UserATK;
    int AlchemyATK = 0;
    double UserCriticalRate;
    int AlchemyCriticalRate = 0;
    double UserCriticalDamage;
    int AlchemyCriticalDamage = 0;
    //battle data part.
    int AlchemyTurn = 0;
    int BattleTurn;
    int BossDeadTime;
    //https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values
    //BossAbility variable is only one used in battle calculation.
    ArrayList<String> BossAbility = new ArrayList<>();

    //using this method to prevent from NullPointException.
    private void MakeAbilityNotNull(){
        //try to catch NullPointerException on get ArrayList:BossAbility from SquareActivity Process.
        //because User would not enter MainActivity from SquareActivity most of time, but this process will happen on every time that user enter MainActivity.
        //And it will cause getStringArrayListExtra() method throw NullPointerException.

        if(BossState > 0){
            BossAbility = getIntent().getStringArrayListExtra("BossAbility");
//            try{
//
//            }catch(NullPointerException e){
//                BossAbility.add("Nothing");
//            }
        }
    }

    //record multi mode boss`s DeadTime to figure out is it need to change the Mode or stop Reborn.You can think this is Boss`s HP Bar number.
    int BossRebornTime = 0;
    int BossModeNumber = 1;
    int UserConflictFloor;
    int CurrentShowFloor;
    int DailyDifficulty;

    //data in battle calculation.
    int CurrentDamage = 0;
    boolean IsCritical = false;

    private void ImportConflictBossData(boolean IsRebornMode){
        //confirm is boss have multi mode, and confirm which Ability List should be imported in battle system.
        //this part is only works on Initializing Boss data, so it can`t used in Reborn.
        if(!IsRebornMode){
            BossModeNumber = getIntent().getIntExtra("ModeNumber",1);
            switch (BossModeNumber){
                case 3:
                    //give this boss 3 time to reborn.
                    BossRebornTime = 3;
                    BossAbility = getIntent().getStringArrayListExtra("BossAbilityThird");
                    break;
                case 2 :
                    BossRebornTime = 2;
                    BossAbility = getIntent().getStringArrayListExtra("BossAbilitySecond");
                    break;
                case 1:
                    BossRebornTime = 1;
                    BossAbility = getIntent().getStringArrayListExtra("BossAbility");
                    break;
            }
            MakeAbilityNotNull();

        }
        //if boss have multi HP, the BossRebornTime variable should be minus 1 when boss is reborned.
        if(BossRebornTime > 0){
            BossRebornTime = BossRebornTime - 1;

            //because of the limitation of ArrayList(can`t be a parameter into a method, because it is a class actually.)
            //So it`s value will be updated and add to SetBossValues method independently.
            //BossAbility need to be changed in other code.
            SetBossValues(
                    getIntent().getStringExtra("Name"),
                    getIntent().getIntExtra("Level",10),
                    getIntent().getIntExtra("HP",1350),
                    getIntent().getIntExtra("SD",0),
                    getIntent().getIntExtra("Turn",10),
                    getIntent().getIntExtra("EXPReward",5),
                    getIntent().getIntExtra("PointReward",350),
                    getIntent().getIntExtra("MaterialReward",0)
            );
            UserConflictFloor = SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0);
            CurrentShowFloor = SupportClass.getIntData(this,"BattleDataProfile","CurrentShowFloor",1);
        }
    }

    //Reward means the present that User will have when it defeat the Boss.
    //Turn means User will have this number`s chance to attack Boss.
    //actually we plan "ability" of boss, but we don`t finish it in code, so this method will be upgraded in future.
    @SuppressLint("SetTextI18n")
    private void SetBossValues(String name, int Level, int HP, int SD, int Turn, int EXPReward, int MaterialReward, int PointReward){
        TextView BossNameView = findViewById(R.id.BossNameView);
        TextView BossLevelView = findViewById(R.id.RecommendLevelView);
        TextView BossHPShowView = findViewById(R.id.BossHPShowView);
        TextView BossShieldShowView = findViewById(R.id.BossShieldShowView);
        TextView BossModeShowView = findViewById(R.id.BossModeShowView);
        TextView BattleTurnView = findViewById(R.id.BattleTurnView);
        TextView BossAbilityShowView = findViewById(R.id.BossAbilityShowView);

        BossNameView.setText(name);
        BossLevelView.setText("Lv." + Level);
        BossHPShowView.setText(HP + "/" + HP);
        BossShieldShowView.setText(SD + "/" + SD);
        BattleTurnView.setText(Turn + "");

        //which used in judging how to make Ability effective.
        String BossAbilityText = BossAbility.toString();
        //set boss Mode display.
        if (BossRebornTime == 1 && BossModeNumber == 3){
            BossModeShowView.setText(R.string.BossMode3WordTran);
        }else if(BossRebornTime == 2 && BossModeNumber == 2){
            BossModeShowView.setText(R.string.BossMode2WordTran);
        }else if(BossRebornTime == 1 && BossModeNumber == 1){
            BossModeShowView.setText(R.string.BossMode1WordTran);
        }else{
            //normal boss would not have any ModeNumber and Ability.
            BossModeShowView.setText(R.string.BossMode1WordTran);
            BossAbilityShowView.setText(getString(R.string.NoBossAbilityWordTran));
        }
        //decided which mode Ability should be loaded, show the result to user.
        if(BossAbilityText.contains("Nothing")){
            BossAbilityShowView.setText(getString(R.string.NoBossAbilityWordTran));
        }
        BossAbilityShowView.setText(BossAbilityText);

        BossTotalHP = HP;
        BossNowHP = BossTotalHP;
        BossTotalSD = SD;
        BossNowSD = BossTotalSD;
        BattleTurn = Turn;
        BossEXPValue = EXPReward;
        BossPointValue = PointReward;
        BossMaterialValue = MaterialReward;
        //set Boss shield Layout state. And reset the HP/SD Bar value.
        ConstraintLayout BossShieldLayout = findViewById(R.id.BossShieldLayout);
        ProgressBar BossHPBar = findViewById(R.id.BossHPBar);
        ProgressBar BossShieldBar = findViewById(R.id.BossShieldBar);
        BossHPBar.setProgress(100);
        if(SD <= 0){
            BossShieldLayout.setVisibility(View.GONE);
        }else{
            BossShieldLayout.setVisibility(View.VISIBLE);
            BossShieldBar.setProgress(100);
        }
    }

    //sub method of Print Battle Data to user.
    @SuppressLint("SetTextI18n")
    private void PrintBattleData(){
        //0.preparation.
        TextView UserATKView = findViewById(R.id.UserATKView);
        TextView UserCriticalRateView = findViewById(R.id.UserCriticalRateView);
        TextView UserCriticalDamageView = findViewById(R.id.UserCriticalDamageView);
        UserATKView.setText(UserATK + "");
        UserCriticalDamageView.setText(UserCriticalDamage + "");
        UserCriticalRateView.setText(UserCriticalRate + "");
    }

    //initializing Battle data. Also being a sub method of onCreate.
    private void ImportUserBattleData(){
        if(AppMode.equals("Game")){
            //1.calculate basic value of user battle data.
            UserATK = 10 + UserLevel *10 + ATKTalentLevel * 3 ;
            UserCriticalRate = 5.00 + CRTalentLevel * 0.05 ;
            UserCriticalDamage = 150.0 + CDTalentLevel * 0.2 ;
            //2.import another way to up the value.
            //notice: the add to basic Data part are put in MakeAbilityEffective method---Certain part.
            CalculateAlchemyEffect();
            //3.show the value to user.
            PrintBattleData();
            //4.load the BossDeadTime from record.
            BossDeadTime = SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0);
        }
    }

    //sub method of ImportUserBattleData() and MakeAbilityEffective() method.
    private void CalculateAlchemyEffect(){
        AlchemyATK =(int) (SupportClass.getIntData(this,"AlchemyDataFile","ATKup",0) * 0.01 * UserATK);// *0.01 is aim to make it to be Percent form.
        AlchemyCriticalRate = SupportClass.getIntData(this,"AlchemyDataFile","CRup",0);
        AlchemyCriticalDamage = SupportClass.getIntData(this,"AlchemyDataFile","CDup",0);
        AlchemyTurn = SupportClass.getIntData(this,"AlchemyDataFile","AlchemyTurn",0);
    }

    //sub method of BattleCalculation method. And this method, making all of Boss Abilities effective.
    @SuppressLint("SetTextI18n")
    private void MakeAbilityEffective(String Position){
        //which used in judging how to make Ability effective.
        String BossAbilityText = BossAbility.toString();
        switch (Position) {
            case "Start":
                //Start Position means that Abilities which should effective on the Start of Battle. Most of these Abilities are about User or Boss Data.
                if(BossAbilityText.contains(getString(R.string.CurseAbilityTran))){
                    UserCriticalRate = 0.0;
                }
                if (BossAbilityText.contains(getString(R.string.FragileAbilityTran))) {
                    TextView UserCriticalRateView = findViewById(R.id.UserCriticalRateView);
                    //Ability:"脆弱", User`s Critical Rate will be added to 100%.
                    UserCriticalRate = 100.00;
                    UserCriticalRateView.setText(UserCriticalRate + "");
                }
                if(BossAbilityText.contains(getString(R.string.WeakSpotAbilityTran))) {
                    //Ability:"弱点", User`s Critical Damage will be added extra 50%.
                    UserCriticalDamage = UserCriticalDamage + 50.0;
                }
                if(BossAbilityText.contains(getString(R.string.DiversionAbilityTran))){
                    UserCriticalDamage = 130.0;
                }
                if(BossAbilityText.contains(getString(R.string.CorrosionAbilityTran))){
                    //Ability:"腐蚀", User`s Critical Rate will lost 25%.
                    UserATK = (int) (UserATK * 0.75);
                }
                if(BossAbilityText.contains(getString(R.string.RushAbilityTran))){
                    BattleTurn = BattleTurn - 1;
                }
                if(BossAbilityText.contains(getString(R.string.SlowAbilityTran))){
                    BattleTurn = BattleTurn + 1;
                }
//                if(BossAbilityText.contains(getString(R.string.DuelAbilityTran))){
//                    BattleTurn = 1;
//                }//not necessary, we can edit Turn number in boss design.
                if(BossAbilityText.contains(getString(R.string.TrialAbilityTran))){
                    BossTotalHP = (int)(BossTotalHP * 1.3);
                    BossNowHP = BossTotalHP;
                }
                if(BossAbilityText.contains(getString(R.string.ShieldAbilityTran))){
                    BossNowSD = (int)(BossTotalHP * 0.35);
                    BossTotalSD = BossNowSD;
                }
                //put Alchemy data in user data.
                CalculateAlchemyEffect();//we need re-calculate the Alchemy data when user basic data is changed.
                if(AlchemyTurn > 0){
                    UserATK = UserATK + AlchemyATK;
                    UserCriticalRate = UserCriticalRate + AlchemyCriticalRate;
                    UserCriticalDamage = UserCriticalDamage + AlchemyCriticalDamage;
                }
                break;
            case "Middle":
                //Middle Position means that Abilities which should effective on the every turn`s end of Battle.
                //in every turn, we will check the Alchemy has enough Turn to Effective or not, if not, user will lost this effect.
                //And the data in AlchemyDataFile will be riped off.
                if(AlchemyTurn > 0){
                    AlchemyTurn = AlchemyTurn - 1;
                }else{
                    AlchemyTurn = 0;
                    UserATK = UserATK - AlchemyATK;
                    UserCriticalRate = UserCriticalRate - AlchemyCriticalRate;
                    UserCriticalDamage = UserCriticalDamage - AlchemyCriticalDamage;
                }
                SharedPreferences A = getSharedPreferences("AlchemyDataFile", MODE_PRIVATE);
                SharedPreferences.Editor editor= A.edit();
                editor.clear();
                editor.apply();

                if(BossAbilityText.contains(getString(R.string.RecoverAbilityTran)) && BossNowHP > 0){
                    BossNowHP = BossNowHP * (1 + SupportClass.CreateRandomNumber(0,25) / 100);
                    if(BossNowHP > BossTotalHP){
                        BossNowHP = BossTotalHP;
                    }
                }
                if(BossAbilityText.contains(getString(R.string.LastHurtAbilityTran)) && BossNowHP > 0){
                    BossNowHP = BossNowHP * (1 - SupportClass.CreateRandomNumber(0,17) / 100);
                    if(BossNowHP < 0){
                        BossNowHP = 0;
                    }
                }
                if(BossAbilityText.contains(getString(R.string.FastStepAbilityTran))){
                    //provide additional BattleTurn minus 1, and it equals to -2 per Turn.
                    BattleTurn = BattleTurn - 1;
                }
                if(BossAbilityText.contains(getString(R.string.ReTimeAbilityTran)) && SupportClass.CreateRandomNumber(0,100) < 33){
                    BattleTurn = BattleTurn + 1;
                }
                break;
            case "Certain":
                //Certain Position means that Abilities which should effective on the specific Turn(s) of Battle.
                //......
                break;
            case "Calculate":
                //Calculate Position means that Abilities which should effective on the every time of damage calculation in Battle.
                if (BossAbilityText.contains(getString(R.string.ProudAbilityTran)) && (float) CurrentDamage / BossTotalHP < 0.11) {
                    //Ability:"傲立", Boss will not take any damage which damage number is lower than Boss`s 11% HP.
                    CurrentDamage = 0;
                }
                if(BossAbilityText.contains(getString(R.string.HopelessAbilityTran))){
                    CurrentDamage = 0;
                }
                if(BossAbilityText.contains(getString(R.string.FearAbilityTran)) && (float) CurrentDamage / BossTotalHP > 0.25){
                    CurrentDamage = 0;
                }

                break;
        }//Some Abilities like "宝藏" will effective by using independent code.
        PrintBattleData();
    }

    //sub method, used in clear all boss Ability quickly.
    private void ClearAllAbility(){
        MakeAbilityNotNull();//To prevent from Clear() method causing NullPointerException.
        if(!BossAbility.isEmpty()){
            BossAbility.clear();
        }

}

    private void RecordBattleData(boolean IsBattleWin){
        SharedPreferences RecordInfo = getSharedPreferences("RecordDataFile", MODE_PRIVATE);
        //record the max Difficulty user have ever finished.
        if(IsBattleWin && RecordInfo.getInt("MaxDailyDifficulty",0) < DailyDifficulty){
            SharedPreferences.Editor editor= RecordInfo.edit();
            editor.putInt("MaxDailyDifficulty", DailyDifficulty);
            editor.apply();
        }
        //if user win on higher floor, record that data.
        if(CurrentShowFloor >= UserConflictFloor){
            UserConflictFloor = CurrentShowFloor;
            SupportClass.saveIntData(this,"BattleDataProfile","UserConflictFloor",UserConflictFloor);
        }
    }

    @SuppressLint("SetTextI18n")
    private void BattleCalculation(boolean IsQuestRight, boolean IsBossHere){
        TextView BossHPShowView = findViewById(R.id.BossHPShowView);
        TextView BossShieldShowView = findViewById(R.id.BossShieldShowView);
        TextView BattleTurnView = findViewById(R.id.BattleTurnView);
        ProgressBar BossHPBar =findViewById(R.id.BossHPBar);
        ProgressBar BossShieldBar = findViewById(R.id.BossShieldBar);

        //Boss Ability, which useful in battle Start`s calculation.
        MakeAbilityEffective("Start");

        if(SupportClass.CreateRandomNumber(0,10000) <= (int) ( UserCriticalRate * 100)){
            IsCritical = true;
            //if Random Number less than CriticalRate *100, system will think NO Critical, so IsCritical variable will keep false.
        }

        //calculate the basic damage to Boss.
        if (IsQuestRight && IsBossHere && BattleTurn >= 1 && BossNowHP > 0){
            if (UserATK <= BossNowSD){
                //CurrentDamage still 0.
                BossNowSD = BossNowSD - UserATK;
            }else {
                CurrentDamage = UserATK - BossNowSD;
                BossNowSD = 0;
            }

            //if critical, then add additional damage to damage counting.
            if(IsCritical) {
                CurrentDamage = (int) (CurrentDamage * UserCriticalDamage / 100 );
            }

            //Boss Ability, which useful in battle progression`s calculation.
            MakeAbilityEffective("Calculate");

            //finally, cost Boss`s HP based on damage counting.
            BossNowHP = BossNowHP - CurrentDamage;

            //after one time`s damage calculation, should cost 1 Turn.
            BattleTurn = BattleTurn - 1;
            MakeAbilityEffective("Middle");

            //after calculation, show User the detail.
            BossHPShowView.setText(BossNowHP + "/" + BossTotalHP);
            BossShieldShowView.setText(BossNowSD + "/" + BossTotalSD);
            BattleTurnView.setText(BattleTurn + "");
            BossHPBar.setProgress(SupportClass.CalculatePercent(BossNowHP,BossTotalHP));
            BossShieldBar.setProgress(SupportClass.CalculatePercent(BossNowSD,BossTotalSD));

            //if boss lost all its HP, still in calculation, then User Win. And close the battle immediately.
            if (BossNowHP <=0){
                CloseBattle(true);
                BossDeadTime = BossDeadTime + 1;
                RecordBattleData(true);
            }

            //judging whether boss is die.
        }else if(IsBossHere || BattleTurn < 1){
            //cost all Turns, but boss still have HP, LOST.
            CloseBattle(false);
            RecordBattleData(false);
        }else if(BossNowHP <= 0){
            //Turns still remain, and boss lost all HP,WIN.
            CloseBattle(true);
            BossDeadTime = BossDeadTime + 1;
            RecordBattleData(true);
        }
        //store the BossDeadTime data.
        SupportClass.saveIntData(this,"BattleDataProfile","BossDeadTime",BossDeadTime);
    }

    //sub method of BattleCalculation, used in confirm whether User win the battle.
    private void CloseBattle(boolean IsWin){
        //close the battle layout,to make App become normal.
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        ConstraintLayout BossShieldLayout = findViewById(R.id.BossShieldLayout);
        BattleInfLayout.setVisibility(View.GONE);
        BossShieldLayout.setVisibility(View.GONE);
        //reset the value of HP and SD bar.
        ProgressBar BossHPBar =findViewById(R.id.BossHPBar);
        ProgressBar BossShieldBar = findViewById(R.id.BossShieldBar);
        BossHPBar.setProgress(SupportClass.CalculatePercent(BossNowHP,BossTotalHP));
        BossShieldBar.setProgress(SupportClass.CalculatePercent(BossNowSD,BossTotalSD));
        //decide whether user can get EXP, Point and Material as reward.
            if (IsWin && BossState == 1) {//in Conflict battle.
                //give user their reward which they should get in battle of Conflict.
                int RewardTimes = 1;
                //according the Ability decide Reward Times (Multiple number).
                if(BossAbility.toString().contains(getString(R.string.TreasureAbilityTran))){
                    //Ability: "宝藏"， User will have double reward after Boss which has this Ability had beaten.
                    RewardTimes = 2;
                }

                if(UserConflictFloor < CurrentShowFloor){//every floor could only get reward for 1 time.
                    GetEXP(BossEXPValue * RewardTimes);
                    GetPoint(BossPointValue * RewardTimes);
                    GetMaterial(BossMaterialValue * RewardTimes);
                }

                //if this battle is happen in Conflict, this part will ripe off any data which not belongs to normal Boss.
                ClearAllAbility();
                BossAbility.add("Nothing");

                //cancel Conflict Battle state.
                BossState = 0;
                //if boss can Reborn, Reborn it.
                ImportConflictBossData(true);

            }else if(IsWin && BossState == 2){
                GetEXP(BossEXPValue);
                GetPoint(BossPointValue);
                GetMaterial(BossMaterialValue);
                //return to normal boss mode.
                BossState = 0;
            } else if(IsWin && BossState == 3){
                SupportClass.saveLongData(this,"TourneyDataFile","MaxPtRecord",BossPtValue);
                //return to normal boss mode.
                BossState = 0;
            }

            //show final result to user.
            if (IsWin && AppMode.equals("Game")) {
                Toast.makeText(getApplicationContext(), getString(R.string.BattleWinTran), Toast.LENGTH_SHORT).show();
            } else if(!IsWin && AppMode.equals("Game")) {
                Toast.makeText(getApplicationContext(), getString(R.string.BattleLostTran), Toast.LENGTH_SHORT).show();
            }
            //save battle data is belongs to RecordBattleData() method.
        }//end of battle system.


    //talent system.
    int ATKTalentLevel;
    int CRTalentLevel;
    int CDTalentLevel;

    private void InitializingTalentData(){
        ATKTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0);
        CRTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0);
        CDTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0);
    }//end of talent system.


    //Note Folder system.
    String CurrentShowingTitle = "";
    String CurrentUserAnswer = "";
    boolean IsTakeNoteEnabled = false;

    public void ChangeTakeNoteState(View view){
        Button ConfirmAnswerButton = findViewById(R.id.ConfirmAnswerButton);
        EditText QuestTitle = findViewById(R.id.QuestTitle);
        EditText QuestAnswer = findViewById(R.id.QuestAnswer);
        SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
        //1.confirm if TakeNote Function is Enabled.
        IsTakeNoteEnabled = !IsTakeNoteEnabled;
        //2.Open or Close TakeNote Function.
        if(IsTakeNoteEnabled){
            //3.close the answering function, prepare for TakeNote function.
            //3.1.close the button, blocked user from answering Quest.
            ConfirmAnswerButton.setClickable(false);
            //3.2.save the current Quest data in variables.
            CurrentShowingTitle = QuestTitle.getText().toString();
            CurrentUserAnswer = QuestAnswer.getText().toString();
            //3.3.clear EditText to load the Note.
            QuestTitle.setText("");
            QuestTitle.setHint(getString(R.string.TakeNoteHintTran));
            QuestAnswer.setText(A.getString("NoteText","-There is no Note-"));
            Toast.makeText(getApplicationContext(), "Now in Note Input Mode!", Toast.LENGTH_SHORT).show();
        }else{
            //4.save Note to SharedPreference.
            SharedPreferences.Editor editor= A.edit();
            //4.1 put the Note Text in Editor.
            editor.putString("NoteText",QuestAnswer.getText().toString());
            editor.apply();
            //4.2 Open the button.
            ConfirmAnswerButton.setClickable(true);
            //4.3 load the current Quest data from variables.
            QuestTitle.setText(CurrentShowingTitle);
            QuestTitle.setHint(R.string.ReloadTripsTran);
            QuestAnswer.setText(CurrentUserAnswer);
            //4.4 reset the Variables.
            CurrentShowingTitle = "";
            CurrentUserAnswer = "";
        }
    }//end of Folder system.


    //One of the basic logic in the Quest System.This part is working for reload the Quest.
    //to store keyboard state.
    //when MainActivity is initialized, the keyboard is hided(false).
    //if AutoKeyboard function is on, when user try to reload the Quest, the keyboard will appear.(turn the state to true)
    //using this variable to detect if keyboard is opened, and be the base of AutoKeyboard function.
    boolean IsKeyboardOpened = false;

    @SuppressLint("SetTextI18n")
    public void ReloadQuest(View view){
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        TextView QuestIDShow = findViewById(R.id.QuestIDShow);
        EditText QuestTitleView = findViewById(R.id.QuestTitle);
        TextView QuestLevelView = findViewById(R.id.QuestLevel);
        TextView QuestTypeShowView = findViewById(R.id.QuestTypeShow);
        TextView EXPValueView =findViewById(R.id.EXPValue);
        TextView PointValueView = findViewById(R.id.PointValue);
        TextView QuestComboShowView = findViewById(R.id.QuestComboShowView);
        QuestDataBaseBasic QuestDataBase = new QuestDataBaseBasic(this);
        SQLiteDatabase db = QuestDataBase.getReadableDatabase();

        //base logic of random number generator.
        int min = 0;
        int max;
        //because the lines in table starting on 0, so we need to minus 1 based on the number that User can see.(this number is based on starting on 1)
        if (QuestTotalNumber > 1){
            max = QuestTotalNumber - 1;
            }else{
            max = 0;
        }
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int RandomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);//The end of random number generator.
        QuestIDShow.setText((RandomNumber + 1) + "");

        //define rules used in searching data in database. Returning entire table in database. And the results will be sorted in the resulting Cursor.
        //return whole table in "Cursor" form.
        Cursor ResultCursor = db.query(
                QuestDataBaseBasic.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        //randomly choose a line in table "ResultCursor" , return all data we need in this line of table.
        ResultCursor.moveToPosition(RandomNumber);
        QuestTitleContent =ResultCursor.getString(ResultCursor.getColumnIndex("QuestTitle")) ;
        QuestSystemAnswer =ResultCursor.getString(ResultCursor.getColumnIndex("QuestAnswer"));
        QuestLevel = ResultCursor.getInt(ResultCursor.getColumnIndex("QuestLevel"));
        QuestType = ResultCursor.getString(ResultCursor.getColumnIndex("QuestType"));
        ResultCursor.close();

        //show these content to user.
        QuestTitleView.setText(QuestTitleContent);
        QuestLevelView.setText(QuestLevel + "");
        QuestTypeShowView.setText(QuestType);
        //help user focus on typing. Auto pop the virtual keyboard.Thanks to:
        // https://www.jianshu.com/p/6f09de9e903b !
        // https://blog.csdn.net/fenglolo/article/details/108893330 !
        EditText QuestAnswer = findViewById(R.id.QuestAnswer);
        if(SupportClass.getBooleanData(this,"TimerSettingProfile", "AutoKeyboard", false)){
            QuestAnswer.requestFocus();
            InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm && !IsKeyboardOpened) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                IsKeyboardOpened = !IsKeyboardOpened;
            }
        }

        //set basic values of every Quest.
        EXPValueView.setText(QuestLevel + "");
        PointValueView.setText((int) ( QuestLevel * 2 + Math.pow(QuestCombo,1.6) ) + "");
        QuestComboShowView.setText(QuestCombo + "");

        //if you turn on "Game" Mode, you have 3% chance to meet Boss when you try to reload the Quest.
        if(AppMode.equals("Game") && SupportClass.CreateRandomNumber(1,100) <= 3 && BattleInfLayout.getVisibility() == View.GONE){
            //calculate User`s battle data.
            ImportUserBattleData();
            //create a Normal Boss.
            int BossHP = (int)( 23 * (BossDeadTime + 1) * ( 14 + (0.1 * (BossDeadTime + 1) ) ) );
            ClearAllAbility();
            BossAbility.add("Nothing");
            SetBossValues("Experience",UserLevel,BossHP,0,1,10,0,0);
            //Change the Appearance of App, according the String variable AppMode.
            BattleInfLayout.setVisibility(View.VISIBLE);
        }

        //load Quest data completed.reset QuestTimer number.
        Chronometer QuestTimer = findViewById(R.id.QuestTimerView);
        QuestTimer.setBase(SystemClock.elapsedRealtime());
        QuestTimer.start();
    }

    //One of the basic logic in the Quest System. This part is written for decide the correct or not in user`s answer. And it will give feedback to user.
    @SuppressLint("SetTextI18n")
    public void AnswerJudge(View view){
        EditText QuestTitleView = findViewById(R.id.QuestTitle);
        EditText QuestAnswerView =findViewById(R.id.QuestAnswer);
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        boolean BossIsHere = false;
        if (BattleInfLayout.getVisibility() != View.GONE && AppMode.equals("Game")){
            BossIsHere = true;
        }//if Battle interface is close, means that Boss isn`t here, so BossIsHere variable will keep false.

        //!XXX.equals(AAA) is same as XXX != AAA. And this "if" is prevent from empty Quest crash.
        if (!QuestTitleView.getText().toString().equals("")){
            //after Answering the Quest, stop the QuestTimer. And it will be Started in ReloadQuest() method.
            Chronometer QuestTimer = findViewById(R.id.QuestTimerView);
            QuestTimer.stop();
            //Quest System Answer finish loading in ReloadQuest method.
            QuestUserAnswer = QuestAnswerView.getText().toString();
            //1.Compare the answer.And show the counting to user.
            if (QuestUserAnswer.equals(QuestSystemAnswer)){
                //show User judge result.
                UserRightTimes = UserRightTimes + 1 ;
                TextView UserRightTimesView =findViewById(R.id.RightTimes);
                UserRightTimesView.setText(UserRightTimes + "");
                //record user right answering times.
                SupportClass.saveIntData(this,"RecordDataFile","RightAnswering",UserRightTimes);

                //give resources to user as reward.
                GetEXP(QuestLevel);
                ChangeQuestCombo(true);
                GetPoint((int) ( QuestLevel * 2 + Math.pow(QuestCombo,1.6) ));
                if(BattleInfLayout.getVisibility() != View.GONE){
                    BattleCalculation(true,BossIsHere);
                }
                Toast.makeText(getApplicationContext(), getString(R.string.RightHintTran),Toast.LENGTH_SHORT).show();
            }else{
                //show User judge result.
                UserWrongTimes = UserWrongTimes + 1 ;
                TextView UserWrongTimesView = findViewById(R.id.WrongTimes);
                UserWrongTimesView.setText(UserWrongTimes +"");
                //record user wrong answering times.
                SupportClass.saveIntData(this,"RecordDataFile","WrongAnswering",UserWrongTimes);

                //lost EXP as justice.
                if (UserHaveEXP > 0){
                   LostEXP(1);
                }
                ChangeQuestCombo(false);
                if(BattleInfLayout.getVisibility() != View.GONE){
                    BattleCalculation(false,BossIsHere);
                }
                SupportClass.CreateOnlyTextDialog(this,
                        getString(R.string.NoticeWordTran),
                        getString(R.string.WrongHintTran) +"\n" + getString(R.string.AfterWrongAnswerHintTran) + "\n" + QuestSystemAnswer,
                        getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
            }
            //end of answer judgement,load a new Quest automatically.
            ReloadQuest(QuestTitleView);
        }else if(QuestTitleView.getText().toString().equals("")){
            //if no Quest, system will reload a new Quest.
            ReloadQuest(QuestTitleView);
        }
    }

    private void InitializingAppMode(){
        SharedPreferences ChooseAppMode = getSharedPreferences("ChooseAppModeProfile", MODE_PRIVATE);
        if(Objects.equals(ChooseAppMode.getString("AppMode", "Normal"), "Normal")){
            AppMode = "Normal";
        }else if(Objects.equals(ChooseAppMode.getString("AppMode", "Normal"), "Game")){
            AppMode = "Game";
        }else if(Objects.equals(ChooseAppMode.getString("AppMode", "Normal"), "Focus")){
            AppMode = "Focus";
        }else{
            //do it just for more reliable.
            AppMode = "Normal";
        }
    }

    //provide support for get id total number in database, sub method of onCreate.
    private void getIdNumberInfo(){
        QuestTotalNumber = SupportClass.getIntData(this,"IdNumberStoreProfile","IdNumber",0);
    }

    //search the exact number of Quests method. Also being a sub method of onCreate.
    private void ChangeQuestTotalNumber(){
        getIdNumberInfo();
        //show hint to user to set some Quest when no Quest available.
        if (QuestTotalNumber <= 0){
            EditText QuestTitleView =findViewById(R.id.QuestTitle);
            QuestTitleView.setHint(R.string.NoQuestAvailableHintTran);
        }
    }

    //change App appearance according AppMode String variable. Also being a sub method of onCreate.
    private void ModeChangeAppearance(){
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        ConstraintLayout QuestValueLayout = findViewById(R.id.QuestValueLayout);
        ConstraintLayout EXPLayout = findViewById(R.id.EXPLayout);

        switch (AppMode) {
            case "Normal":
                BattleInfLayout.setVisibility(View.GONE);
                break;
            case "Game":
                if(BossState > 0){
                    BattleInfLayout.setVisibility(View.VISIBLE);
                }
                //BossShieldLayout`s visibility will be set in SetBossValue method.
                break;
            case "Focus":
                BattleInfLayout.setVisibility(View.GONE);
                QuestValueLayout.setVisibility(View.GONE);
                EXPLayout.setVisibility(View.GONE);
                break;
        }
    }//end of Layout Management system.


    //timer system.
    int TimerProgress = 0;
    int TimerTargetDay = 0;

    @SuppressLint("SetTextI18n")
    private void InitializingTimer(){
        SharedPreferences TimerInfo = getSharedPreferences("TimerSettingProfile", MODE_PRIVATE);
        boolean TimerState = TimerInfo.getBoolean("TimerEnabled",false);
        boolean TimerFortuneState = TimerInfo.getBoolean("TimerFortune",false);
        TimerProgress = TimerInfo.getInt("TimerProgress", 0);
        TimerTargetDay = TimerInfo.getInt("TimerTargetDay",30);

        String TimerUnitText = TimerInfo.getString("TimerUnit","Sol.");
        String TimerFortuneText;

        if(TimerFortuneState){
            List <String> FortuneList = new LinkedList<>();
            FortuneList.add("[Perfect]");
            FortuneList.add("[Great]");
            FortuneList.add("[Good]");
            FortuneList.add("[Bad]");
            FortuneList.add("[Worst]");
            TimerFortuneText = "The Fortune of Today is:"+ FortuneList.get(SupportClass.CreateRandomNumber(0,4));
        }else{
            TimerFortuneText ="";
        }

        if(TimerState){
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle(R.string.TimerNameWordTran);
            dialog.setMessage("Time Progress:"+"\n"+
                    TimerUnitText+
                    TimerProgress +"/"+TimerTargetDay+"\n"+
                    "Please touch the [Sign] button to add Progress.\n"+
                    TimerFortuneText);
            dialog.setCancelable(false);
            //this variable is only working in this dialog method because of coding limitation.
            final int finalTimerProgress = TimerProgress;
            dialog.setPositiveButton(
                    "Sign",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(finalTimerProgress < TimerTargetDay){
                                //confirm [sign] button be touched.
                                TimerProgress = TimerProgress + 1;
                                //thanks to: https://stackoverflow.com/questions/23179795/android-onclicklistener-intent-and-context !
                                SupportClass.saveIntData(MainActivity.this,"TimerSettingProfile","TimerProgress",TimerProgress);
                            }
                            dialog.cancel();
                        }
                    });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }//end of timer system.


    //Activity support system.
    //Setting button`s function--jump to SettingActivity.
    public void GoToSetting(View view){
        Intent i = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(i);
        //finish();
    }
    //Square button`s function--jump to SquareActivity.
    public void GoToSquare(View view){
        Intent i = new Intent(MainActivity.this, SquareActivity.class);
        if(AppMode.equals("Game")){
           i.putExtra("AppModeImport",100);
        }
        startActivity(i);
    }

    //Folder button`s function--jump to FolderActivity.
    public void GoToFolder(View view){
        Intent i = new Intent(MainActivity.this, FolderActivity.class);
        startActivity(i);
    }

    //Memory button`s function--jump to MemoryActivity.
    public void GoToMemory(View view){
        Intent i = new Intent(MainActivity.this, MemoryActivity.class);
        startActivity(i);
    }

    //Return button`s function--to close the app.
//    public void closeApp(View view){
//        finish();
//    }
    //end of Activity support system.
}