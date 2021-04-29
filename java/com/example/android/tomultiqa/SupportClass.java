package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import java.util.concurrent.ThreadLocalRandom;

public class SupportClass {

    //building support:
    //https://stackoverflow.com/questions/7491287/android-how-to-use-sharedpreferences-in-non-activity-class !
    //https://stackoverflow.com/questions/23179795/android-onclicklistener-intent-and-context !

    //math calculation of two values` percent. Using float number calculation.
    //Thanks to: https://stackoverflow.com/questions/787700/how-to-make-the-division-of-2-ints-produce-a-float-instead-of-another-int
    public static int CalculatePercent(int valueCurrent, int valueTotal){
        int percent;
        percent = Double.valueOf((float) valueCurrent/ valueTotal *100).intValue();
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

    //thanks to:
    // https://blog.csdn.net/weixin_29053561/article/details/88564791 !
    // https://stackoverflow.com/questions/13444546/android-adt-21-0-warning-implicitly-using-the-default-locale !
    //keep 2 bit of the part of under 0, like 1.01234... equals to 1.01.
    @SuppressLint("DefaultLocale")
    public static String ReturnTwoBitText(double NeedToReturn){
        return String.format("%.2f", NeedToReturn);
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
}
