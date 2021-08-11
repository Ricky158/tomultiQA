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
                        "【0.31.0版】2021.8.11\n" +
                        "【重要变更】\n" +
                        "1.出于备份方便考虑，修改了“比武台”boss数据保存的文件，因此旧版本保留的boss数据将不再生效，需要重新设置。（最高评价依然保留）\n" +
                        "2.出于备份稳定性考虑，修改了“备份文件”当中版本号的规范。旧版（0.31.0版以下）的备份文件可能会在后续版本放弃兼容。\n" +
                        "【功能性更新】\n" +
                        "1.现在，在主页面回答错误时，可以在显示答案的弹窗当中将当前题目加入收藏了。\n" +
                        "2.增加了“自动大小写”的功能，开启后，用户输入的答案当中的文本将会被自动替换为统一的大写/小写形式。该功能默认关闭。\n" +
                        "比如：“aBC”可以被替换为“ABC”或“abc”，取决于用户希望全部大写还是全部小写。\n" +
                        "（注1：由于国家或地区的语言设定不同，这个功能的效果可能会不符合预期）\n" +
                        "3.增加了boss的等级提示，当用户挑战的boss等级较高时，boss的等级会被染色，根据boss高于用户的等级数，颜色有所差别：\n" +
                        "  3.1 相差30级及以上：黑色\n" +
                        "  3.2 相差20级及以上：红色\n" +
                        "  3.3 相差10级及以上：绿色（不是黄色的原因是对比度太低，不方便查看）\n" +
                        "其余情况保持默认颜色。\n" +
                        "4.现在，“工匠屋”的合成完毕弹窗当中会提示合成的数量了。\n" +
                        "5.现在，回答完毕后且光标焦点在回答框时，可通过点击虚拟键盘的【完成】按钮进行判定（替代“确认回答”按钮的功能）。\n" +
                        "6.“编辑题库”功能追加“删除本题”功能，现在，您可以删除编辑器当前显示的题目，而无需删除整个题库了。\n" +
                        "【内容更新】\n" +
                        "1.“备份”功能新增可备份内容4条：\n" +
                        "  1.1 战斗失败次数\n" +
                        "  1.2“比武台”boss设定等级\n" +
                        "  1.3“比武台”boss设定HP\n" +
                        "  1.4“比武台”boss设定回合数\n" +
                        "本次更新兼容先前的备份文件。\n" +
                        "2.为下列内容追加翻译文本：\n" +
                        "  2.1“自动删除事项（Auto Delete）”功能及其帮助\n" +
                        "3.多数的“辅助功能”现在都在“收藏”页面生效了。（除“伤害值显示”以外）\n" +
                        "4.对应更新了部分功能的帮助文本。\n" +
                        "【内容调整】\n" +
                        "1.下列页面的UI调整：\n" +
                        "  1.1 “设置”-“附加功能”页面\n" +
                        "  1.2 “回忆”-“图鉴”页面\n" +
                        "  1.3 “主页面”（含答题界面和战斗界面）\n" +
                        "  1.4 “工匠屋”\n" +
                        "  1.5 “编辑题库”\n" +
                        "2.“天数计时”-“目标天数”的设定方式由滑动选择改为直接输入。\n" +
                        "3.“比武台”-“最高评价”数值显示追加逗号。\n" +
                        "4.为“行商集”-“开箱”功能添加了单次最高500个的上限，避免过长时间的卡顿产生。\n" +
                        "5.现在，“宝箱”和“钥匙”两项道具最多同时持有1,000,000个，以防止数据溢出。\n" +
                        "【开发相关】\n" +
                        "1.删除或优化了下列机制的非必要代码，以提高效率：\n" +
                        "  1.1“自动删除事项”功能\n" +
                        "  1.2 战斗初始化\n" +
                        "2.优化了下列机制的执行效率：\n" +
                        "  2.1 积分获取\n" +
                        "  2.2 素材获取\n" +
                        "  2.3 boss能力机制\n" +
                        "  2.4 连对记录机制\n" +
                        "  2.5 等级计算机制\n" +
                        "3.以下页面的返回机制变更：\n" +
                        "“收藏”“广场”“商贸居”“试炼场”，\n" +
                        "以修复偶发的数据错误问题。\n" +
                        "4.在App当中，系统“手势提示线（小白条）”的背景颜色改为透明以改善视觉效果。\n" +
                        "5.提供了部分供“屏幕阅读”辅助功能使用的“内容标签”。\n" +
                        "6.自当前版本起，正式版的apk文件去掉了测试库，以减少体积。\n" +
                        "【bug修复】\n" +
                        "1.修复了“输入的等级不得超出用户的±33级，否则会被重置为默认等级”的限制未生效的bug。\n" +
                        "2.修复了“备份”在多次导出数据时，第2次起写入的内容无效的bug。\n" +
                        "3.修复了“个人”页面会在“回忆”页面初始化直接打开的bug。\n" +
                        "4.修复了“工匠屋”页面显示物品数量功能无法工作的bug。\n" +
                        "5.修复了“工匠屋”合成时消耗数量异常的bug。\n" +
                        "6.修复了“行商集”的价格变更弹窗显示格式不正确的bug（内容无误）。\n" +
                        "7.修复了“工匠屋”合成完毕后获得物品数量异常的bug。\n" +
                        "8.修复了“编辑题库”-“清空题库”功能的确认弹窗无效的bug。\n" +
                        "（换言之，无论确认与否，功能都会生效）\n" +
                        "9.修复了“收藏”页面可以在收藏为空时进入的偶发bug。\n" +
                        "10.修复了“编辑题库”功能在“更改题目难度”时，会使所有具备相同难度的题目被一并更改的bug。\n" +
                        "11.修复了“编辑题库”功能显示搜索结果的弹窗格式不正确的bug。\n" +
                        "12.修复了“望晨崖”的扫荡完毕弹窗当中，括号显示不正确的bug。\n" +
                        "13.修复了在事项被删除后，没有其他事项时，“日程”页面的UI不会自动刷新的bug。\n" +
                        "14.修复了可以在“突破任务”未完成时继续突破的bug。\n" +
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