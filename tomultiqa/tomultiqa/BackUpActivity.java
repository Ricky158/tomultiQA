package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

public class BackUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        //......
    }

    //file manage system.
    //thanks to: https://blog.csdn.net/qq_16068303/article/details/106758659 !
    // https://blog.csdn.net/dandelionela/article/details/96482372 !
    // https://blog.csdn.net/qq_32611951/article/details/78048309 !
    // https://stackoverflow.com/questions/5455794/removing-whitespace-from-strings-in-java?noredirect=1&lq=1 !
    // https://blog.csdn.net/ruiruiddd/article/details/106543067 !
    // https://cloud.tencent.com/developer/article/1705324 !
    // https://blog.csdn.net/chyychfchx/article/details/59484332 !

    //Export Part.
    //each element in List is a line in .txt file.
    ArrayList<String> TextElement = new ArrayList<>();

    //lv.3 method, main method of File Import Part.
    public void StartExportData(View view) throws IOException {
        StartButtonLock();
        //0.preparation.
        File BackUpFile;
        String BackUpFilePath = getExternalFilesDir("BackUp").getAbsolutePath();
        String BackUpFileName = ".txt";//Word "BackUp" is in the path sentence.
        //1.get the file in /data/com.android.tomultiQA/file/BackUp/BackUp.txt
        BackUpFile = CreateFile(BackUpFilePath,BackUpFileName);
        if(BackUpFile.exists()){
            //1.1 if file exist,return file path.
            Toast.makeText(getApplicationContext(), BackUpFilePath, Toast.LENGTH_SHORT).show();
        }
        //1.2 the content in file.Each AddTextToFile() is a line in .txt file.

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
        //Record.
        int BossDeadTime = SupportClass.getIntData(this,"BattleDataProfile","BossDeadTime",0);
        int EXPRecord = SupportClass.getIntData(this,"RecordDataFile","EXPGotten",0);
        long PointRecord = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
        int MaterialRecord = SupportClass.getIntData(this,"RecordDataFile","MaterialGotten",0);
        int ComboRecord = SupportClass.getIntData(this,"RecordDataFile","ComboGotten",0);
        //Conflict.
        int UserConflictFloor = SupportClass.getIntData(this,"BattleDataProfile","UserConflictFloor",0);
        //Tourney.
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

        //2.1 these lines and part 2.3 are the declaration which will be wrote in file.
        AddTextToFile("tomultiQA BackUp");//notification and app version code.
        AddTextToFile(ValueClass.APP_VERSION);//the App version which file be made.
        //2.2 if Android Version is higher than Android N (7.0+), using api to get system time.and write time to file.
        String CurrentDate = "---";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat d_f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            d_f.setTimeZone(TimeZone.getTimeZone("GMT+08"));  //设置时区，+08是北京时间
            CurrentDate = d_f.format(new Date()) + " (GMT+8)";
        }
        AddTextToFile("Date: " + CurrentDate);
        //2.3 write actual content to file.
        AddTextToFile(String.valueOf(UserLevel));
        AddTextToFile(String.valueOf(UserHaveEXP));
        AddTextToFile(String.valueOf(UserRightTimes));
        AddTextToFile(String.valueOf(UserWrongTimes));
        AddTextToFile(String.valueOf(DayProgress));
        AddTextToFile(String.valueOf(DayGoal));
        AddTextToFile(String.valueOf(ATKTalentLevel));
        AddTextToFile(String.valueOf(CRTalentLevel));
        AddTextToFile(String.valueOf(CDTalentLevel));
        AddTextToFile(String.valueOf(QuestCombo));
        AddTextToFile(String.valueOf(UserPoint));
        AddTextToFile(String.valueOf(UserMaterial));
        AddTextToFile(String.valueOf(BossDeadTime));
        AddTextToFile(String.valueOf(EXPRecord));
        AddTextToFile(String.valueOf(PointRecord));
        AddTextToFile(String.valueOf(MaterialRecord));
        AddTextToFile(String.valueOf(ComboRecord));
        AddTextToFile(String.valueOf(UserConflictFloor));
        AddTextToFile(String.valueOf(TourneyPt));
        AddTextToFile(String.valueOf(DailyDifficulty));
        AddTextToFile(String.valueOf(MasteryLevel));
        AddTextToFile(String.valueOf(MasteryEXP));
        AddTextToFile(String.valueOf(LevelLimit));
        AddTextToFile(String.valueOf(LevelExcessRank));
        AddTextToFile(String.valueOf(LevelExcessATK));
        AddTextToFile(String.valueOf(LevelExcessCR));
        AddTextToFile(String.valueOf(LevelExcessCD));
        StopButtonLock();//after import over, stop the ButtonLock.
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.ProgressCompletedTran),
                getString(R.string.ConfirmWordTran)
        );
    }//end of Export part.


    //Import part.
    //lv.2 method, main method of File Import Part.
    public void StartImportData(View view){
        StartButtonLock();
        //0.preparation.
        String BackUpFilePath = getExternalFilesDir("BackUp").getAbsolutePath();
        String BackUpFileName = ".txt";//Word "BackUp" is in the path sentence.
        //1.get text in .txt file, and save them by lines.
        ReadByLineFromFile(BackUpFilePath + BackUpFileName);
        //2.BacKUp the data according the file.
        //ArrayList start from 0, instead of 1 !
        //Line 0-2 in file are declaration.
        SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserLevel",getPureInt(TextElement.get(3)));
        SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",getPureInt(TextElement.get(4)));
        SupportClass.saveIntData(this,"RecordDataFile","RightAnswering",getPureInt(TextElement.get(5)));
        SupportClass.saveIntData(this,"RecordDataFile","WrongAnswering",getPureInt(TextElement.get(6)));
        SupportClass.saveIntData(this,"TimerSettingProfile","TimerProgress",getPureInt(TextElement.get(7)));
        SupportClass.saveIntData(this,"TimerSettingProfile","TimerTargetDay",getPureInt(TextElement.get(8)));
        SupportClass.saveIntData(this,"BattleDataProfile","ATKTalentLevel",getPureInt(TextElement.get(9)));
        SupportClass.saveIntData(this,"BattleDataProfile","CRTalentLevel",getPureInt(TextElement.get(10)));
        SupportClass.saveIntData(this,"BattleDataProfile","CDTalentLevel",getPureInt(TextElement.get(11)));
        SupportClass.saveIntData(this,"BattleDataProfile","QuestCombo",getPureInt(TextElement.get(12)));
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",getPureInt(TextElement.get(13)));
        SupportClass.saveIntData(this,"BattleDataProfile","UserMaterial",getPureInt(TextElement.get(14)));
        SupportClass.saveIntData(this,"BattleDataProfile","BossDeadTime",getPureInt(TextElement.get(15)));
        SupportClass.saveIntData(this,"RecordDataFile","EXPGotten",getPureInt(TextElement.get(16)));
        SupportClass.saveLongData(this,"RecordDataFile","PointGotten",getPureLong(TextElement.get(17)));
        SupportClass.saveIntData(this,"RecordDataFile","MaterialGotten",getPureInt(TextElement.get(18)));
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
                        getString(R.string.LevelExcessCDTran) + TextElement.get(29) + "\n",
                getString(R.string.ConfirmWordTran)
        );
        //4.in the end,
        TextElement = new ArrayList<>();
        StopButtonLock();
    }

    //lv.2 method, main method of File Import Part. Pop up a dialog to show user the .txt file content.
    public void StartPreView(View view){
        //0.preparation.
        String BackUpFilePath = getExternalFilesDir("BackUp").getAbsolutePath();
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.FileContentWordTran),
                ReadAllFromFile(BackUpFilePath),
                getString(R.string.ConfirmWordTran)
        );
    }

    //lv.1 method, sub method of StartImportData() method. Get all text in .txt file.
    public String ReadAllFromFile(String FilePath) {
        String content = "";
        File file = new File(FilePath + ".txt");
        if (file.isDirectory()) {
            Toast.makeText(getApplicationContext(), getString(R.string.FileNotExistTran), Toast.LENGTH_SHORT).show();
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                instream.close();
            } catch (java.io.FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.FileNotExistTran), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return content;
    }

    //lv.1 method, sub method of StartExportData() method. Add one line text to .txt file.
    public void AddTextToFile(String Content) throws IOException {
        //0.preparation.
        String BackUpFilePath = getExternalFilesDir("BackUp").getAbsolutePath();
        String BackUpFileName = ".txt";//Word "BackUp" is in the path sentence.
        //1.if no folder there, create a new one.
        //CreateFolder(BackUpFilePath);
        String strFilePath = BackUpFilePath + BackUpFileName;
        String strContent = Content + "\n";//auto change input to next line in file.\r\n
            File file = new File(strFilePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
    }

    public void ReadByLineFromFile(String FilePath){
        //打开文件
        File file = new File(FilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()){
            Log.d("TestFile", getString(R.string.FileNotExistTran));
        }
        else{
            try {
                InputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while (( line = buffreader.readLine()) != null) {
                    TextElement.add(line+"\n");
                }
                instream.close();
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", getString(R.string.FileNotExistTran));
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
    }

    //File Support Part.
    //lv.1 method, create a new File in Specific Folder, if Folder is not exist, method will create a new one.
    public File CreateFile(String FilePath, String FileName){
        //生成文件
        File file = null;
        try {
            file = new File(FilePath + FileName);
            if (!file.exists()) {
                boolean CreateSuccess = file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

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


    //Button Protect system.
    private void StartButtonLock(){
        Button BackUpExportButton = findViewById(R.id.BackUpExportButton);
        Button BackUpImportButton = findViewById(R.id.BackUpImportButton);
        BackUpExportButton.setEnabled(false);
        BackUpImportButton.setEnabled(false);
    }

    private void StopButtonLock(){
        Button BackUpExportButton = findViewById(R.id.BackUpExportButton);
        Button BackUpImportButton = findViewById(R.id.BackUpImportButton);
        BackUpExportButton.setEnabled(true);
        BackUpImportButton.setEnabled(true);
    }//end of Button Protect system.
}