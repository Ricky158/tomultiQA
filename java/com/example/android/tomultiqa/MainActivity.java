package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Locale;

// 2021.2.4 experience:
// 1.setText() can not accept int ,but we can plus a "empty String" to transform int to String.
// 2.can`t think member variable equal to global variable ,it only works in parts!
// 3.if you want to use "A=B is true" in "if-else",please use "=="!
// 2021.3.14 : Android icon making: https://blog.csdn.net/zuo_er_lyf/article/details/82254316 !
// 2021.4.28 : if you want to add a timer to Activity, you need to implement OnChronometerTickListener() here by system, instead of add it in anywhere by yourself.
// !XXX.equals(AAA) is same as XXX != AAA.
// 2021.5.10 : Chronometer class, only it`s widget is [VISIBLE], then the function will work!
// 2021.8.10 : [Enter] Button on keyboard Listener support, thanks to: https://blog.csdn.net/little_shengsheng/article/details/51557084 ! (set in AutoKeyboardControl() method.)
//Website Collection: https://www.jianshu.com/p/eedc3560d192 and https://blog.csdn.net/liuweiballack/article/details/46708697 ! (android IME guide)
// https://www.cnblogs.com/xgjblog/p/9517645.html (navigation bar)
// https://www.jianshu.com/p/b4199549b2f5 and https://www.jianshu.com/p/7ca9b37ec1d5 (signed apk intro)
// 2021.8.30: imageview click animation: https://blog.csdn.net/weixin_39875675/article/details/117545988 !


public class MainActivity extends AppCompatActivity {

    private static final int QUEST_LOAD_DONE = 0;
    private static final int BOSS_IMPORT_DONE = 1;
    private static final int MODE_IMPORT_DONE = 2;
    private static final int USER_IMPORT_DONE = 3;
    private static final int USER_BATTLE_LOAD_DONE = 4;
    private static final int BOSS_VALUE_UI_LOAD = 5;
    private static final int PRINT_COMBO_NUMBER = 6;
    private static final int PRINT_EXP_DATA = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //onCreate() load necessary methods and data before show Activity to user.
        //1.1 load Quest database in new thread.
        //thanks to: https://zhidao.baidu.com/question/1643415767981680180.html !
        Thread MainLoad1 = new Thread(() -> {//load database background.
            //define rules used in searching data in database. Returning entire table in database. And the results will be sorted in the resulting Cursor.
            //return whole table in "Cursor" form.
            QuestDbHelper QuestDataBase = new QuestDbHelper(this);
            SQLiteDatabase db = QuestDataBase.getReadableDatabase();
            ResultCursor = db.query(
                    QuestDbHelper.TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            Message message = new Message();
            message.what = QUEST_LOAD_DONE;
            handler.sendMessage(message);
        });
        MainLoad1.start();
        Thread MainLoad2 = new Thread(() -> {
            //initializing Mode of whole App.
            InitializingAppMode();
            Message message = new Message();
            message.what = MODE_IMPORT_DONE;
            handler.sendMessage(message);
        });
        MainLoad2.start();
        Thread MainLoad3 = new Thread(() -> {
            //import battle data.
            //The EXP information must loaded faster than Boss data to prevent from Calculate Error.
            InitializingEXPInformation();
            InitializingTalentData();
            //Warning: DO NOT move the position between ImportBossState(), ImportConflictBossData() and ImportUserBattleData(), it will make calculation wrong!
            ImportBossState();
            ImportConflictBossData(false);
            Message message = new Message();
            message.what = BOSS_IMPORT_DONE;
            handler.sendMessage(message);
        });
        MainLoad3.start();
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //set Timer Tick Listener.
        Chronometer QuestTimer = findViewById(R.id.FavoriteTimerView);
        QuestTimer.setOnChronometerTickListener(chronometer -> {
            if(BossState > 0 || BattleTurn > 0){//Boss except Normal class, and can't change data during battle nearly close.
                QuestStartTime = QuestStartTime + 1;
                MakeAbilityEffective(4);
            }
        });
    }

