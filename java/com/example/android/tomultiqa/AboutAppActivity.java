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
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        //Window window = getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        //* these code are useless now, I keep it here for study reason.
        ImageView AppIconView = findViewById(R.id.AppIconView);
        AppIconView.setImageDrawable(ContextCompat.getDrawable(this,R.mipmap.ic_launcher_round));
    }

    //About App function.
    public void ShowAboutAppText(View view){
        //You need to use Edit Text software to edit text, and paste here to keep format.
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.ChangelogWordTran),
                "This part is only provided with Chinese.\n"+
                        "Make sure you have skill to read Chinese.\n"+
                        "\n" +//do not remove text above this line. these are language hint.
                        "【0.60.0版】2021.11.16\n" +
                        "【已知问题】\n" +
                        "1.部分情况下，积分获取统计的数值会出错，原因不明。\n" +
                        "2.“成就”页面改版后，获取“Million Master”成就的触发由于无处可去，因此将其安排在点击成就页面时触发，请注意。\n" +
                        "【功能性更新】\n" +
                        "1.“题库编辑”功能改进：\n" +
                        "现在，处于“题目/答案/提示”搜索模式下的“搜索”功能，支持模糊搜索了。（先前版本均为“精准搜索”）\n" +
                        "而“全选/难度”模式下仍为精准搜索。\n" +
                        "（注：“难度”模式不支持“模糊搜索”是因为，如果支持，就会出现搜索“0”或“1”时，会返回难度“10”的结果的bug）\n" +
                        "2.“剧情”页面功能改进：\n" +
                        "  2.1 新增“放映模式”：\n" +
                        "    2.1.1 现在，剧情文本可以像很多RPG游戏那样弹出窗口，逐段显示了。\n" +
                        "    2.1.2 使用方法：\n" +
                        "\t(1) 点击顶栏的“>”按钮。页面上就会出现一个灰色的窗口。\n" +
                        "\t(2) 点击窗口即可从头开始，逐段放映当前选中的剧情文本。\n" +
                        "\t(3) 此时，点击“上一页”/“下一页”会切换窗口显示的章节。选择完毕后，点击顶栏的“>”或点击窗口以刷新页面。\n" +
                        "\t(4) 点击窗口左上角的“时间”按钮即可弹出已经阅读过的文本回顾。\n" +
                        "\t(5) 点击窗口右上角的“X”按钮即可跳过放映并关闭窗口。\n" +
                        "（注意：在读完一章的所有内容或点击“跳过”按钮关闭窗口之前，“剧情”页面原有的阅读器将不会显示。）\n" +
                        "3.“天赋社”页面变更：\n" +
                        "  3.1 UI改进。\n" +
                        "  3.2 取消了天赋升级完成后的弹窗提示。\n" +
                        "  3.3 现在，不再需要输入来决定连续升级的次数，而是通过滑动条弹窗设定。\n" +
                        "（注：为方便选择，单次连续升级的最大上限由1000下调至100）\n" +
                        "4.“修身馆”支持后台计时。\n" +
                        "（注：如果App被系统杀后台，那么本次计时的所有数据均会丢失，还请注意。）\n" +
                        "5.“广场”“回忆”“设置”页面的按钮列表支持滚动了。\n" +
                        "6.“数据备份”页面改进：\n" +
                        "  6.1 在选择完目标文件进行“读取”或“写入”操作时，基本不会再卡在选择器页面了。\n" +
                        "  6.2 添加了可视化的进度提示。（注：由于部分步骤耗时较短，因此可能会导致进度来不及刷新（不显示）的情况，只要显示“完成”字样即为操作成功。）\n" +
                        "7.“答题错误”后弹窗的功能现在可在“附加功能”页面选择是否开启。（默认开启）\n" +
                        "8.“日程”页面功能改进：\n" +
                        "  8.1 在编辑、删除事项时，如果事项的文本过长（15个字符或3个换行符以上），就会被截断并以“...”代替。以减少文本过长导致UI无法完整显示的问题。\n" +
                        "（即便如此，如果您输入了过多行数的文本，比如10行以上，这个问题依然存在。后续可能会进一步改进。）\n" +
                        "  8.2 “添加事项”现在支持输入多行文本了。\n" +
                        "9.主页面/“收藏”页面在文本过长时，关闭键盘后输入框可以将页面折叠并滚动，以减少键盘遮挡内容的概率。\n" +
                        "10.“数据备份”功能更新：\n" +
                        "  10.1 支持备份笔记内容。\n" +
                        "【内容更新】\n" +
                        "1.“图鉴”页面改进：\n" +
                        "  1.1 页面改为独立的Activity，不再依附于“回忆”页面。\n" +
                        "  1.2 页面的UI更新。\n" +
                        "  1.3 “boss能力”部分，在显示能力信息时，会一并提供对应的“评价加成系数”了。\n" +
                        "（关于“评价加成系数”：详见“图鉴”-“游戏公式”部分）\n" +
                        "2.为“主页面”的“模式选择”对话框添加了帮助文本。\n" +
                        "3.“商贸居”在显示商品总价数值时会添加逗号了。\n" +
                        "4.为下列场景添加翻译：\n" +
                        "  4.1 为“行商集”对话框显示的物品类型添加了翻译。\n" +
                        "  4.2 为“天赋社”的“积分不足”弹窗内容添加翻译。\n" +
                        "5.“日程”页面改进：\n" +
                        "  5.1 为添加/编辑/删除“事项”的操作添加了动画。\n" +
                        "  5.2 改进了页面的刷新方式。\n" +
                        "【内容调整】\n" +
                        "1.下列页面的UI调整：\n" +
                        "  1.1“广场”\n" +
                        "  1.2“回忆”\n" +
                        "  1.3“设置”\n" +
                        "  1.4“日程”\n" +
                        "  1.5“成就”\n" +
                        "现在，您可以直接返回，而无需单独点击1.1-1.3页面内置的“返回”键了。\n" +
                        "（注意：这个效果对顶栏上的返回键无效，对其点击，依然只会返回上一级页面。）\n" +
                        "2.“望晨崖”页面的顶栏名称修正。\n" +
                        "3.删除了“回忆”-“图鉴”处的多余提示文本。\n" +
                        "4.“设置”-“工作模式”的页面现在和主页面共用一套实现，取消原有的页面。\n" +
                        "5.“工作模式”的选择对话框的按钮文本修正。\n" +
                        "6.调整了“行商集”价格变动对话框的内容。\n" +
                        "7.“题库编辑”页面的“移除此题”功能取消了非空限制，以方便用户移除内容不完整的题目。\n" +
                        "8.数值限制追加：\n" +
                        "  8.1 EXP，持有上限为：2,000,000；单次获取上限为：1,000,000。\n" +
                        "  8.2 积分，持有上限为：2,000,000,000；单次获取上限为：10,000,000。\n" +
                        "9.“工匠屋”在执行合成成功后，顶部的物品计数会消失，需要重新点击物品以显示。由此减少数据错误显示问题。\n" +
                        "10.调整了“游戏公式”部分的文本和细节。\n" +
                        "11.修改翻译错误及细节若干。（不会变更实际效果）\n" +
                        "12.“日程”页面调整：\n" +
                        "  12.1“添加事项”“编辑事项”的弹窗输入框字号缩小并限制最大行数以减少UI面积占用。\n" +
                        "  12.2 现在，“天数计时”的计时器只会在功能开启时显示了。 \n" +
                        "13.主页面和“收藏”页面的输入框的最大行数限制由1上调至5，超过时将会将内容折叠，可滚动查看。\n" +
                        "14.由于和输入框滚动功能冲突，取消点击键盘的Enter键触发题目判定的功能。\n" +
                        "【数值调整】\n" +
                        "1.“攻击力”天赋的加成数值由每级3点下调到每级2点，每级费用由135 + 等级 ^ 1.12升至135 + 等级 ^ 1.15。\n" +
                        "2.“暴击伤害”天赋的加成数值由每级0.2%下调到每级0.1%，等级上限由750升至1000，每级费用由200 + 等级 ^ 1.33下调到200 + 等级 ^ 1.29。\n" +
                        "3.普通bossHP的生成公式调整：\n" +
                        "附：x = 战斗胜利次数\n" +
                        "由：23 * (x + 1) * [14 + 0.1 * (x + 1) ]\n" +
                        "调整为：10 * (x + 1) * [11 + 2.2 * (x + 1) ]\n" +
                        "最终数值向下取整，最大值为int型上限。\n" +
                        "参考数值：\n" +
                        "初次战斗时的HP：324 》132；\n" +
                        "15次战斗后的HP：5741 》7392；\n" +
                        "30次战斗后的HP：12193 》24552。\n" +
                        "（由于本人水平不足，这些数值并非最终版本，不排除后续更改的可能。）\n" +
                        "【开发相关】\n" +
                        "1.代码显著变更：\n" +
                        "  1.1 新类：ResourceIO\n" +
                        "    1.1.1 类似于先前版本的ExpIO类，开发者可以使用它快速完成对用户积分和素材数量的操作。\n" +
                        "  1.2  新类：StoryLib\n" +
                        "    1.2.1 将原有的剧情文本由ValueClass移至此处以便于管理。\n" +
                        "    1.2.2 若需要自定义剧情文本，您需要在此处修改汇总各类故事的String数组元素。此后，开发者不再需要于StoryActivity.java当中设置元素的上限数，范围将由代码自动控制。\n" +
                        "    1.2.3 注意，由于“放映模式”以“\\n”（换行符）作为换页依据。因此，如果您希望您的文本在“放映模式”能够良好显示，请适当安排文本的行数和排版。\n" +
                        "  1.3 新类：ItemIO\n" +
                        "    1.3.1 类似于先前版本的ExpIO类，开发者可以使用它快速完成对用户物品相关的操作。\n" +
                        "  1.4 出于稳定性考虑，“统计”和“数据备份”页面的对应数据载入依然采用旧方式，不使用IO统一载入的新方式。\n" +
                        "  1.5 修改了项目当中多数的上述操作实现。\n" +
                        "  1.6 下列功能类改名：\n" +
                        "    1.6.1 AbilityList 》AbilityIO\n" +
                        "    1.6.2 ValueClass 》ValueLib\n" +
                        "    1.6.3 SupportClass 》SupportLib\n" +
                        "    1.6.4 所有基于SQLiteOpenHelper的类（……Basic）》……DbHelper\n" +
                        "2.改进了“天赋社”-“天赋升级”的实现，减少了不必要的UI刷新和逻辑，改进其性能。\n" +
                        "3.改进了“成就”页面的实现。\n" +
                        "4.弃用“剧情”-“载入阅读进度”功能。其数据不再使用。以下是弃用的SharedPreferences文件信息：\n" +
                        "文件名：\"StoryDatafile\"，数据标签：\"ProgressLoad\"，\"UserStoryProgress\"。\n" +
                        "5.SupportClass下的CreateWindowDialog()和StoryActivity下的类似方法存在一定差异，使用时请注意。详见方法的JavaDoc。\n" +
                        "6.SupportClass下的SharedPreferences快捷方法增加了同时读写单一文件内多个标签数据的方法，以减少新建对象的开支，并提升效率。\n" +
                        "（方法的示例可见主页面，IO类或备份页面的实现。）\n" +
                        "7.优化了主页面不必要的全局变量和逻辑。\n" +
                        "【bug修复】\n" +
                        "1.修复了如果在尝试进行“合成”时，配方当中存在未解锁的物品时，会立即崩溃的bug。\n" +
                        "2.修复了部分情况下，无法删除收藏题目的bug。\n" +
                        "3.修复了在购买“行商集”的宝箱和钥匙后，市场价格不变的bug。\n" +
                        "4.修复了没有输入购买/出售数时，依然会引发市场价变动事件的bug。\n" +
                        "5.修复了可以保存问题/答案为空的题目的bug。\n" +
                        "6.修复了由“行商集”初次获得一种物品时，其初始数量为0的bug。\n" +
                        "7.修复了可以在主页面处于“记录笔记”及“查看提示”状态下，通过键盘触发题目判定的bug。\n" +
                        "8.修复了在天赋连续/同时升级时，若“攻击力”天赋费用不足，会导致其他天赋升级即使费用足够也无法继续的bug。\n" +
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