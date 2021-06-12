package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        SetLanguageHint();
        InitializingProgressLoad();

        final SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        PageMoveBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                SaveReadProgress();
                LoadStoryText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                SaveReadProgress();
                LoadStoryText();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                SaveReadProgress();
                LoadStoryText();
            }
        });

        //initializing SeekBar.
        PageMoveBar.setMax(MainStoryLimit - 1);
    }


    //switch function.
    private void InitializingProgressLoad(){
        Switch ProgressLoadSwitch = findViewById(R.id.ProgressLoadSwitch);
        SharedPreferences SwitchInfo = getSharedPreferences("StoryDatafile", MODE_PRIVATE);
        ProgressLoadSwitch.setChecked(SwitchInfo.getBoolean("ProgressLoad",true));
        if(ProgressLoadSwitch.isChecked()){
            CurrentStoryNumber = SwitchInfo.getInt("UserStoryProgress",1);
            LoadStoryText();
        }
    }

    public void ChangeSwitchState(View view){
        Switch ProgressLoadSwitch = findViewById(R.id.ProgressLoadSwitch);
        SharedPreferences SwitchInfo = getSharedPreferences("StoryDatafile", MODE_PRIVATE);
        SharedPreferences.Editor editor= SwitchInfo.edit();
        editor.putBoolean("ProgressLoad", ProgressLoadSwitch.isChecked());
        editor.apply();
    }//end of switch function.


    //story system.
    String StoryType = "Main";
    //limit number of story pages.
    static int MainStoryLimit = 6;
    static int DeveloperStoryLimit = 4;
    static int SubStoryLimit = 1;
    static int TaleStoryLimit = 1;
    int CurrentStoryNumber = 1;

    public void ChangeStoryType(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        RadioGroup StoryTypeChoose = findViewById(R.id.StoryTypeChoose);
        RadioButton MainStoryChoose = findViewById(R.id.MainStoryChoose);
        RadioButton DevelopStoryChoose = findViewById(R.id.DevelopStoryChoose);
        RadioButton TaleStoryChoose = findViewById(R.id.TaleStoryChoose);

        int ModeCheckedId;
        ModeCheckedId = StoryTypeChoose.getCheckedRadioButtonId();
        if (ModeCheckedId == MainStoryChoose.getId()){
            StoryType = "Main";
            PageMoveBar.setMax(MainStoryLimit - 1);
        }else if(ModeCheckedId == DevelopStoryChoose.getId()){
            StoryType = "Develop";
            PageMoveBar.setMax(DeveloperStoryLimit - 1);
        }else if(ModeCheckedId == TaleStoryChoose.getId()){
            StoryType = "Tale";
            PageMoveBar.setMax(TaleStoryLimit - 1);
        }
        //change the reading progress.
        CurrentStoryNumber = 1;
        PageMoveBar.setProgress(CurrentStoryNumber - 1);
        SaveReadProgress();
    }

    public void NextPage(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        if(CurrentStoryNumber < MainStoryLimit && StoryType.equals("Main")){
            CurrentStoryNumber = CurrentStoryNumber + 1;
        }else if(CurrentStoryNumber < DeveloperStoryLimit && StoryType.equals("Develop")){
            CurrentStoryNumber = CurrentStoryNumber + 1;
        }else if(CurrentStoryNumber < TaleStoryLimit && StoryType.equals("Tale")){
            CurrentStoryNumber = CurrentStoryNumber + 1;
        }else{
            CurrentStoryNumber = 1;
        }

        PageMoveBar.setProgress(CurrentStoryNumber - 1);
        SaveReadProgress();
        LoadStoryText();
    }

    public void LastPage(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        EditText StoryText = findViewById(R.id.StoryText);
        if(CurrentStoryNumber > 1){
            CurrentStoryNumber = CurrentStoryNumber - 1;
        }else if(StoryText.getText().toString().equals("")){
            CurrentStoryNumber = 1;
        }else if(StoryType.equals("Main")){
            CurrentStoryNumber = MainStoryLimit;
        }else if(StoryType.equals("Develop")){
            CurrentStoryNumber = DeveloperStoryLimit;
        }else if(StoryType.equals("Tale")){
            CurrentStoryNumber = TaleStoryLimit;
        }

        PageMoveBar.setProgress(CurrentStoryNumber - 1);
        SaveReadProgress();
        LoadStoryText();
    }

    //main story showing method.
    //Use Windows NotePad to edit Story Text, and copy it to here.
    @SuppressLint("SetTextI18n")
    private void LoadStoryText(){
        TextView ChapterShowView = findViewById(R.id.ChapterShowView);
        switch (StoryType) {
            case "Main":
                ChapterShowView.setText(CurrentStoryNumber + " / " +  MainStoryLimit);
                PrintStoryText(1,"序章1 应用商店开始的冒险\n" +
                        "打败了\"生活\"这个反派.勇者成为了人们口口相传的英雄.但他并没有沉醉于人们对他功绩的庆贺和敬仰当中,而是独自一人坐在山巅之上,回忆着自己的冒险之旅.最初,自己是怎么成为一位勇者的呢?\n" +
                        "......想当初,自己还是一个爱玩的孩子.有天,也许是想找个游戏玩.他点进了\"应用商店\".在一个偏僻的栏目介绍当中,他结识了这个APP.\n" +
                        "\"游戏式练习\",他被这个宣传挑起了好奇心.对他而言,学习是违背自己的天性的.这世界上,真的有所谓的\"寓教于乐\"吗?怀着试一试的心态,他下载了APP......");
                PrintStoryText(2,"序章2 与少女的匆匆会面\n" +
                        "他果断地点进了APP,突然,他感觉到有什么变得不一样了.反应过来,自己似乎来到了另一个地方.这里的道路不像城市,反倒像他印象里偏僻地方的土路.\n" +
                        "\"我...我这是在哪?\"正这样想着,有一个声音告诉他:\"这里是APP里的世界,你是这个世界的勇者.这个世界需要你的力量来拯救!做题吧,勇者......\"那个人没说两句就被主人公打断,\"等等,我来到这人生地不熟,身无一物.按照套路,新手上路不是应该整点什么的吗?\"\n" +
                        "\"啊,这...这不是一般的冒险嗷~你的智力就是你的武器.你要对战的,是那些你或熟悉,或陌生的题目.从这些智慧的结晶当中,寻找零碎的线索,拼凑出这个世界的真相啊,少年!\"\"...那个,你是希望我按返回键还是删除掉你的进程来说'再见...啊不,应该是'再也不见'？\"\n" +
                        "但声音的演说显然没有打动眼前的少年.\"真是的...那这样可以了吧!\"突然,少年觉得自己的手臂上感觉到了什么.\n" +
                        "回头,他看到了一位少女.她整个身子都靠在主人公身上,\"我想你留下来.\"她的语气有些低落.\n" +
                        "少年有些不太敢相信自己的眼睛,于是他试探道:\"那个,抱上来的时候没什么感觉呢......\"\"诶?\"少女一楞,随后当然是喜闻乐见的巴掌.\n" +
                        "\"啪!\"\"啊...不是梦啊...\"\"当然不是!我是在很认真地和你说呢!这可是......等等,有些不对,那个...嗯,看来这次是没法继续了.下次再见!\"\n" +
                        "\"诶不是...\"少年眼前景色变换,瞬间,他又回到了自己的房间.手机上,只显示着\"tomultiQA\"停止运行的错误提示.正当他摸不着头脑时,房门被人敲响了,\"XX,你看你考成什么样?!\"这个声音,一听就是妈妈的了.\n" +
                        "\"哎.\"少年轻叹,又看了一眼手机,\"难道是幻觉吗?可是...\"少年还能感到自己脸上的火热.\"XX!\"妈妈叫少年名字的声音更大了,少年关掉手机,走向了客厅......");
                PrintStoryText(3,"序章3 勇者的执念\n" +
                        "“啊！不是我说你，现在谁家孩子还能像你一样考这个数，不是妈妈想多说什么...”母亲欲言又止，“如果你这样下去的话，以后你要怎么一个人活下去？！”母亲眼角流出了泪，少年紧闭着嘴，一言不发，“学习的话，我...我会努力的。”\n" +
                        "少年思索许久，回答了这样一句话。那样就好了吧，少年这样想着。\n" +
                        "可是，母亲显然是没有接受：“你每次都和我这样说，可是这些，还有...”她由拿出一份复习资料，“还有这个，妈妈给你报的补习班，如果你真的努力了，为什么会这样？”母亲的回答没有父亲那样的气势，却字字打在少年的心上，\n" +
                        "“那我呢？”“嗯,什么？”“那我呢！我们之间商量过多少次，可那有用吗？你有什么时候愿意接受我说的话，没有，不管是补习班也好，学习也好，讨论的结果只能是我的妥协和您的胜利，您和我之间有过真正的和谐相处吗？”少年终于忍不住道出了自己的心声。\n" +
                        "母亲没有回答，又看了沙发上坐着的父亲，父亲没有说话，只是手上又点了一根烟。母亲回过头来，说：“那你长大以后会知道的。”\n" +
                        "还是这一句，少年听了很多的话，不过这次，想起刚才的奇遇，他没有反驳母亲，而是默默从客厅走开了，回到房间的路上，少年想着，那不是幻觉，总有一天他会证明自己。\n" +
                        "“实践是检验真理的唯一标准。”那位大人说的话，是他在历史课上记得最深刻的一句。");
                PrintStoryText(4,"序章4 转身，进发\n" +
                        "关上门，少年却迟迟不敢点击那个异世界的入口......他的理智思考着这一切。\n" +
                        "“这个APP，还有刚才看到的东西，会是幻觉吗？”这时，他回想到下载APP时的标语“游戏化练习”，这就像梦一样，少年记得小时候，梦想着学习能够像游戏一样，既能够获得快乐，也能够获得知识。学习本来应该是快乐的事情。\n" +
                        "可是，随着时间的推移，他又对儿时的梦想感到怀疑。\n" +
                        "其实，对少年而言，真正的压力不在学习本身，而在于人们的期望。他是个“很优秀”的孩子，至少“人们”是这样说的。\n" +
                        "但是，他愈发感到，人生正在变成一场看不到尽头的马拉松。大家，不论是父母，亲戚，同学。朋友......都在一条跑道上或快或慢地前进着。\n" +
                        "他看着大家，总觉得有种什么力量催促他也和大家一样前进。这种力量几度让他感到愤怒，无助，疲劳。\n" +
                        "夜深人静时，床上的枕头也会流下的泪水打湿。他感到了自己的无力，尽管自己已然尽力奔跑，但身后的那股力量却未曾停止过对他的折磨。\n" +
                        "即使他想停下，生活也未曾向他放低姿态。\n" +
                        "这一步，他想过很久，却迈不出去。\n" +
                        "犹豫再三，他还是打开了手机，回味着那个相遇的瞬间。那是海边的沙滩，是爽朗的海风。一个让人宁静的地方。\n" +
                        "少年似乎感觉到有什么在呼唤他，那是来自大陆的脉动，来自世界的暖意。轻声告诉他，他有一份使命，用一份力量，战一个痛快。\n" +
                        "“出发吧！”“诶？”少年定睛一看，一条通知从手机里弹出，又是一条“相信你会成为人人称颂的英雄！”\n" +
                        "少年想将通知划开，但是通知就像黏在屏幕顶端一样不肯离开。他想，这肯定是那个女孩的恶作剧吧。\n" +
                        "“那就陪你任性一回吧！”\n" +
                        "这个“你”，不仅是在称呼少女，更是在唤醒沉睡在心底的自己。\n" +
                        "右手一点，光芒四射。少年感到周围的景色在波动，\n" +
                        "“咔嚓！”门锁锁上了，墙上时钟的秒针仿佛被冻结住，一动不动。少年笑了，因为他明白——\n" +
                        "冒险已然开始。");
                PrintStoryText(5,"主线1-1 立志，启程\n" +
                        "白光一闪，少年就从房间里消失了。\n" +
                        ".......再次醒来，少年只觉得头有些发痛，“奇怪，明明上次还不会这样.......”少年发现自己正躺在一个宽敞的房间里的木床上。\n" +
                        "“您醒啦？我就知道您会回来。”床边，有一位少女弯下腰来看着他。\n" +
                        "“‘知道’是什么意思？”少年对她的说法感到好奇。“当然，因为您是被‘邀请’的人哦~话说，在我见过的人里，您还是第一个这么冷静的呢。”\n" +
                        "少年从床上直起身来，少女正欲扶起少年，房间外的阳光就从少女让开的缝隙里透射进来。“诶...抱歉，会晒到你吗？”少女发现不对，赶忙小跑去将窗帘拉了起来。\n" +
                        "少年从床上坐起，打量着周围的一切。这是一个木制的小屋，从屋子的地板到墙壁都透露出一种自然的芳香。显然，房间的主人有好好的打理着这一切。\n" +
                        "“那个，你...你还没有回答我的问题呢。”少年突然想起自己的目的，少女张开嘴说话，但是.......不知为何，少年什么有用的信息都没有听到，耳边只有“嗤嗤”的噪音。\n" +
                        "“不对！”少年这样想着。顷刻，少女似乎说完了，噪音也消失了。“.......就是这样...好了，我也告诉了你我的名字，那么你的名字是...”\n" +
                        "没等少女说完，少年抢答道，“你就叫我‘勇者大人’吧！”“啊...是...”少女稍微被少年的情绪吓到了，在稳定了情绪之后，她说道“那...勇者大人，出发去冒险吧！”\n" +
                        "“冒险？”少年猛然发现，“对啊！这是‘游戏化’的世界，但是...”“那学习呢？”他提问道。“可谁说，‘冒险’就不能让人学习呢？”少女还特意歪了一下头。“作为初到大陆的冒险者，要先接受一些磨炼才能开始伟大的冒险哦。”\n" +
                        "少女说完，不由分说的拉起了少年的手，“出发吧！”正当少女要拉着少年出门时，“等等！”少年突然打断，“我刚才没有听清，能再说一遍你的名字吗？”\n" +
                        "少女有些生气，“勇者大人没有记住我的名字吗?真是不靠谱......好吧，再说一次，我叫做...”“来了！”少年心里暗想着，果然，噪音马上就出现了。\n" +
                        "但是，这回少年可没有坐以待毙。而是寻找着噪音的来源，在屋外的广阔海岸，地平线开外的地方，树立着一座高塔，它仿佛直通天地。可是，在高塔的周围，原本苍蓝的天空却显现出阴暗而恐怖的色彩。\n" +
                        "“勇者大人？您在看那座塔吗？”\n" +
                        "“啊，是的。”\n" +
                        "“那就是您将来的目标哦！”\n" +
                        "“啊？”\n" +
                        "少女牵起了少年的手，“我明白，您现在有很多很多的疑惑，但是，我会引导您的成长，陪伴您的冒险，您可以，相信我吗？”少女露出了美丽的笑容。\n" +
                        "风起了，但是却不是湿润而温暖的海风，而是从海岸后方的山岭上传来的冷风。它严酷而冷峻，仿佛想将两人推向那个恐怖的高塔。\n" +
                        "不过，少年下定决心，他不会让恐怖和黑暗笼罩这片大陆，“相信我，这里永远会是和谐安宁的圣地。”少年突然弹出这么一句，但是少女并没有嘲笑少年的志向，她只是说道：“愿神明护佑您的道路，我最亲爱的勇者大人。”");
                PrintStoryText(6,"主线1-2 风吹白花\n" +
                        "下午，少女正想陪着少年前往村庄的委托看板。在二人即将出发时......\n" +
                        "“那个，嗯......”少年想要叫住少女，问问她些什么。但是，很尴尬的问题摆在了少年的面前：即使少女之前又说过了一遍自己的名字，但是少年仍然是什么都没有听到。\n" +
                        "“那边的...女孩”少年就差没有捂着脸说出这句话了，而少女似乎是知道少年要叫她，停了下来。\n" +
                        "“怎么了？”不同于之前见面时她的笑容，这次，她的脸上带着一些无奈的神色。看着少年发窘的样子，少女开了口：“是又不记得我的名字了吗？”\n" +
                        "“啊...是的。”少年不敢直视她，害怕自己一次次的提问消磨掉她的耐心。\n" +
                        "可是，事情却不像他所想的那样，少女并没有生气，而是问起了原因来，“您可以告诉我，不记得我名字的原因是什么吗？之前的‘引导’，都没有发生这样的现象。‘’\n" +
                        "“嗯，其实是，”少年组织着语言，思考着怎样向面前一无所知的少女解释这一切。可思考的时候，那个“噪音”，却隐隐约约地干扰着他的思绪。\n" +
                        "“你在说出自己的名字的时候，就会...什么也听不见。”少年隐瞒了“噪音”的存在，因为他猜测，“噪音”是在阻止自己说出真相。\n" +
                        "“滋滋...”突然，沉默许久的少女开口。随之而来的就是刺耳的“噪音”。“是这样吗？刚才我又说了一遍自己的名字。”\n" +
                        "“嗯。”少年有些吃惊，她似乎很容易就猜到了自己的想法。\n" +
                        "“这样啊，看来您惹上了很大的麻烦呢。”少女的脸色非常严肃，“干扰现实当中行为的存在，也只有......”少女看向远方，“只有‘生活’了。”\n" +
                        "“‘生活’？”少年对少女的说法有些不太理解，“那是...什么？”\n" +
                        "“所谓‘生活’，就是创造出那座纵贯天地的高塔的存在。”少女解释道，“它是支配情绪和命运的存在，也是人们信仰的神明之一。自从那座高塔出现以来，大陆上就出现了很多的消极和负面事件。与此同时，也传出更多因为‘霉运’而死伤的例子。这些线索都将罪状指向了‘生活’，它也就渐渐失去了人们的信仰，成为了人们的口中的‘邪神’。”\n" +
                        "“没有人出面阻止它吗？”少年提问道。\n" +
                        "“当然是有的，但是因为人神的巨大差异，结果大多都不尽如人意。其中，”少女顿了一会，“也不乏......被‘引导’的勇者。”\n" +
                        "听到这里，少年感到些许不安，“勇者？”，\n" +
                        "少女看出来了他的心思，继续说道：“勇者的责任是维护正义，为了承担这样的责任，许多人付出了巨大的代价。但是我们，‘引导者’也很无奈。一般人根本没有战斗能力，而勇者就是我们对抗邪恶的最大依仗。况且，还记得吗，勇者大人？这是个怎样的世界？”\n" +
                        "“‘游戏化’的世界。”\n" +
                        "“嗯...所以，我最初‘邀请’您的目的，其实不仅仅在于让您能够享受游戏，还有一些，自己的私心。”少女低下了头，语气里慢慢带上了一些委屈。“我希望，有更多像您一样的英雄，能够挺身而出，拯救大陆和人民于水火之中。”\n" +
                        "“您知道吗，我了解到您被‘生活’的力量所干扰，既兴奋又不安。我很高兴您的实力能够让高高在上的神明也会注意着您，但又担心您会像以前的人们一样，被它无情的抹杀......其实您也可以选择......”\n" +
                        "少女正欲继续，但少年止住了她的话语。“好了，我既然选择了来到这里，就做好了觉悟。毕竟，你也说，这是勇者的责任。”\n" +
                        "“嗯！”少女的脸上卸下了精神的负担。“我也会好好地‘引导’您，和您携手，拯救世界的。啊，对了......”少女说着话的时候，突然跑开，\n" +
                        "正当少年有些摸不着头脑时，她回来了。手里握着一朵盛开的白花，\n" +
                        "“这是......？”少年说道。“花？”\n" +
                        "“是的，这是只在春天盛开的鲜花。”少女慢慢抬起头来，“从今往后，您就叫我‘白花’吧。即使我的真名没有办法告诉您，但我相信，您一定会有听到我名字的一天。”少女微笑着。\n" +
                        "意外地，这次，“噪音”没有出现。“真是奇怪啊，我在异世界遇见了一个少女，可是我们连彼此的真名都不曾了解，就要一起冒险了......”少年心想着，\n" +
                        "“那以后请多多指教咯，白花......”\n" +
                        "少女凑上前来，靠近少年耳边，\n" +
                        "“哎？”\n" +
                        "“啾。”");
                break;
            case "Develop":
                ChapterShowView.setText(CurrentStoryNumber + " / " +  DeveloperStoryLimit);
                PrintStoryText(1,"1 初衷与命名\n" +
                        "最近，我实在是水不出主线剧情（大雾），于是突发奇想开个新坑谈谈这个APP，以及围绕着它的世界观。\n" +
                        "那就开始吧！\n" +
                        "1.1 初衷：\n" +
                        "最初，我开发这个APP的目的仅仅在于锻炼自己的政治问答题，因为那些知识点实在是太长了，而且考试还要默写原文，天天在本子上默写不是一般的累手。\n" +
                        "所以，我就根据“重复记忆”的思想开发了这个APP。\n" +
                        "最初，它的功能非常简单，就是只有设置题目和问答。\n" +
                        "当时我的使用方法就是将要背诵的原文故意空出一部分，然后在输入答案将它补全。（类似试卷上的填空题）。\n" +
                        "不得不说，这个“技术手段”还是很有效的，我真的依靠它记住了一些知识点。\n" +
                        "后来我就想，不如扩大它的功能吧，于是这个APP的开发就成为我高中时期周末的消遣之一。\n" +
                        "慢慢的变成了今天的样子。\n" +
                        "1.2 关于APP的取名：\n" +
                        "当时是在上课的时候摸鱼（笑），然后突然，脑子里闪过一个创意，想要做一个有学习意义的APP。\n" +
                        "我当时深受一个叫做“人升”的APP启发，它提出的“游戏化”理念深刻地打动了我。\n" +
                        "这个观念提倡将人生的很多事情想象为一场RPG游戏。而自己在完成自己的目标的同时，自己也像RPG当中的勇者一样变强，最终征服困难，走上巅峰。\n" +
                        "打定这个主意之后，我想要做一个像它一样的APP。\n" +
                        "为了差异化，我将“人升”APP的功能进行了研究。它是一款游戏化的TODO应用。\n" +
                        "在游戏当中，“时间”和“日常”的概念非常重要，因为玩家的行为很多时候都是基于时间的指导。\n" +
                        "于是这个APP就保留了“天数计时”（当时的名称还是neta《火星救援》的sol.计数器）的系统。\n" +
                        "“问答”是这个APP的着力点，因为其初衷如此，我也希望它能始终继承这一理念。\n" +
                        "“tomultiQA”这个名称的“to”和“QA（Question & Answer）”由此而来。\n" +
                        "至于“multi”，则来自学习当中的“重复记忆”。因为，即使是游戏化的学习，也是无法逃脱大量且重复的题目练习的。\n" +
                        "所以，这三个词语的组合，就构成了这个APP的核心理念。也就有了这个看着有点怪，也很难读的名称。\n" +
                        "今天的故事就到这里。");
                PrintStoryText(2,"2 UI和游戏化设计\n" +
                        "好的，今天的故事，或者说“漫谈”的主题，就是“UI和游戏化设计”。\n" +
                        "书接上回，在定下APP的基调之后，当然就是着手开发了，首当其冲的内容就是UI设计。\n" +
                        "一个问答APP的基本界面很简单，一个文本框显示问题，一个文本框输入答案，在加一个“确定回答”按钮就好了。\n" +
                        "但是，这时我想到了一个使用上的一个问题，不像电脑，手机或者平板等安卓设备的键盘都是虚拟的，而且会显著地占用屏幕空间。\n" +
                        "那么，如果将输入框放在APP界面的下方，就会在键盘弹出和收起当中浪费时间与消磨耐心。\n" +
                        "因此，我为自己立下了这个APP的第1条设计准则：输入框和回答按钮的高度不低于多数设备键盘的高度。即使到今天，也是如此。\n" +
                        "这个设计可以让用户不需要收起键盘就可以打字，并点击回答。\n" +
                        "第2条准则就是开发者必须使用自己的产品。\n" +
                        "一个APP，在开发中的多数情况下，开发者就是它唯一的用户。把自己放在用户的位置，考虑问题，才能优化APP质量。\n" +
                        "第3条准则就是简洁，不过我得承认，相较于前面的准则，这一条我做的还不是特别好。\n" +
                        "随着APP的演进，功能的增加必然会挤占有限的屏幕空间。所以，在开发过程中，为了在有限的空间内挤下内容，我采用了“伸缩”式的内容生成。\n" +
                        "比如说，“专注模式”下，EXP显示等多余组件就会消失。只保留对答题和学习有帮助的功能，比如显示计时，题号和正误数等。\n" +
                        "又像在“游戏模式”下，同样采取这样的方式，让boss和玩家的信息显示的同时依然保留合理的空间。\n" +
                        "第4准则就是，嗯，实用至上（笑）。\n" +
                        "因为开发平台机能限制和我的美术水平问题，APP的界面从1.0到7.0全都是这种灰白交杂的画风。\n" +
                        "不过，这样的设计也有好处，这样可以让人们专注于内容本身，而不至于被吸引注意力。\n" +
                        "当然，为了至少让人看得过去，APP的内容我还是稍微进行了一些排版工作，尽量让组件看上去整齐。\n" +
                        "之后就是逻辑和内容设计，我前面也提到过，这是一个“游戏化”为主导的APP。\n" +
                        "我希望用户能从游戏化的方式当中既能获得快乐，也能巩固自己的知识。\n" +
                        "之前有很多类似的尝试，但它们大多以失败告终，老实说，我的思路和这些先驱很相似，就是将答题和战斗捆绑起来，通过用户回答问题来指挥人物战斗。\n" +
                        "这种做法的缺点就是生硬的插入，答对就攻击，答错就不攻击。这就是答题和游戏唯一的联系。\n" +
                        "这也是我最初的做法，但是，这样没法让人真正接受“游戏化”。因为这相当于将两项割裂的内容强行拼接在一起。\n" +
                        "为了缓解这种手法带给人的不适感，我借鉴了养成手游的游戏玩法，没错，就是“肝”。\n" +
                        "在APP当中，几乎所有的资源都要和答题挂钩，这直接决定了“答题”从“战斗”和“肝”的附属变成了真正的主要玩法。\n" +
                        "为了配合用户的努力，之后，我设计出了一套围绕了答题奖励“积分”和“素材”的养成线。\n" +
                        "但是，让用户日复一日地在主UI打自动生成的怪物显然是太无聊了，而且，最大的问题在于函数生成的boss完全无法追赶用户的数值成长，在后期毫无挑战性的情况下，用户很容易会失去坚持的毅力。\n" +
                        "于是，为了构建激励机制，我参考《公主连结》的“露娜之塔”，《明日方舟》的“危机合约”。\n" +
                        "构筑起了APP的PVE玩法，通过人为设定的数值和挑战，弥补自动化生成的不足，保证APP的游戏难度水平控制合理，并使用户在养成之后能够挑战自己，获得快乐和成就感。\n" +
                        "手游的“日常”系统也不能少，以鼓励用户时常练习。关卡的强度也都要经过我亲自试玩，才能确定一个合理的数值，既能够让人有上进心，也不会被数值地狱劝退。\n" +
                        "为了鼓励用户多“肝”，多答题。我还设计了“成就”系统，借鉴《Minecraft》的统计项，将用户自我挑战的成长和足迹直观的展示给用户自己。\n" +
                        "但是这样想要吸引人还不够，所以我就借鉴了《公主连结》的剧情系统，让玩家在达到要求之后可以查看剧情，享受文字提供的惬意。\n" +
                        "不过，不得不承认，这部分我由于个人水平问题，做得不甚理想。至于音乐，立绘这些传统的吸引性项目，这个APP更是一项都没有，我们只能够在其他方面加以弥补。\n" +
                        "当然，不乏有用户讨厌“游戏化”的生活，所以我们一直保留着“专注模式”和“记忆模式”，提供给用户纯净的学习体验，\n" +
                        "并且，“剧情”“漫谈”等纯属娱乐的部分的开放要求都不会和“游戏模式”限定的数值绑定，让这部分用户也能享受到内容。\n" +
                        "我们之后也将一直为不同用户带来良好的使用体验，祝各位玩的开心~！");
                PrintStoryText(3,"3.1 APP重构和挑战（上）\n" +
                        "好的，经过很长时间的咕咕咕之后（逃~），我又给大家带来的新一期的《开发者漫谈》了。本期的主题是《App重构与挑战》。\n" +
                        "书接上回，在决定进行tomultiQA的开发之后，我高中时代的空闲时间很大的一部分就花在了这上面。\n" +
                        "凭借经验和实践，我慢慢无师自通了App Inventor，在我以为自己能够在这个平台上一直更新的时候，意外发生了。\n" +
                        "在APP大约6.0.0版本（2020.11.22）的时候，我有史以来第一次在这个平台上收到了编译错误。\n" +
                        "那是在我正在策划“排位”系统，也就是今天的“比武台”系统的前身的时候。\n" +
                        "就当我完成了一天的开发准备导出APK时，这个错误就突如其来地出现在我面前。\n" +
                        "尽管，我在删除了自己刚刚提交的代码之后，APK还是成功导出了。我松了一口气，但万万没想到，麻烦会接踵而至。\n" +
                        "后来，我发现，这是因为App Inventor在同屏（Activity）存在过多的代码块时（AI是一个像拼图一样实现编程功能的网页平台）会导致编译失败。\n" +
                        "所以，我就将不同的功能放在了不同的页面。这也是现在新版的tomultiQA当中仍然有“广场”“回忆”之类的分页面的主要原因，我的开发习惯就是在那样的时期养成的。\n" +
                        "但接下来的错误更是我始料未及的：7.0.0版本以后，boss的能力系统居然无法传输数据了。雪上加霜的是，我并没有保存项目的旧版本文件。\n" +
                        "要知道，对于tomultiQA的战斗玩法而言，能力系统是一个十分重要的组成部分，当时我就感到很难过。\n" +
                        "对这个问题的修复尝试并没有停止。接下来的7.1.0（2020.12.15），7.2.0（2021.1.9）版本，我都在尝试修复这个问题，希望让它再多坚持一会。\n" +
                        "但是不行。\n" +
                        "后来，我也尝试过将项目移植到App Inventor的不同实现上，但是，移植版本的问题更加严重，甚至连题目数据的传输都出现了问题。\n" +
                        "前后大概一个月的挣扎，我感到十分疲劳。\n" +
                        "那个时候，我刚刚开始我的寒假生活，我想，如果我不能够修好它，那么，不如试试对它进行重写？\n" +
                        "这个想法是很大胆的，但是，我除了App Inventor之外，没有接触过其他的开发方式。不过，人就是在空闲下来的时候，才有心思做这样的事。\n" +
                        "于是，我点开了Android的开发者页面，因为我多少算是对编程语言有一点点了解，知道java可以用于Android的程序编写。\n" +
                        "当时，已经是Android10的时代，kotlin已经成为Android的主要编程语言。\n" +
                        "不过，我当时倒是非常自信的认为，kotlin是一门新生语言。即使以后我不再想要开发Android应用，Java相较于kotlin也能够更好地帮我转到其他IT行业。\n" +
                        "现在想来，这个决定让我付出了不小代价。因为那个java视频教程录制于2015年，不少当年的技术如今已经不再适用。\n" +
                        "所以这让我在面向搜索引擎编程（笑）的时候，多了很多很多的搜索时间。而官网的kotlin教程，是2020年的。\n" +
                        "所以在技术栈（应该是这么称呼的吧...）上，我就落后了不少。一直到最近，我才尝试开始运用一些新的事物。\n" +
                        "总之，那时的我就开始学习官网的java开发教程。自此成为一名开发新人。开始了我的App重构之路。");
                PrintStoryText(4,"3.2 APP重构与挑战（下）\n" +
                        "刚开始，是App的UI设计工作，对于我来说，这种工作反倒是比较简单的。因为我本人基本没有什么艺术素养，运用Android默认的控件就是我能做到的极限了。\n" +
                        "所以，所谓的“设计”工作对我而言，只剩下了控件的选用和排版。这些倒是不难，再加上以往的开发经验，我很快就做好了这些工作。\n" +
                        "接下来，我就感受到了第一个困难的事物，数据库。\n" +
                        "对于一个问答App来说，数据库系统是一个难度不小却又不可或缺的事物。当时我翻了得有十几篇文章，最后在Android的开发者网站上复制了官方的代码示例。\n" +
                        "通过修改，才让整个系统勉强运行起来。尽管那时在网上一篇篇文章的寻找答案真的很憋屈，但是当功能成功实现之后，我真正的感受到了快乐和满足感。\n" +
                        "类似这样的挑战还有很多，比如RecyclerView,Dialog,OnClickListener，等等......但是，我都慢慢坚持了过来。\n" +
                        "如今代码里面注释越多的部分，往往就越是我克服万难之后，好不容易憋出来的成果和思路。\n" +
                        "就这样，在两个多月的学习和摸索之后，我终于憋出来了重构之后的新版App。\n" +
                        "在这之后，慢慢地迭代，修复bug，追加功能，就变成了今天这个样子。\n" +
                        "我明白，这个App和市场上的同类们相比起来，功能十分粗糙，设计差强人意。\n" +
                        "不过，我真的从中体会到很多属于开发者的艰难，我渐渐理解程序员们的思维，思考身边的App的设计思路。\n" +
                        "我真切的获得了脱离了低级趣味，真正融入到了知识和科学的高尚之中的快乐。这种快乐是一般的娱乐方式所不能够提供的。\n" +
                        "在App开发的很多个日夜里，我有过很多次放弃和妥协的念头，虽然也有一些成为了事实，但这些念头当中的大多数都被我打消了。\n" +
                        "我曾经很多次的想象过，如果自己成为了一个有名的人，自己的App也出人头地，最后名利双收的时刻。\n" +
                        "那种将梦想兑现，获得精神和物质双重快乐的感觉。即使只是想象，也足够让人沉醉其中。\n" +
                        "我告诉自己，即使现在没有什么事实上的“用户”，真正的“用户”也只有自己，我也要继续坚持这份心愿。\n" +
                        "这种时刻，总是能够让我充满动力，投身到无穷的事业当中。\n" +
                        "tomultiQA，为人们的学习，送去快乐，带来知识的初心，我也会一直发扬下去的，我们永远在路上。");
                break;
            case "SubLine":
                ChapterShowView.setText(CurrentStoryNumber + " / " +  SubStoryLimit);
                break;
            case "Tale":
                PrintStoryText(1,"《贵族和炼金术》\n" +
                        "在“世界”当中，“炼金术”是一门令人羡慕的手艺活。\n" +
                        "想想看，将一些神秘的物品，加上一点魔法和希望，扔进炼金炉里，就能得到无数的财富。\n" +
                        "这样简单的致富方式，自然让人神往。\n" +
                        "那么，（哔——），代价是什么呢？\n" +
                        "那就是......学院里的实验室三番四次的不停爆炸。\n" +
                        "甚至，其次数夸张到学院附近街道的居民和商贩面对不知何时又在发出冒烟或是巨响的建筑时，内心早已经毫无波动。\n" +
                        "也正因此，学院附近尽管交通便利，教育良好，也几乎没有任何贵族愿意在此定居。\n" +
                        "毕竟，哪天说不准就丢掉小命的地方可太危险了。\n" +
                        "更让人哭笑不得的是，相较于工农平民，那些养尊处优，被报以极大期待的贵族后代们，反倒是引发这些事件的最大元凶。\n" +
                        "在他们的生活当中，管家和仆人包办了几乎一切琐事，这也让他们几乎失去了对一切事物的本质认知。\n" +
                        "在实验中，手忙脚乱，不知所措，已经成为这个群体的特色了。\n" +
                        "面对蜥蜴尾巴，各类矿石，要素的流动这些他们从未见识过的事物，他们往往是无助的。\n" +
                        "要知道，炼金术士对自己所做的事情毫无了解可是大忌。\n" +
                        "对这些人来说，更可怕的是炼金术的随机性。\n" +
                        "每次把原料扔进炉子的时候，没有人知道迎接自己的是无尽的财富还是猛烈的爆炸。\n" +
                        "而这个性质，甚至在使用完全一致的配方时也不会例外。\n" +
                        "“轰隆！”当又一声爆炸传来时，办公室里的院长总是摇摇头，无可奈何。");
                ChapterShowView.setText(CurrentStoryNumber + " / " +  TaleStoryLimit);
        }
    }

    //sub method.
    private void SaveReadProgress(){
        SharedPreferences A = getSharedPreferences("StoryDatafile", MODE_PRIVATE);
        SharedPreferences.Editor editor= A.edit();
        editor.putInt("UserStoryProgress",CurrentStoryNumber);
        editor.apply();
    }

    //sub method.
    private void PrintStoryText(int Chapter, String Story){
        EditText StoryText = findViewById(R.id.StoryText);
        if(CurrentStoryNumber == Chapter){
         StoryText.setText(Story);
        }
    }//end of story system.


    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle("Hint");
            dialog.setMessage("--The Story Text has not translated yet, you need to have the language skill to read Chinese.--");
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    //set left button`s text.
                    getString(R.string.ConfirmWordTran),
                    (dialog1, id) -> dialog1.cancel());
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }//end of hint function.
}