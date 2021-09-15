package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.text.format.Time;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class SupportClass {

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

    //lv.1 method, sub method of BuyMarketItem() and SellMarketItem() method.
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

    //provide quick support for read int form data from SharedPreference.
    public static int getIntData(Context MethodContext, String ProfileName, String ContentKey, int DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        return A.getInt(ContentKey,DefValue);
    }

    //provide quick support for read long form data from SharedPreference.
    public static long getLongData(Context MethodContext, String ProfileName, String ContentKey, int DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        return A.getLong(ContentKey,DefValue);
    }

    //provide quick support for load boolean form data from SharedPreference.
    public static boolean getBooleanData(Context MethodContext, String Profile, String ContentKey, boolean DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(Profile, Context.MODE_PRIVATE);
        return A.getBoolean(ContentKey,DefValue);
    }

    //provide quick support for load boolean form data from SharedPreference.
    public static String getStringData(Context MethodContext, String Profile, String ContentKey, String DefValue){
        SharedPreferences A = MethodContext.getSharedPreferences(Profile, Context.MODE_PRIVATE);
        return A.getString(ContentKey,DefValue);
    }

    //provide quick support for save int form data from SharedPreference.
    public static void saveIntData(Context MethodContext, String ProfileName, String ContentKey, int Content){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        //获取Editor
        SharedPreferences.Editor editor= A.edit();
        //得到Editor后，写入需要保存的数据
        editor.putInt(ContentKey, Content);
        editor.apply();//提交修改
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveLongData(Context MethodContext, String ProfileName, String ContentKey, long Content){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putLong(ContentKey,Content);
        editor.apply();
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveBooleanData(Context MethodContext, String ProfileName, String ContentKey, boolean Content){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putBoolean(ContentKey,Content);
        editor.apply();
    }

    //provide quick support for save long form data from SharedPreference.
    public static void saveStringData(Context MethodContext, String ProfileName, String ContentKey, String Content){
        SharedPreferences A = MethodContext.getSharedPreferences(ProfileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putString(ContentKey,Content);
        editor.apply();
    }
}
