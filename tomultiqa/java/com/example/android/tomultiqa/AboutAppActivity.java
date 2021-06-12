package com.example.android.tomultiqa;

import android.content.Intent;
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
                        "【0.20.0版】2021.6.12\n" +
                        "【重要变更】\n" +
                        "1.App的包名变更为com.android.tomultiqa，这使得10.4.1/0.10.5版本的App不再支持覆盖升级，非常抱歉。\n" +
                        "2.App依赖库由android.support支持库迁移到androidX支持库。\n" +
                        "【功能性更新】\n" +
                        "1.“炼金等级”现在支持自动连续升级了。\n" +
                        "2.在“商贸居”购买物品完毕后，会弹窗显示积分变化和消耗量。\n" +
                        "3.追加了伤害显示功能。默认开启，可在“设置”-“附加功能”当中关闭。\n" +
                        "4.取消了回答正确后弹出toast的设定，改为在计数器上限时显示颜色来提示。以减少长时间的视觉干扰。\n" +
                        "5.“修身馆”功能更新：\n" +
                        " 5.1 现在支持记录计时器被暂停的时长，并会在计时器暂停时自动刷新。\n" +
                        " 5.2 计时器的“进度条”则会显示【工作中】时长占【总时长】的比例了。\n" +
                        " 5.3 可以选择计时器重置时，是从【工作中】开始，还是从【休息中】开始了。\n" +
                        " 5.4 计时器重置时，App会弹窗提供本次计时的报告。\n" +
                        "6.现在，如果题库内没有题目，将不再允许点击“确定回答”按钮和“刷新”按钮，以减少App崩溃。\n" +
                        "7.追加了实验性的“备份”系统，支持将App数据以TXT文件备份到/data/com.android.tomultiQA/file/BackUp/目录下。（beta版）\n" +
                        "8.“等级突破”系统追加！\n" +
                        "【注意】\n" +
                        " 8.1 自0.12.0版本起，用户的等级在达到一定程度后，EXP会累积而无法升级，需要通过等级突破的考验才能解锁升级权限。\n" +
                        " 8.2 用户的初始等级上限下调至50级，更高的等级上限需要后续解锁。\n" +
                        " 8.3 在此版本以前，等级已超过50级的用户，离您当前等级【最近】的等级上限将会对您生效。\n" +
                        "（注：最高等级上限仍为300级不变。）\n" +
                        "9.App工作模式机制变更：\n" +
                        " 9.1在App开启“专注模式”后，“广场”功能的入口将会被改为“修身馆”的入口。（为保持内容排版，按钮文本不会变更）\n" +
                        " 【注意】9.2 现在，“普通模式”下也不再能够使用下列功能：“敌临境”“望晨崖”“炼金室”“比武台”。\n" +
                        "10.增加了“推荐难度”的功能，可以根据题目和答案的长度来给出App认为合理的难度值。\n" +
                        "您只需在“难度”界面下开启此功能，App会在您输入题目和答案时自动统计其长度，并根据函数自动生成难度数值与移动难度选择滑动条。\n" +
                        "（注：出于尊重用户选择的考虑，这个功能默认关闭。）\n" +
                        " 10.1 计算公式：（题目长度 + 答案长度）^ 0.57。\n" +
                        "11.“笔记”功能更新：\n" +
                        " 11.1 现在，“笔记”可以在关闭“文书”页面时自动保存了，不再需要手动点击保存。\n" +
                        " 11.2 “文书”-“笔记”页面的“清空文本”功能增加了对话框确认，防止误操作。\n" +
                        " 11.3 现在可以通过点击“文书”页面标题栏的“复制”按钮将笔记复制到剪贴板了。\n" +
                        "12.综合玩法更新：\n" +
                        " 12.1 App以往只有“积分”和“素材”这类“货币”系统，而在本次更新中，开发组为大家带来了“物品”系统。以下是更新详情：\n" +
                        " 12.2 新增“背包”系统，用户可于此处管理自己在游戏进程当中获得的物品。\n" +
                        " 12.3“广场”下新增“行商集”页面，用户可于此处开启和交易自己在不同途径当中获得的宝箱和钥匙，通过钥匙开启宝箱，获取新的素材和随机奖励！\n" +
                        " 12.4 “广场”下 新增“工匠屋”（合成系统）页面，用户可于此消耗素材和积分合成出不同的物品。\n" +
                        " 12.5 该系统处于测试阶段，需要在“设置”-“实验性功能”当中开启。获得的物品暂无实际用途，还请知悉。\n" +
                        "13.增加“自动清除回答”功能。开启后，用户确定回答之后，刷新题目时会一并清除已经输入的回答文本。此功能默认关闭。\n" +
                        "【内容更新】\n" +
                        "1.“商贸居”“修身馆”“纪录”页面的相关文本追加翻译。\n" +
                        "2.对于“刘海屏”的机型，会在刘海部分同步App标题栏的颜色了。\n" +
                        "3.“敌临境”开放21-25层。\n" +
                        "4.追加了新的兑换码（指令）。\n" +
                        "5.顺便追加了“文书”-“笔记”页面的部分翻译。\n" +
                        "6.“剧情”阅读器更新。\n" +
                        " 6.1 新增“剧情类型”-- 轶事（Tale.）\n" +
                        "相较于主线和手记，轶事的文笔非常随意，长度也飘忽不定，希望能够大家提供一些不一样的娱乐。\n" +
                        " 6.2 以下“剧情类型”的名称变更：\n" +
                        "“主线剧情（MainStory）> 主线（Main.）”\n" +
                        "“开发者漫谈（DeveloperStory）>手记（Dev.）”\n" +
                        " 6.3 追加新的1篇“轶事”--《贵族和炼金术》\n" +
                        " 6.4“开发者漫谈”追加2篇。\n" +
                        " 6.5 阅读器的字体大小下降。\n" +
                        " 6.6 阅读器UI微调。\n" +
                        "7.战斗系统更新：\n" +
                        " 7.1 追加“等级压制”机制，Boss每与用户相差1级，用户的输出总伤害变动3%（伤害可被扣至负数）。\n" +
                        "Boss等级较高，则受到伤害下降，反之则提高。\n" +
                        " 7.2 用户伤害值的计算改为全程浮点计算，只在结束计算时转化为整数扣除HP和显示。\n" +
                        "8.“纪录”“敌临境”页面追加部分翻译。\n" +
                        "9.追加新的“兑换码”，\n" +
                        " 9.1 “maxrank”：将等级突破及其附加属性升至满级。\n" +
                        " 9.2 “unlockvhquest”：将“敌临境”的“已通关层数”设为最大（解锁所有存在的层数）。\n" +
                        " 9.3 附上早期版本已经追加的兑换码：\n" +
                        " 9.3.1“greedisgood”：使用户获得输入数量的积分。\n" +
                        " 9.3.2“whosyourdaddy”：使用户的攻击力追加输入数值（注：重复更改将会覆盖效果！）\n" +
                        " 9.3.3 “/xp”：给予用户输入数量的EXP。\n" +
                        "10.“图鉴”-“游戏公式”内容更新。\n" +
                        "11.追加新的页面：“冒险”和“关于应用”\n" +
                        "原“广场”页签下的战斗相关入口移动到本页面下。方便统一管理和使用。\n" +
                        "原“设置”-“附加功能”下的“关于App”功能移动到独立的“关于应用”页面，并追加了访问App官方主页和下载页的功能。\n" +
                        "变更范围如下：\n" +
                        "“试炼场”“望晨崖”“敌临境”“比武台”\n" +
                        "12.“成就”页面追加翻译内容。\n" +
                        "13.“游戏公式”增加了“自动生成难度”的公式。\n" +
                        "14.在非【游戏】模式下点击“冒险”按钮，会弹出提示。\n" +
                        "【内容调整】\n" +
                        "1.“广场”页面未开发完成的“任务（委托所）”入口关闭，避免误操作。\n" +
                        "2.在战斗时，主界面上的“EXP计数器”将会消失以节省屏幕空间。\n" +
                        "3.进入“广场”时，未开启游戏模式的提示文本更改。\n" +
                        "4.出于平衡性目的，炼金要素“平衡”提供的持续回合由 3 / 每要素增加到 5 / 每要素。\n" +
                        "5.在达到当前等级上限时，不再显示升级所需经验，且等级会被标红。直到等级突破到下一阶段为止。\n" +
                        "6.取消了“只有在持有EXP为正数时答错才会扣除EXP”的设定，现在无论EXP为何值均会执行扣除。\n" +
                        "7.现在，App当中数值较大的数字会被显示为带有逗号分隔的格式，便于查看。\n" +
                        "8.购买时积分不足的提示英文文本变更。\n" +
                        "9.商贸居展示的“Million Master”商品名格式变更。\n" +
                        "10.主页面UI变更：\n" +
                        " 10.1 原“设置”按钮功能移动到App的标题栏的“齿轮”按钮上。\n" +
                        " 10.2 原“设置”按钮位置变更为“冒险”页面按钮。\n" +
                        "11.微调了EXP计数器的UI。\n" +
                        "12.“剧情”篇目“主线1-2”“开发者漫谈1.2”的部分表达和文本变更。\n" +
                        "13.出于备份系统的兼容性考虑，“笔记”现在最大限制记录20行文本。\n" +
                        "14.去掉了“文书”页面废弃的“保存”按钮和“自动保存”开关。\n" +
                        "15.“成就”显示格式略有变更。\n" +
                        "16.“天赋”满级后的显示效果略有调整，更加清晰。\n" +
                        "17.答题获取积分变更：\n" +
                        "计算公式（向下取整）：题目难度 * 2 + 连对数 ^ 1.6 → 题目难度 * 10 + 连对数 ^ 2.6\n" +
                        "同时，追加10000积分/题的上限。\n" +
                        "【优化相关】\n" +
                        "1.删除了部分无用代码。\n" +
                        "2.删除了无用的按钮控件。\n" +
                        "3.整理了主页面的代码。\n" +
                        "4.移除了开发时未使用的支持库，减少存储占用。\n" +
                        "（当然，由于更新内容较多，多出的容量抵消了这个效果。）\n" +
                        "5.为主页面追加了数据恢复的代码，减少了主页面数据刷新不及时的可能。\n" +
                        "6.整理并抽象化了部分主页面的代码。\n" +
                        "7.精简了主页面导入App工作模式的代码。\n" +
                        "8.将App模式作为静态值定义在子类“ViewClass”当中。\n" +
                        "9.精简了“敌临境”用户通关层数导入时不必要的代码。\n" +
                        "10.减少了答题积分奖励的重复运算。\n" +
                        "11.题目处理优化：将题库和伤害显示设置的读取移至初始化并保存。\n" +
                        "【bug修复】\n" +
                        "1.修复了“望晨崖”无法解锁扫荡功能的bug。\n" +
                        "2.修复了“脆弱”和“诅咒”能力同时存在时，“脆弱”能力会生效的bug。\n" +
                        "（设定上，大多数负面能力的效果是优先于正面能力的）\n" +
                        "3.更换了“修身馆”的计时器实现代码，修复了计时器无法实际停止的bug。\n" +
                        "4.修复了“修身馆”计时奖励积累速率显示的异常bug。\n" +
                        "5.修复了战斗时暴击存在异常的bug。\n" +
                        "6.修复了普通boss的回合数只有1（正常为10）的bug。\n" +
                        "7.修复了多阶段boss复活时可能产生数据错误的bug。\n" +
                        "8.修复了普通boss可于其他类型boss战斗中生成的bug。\n" +
                        "9.修复了“敌临境”战斗失败时，也可能记录通关层数的bug。\n" +
                        "10.修复了“天赋社”天赋效果显示小数位过长的bug。\n" +
                        "11.修复了“主页面”用户暴击率/暴击伤害显示小数位过长的bug。\n" +
                        "12.修复了“阅读”进度条的初始位置异常的bug。\n" +
                        "13.修复了“成就”-不同种类的“普通成就”显示不一的bug。\n" +
                        "14.修复了进入战斗时，数据初始化时可能产生数据错误的bug。\n" +
                        "15.修复了“考验”“护盾”“腐蚀”等战斗开始时触发的能力会在每回合结束后触发的bug。\n" +
                        "16.修复了“敌临境”用户通关层数数值过高时，boss数据加载异常的bug。\n" +
                        "17.修复了购买商品后，无法再次进行购买的bug。\n" +
                        "18.修复了特定情况下，达到等级上限后仍然能购买EXP的bug。 \n" +
                        "19.修复了在事先输入过购买数量的情况下，选择购买“成就”不会重置数量显示的bug。（实际效果无异常）\n" +
                        "20.修复了炼金等级效果在页面初始化时不显示的bug。（实际效果无异常）\n" +
                        "21.修复了“等级成就”第4级的文本错误。（达成条件无异常）\n" +
                        "22.修复了“主线剧情”-“主线1-2”篇目的标题缺失问题。\n" +
                        "23.修复了“炼金属性”在回合耗尽后会被多次扣除的bug。\n" +
                        "\n" +
                        "\n" +
                        "【已知问题】\n" +
                        "1.获得的素材量是正常的10倍（原因未知，不过不影响游戏，暂时无法修复）\n" +
                        "tomultiQA 开发组",
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