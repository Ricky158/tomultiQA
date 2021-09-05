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
                        "【0.40.0版】2021.9.5\n" +
                        "【已知问题】\n" +
                        "1.新版的“数据备份”系统在生成备份文件时，每在同一路径生成1次，就会在“文件选择”界面留下1次记录，目前原因未知。不过，这个问题不会额外消耗您设备的存储容量。\n" +
                        "  1.1 请您仔细辨认保存时间和文件名，避免读取早期数据。\n" +
                        "  1.2 注：您可以通过复写已有的备份文件来阻止生成新的文件记录。\n" +
                        "【重要变更】\n" +
                        "1.由于“数据备份”机制的变更，先前版本的备份文件需要您手动移出 /storage/emulated/0/Android/data/com.android.tomultiqa/file目录。这是因为新版的备份系统无法直接读取这一目录。\n" +
                        "换言之，旧版的“数据备份”，在Android11限制访问/data时，实际上无法发挥任何作用。因为一旦备份文件被移出/data，那么就无法再次移入，从而使备份无法继续。\n" +
                        "因此，使用Android11及以上设备的用户，请您务必升级至此版本，以保证数据安全。\n" +
                        "注意：部分Android11定制系统的设备会阻止用户访问/data文件夹，对此：\n" +
                        "  1.1 您可以升级至此版本，并执行“数据导出”，以保存当前状态的数据。但旧文件会被弃用，且其不会被删除。\n" +
                        "  1.2 或者，使用电脑导出文件，再进行操作。\n" +
                        "建议您将该目录下的备份文件移动到系统的 /storage/emulated/0/Download文件夹（也就是根目录下的Download文件夹），以方便访问。\n" +
                        "备份文件的内容和原理与0.31.0版本没有区别，文件可继续兼容。\n" +
                        "2.启用了android studio的编译优化，大幅缩减了Apk文件体积。但由于缺乏测试，可能会引发问题，还请您注意。\n" +
                        "【功能性更新】\n" +
                        "1.新增了签到时显示名言的功能。\n" +
                        "  1.1 当前版本下，内置的中英名言共20句，每次签到随机抽取。\n" +
                        "  1.2 此功能由“设置”-“附加功能”-“天数计时”-“显示今日运势”控制。\n" +
                        "2.现在，“比武台”挑战开始前，会弹出确认对话框了。同时，用户可通过此对话框预览boss将会获得的各能力效果。\n" +
                        "  2.1 同时，移除“比武台”页面顶栏的帮助按钮，以减少内容重复。\n" +
                        "3.增加“编辑事项”功能，和“删除事项”的使用方法类似。可以编辑已有的事项文本。\n" +
                        "4.现在，“签到”功能会在点击进入主页面的按钮时自动完成，并弹出通知提示。\n" +
                        "（请将应用的“通知权限”和“通知权限”-“通知类别”-“签到提示（SignChannel）”打开以接收通知）\n" +
                        "  4.1 注：在MIUI等定制系统上，通知出现后，第2次（及以上）进入系统通知栏查看App通知时，通知的内容可能会无法展开。\n" +
                        "（解决方案：如果通知没有下拉标志，您可以通过长按或点击通知来尝试将其展开。）\n" +
                        "  请您及时查看通知以避免错过信息展示。\n" +
                        "  表现参考：https://bleepcoder.com/cn/cwa-app-android/760262479/notification-doesn-t-expand-when-dragging\n" +
                        "5.增加了显示事项数量的计数器。\n" +
                        "6.重写了“数据备份”机制。\n" +
                        "  6.1 新功能：可以自由选择文件路径（除/data等特殊文件夹外）作为储存位置。\n" +
                        "  6.2 新功能：您可以保存多份备份文件，并且可以自由选取读取其中任意一份。如果您不需要多份文件，您可以选中已有的备份文件，复写其内容。\n" +
                        "  6.3 新功能：各备份文件的名称可以自定义，不再局限于规定的名称。\n" +
                        "  6.4 注意：由于使用了系统的文件选择器，请您务必不要选择备份文件以外的其他文件，否则可能会引发数据错误或崩溃，逻辑故障等问题。\n" +
                        "7.题目系统添加“提示”功能：\n" +
                        "  7.1 在编辑题目时，可以输入对于当前题目的提示文本。\n" +
                        "  7.2 在主页面答题时，点击“灯泡”图标可以在答案输入框处显示题目的提示文本，再次点击图标可恢复已经输入的答案内容。（效果和“记录笔记”功能相似）\n" +
                        "  7.3 由于和“记录笔记”功能采用了一样的显示方式，因此，这两个功能不能够同时使用（文本不可同时显示）。\n" +
                        "  7.4 本次改版兼容旧版题库，以往的题目会被视作“没有提示”。您也可以在“题库编辑”功能当中为已有题目添加和修改提示文本。\n" +
                        "  7.5 提示文本是选择性设置的，设定题目时若不输入则视作“没有提示”。\n" +
                        "8.新增了“事项备份”的功能：\n" +
                        "  8.1 您可以点击页面当中的“复制”图标，您的所有事项的内容都会以文字形式显示在弹出的对话框当中，您可以手动选取部分内容复制，也可以点击对话框的“复制”键，一键复制所有内容到剪贴板。\n" +
                        "【内容更新】\n" +
                        "1.为下列内容追加翻译文本：\n" +
                        "  1.1 “今日运势”的结果\n" +
                        "2.追加新的boss能力：\n" +
                        "  2.1 “活跃（Active）”:战斗状态下，每经过1秒，均有3%概率使boss回复10%的最大HP，并刷新你正在回答的题目。此效果不消耗回合数。\n" +
                        "（比武台评价：+9%）\n" +
                        "3.新增“兑换码”：\n" +
                        "  3.1 “GoodMorning”:重置签到状态。\n" +
                        "4.修改与新增了“主线剧情”第1章，第2章的部分文本内容及情节，以完善世界观。\n" +
                        "5.现在，“数据编辑”功能在切换结果时，首个结果和最终结果之间可以贯通了。\n" +
                        "（即：位于第1个结果时，再次向前则会到最终结果；位于最终结果时，再次向后会到第1个结果）\n" +
                        "6.“回忆”页面增加显示“炼金效果”的剩余回合。\n" +
                        "【内容调整】\n" +
                        "1.下列页面的UI调整：\n" +
                        "  1.1 主页面\n" +
                        "    1.1.1 附注：由于功能增加导致位置不足，原有的答题界面“答案”标题所在行的按钮被替换为了图标，以下是各个图标指代的功能（从左到右）：\n" +
                        "    （1）“灯泡”图标：查看提示（新增）\n" +
                        "    （2）“爱心”图标：收藏题目\n" +
                        "    （3）“文件”图标：记录笔记\n" +
                        "    （4）“对勾”图标：确定回答\n" +
                        "  1.2 “日程”页面（启动页）\n" +
                        "  1.3 “题库编辑”页面\n" +
                        "  1.4 “望晨崖（日常）”页面\n" +
                        "2.“剧情”页面，底部的滑动条调整以方便触摸，屏蔽故障的“保存阅读进度”功能。\n" +
                        "  2.1 此功能存在设计问题，无法使用。且由于优先级较低，后续修复排期待定，也有可能放弃。\n" +
                        "3.主页面UI，日程页面UI。\n" +
                        "4.boss能力图鉴的部分描述（实际效果无变更）\n" +
                        "5.取消了在没有事项时，toast的弹出提示。\n" +
                        "6.“暴击伤害”一词的英文翻译变更：“Critical Damage”变为“Critical DMG”，以适应UI变更。\n" +
                        "7.取消了在完成事项删除时的提示toast。\n" +
                        "8.“编辑事项”的“取消删除”按钮改为“取消”。\n" +
                        "9.“事项”-“自动删除”功能现在不会在日程页面处于“删除”或新增的“编辑”模式下生效。\n" +
                        "10.对应“主页面”按钮变更，调整了部分提示文本的表述。\n" +
                        "11.取消了“记录笔记”功能开启时的toast提示。\n" +
                        "12.单次出售“宝箱”和“钥匙”的最大数量限制为100，且在每次出售之后会立即变更价格。\n" +
                        "13.删除了版本未实装的“Boss能力”图鉴信息，避免体验不佳。\n" +
                        "14.重新编排了“敌临境”标题栏的“Boss能力图鉴”。\n" +
                        "15.删除了“游戏公式”内容当中的“比武台能力相关”部分，避免与“能力图鉴”重复，同时便于维护。\n" +
                        "【开发相关】\n" +
                        "1.调整了“主页面”和“日程”页面的载入流程以修复部分数据不同步的bug。（载入速度基本不变）\n" +
                        "2.下列内容独立为一个类，方便开发者编辑。同时替换了部分重复实现。\n" +
                        "  2.1 Boss能力\n" +
                        "  2.2 Exp相关\n" +
                        "【bug修复】\n" +
                        "1.修复了boss素材获取量异常过高的bug。\n" +
                        "2.修复了“收藏”页面清空数据后，再次请求刷新题目时会引发崩溃的bug。\n" +
                        "3.修复了“炼金术/比武台”成就无法显示的bug。\n" +
                        "4.修复了“比武台”最高评价记录异常的bug。\n" +
                        "5.修复了“比武台”评价数值上限与数据类型不符的bug。\n" +
                        "（即：使用了long型数值却在计算时使用int的问题）\n" +
                        "6.修复了“炼金室”重置配方时，炼制的成功率不变的bug。\n" +
                        "7.修复了题库被清空或题目已被全部删除时，返回设置页面，再次尝试进入“编辑题库”页面可以成功的bug。\n" +
                        "8.修复了“删除事项”的弹窗标题为“添加事项”的bug。\n" +
                        "9.修复了“事项帮助”内容不完整的bug。\n" +
                        "10.修复了在页面切换时，特定情况下可以重复签到操作的bug。\n" +
                        "11.修复了在部分情况下，“编辑事项”功能无法被点击的bug。\n" +
                        "12.修复了部分情况下，“行商集”页面初始化时会直接卡死的bug。\n" +
                        "13.修复了个别翻译文本，内容文本拼写错误的bug。\n" +
                        "14.修复了部分情况下，“题库编辑”功能会无法切换搜索结果的bug。\n" +
                        "15.修复了部分情况下，“题库编辑”功能在切换不同结果时，会出现结果编号显示和内容不符的bug。（实际内容正常）\n" +
                        "16.修复了“望晨崖”扫荡不消费次数的bug。\n" +
                        "17.修复了“比武台”数据由备份功能导入时，数据可以输入不符要求的值的bug。\n" +
                        "18.修复了“个人”页面统计总攻击力时，未计入炼金效果加成的bug。\n" +
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