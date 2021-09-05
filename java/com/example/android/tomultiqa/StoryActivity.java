package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
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
    static int MainStoryLimit = 3;
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
        PageMoveBar.setProgress(0);//move to first step.
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
                PrintStoryText(1,"序章 回忆\n" +
                        "随着“高塔”的轰然倒塌，周围天空当中的阴云渐渐散去。\n" +
                        "这片广阔的土地，一洗过去数百年的灰暗，居住在地下的地精钻出地表，它们当中的年长者双膝下跪，捧着阳光拂过的土壤，泪水滴落。\n" +
                        "而在他的身后，是刚刚褪去死气的褐色平原。尽管地上未有一点生机，但生命的痕迹已然开始生长。这里的人们，生物们等待这一刻已经太久了。\n" +
                        "这里并不是什么富庶之地，在有历史记载的时期起，这里就已经是沙漠广布，寸草不生的地方了。\n" +
                        "尽管粮食紧缺，生活非常困难，但人们还是凭借着流经这片土地的大河，慢慢积攒起了属于这片土地的一份生机。十几代人的汗水和奋斗，帮助风沙横扫的河岸，护住了最后的绿色。\n" +
                        "但是，好景不长，这是存在神明的世界。尽管“世界”的琐事对于神而言，不过是小打小闹。可一旦连神明之间都要战斗，那么人间的一切，就会如风一般飘散。\n" +
                        "命运给予了这里又一次重击。\n" +
                        "有一天，天空当中突然降下前所未见的雷电。存世典籍里对它的记载如下：\n" +
                        "“有雷鸣于极北地，为过往未见之势。此雷大如山间壁，电光通彻天地间。有如百般金石碰撞之声，目视风云变幻紫黑并立。映照四方生灵，一瞬地震并起，仿若天君亲临。弹指间，大地崩裂，说时迟，高台飞升，那时快，一塔已成......”\n" +
                        "是时，一座“高塔”拔地而起，没有人能够说清它的来历，只知道自它诞生起，那附近的天空就再也没有晴朗，土地就再也没有生机。河流的脉动也越来越沉重，河岸的人们，失去了自然最后的临幸。\n" +
                        "直到村里接受了一位陌生的来客，这一切就如同浸入水中的墨笔的黑墨一般，都随之慢慢被沁开在水中，明晰起来。\n" +
                        "“这是很久以前的故事了。”勇者回头看向身后的少女，手合上了一本破旧的故事书。\n" +
                        "“您是要离开了吗？”少女不敢看着他，“这里，已经无法挽留您了吗？”\n" +
                        "“......”，勇者呼唤了少女的名字，少女害怕的捂紧耳朵，但那个本应出现的东西没有再次出现。\n" +
                        "“它已经走了。”勇者说道，“可是人们还需要你，你是‘引导者’。你能够引导我的冒险，自然也能够引导人们的未来。但我......”\n" +
                        "“不要说了！”少女一反往常的乖巧，“您为什么要走，我不要离开您！”\n" +
                        "勇者明白她的不舍，她的哭声，他都好好的听着。\n" +
                        "少女望着山崖上的勇者，她害怕他的离开，她冲上去，希望能够拦着他。\n" +
                        "可勇者却拿出了些什么，按了下去。\n" +
                        "终于追到了，但当她伸出手的时候，她什么也没有握住。\n" +
                        "......\n" +
                        "“这个房间......”，勇者想。\n" +
                        "这是自己的房间，是那个她邀请自己前往异世界的地方。他还记得，自己当初其实不曾抱有回来的心态，他以为这会是一场永远不会结束的冒险。\n" +
                        "“但是没有什么是永恒的。”正如他踏上高塔的最后一段阶梯时，上面传来的声音所言。\n" +
                        "他拾起了被自己扔到地上的试卷，看着上面的成绩，他记起了自己离开的理由。\n" +
                        "“为什么妥协的只有我！”他向父母说道。\n" +
                        "他闭上眼睛，眼前除了黑暗，还如走马灯一般放映了过去的自己。过去，自己是一个小有成就，但却深受期望束缚的人。面对生活给予他的“礼物”。他最终都选择了妥协。\n" +
                        "也就是那一天，在经历了一场和平常相差无几的争吵之后，他决定为自己活一次。\n" +
                        "就在他热血上涌之际，一条来自手机的通知吸引了他。\n" +
                        "“出发吧！”\n" +
                        "他不记得自己曾经有过在其他人面前暴露过自己的内心，这条通知成功让他停下了脑中的计划。\n" +
                        "“相信你会成为人人称颂的英雄！”\n" +
                        "又是一条，他五味杂陈，为有人愿意尊重他的内心而高兴，却又因有人了解自己的内在而担忧。“只是恶作剧吧。”他想。\n" +
                        "通知在信息栏里跳动了一下。\n" +
                        "他瞪大眼睛，无法相信自己看到的一切。这证明，有什么在等待着他。\n" +
                        "依稀能够听到走廊里传来的声音，但他这次没有反复责问自己，而是锁上房门，轻轻触碰了那个白色的区域......\n" +
                        "故事就这样开始了。\n");
                PrintStoryText(2,"第1章 启程\n" +
                        "……白光一闪，少年就从房间里消失了。再次醒来的时候，少年只觉得头有些发痛，“奇怪，明明上次还不会这样.......”少年发现自己正躺在一个宽敞的房间里的木床上。\n" +
                        "“您醒啦？我就知道您会来的。”床边，有一位少女弯下腰来看着他。\n" +
                        "“‘知道’是什么意思？”少年对她的说法感到好奇。“当然，因为您是被‘邀请’的人哦~话说，在我见过的【玩家】里，您还是第一个这么冷静的呢。”\n" +
                        "少年从床上直起身来，少女正欲扶起少年，房间外的阳光就从少女让开的缝隙里透射进来。“诶...抱歉，会晒到你吗？”少女发现不对，赶忙小跑去将窗帘拉了起来。\n" +
                        "少年从床上坐起，打量着周围的一切。这是一个木制的小屋，从屋子的地板到墙壁都透露出一种自然的芳香。显然，房间的主人有好好的打理着这一切。\n" +
                        "“那个，你...你还没有回答我的问题呢。”少年突然想起自己的目的，少女张开嘴说话，但是.......不知为何，少年什么有用的信息都没有听到，耳边只有“嗤嗤”的噪音。\n" +
                        "“不对！”少年这样想着。\n" +
                        "顷刻，少女似乎说完了，噪音也消失了。“.......就是这样...好了，我也告诉了您我的名字，那么您的名字是...”\n" +
                        "没等少女说完，少年抢答道，“你就叫我‘勇者大人’吧！”\n" +
                        "“啊...是...”少女稍微被少年的情绪吓到了，在稳定了情绪之后，她说道“那...勇者大人，出发去冒险吧！”\n" +
                        "“冒险？”少年猛然发现，“对啊！之前她告诉过我的来着...”\n" +
                        "“对了！您还记得吗？您来到这个世界的目的。”少女似乎想起了什么。\n" +
                        "“目的？”少年想起，自己似乎是一时冲动，就进入了穿越的入口。至于自己到底是抱有什么目的前来，反倒是没有认真考虑过。\n" +
                        "“是的，我明白您希望进行一场伟大的冒险，但是，在那之前，还请您记住三条【法则】：”\n" +
                        "------\n" +
                        "【知识】是世界的秩序。\n" +
                        "【情绪】是诸事的因果。\n" +
                        "【对决】是众生的往来。\n" +
                        "------\n" +
                        "少女说完，不由分说的拉起了少年的手，“出发吧！”正当少女要拉着少年出门时，“等等！”少年突然打断，“我刚才没有听清，能再说一遍你的名字吗？”\n" +
                        "少女有些生气，“勇者大人没有记住我的名字吗?真是不靠谱......好吧，再说一次，我叫做...”“来了！”少年心里暗想着，果然，噪音马上就出现了。\n" +
                        "但是，这回少年可没有坐以待毙。而是寻找着噪音的来源。\n" +
                        "在屋外的广阔海岸，地平线开外的地方，树立着一座高塔，它仿佛直通天地。\n" +
                        "可是，在高塔的周围，原本苍蓝的天空却显现出阴暗而恐怖的色彩。\n" +
                        "“勇者大人？您在看那座塔吗？”\n" +
                        "“啊，是的。”\n" +
                        "“那就是您将来的目标哦！”\n" +
                        "“啊？”\n" +
                        "少女牵起了少年的手，“我明白，您现在有很多很多的疑惑，但是，我会引导您的成长，陪伴您的冒险，您可以，相信我吗？”少女露出了美丽的笑容。\n" +
                        "风起了，但是却不是湿润而温暖的海风，而是从海岸后方的山岭上传来的冷风。\n" +
                        "它严酷而冷峻，仿佛想将两人推向那个恐怖的高塔。\n" +
                        "不过，少年下定决心，他不会让恐怖和黑暗笼罩这片大陆，“相信我，这里永远会是和谐安宁的圣地。”\n" +
                        "少年突然弹出这么一句，但是少女并没有嘲笑少年的志向，她只是说道：“愿神明护佑您的道路，我最亲爱的勇者大人。”");
                PrintStoryText(3,"第2章 风吹白花\n" +
                        "在少年应许了少女的邀请之后，两人决定前往附近村落的委托所，开始接取一些简单的委托。\n" +
                        "少女站在一边，从柜子上将出门冒险所用的物品收入随身携带的小包之中。\n" +
                        "而少年却一脸苦相：“那个，嗯......”少年想要叫住少女，询问她些什么。\n" +
                        "但是，很尴尬的问题摆在了少年的面前：即使少女之前又说过了一遍自己的名字，但是少年仍然是什么都没有听到。\n" +
                        "“那边的...女孩”少年就差没有捂着脸说出这句话了，而少女似乎是知道少年要叫她，停下了收拾的动作。\n" +
                        "“怎么了？”不同于之前见面时她的笑容，这次，她的脸上带着一些无奈的神色。看着少年发窘的样子，少女开了口：“是又不记得我的名字了吗？”\n" +
                        "“啊...是的。”少年不敢直视她，害怕自己一次次的提问消磨掉她的耐心。\n" +
                        "可是，事情却不像他所想的那样，少女并没有对他发火，而是问起了原因来，“您可以告诉我，不记得我名字的原因是什么吗？之前的‘引导’，都没有发生这样的现象。‘’\n" +
                        "“嗯，其实是，”少年组织着语言，思考着怎样向面前一无所知的少女解释这一切。可思考的时候，那个“噪音”，却隐隐约约地干扰着他的思绪。\n" +
                        "“你在说出自己的名字的时候，就会...什么也听不见。”少年隐瞒了“噪音”的存在，因为他猜测，“噪音”是在阻止自己说出真相。\n" +
                        "“滋滋...”突然，沉默许久的少女开口。随之而来的就是刺耳的“噪音”。“是这样吗？刚才我又说了一遍自己的名字。”\n" +
                        "“嗯。”少年有些吃惊，她似乎很容易就猜到了自己的想法。\n" +
                        "“这样啊，看来您惹上了很大的麻烦呢。”少女的脸色非常严肃，“干扰现实当中行为的存在，也只有......”少女看向远方，“只有‘生活’了。”\n" +
                        "“‘生活’？”少年对少女的说法有些不太理解，“那是...什么？”\n" +
                        "“所谓‘生活’，就是创造出那座纵贯天地的高塔的存在。”少女解释道，“它是支配情绪和命运的存在，还记得吗？那条【情绪】的法则……”\n" +
                        "少年回答：“嗯，【情绪】是诸事的因果。”\n" +
                        "少女继续说道，“没错，正是因为【情绪】对于世界的影响十分凸显，这也使得“生活”是人们最为信仰的神明之一。”\n" +
                        "“但，自从那座高塔出现以来，大陆上就出现了很多的消极和负面事件。”\n" +
                        "“与此同时，也传出更多因为‘霉运’而死伤的例子。这些线索都将罪状指向了‘生活’，它也就渐渐失去了人们的信仰，成为了人们的口中的‘邪神’。”\n" +
                        "“没有人出面阻止它吗？”少年提问道。\n" +
                        "“当然是有的，但是因为人神的巨大差异，结果大多都……不尽如人意。其中，”少女顿了一会，“重伤，毙命的挑战者里也不乏被‘引导’的勇者。”\n" +
                        "听到这里，少年感到些许不安，少女看出来了他的心思，继续说道：\n" +
                        "“勇者的责任是维护正义，为了承担这样的责任，许多人付出了巨大的代价。”\n" +
                        "“但是我们，‘引导者’也很无奈。一般人根本没有战斗能力，而勇者就是我们对抗邪恶的最大依仗。”\n" +
                        "“况且，还记得吗，勇者大人？您的目的，当时您和我都没有说出自己的想法，于我而言，‘邀请’您的目的，其实不仅仅在于让您能够享受游戏，还有一些，自己的私心。”\n" +
                        "少女低下了头，语气里慢慢带上了一些委屈。“我希望，有更多像您一样的英雄，能够挺身而出，拯救大陆和人民于水火之中。”\n" +
                        "“您知道吗，我了解到您被‘生活’的力量所干扰，既兴奋又不安。我很高兴您的实力能够让高高在上的神明也会注意着您，但又担心您会像以前的人们一样，被它无情的抹杀......”\n" +
                        "“其实，您也可以选择！......啊”\n" +
                        "少女正欲继续，但少年止住了她的话语，摇摇头。“那么，我想，我的目的，也许就是拯救世界了吧。”\n" +
                        "“诶？”\n" +
                        "“是啊，‘被邀请的人’，多么像那些穿越故事的开头。”\n" +
                        "“那么，既然要上演一幕穿越奇闻，那么，我会成为英雄的。”\n" +
                        "少女的眼中闪烁着光芒，“嗯！我也会好好地‘引导’您，和您携手共同冒险的。”\n" +
                        "“啊，对了......”少女说着话的时候，突然跑开。\n" +
                        "正当少年有些摸不着头脑时，她回来了。手里握着一朵盛开的白花。少女将花双手捧给少年。\n" +
                        "“这是......？”少年说道。“花？”\n" +
                        "“是的，这是只在春天盛开的鲜花。”少女慢慢抬起头来，“从今往后，您就叫我……‘白花’吧。即使我的真名没有办法告诉您，但我相信，您一定会有听到我名字的一天。”少女微笑着。\n" +
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