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
                        "【0.50.0版】2021.9.15\n" +
                        "【已知问题】\n" +
                        "1.如果使用和App当前语言不符的备份文件（比如：中文版的App导入英文版的备份文件），会导致“储物箱”数据读取异常的问题。\n" +
                        "（由于物品数据文本使用了当前语言保存，这个问题暂时无法修复。）\n" +
                        "2.能力“苏醒”的计时会略微提前于设定值（“苏醒”的回复效果会在大约13s时就开始生效），原因不明。\n" +
                        "【重要变更】\n" +
                        "1.“数据备份”功能仅向前兼容旧版本（0.50.0版以下）的备份文件。\n" +
                        "生成的新版备份文件导入旧版可能导致不明后果，请谨慎使用。\n" +
                        "2.下列功能添加了限制：\n" +
                        "  2.1 题目系统限制：可添加题目的最大数量为5,000条。\n" +
                        "  2.2 物品系统限制：“储物箱”最多同时存放2,500种物品。\n" +
                        "如果您在先前版本突破限制，新版的备份系统会拒绝导出数据。\n" +
                        "3.TargetAPI升至31（Android12）。\n" +
                        "【功能性更新】\n" +
                        "1.“修身馆”追加“计时历史”功能，在您完成一次计时（以点击重置按钮为准）后，App会记录您本次计时结束后弹出的报告。\n" +
                        "并按时间倒序保存下来，您可以在后续查阅记录。\n" +
                        "  1.1 由于设计问题，您只能够“查看”，“删除”或“复制”【全部】记录，而无法单独操作【某一条】记录。\n" +
                        "  1.2 出于性能考虑，当记录文本的长度大于40,000字符时，App将会弹窗提示。大于50,000字符时，记录将会被直接清空。请您及时备份。\n" +
                        "  1.3 出于减少重复保存考虑，“计时历史”只会在退出页面时保存，换言之，未退出页面前，无法刷新“计时历史”。\n" +
                        "2.“储物箱”功能更新：\n" +
                        "  2.1 可以在出售物品时，查看物品的持有数量了。\n" +
                        "  2.2 物品的描述文本可以滚动查看，以适应小屏幕设备。（不确定是否可用）\n" +
                        "  2.3 页面上方的页签功能正式开放，点击页签，将显示存储的不同类型的物品了。多种类型的物品不会再显示在同一页面当中了。\n" +
                        "（虽然现阶段根本就没有其他类型物品就是了）\n" +
                        "3.“数据备份”功能更新：\n" +
                        "  3.1 现在，备份功能支持备份“题库”与“储物箱”的数据了。\n" +
                        "  3.2 出于稳定性考虑，对数据库读写等新增操作不使用多线程优化，故“数据备份”的各项功能的耗时大幅增加。\n" +
                        "（取决于数据量，可能需要等待数秒至数十秒）\n" +
                        "4.“行商集”功能更新：\n" +
                        "  4.1 可以在“开箱”时查看宝箱和钥匙的数量了。\n" +
                        "  4.2 可以在“出售”时查看“钥匙”或“宝箱”的数量与当前市场价了。\n" +
                        "5.“敌临境”-“层数传送”功能更新：\n" +
                        "  5.1 追加标题翻译文本。\n" +
                        "  5.2 现在，对话框会一并显示已经解锁的可传送层数了。\n" +
                        "6.“题目设定”功能增加“清空文本”按钮功能和文本翻译，点击可以清空题目设定的输入。\n" +
                        "7.原“限时活动”更名为“事件”，并追加2项事件：\n" +
                        "  7.1 设备时间的每月1日，会发生“炼金室”的相关事件。\n" +
                        "  7.2 设备时间的每月28日，会发生“商贸居”相关事件。\n" +
                        "  7.3 对应的，为“炼金室”添加了顶栏按钮。\n" +
                        "8.“商贸居”增加功能：可以在选中商品的同时显示商品单价了。\n" +
                        "具体内容请您自行探索。\n" +
                        "【内容更新】\n" +
                        "1.追加新的boss能力：\n" +
                        "  1.1“苏醒（Wake）”：战斗状态下，当前题目答题用时超过15秒，boss将每秒回复1.5%最大HP，且回复量每秒上升0.1%最大HP的数值。\n" +
                        "（比武台评价：+34%）\n" +
                        "  1.2“破坏（Destruct）”：战斗状态下，当前题目答题用时超过30秒，题目将被强制刷新。此效果不消耗回合数。\n" +
                        "（比武台评价：+10%）\n" +
                        "2.“日程”页面更新：\n" +
                        "  2.1 添加“功能帮助”。\n" +
                        "  2.2 为部分图标添加点击动画。\n" +
                        "3.新增“兑换码”：\n" +
                        "  3.1“WipeQuestData”：清空题库数据。\n" +
                        "  3.2“WipeItemData”：清空“储物箱”数据。\n" +
                        "4.“剧情”-“开发者漫谈”追加1篇。\n" +
                        "1.boss能力“惧意（Fear）”：boss受到的伤害不高于最大HP的【25% 》15%】\n" +
                        "（比武台评价：+8% 》+10%）\n" +
                        "【内容调整】\n" +
                        "1.页面调整：\n" +
                        "  1.1“关于应用”页面按钮文本变更。\n" +
                        "  1.2“日程”页面左下角的“帮助按钮”现在转为显示“日程”页面新增的【功能帮助】。（原“事项设置”帮助内容转移到“事项设置”对话框内。）\n" +
                        "  1.3“设置”-“题目设定”的输入框及其字号缩小，以减少屏幕占用。\n" +
                        "  1.4“主页面”更改若干。\n" +
                        "    1.4.1 新增的图标解释：在boss的信息UI当中，“秒表”图标代指战斗剩余回合数。另外的“危险标志”图标暂未使用，敬请期待。\n" +
                        "    1.4.2 为部分View添加了最小宽度属性，以此减少在显示不同数值时的UI位移与闪烁，减少干扰。\n" +
                        "  1.5“储物箱”/“工匠屋”/“商贸居”的物品选择页面。\n" +
                        "  1.6“炼金室”页面。\n" +
                        "  1.7“设置”-“工作模式”页面\n" +
                        "2.机制调整：\n" +
                        "  2.1 在“行商集”完成对“宝箱”和“钥匙”的购买后，会立即变动价格，以增加随机性。\n" +
                        "3.翻译变更：“Boss死亡次数”》“战斗胜利数”\n" +
                        "3.关闭了未使用的“敌临境”-“场地效果”按钮，出于排版美观考虑暂不删除。\n" +
                        "4.主页面的部分按钮在初始化后不再默认屏蔽。\n" +
                        "5.取消了“储物箱”没有物品时，显示“空无一物”的项目和弹窗提示。\n" +
                        "6.“天数计时”的设定追加了以下限制：\n" +
                        "  6.1“计时单位”的字数限制最大6个字符（3个汉字），超过会被重置为默认的“Sol.”\n" +
                        "  6.2“目标天数”最大为3660天（10年+10天），超过会被重置为默认的30天。\n" +
                        "7.“收藏”页面在未刷新出任何题目时，无法再呼出确认删除的对话框了。\n" +
                        "8.顶栏按钮的文本变更：“功能帮助”》“帮助”。\n" +
                        "【开发相关】\n" +
                        "1.删除了多余的一些代码。\n" +
                        "2.小幅修改了“日程”页面的载入流程。\n" +
                        "【bug修复】\n" +
                        "1.修复了在题库没有题目的状态下，点击“提示”按钮会解锁“确定回答”按钮的bug。\n" +
                        "2.修复了“敌临境”的通关层数初始记录可能是第1层的bug。（默认应为第0层--即未曾挑战）\n" +
                        "3.修复了因为空题库导致点击“确定回答”按钮引发崩溃的bug。\n" +
                        "4.修复了特定情况下，反复切换“主页面”和“日程”页面可以反复签到的bug。\n" +
                        "5.修复了“数据备份”功能在先进行“预览”，后尝试“导入”时，会一直保持“预览”状态，从而无法载入数据的bug。\n" +
                        "6.修复了“数据备份”功能在反复点击“导出”时，即使指定将数据保存在不同文件，也会使数据异常累积在文件当中的bug。\n" +
                        "7.修复了多处帮助文本的部分描述错误。\n" +
                        "8.修复了能力“腐蚀”的描述错误：对此Boss的攻击力【数值 》 比例】下降25%，数值向下取整。实际效果无变更。\n" +
                        "9.修复了多次导入“题库”数据时，可能会导致显示的题目数量和实际不一致的bug。\n" +
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