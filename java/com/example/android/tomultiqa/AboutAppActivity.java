package com.example.android.tomultiqa;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //these code are now useless now, I keep it here for study reason.
//        //make StatusBar is same color as ActionBar in Activity.
//        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
//        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.rgb(38,198,218));
//        //底部导航栏
//        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
//        //main method below.
        //* these code are now useless now, I keep it here for study reason.
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
                        "【0.51.0版】2021.9.19\n" +
                        "【功能性更新】\n" +
                        "1.“题库编辑”功能更新：\n" +
                        "  1.1 UI变更，搜索框移至顶栏，并更改了“搜索模式”的选择器。\n" +
                        "  1.2“搜索”功能正式开放。现在，您可以根据不同的搜索条件来搜索题库当中的题目了。\n" +
                        "  1.3 “未找到结果”的提示文本变更。\n" +
                        "注意：与先前版本不同，由于方法源代码限制，现在在“全选”模式下，也需要输入内容来开始搜索了。\n" +
                        "输入的内容在“全选”模式下不作为搜索依据。（但内容不能仅由空格组成）\n" +
                        "2.“工匠屋”功能更新：\n" +
                        "  2.1 确认合成时，会显示当前正在合成的数量了。\n" +
                        "3.战斗当中，可以实时显示炼金效果的剩余回合数了。\n" +
                        "（注1：如果没有炼金效果，会固定显示为0回合）\n" +
                        "（注2：显示在回合数图标的左侧，图标为“烧瓶”）\n" +
                        "【内容调整】\n" +
                        "1.下列页面的UI调整：\n" +
                        "  1.1“回忆”-“个人”页面\n" +
                        "  1.2 主页面\n" +
                        "2.下列情况的文本调整：\n" +
                        "  2.1 非“游戏”模式下，点击“冒险”按钮的提示文本。（使其与“炼金室”的提示统一）\n" +
                        "  2.2 “数据导出”/“数据导入”功能的提示文本。\n" +
                        "3.出于方便管理的需要，物品在数量为0时不会再被删除，而是继续作为物品之一保留。\n" +
                        "【数值调整】\n" +
                        "1.用户升级所需的EXP量调整。（注：“等级”指“当前用户等级”）\n" +
                        "（等级 ^ 1.6 + 15 - 等级 ^ 1.1）》（等级 ^ 1.6 + 200 - 等级 ^ 1.3）\n" +
                        "通过公式调整，增加基础消耗量，并减少附加消耗，以平滑升级曲线。同时不会过大幅度变化EXP的总需求量。\n" +
                        "【bug修复】\n" +
                        "1.修复了在App处于“修身馆”页面时，反复将App在前台和后台之间切换，会重复保存多次计时历史的bug。\n" +
                        "2.修复了完成“物品出售”操作后，选择“储物箱”物品时，会引发崩溃的bug。\n" +
                        "3.修复了在合成物品时，可以选择合成0个的bug。\n" +
                        "4.修复了部分情况下，“炼金效果”的回合数消耗异常的bug。\n" +
                        "5.修复了“数据导入”功能在导入物品环节的故障。\n" +
                        "tomultiQA开发组",
                getString(R.string.ConfirmWordTran));
    }//end of About App function.

    //Open App Official website on Github.
    //url: https://github.com/Ricky158/tomultiQA/releases !
    public void OpenAppWebsite(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.OfficalWebsiteHintTran));
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