    //load UI and logic code, which can be loaded after Activity showed.
    @Override
    protected void onStart() {
        super.onStart();
        //these methods below has its own internal thread, don't put them in another thread!
        InitializingFavorite();
        InitializingRecordData();
        InitializingAssistFunction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChangeQuestTotalNumber();
        InitializingAppMode();
        InitializingEXPInformation();
        //initializing EXP data, including User haveEXP and level.
        InitializingResourceData();
        InitializingTalentData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        expIO.ApplyChanges(this);
        resourceIO.ApplyChanges(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ResultCursor.close();
    }


    //multi-thread UI handler.
    private final Handler handler = new Handler(msg -> {
        switch (msg.what){
            case QUEST_LOAD_DONE:
                //1.2 get total number of id in database according records in SharedPreference, and initializing the database in variables form.
                ChangeQuestTotalNumber();
                break;
            case MODE_IMPORT_DONE:
                //according App Mode to change function module (parts of App UI).
                ModeChangeAppearance(false);
                break;
            case BOSS_IMPORT_DONE:
                //continue load battle data(including battle UI changes.)
                ImportUserBattleData();
                //EXP data will be loaded in another Thread, but UI changes loading in here.
                PrintEXPChanges();
                break;
            case BOSS_VALUE_UI_LOAD:
                //continue ImportConflictBossData() UI changes.
                Intent BossValueIntent = getIntent();
                SetBossValues(
                        BossValueIntent.getStringExtra("Name"),
                        BossValueIntent.getIntExtra("Level",10),
                        BossValueIntent.getIntExtra("HP",1350),
                        BossValueIntent.getIntExtra("SD",0),
                        BossValueIntent.getIntExtra("Turn",10),
                        BossValueIntent.getIntExtra("EXPReward",5),
                        BossValueIntent.getIntExtra("MaterialReward",0),
                        BossValueIntent.getIntExtra("PointReward",350)
                );
                break;
            case USER_IMPORT_DONE:
                //continue load Answering data(including UI changes.)
                InitializingAnsweringData();
                break;
            case USER_BATTLE_LOAD_DONE:
                //finish all boss/user data, Ability, and value calculation, show them to user.
                PrintUserBattleData();
                ReloadBossBaseInf();
                ReloadBossBarData();
                break;
            case PRINT_COMBO_NUMBER:
                PrintComboNumber();
                break;
            case PRINT_EXP_DATA:
                PrintEXPChanges();
                break;
            default:
                break;
        }
        return false;
    });

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int Id = item.getItemId();
        if (Id == R.id.action_copy) {//if setting icon in Menu be touched.
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        }else if(Id == R.id.action_favorite && SupportLib.getIntData(this,"FavoriteFile","FavoriteNumber",0) > 0){
            Intent i = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(i);
        }else if(Id == R.id.action_favorite){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    getString(R.string.EnterFavoriteFailTran),
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
    }

    //Quest system.
    //store the Quest title text for display.
    String QuestTitleContent = "";
    //store total number of Quests in database.Following the search method to define this variable.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    int QuestTotalNumber = 0;
    //store user`s answer text for judge.
    String QuestUserAnswer = "";
    //store system`s answer for judge.
    String QuestSystemAnswer = "";
    //store Quest's Hint text to help user.
    String QuestHintContent = "";
    //store Quest`s type text for display.
    String QuestType = "";
    //store Quest`s level number for display.
    int QuestLevel = 1;
    //store Quest Answer Reward Point Number.
    int QuestCorrectReward = 0;
    //store user`s right times.
    int UserRightTimes = 0;
    //store user`s wrong times.
    int UserWrongTimes = 0;
    //store whole App Mode.
    String AppMode = "Normal";
    //store Quest data in RAM.
    Cursor ResultCursor = null;
    //Secs from Quest Reloaded.
    int QuestStartTime = 0;

    //lv.4 method, main method of Quest system.
    public void AnswerJudge(View view){
        EditText QuestAnswerView = findViewById(R.id.QuestAnswer);

        if (!QuestTitleContent.equals("") && QuestTotalNumber > 0){
            //0.after Answering the Quest, stop the QuestTimer. And it will be Started in ReloadQuest() method.
            StopChronometer();
            //0.1 Quest System Answer finish loading in ReloadQuest() method.
            QuestUserAnswer = QuestAnswerView.getText().toString();
            //0.2 if user enable Auto Case function, App will fix Letter Case automatically.
            //"-1" means function not enable.
            //"0" means auto fix to Lower Case (A to a).
            //"1" means auto fix to Upper Case (a to A).
            if(AutoCaseState == 0){
                QuestUserAnswer = QuestUserAnswer.toLowerCase(Locale.ROOT);
            }else if(AutoCaseState == 1){
                QuestUserAnswer = QuestUserAnswer.toUpperCase(Locale.ROOT);
            }
            //1.Compare the answer.And show the counting to user.
            if (QuestUserAnswer.equals(QuestSystemAnswer)){
                //show User judge result.
                JudgeReport(true);
                //give resources to user as reward.
                expIO.GetEXP(QuestLevel);
                PrintEXPChanges();
                resourceIO.GetPoint(QuestCorrectReward);
                ChangeQuestCombo(true);
                //do battle calculation.
                BattleCalculation(true);
            }else{
                //show User judge result.
                JudgeReport(false);
                //lost EXP as justice.
                expIO.LostEXP(QuestLevel);
                ChangeQuestCombo(false);
                //do battle calculation.
                BattleCalculation(false);
                //show correct answer to user.
                if(IsAutoWrongWindow){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.NoticeWordTran))
                            .setMessage(getString(R.string.WrongHintTran) + "\n" + getString(R.string.AfterWrongAnswerHintTran) + "\n" + QuestSystemAnswer)
                            .setCancelable(true)
                            .setPositiveButton(
                                    getString(R.string.ConfirmWordTran),
                                    (dialog12, id) -> dialog12.cancel())
                            .setNegativeButton(
                                    getString(R.string.FavoriteWordTran),
                                    (dialog1, which) -> {
                                        AddFavorite(null);
                                        dialog1.cancel();
                                    });
                    AlertDialog DialogView = dialog.create();
                    DialogView.show();
                }
            }
            //end of answer judgement,load a new Quest automatically.
            ReloadQuest(QuestAnswerView);
        }else if(QuestTitleContent.equals("") && QuestTotalNumber > 0){
            //if no Quest, system will reload a new Quest.
            ReloadQuest(QuestAnswerView);
        }
    }

    /**
     * lv.3 method, main method of Quest system. and sub method of ReloadQuest() method.
     * @param ShareAnswerView if you have created EditText Object, you can input it to param to improve running cost.<br/>
     *                        if not, then input <code>null</code> to auto create a new object.
     */
    @SuppressLint({"SetTextI18n", "Range"})
    private void ReloadQuest(EditText ShareAnswerView){
        TextView QuestIDShow = findViewById(R.id.FavoriteIDView);
        EditText QuestTitleView = findViewById(R.id.QuestTitle);
        TextView QuestLevelView = findViewById(R.id.QuestLevel);
        TextView QuestTypeShowView = findViewById(R.id.QuestTypeShow);
        TextView EXPValueView =findViewById(R.id.EXPValue);
        TextView PointValueView = findViewById(R.id.PointValue);

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

        int RandomNumber = SupportLib.CreateRandomNumber(min, max);//The end of random number generator.
        QuestIDShow.setText(String.valueOf((RandomNumber + 1)));
        //Cursor is loaded in ChangeQuestTotalNumber() method.
        //randomly choose a line in table "ResultCursor" , return all data we need in this line of table.
        ResultCursor.moveToPosition(RandomNumber);
        QuestTitleContent = ResultCursor.getString(ResultCursor.getColumnIndex("QuestTitle")) ;
        QuestSystemAnswer = ResultCursor.getString(ResultCursor.getColumnIndex("QuestAnswer"));
        QuestHintContent = ResultCursor.getString(ResultCursor.getColumnIndex("QuestHint"));
        QuestLevel = ResultCursor.getInt(ResultCursor.getColumnIndex("QuestLevel"));
        QuestType = ResultCursor.getString(ResultCursor.getColumnIndex("QuestType"));
        //Cursor will be closed in OnDestroy() method.

        //show these content to user.
        QuestTitleView.setText(QuestTitleContent);
        QuestLevelView.setText(String.valueOf(QuestLevel));
        QuestTypeShowView.setText(QuestType);

        //control Auto Keyboard function is enable or not.
        if(ShareAnswerView == null){//if view variable is null, then create a new one to decrease creating cost. if not, then skip this branch.
            ShareAnswerView = findViewById(R.id.QuestAnswer);
        }
        AutoKeyboardControl(ShareAnswerView);
        AutoClearAnswerControl(ShareAnswerView);

        //set basic values of every Quest.
        EXPValueView.setText(String.valueOf(QuestLevel));
        QuestCorrectReward =(int) Math.min(( QuestLevel * 10 + Math.pow(QuestCombo,2.6) ),10000);
        PointValueView.setText(String.valueOf(QuestCorrectReward));
        PrintComboNumber();

        //if you turn on "Game" Mode, you have 3% chance to meet Boss when you try to reload the Quest.
        if(AppMode.equals(ValueLib.GAME_MODE) && SupportLib.CreateRandomNumber(1,100) <= 3 && BossState == -1){
            //BossState == -1: no boss or just a normal boss dead in last battle.
            //calculate User`s battle data.
            ImportUserBattleData();
            //create a Normal Boss.
            BossState = 0;
            int Index = BossDeadTime + 1;
            int BossHP = (int)( 10 * (Index) * ( 11 + ( 2.2 * (Index) ) ) );
            //DO NOT USE same name method in handler.
            SetBossValues(getString(R.string.ExperienceBossNameTran), expIO.UserLevel,BossHP,0,10,10,0,0);
            //Change the Appearance of App, according the String variable AppMode.
            OpenBattleLayout();
        }

        //load Quest data completed.Unlock Favorite adding and Reset QuestTimer.
        ImageView FavoriteQuestButton = findViewById(R.id.FavoriteQuestButton);
        if(!FavoriteQuestButton.isEnabled()){
            FavoriteQuestButton.setEnabled(true);
        }
        StartChronometer();
    }

    /**
     * lv.2 method, sub method of AnswerJudge() method.
     * @param IsQuestRight if this param is <code>true</code>, then refresh UI of Battle combo, Answering Right/Wrong data according variables.
     */
    @SuppressLint("SetTextI18n")
    private void JudgeReport(boolean IsQuestRight){
        if(IsQuestRight){
            final TextView UserRightTimesView =findViewById(R.id.RightTimes);
            UserRightTimes = UserRightTimes + 1 ;
            UserRightTimesView.setText(String.valueOf(UserRightTimes));
            //record user right answering times.
            SupportLib.saveIntData(this,"RecordDataFile","RightAnswering",UserRightTimes);
            //show to user with Color effect.
            ReportQuestForWhile(UserRightTimesView);
        }else{
            final TextView UserWrongTimesView = findViewById(R.id.WrongTimes);
            UserWrongTimes = UserWrongTimes + 1 ;
            UserWrongTimesView.setText(String.valueOf(UserWrongTimes));
            //record user wrong answering times.
            SupportLib.saveIntData(this,"RecordDataFile","WrongAnswering",UserWrongTimes);
            ReportQuestForWhile(UserWrongTimesView);
        }
    }

    //lv.1 method, main method of Print Quest Hint to user.
    boolean IsShowHintEnabled = false;
    public void ShowQuestHint(View view){
        ImageView ConfirmAnswerButton = findViewById(R.id.ConfirmAnswerButton);
        ImageView TakeNoteButton = findViewById(R.id.TakeNoteButton);
        EditText QuestTitle = findViewById(R.id.QuestTitle);
        EditText QuestAnswer = findViewById(R.id.QuestAnswer);
        //0.close note function, because they use same view to display.
        TakeNoteButton.setEnabled(false);
        //1.change hint function state.
        IsShowHintEnabled = !IsShowHintEnabled;
        //2.confirm if TakeNote Function is Enabled.
        if(IsShowHintEnabled){
            //3.close the answering function, prepare for Hint function.
            //3.1.close the button, blocked user from answering Quest.
            ConfirmAnswerButton.setEnabled(false);
            //3.2.save the current Quest data in variables.
            CurrentShowingTitle = QuestTitle.getText().toString();
            CurrentUserAnswer = QuestAnswer.getText().toString();
            //3.3.clear EditText to load the Hint.
            QuestTitle.setText("");
            //3.4 Hint Content data fix,
            QuestTitle.setHint(getString(R.string.ShowingQuestHintTran));
            if(QuestHintContent.equals("Nothing")){//"Nothing" is default value of Quest Hint Setting. DO NOT CHANGE!
                QuestHintContent = getString(R.string.ThisQuestNoHintTran);//no Quest loaded.
            }else if(QuestHintContent.equals("")){
                QuestHintContent = getString(R.string.QuestHintNoDataTran);
            }
            //3.5 show to user.
            QuestAnswer.setText(QuestHintContent);
        }else{
            //4.1 Open the confirm button.
            ConfirmAnswerButton.setEnabled(true);
            //4.2 load the current Quest data from variables.
            QuestTitle.setText(CurrentShowingTitle);
            QuestTitle.setHint(R.string.ReloadTripsTran);
            QuestAnswer.setText(CurrentUserAnswer);
            //4.3 reset the Variables.
            CurrentShowingTitle = "";
            CurrentUserAnswer = "";
            //4.4 open note function.
            TakeNoteButton.setEnabled(true);
        }
    }

    //lv.1 method, main import data method.
    @SuppressLint("SetTextI18n")
    private void InitializingAnsweringData(){
        TextView RightTimes = findViewById(R.id.RightTimes);
        TextView WrongTimes = findViewById(R.id.WrongTimes);
        int[] RecordData = SupportLib.getMultiInt(
                this,
                "RecordDataFile",
                new String[]{"RightAnswering","WrongAnswering"},
                new int[]{0,0}
        );
        UserRightTimes = RecordData[0];
        RightTimes.setText(String.valueOf(UserRightTimes));
        UserWrongTimes = RecordData[1];
        WrongTimes.setText(String.valueOf(UserWrongTimes));
    }

    //default TextView color, thanks to: https://www.javaroad.cn/questions/37295 !
    /**
     * lv.1 method, sub method of JudgeReport() method. Using in give specific TextView a short animation effect to tell user: "it changes".
     * @param Target which TextView need to refresh with animation.
     */
    private void ReportQuestForWhile(final TextView Target){
        Target.setTextColor(Color.BLACK);
        Target.setAlpha(1.0f);
        //reset color in 0.5 sec later.
        handler.postDelayed(() -> Target.setAlpha(0.54f), 500);
    }//end of Quest system.(not including sub parts of it.)


    //battle system.
    //value "SD" means Shield point, value
    //basic values of Battle.
    //boss data part.
    /**
     * BossState:<br/>
     * -1 is no boss. (no battle are ready to start)<br/>
     * 0 is normal boss.<br/>
     * 1 is Conflict boss.<br/>
     * 2 is Daily boss.<br/>
     * 3 is Tourney boss.<br/>
     * The initial(default) value of this variable is -1.<br/>
     * And it will be loaded in ImportBossState() method.
     */
    int BossState;
    int BossLevel = 0;
    int BossTotalHP = 0;
    int BossNowHP = 0;
    int BossTotalSD = 0;
    int BossNowSD = 0;
    int BossEXPValue;
    int BossPointValue;
    int BossMaterialValue;
    //"Pt" is a record unit in Tourney function, is not the same thing with "Point".
    long BossPtValue;
    //user data part.
    int UserATK;//total value.
    int AlchemyATK = 0;
    double UserCriticalRate;//total value.
    int AlchemyCriticalRate = 0;
    double UserCriticalDamage;//total value.
    int AlchemyCriticalDamage = 0;
    /*
    Cheat ATK, Level Excess data are variables in ImportUserBattleData() to save RAM usage.
     */
    //battle data part.
    int AlchemyTurn;
    int BattleTurn;
    int BossDeadTime;
    //https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values
    //https://stackoverflow.com/questions/10976212/arraylist-as-global-variable
    //Ability data part.
    ArrayList<String> BossAbility = new ArrayList<>();//this variable is used in import data from another Activity, and using in Ability calculation.
    //record multi mode boss`s DeadTime to figure out is it need to change the Mode or stop Reborn.You can think this is Boss`s HP Bar number.
    int BossRebornTime = 0;
    /**
     * <code>BossModeNumber</code> means Multi Mode Boss`s Mode total number.<br/>
     * In current version, this variable always be 1 (have 1 HP bar number).
     */
    int BossModeNumber = 1;
    int UserConflictFloor;
    int CurrentShowFloor;
    int DailyDifficulty;
    //data in battle calculation.
    /**
     * <code>LevelDamageChange</code> means the index which control damage add or less by Level difference between Boss and User.
     */
    double LevelDamageChange = 0.0;
    double CurrentDamage = 0;

    //import boss data part.
    /**
     * lv.4 method. import BossState from another Activity to confirm Boss's type, and do some specific loading for given BossState.
     */
    private void ImportBossState(){
        BossState = getIntent().getIntExtra("BossState",-1);
        //don`t move ImportUserBattleData() to here,because not all Boss comes from Another Activities!
        switch (BossState){
            //case 0 is same as case 1, all skip this branch.
            case 1:
                break;
            case 2:
                DailyDifficulty = getIntent().getIntExtra("DailyDifficulty",1);
                break;
            case 3:
                BossPtValue = getIntent().getLongExtra("BossPtValue",100);
                break;
        }
    }

    /**
     * lv.3 method, main method of import data from another Activity. if another Activity pass a boss information through Intent.<br/>
     * This method will read them and call UI update.
     * @param IsRebornMode In future, the App will have multi-round battle, if boss have multiple HP bar, then it's Ability data should be refresh.<br/>
     *                     For this situation, you should put <code>true</code> in this param.<br/>
     *                     If you need Initialize with page, put false here.
     */
    private void ImportConflictBossData(boolean IsRebornMode){
        //confirm is boss have multi mode, and confirm which Ability List should be imported in battle system.
        //this part is only works on Initializing Boss data, so it can`t used in Reborn.
        if(!IsRebornMode && BossState > 0){
            BossModeNumber = getIntent().getIntExtra("ModeNumber",1);
            BossRebornTime = BossModeNumber;//give this boss X times chance to reborn.
            switch (BossModeNumber){
                case 3:
                    BossAbility = getIntent().getStringArrayListExtra("BossAbilityThird");
                    break;
                case 2 :
                    BossAbility = getIntent().getStringArrayListExtra("BossAbilitySecond");
                    break;
                case 1:
                    BossAbility = getIntent().getStringArrayListExtra("BossAbility");
                    break;
            }
            //save data in String, because making ArrayList to be global variable is very hard.
        }
        //if boss have multi HP, the BossRebornTime variable should be minus 1 when boss is reborned.
        if(BossRebornTime > 0 && BossState > 0){
            BossRebornTime = BossRebornTime - 1;
            //because of the limitation of ArrayList(can`t be a parameter into a method, because it is a class actually.)
            //So it`s value will be updated and add to SetBossValues method independently.
            //BossAbility need to be changed in other code.
            ImportBossAbility();
            //call SetBossValue() method in handler.
            Message message = new Message();
            message.what = BOSS_VALUE_UI_LOAD;
            handler.sendMessage(message);
            Thread thread = new Thread(() -> {
                int[] FloorData = SupportLib.getMultiInt(
                        this,
                        "BattleDataProfile",
                        new String[]{"UserConflictFloor","CurrentShowFloor"},
                        new int[]{0,1}
                );
                UserConflictFloor = FloorData[0];
                CurrentShowFloor = FloorData[1];
            });
            thread.start();
        }
    }

    //lv.3 method, main method of import data.
    private void ImportUserBattleData(){
        Thread UserBattleLoad = new Thread(() -> {
            if(AppMode.equals(ValueLib.GAME_MODE)){
                int CheatATK = SupportLib.getIntData(this,"CheatFile","CheatATK",0);
                int[] ExcessData = SupportLib.getMultiInt(
                        this,
                        "ExcessDataFile",
                        new String[]{"LevelExcessATK","LevelExcessCR","LevelExcessCD"},
                        new int[]{0,0,0}
                        );
                int LevelExcessATK = ExcessData[0];
                int LevelExcessCR = ExcessData[1];
                int LevelExcessCD = ExcessData[2];
                //1.calculate basic value of user battle data.
                UserATK = 10 + expIO.UserLevel * 10 + ATKTalentLevel * ValueLib.ATK_TALENT_INDEX + CheatATK + LevelExcessATK;
                UserCriticalRate = 5.00 + CRTalentLevel * ValueLib.CR_TALENT_INDEX + LevelExcessCR;
                UserCriticalDamage = 150.0 + CDTalentLevel * ValueLib.CD_TALENT_INDEX + LevelExcessCD;
                //2.import another way to up the value.
                //put Alchemy data in user data.
                ImportAlchemyEffect();//we need re-calculate the Alchemy data when user basic data is changed.and get Alchemy adding data.
                if(AlchemyTurn > 0){
                    UserATK = UserATK + AlchemyATK;
                    UserCriticalRate = UserCriticalRate + AlchemyCriticalRate;
                    UserCriticalDamage = UserCriticalDamage + AlchemyCriticalDamage;
                }
                //2.1 Boss Ability, which useful in battle Start`s calculation.
                MakeAbilityEffective(0);
            }
            Message message = new Message();
            message.what = USER_BATTLE_LOAD_DONE;
            handler.sendMessage(message);
        });
        UserBattleLoad.start();
    }//end of import boss data part.

    //lv.3 method. main method of battle system.
    //delay executed, thanks to: https://blog.csdn.net/mq2856992713/article/details/52005253 !
    boolean IsShowDamage;

    @SuppressLint("SetTextI18n")
    private void BattleCalculation(boolean IsQuestRight){
        final TextView DamageShowView = findViewById(R.id.DamageShowView);
        if(BossState != -1){//if boss is exist(battle UI is ON), then calculate battle detail, or skip process to save time.
            //if Random Number less than CriticalRate * 100, system will think NO Critical, so IsCritical variable will keep false.
            boolean IsCritical = SupportLib.CreateRandomNumber(0, 10000) <= (UserCriticalRate * 100);
            //calculate the basic damage to Boss.
            if (BattleTurn > 0 && BossNowHP > 0){
                //if critical, then add additional damage to damage counting.
                //check Boss and User Level, and change the Damage.
                if(IsCritical){
                    CurrentDamage = UserATK * UserCriticalDamage / 100.0 * LevelDamageChange;
                }else{
                    CurrentDamage = UserATK * LevelDamageChange;
                }
                //Boss Ability, which useful in battle progression`s calculation.
                MakeAbilityEffective(3);
                //calculate damage to user in double form,but show to user with int form.
                int DamageToUser;
                //if user answer wrong, let the damage to 0. or passed these code when it's right.
                if(IsQuestRight){
                    DamageToUser = (int) CurrentDamage;
                }else{
                    DamageToUser = 0;
                }
                //if damage number smaller than 0, fix it to 0.
                DamageToUser = Math.max(0,DamageToUser);
                //calculating Shield effect.
                if (DamageToUser <= BossNowSD){
                    //Damage still 0 when it after Shield process.
                    BossNowSD = BossNowSD - DamageToUser;
                    DamageToUser = 0;
                }else {
                    DamageToUser = DamageToUser - BossNowSD;
                    BossNowSD = 0;
                }
                //finally, cost Boss`s HP based on damage counting.
                BossNowHP = BossNowHP - DamageToUser;
                if(!IsCritical && IsShowDamage){
                    DamageShowView.setText("- " + DamageToUser);
                }else if(IsCritical && IsShowDamage){
                    DamageShowView.setText("Critical! - " + DamageToUser);
                }
                //after one time`s damage calculation, should cost 1 Turn.
                BattleTurn = BattleTurn - 1;
                MakeAbilityEffective(1);
                MakeAlchemyEffective();
                //after calculation, show User the detail.
                PrintUserBattleData();
                ReloadBossBaseInf();
                ReloadBossBarData();
                //report damage data to user, and close showing after a while.
                handler.postDelayed(() -> DamageShowView.setText("--"), 475);
                //if boss lost all its HP, still in calculation, then User Win. And close the battle immediately.
                if (BossNowHP <= 0){
                    CloseBattle(true);
                    BossDeadTime = BossDeadTime + 1;
                    RecordBattleData(true);
                }
                //judging whether boss is die.
            }else if(BattleTurn < 1){
                //cost all Turns, but boss still have HP, LOST.
                CloseBattle(false);
                RecordBattleData(false);
            }else{//BossHP <= 0.
                //Turns still remain, and boss lost all HP,WIN.
                CloseBattle(true);
                BossDeadTime = BossDeadTime + 1;
                RecordBattleData(true);
            }
            //store the BossDeadTime data.
            SupportLib.saveIntData(this,"BattleDataProfile","BossDeadTime",BossDeadTime);
        }
    }

    //lv.2 method, sub method of BattleCalculation() method, used in confirm whether User win the battle.
    private void CloseBattle(boolean IsWin){
        //close the battle layout,to make App become normal.
        ConstraintLayout EXPLayout = findViewById(R.id.EXPLayout);
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        //make EXP counter visible for user after battle.
        EXPLayout.setVisibility(View.VISIBLE);
        BattleInfLayout.setVisibility(View.GONE);
        //reset the value of HP and SD bar.
        ReloadBossBarData();
        //decide whether user can get EXP, Point and Material as reward.
        if(IsWin){
            switch (BossState){
                case 0:
                    BossState = -1;//for normal boss, just reset the BossState to -1, means no boss ready to fight.
                    break;
                case 1://in Conflict battle.
                    //give user their reward which they should get in battle of Conflict.
                    int RewardTimes = 1;
                    //according the Ability decide Reward Times (Multiple number).
                    if(BossAbility.toString().contains(getString(R.string.TreasureAbilityTran))){
                        //Ability: "宝藏"， User will have double reward after Boss which has this Ability had beaten.
                        RewardTimes = 2;
                    }
                    if(UserConflictFloor < CurrentShowFloor){//every floor could only get reward for 1 time.
                        expIO.GetEXP(BossEXPValue * RewardTimes);
                        PrintEXPChanges();
                        resourceIO.GetPoint(BossPointValue * RewardTimes);
                        resourceIO.GetMaterial(BossMaterialValue * RewardTimes);
                    }
                    //if boss can Reborn, Reborn it.
                    ImportConflictBossData(true);
                    if(BossRebornTime <= 0){
                        //cancel Conflict Battle state. return to no boss here.
                        BossState = -1;
                    }
                    break;
                case 2:
                    expIO.GetEXP(BossEXPValue);
                    PrintEXPChanges();
                    resourceIO.GetPoint(BossPointValue);
                    resourceIO.GetMaterial(BossMaterialValue);
                    //return to no boss here.
                    BossState = -1;
                    break;
                case 3:
                    //record max evaluation number.
                    Thread thread = new Thread(() -> {
                        if(BossPtValue >= SupportLib.getLongData(this,"TourneyDataFile","MaxPtRecord",0) ){
                            SupportLib.saveLongData(this,"TourneyDataFile","MaxPtRecord",BossPtValue);
                        }
                    });
                    thread.start();
                    //return to no boss here.
                    BossState = -1;
                    break;
            }
            //for any kind of boss: Clear last boss's Ability data to make sure right calculation.
            //(ripe off any data which not belongs to normal Boss.)
            ClearAllAbility();
            //show final result to user.
            Toast.makeText(this, getString(R.string.BattleWinTran), Toast.LENGTH_SHORT).show();
        }else{
            if(BossState != -1){//if CloseBattle() are called, then the battle still not closed, then close it.
                BossState = -1;
            }
            //show final result to user. And save data.
            SupportLib.saveIntData(this,"RecordDataFile","BattleFail", SupportLib.getIntData(this,"RecordDataFile","BattleFail",0) + 1);
            Toast.makeText(this, getString(R.string.BattleLostTran), Toast.LENGTH_SHORT).show();
        }
    }//save battle data is belongs to RecordBattleData() method.

    //lv.2 method, sub method of battle system.
    //text color, thanks to: https://stackoverflow.com/questions/6468602/what-is-default-color-for-text-in-textview !
    //Reward means the present that User will have when it defeat the Boss.
    //Turn means User will have this number`s chance to attack Boss.
    @SuppressLint("SetTextI18n")
    private void SetBossValues(String name, int Level, int HP, int SD, int Turn, int EXPReward, int MaterialReward, int PointReward){
        TextView BossNameView = findViewById(R.id.BossNameView);
        TextView BossLevelView = findViewById(R.id.RecommendLevelView);
        TextView BossModeShowView = findViewById(R.id.BossModeShowView);
        TextView BossAbilityShowView = findViewById(R.id.BossAbilityShowView);

        BossNameView.setText(name);
        BossLevel = Level;
        //calculate Level Damage Change Index.
        //because of multi-thread loading not sync to battle loading, so we need to single load UserLevel again to make sure it is correct.
        int LevelDifference = expIO.UserLevel - BossLevel;
        LevelDamageChange = 1.0 + LevelDifference * 0.03;
        //if BossLevel is different with UserLevel, then add Color to TextView to help user recognize situation.
        //save original colors. (because of BossLevelView's color will not automatically back to default, so the color sample can't be itself.)
        ColorStateList TextDefaultColor =  BossModeShowView.getTextColors();
        if(LevelDifference <= -30){
            BossLevelView.setTextColor(Color.BLACK);
        }else if(LevelDifference <= -20){
            BossLevelView.setTextColor(Color.RED);
        }else if(LevelDifference <= -10){
            BossLevelView.setTextColor(Color.GREEN);
        }else{
            BossLevelView.setTextColor(TextDefaultColor);
        }
        BossLevelView.setText("Lv." + BossLevel);

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
        if(BossAbility.isEmpty()){
            BossAbilityShowView.setText(getString(R.string.NoBossAbilityWordTran));
        }else{
            BossAbilityShowView.setText(BossAbility.toString());
        }

        BossTotalHP = HP;
        BossNowHP = BossTotalHP;
        BossTotalSD = SD;
        BossNowSD = BossTotalSD;
        BattleTurn = Turn;
        BossEXPValue = EXPReward;
        BossPointValue = PointReward;
        BossMaterialValue = MaterialReward;
        //in Initialization, NowData is equals to MaxData.
        ReloadBossBaseInf();
        //reset the HP/SD Bar value.
        //this code can`t be replaced with ReloadBossBarData() method.
        ProgressBar BossHPBar = findViewById(R.id.BossHPBar);
        //set progressbar initial value.
        BossHPBar.setSecondaryProgress(100);//HP initial value.
        if(SD <= 0){//SD initial value.
            BossHPBar.setProgress(0);
        }else{
            BossHPBar.setProgress(100);
        }
    }

    //lv.1 method, sub method of Print Battle Data to user.
    @SuppressLint("SetTextI18n")
    private void PrintUserBattleData(){
        TextView UserATKView = findViewById(R.id.UserATKView);
        TextView UserCriticalRateView = findViewById(R.id.UserCriticalRateView);
        TextView UserCriticalDamageView = findViewById(R.id.UserCriticalDamageView);
        UserATKView.setText(String.valueOf(UserATK));
        UserCriticalDamageView.setText(SupportLib.ReturnTwoBitText(UserCriticalDamage));
        UserCriticalRateView.setText(SupportLib.ReturnTwoBitText(UserCriticalRate));
    }

    //lv.1 method, sub method of BattleCalculation() method.
    private void RecordBattleData(boolean IsBattleWin){
        Thread thread = new Thread(() -> {
            //record the max Difficulty user have ever finished.
            if(IsBattleWin && SupportLib.getIntData(this,"RecordDataFile","MaxDailyDifficulty",0) < DailyDifficulty){
                SupportLib.saveIntData(this,"RecordDataFile","MaxDailyDifficulty", DailyDifficulty);
            }
            //if user win on higher floor, record that data.
            if(IsBattleWin && CurrentShowFloor >= UserConflictFloor){
                UserConflictFloor = CurrentShowFloor;
                SupportLib.saveIntData(this,"BattleDataProfile","UserConflictFloor",UserConflictFloor);
            }
        });
        thread.start();
    }

    //lv.1 method, sub method of BattleCalculation() and SetBossValues() method.
    //Max SD is a internal value, not need to show to user.
    @SuppressLint("SetTextI18n")
    private void ReloadBossBaseInf(){
        TextView BossHPShowView = findViewById(R.id.BossHPShowView);
        TextView BattleTurnView = findViewById(R.id.BattleTurnView);
        TextView AlchemyTurnView = findViewById(R.id.AlchemyTurnView);
        if(BossNowSD > 0){
            BossHPShowView.setText(BossNowHP + " (+" + BossNowSD + ") / " + BossTotalHP);
        }else{
            BossHPShowView.setText(BossNowHP + " / " + BossTotalHP);
        }
        BattleTurnView.setText(String.valueOf(BattleTurn));
        AlchemyTurnView.setText(String.valueOf(AlchemyTurn));
    }

    //lv.1 method,sub method of BattleCalculation() and CloseBattle() method.
    private void ReloadBossBarData(){
        ProgressBar BossHPBar = findViewById(R.id.BossHPBar);
        BossHPBar.setSecondaryProgress(SupportLib.CalculatePercent(BossNowHP,BossTotalHP));//background progress.
        if(BossNowSD > 0){//2.if SD is more than 0, reset progress. keep this method is make sure that boss without Shield can display correct.
            BossHPBar.setProgress(SupportLib.CalculatePercent(BossNowSD,BossTotalSD));//foreground progress.
        }else{
            BossHPBar.setProgress(0);
        }
    }//end of main part.


    //talent part.
    int ATKTalentLevel;
    int CRTalentLevel;
    int CDTalentLevel;

    //lv.1 method, import method.
    private void InitializingTalentData(){
        int[] TalentData = SupportLib.getMultiInt(
                this,
                "BattleDataProfile",
                new String[]{"ATKTalentLevel","CRTalentLevel","CDTalentLevel"},
                new int[]{0,0,0}
                );
        ATKTalentLevel = TalentData[0];
        CRTalentLevel = TalentData[1];
        CDTalentLevel = TalentData[2];
    }//end of talent part.


    //Boss Ability part.
    //lv.2 method, sub method of BattleCalculation() method. And this method, making all of Boss Abilities effective.
    boolean IsAlchemyStop = false;//record is Alchemy add has been removed, prevent from multiple Remove bug.

    /**
     * Controls all Ability Effect in battle. Using Position attribute to define Ability Effect in different situations.<br/>
     * Notice:<br/>
     * Some Abilities like "宝藏(treasure)" will affecting in independent code.
     * @param Position
     * A number represents Ability affect situation.<br/>
     * "0" means "Start": Abilities which should effective on the Start of Battle. Most of these Abilities are about User or Boss Data.<br/>
     * "1" means "Middle": Middle Position means that Abilities and Alchemy Effect, which should effective on the every turn`s end of Battle.<br/>
     * Actually, Alchemy is not one of Ability part,but we put it here to calculate effect per Turn more conveniently.<br/>
     * "2" means "Certain": Abilities which should effective on the specific Turn(s) of Battle. But we don't have this kind of Ability yet.<br/>
     * "3" means "Calculate": Abilities which should effective on the every time of damage calculation in Battle.<br/>
     * "4" means: "PerSecond": Each second in Answering Quest will have chance to activated some Ability.<br/>
     */
    @SuppressLint("SetTextI18n")
    private void MakeAbilityEffective(int Position){// TODO: 2021/9/19 define BossAbility Effect here.
        //which used in judging how to make Ability effective.
        if(!BossAbility.isEmpty()){
            switch (Position) {
                case 0:
                    if (BossAbility.contains(getString(R.string.FragileAbilityTran))) {
                        //Ability:"脆弱", User`s Critical Rate will be added to 100%.
                        UserCriticalRate = 100.00;
                    }
                    if(BossAbility.contains(getString(R.string.CurseAbilityTran))){
                        UserCriticalRate = 0.0;
                    }
                    if(BossAbility.contains(getString(R.string.WeakSpotAbilityTran))) {
                        //Ability:"弱点", User`s Critical Damage will be added extra 50%.
                        UserCriticalDamage = UserCriticalDamage + 50.0;
                    }
                    if(BossAbility.contains(getString(R.string.DiversionAbilityTran))){
                        UserCriticalDamage = (int)(UserCriticalDamage * 0.6);
                    }
                    if(BossAbility.contains(getString(R.string.CorrosionAbilityTran))){
                        //Ability:"腐蚀", User`s Critical Rate will lost 25%.
                        UserATK = (int) (UserATK * 0.75);
                    }
                    if(BossAbility.contains(getString(R.string.RushAbilityTran))){
                        BattleTurn = BattleTurn - 1;
                    }
                    if(BossAbility.contains(getString(R.string.SlowAbilityTran))){
                        BattleTurn = BattleTurn + 1;
                    }
                    //the Duel Ability actually not effect at all, just notice user this boss only have 1 turn to fight, so there are no code for it.
                    if(BossAbility.contains(getString(R.string.TrialAbilityTran))){
                        BossTotalHP = (int)(BossTotalHP * 1.3);
                        BossNowHP = BossTotalHP;
                    }
                    if(BossAbility.contains(getString(R.string.ShieldAbilityTran))){
                        BossNowSD = (int)(BossTotalHP * 0.35);
                        BossTotalSD = BossNowSD;
                    }
                    break;
                case 1:
                    if(BossAbility.contains(getString(R.string.RecoverAbilityTran)) && BossNowHP > 0){
                        BossNowHP = (int) (BossNowHP * (1 + SupportLib.CreateRandomNumber(0,25) / 100.0));
                        if(BossNowHP > BossTotalHP){
                            BossNowHP = BossTotalHP;
                        }
                    }
                    if(BossAbility.contains(getString(R.string.LastHurtAbilityTran)) && BossNowHP > 0){
                        BossNowHP = (int) (BossNowHP * (1 - SupportLib.CreateRandomNumber(0,17) / 100.0));
                        if(BossNowHP < 0){
                            BossNowHP = 0;
                        }
                    }
                    if(BossAbility.contains(getString(R.string.FastStepAbilityTran))){
                        //provide additional BattleTurn minus 1, and it equals to -2 per Turn.
                        BattleTurn = BattleTurn - 1;
                    }
                    if(BossAbility.contains(getString(R.string.ReTimeAbilityTran)) && SupportLib.CreateRandomNumber(0,100) < 33){
                        BattleTurn = BattleTurn + 1;
                    }
                    break;
                case 2:
                    //Certain Position means that Abilities which should effective on the specific Turn(s) of Battle.
                    //......
                    break;
                case 3:
                    if (BossAbility.contains(getString(R.string.ProudAbilityTran)) && CurrentDamage / BossTotalHP < 0.11) {
                        //Ability:"傲立", Boss will not take any damage which damage number is lower than Boss`s 11% HP.
                        CurrentDamage = 0;
                    }
                    if(BossAbility.contains(getString(R.string.HopelessAbilityTran))){
                        CurrentDamage = 0;
                    }
                    if(BossAbility.contains(getString(R.string.FearAbilityTran)) && CurrentDamage / BossTotalHP > 0.15){
                        CurrentDamage = BossTotalHP * 0.15;
                    }
                    break;
                case 4:
                    if(BossAbility.contains(getString(R.string.ActiveAbilityTran)) && SupportLib.CreateRandomNumber(0,100) < 3){
                        ReloadQuest(null);
                        BossNowHP = (int) (BossNowHP + BossTotalHP * 0.1);//recover 10% HP.
                    }
                    if(BossAbility.contains(getString(R.string.WakeAbilityTran)) && QuestStartTime >= 15){
                        BossNowHP = (int) (BossNowHP + BossTotalHP * 0.001 * QuestStartTime);//recover 1.5%+ HP.
                    }
                    if(BossAbility.contains(getString(R.string.DestructAbilityTran)) && QuestStartTime >= 30){
                        QuestStartTime = 0;//reset timer to prevent from error.
                        ReloadQuest(null);
                    }
                    if(BossNowHP >= BossTotalHP){//data fix.
                        BossNowHP = BossTotalHP;
                    }
                    //Reset UI.
                    ReloadBossBaseInf();
                    ReloadBossBarData();
                    break;
            }
        }
    }

    /**
     * In each turn (no matter the Answer is right or not) in battle, the Alchemy Turn will be cost, then refresh by this method.
     */
    private void MakeAlchemyEffective(){
        //in every turn, we will check the Alchemy has enough Turn to Effective or not, if not, user will lost this effect.
        //And the data in AlchemyDataFile will be riped off.
        if(AlchemyTurn > 0){
            AlchemyTurn = AlchemyTurn - 1;
        }else if(!IsAlchemyStop){//and Alchemy Turn is <= 0.
            UserATK = UserATK - AlchemyATK;
            UserCriticalRate = UserCriticalRate - AlchemyCriticalRate;
            UserCriticalDamage = UserCriticalDamage - AlchemyCriticalDamage;
            AlchemyTurn = 0;
            IsAlchemyStop = true;
            SupportLib.saveIntData(this,"AlchemyDataFile","AlchemyTurn",0);
        }
    }

    //lv.1 method, sub method of ImportUserBattleData() and MakeAbilityEffective() method.
    //for stable reason, can't use multi-thread on this method.
    private void ImportAlchemyEffect(){
        int[] AlchemyData = SupportLib.getMultiInt(
                this,
                "AlchemyDataFile",
                new String[]{"ATKup","CRup","CDup","AlchemyTurn"},
                new int[]{0,0,0,0}
        );
        AlchemyATK = (int) (AlchemyData[0] * 0.01 * UserATK);// *0.01 is aim to make it to be Percent form.
        AlchemyCriticalRate = AlchemyData[1];
        AlchemyCriticalDamage = AlchemyData[2];
        AlchemyTurn = AlchemyData[3];
    }

    //lv.1 method, method to clear all boss Ability quickly.
    private void ClearAllAbility(){
        Thread thread = new Thread(() -> {
            ImportBossAbility();//To prevent from Clear() method causing NullPointerException.
            if(!BossAbility.isEmpty()){
                BossAbility.clear();
            }
        });
        thread.start();
    }

    /**
     * lv.1 method, import Boss's Ability according to <code>BossState</code>'s value.<br/>
     * if <code>BossState</code> smaller than 0, then skip it to prevent NPE.<br/>
     * Notice: for stable reason, can't use multi-thread on this method.
     */
    private void ImportBossAbility(){
        if(BossState > 0){
            BossAbility = getIntent().getStringArrayListExtra("BossAbility");
        }
    }//end of Boss Ability part.
    //end of battle system.


    //Note Folder system.
    String CurrentShowingTitle = "";
    String CurrentUserAnswer = "";
    boolean IsTakeNoteEnabled = false;

    public void ChangeTakeNoteState(View view){
        ImageView ConfirmAnswerButton = findViewById(R.id.ConfirmAnswerButton);
        ImageView ShowHintButton = findViewById(R.id.ShowHintButton);
        EditText QuestTitle = findViewById(R.id.QuestTitle);
        EditText QuestAnswer = findViewById(R.id.QuestAnswer);
        SharedPreferences A = getSharedPreferences("FolderProfile", MODE_PRIVATE);
        //0.close Hint function, because they use same view to display.
        ShowHintButton.setEnabled(false);
        //1.confirm if TakeNote Function is Enabled.
        IsTakeNoteEnabled = !IsTakeNoteEnabled;
        //2.Open or Close TakeNote Function.
        if(IsTakeNoteEnabled){
            //3.close the answering function, prepare for TakeNote function.
            //3.1.close the button, blocked user from answering Quest.
            ConfirmAnswerButton.setEnabled(false);
            //3.2.save the current Quest data in variables.
            CurrentShowingTitle = QuestTitle.getText().toString();
            CurrentUserAnswer = QuestAnswer.getText().toString();
            //3.3.clear EditText to load the Note.
            QuestTitle.setText("");
            QuestTitle.setHint(getString(R.string.TakeNoteHintTran));
            QuestAnswer.setText(A.getString("NoteText","-There is no Note-"));
        }else{
            //4.save Note to SharedPreference.
            SharedPreferences.Editor editor= A.edit();
            //4.1 put the Note Text in Editor.
            editor.putString("NoteText",QuestAnswer.getText().toString());
            editor.apply();
            //4.2 Open the button.
            ConfirmAnswerButton.setEnabled(true);
            //4.3 load the current Quest data from variables.
            QuestTitle.setText(CurrentShowingTitle);
            QuestTitle.setHint(R.string.ReloadTripsTran);
            QuestAnswer.setText(CurrentUserAnswer);
            //4.4 reset the Variables.
            CurrentShowingTitle = "";
            CurrentUserAnswer = "";
            ShowHintButton.setEnabled(true);
        }
    }//end of Note Folder system.


    //Record system.
    //Exp Record is loaded in ExpIO class, you need to create a Instance to call it.
    //Point and Material Record is loaded in ResourceIO class, you need to create a Instance to call it.
    int ComboRecord = 0;

    private void InitializingRecordData(){
        Thread thread = new Thread(() -> {
            ComboRecord = SupportLib.getIntData(this,"RecordDataFile","ComboGotten",0);
            BossDeadTime = SupportLib.getIntData(this,"BattleDataProfile","BossDeadTime",0);
        });
        thread.start();
    }//end of Record system.


    //Game Resource system. Including Point, Material and Combo.
    ResourceIO resourceIO;
    int QuestCombo = 0;

    /**
     * When combo data changed, you can call this method to refresh UI in main method.
     * @param IsQuestRight you can input whether current Quest's Answering are right or Wrong. This method will auto calculate is it need to change combo.
     */
    @SuppressLint("SetTextI18n")
    private void ChangeQuestCombo(boolean IsQuestRight){
        Thread thread = new Thread(() -> {
            //change Quest Combo.
            if(IsQuestRight){
                QuestCombo = QuestCombo + 1;
            }else{
                QuestCombo = 0;
            }
            //save Combo Record.
            if(QuestCombo >= ComboRecord){
                ComboRecord = QuestCombo;
                SupportLib.saveIntData(this,"RecordDataFile","ComboGotten",ComboRecord);
                SupportLib.saveIntData(this,"BattleDataProfile","QuestCombo",QuestCombo);
            }
        });
        thread.start();
        Message message = new Message();
        message.what = PRINT_COMBO_NUMBER;
        handler.sendMessage(message);
    }

    //lv.1 method, sub method of ChangeQuestCombo() and handler, I need this method to dismiss error by illegal forward reference.
    @SuppressLint("SetTextI18n")
    private void PrintComboNumber(){
        TextView QuestComboShowView = findViewById(R.id.QuestComboShowView);
        QuestComboShowView.setText(String.valueOf(QuestCombo));
    }

    private void InitializingResourceData(){
        Thread thread = new Thread(() -> {
            QuestCombo = SupportLib.getIntData(this,"BattleDataProfile","QuestCombo",0);
            resourceIO = new ResourceIO(this);
            Message message = new Message();
            message.what = USER_IMPORT_DONE;
            handler.sendMessage(message);
        });
        thread.start();
    }//end of Game Resource system.


    //Exp system, based on ExpIO Class.
    ExpIO expIO;

    private void InitializingEXPInformation() {//DO NOT put inner method in Thread, because of referring in OnCreate() already has one thread.
        expIO = new ExpIO(this);
    }

    /**
     * lv.1 method, sub method of CheckLevel() and handler.<br/>
     * When some Exp data are changed, you can call this method to refresh UI in main method.
     */
    @SuppressLint("SetTextI18n")
    private void PrintEXPChanges(){
        TextView EXPrateView = findViewById(R.id.EXPrate);
        ProgressBar EXPBarView = findViewById(R.id.EXPBar);
        TextView NowLevel = findViewById(R.id.NowLevel);
        TextView NowHaveEXPView = findViewById(R.id.NowHaveEXP);
        String NextToDisplay;
        NowLevel.setText("Lv.  " + expIO.UserLevel);
        //2.check if user touched LevelLimit, and change the appearance based on this.
        if(expIO.UserLevel >= expIO.LevelLimit){
            NextToDisplay = "--";
        }else{
            NextToDisplay = String.valueOf(expIO.UserUpgradeEXP);
        }
        NowHaveEXPView.setText(expIO.UserHaveEXP + "     /    " + NextToDisplay);
        //3.And show the result to user.
        int Percent = SupportLib.CalculatePercent(expIO.UserHaveEXP, expIO.UserUpgradeEXP);
        EXPrateView.setText(Percent + "%");
        EXPBarView.setProgress(Percent);
    }//the end of EXP system.


    //Assist Function system.
    //store function state (ON or OFF).
    boolean IsAutoKeyboard = false;//default: false.
    boolean IsAutoClearAnswer = false;//default: false.
    boolean IsAutoWrongWindow = true;//default: true.
    //Auto Case Function.
    /**
     * "-1" means function not enable.
     * "0" means auto fix to Lower Case (A to a).
     * "1" means auto fix to Upper Case (a to A).
     */
    int AutoCaseState = -1;

    //main method.
    private void InitializingAssistFunction(){
        Thread thread = new Thread(() -> {
            boolean[] AssistData = SupportLib.getMultiBoolean(
                    this,
                    "TimerSettingProfile",
                    new String[]{"AutoKeyboard","AutoClearAnswer","StopDamageDialog","AutoWrongWindow"},
                    new boolean[]{false,false,true,true}
            );
            IsAutoKeyboard = AssistData[0];
            IsAutoClearAnswer = AssistData[1];
            AutoCaseState = SupportLib.getIntData(this,"TimerSettingProfile","AutoCase",-1);
            //3.load the BossDeadTime from record.
            IsShowDamage = AssistData[2];
            IsAutoWrongWindow = AssistData[3];
        });
        thread.start();
    }

    //Auto keyboard function.
    /**
     * to store keyboard state.
     */
    boolean IsKeyboardOpened = false;

    /**
     * when MainActivity is initialized, the keyboard is hidden(false).<br/>
     * if AutoKeyboard function is on, when user try to reload the Quest, the keyboard will appear.(turn the state to true)<br/>
     * using <code>IsKeyboardOpened</code> variable to detect if keyboard is opened, and be the base of AutoKeyboard function.
     * @param ShareObject Using created EditText object to save creating cost. This is NonNull param!
     */
    private void AutoKeyboardControl(@NonNull EditText ShareObject){
        //help user focus on typing. Auto pop the virtual keyboard.Thanks to:
        // https://www.jianshu.com/p/6f09de9e903b !
        // https://blog.csdn.net/fenglolo/article/details/108893330 !
        if(IsAutoKeyboard){
            ShareObject.requestFocus();
            InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm && !IsKeyboardOpened) {
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                IsKeyboardOpened = !IsKeyboardOpened;
            }
        }
    }//end of Auto keyboard function.

    //Auto Clear Answer Function.
    /**
     * If this function is ON (depending <code>IsAutoClearAnswer</code> state), then after user typed in Answering EditText. User's input will be auto cleared.
     * @param ShareObject Using created EditText object to save creating cost. This is NonNull param!
     */
    private void AutoClearAnswerControl(@NonNull EditText ShareObject){
        if(IsAutoClearAnswer){
            ShareObject.setText("");
        }
    }//end of Auto Clear Answer Function.
    // end of Assist Function system.


    //Chronometer function, sub part of Quest system.
    private void StopChronometer(){
        Chronometer QuestTimer = findViewById(R.id.FavoriteTimerView);
        QuestStartTime = 0;
        QuestTimer.stop();
    }

    private void StartChronometer(){
        Chronometer QuestTimer = findViewById(R.id.FavoriteTimerView);
        QuestTimer.setBase(SystemClock.elapsedRealtime());
        QuestTimer.start();
    }//end of Chronometer function.


    //Quest number function, sub part of Quest system.
    //thanks to: https://stackoverflow.com/questions/23293572/android-cannot-perform-this-operation-because-the-connection-pool-has-been-clos !
    /**
     * Search the exact number of Quests method. Also being a sub method of onCreate().
     */
    private void ChangeQuestTotalNumber(){
        //1.provide support for get id total number in database, sub method of onCreate.
        QuestTotalNumber = SupportLib.getIntData(this,"IdNumberStoreProfile","IdNumber",0);
        //2.show hint to user to set some Quest when no Quest available.
        if (QuestTotalNumber <= 0){
            EditText QuestTitleView =findViewById(R.id.QuestTitle);
            ImageView FavoriteQuestButton = findViewById(R.id.FavoriteQuestButton);
            FavoriteQuestButton.setEnabled(false);
            QuestTitleView.setHint(R.string.NoQuestAvailableHintTran);
        }
    }//end of Quest number function.


    //Quest Favorite function, sub part of Quest system.
    int FavoriteNumber;
    boolean IsFavoriteLoaded = false;

    /**
     * Initializing Favorite number to help App to judge if user can access Favorite Activity.
     */
    private void InitializingFavorite(){
        Thread thread = new Thread(() -> {
            FavoriteNumber = SupportLib.getIntData(this,"FavoriteFile","FavoriteNumber",0);
            IsFavoriteLoaded = true;
        });
        thread.start();
    }

    public void AddFavorite(View view){
        if(!QuestTitleContent.equals("") && IsFavoriteLoaded){
            //1.initializing.
            ImageView FavoriteQuestButton = findViewById(R.id.FavoriteQuestButton);
            FavoriteDbHelper FavoriteDbHelper = new FavoriteDbHelper(this);
            SQLiteDatabase FavoriteDb = FavoriteDbHelper.getWritableDatabase();
            //2.Show operation Dialog to user.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.HintWordTran));
            dialog.setMessage(getString(R.string.AddFavoriteHintTran) + "\n" +
                    getString(R.string.QuestShowTitleTran) + "\n" + QuestTitleContent
            );
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    getString(R.string.ConfirmWordTran),
                    (dialog12, id) -> {//3.if user choose to add Favorite.
                        //3.1 store Quest to Favorite database.
                        ContentValues values = new ContentValues();
                        values.put(com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteTitle,QuestTitleContent);
                        values.put(com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteAnswer,QuestSystemAnswer);
                        values.put(com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteType,QuestType);
                        values.put(com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.COLUMN_NAME_FavoriteHint,"Nothing");
                        FavoriteDb.insert(com.example.android.tomultiqa.FavoriteDbHelper.DataBaseEntry.TABLE_NAME, null, values);
                        FavoriteNumber = FavoriteNumber + 1;
                        SupportLib.saveIntData(this,"FavoriteFile","FavoriteNumber",FavoriteNumber);
                        //3.2 close dialog.
                        dialog12.cancel();
                        //3.3 show finished hint to user.
                        Toast.makeText(this,getString(R.string.AddFavoriteCompletedTran),Toast.LENGTH_SHORT).show();
                        //3.4 lock the button to prevent from repeated adding.(it will be unlocked in ReloadQuest() method).
                        FavoriteQuestButton.setEnabled(false);
                    });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    (dialog1, id) -> dialog1.cancel());
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }else{
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    getString(R.string.NoQuestAddFavoriteTran),
                    getString(R.string.ConfirmWordTran));
        }
    }//end of Quest Favorite function.


    //FloatingButton Menu function.
    //thanks to: https://developer.android.google.cn/guide/topics/ui/dialogs !
    public void ShowButtonMenu(View view){
        //1.Mode List preparation.
        CharSequence[] ModeItemList = {ValueLib.NORMAL_MODE, ValueLib.FOCUS_MODE, ValueLib.GAME_MODE};
        int SelectedItemId;
        if(AppMode.equals(ValueLib.NORMAL_MODE)){
            SelectedItemId = 0;
        }else if(AppMode.equals(ValueLib.FOCUS_MODE)){
            SelectedItemId = 1;
        }else{
            SelectedItemId = 2;
        }
        //2.set single choice dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.AppModeWordTran));
        dialog.setSingleChoiceItems(ModeItemList, SelectedItemId, (dialog12, which) -> {
            switch (which)
            {
                case -1://but in nearly all situations, this case actually not existed.
                    SupportLib.CreateNoticeDialog(MainActivity.this,
                            getString(R.string.ErrorWordTran),
                            "NO Mode Selected, please Select a Mode or Cancel the dialog to keep the original option.",
                            getString(R.string.ConfirmWordTran));
                    break;
                case 0:
                    AppMode = ValueLib.NORMAL_MODE;
                    break;
                case 1:
                    AppMode = ValueLib.FOCUS_MODE;
                    break;
                case 2:
                    AppMode = ValueLib.GAME_MODE;
                    break;
            }
            //3.save AppMode changed.
            SupportLib.saveStringData(MainActivity.this,"ChooseAppModeProfile","AppMode",AppMode);
            //4.reload the App data about Mode.
            ModeChangeAppearance(true);
        });
        dialog.setCancelable(true);
        dialog.setNegativeButton(getString(R.string.HelpWordTran), (dialogInterface, i) -> SupportLib.CreateNoticeDialog(
                this,
                getString(R.string.HelpWordTran),
                getString(R.string.AppModeHelpTran),
                getString(R.string.ConfirmWordTran))
        );
        dialog.setPositiveButton(
                getString(R.string.CloseWordTran),
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of FloatingButton Menu function.


    //Mode and Appearance system.
    private void InitializingAppMode(){
        AppMode = SupportLib.getStringData(this,"ChooseAppModeProfile","AppMode", ValueLib.NORMAL_MODE);
    }

    /**
     * Because of not all UI parts or Functions are available in any <code>AppMode</code>.<br/>
     * So this method can quickly change App appearance according <code>AppMode</code>variable.
     * Also being a sub method of onCreate() and ShowButtonMenu() method.
     * @param IsInsideCall IsInsideCall means if the method is called in first launching Activity (true) or not (false).
     */
    private void ModeChangeAppearance(boolean IsInsideCall){
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        ConstraintLayout QuestValueLayout = findViewById(R.id.QuestValueLayout);
        ConstraintLayout EXPLayout = findViewById(R.id.EXPLayout);
        switch (AppMode) {
            case ValueLib.NORMAL_MODE:
                BattleInfLayout.setVisibility(View.GONE);
                QuestValueLayout.setVisibility(View.VISIBLE);
                EXPLayout.setVisibility(View.VISIBLE);
                break;
            case ValueLib.GAME_MODE:
                QuestValueLayout.setVisibility(View.VISIBLE);
                EXPLayout.setVisibility(View.VISIBLE);
                if(BossState > 0 && !IsInsideCall){//BattleInfLayout will be ON or OFF depending on BossState.
                    OpenBattleLayout();
                }
                //BossShieldLayout`s visibility will be set in SetBossValue() method.
                break;
            case ValueLib.FOCUS_MODE:
                BattleInfLayout.setVisibility(View.GONE);
                QuestValueLayout.setVisibility(View.GONE);
                EXPLayout.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * sub method of ModeChangeAppearance() and ReloadQuest() method. if the battle happened(started), then call this method can easily make battle UI visible.
     */
    private void OpenBattleLayout(){
        ConstraintLayout BattleInfLayout = findViewById(R.id.BattleInfLayout);
        ConstraintLayout EXPLayout = findViewById(R.id.EXPLayout);
        BattleInfLayout.setVisibility(View.VISIBLE);
        EXPLayout.setVisibility(View.GONE);
    }//end of Mode and Appearance system.


    //support method collection.
    //Square button`s function--jump to SquareActivity.
    public void GoToSquare(View view){
        //if in [Focus] mode, then user press [Square] button will be sent to Chronometer.
        Intent i;
        if(!AppMode.equals(ValueLib.FOCUS_MODE)){
            i = new Intent(MainActivity.this, SquareActivity.class);
            i.putExtra("AppModeImport",100);//I don`t know why I put this value here, but for stable purpose, I suggest do not remove it.
        }else{
            i = new Intent(MainActivity.this, TimerActivity.class);
        }
        startActivity(i);
    }

    //Adventure button`s function--jump to AdventureActivity.
    public void GoToAdventure(View view){
        if(AppMode.equals(ValueLib.GAME_MODE)){
            Intent i = new Intent(MainActivity.this, AdventureActivity.class);
            startActivity(i);
        }else{
            SupportLib.CreateNoticeDialog(this,getString(R.string.HintWordTran),getString(R.string.NotInGameModeTran),getString(R.string.ConfirmWordTran));
        }
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
    }//end of support method collection.
}