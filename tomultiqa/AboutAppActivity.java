package com.example.android.tomultiqa;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        ImageView AppIconView = findViewById(R.id.AppIconView);
        AppIconView.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_launcher_round));
    }

    //About App function.
    public void ShowAboutAppText(View view){
        //You need to use Edit Text software to edit text, and paste here to keep format.
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.AboutAppWordTran),
                "This part is only provided with Chinese.\n"+
                        "Make sure you have skill to read Chinese.\n"+
                        "\n" +//do not remove text above this line. these are language hint.
                        "【0.21.0版】2021.6.20\n" +
                        "【功能性更新】\n" +
                        "1.增加“收藏题目”功能，您现在可以将希望重点学习的题目进行收藏，并在新增的“收藏”页面下专门复习这些收藏的题目。\n" +
                        " 1.1 功能入口：主页面-顶栏“爱心”按钮。\n" +
                        " 1.2 添加收藏：在刷新出一个题目后，点击主页面的“收藏题目”按钮进行收藏。\n" +
                        " 其余使用技巧和方法请点击“收藏”页面顶栏的帮助按钮了解。\n" +
                        "2.支持在主页面直接变更“App工作模式”。\n" +
                        "（入口：主页面右下角的“浮动按钮”处。）\n" +
                        "（注：出于用户习惯和稳定性考虑，“设置”处的“工作模式”选项会继续保留。）\n" +
                        "【内容更新】\n" +
                        "1.为主页面布局变动的场景（App模式变更）添加了动画。\n" +
                        "2.为“附加功能”页面的部分表达追加翻译。\n" +
                        "3.“附加功能”-“功能帮助”按钮追加帮助文本。\n" +
                        "【内容调整】\n" +
                        "1.右下角的“浮动按钮”的功能由“刷新问题”变为“更改工作模式”。\n" +
                        "2.“附加功能”页面的UI微调。\n" +
                        "（问题将在答题确认时自动刷新。）\n" +
                        "【bug修复】\n" +
                        "1.修复了“等级突破”界面无法打开的bug。\n" +
                        "2.修复了版本号显示异常的bug。\n" +
                        "3.修复了“关于APP”页面顶栏未适配的bug。\n" +
                        "4.修复了物品系统关于数据库的抛弃方法引用不正确的bug。\n" +
                        "5.修复了对“天数计时”功能的“计时单位”的更改无法生效的bug。\n" +
                        "tomultiQA开发组",
                getString(R.string.ConfirmWordTran));
    }//end of About App function.

    //Open App Official website on Github.
    //url: https://github.com/Ricky158/tomultiQA/releases !
    public void OpenAppWebsite(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage("You are going to App Official Download Page on Github.\n"+
                "Do you confirm to Open a Website?\n" +
                "(Sometimes, the website may not accessible.)");
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Ricky158/tomultiQA/releases"));
                    startActivity(browserIntent);
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of App Official website function.
}