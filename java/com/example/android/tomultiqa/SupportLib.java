package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupportLib {

    //building support:
    //https://stackoverflow.com/questions/7491287/android-how-to-use-sharedpreferences-in-non-activity-class !
    //https://stackoverflow.com/questions/23179795/android-onclicklistener-intent-and-context !

    //math calculation of two values` percent. Using float number calculation.
    //Thanks to: https://stackoverflow.com/questions/787700/how-to-make-the-division-of-2-ints-produce-a-float-instead-of-another-int
    public static int CalculatePercent(int valueCurrent, int valueTotal){
        int percent;
        percent = Double.valueOf((float) valueCurrent/ valueTotal * 100).intValue();
        return percent;
    }

    //int form Random Number generator.Thanks to:
    // https://developer.android.google.cn/training/data-storage/sqlite?hl=zh_cn#java
    // https://stackoverflow.com/questions/6029495/how-can-i-generate-random-number-in-specific-range-in-android
    // https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
    // https://www.jianshu.com/p/b290eecbd87e
    public static int CreateRandomNumber(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    //because of the random number generator use time as seed, so, for multiple calls from close time, the function will give us same number.
    //for scenario like Sign at specific time, we will get same result every turn.
    //and Math.random() method can probably fix this problem.
    //refer: https://blog.csdn.net/weixin_39890517/article/details/114558082 !
    //thanks to: https://blog.csdn.net/qq_21808961/article/details/79931087 !
    public static int CreateRandomNumber2(int min, int max){
        return (int) (Math.random() * (max + 1 - min) );
    }

    //the standard text dialog form or sample, thanks to:
    /**
     * @deprecated this method not suggested to use in actual environment, we just leave it here for learning purpose.
     * but don't delete it, because some old code still relying this method.
     */
    // https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android !
    // https://developer.android.google.cn/guide/topics/ui/dialogs#PassingEvents !
    public static void CreateOnlyTextDialog(Context MethodContext, String Title, String ContentText, String LeftButtonText, String RightButtonText, boolean OnlyShowInLeftButton){
        //1.using android api to create a dialog object.
        AlertDialog.Builder dialog = new AlertDialog.Builder(MethodContext);
        //2.set basic values of dialog, including content text,button text,and title.
        dialog.setTitle(Title);
        dialog.setMessage(ContentText);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                //set left button`s text.
                LeftButtonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //set the things that pressed left button to do.
                        //......
                        //after doing things, close whole dialog.
                        dialog.cancel();
                    }
                });
        if (!OnlyShowInLeftButton) {
            dialog.setNegativeButton(
                    //set right button`s text.
                    RightButtonText,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //set the things that pressed right button to do.
                            //......
                            //after doing things, close whole dialog.
                            dialog.cancel();
                        }
                    });
        }
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //Nearly same as CreateOnlyTextDialog(), but shorter and quicker to use.
    public static void CreateNoticeDialog(Context MethodContext, String Title, String ContentText, String ButtonText){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MethodContext);
        dialog.setTitle(Title);
        dialog.setMessage(ContentText);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                ButtonText,
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    /**
     * Description:<br/>
     * Show a Window to cover the screen as dialog, the Text inputted will be automatically spilt to lines(according "/l").<br/>
     * Just like many modern game, this window can provide similar experience.<br/>
     * You can read the content by each line, and skip the content.<br/>
     * Notice: <br/>
     * We don't provide any callback to handle methods. If You need, then you have to modify your own method.<br/>
     * You can get example in <code>StoryActivity.onOptionsItemSelected()</code>.<br/>
     * Using:<br/>
     * 1. Click the Window to display next line of Content.<br/>
     * 2. Click "X" Button or finish all Content reading to close the dialog.<br/>
     * @param MethodContext Which Position the Window should appear. Usually is Activity or [this].
     * @param Attach Which View the Window should attach to, it will display on the top of View, and it's Width will depend on View's.<br/>
     *               So we suggest put root Layout of screen as Attach.
     * @param AllContent The Content that Window should display, and it will be spilt by <code>/l</code><br/>
     *                   (not using "\n" is that sometime we need to display multi-line in single window)<br/>
     *                   So you need to arrange text to make it more natural.
     */
    @SuppressLint("InflateParams")
    public static void CreateWindowDialog(Context MethodContext, View Attach, String AllContent){
        String[] EachLineStory = AllContent.split("/l");
        final int[] ReadProcess = {0};
         //the root view of window will be set in PopupWindow.showAtLocation().
        View Window = LayoutInflater.from(MethodContext).inflate(R.layout.story_window,null);
        PopupWindow StoryBoard = new PopupWindow(Window, Attach.getWidth() - 60, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView StoryTextView = Window.findViewById(R.id.StoryTextView);
        ImageView StoryNextPin = Window.findViewById(R.id.StoryNextPin);
        ImageView StorySkipButton = Window.findViewById(R.id.StorySkipButton);
        ImageView StoryBackButton = Window.findViewById(R.id.StoryBackButton);
        //2.animation to StoryText in Closing Window.
        AlphaAnimation AlphaStartTran = new AlphaAnimation(0.1f,0.9f);
        AlphaStartTran.setDuration(500);
        StoryTextView.setAnimation(AlphaStartTran);
        StorySkipButton.setAnimation(AlphaStartTran);
        StoryBackButton.setAnimation(AlphaStartTran);
        //3.animation to UI pin. (the triangle)
        TranslateAnimation SizeTran = new TranslateAnimation(0f,0f,0f,12f);//move from the left|top corner of view's (0,0) to (0,12).
        SizeTran.setDuration(1000);
        SizeTran.setRepeatMode(TranslateAnimation.REVERSE);
        SizeTran.setRepeatCount(TranslateAnimation.INFINITE);
        StoryNextPin.setAnimation(SizeTran);
        //4.fill content.
        final int ReadMaxLine = EachLineStory.length;
        //4.1 set OnClick method.
        StoryTextView.setOnClickListener(view -> {
            if(ReadProcess[0] < ReadMaxLine){//still has text not read.
                StoryTextView.setText(EachLineStory[ReadProcess[0]]);//load the next line of Story text.
                ReadProcess[0] = ReadProcess[0] + 1;
            }else{//all text in this chapter are read.

                //animation for StoryText in Closing Window.
                AlphaAnimation AlphaEndTran = new AlphaAnimation(0.9f,0.1f);
                AlphaEndTran.setDuration(400);
                StoryTextView.setAnimation(AlphaEndTran);
                //when the Animation are executed does.
                AlphaEndTran.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        //I don't need this part but I have to override it.
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {//close the Window.
                        //reset to initial value.
                        ReadProcess[0] = 0;
                        StoryBoard.dismiss();//close the Board.
                        //close the animation to prevent from RAM leak.
                        StoryTextView.clearAnimation();
                        StorySkipButton.clearAnimation();
                        StoryNextPin.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        //I don't need this part but I have to override it.
                    }
                });
            }
        });
        StorySkipButton.setOnClickListener(view -> {//close the Window.
            //reset to initial value.
            ReadProcess[0] = 0;
            StoryBoard.dismiss();//close the Board.
            //close the animation to prevent from RAM leak.
            StoryTextView.clearAnimation();
            StorySkipButton.clearAnimation();
            StoryBackButton.clearAnimation();
            StoryNextPin.clearAnimation();
        });
        StoryBackButton.setOnClickListener(v -> {
            StringBuilder ReadStory = new StringBuilder();
            int LoadProcess = 0;
            while (LoadProcess < ReadProcess[0]){
                ReadStory.append(EachLineStory[LoadProcess]).append("\n\n");
                LoadProcess = LoadProcess + 1;
            }
            SupportLib.CreateNoticeDialog(MethodContext,
                    MethodContext.getString(R.string.ReadHistoryWordTran),
                    ReadStory.toString(),
                    MethodContext.getString(R.string.ConfirmWordTran));
        });
        //5.show to user.
        StoryBoard.showAtLocation(Attach, Gravity.BOTTOM,0,100);//show the window above the screen's bottom, and lift it up 80px.
    }

    //thanks to:
    // https://blog.csdn.net/weixin_29053561/article/details/88564791 !
    // https://stackoverflow.com/questions/13444546/android-adt-21-0-warning-implicitly-using-the-default-locale !
    //keep 2 bit of the part of under 0, like 1.01234... equals to 1.01.
    @SuppressLint("DefaultLocale")
    public static String ReturnTwoBitText(double NeedToReturn){
        return String.format("%.2f", NeedToReturn);
    }

    //make int number like financial form, like 1000000 will be transformed to 1,000,000.
    //thanks to: https://blog.csdn.net/sinat_35241409/article/details/53710227?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_title-1&spm=1001.2101.3001.4242 !
    public static String ReturnKiloIntString(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(num);
    }

    //same as ReturnKiloIntString(), but for long data type.
    public static String ReturnKiloLongString(long num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(num);
    }

    //to turn secs to hh:mm:ss, like 61s will be transformed to 00:01:01.
    @SuppressLint("DefaultLocale")
    public static String ReturnTimerString(int SecondNumber){
        int Hour;
        int Minute;
        int Second;
        Hour = SecondNumber/3600;
        Minute = (SecondNumber%3600)/60;
        Second = (SecondNumber%3600)%60;
        return String.format("%02d:%02d:%02d", Hour, Minute, Second);
    }

    public static String getSystemTime(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//if Android Version is higher than Android N (7.0+), using api to get system time.and write time to file.
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            df.setTimeZone(TimeZone.getTimeZone("GMT+08"));  //设置时区，+08是北京时间
            return df.format(new Date()) + " (GMT+8)";
        }else{
            Time time = new Time();
            time.setToNow();
            return time.format("%Y-%m-%d %H:%M:%S");
        }
    }

    //get number which inputted in EditText.
    public static int getInputNumber(EditText NeedToGet){
        //after input confirmed.
        int Number;//store the exact Number in Code in int form.
        //try to catch the error, if there are no number in Resource Code String,
        //this method will give this variable a default value, to prevent it from crash.
        try{
            Number = Integer.parseInt(NeedToGet.getText().toString());
        }catch (NumberFormatException e){
            Number = 0;
        }
        return Number;
    }

    //thanks to: https://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string# !
    public static int getTextLineNumber(String Input){
        Matcher m = Pattern.compile("\r\n|\r|\n").matcher(Input);
        int lines = 1;
        while (m.find())
        {
            lines ++;
        }
        return lines;
    }

    //it haven't use in actual development, we don't know whether this method is usable or not.
    //Check when this method executed, the virtual keyboard of device is ON or OFF.
    //if method return true, keyboard is ON. or when it return false, keyboard is OFF.
    //thanks to: https://www.cnblogs.com/gejs/p/4363460.html !
    // https://blog.csdn.net/u010356768/article/details/89532846 !
