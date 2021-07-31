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
                        "【0.30.1版】2021.7.31\n" +
                        "【功能性更新】\n" +
                        "1.“比武台”更新：\n" +
                        " 1.1 支持调整挑战的boss等级（默认为用户等级），boss等级将会影响总评价，详情请于“回忆”-“图鉴”-“游戏公式”当中查看。\n" +
                        "    1.1.1 注意：输入的等级不得超出用户的±33级，否则会被重置为默认等级。\n" +
                        " 1.2 现在，“比武台”在调整boss回合数时，使用输入而非按钮调整。\n" +
                        " 1.3 现在，“比武台”在退出页面时，会记录用户最后输入的数值（不含boss能力），并在下次使用时自动载入，减少用户负担。\n" +
                        "2.为“敌临境”“比武台”页面增加了“boss能力图鉴”，可在顶栏点击“问号”查看。\n" +
                        "3.“修身馆”更新：\n" +
                        "  3.1 增加关闭显示“计时”的功能，开启后可以在计时的时候不再显示计时数值。可通过开关控制，默认关闭。\n" +
                        "  3.2 重置计时后，不再自动重新开始计时，而是恢复至初始状态，可由用户自主决定是否再次计时。\n" +
                        "4.为“成就”页面增加了普通成就的列表，方便用户查看。\n" +
                        "5.“回忆”-“个人”页面更新：\n" +
                        "  5.1 页面UI调整，去掉了多余元素。\n" +
                        "  5.2 现在页面只会显示属性的总值，具体构成需要点击“构成详情”按钮查看。\n" +
                        "6.“商贸居”页面代码和UI重写。\n" +
                        "  4.1 “商贸居”页面由于逻辑变动，不再支持根据积分数量设定商品数量的功能。取而代之的是，用户可以通过弹窗显示的滑动条选择购买数量。\n" +
                        "  4.2 “商贸居”机制调整：满级后仍然可以购买EXP，但是在当前持有的EXP大于1,000,000时无法购买。\n" +
                        "7.“工匠屋”更新：\n" +
                        "  7.1 在左侧选择物品时，可以看到物品的数量了。\n" +
                        "  7.2 “配方详情”现在会直接显示在UI右侧，不再需要点击按钮弹窗查看。\n" +
                        "8.“日程”页面更新：\n" +
                        "  8.1 现在，“日程”支持勾选之后自动删除的操作了，此功能默认关闭。\n" +
                        "【内容更新】\n" +
                        "1.为以下页面的部分内容追加翻译文本。\n" +
                        "  1.1 主页面\n" +
                        "  1.2 “望晨崖”\n" +
                        "  1.3 “广场”-“未开启游戏模式”提示。\n" +
                        "  2.“成就”页面更新：\n" +
                        " 2.1 “高难成就”新增3个：\n" +
                        "    2.1.1 ...是1000%！\n" +
                        "    2.1.2 いつも戦い\n" +
                        "    2.1.3 0/21/0\n" +
                        " 2.2 “普通成就”新增19个：“比武台评价”系列。\n" +
                        "它们的获得方法请您自行寻找~\n" +
                        "3.“统计”页面新增统计项：战斗失败次数。\n" +
                        "（注：新增的统计项要在此版本及以上完成的战斗才会计入，早期的数据不会记录，还请注意。）\n" +
                        "4.“敌临境”追加26~30层。（除30层外，26-29层HP相较旧版上调了约500点，以应对等级突破带来的提升。）\n" +
                        " 4.1 由于二阶能力未实装，30层boss的“卸力II”被替换为“考验”，同时HP下调至14800，以平衡难度。\n" +
                        "5.在“比武台”挑战未符合开始条件时，App会弹窗提示了。\n" +
                        "【内容调整】\n" +
                        "1.以下页面的UI样式调整：\n" +
                        "  1.1 “天赋社”\n" +
                        "  1.2 “比武台”\n" +
                        "  1.3 “广场”\n" +
                        "  1.4 “回忆”\n" +
                        "  1.5 “设置”\n" +
                        "  1.6 “试炼场”\n" +
                        "  1.7 战斗系统UI\n" +
                        "    1.7.1 HP和SD（护盾值）的显示合并，SD值的比例会覆盖显示在HP值的比例上方，同时，当前SD值会显示在当前HP的附近，以（+0）的格式显示。\n" +
                        "    1.7.2 原SD显示的UI不再显示，但组件本身仍会保留以用于未来的开发工作。\n" +
                        "    1.7.3 HP和SD显示的颜色变更。\n" +
                        "  1.8 “工匠屋”\n" +
                        "2.自该版本起，“望晨崖”挑战时将会消耗次数。次数可由“日程”-“签到”获取。每次“签到”可获取1次数。\n" +
                        "5.“储物箱”的物品名称字号缩小。\n" +
                        "5.删除了“修身馆”页面未使用的“帮助”按钮以节约空间。\n" +
                        "6.“剧情”-“主线剧情”-“序章”的个别表达修改。\n" +
                        "7.进入“广场”页面时，不再显示“未开启游戏模式”的提示，在此时直接禁用“炼金室”按钮的限制也一并取消，改为点击“炼金室”按钮时显示。\n" +
                        "8.修改了未完成“普通成就”时，成就名称显示的细节。\n" +
                        "9.从商店当中移除“Million Master”成就的出售。请通过点击关于“高难成就”按钮了解新的获取途径。\n" +
                        "10.更改了“个人”页面-“构成详情”的内容排版。\n" +
                        "11.调整了“伤害显示”的样式。\n" +
                        "【数值调整】\n" +
                        "1.“望晨崖（日常）”：\n" +
                        "积分产出：1000 + 难度 * 1500 → 5000 + 90 * 用户等级 * 难度\n" +
                        "bossHP值：用户等级 * 【随机数：11 + （难度 * 10） ~ 61 +（难度 * 8）】 → 用户等级 * 【随机数：11 +（难度 * 11）~ 61 +（难度 * 9）】\n" +
                        "2.“商贸居”部分商品单价调整：\n" +
                        " 2.1 “50EXP”，由2500积分上调至8500积分。\n" +
                        " 2.2 “2500EXP”，由225000积分上调至380000积分。\n" +
                        "【开发相关】\n" +
                        "1.通过优化载入流程，减少了主页面和“日程”页面的初始化用时。\n" +
                        "2.去掉了“日程”代码的非必要部分。\n" +
                        "3.去掉了战斗结算代码当中的重复部分。\n" +
                        "【bug修复】\n" +
                        "1.修复了“望晨崖（日常）”功能生成的boss奖励异常的bug。\n" +
                        "2.修复了“惧意”能力会使玩家的伤害数值异常变为0的bug。\n" +
                        "（正常情况是将伤害限制到boss最大HP的25%）。\n" +
                        "3.修复了购买（解锁）“限定商品”后，无法购买“普通商品”的bug。\n" +
                        "4.修复了购买（解锁）“限定商品”后，总价计算错误的bug。\n" +
                        "5.修复了“冒险”页面未适配刘海屏的bug。\n" +
                        "6.修复了“比武台”点击“Boss增强”字样会导致崩溃的bug。\n" +
                        "7.修复了“成就”页面“普通成就”数量显示不符的bug。\n" +
                        "8.修复了“个人”页面不会统计炼金和突破产生的属性加成的bug。\n" +
                        "9.修复了“题库编辑”在变更难度时如果不输入直接确定会导致崩溃的bug。\n" +
                        "10.修复了在“望晨崖”“敌临境”“比武台”多次重复开始战斗时会使返回栈异常的bug。\n" +
                        "（减少了“重复页面”的出现概率）\n" +
                        "11.修复了“等级突破”在突破后，显示积分数值格式不正确的bug。\n" +
                        "12.修复了“等级突破”在未满足任务条件时也可以突破的bug。\n" +
                        "13.修复了“行商集”可以在宝箱或钥匙数量不足时开箱的bug。\n" +
                        "14.修复了“工匠屋”“行商集”在未完全加载时退出并再次进入会引发崩溃的bug。\n" +
                        "15.修复了“残伤（LastHurt）”“恢复（Recover）”能力没有效果的bug。\n" +
                        "16.修复了合成“储物箱”当中没有的物品时，数量不正确的bug。\n" +
                        "17.修复了选择合成目标后，对话框的目标提示仍为null的bug。\n" +
                        "18.修复了合成物品时，即使所需消耗不足，也可以继续合成的bug。\n" +
                        "19.修复了即使题库为空也可以访问“编辑模式”页面和查找，从而引发崩溃的bug。\n" +
                        "20.修复了“比武台”初始化后需要修改任意项目才能触发计分，开始挑战的bug。\n" +
                        "21.修复了在未曾访问过“敌临境”页面时，先访问“个人”页面，会引起通关层数记录异常的bug。\n" +
                        "22.修复了偶发在“设置”添加题目后返回主页面，回答按钮仍被锁定的bug。\n" +
                        "23.修复了对boss的伤害值可以低于0的bug。\n" +
                        "24.修复了护盾结算机制使：护盾以攻击数值，而非最终伤害为准，进行结算的bug。\n" +
                        "（换言之，先前版本的护盾格挡伤害的效果数值，实际上是异常偏高的）\n" +
                        "25.修复了“比武台”的数据保存机制异常的bug。\n" +
                        "26.修复了“试炼场”返回按键显示不正确的bug。\n" +
                        "27.修复了战斗中回答错误会直接导致战斗失败的bug。\n" +
                        "28.修复了“比武台”在boss的HP设定为1时无法开始战斗的bug。" +
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