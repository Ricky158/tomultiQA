package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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
        //Folder-Note.
        String NoteText = SupportClass.getStringData(this,"FolderProfile","NoteText","-There is no Note-");
        //LimitUp.
        int LevelLimit = SupportClass.getIntData(this,"ExcessDataFile","LevelLimit",50);
        int LevelExcessRank = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessRank",0);
        int LevelExcessATK= SupportClass.getIntData(this,"ExcessDataFile","LevelExcessATK",0);
        int LevelExcessCR = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCR",0);
        int LevelExcessCD = SupportClass.getIntData(this,"ExcessDataFile","LevelExcessCD",0);

        //2.1 these lines and part 2.3 are the declaration which will be wrote in file.
        AddTextToFile("tomultiQA BackUp");//notification and app version code.
        AddTextToFile("App Version: 0.12.0");//the App version which file be made.
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
        AddTextToFile(NoteText);
        AddTextToFile(String.valueOf(LevelLimit));
        AddTextToFile(String.valueOf(LevelExcessRank));
        AddTextToFile(String.valueOf(LevelExcessATK));
        AddTextToFile(String.valueOf(LevelExcessCR));
        AddTextToFile(String.valueOf(LevelExcessCD));
        StopButtonLock();//after import over, stop the ButtonLock.
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.ProgressCompletedTran),
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
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
        SupportClass.saveStringData(this,"FolderProfile","NoteText",TextElement.get(25));
        SupportClass.saveIntData(this,"ExcessDataFile","LevelLimit",getPureInt(TextElement.get(26)));
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessRank",getPureInt(TextElement.get(27)));
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessATK",getPureInt(TextElement.get(28)));
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCR",getPureInt(TextElement.get(29)));
        SupportClass.saveIntData(this,"ExcessDataFile","LevelExcessCD",getPureInt(TextElement.get(30)));
        //3.after used in BackUp process, report result to user.
        SupportClass.CreateOnlyTextDialog(this,
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
                        getString(R.string.NoteWordTran) + TextElement.get(25) + "\n" +
                        getString(R.string.LevelLimitTran) + TextElement.get(26) + "\n" +
                        getString(R.string.LevelExcessTran) + TextElement.get(27) + "\n" +
                        getString(R.string.LevelExcessATKTran) + TextElement.get(28) + "\n" +
                        getString(R.string.LevelExcessCRTran) + TextElement.get(29) + "\n" +
                        getString(R.string.LevelExcessCDTran) + TextElement.get(30) + "\n",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true
        );
        //4.in the end,
        TextElement = new ArrayList<>();
        StopButtonLock();
    }

    //lv.2 method, main method of File Import Part. Pop up a dialog to show user the .txt file content.
    public void StartPreView(View view){
        //0.preparation.
        String BackUpFilePath = getExternalFilesDir("BackUp").getAbsolutePath();
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.FileContentWordTran),
                ReadAllFromFile(BackUpFilePath),
                getString(R.string.ConfirmWordTran),
                "nothing",
                true);
    }

    //lv.1 method, sub method of StartImportData() method. Get all text in .txt file.
    public String ReadAllFromFile(String FilePath) {
        String content = "";
        File file = new File(FilePath + ".txt");
        if (file.isDirectory()) {
            Toast.makeText(getApplicationContext(), "The File doesn't not exist.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "The File doesn't not exist.", Toast.LENGTH_SHORT).show();
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
            Log.d("TestFile", "The File doesn't not exist.");
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
                Log.d("TestFile", "The File doesn't not exist.");
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
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.ShopHelpTitleTran),
                "This Help File is only provided with Chinese edition.\n" +
                        "更新日期：2021/5/26\n" +
                        "\n" +
                        "0 声明\n" +
                        "欢迎使用tomultiQA的备份系统。\n" +
                        "由于技术水平问题，备份系统目前暂时处于测试阶段，可能存在bug，欢迎您提出反馈！\n" +
                        "\n" +
                        "1 数据导出\n" +
                        "目前，您可以点击“数据导出”键将App内的用户数据导出到.txt格式的备份文件当中。\n" +
                        "备份文件位于 /storage/emulated/0/Android/data/com.android.tomultiqa/file目录下。\n" +
                        "您可以将文件复制到App的对应文件夹下，让App进行读取。读取的操作详见帮助的第2部分。\n" +
                        "\n" +
                        "2 数据导入\n" +
                        "（1）将数据导出功能生成的“BackUp.txt”文件移动到/storage/emulated/0/Android/data/com.android.tomultiqa/file目录下，\n" +
                        "（2）再点击“数据导入”进行读取，读取完毕后App会弹框提示。\n" +
                        "2.1 注：在文件位置不正确或是文件不存在时，App会进行提示。\n" +
                        "2.2 如果您对备份文件是否准备好不甚清楚，您可以点击“数据导出”按钮下方的“文件预览”按钮。\n" +
                        "\n" +
                        "3 备份范围\n" +
                        "3.1 当前支持范围\n" +
                        "见帮助的4.3.1 备份文件概述的列出项目。\n" +
                        "3.2 当前暂不支持\n" +
                        "用户定义的题库（关键！）\n" +
                        "“Million Master”成就的获得与否\n" +
                        "作弊得到的用户属性\n" +
                        "\n" +
                        "4 常见问题和使用注意\n" +
                        "4.1 关于重复导出操作\n" +
                        "重复点击“数据导出”按钮将会将数据不断复制到文件当中。\n" +
                        "这虽然不影响读取，但是会额外占用空间和影响读取效率，更不便于您查看。\n" +
                        "因此，仅点击1次数据导出按钮即可，请勿重复点击！\n" +
                        "\n" +
                        "4.2 存在多份文件的情况\n" +
                        "如果file文件夹下存在多份备份文件，由于设计问题，App只会读写名为“BackUp.txt”的文件。\n" +
                        "因此，如果您有多份备份文件。请手动将需要读写的备份文件改为上述名称，或是删除不需要的文件。\n" +
                        "\n" +
                        "4.3 备份文件的手动修改\n" +
                        "4.3.1 备份文件概述\n" +
                        "首先，我们不建议您手动修改备份文件。因为人为修改可能会导致读取系统无法工作。\n" +
                        "其次，如果您实在希望这样做。以下是关于备份文件的说明：\n" +
                        "备份的原理是将App当中的每个变量（比如：用户的积分数量，素材数量等数值，它们之中的每一个都是“变量”）从App内部存储空间当中提取出来，\n" +
                        "并按事先安排的顺序写入备份文件。其中，每个变量在被写入时都会自动换行。\n" +
                        "（注：第3行是文件生成时的系统时间，由于API限制，只在Android 7.0及以上提供，低于7.0版本的会显示“---”）\n" +
                        "文件的构成如下：\n" +
                        "tomultiQA BackUp -- 文件标题\n" +
                        "App Version: 1.0.0 -- 文件关联的App版本\n" +
                        "Date：......              -- 文件生成的系统时间\n" +
                        "用户等级\n" +
                        "用户当前持有的经验值\n" +
                        "用户正确回答数\n" +
                        "用户错误回答数\n" +
                        "“天数计时”进度\n" +
                        "“天数计时”目标\n" +
                        "攻击力天赋等级\n" +
                        "暴击率天赋等级\n" +
                        "暴击伤害天赋等级\n" +
                        "当前答题连对次数\n" +
                        "当前积分数量\n" +
                        "当前素材数量\n" +
                        "boss累计死亡次数\n" +
                        "累计获得经验值（用户等级经验）\n" +
                        "累计获得积分数\n" +
                        "累计获得素材数\n" +
                        "最大答题连对数\n" +
                        "“敌临境”通关层数\n" +
                        "“比武台”最高评价\n" +
                        "“望晨崖”通关难度\n" +
                        "用户炼金等级\n" +
                        "当前炼金经验值\n" +
                        "用户笔记文本\n" +
                        "等级上限\n" +
                        "等级突破阶段\n" +
                        "等级突破攻击加成\n" +
                        "等级突破暴击加成\n" +
                        "等级突破暴伤加成\n" +
                        "\n" +
                        "4.3.2 文件修改指导\n" +
                        "自由修改：\n" +
                        "文件标题\n" +
                        "可以修改：\n" +
                        "文件版本，备份的数值。\n" +
                        "\n" +
                        "4.3.3 修改注意事项\n" +
                        "（1）数值上限\n" +
                        "大多数数字的上限采用了int型，最大支持2^32-1大小的数值。\n" +
                        "只有“累计获得积分”和“‘比武台’最高评价”两项使用了long型，最大支持2^63-1的数值。\n" +
                        "如果数值过大，可能会导致App崩溃，或者使数值溢出为负值。\n" +
                        "（2）数值下界\n" +
                        "文件当中数值没有低于0的部分，请在修改时不要将数值改为0以下，这可能引起App的行为异常。\n" +
                        "其中，特例的是“用户等级”“用户炼金等级”“‘天数计时’目标”三项，它们的最小值是1，而不是0。\n" +
                        "\n" +
                        "4.4 关于备份文件的版本\n" +
                        "我们建议App使用对应版本的备份文件，因为备份的读取系统不会因为文档内容的变更而更改读取方式。\n" +
                        "跨版本的读取轻则引起数据异常，重则引起App崩溃，无法使用，备份文件作废等后果。\n" +
                        "但我们并未在App当中作出硬性限制，文件当中的版本号不会影响读取操作。仅仅是出于提醒用户注意的目的设定的。\n" +
                        "还请您在实际操作当中注意。\n" +
                        "\n" +
                        "4.5 关于文件目录的问题\n" +
                        "由于开发者技术问题，备份目前只支持单一规定目录的读写，也许未来会提供其他目录的读写。\n" +
                        "\n" +
                        "4.6 文件保存注意事项\n" +
                        "由于备份文件是保存在Android应用的“专属外部存储空间”当中的，因此在以下情况下，备份文件会被一并删除。\n" +
                        "情况：\n" +
                        "（1）App被卸载时\n" +
                        "（2）App被系统执行“数据删除”的指令。\n" +
                        "请及时将文件转移至生成目录以外的区域。\n" +
                        "\n" +
                        "4.7 关于文件的复用\n" +
                        "注：由于备份文件在被重复写入时会产生问题（详见4.1部分），请【务必】在导入完毕（使用后）从文件夹当中移除备份文件；\n" +
                        "否则，重复写入的数据将无法被读取。请【务必】注意这一点，避免无效备份！\n" +
                        "\n" +
                        "4.8 空白的“BackUp”文件夹\n" +
                        "由于开发者水平问题，备份系统生成“BackUp.txt”的备份文件时，也会生成一个空白的同名文件夹。\n" +
                        "出于维持App正常运转的目的，这个bug暂时不考虑修复。\n" +
                        "您对此文件夹的删除或是读写操作不会影响备份系统的工作。\n" +
                        "我们不建议您对该文件夹删除，因为即使是手动删除，您也无法释放设备存储，且文件夹会在备份时自动生成。\n" +
                        "由此导致删除没有实际意义，如果引起了您的不适，我们深感歉意。",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
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