//    public boolean CheckKeyboardState() {
//        Rect r = new Rect();
//        //获取当前界面可视部分
//        MainActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//        //获取屏幕的高度
//        int screenHeight =  MainActivity.this.getWindow().getDecorView().getRootView().getHeight();
//        //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//        int heightDifference = screenHeight - r.bottom;
//        return heightDifference > 0;
//    }

    //provide quick support for read int form data from SharedPreference.
    public static int getIntData(Context MethodContext, String FileName, String ContentKey, int DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return A.getInt(ContentKey,DefValue);
    }

    public static int[] getMultiInt(Context MethodContext, String FileName, String[] ContentKey, int[] DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        int NowId = 0;
        int MaxId = ContentKey.length;
        int[] Result = new int[MaxId];
        while(NowId < MaxId){
            Result[NowId] = A.getInt(ContentKey[NowId],DefValue[NowId]);
            NowId = NowId + 1;
        }
        return Result;
    }

    //provide quick support for read long form data from SharedPreference.
    public static long getLongData(Context MethodContext, String FileName, String ContentKey, int DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return A.getLong(ContentKey,DefValue);
    }

    //provide quick support for load boolean form data from SharedPreference.
    public static boolean getBooleanData(Context MethodContext, String FileName, String ContentKey, boolean DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return A.getBoolean(ContentKey,DefValue);
    }

    public static boolean[] getMultiBoolean(Context MethodContext, String FileName, String[] ContentKey, boolean[] DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        int NowId = 0;
        int MaxId = ContentKey.length;
        boolean[] Result = new boolean[MaxId];
        while(NowId < MaxId){
            Result[NowId] = A.getBoolean(ContentKey[NowId],DefValue[NowId]);
            NowId = NowId + 1;
        }
        return Result;
    }

    //provide quick support for load boolean form data from SharedPreference.
    public static String getStringData(Context MethodContext, String FileName, String ContentKey, String DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return A.getString(ContentKey,DefValue);
    }

    //provide quick support for save int form data from SharedPreference.
    public static void saveIntData(Context MethodContext, String FileName, String ContentKey, int Content){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        //获取Editor
        SharedPreferences.Editor editor= A.edit();
        //得到Editor后，写入需要保存的数据
        editor.putInt(ContentKey, Content);
        editor.apply();//提交修改
    }

    public static void saveMultiInt(Context MethodContext, String FileName, String[] ContentKey, int[] Content){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        //获取Editor
        SharedPreferences.Editor editor= A.edit();
        //得到Editor后，写入需要保存的数据
        int NowId = 0;
        int MaxId = ContentKey.length;
        while(NowId < MaxId){
            editor.putInt(ContentKey[NowId], Content[NowId]);
            NowId = NowId + 1;
        }
        editor.apply();//提交修改
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveLongData(Context MethodContext, String FileName, String ContentKey, long Content){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putLong(ContentKey,Content);
        editor.apply();
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveBooleanData(Context MethodContext, String FileName, String ContentKey, boolean Content){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putBoolean(ContentKey,Content);
        editor.apply();
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveStringData(Context MethodContext, String FileName, String ContentKey, String Content){
        SharedPreferences A = MethodContext.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putString(ContentKey,Content);
        editor.apply();
    }
}
