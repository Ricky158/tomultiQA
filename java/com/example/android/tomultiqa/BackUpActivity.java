package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class BackUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //......
    }

    //file manage system.
    //thanks to: https://blog.csdn.net/qq_16068303/article/details/106758659 !
    // https://blog.csdn.net/dandelionela/article/details/96482372 !
    // https://blog.csdn.net/qq_32611951/article/details/78048309 !
    // https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java?noredirect=1&lq=1 !
    // https://blog.csdn.net/ruiruiddd/article/details/106543067 !
    // https://blog.csdn.net/chyychfchx/article/details/59484332 !
    // https://blog.csdn.net/baidu_17508977/article/details/51007904 !
    // https://www.jianshu.com/p/24d047ef641a !
    // https://www.cnblogs.com/yizijianxin/p/12000274.html !

    //each element in List is a line in .txt file.
    ArrayList<String> TextElement = new ArrayList<>();
    //the texts which need to wrote to file.
    String ContentNeedToAdd = "";

    //File Support Part.
    //newer version, base on android Uri system, thanks to: https://developer.android.google.cn/training/data-storage/shared/documents-files#open !
    // https://www.jianshu.com/p/75eccd29c229 !
    Uri BackUpFileUri = null;
    //Request code for creating a .txt document.
    static final int CREATE_FILE = 1;
    //Request code for selecting backup document.
    static final int PICK_FILE = 2;

    //overall backup output process:
    //StartInput() - OpenPicker() - OnActivityResult() - handler() - GetLinesFromUri() - completed.
    //overall backup input process:
    //StartOutput() - NewFile() - OnActivityResult() - handler() - ReWriteFile() - completed.
    //overall backup preview process:
    //StartPreview() - Same as output process, but not actually use or store these data to SharePreferences.

    //if call StartPreview(), it will set IsPreview to true, and execute OpenPicker() to continue process.
    //if false, the data will be actually using in recover data.
    boolean IsPreview = false;//default is false.

    //lv.3 method, main method of handle callback from OnActivityResult() method, and complete process.
    static final int GET_URI_SUCCESS = 100;
    static final int FILE_CREATE_SUCCESS = 101;

    private final Handler handler = new Handler(msg -> {
        switch (msg.what){
            case GET_URI_SUCCESS:
                //1.get data from file.
                GetLinesFromUri();
                //1.1 compatible with Old version files (before 0.31.0), to prevent NPE.
                if(TextElement.size() == 30){//in version 0.31.0, the file have 30 lines(0 ~ 30）, so we need to add additional 4 lines' default value manually.
                    TextElement.add(30,"0");
                    TextElement.add(31, String.valueOf(SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1)));
                    TextElement.add(32,"1000");
                    TextElement.add(33,"10");
                }
                //2.write data to SharePreferences.
                if(!IsPreview){
                    SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserLevel",getPureInt(TextElement.get(3)));
                    SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",getPureInt(TextElement.get(4)));
                    SupportClass.saveIntData(this,"RecordDataFile","RightAnswering",getPureInt(TextElement.get(5)));
                    SupportClass.saveIntData(this,"RecordDataFile","WrongAnswering",getPureInt(TextElement.get(6)));
                    SupportClass.saveIntData(this,"TimerSettingProfile","TimerProgress",getPureInt(TextElement.get(7)));
                    SupportClass.saveIntData(this,"TimerSettingProfile","TimerTargetDay",getPureInt(TextElement.get(8)));
                    SupportClass.saveIntData(this,"BattleDataProfile","ATKTalentLevel",getPureInt(TextElement.get(9)));
                    SupportClass.saveIntData(this,"BattleDataProfile","CRTalentLevel",getPureInt(TextElement.get(10)));
                    SupportClass.saveIntData(this,"BattleDataProfile","QuestCombo",getPureInt(TextElement.get(12)));
                    SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",getPureInt(TextElement.get(13)));
                    SupportClass.saveIntData(this,"BattleDataProfile","UserMaterial",getPureInt(TextElement.get(14)));
                    SupportClass.saveIntData(this,"BattleDataProfile","BossDeadTime",getPureInt(TextElement.get(15)));
                    SupportClass.saveIntData(this,"RecordDataFile","EXPGotten",getPureInt(TextElement.get(16)));
                    SupportClass.saveLongData(this,"RecordDataFile","PointGotten",getPureLong(TextElement.get(17)));
                    SupportClass.saveIntData(this,"RecordDataFile","MaterialGotten",getPureInt(TextElement.get(18)));
                    SupportClass.saveIntData(this,"BattleDataProfile","CDTalentLevel",getPureInt(TextElement.get(11)));
                    SupportClass.saveIntData(this,"RecordDataFile","ComboGotten",getPureInt(TextElement.get(19)));
                    SupportClass.saveIntData(this,"BattleDataProfile","UserConflictFloor",getPureInt(TextElement.get(20)));
                    SupportClass.saveLongData(this,"TourneyDataFile","MaxPtRecord",getPureLong(TextElement.get(21)));
                    SupportClass.saveIntData(this,"RecordDataFile","MaxDailyDifficulty",getPureInt(TextElement.get(22)));
                    SupportClass.saveIntData(this,"AlchemyMasteryFile","MasteryLevel",getPureInt(TextElement.get(23)));
                    SupportClass.saveIntData(this,"AlchemyMasteryFile","MasteryEXP",getPureInt(TextElement.get(24)));
                    SupportClass.saveIntData(this,"ExcessDataFile","LevelLimit",getPureInt(TextElement.get(25)));
                    SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessRank",getPureInt(TextElement.get(26)));
                    SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessATK",getPureInt(TextElement.get(27)));
                    SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCR",getPureInt(TextElement.get(28)));
                    SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCD",getPureInt(TextElement.get(29)));
                    SupportClass.saveIntData(this,"RecordDataFile","BattleFail",getPureInt(TextElement.get(30)));
                    //import Boss Level data belongs to Tourney correction.
                    int LevelNeedWrite = getPureInt(TextElement.get(31));
                    int UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
                    if(LevelNeedWrite < 1 || LevelNeedWrite < UserLevel - 33 || LevelNeedWrite > UserLevel + 33){
                        LevelNeedWrite = UserLevel;
                    }
                    SupportClass.saveIntData(this,"TourneyDataFile","BossLevel",LevelNeedWrite);
                    //import Boss HP data belongs to Tourney correction.
                    int HPNeedWrite = getPureInt(TextElement.get(32));
                    if(HPNeedWrite < 1){
                        HPNeedWrite = 1;
                    }
                    SupportClass.saveIntData(this,"TourneyDataFile","BossHP",HPNeedWrite);
                    //import Boss Turn data belongs to Tourney correction.
                    int TurnNeedWrite = getPureInt(TextElement.get(33));
                    if(TurnNeedWrite < 1 || TurnNeedWrite > 1000){
                        TurnNeedWrite = 1;
                    }
                    SupportClass.saveIntData(this,"TourneyDataFile","BossTurn",TurnNeedWrite);
                }
                //3.after used in BackUp process, report result to user.
                SupportClass.CreateNoticeDialog(this,
                        getString(R.string.FileContentWordTran),
                        getString(R.string.ProgressCompletedTran) + "\n" +
                                getString(R.string.LevelWordTran) + TextElement.get(3) + "\n" +
                                getString(R.string.EXPWordTran) + TextElement.get(4) + "\n" +
                                getString(R.string.AnsweringRightTimeTran) + TextElement.get(5) + "\n" +
                                getString(R.string.AnsweringWrongTimeTran) + TextElement.get(6) + "\n" +
                                getString(R.string.TimerProgressWordTran) + TextElement.get(7) + "\n" +
                                getString(R.string.TimerGoalWordTran) + TextElement.get(8) + "\n" +
                                getString(R.string.ATKTalentLevelTran) + TextElement.get(9) + "\n" +
                                getString(R.string.CRTalentLevelTran) + TextElement.get(10) + "\n" +
                                getString(R.string.CDTalentLevelTran) + TextElement.get(11) + "\n" +
                                getString(R.string.CurrentQuestComboTran) + TextElement.get(12) + "\n" +
                                getString(R.string.PointWordTran) + TextElement.get(13) + "\n" +
                                getString(R.string.MaterialWordTran) + TextElement.get(14) + "\n" +
                                getString(R.string.BossDeadTimeTran) + TextElement.get(15) + "\n" +
                                getString(R.string.EXPRecordTran) + TextElement.get(16) + "\n" +
                                getString(R.string.PointRecordTran) + TextElement.get(17) + "\n" +
                                getString(R.string.MaterialRecordTran) + TextElement.get(18) + "\n" +
                                getString(R.string.MaxComboTran) + TextElement.get(19) + "\n" +
                                getString(R.string.ConflictClearTran) + TextElement.get(20) + "\n" +
                                getString(R.string.TourneyPtWordTran) + TextElement.get(21) + "\n" +
                                getString(R.string.DailyClearTran) + TextElement.get(22) + "\n" +
                                getString(R.string.MasteryWordTran) + TextElement.get(23) + "\n" +
                                getString(R.string.MasteryEXPTran) + TextElement.get(24) + "\n" +
                                getString(R.string.LevelLimitTran) + TextElement.get(25) + "\n" +
                                getString(R.string.LevelExcessTran) + TextElement.get(26) + "\n" +
                                getString(R.string.LevelExcessATKTran) + TextElement.get(27) + "\n" +
                                getString(R.string.LevelExcessCRTran) + TextElement.get(28) + "\n" +
                                getString(R.string.LevelExcessCDTran) + TextElement.get(29) + "\n" +
                                getString(R.string.BossFailTran) + TextElement.get(30) + "\n" +
                                getString(R.string.BossLevelWordTran) + TextElement.get(31) + "\n" +
                                getString(R.string.BossHPWordTran) + TextElement.get(32) + "\n" +
                                getString(R.string.BattleTurnWordTran) + TextElement.get(33),
                        getString(R.string.ConfirmWordTran)
                );
                //4.in the end,
                TextElement = new ArrayList<>();//reset.
                break;
            case FILE_CREATE_SUCCESS:
                //1.1 load data from SharePreferences.
                //1.2 the content in file.Each AddContent() is a line in .txt file.
                //User.
                int UserLevel = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
                int UserHaveEXP = SupportClass.getIntData(this,"EXPInformationStoreProfile","UserHaveEXP",0);
                //Answering.
                int UserRightTimes = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
                int UserWrongTimes = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);
                //Sol.Timer
                int DayProgress = SupportClass.getIntData(this,"TimerSettingProfile","TimerProgress",0);
                int DayGoal = SupportClass.getIntData(this,"TimerSettingProfile","TimerTargetDay",0);
                //Talent.
                int ATKTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","ATKTalentLevel",0);
                int CRTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CRTalentLevel",0);
                int CDTalentLevel = SupportClass.getIntData(this,"BattleDataProfile","CDTalentLevel",0);
                //Resource.
                int QuestCombo = SupportClass.getIntData(this,"BattleDataProfile","QuestCombo",0);
                int UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
                int UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
                //Record part1.
                int BossDeadTime = SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0);
                int EXPRecord = SupportClass.getIntData(this,"RecordDataFile","EXPGotten",0);
                long PointRecord = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
                int MaterialRecord = SupportClass.getIntData(this,"RecordDataFile","MaterialGotten",0);
                int ComboRecord = SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0);
                //Conflict.
                int UserConflictFloor = SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0);
                //Tourney part1.
                long TourneyPt = SupportClass.getLongData(this,"TourneyDataFile","MaxPtRecord",0);
                //Daily.
                int DailyDifficulty = SupportClass.getIntData(this,"RecordDataFile","MaxDailyDifficulty",0);
                //Alchemy.
                int MasteryLevel = SupportClass.getIntData(this,"AlchemyMasteryFile","MasteryLevel",0);
                int MasteryEXP = SupportClass.getIntData(this,"AlchemyMasteryFile","MasteryEXP",0);
                //LimitUp.
                int LevelLimit = SupportClass.getIntData(this,"ExcessDataFile","LevelLimit",50);
                int LevelExcessRank = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessRank",0);
                int LevelExcessATK= SupportClass.getIntData(this,"ExcessDataFile","LevelExcessATK",0);
                int LevelExcessCR = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCR",0);
                int LevelExcessCD = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCD",0);
                //Record part2.
                int BossFail = SupportClass.getIntData(this,"RecordDataFile","BattleFail",0);
                //Tourney part2.
                int BossLevel = SupportClass.getIntData(this,"TourneyDataFile","BossLevel",UserLevel);
                int BossHP = SupportClass.getIntData(this,"TourneyDataFile","BossHP",1000);
                int BossTurn = SupportClass.getIntData(this,"TourneyDataFile","BossTurn",10);
                //2.1 these lines and part 2.3 are the declaration which will be wrote in file.
                AddContent("tomultiQA BackUp");//line1: notification and app version code.
                AddContent(String.valueOf(ValueClass.APP_VERSION));//line2: the App version which file be made.
                //2.2 if Android Version is higher than Android N (7.0+), using api to get system time.and write time to file.
                String CurrentDate;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat d_f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    d_f.setTimeZone(TimeZone.getTimeZone("GMT+08"));  //设置时区，+08是北京时间
                    CurrentDate = d_f.format(new Date()) + " (GMT+8)";
                }else{
                    Time time = new Time();
                    time.setToNow();
                    CurrentDate = time.format("%Y-%m-%d %H:%M:%S");
                }
                AddContent("Date: " + CurrentDate);//line3: file date.
                //2.3 write actual content to backup.(line4 and above)
                AddContent(String.valueOf(UserLevel));
                AddContent(String.valueOf(UserHaveEXP));
                AddContent(String.valueOf(UserRightTimes));
                AddContent(String.valueOf(UserWrongTimes));
                AddContent(String.valueOf(DayProgress));
                AddContent(String.valueOf(DayGoal));
                AddContent(String.valueOf(ATKTalentLevel));
                AddContent(String.valueOf(CRTalentLevel));
                AddContent(String.valueOf(CDTalentLevel));
                AddContent(String.valueOf(QuestCombo));
                AddContent(String.valueOf(UserPoint));
                AddContent(String.valueOf(UserMaterial));
                AddContent(String.valueOf(BossDeadTime));
                AddContent(String.valueOf(EXPRecord));
                AddContent(String.valueOf(PointRecord));
                AddContent(String.valueOf(MaterialRecord));
                AddContent(String.valueOf(ComboRecord));
                AddContent(String.valueOf(UserConflictFloor));
                AddContent(String.valueOf(TourneyPt));
                AddContent(String.valueOf(DailyDifficulty));
                AddContent(String.valueOf(MasteryLevel));
                AddContent(String.valueOf(MasteryEXP));
                AddContent(String.valueOf(LevelLimit));
                AddContent(String.valueOf(LevelExcessRank));
                AddContent(String.valueOf(LevelExcessATK));
                AddContent(String.valueOf(LevelExcessCR));
                AddContent(String.valueOf(LevelExcessCD));
                AddContent(String.valueOf(BossFail));
                AddContent(String.valueOf(BossLevel));
                AddContent(String.valueOf(BossHP));
                AddContent(String.valueOf(BossTurn));
                //3. write all texts to file.
                ReWriteFile();
                break;
            default:
                break;
        }
        return false;//this is default adding, I just leave it here.
    });

    //lv.3 method, sub method of handler(), after got Uri from picker, read the text from file which has specific Uri.
    private void GetLinesFromUri(){
        if(BackUpFileUri != null){//NPE prevent.
            try (InputStream inputStream = getContentResolver().openInputStream(BackUpFileUri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    TextElement.add(line+"\n");
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                finish();
            }
        }else{
            Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
        }
    }

    //lv.3 method, sub method of handler(), Re-Write the texts of file.
    //thanks to: https://blog.csdn.net/daniel80110_1020/article/details/55260510 to fix android official example problem.
    private void ReWriteFile() {
        if(BackUpFileUri != null){
            try {
                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(BackUpFileUri, "w");
                FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                fileOutputStream.write(ContentNeedToAdd.getBytes());
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close();
                pfd.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
        }
    }

    //lv.2 method, main method of handle callback the result of system file picker.
    //when system file picker have get result, then record it to a variable.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Message message = new Message();
        if(resultCode == Activity.RESULT_OK){
            // The result data contains a URI for the document or directory that
            // the user selected.
            if (resultData != null) {
                BackUpFileUri = resultData.getData();
                // Perform operations on the document using its URI.
                //use handler to callback functions.
            }
            switch (requestCode){
                case PICK_FILE:
                    message.what = GET_URI_SUCCESS;
                    break;
                case CREATE_FILE:
                    message.what = FILE_CREATE_SUCCESS;
                    break;
                default:
                    break;
            }
            handler.sendMessage(message);
        }
    }

    //lv.1 main method for button Output data OnClick().
    public void StartOutput(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.DataOutputHintTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.StartWordTran),
                (dialog1, id) -> {
                    NewFile();
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, id) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.1 main method for button Input data OnClick().
    public void StartInput(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.DataInputHintTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.StartWordTran),
                (dialog1, id) -> {
                    OpenPicker();
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
                AlertDialog DialogView = dialog.create();
                DialogView.show();
    }

    //lv.1 main method for button Preview file OnClick().
    public void StartPreview(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.DataPreviewHintTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.StartWordTran),
                (dialog1, id) -> {
                    IsPreview = true;
                    OpenPicker();
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.1 sub method, call system file picker to select path to store backup file.
    private void NewFile(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");//represent .txt file, thanks to: https://blog.csdn.net/dodod2012/article/details/88868930 !
        intent.putExtra(Intent.EXTRA_TITLE, "tomultiQA.txt");
        startActivityForResult(intent, CREATE_FILE);
    }

    //lv.1 sub method, call system file picker to help user to pick the backup file.
    private void OpenPicker(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");//represent .txt file, thanks to: https://blog.csdn.net/dodod2012/article/details/88868930 !
        startActivityForResult(intent, PICK_FILE);
    }

    //lv.1 method, sub method of StartExportData() method. Add one line in Content which need to wrote in .txt file.
    private void AddContent(String Text){
        ContentNeedToAdd = ContentNeedToAdd + Text + "\n";
    }//end of newer version of backup support code.

    //lv.1 method, sub method of StartImportData() method.
    private static int getPureInt(String ElementInList){
        //String.replaceAll("\\s+","") removes all whitespaces and non-visible characters (e.g., tab, \n),;
        //if String in List have not clear all WHITESPACE and \n character, the reading code will have Error.
        //So we design this code to do Clear work quickly.
        return Integer.parseInt(ElementInList.replaceAll("\\s+",""));
    }

    //lv.1 method, sub method of StartImportData() method.
    private static long getPureLong(String ElementInList){
        return Long.parseLong(ElementInList.replaceAll("\\s+",""));
    }//end of File Support Part.
    //end of File manage system.


    //Help function.
    public void ShowBackUpHelp(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.ShopHelpTitleTran),
                ValueClass.BACKUP_HELP,
                getString(R.string.ConfirmWordTran)
             );
    }//end of Help function.
}