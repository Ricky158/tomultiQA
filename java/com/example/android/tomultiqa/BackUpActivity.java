package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;
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
    ArrayList<String> LinesInFile = new ArrayList<>();
    //the texts which need to wrote to file.
    StringBuilder ContentNeedToAdd = new StringBuilder();

    //newer version, base on android Uri system, thanks to: https://developer.android.google.cn/training/data-storage/shared/documents-files#open !
    // https://www.jianshu.com/p/75eccd29c229 !
    Uri BackUpFileUri = null;
    //Request code for creating a .txt document.
    static final int CREATE_FILE = 1;
    //Request code for selecting backup document.
    static final int PICK_FILE = 2;
    //Request code for refresh the content of process report UI.
    static final int PROCESS_SHOW = 3;

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
    static final int FILE_IMPORT_DONE = 100;
    static final int FILE_EXPORT_DONE = 101;

    //SharePreference and Total IO.
    //thanks to: https://www.yisu.com/zixun/130629.html !
    @SuppressLint("SetTextI18n")
    private final Handler handler = new Handler(msg -> {
        switch (msg.what){
            case FILE_IMPORT_DONE:
                String[] ReportSet;
                if(msg.obj instanceof String[]){//if this object is a String[] instance.
                    ReportSet = (String[]) msg.obj;
                }else{
                    ReportSet = new String[]{"Error","Error","Error"};
                }
                StringBuilder AllReport = new StringBuilder();
                for(String Each: ReportSet){
                    AllReport.append(Each).append("\n\n");
                }
                SupportLib.CreateNoticeDialog(this,
                        getString(R.string.FileContentWordTran),
                        getString(R.string.ProgressCompletedTran) + "\n" +
                                getString(R.string.LevelWordTran) + LinesInFile.get(3) + "\n" +
                                getString(R.string.EXPWordTran) + LinesInFile.get(4) + "\n" +
                                getString(R.string.AnsweringRightTimeTran) + LinesInFile.get(5) + "\n" +
                                getString(R.string.AnsweringWrongTimeTran) + LinesInFile.get(6) + "\n" +
                                getString(R.string.TimerProgressWordTran) + LinesInFile.get(7) + "\n" +
                                getString(R.string.TimerGoalWordTran) + LinesInFile.get(8) + "\n" +
                                getString(R.string.ATKTalentLevelTran) + LinesInFile.get(9) + "\n" +
                                getString(R.string.CRTalentLevelTran) + LinesInFile.get(10) + "\n" +
                                getString(R.string.CDTalentLevelTran) + LinesInFile.get(11) + "\n" +
                                getString(R.string.CurrentQuestComboTran) + LinesInFile.get(12) + "\n" +
                                getString(R.string.PointWordTran) + LinesInFile.get(13) + "\n" +
                                getString(R.string.MaterialWordTran) + LinesInFile.get(14) + "\n" +
                                getString(R.string.BossDeadTimeTran) + LinesInFile.get(15) + "\n" +
                                getString(R.string.EXPRecordTran) + LinesInFile.get(16) + "\n" +
                                getString(R.string.PointRecordTran) + LinesInFile.get(17) + "\n" +
                                getString(R.string.MaterialRecordTran) + LinesInFile.get(18) + "\n" +
                                getString(R.string.MaxComboTran) + LinesInFile.get(19) + "\n" +
                                getString(R.string.ConflictClearTran) + LinesInFile.get(20) + "\n" +
                                getString(R.string.TourneyPtWordTran) + LinesInFile.get(21) + "\n" +
                                getString(R.string.DailyClearTran) + LinesInFile.get(22) + "\n" +
                                getString(R.string.MasteryWordTran) + LinesInFile.get(23) + "\n" +
                                getString(R.string.MasteryEXPTran) + LinesInFile.get(24) + "\n" +
                                getString(R.string.LevelLimitTran) + LinesInFile.get(25) + "\n" +
                                getString(R.string.LevelExcessTran) + LinesInFile.get(26) + "\n" +
                                getString(R.string.LevelExcessATKTran) + LinesInFile.get(27) + "\n" +
                                getString(R.string.LevelExcessCRTran) + LinesInFile.get(28) + "\n" +
                                getString(R.string.LevelExcessCDTran) + LinesInFile.get(29) + "\n" +
                                getString(R.string.BossFailTran) + LinesInFile.get(30) + "\n" +
                                getString(R.string.BossLevelWordTran) + LinesInFile.get(31) + "\n" +
                                getString(R.string.BossHPWordTran) + LinesInFile.get(32) + "\n" +
                                getString(R.string.BattleTurnWordTran) + LinesInFile.get(33) + "\n" +
                                AllReport,
                        getString(R.string.ConfirmWordTran)
                );
                //4.in the end,
                LinesInFile = new ArrayList<>();//reset.
                break;
            case FILE_EXPORT_DONE:
                SupportLib.CreateNoticeDialog(
                        this,
                        getString(R.string.NoticeWordTran),
                        getString(R.string.ExportAppDataTran) + " " + getString(R.string.CompletedWordTran),
                        getString(R.string.ConfirmWordTran)
                );
                break;
            case PROCESS_SHOW:
                TextView BackupReportView = findViewById(R.id.BackupReportView);
                if(msg.obj instanceof String){
                    BackupReportView.setText( (String) msg.obj);
                }else{
                    BackupReportView.setText("Error");
                }
                break;
            default:
                break;
        }
        return false;//this is default adding, I just leave it here.
    });

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
                case PICK_FILE:// TODO: 2021/9/12 Input data to file method.
                    //After get all lines in file, then write them into App's database or SharedPreference.
                    Thread ImportThread = new Thread(() -> {
                        //1.get data from file.
                        ShowProcessReport(getString(R.string.StartWordTran));
                        GetLinesFromUri();
                        ShowProcessReport(getString(R.string.FinishFileLoadTran));
                        //1.1 compatible with Old version files (before 0.31.0), to prevent NPE.
                        if(LinesInFile.size() == 30){//in version 0.31.0, the file have 30 lines(0 ~ 30）, so we need to add additional 4 lines' default value manually.
                            LinesInFile.add(30,"0");
                            LinesInFile.add(31, String.valueOf(SupportLib.getIntData(this,"EXPInformationStoreProfile","UserLevel",1)));
                            LinesInFile.add(32,"1000");
                            LinesInFile.add(33,"10");
                        }
                        //2.write data to SharePreferences.
                        if(!IsPreview){
                            SupportLib.saveMultiInt(
                                    this,
                                    "EXPInformationStoreProfile",
                                    new String[]{"UserLevel","UserHaveEXP"},
                                    new int[]{getPureInt(LinesInFile.get(3)),getPureInt(LinesInFile.get(4))}
                                    );
                            SupportLib.saveMultiInt(
                                    this,
                                    "RecordDataFile",
                                    new String[]{"RightAnswering","WrongAnswering","EXPGotten","MaterialGotten","ComboGotten","MaxDailyDifficulty","BattleFail"},
                                    new int[]{getPureInt(LinesInFile.get(5)),getPureInt(LinesInFile.get(6)),getPureInt(LinesInFile.get(16)),getPureInt(LinesInFile.get(18)),getPureInt(LinesInFile.get(19)),getPureInt(LinesInFile.get(22)),getPureInt(LinesInFile.get(30))}
                                    );
                            SupportLib.saveLongData(this,"RecordDataFile","PointGotten",getPureLong(LinesInFile.get(17)));
                            SupportLib.saveMultiInt(
                                    this,
                                    "TimerSettingProfile",
                                    new String[]{"TimerProgress","TimerTargetDay"},
                                    new int[]{getPureInt(LinesInFile.get(7)),getPureInt(LinesInFile.get(8))}
                            );
                            SupportLib.saveMultiInt(
                                    this,
                                    "BattleDataProfile",
                                    new String[]{"ATKTalentLevel","CRTalentLevel","CDTalentLevel","QuestCombo","UserPoint","UserMaterial","BossDeadTime","UserConflictFloor"},
                                    new int[]{getPureInt(LinesInFile.get(9)),getPureInt(LinesInFile.get(10)),getPureInt(LinesInFile.get(11)),getPureInt(LinesInFile.get(12)),getPureInt(LinesInFile.get(13)),getPureInt(LinesInFile.get(14)),getPureInt(LinesInFile.get(15)),getPureInt(LinesInFile.get(20))}
                                    );
                            SupportLib.saveMultiInt(
                                    this,
                                    "AlchemyMasteryFile",
                                    new String[]{"MasteryLevel","MasteryEXP",},
                                    new int[]{getPureInt(LinesInFile.get(23)),getPureInt(LinesInFile.get(24))}
                            );
                            SupportLib.saveMultiInt(
                                    this,
                                    "ExcessDataFile",
                                    new String[]{"LevelLimit","LevelExcessRank","LevelExcessATK","LevelExcessCR","LevelExcessCD"},
                                    new int[]{getPureInt(LinesInFile.get(25)),getPureInt(LinesInFile.get(26)),getPureInt(LinesInFile.get(27)),getPureInt(LinesInFile.get(28)),getPureInt(LinesInFile.get(29))}
                                    );
                            //2.1 import Boss Level data belongs to Tourney correction.
                            int LevelNeedWrite = getPureInt(LinesInFile.get(31));
                            int UserLevel = SupportLib.getIntData(this,"EXPInformationStoreProfile","UserLevel",1);
                            if(LevelNeedWrite < 1 || LevelNeedWrite < UserLevel - 33 || LevelNeedWrite > UserLevel + 33){
                                LevelNeedWrite = UserLevel;
                            }
                            //2.2 import Boss HP data belongs to Tourney correction.
                            int HPNeedWrite = getPureInt(LinesInFile.get(32));
                            if(HPNeedWrite < 1){
                                HPNeedWrite = 1;
                            }
                            //2.3 import Boss Turn data belongs to Tourney correction.
                            int TurnNeedWrite = getPureInt(LinesInFile.get(33));
                            if(TurnNeedWrite < 1 || TurnNeedWrite > 1000){
                                TurnNeedWrite = 1;
                            }
                            //2.4 write data after correction.
                            SupportLib.saveMultiInt(
                                    this,
                                    "TourneyDataFile",
                                    new String[]{"BossLevel","BossHP","BossTurn",},
                                    new int[]{LevelNeedWrite,HPNeedWrite,TurnNeedWrite}
                            );
                            SupportLib.saveLongData(this,"TourneyDataFile","MaxPtRecord",getPureLong(LinesInFile.get(21)));
                            ShowProcessReport(getString(R.string.FinishShareLoadTran));
                        }
                        //2.5 loading Quest data.
                        String[] ReportSet = new String[]{"Error","Error","Error"};//default value.
                        int LoadedQuestNumber = ReadAllQuest();
                        if(LoadedQuestNumber <= 0){
                            ReportSet[0] = getString(R.string.BackupEmptyQuestTran);
                        }else if(IsPreview){
                            ReportSet[0] = LoadedQuestNumber + getString(R.string.DetectedQuestNumTran);
                        }else{//not preview mode, file have more than 0 Quest.
                            ReportSet[0] = LoadedQuestNumber + getString(R.string.SuccessLoadQuestTran);
                        }
                        ShowProcessReport(getString(R.string.FinishQuestLoadTran));
                        //2.6 loading Item data.
                        int LoadedItemNumber = ReadAllItems();
                        if(LoadedItemNumber <= 0){
                            ReportSet[1] = getString(R.string.BackupEmptyItemTran);
                        }else if(IsPreview){
                            ReportSet[1] = LoadedItemNumber + getString(R.string.DetectedItemNumTran);
                        }else{
                            ReportSet[1] = LoadedItemNumber + getString(R.string.SuccessLoadItemTran);
                        }
                        ShowProcessReport(getString(R.string.FinishItemLoadTran));
                        //2.7 loading Note data.
                        int NoteLoadLines = ReadAllNote();
                        if(NoteLoadLines <= 0){
                            ReportSet[2] = getString(R.string.BackupEmptyNoteTran);
                        }else if(IsPreview){
                            ReportSet[2] = NoteLoadLines + getString(R.string.DetectedItemNumTran);
                        }else{
                            ReportSet[2] = NoteLoadLines + getString(R.string.SuccessLoadNoteTran);
                        }
                        ShowProcessReport(getString(R.string.FinishNoteLoadTran));
                        //3.after used in BackUp process, report result to user.
                        message.obj = ReportSet;
                        message.what = FILE_IMPORT_DONE;
                        handler.sendMessage(message);
                        ShowProcessReport(getString(R.string.CompletedWordTran));
                    });
                    ImportThread.setPriority(Thread.MAX_PRIORITY);
                    ImportThread.start();
                    break;
                case CREATE_FILE:// TODO: 2021/9/12 Output data to file method.
                    //After file is successfully created in storage, prepare data to be wrote.
                    Thread ExportThread = new Thread(() -> {
                        //0.preparation.
                        ShowProcessReport(getString(R.string.StartWordTran));
                        //1.1 load data from SharePreferences.
                        //1.2 the content in file.Each AddContent() is a line in .txt file.
                        //EXPInformationStoreProfile.
                        int[] ExpSet = SupportLib.getMultiInt(
                                this,
                                "EXPInformationStoreProfile",
                                new String[]{"UserLevel","UserHaveEXP"},
                                new int[]{1,0}
                        );
                        int UserLevel = ExpSet[0];//User.
                        int UserHaveEXP = ExpSet[1];
                        //TimerSettingProfile.
                        int[] TimeSet = SupportLib.getMultiInt(
                                this,
                                "TimerSettingProfile",
                                new String[]{"TimerProgress","TimerTargetDay"},
                                new int[]{0,0}
                        );
                        int DayProgress = TimeSet[0];//Sol.Timer
                        int DayGoal = TimeSet[1];
                        //BattleDataProfile.
                        int[] BattleSet = SupportLib.getMultiInt(
                                this,
                                "BattleDataProfile",
                                new String[]{"ATKTalentLevel","CRTalentLevel","CDTalentLevel","QuestCombo","UserPoint","UserMaterial","BossDeadTime","UserConflictFloor"},
                                new int[]{0,0,0,0,0,0,0,0}
                        );
                        int ATKTalentLevel = BattleSet[0];//Talent.
                        int CRTalentLevel = BattleSet[1];
                        int CDTalentLevel = BattleSet[2];
                        int QuestCombo = BattleSet[3];//Resource.
                        int UserPoint = BattleSet[4];
                        int UserMaterial = BattleSet[5];
                        int BossDeadTime = BattleSet[6];//Record part1.
                        int UserConflictFloor = BattleSet[7];//Conflict.
                        //RecordDataFile.
                        int[] RecordSet = SupportLib.getMultiInt(
                                this,
                                "RecordDataFile",
                                new String[]{"RightAnswering","WrongAnswering","EXPGotten","MaterialGotten","ComboGotten","BattleFail","MaxDailyDifficulty"},
                                new int[]{0,0,0,0,0,0,0}
                        );
                        int UserRightTimes = RecordSet[0];//Answering.
                        int UserWrongTimes = RecordSet[1];
                        int EXPRecord = RecordSet[2];//Record part1.
                        long PointRecord = SupportLib.getLongData(this,"RecordDataFile","PointGotten",0);
                        int MaterialRecord = RecordSet[3];
                        int ComboRecord = RecordSet[4];
                        int BossFail = RecordSet[5];//Record part2.
                        int DailyDifficulty = RecordSet[6];//Daily.
                        //AlchemyMasteryFile.
                        int[] AlchemySet = SupportLib.getMultiInt(
                                this,
                                "AlchemyMasteryFile",
                                new String[]{"MasteryLevel","MasteryEXP"},
                                new int[]{0,0}
                        );
                        int MasteryLevel = AlchemySet[0];//Alchemy.
                        int MasteryEXP = AlchemySet[1];
                        //LimitUp.
                        int[] ExcessSet = SupportLib.getMultiInt(
                                this,
                                "ExcessDataFile",
                                new String[]{"LevelLimit","LevelExcessRank","LevelExcessATK","LevelExcessCR","LevelExcessCD"},
                                new int[]{50,0,0,0,0}
                        );
                        int LevelLimit = ExcessSet[0];
                        int LevelExcessRank = ExcessSet[1];
                        int LevelExcessATK = ExcessSet[2];
                        int LevelExcessCR = ExcessSet[3];
                        int LevelExcessCD = ExcessSet[4];
                        //TourneyDataFile.
                        int[] TourneySet = SupportLib.getMultiInt(
                                this,
                                "TourneyDataFile",
                                new String[]{"BossLevel","BossHP","BossTurn"},
                                new int[]{UserLevel,1000,10}
                        );
                        //Tourney.
                        long TourneyPt = SupportLib.getLongData(this,"TourneyDataFile","MaxPtRecord",0);//Tourney part1.
                        int BossLevel = TourneySet[0];//Tourney part2.
                        int BossHP = TourneySet[1];
                        int BossTurn = TourneySet[2];
                        ShowProcessReport(getString(R.string.FinishFileLoadTran));
                        //2.1 these lines and line4 above are the declaration which will be wrote in file.
                        AddContent("tomultiQA BackUp");//line1: notification and app version code.
                        AddContent(String.valueOf(ValueLib.APP_VERSION));//line2: the App version which file be made.
                        String CurrentDate = SupportLib.getSystemTime();//get file create time.
                        AddContent("Date: " + CurrentDate);//line3: file date.
                        //2.2 write actual content to backup.(line4 and above)
                        /*
                        DO NOT CHANGE ORDER OF THESE CODE!
                        It will affect capability of older version of backup file.
                        */
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
                        //in current version, file have 33 lines, so we will add "0" lines until Line 100.
                        PlaceHolderLine(67);
                        ShowProcessReport(getString(R.string.FinishShareLoadTran));
                        //Start with Line 101.
                        //DO NOT MOVE THESE METHOD POSITION, let the order to be Quest - Items.
                        AddAllQuest();
                        ShowProcessReport(getString(R.string.FinishQuestLoadTran));
                        AddAllItems();
                        ShowProcessReport(getString(R.string.FinishItemLoadTran));
                        AddAllNote();
                        ShowProcessReport(getString(R.string.FinishNoteLoadTran));
                        //3. write all texts above to file, override file's native content.
                        ReWriteFile();
                        message.what = FILE_EXPORT_DONE;
                        handler.sendMessage(message);
                        ShowProcessReport(getString(R.string.CompletedWordTran));
                    });
                    ExportThread.setPriority(Thread.MAX_PRIORITY);
                    ExportThread.start();
                    break;
                default:
                    break;
            }
        }
    }

    //lv.2 main method for button Output data OnClick().
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

    //lv.2 main method for button Input data OnClick().
    public void StartInput(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.DataInputHintTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.StartWordTran),
                (dialog1, id) -> {
                    IsPreview = false;
                    OpenPicker();
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
                AlertDialog DialogView = dialog.create();
                DialogView.show();
    }

    //lv.2 main method for button Preview file OnClick().
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

    //lv.1 method, sub method of handler(), after got Uri from picker, read the text from file which has specific Uri.
    private void GetLinesFromUri(){
        if(BackUpFileUri != null){//NPE prevent.
            try (InputStream inputStream = getContentResolver().openInputStream(BackUpFileUri);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    LinesInFile.add(line+"\n");
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

    //lv.1 method, sub method of handler(), Re-Write the texts of file.
    //thanks to: https://blog.csdn.net/daniel80110_1020/article/details/55260510 to fix android official example problem.
    private void ReWriteFile() {
        if(BackUpFileUri != null){
            try {
                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(BackUpFileUri, "w");
                FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                fileOutputStream.write(ContentNeedToAdd.toString().getBytes());
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close();
                pfd.close();
                //reset the record variable.
                ContentNeedToAdd = new StringBuilder();
            } catch (FileNotFoundException e) {
                Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this,getString(R.string.FileNotExistTran),Toast.LENGTH_SHORT).show();
        }
    }

    //lv.2 method, sub method to add Empty Line to file.
    private void PlaceHolderLine(int LineNumber){// TODO: 2021/9/10 for flexibility, I don't recommend you to delete this variable.
        while (LineNumber > 0){
            ContentNeedToAdd = ContentNeedToAdd.append("0").append("\n");
            LineNumber = LineNumber - 1;
        }
        //do not use AddContent("0\n") method in this Activity to replace it, it will generate multi WHITESPACE key.
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
    }//end of SharePreference and Total IO.


    //Quest db IO.
    Cursor QuestCursor = null;//store all Quests data in RAM.

    //these are file head and file end, DO NOT CHANGE THEM.
    public static final String QuestFileHead = "-/Quest-Data-Start/-";
    public static final String QuestFileEnd = "-=Quest-Data-End=-";
    public static final String ItemsFileHead = "--Item-Data-Part--";
    public static final String ItemFileEnd = "-=Item-Data-End=-";
    public static final String NoteFileHead = "-/Note-Data-Start/-";
    public static final String NoteFileEnd = "-/Note-Data-End/-";

    private void AddAllQuest(){
        AddContent(QuestFileHead);//define which line is start for Quest storage.
        //load database.
        QuestDbHelper QuestDbHelper = new QuestDbHelper(this);
        SQLiteDatabase db = QuestDbHelper.getReadableDatabase();
        QuestCursor = db.query(
                com.example.android.tomultiqa.QuestDbHelper.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        //Start Prepare String which represent all Quest's data.
        //this number [Counting].
        //when it is 0, means it need to write the Title text in current position in Cursor.
        //when it is 1, write Answer.
        //when it is 2, write Hint.
        //when it is 3, write Level of Quest.
        //when it is 4, write Type of Quest.
        //when it is 5, means all information in a Quest are wrote, need to move Cursor to next Position, and do all above again.
        //when Cursor has moved, the Counting variable should set to 0, and do a loop until All Quest in Cursor are Wrote to file.
        int Counting = 0;
        StringBuilder NowAddText = new StringBuilder();
        //preparation.
        int TitleColumnId = QuestCursor.getColumnIndex(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestTitle);
        int AnswerColumnId = QuestCursor.getColumnIndex(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestAnswer);
        int HintColumnId = QuestCursor.getColumnIndex(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestHint);
        int LevelColumnId = QuestCursor.getColumnIndex(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestLevel);
        int TypeColumnId = QuestCursor.getColumnIndex(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestType);
        if(QuestCursor.moveToFirst()){//if Quest data not empty.
            while (!QuestCursor.isLast()){//if Cursor not pointing last line, the loop will continue.
                //assume Cursor have X number of Quest.
                //In this step, the method will write Quest 1 ~ (X - 1) to String.
                switch (Counting){//according Counting record, load different parts in one line of Cursor to StringBuilder Variable.
                    case 0:
                        NowAddText.append(QuestCursor.getString(TitleColumnId)).append("\n");
                        break;
                    case 1:
                        NowAddText.append(QuestCursor.getString(AnswerColumnId)).append("\n");
                        break;
                    case 2:
                        NowAddText.append(QuestCursor.getString(HintColumnId)).append("\n");
                        break;
                    case 3:
                        NowAddText.append(QuestCursor.getString(LevelColumnId)).append("\n");
                        break;
                    case 4:
                        NowAddText.append(QuestCursor.getString(TypeColumnId)).append("\n");
                        break;
                    default:
                        break;
                }
                Counting = Counting + 1;
                if(Counting >= 5){
                    Counting = 0;
                    QuestCursor.moveToNext();
                }
            }
            //because of using !Cursor.isLast() as condition, the last Quest in Cursor can't be wrote to file. So we need to separately load last Quest in Cursor.
            if(QuestCursor.getCount() > 0){//if Cursor has only One Quest, we also need to load this single Quest.
                QuestCursor.moveToLast();
                NowAddText.append(QuestCursor.getString(TitleColumnId)).append("\n")
                        .append(QuestCursor.getString(AnswerColumnId)).append("\n")
                        .append(QuestCursor.getString(HintColumnId)).append("\n")
                        .append(QuestCursor.getString(LevelColumnId)).append("\n")
                        .append(QuestCursor.getString(TypeColumnId));
            }//Done Last Quest Loading.
            //finish! after loop. Done reading Cursor. add all data to String, ready to do next step.
            AddContent(NowAddText.toString());
            //add data end, to help app to process file.
            AddContent(QuestFileEnd);
        }else if(QuestCursor.getCount() > ValueLib.QUEST_MAX_NUMBER) {
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    getString(R.string.EmptyQuestFileTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    private int ReadAllQuest(){
        //use ArrayList function to find file head text to locate which is the start of Reading Quest data, "+1" means move Position to next line of file head.
        //(also the first line of all Quest data).
        //List.indexOf() can't find head out.
        int Position = -1;
        //the max length of all Quest data is the last line of Item's file head 's position.
        int MaxLength = -1;
        for(String Element: LinesInFile){
            if(Element.contains(QuestFileHead)){//get start file head and line.
                Position = LinesInFile.indexOf(Element) + 1;
            }
            if(Element.contains(QuestFileEnd)){//get end file head and line.
                MaxLength = LinesInFile.indexOf(Element) - 1;
            }
        }
        if(Position == -1 || MaxLength == -1){
            return 0;//if method can't find any file head or file end to mark the range, will cancel this part of loading.
        }
        //Start export String which represent all Quest's data.
        //this number [Counting]. Same as AddAllQuest() method.
        int Counting = 0;
        //statistic variable, when a Quest loaded done, add 1 to this, later, this variable will report to user.
        int LoadedQuest = 0;
        //the String Element in List: TextElement. defining here is for reduce repeat calling.
        String ThisLine;
        //here are the loading process.
        QuestDbHelper QuestDbHelper = new QuestDbHelper(this);
        SQLiteDatabase db = QuestDbHelper.getWritableDatabase();
        ContentValues Inputs = new ContentValues();
        while (Position <= MaxLength){//reading until done MaxLength.
            ThisLine = getPureString(LinesInFile.get(Position) );//get this line of String.
            switch (Counting){
                case 0:
                    Inputs.put(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestTitle,ThisLine);
                    break;
                case 1:
                    Inputs.put(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestAnswer,ThisLine);
                    break;
                case 2:
                    Inputs.put(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestHint,ThisLine);
                    break;
                case 3:
                    Inputs.put(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestLevel,ThisLine);
                    break;
                case 4:
                    Inputs.put(com.example.android.tomultiqa.QuestDbHelper.DataBaseEntry.COLUMN_NAME_QuestType,ThisLine);
                    break;
                default:
                    break;
            }
            Counting = Counting + 1;
            Position = Position + 1;
            if(Counting >= 5){//finish one Quest Import, reset Counting to change the [way] of data entrance.
                Counting = 0;
                LoadedQuest = LoadedQuest + 1;//recording import Quest number.
                //finish one Quest data import, submit changes to database.
                if(!IsPreview){//only not Preview state will actually change database.
                    db.insert(com.example.android.tomultiqa.QuestDbHelper.TABLE_NAME,null,Inputs);
                    //after submit data, clear the ContentValue, to prevent from repeat reading.
                    Inputs.clear();
                }
            }
        }
        //after all Quest Loaded, add [LoadedQuest] number to SharedPreference.
        if(!IsPreview){
            if(QuestCursor == null){//if null, query to fill it.
                //load database.
                QuestCursor = db.query(
                        com.example.android.tomultiqa.QuestDbHelper.TABLE_NAME,   // The table to query
                        null,             // The array of columns to return (pass null to get all)
                        null,              // The columns for the WHERE clause
                        null,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        null               // The sort order
                );
            }
            SupportLib.saveIntData(this,"IdNumberStoreProfile","IdNumber",QuestCursor.getCount());
        }
        return LoadedQuest;
    }//end of Quest db IO.


    //Item db IO.
    Cursor ItemCursor;//store all Items data in RAM.

    private void AddAllItems(){
        AddContent(ItemsFileHead);//define which line is start for Item storage.
        //load database.
        ItemDbHelper ItemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase db = ItemDbHelper.getReadableDatabase();
        ItemCursor = db.query(
                com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        //Start Prepare String which represent all Item's data.
        //this number [Counting].
        //when it is 0, means it need to write the Name text in current position in Cursor.
        //when it is 1, write Item's Type.
        //when it is 2, write Item's Text.
        //when it is 3, write Item's number.
        //when it is 4, means all information in a Item line are wrote, need to move Cursor to next Position, and do all above again.
        //when Cursor has moved, the Counting variable should set to 0, and do a loop until All Items in Cursor are Wrote to file.
        int Counting = 0;
        StringBuilder NowAddText = new StringBuilder();
        //preparation.
        int NameColumnId = ItemCursor.getColumnIndex(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName);
        int TypeColumnId = ItemCursor.getColumnIndex(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemType);
        int TextColumnId = ItemCursor.getColumnIndex(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemText);
        int NumberColumnId = ItemCursor.getColumnIndex(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber);
        if(ItemCursor.moveToFirst()){//if Quest data not empty.
            while (!ItemCursor.isLast()){//if Cursor not pointing last line, the loop will continue.
                //assume Cursor have X number of Quest.
                //In this step, the method will write Quest 1 ~ (X - 1) to String.
                switch (Counting){//according Counting record, load different parts in one line of Cursor to StringBuilder Variable.
                    case 0:
                        NowAddText.append(ItemCursor.getString(NameColumnId)).append("\n");
                        break;
                    case 1:
                        NowAddText.append(ItemCursor.getString(TypeColumnId)).append("\n");
                        break;
                    case 2:
                        NowAddText.append(ItemCursor.getString(TextColumnId)).append("\n");
                        break;
                    case 3:
                        NowAddText.append(ItemCursor.getString(NumberColumnId)).append("\n");
                        break;
                    default:
                        break;
                }
                Counting = Counting + 1;
                if(Counting >= 4){
                    Counting = 0;
                    ItemCursor.moveToNext();
                }
            }
            //because of using !Cursor.isLast() as condition, the last Quest in Cursor can't be wrote to file. So we need to separately load last Quest in Cursor.
            if(ItemCursor.getCount() > 0){//if Cursor has only One Quest, we also need to load this single Quest.
                ItemCursor.moveToLast();
                NowAddText.append(ItemCursor.getString(NameColumnId)).append("\n")
                        .append(ItemCursor.getString(TypeColumnId)).append("\n")
                        .append(ItemCursor.getString(TextColumnId)).append("\n")
                        .append(ItemCursor.getString(NumberColumnId));
            }//Done Last Quest Loading.
            //finish! after loop. Done reading Cursor. add all data to String, ready to do next step.
            AddContent(NowAddText.toString());
            //add data end of Item, to help app to process file.
            AddContent(ItemFileEnd);
        }else if(ItemCursor.getCount() > ValueLib.ITEM_MAX_NUMBER){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    getString(R.string.EmptyItemFileTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    private int ReadAllItems(){
        //use ArrayList function to find file head text to locate which is the start of Reading Quest data, "+1" means move Position to next line of file head.
        //(also the first line of all Quest data).
        //List.indexOf() can't find head out.
        int Position = -1;
        //the max length of all Quest data is the last line of Item's file head 's position.
        int MaxLength = -1;
        for(String Element: LinesInFile){
            if(Element.contains(ItemsFileHead)){//get start file head and line.
                Position = LinesInFile.indexOf(Element) + 1;
            }
            if(Element.contains(ItemFileEnd)){//get end file head and line.
                MaxLength = LinesInFile.indexOf(Element) - 1;
            }
        }
        if(Position == -1 || MaxLength == -1){
            return 0;//if method can't find any file head or file end to mark the range, will cancel this part of loading.
        }
        //Start Prepare String which represent all Item's data.
        //this number [Counting]. Same as AddAllItems() method.
        int Counting = 0;
        //statistic variable, when a Quest loaded done, add 1 to this, later, this variable will report to user.
        int LoadedItem = 0;
        //the String Element in List: TextElement. defining here is for reduce repeat calling.
        String ThisLine;
        //here are the loading process.
        ItemDbHelper ItemDbHelper = new ItemDbHelper(this);
        SQLiteDatabase db = ItemDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = ItemDbHelper.getReadableDatabase();
        ContentValues Inputs = new ContentValues();
        String ThisItemName = "";
        while (Position <= MaxLength){//reading until done MaxLength.
            //get this line of String.
            ThisLine = getPureString(LinesInFile.get(Position) );
            //decide where this line in file should write to.
            switch (Counting){
                case 0:
                    Inputs.put(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName,ThisLine);
                    ThisItemName = ThisLine;//get this to check if this item is existed in database.
                    break;
                case 1:
                    Inputs.put(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemType,ThisLine);
                    break;
                case 2:
                    Inputs.put(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemText,ThisLine);
                    break;
                case 3:
                    Inputs.put(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber,ThisLine);
                    break;
                default:
                    break;
            }
            Counting = Counting + 1;
            Position = Position + 1;
            //finish one Item Import, reset Counting to change the [way] of data entrance.
            if(Counting >= 4){
                Counting = 0;
                LoadedItem = LoadedItem + 1;//recording import Item number.
                //finish one item data import, submit changes to database.
                if(!IsPreview){//only not Preview state will actually change database.
                    //search this item whether in the database.
                    String Selection = com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
                    String[] SelectionArgs = new String[]{ThisItemName};
                    ItemCursor = db2.query(
                            com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.TABLE_NAME,
                            null,
                            Selection,
                            SelectionArgs,
                            null,
                            null,
                            null
                    );
                    if(ItemCursor.moveToFirst()){
                        //check if this item is existed in database now, if true, method will not insert new line, instead of delete existed line first, and insert new one later.
                        //delete existed line (because of you can't update exist row with a ContentValue, which has full data of a line.)
                        db.delete(
                                com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.TABLE_NAME,
                                Selection,
                                SelectionArgs
                        );
                    }
                    db.insert(com.example.android.tomultiqa.ItemDbHelper.DataBaseEntry.TABLE_NAME,null,Inputs);
                    //after submit data, clear the ContentValue, to prevent from repeat reading.
                    Inputs.clear();
                }
            }
        }
        //after all Item kinds Loaded, report the number.
        return LoadedItem;
    }//end of Item db IO.


    //Note content IO.
    private void AddAllNote(){
        AddContent(NoteFileHead);
        AddContent(SupportLib.getStringData(this,"FolderProfile","NoteText","-There is no Note-"));
        AddContent(NoteFileEnd);
    }

    private int ReadAllNote(){
        //preparation.
        StringBuilder NoteFromFile = new StringBuilder();
        int Position = -1;
        int MaxLength = -1;
        //line number of Note content.
        int Counting = -1;
        //get positions.
        for(String Element: LinesInFile){
            if(Element.contains(NoteFileHead)){//get start file head and line.
                Position = LinesInFile.indexOf(Element) + 1;
            }
            if(Element.contains(NoteFileEnd)){//get end file head and line.
                MaxLength = LinesInFile.indexOf(Element) - 1;
            }
        }
        if(Position == -1 || MaxLength == -1){
            return 0;//if method can't find any file head or file end to mark the range, will cancel this part of loading.
        }
        //after get the start and end of content, then read the content in file, and save them in variable.
        while (Position <= MaxLength){
            NoteFromFile.append(LinesInFile.get(Position));//don't use getPureString() in here, it will ripe "\n" in note content.
            Position = Position + 1;
            Counting = Counting + 1;
        }
        if(!IsPreview){
            //write the data to SharePreferences depending on the content in file with specific position.
            SupportLib.saveStringData(
                    this,
                    "FolderProfile",
                    "NoteText",
                    NoteFromFile.toString()
            );
        }
        return Counting;
    }


    //File Support part.
    //lv.1 method, main method to refresh the Process Report of Backup function UI.
    ArrayList<String> BackupReport = new ArrayList<>();

    private void ShowProcessReport(String Content){
        Thread thread = new Thread(() -> {
            BackupReport.add(Content);
            StringBuilder AllReport = new StringBuilder();
            for(String Each: BackupReport){
                AllReport.append(Each).append("\n");
            }
            Message message = Message.obtain();
            message.what = PROCESS_SHOW;
            message.obj = AllReport.toString();
            handler.sendMessage(message);
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    //lv.1 method, sub method of StartExportData() method. Add one line in Content which need to wrote in .txt file.

    /**
     * Use this method to add single line to the total content, which is waiting to wrote to file.
     * @param Text a line of text you want to input to file.
     */
    private void AddContent(String Text){
        ContentNeedToAdd = ContentNeedToAdd.append(Text).append("\n");
    }

    /**
     * lv.1 method, sub method of StartImportData() method.
     * String.replaceAll("\\s+","") removes all whitespaces and non-visible characters (e.g., tab, \n),;
     * if String in List have not clear all WHITESPACE and \n character, the reading code will have Error.
     * So we design this code to do Clear work quickly. And it will turn number in text format to int form.
     * @param ElementInList Input specific line of file's text.
     * @return int number after cleared.
     */
    private static int getPureInt(String ElementInList){
        //
        return Integer.parseInt(ElementInList.replaceAll("\\s+",""));
    }

    /**
     * lv.1 method, sub method of StartImportData() method.
     * String.replaceAll("\\s+","") removes all whitespaces and non-visible characters (e.g., tab, \n),;
     * if String in List have not clear all WHITESPACE and \n character, the reading code will have Error.
     * So we design this code to do Clear work quickly. And it will turn number in text format to long form.
     * @param ElementInList Input specific line of file's text.
     * @return long number after cleared.
     */
    private static long getPureLong(String ElementInList){
        return Long.parseLong(ElementInList.replaceAll("\\s+",""));
    }


    /**
     * lv.1 method, sub method to delete all WHITESPACE and /n letter in the String.
     * if we don't do it for string in the file, the String inputted will include these characters and can't combine with existed data.
     * @param ElementInList Input specific line of file's text.
     * @return String after cleared.
     */
    private static String getPureString(String ElementInList){
        return ElementInList.replaceAll("\\s+", "");
    }//end of File Support Part.
    //end of File manage system.


    //Help function.
    public void ShowBackUpHelp(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.ShopHelpTitleTran),
                ValueLib.BACKUP_HELP,
                getString(R.string.ConfirmWordTran)
             );
    }//end of Help function.
}