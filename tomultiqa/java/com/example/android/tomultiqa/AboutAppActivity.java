package com.example.android.tomultiqa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ImageView AppIconView = findViewById(R.id.AppIconView);
        AppIconView.setImageDrawable(getDrawable(R.mipmap.ic_launcher_round));
    }

    //About App function.
    public void ShowAboutAppText(View view){
        //You need to use Edit Text software to edit text, and paste here to keep format.
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.AboutAppWordTran),
                "This part is only provided with Chinese.\n"+
                        "Make sure you have skill to read Chinese.\n"+
                        "\n" +//do not remove text above this line. these are language hint.
                        "【0.20.0版】2021.6.5 开发中\n" +
                        "【重要变更】\n" +
                        "1.App依赖库由android.support支持库迁移到androidX支持库。\n" +
                        "【功能性更新】\n" +
                        "1.增加了“推荐难度”的功能，可以根据题目和答案的长度来给出App认为合理的难度值。\n" +
                        "您只需在“难度”界面下开启此功能，App会在您输入题目和答案时自动统计其长度，并根据函数自动生成难度数值与移动难度选择滑动条。\n" +
                        "（注：出于尊重用户选择的考虑，这个功能默认关闭。）\n" +
                        "2.现在，“笔记”可以在关闭“文书”页面时自动保存了，不再需要手动点击保存。\n" +
                        "3.综合玩法更新：\n" +
                        " 3.1 App以往只有“积分”和“素材”这类“货币”系统，而在本次更新中，开发组为大家带来了“物品”系统。以下是更新详情：\n" +
                        " 3.2 新增“背包”系统，用户可于此处管理自己在游戏进程当中获得的物品。（开发中，85%）\n" +
                        "process：基本完成，功能正常，需要进行细节调整。\n" +
                        " 3.3“广场”下新增“行商集”页面，用户可于此处开启和交易自己在不同途径当中获得的宝箱和钥匙，通过钥匙开启宝箱，获取新的素材和随机奖励！（开发中，75%）\n" +
                        "process：基本完成，数据抽取机制工作正常，需要补充细节，展示可获得物品列表等收尾工作。\n" +
                        " 3.4  新增“合成”系统，用户可于此消耗素材和积分合成出不同的物品。（未开始）\n" +
                        "【内容更新】\n" +
                        "1.追加新的页面：“冒险”和“关于应用”\n" +
                        "原“广场”页签下的战斗相关入口移动到本页面下。方便统一管理和使用。\n" +
                        "原“设置”-“附加功能”下的“关于App”功能移动到独立的“关于应用”页面，并追加了访问App官方主页和下载页的功能。\n" +
                        "变更范围如下：\n" +
                        "“试炼场”“望晨崖”“敌临境”“比武台”\n" +
                        "2.“剧情”-“开发者漫谈”追加2篇。\n" +
                        "【内容调整】\n" +
                        "1.主页面UI变更：\n" +
                        " 1.1 原“设置”按钮功能移动到App的标题栏的“齿轮”按钮上。\n" +
                        " 1.2 原“设置”按钮位置变更为“冒险”页面按钮。\n" +
                        "2.微调了EXP计数器的UI。\n" +
                        "3.“剧情”-“主线”-篇目“主线1-2”的部分表达和文本变更。\n" +
                        "4.出于备份系统的兼容性考虑，“笔记”现在最大限制记录20行文本。\n" +
                        "5.去掉了“文书”页面废弃的“保存”按钮和“自动保存”开关。\n" +
                        "6.出于兼容性考虑，不再支持备份“笔记”内容。\n" +
                        "【开发相关】\n" +
                        "1.通过移除不必要的资源使App体积下降（当然，更新内容导致效果被抵消了）。\n" +
                        "【bug修复】\n" +
                        "1.修复了购买商品后，无法再次进行购买的bug。\n" +
                        "2.修复了特定情况下，达到等级上限后仍然能购买EXP的bug。 \n" +
                        "3.修复了在事先输入过购买数量的情况下，选择购买“成就”不会重置数量显示的bug。（实际效果无异常）\n" +
                        "4.修复了炼金等级效果在页面初始化时不显示的bug。（实际效果无异常）\n" +
                        "tomultiQA开发组",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
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