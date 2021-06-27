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
                        "【0.22.0版】2021.6.27\n" +
                        "【重要变更】\n" +
                        "1.App默认的启动屏幕变更为“日程（List）”页面。\n" +
                        "【功能性更新】\n" +
                        "1.新增“日程”页面。新页面接管了过去设置在主页面的“天数计时”功能。并且，您现在可以在App内记录待办（TODO）事项了。\n" +
                        "【内容更新】\n" +
                        "1.为“工作模式”和“敌临境”的部分文本追加翻译，并调整了部分不正确表达。\n" +
                        "【内容调整】\n" +
                        "1.“剧情”-“序章1~序章4”重写，合并为1篇（“序章”）。标题对应更改。\n" +
                        "2.题库为空的提示表达调整。\n" +
                        "3.调整了“敌临境”-“挑战奖励”对话框的文本格式。\n" +
                        "【bug修复】\n" +
                        "1.修复了“备份”功能版本号不正确的bug。\n" +
                        "2.修复了“行商集”页面中“钥匙”价格显示不正确的bug（数值本身正常）。\n" +
                        "3.修正了多处错误英语拼写。\n" +
                        "4.修复了在主页面内部的“工作模式”调整至“游戏模式（Game）”时，偶发异常显示Boss战斗UI的bug。\n" +
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