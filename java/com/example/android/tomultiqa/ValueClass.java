package com.example.android.tomultiqa;


public class ValueClass {
    //App Version.
    public static final int APP_VERSION = 400;
    //App Mode.
    public static final String NORMAL_MODE = "Normal";
    public static final String GAME_MODE = "Game";
    public static final String FOCUS_MODE = "Focus";
    //System notification ID.
    public static final String CHANNEL_ID = "tomultiQA";
    public static final int NOTIFICATION_SYSTEM_ID = 156900;//big number unique id used in notification, just leave it here.
    //Item Rare Rank.
//    public static final String ITEM_RANK_EPIC = "Epic";
//    public static final String ITEM_RANK_RARE = "Rare";
//    public static final String ITEM_RANK_DECENT = "Decent";
//    public static final String ITEM_RANK_NORMAL = "Normal";

    //App Help Text. Collect them in here are convenient to edit.
    public static final String ITEM_SYSTEM_HELP =
            "This Help File is only provided with Chinese edition.\n" +
                    "更新日期：2021/6/11\n" +
                    "\n" +
                    "0 声明\n" +
                    "欢迎使用tomultiQA的物品系统。\n" +
                    "由于技术水平问题，物品系统目前暂时处于测试阶段，可能存在bug，欢迎您提出反馈！\n" +
                    "\n" +
                    "1 已知问题\n" +
                    "由于物品名称是以当前语言格式存档的。因此，如果您在App使用过程当中切换语言。有可能会引起数据异常。\n" +
                    "（比如：同一种物品在数据库当中以不同语言版本出现多次，合成时无法检索到物品等问题。）\n" +
                    "出于稳定性考虑，这个问题暂时不会修复。\n" +
                    "\n" +
                    "2 概述\n" +
                    "“物品”系统相当于其他游戏的“背包”。不同于“积分”“素材”等“货币”，这赋予了物品系统很大的灵活性。\n" +
                    "未来，App会支持使用物品进行合成，自定义物品等功能，赋予用户极大的自由度。（计划中）\n" +
                    "\n" +
                    "3 物品类型\n" +
                    "目前（0.20.0版）环境下，我们主要规划了3类物品：\n" +
                    " 3.1 资源，类似于其他游戏的“合成素材”，不能使用。\n" +
                    " 3.2 物品，目前尚未开放。\n" +
                    " 3.3 贵重物品，类似于其他游戏的“道具”，一般而言，它们可以被使用，且不能被出售。目前尚未开放。\n" +
                    "（由于物品系统处于测试阶段，不排除这一部分存在变更的可能，目前仅供参考。）\n" +
                    "\n" +
                    "4 物品获取途径\n" +
                    "现阶段，我们规划了3个主要获取物品的途径。\n" +
                    " 4.1 “行商集”功能，可以通过收集“宝箱”和“钥匙”进行“开箱”，获取物品。\n" +
                    " 4.2 Chessboard（暂定名，后面会更改），通过类似“棋盘”的游戏获取物品。（未开发）\n" +
                    " 4.3 战斗获取，击败特定boss获取物品。（未开发）\n" +
                    "\n" +
                    "5 物品图鉴\n" +
                    "以下给出当前（0.20.0）版本的所有物品。\n" +
                    "格式：物品名称/物品类型；物品描述（物品获取途径）\n" +
                    "5.1 史诗（Epic）品质\n" +
                    " 5.1.1 奥术碎片（Magic Piece）/资源/史诗；\n" +
                    "这是“世界”经历大战后，古人遗留下来的魔力凝结的物质。是极佳的素材。被各国的商人们高价收购。据说法术学院当中的那些“帽子”们，在它身上发现了重大的秘密。（行商集获取）\n" +
                    "\n" +
                    "5.2 稀有（Rare）品质\n" +
                    " 5.2.1 精灵树之木（Elf Tree Wood）/资源/稀有；\n" +
                    "自精灵一族居住的森林当中获得的树木。就产量而言，这一材料并不稀有。真正让它具备价值的原因，在于精灵族对森林的守护。能从精灵森林的重重陷阱当中逃脱的伐木工可为数不多。（行商集获取）\n" +
                    " 5.2.2 元素流瓶（Element Flow Bottle）/资源/稀有；\n" +
                    "装有流动元素的玻璃瓶，是缺乏魔力者和初学者使用魔法的重要道具。只有熟练的炼金术师和法师们联合才能制作。且其市场流通被严格监管，供不应求。（行商集获取）\n" +
                    " 5.2.3 清梦果（Dream Fruit）资源/稀有；\n" +
                    "虽然“清梦果”的外形酷似果实，但需要澄清的是，尽管名字里有“果”，但清梦果实际上并不能食用。曾经有人品尝过它，但当人们询问他滋味时，他却无法表达出来。也因此，它被巫师们用作忘记人间疾苦的道具，也就有了“清梦”的名号。（合成获得）\n" +
                    "\n" +
                    "5.3 精制（Decent）品质\n" +
                    " 5.3.1 灵气石（Spirit Stone）/资源/精制；\n" +
                    "蕴含灵气的矿石，可以从中提取出编制魔法术式的能量，或是提炼过后，作为机械的动力源。是一种广泛使用的资源。不过，矿石本身其实价值有限，其珍贵实际上体现在被提炼的纯度上。（行商集获取）\n" +
                    " 5.3.2 纯洁束绳（Pure String）/资源/精制；\n" +
                    "通过精心清洁和祝福的绳子，相较于它“弱不禁风”的外观，它的束缚力其实很大。一些贵族也因为其纯白无暇的外观而将这种绳子用于装饰。是一种兼具实用和装饰性的资源。（行商集获取）\n" +
                    "\n" +
                    "5.4 普通（Normal）品质\n" +
                    " 5.4.1 韧铁（Solid Metal）/资源/普通\n" +
                    "经过铁匠锻造，塑性的优质金属。是被用于制作武器，防具等用途的素材。注意，虽然名字叫“韧铁”，但不一定是金属铁，也可能是其他物质的混合体。打造这些金属的细节，是各个地区的铁匠们不约而同要保守的秘密。（行商集获取）\n" +
                    "5.4.2 蚀金（Curved Gold）/资源/普通\n" +
                    "金银等贵金属具备良好的魔法适应性，是用于制作法器，魔法制品的好素材。“蚀金”就是通过炼金术师之手刻蚀的黄金。其上的刻痕可以是魔力的流动更为畅通，但有市井传言道：真正优良的“蚀金”是另有制作方法的。这可能就是普通的“蚀金”明明含有不少黄金，价值却平平无奇的原因吧。（行商集获取）";

    public static final String OPEN_POSSIBILITY_HELP =
            "This Help File is only provided with Chinese edition.\n" +
                    "以下是“开箱”功能当中，各品质物品的出现概率：\n" +
                    "史诗（Epic）1%\n" +
                    "稀有（Rare）12%\n" +
                    "精制（Decent）24%\n" +
                    "普通（Normal）58%";

    public static final String GAME_FORMULA_HELP =
            "This Help File is only provided with Chinese edition.\n" +
                    "更新日期：2021.8.15\n" +
                    "APP内的公式：\n" +
                    "\n" +
                    "1 用户/答题相关\n" +
                    "1.1 升级所需的EXP\n" +
                    "当前等级 ^ 1.6 + 15 - 当前等级 ^ 1.1\n" +
                    "1.2 答题奖励积分\n" +
                    "题目难度  *  10 + 连击数 ^ 2.6\n" +
                    "（最大值为10000）\n" +
                    "1.3 自动生成难度\n" +
                    "（题目长度 + 答案长度）^ 0.57\n" +
                    "\n" +
                    "2 天赋相关\n" +
                    "2.1 升级费用\n" +
                    "（注：X为天赋当前等级）\n" +
                    "2.1.1 攻击天赋\n" +
                    "135 + X ^ 1.12 积分 / 级\n" +
                    "2.1.2 暴击率天赋\n" +
                    "165 + X ^ 1.37 积分 / 级\n" +
                    "2.1.3 暴击伤害天赋\n" +
                    "200 + X ^ 1.33 积分 / 级\n" +
                    "\n" +
                    "2.2 天赋效果\n" +
                    "2.2.1 攻击天赋\n" +
                    "+ 3 攻击力 / 级\n" +
                    "2.2.2 暴击率天赋\n" +
                    "+ 0.05% 暴击率 / 级\n" +
                    "2.2.3 暴击伤害天赋\n" +
                    "+ 0.2% 暴击伤害 / 级\n" +
                    "\n" +
                    "3 日常副本（望晨崖）相关\n" +
                    "（注1：A = 用户等级，B = 副本难度）\n" +
                    "（注2：【随机数：a~b】意为，大小在a和b之间（含a，b）的随机整数。例如：【随机数：10~20】可以是10到20之间的任何整数（包含10和20）。）\n" +
                    "3.1 Boss的HP值\n" +
                    "A * 【随机数：11 + （B * 11）~ 61 + （B * 9）】\n" +
                    "3.2 副本奖励\n" +
                    "3.2.1 EXP\n" +
                    "获得：（20+B*30 ）EXP\n" +
                    "3.2.2 积分\n" +
                    "获得：（5000 + 90 * A * B）积分\n" +
                    "3.2.3 素材\n" +
                    "获得：（2 + B * 3）素材\n" +
                    "\n" +
                    "4 战斗相关\n" +
                    "（注：A = boss的累计击败数）\n" +
                    "4.1 普通Boss的HP\n" +
                    "23 * (A + 1) * {14 + 【0.1 *  (A+ 1) 】}\n" +
                    "4.2 伤害构成\n" +
                    "伤害计算共有3个乘区，其公式如下：\n" +
                    "攻击力 * 暴击伤害 / 100 * 增伤数值\n" +
                    "公式为浮点计算，在所有步骤（包含能力部分的计算）结束后，转为整数显示。\n" +
                    "“攻击力”乘区包含：\n" +
                    "基础值（10），等级基础值（10 * 等级），天赋加成，炼金加成，作弊加成，等级突破加成\n" +
                    "“暴击伤害”乘区包含：\n" +
                    "基础值（150.0），天赋加成，炼金加成，等级突破加成\n" +
                    "“增伤数值”乘区包含：\n" +
                    "等级压制效果\n" +
                    "（boss较用户等级高1级，受到伤害 -3%，反之 +3% / 级）\n" +
                    "\n" +
                    "5 炼金相关\n" +
                    "（注：A = 要素数量， B = 要素上限--通常是6个（最大可达8个），C = 二阶要素使用量）\n" +
                    "5.1 炼金成功率\n" +
                    "【100 - A * （B - 3）】% / 次 \n" +
                    "5.2 素材消耗量\n" +
                    "{【（A / B）* 3 * B】 + C  * 7 } 素材 / 次\n" +
                    "5.3 炼金效果持续回合\n" +
                    "（注：此处的要素数量不含“平衡”系列的要素！“平衡”系列的要素会按照描述相应增加效果的持续时间。）\n" +
                    "【6 - A】回合 / 次\n" +
                    "5.4 炼金术等级\n" +
                    "5.4.1 等级需求和等级加成\n" +
                    "注1：升级后原有EXP将会被扣除！\n" +
                    "注2：各级效果不叠加，取最高值计算。\n" +
                    "等级 / 升级经验 / 效果\n" +
                    "lv.1    300EXP 成功率+2%\n" +
                    "lv.2 500EXP 成功率+4%\n" +
                    "lv.3 900EXP 成功率+7%\n" +
                    "lv.4 1400EXP 成功率+7%，要素上限+1\n" +
                    "lv.5 1900EXP 成功率+10%，要素上限+1\n" +
                    "lv.6 2500EXP 成功率+12%，要素上限+1\n" +
                    "lv.7 3200EXP 成功率+14%，要素上限+1\n" +
                    "lv.8 3900EXP 成功率+16%，要素上限+1\n" +
                    "lv.9 4700EXP 成功率+18%，要素上限+1\n" +
                    "lv.10 5500EXP 成功率+20%，要素上限+1\n" +
                    "lv.11 6400EXP 成功率+22%，要素上限+1\n" +
                    "lv.12 7300EXP 成功率+22%，要素上限+2\n" +
                    "lv.13 8200EXP 成功率+24%，要素上限+2\n" +
                    "lv.14 9100EXP 成功率+27%，要素上限+2\n" +
                    "lv.15 10000EXP 成功率+30%，要素上限+2\n" +
                    "5.4.2 经验获取\n" +
                    "每次炼金成功都将会获得：\n" +
                    "“要素数量 * 炼金消耗 * 【随机数：0~10】点”的炼金EXP。\n" +
                    "5.5 炼金效果及补充说明\n" +
                    "5.5.1 不同种类要素的效果是同时生效的。\n" +
                    "5.5.2 一次炼金当中，不同种类要素，多个同种要素的效果均会叠加，并持续至炼金效果的持续回合耗尽。\n" +
                    "5.5.3 一次炼金当中，包含的所有要素将同时开始和结束计时。\n" +
                    "\n" +
                    "6“修身馆”相关\n" +
                    "6.1 【工作中】计时奖励积分\n" +
                    "【0.1 + （用户累计答题数）/ 800】 积分 / 秒\n" +
                    "\n" +
                    "7“比武台”相关\n" +
                    "7.1 评价计算公式：\n" +
                    "boss的HP / boss的回合数 * 评价加成系数 * 等级加成系数。\n" +
                    "公式为int整数运算,最大值同int型上限。\n" +
                    "7.2 评价加成系数：\n" +
                    "所有已选择能力数值的总和。（注：其数值为0时，等价于1。）\n" +
                    "详情请见“图鉴”。\n" +
                    "7.3 等级加成系数：\n" +
                    "公式：（boss等级 - 用户等级） * 0.03 + 1\n" +
                    "简而言之，boss每比用户高1级，总评价上升3%，反之则下降3%。双方同级则系数为1。\n" +
                    "注意：输入的boss等级不得超出用户的±33级，否则boss会被重置为用户的当前等级。\n" +
                    "\n" +
                    "8“试炼场”相关\n" +
                    "8.1 等级突破\n" +
                    "8.1.1 突破需求\n" +
                    "突破阶段/积分/EXP/答题正确率（%）/总答题数/最高答题连对\n" +
                    "1/10万/750/50/25/2\n" +
                    "2/25万/2000/60/100/3\n" +
                    "3/50万/3500/65/220/4\n" +
                    "4/80万/5000/70/400/5\n" +
                    "5/120万/7000/75/600/6\n" +
                    "6/160万/9000/80/850/7\n" +
                    "7/200万/1.1万/83/1200/8\n" +
                    "8/250万/1.3万/86/1800/9\n" +
                    "9/325万/1.5万/89/3000/10\n" +
                    "10/500万/2.0万/90/4500/10\n" +
                    "8.1.2 突破效果\n" +
                    "突破阶段/等级上限/攻击加成/暴击加成/暴伤加成\n" +
                    "1/75/80/2/5\n" +
                    "2/100/250/4/10\n" +
                    "3/125/400/6/15\n" +
                    "4/150/600/8/20\n" +
                    "5/175/1000/10/25\n" +
                    "6/200/1500/12/30\n" +
                    "7/225/2300/14/35\n" +
                    "8/250/3000/17/40\n" +
                    "9/275/4000/20/50\n" +
                    "10/300/5000/25/50";

    public static final String BACKUP_HELP =
            "This Help File is only provided with Chinese edition.\n" +
                    "更新日期：2021/8/29\n" +
                    "\n" +
                    "0 声明\n" +
                    "欢迎使用tomultiQA的备份系统。\n" +
                    "由于技术水平问题，备份系统目前暂时处于测试阶段，可能存在bug，欢迎您提出反馈！\n" +
                    "\n" +
                    "1 数据导出\n" +
                    "目前，您可以点击“数据导出”键将App内的用户数据导出到.txt格式的备份文件当中。\n" +
                    "在点击按钮之后，App会调用系统的文件选择器来让您指定需要保存的文件位置和文件名称。\n" +
                    "默认的保存位置是系统的“下载(Download)”文件夹（/storage/emulated/0/Download）。\n" +
                    "在点击“保存”键之后，就完成了数据导出的操作。\n" +
                    "读取的操作详见帮助的第2部分。\n" +
                    "\n" +
                    "2 数据导入\n" +
                    "点击“数据导入”按钮，App会调用系统的文件选择器来让您指定需要读取的备份文件。\n" +
                    "点击您需要读取的备份文件，App就会自动完成导入流程。完成后会弹窗显示导入的数据概览。\n" +
                    "再次点击“确定”即可完成流程。\n" +
                    "虽然理论上，您可以指定任意的.txt格式的文件，但请您务必不要指定非App的备份文件，否则会引发App闪退或崩溃。\n" +
                    "2.1 文件预览\n" +
                    "如果您持有多份备份文件，但却不清楚应该导入哪一份的话，那么您可以使用“文件预览”功能。\n" +
                    "点击“文件预览”按钮后，App将会执行和“数据导入”一致的流程。但是，在读取文件之后，文件当中的数据将不会实际保存到App当中。以此完成“预览”的功能。\n" +
                    "注意：即便不影响实际数据，但选择非备份文件进行预览时依然会引发App闪退或崩溃。\n" +
                    "\n" +
                    "3 备份范围\n" +
                    "3.1 当前支持范围\n" +
                    "见帮助的4.3.1 备份文件概述的列出项目。\n" +
                    "3.2 当前暂不支持\n" +
                    "用户定义的题库（关键！）\n" +
                    "用户的背包数据（关键！）\n" +
                    "用户记录的笔记（关键！）\n" +
                    "用户获取的物品（关键！）\n" +
                    "“Million Master”成就的获得与否\n" +
                    "作弊得到的用户属性\n" +
                    "\n" +
                    "4 常见问题和使用注意\n" +
                    "4.1 备份文件的手动修改\n" +
                    "4.1.1 备份文件概述\n" +
                    "首先，我们不建议您手动修改备份文件。因为人为修改可能会导致读取系统无法工作。\n" +
                    "其次，如果您实在希望这样做。以下是关于备份文件的说明：\n" +
                    "备份的原理是将App当中的每个变量（比如：用户的积分数量，素材数量等数值，它们之中的每一个都是“变量”）从App内部存储空间当中提取出来，\n" +
                    "并按事先安排的顺序写入备份文件。其中，每个变量在被写入时都会自动换行。\n" +
                    "文件的构成如下：\n" +
                    "文件标题\n" +
                    "文件关联的App版本\n" +
                    "文件生成的系统时间\n" +
                    "用户等级\n" +
                    "用户当前持有的经验值\n" +
                    "用户正确回答数\n" +
                    "用户错误回答数\n" +
                    "“天数计时”进度\n" +
                    "“天数计时”目标\n" +
                    "攻击力天赋等级\n" +
                    "暴击率天赋等级\n" +
                    "暴击伤害天赋等级\n" +
                    "当前答题连对次数\n" +
                    "当前积分数量\n" +
                    "当前素材数量\n" +
                    "boss累计死亡次数\n" +
                    "累计获得经验值（用户等级经验）\n" +
                    "累计获得积分数\n" +
                    "累计获得素材数\n" +
                    "最大答题连对数\n" +
                    "“敌临境”通关层数\n" +
                    "“比武台”最高评价\n" +
                    "“望晨崖”通关难度\n" +
                    "用户炼金等级\n" +
                    "当前炼金经验值\n" +
                    "等级上限\n" +
                    "等级突破阶段\n" +
                    "等级突破攻击加成\n" +
                    "等级突破暴击加成\n" +
                    "等级突破暴伤加成\n" +
                    "战斗失败次数\n" +
                    "“比武台”boss设定等级\n" +
                    "“比武台”boss设定HP\n" +
                    "“比武台”boss设定回合数\n" +
                    "\n" +
                    "4.1.2 文件修改指导\n" +
                    "自由修改：\n" +
                    "文件标题\n" +
                    "可以修改：\n" +
                    "文件版本，备份的数值。\n" +
                    "不能修改：\n" +
                    "文件名称，文件排版。\n" +
                    "\n" +
                    "4.1.3 修改注意事项\n" +
                    "（1）数值上限\n" +
                    "大多数数字的上限采用了int型，最大支持2^32-1大小的数值。\n" +
                    "只有“累计获得积分”和“‘比武台’最高评价”两项使用了long型，最大支持2^63-1的数值。\n" +
                    "如果数值过大，可能会导致App崩溃，或者使数值溢出为负值。\n" +
                    "（2）数值下界\n" +
                    "文件当中数值没有低于0的部分，请在修改时不要将数值改为0以下，这可能引起App的行为异常。\n" +
                    "其中，特例的是“用户等级”“用户炼金等级”“‘天数计时’目标”三项，它们的最小值是1，而不是0。\n" +
                    "\n" +
                    "4.2 关于备份文件的版本\n" +
                    "我们建议App使用对应版本的备份文件，因为备份的读取系统不会因为文档内容的变更而更改读取方式。\n" +
                    "跨版本的读取轻则引起数据异常，重则引起App崩溃，无法使用，备份文件作废等后果。\n" +
                    "但我们并未在App当中作出硬性限制，文件当中的版本号不会影响读取操作。仅仅是出于提醒用户注意的目的设定的。\n" +
                    "还请您在实际操作当中注意。";

    public final static String EXPERIMENT_HELP =
            "This Help File is only provided with Chinese edition.\n" +
            "1.什么是“实验性功能”？\n" +
            "目前处于测试阶段，功能基本可用；但完成度不高，且可能存在bug，不排除未来有行为变更可能性的App功能。\n" +
            "这些功能出于稳定性和未来可维护性的考虑，需要用户手动打开功能开关才能使用。\n" +
            "1.1 部分功能在后续更新当中会移出“实验性功能”页面。转为正式功能。\n" +
            "1.2 处于测试阶段的功能不保证在后续版本更新当中工作正常。请谨慎使用。\n" +
            "\n" +
            "2.“实验性功能”一览：\n" +
            "以下是处于实验阶段的功能。\n" +
            " 2.1“物品”系统\n" +
            "为App添加“物品”系统，开启后开放“储物箱（背包）”“行商集（市场）”“工匠屋（合成）”3个功能页面的访问。\n" +
            "相较于“积分”等货币形式的财富，“物品”系统为App的游戏系统提供了更加灵活多样的选择。\n" +
            "未来，部分功能将要使用“物品”系统提供的功能。\n" +
            "该功能目前仅为测试阶段，所有可获得的“物品”没有实际用途，仅供预览体验之用。";

    public final static String FAVORITE_HELP =
            "This Help File is only provided with Chinese edition.\n" +
            "0 简述\n" +
            "欢迎使用tomultiQA的“收藏”功能。您可以在这里专门学习您收藏的题目，而无需等待您想要的题目被系统刷出。\n" +
            "“收藏”是类似于将现实当中的“错题集”和“默写/背诵”结合起来的功能。您可以对收藏的题目同时完成这两项功能。\n" +
            "详细用法请见下方。\n" +
            "\n" +
            "1 使用方法\n" +
            " 1.1 添加收藏\n" +
            "使用本功能之前，您需要将平时答题过程当中，一些您认为重要的题目，点击主页面的“收藏题目”按钮收藏起来。\n" +
            "在收藏了至少1题之后，就可以进行下一步了。\n" +
            "\n" +
            " 1.2 组件介绍\n" +
            "点击主页面顶栏的“爱心”按钮，进入“收藏”页面后，您应该可以看到带有下列按钮的界面：\n" +
            "（1）右上角的计数器“- / -”\n" +
            "这是显示【您当前正在查看的题目 / 收藏的题目总量】的进度的计数器。由此，您可以方便的掌握自己正在查看的是第几个收藏。\n" +
            "（2）和主页面一致的“问题 / 答案”输入框\n" +
            "它们和主页面的同类发挥基本一样的功能，不再赘述。\n" +
            "（3）写有“展示答案”的开关\n" +
            "通过这一开关，您可以自己决定【是否要在题目刷新时展示答案】，如果开启，则在题目刷新时，问题对应的答案会直接显示在答案的输入框当中。\n" +
            "如果关闭开关，若界面上已经刷新出题目，则答案会消失（包括您自己输入的部分！）；若没有题目，则会在之后刷新题目时不再展示答案。\n" +
            "（4）“上一题”/“下一题”按钮\n" +
            "和主页面不同，发挥“确定回答”作用的按钮在这里有两个。\n" +
            "如果收藏的“刷新模式”处于“随机”，则这两个按钮的功能是一致的。\n" +
            "如果收藏的“刷新模式”处于“顺序”，则您可以通过点击这两个按钮在上一题/下一题之间切换。\n" +
            "“刷新模式”的更改方式将在下文给出。\n" +
            "（5）“取消收藏”按钮\n" +
            "如果您认为当前显示的题目收藏不再需要，则您可以点击此按钮，点击后，App会弹框询问您是否要删除这一题目。点击对话框当中的“确定”即可删除当前正在显示的收藏。\n" +
            "【警告：由于技术问题，删除功能会将所有问题为“当前显示值”的题目进行删除。】\n" +
            "【因此，如果“收藏”内有多个问题完全相同的题目，则它们会被一起删除，请您务必不要添加内容完全一致的题目！】\n" +
            "注：如果您删除了所有“收藏”里的题目，则这一页面会被锁定，不能够再使用。这时，请您退出“收藏”页面，并从1.1“添加收藏”一节重新开始。\n" +
            "（6）右下角的“设置”（齿轮图标）按钮\n" +
            "这是本页面的设置页，您可以在此设定一些功能，具体功能如下：\n" +
            "（6.1）“判定正误”开关\n" +
            "默认关闭，如果开启，则App会像主页面一样在您点击“上一题”/“下一题”按钮时对题目的正误进行判定，并显示给您。\n" +
            "【注意：出于不干扰用户连续性体验的考虑，“收藏”页面的判定在题目回答错误时“不会”给出正确答案的弹窗，以减少干扰，后续会考虑增加选项。】\n" +
            "（6.2）“刷新模式”\n" +
            "有两个选项，默认选择的是“随机”。\n" +
            "“随机”模式下，收藏的题目会被随机刷新。\n" +
            "“顺序”模式下，收藏的题目会按照收藏顺序由先到后进行刷新。\n" +
            "注：“顺序”模式下，切换是相互贯通的，当达到最后一个题目时，“下一题”操作会重新刷新第1题；同理，当处于第1个题目时，点击“上一题”会跳到最后一题。\n" +
            "（6.3）重置顺序\n" +
            "这是为“收藏”题目过多的情况设计的，如果您当前处于的题目顺序较后，则可点击此按钮快速让App重新从第1题开始刷新。\n" +
            "\n" +
            "1.3 推荐用法\n" +
            "以下是组合“收藏”页面提供的功能带来的用法推荐：\n" +
            "（1）背诵\n" +
            "这一场景，需要将“显示答案”设定为“关”。（“判定正误”可以按需开关）\n" +
            "您可以看着题目背诵出对应的答案。\n" +
            "（比如，古诗词填空题里，题目是上半句，答案是下半句。您只要按需开关“显示答案”。就能够在打开答案时查看原文，关闭答案时回忆内容，从而达到背诵的效果。）\n" +
            "（2）默写/答题/测验\n" +
            "这一场景和“背诵”类似，不过还要将“判定正误”打开。\n" +
            "既然背诵完毕了，那么就可以进行自我的测试了，通过输入答案，App判定您回答的内容是否正确，从而检验您是否掌握了知识。\n" +
            "【虽然主页面也能提供基本一致的功能，但“收藏”的出题范围更集中，不同用法的切换更加灵活，故而推荐在此完成。】\n" +
            "（3）浏览\n" +
            "这一场景，需要将“判定正误”关闭。（“显示答案”可以按需开关）\n" +
            "您可以收藏多个有关同一知识点的题目，通过“顺序”模式在不同题目间跳转，集中复习。\n" +
            "【虽然主页面也能提供基本一致的功能，但“收藏”的刷新方式更便捷，且刷新时不会判定正误，也就不会弹窗和影响效率。】";

    public static final String ASSIST_FUNCTION_HELP =
            "This Help File is only provided with Chinese edition.\n" +
                    "更新日期：2021/8/11\n" +
                    "您看到的这些是App提供的“附加功能”，以下各自介绍它们的功能：\n" +
                    "1.“自动弹出键盘”\n" +
                    "开启此功能后，您在初始化进入答题页面时，点击“确定回答”按钮刷新问题时，键盘会一并升起，以节约您点击回答输入框弹出键盘的时间。\n" +
                    "这一功能默认关闭。\n" +
                    "之所以不设计为默认开启，是因为考虑到两方面的使用人群。\n" +
                    "一是使用“Android模拟器”的用户，这类用户通常具备实体键盘。\n" +
                    "在此时开启这一功能，有可能会弹出模拟器自带的虚拟键盘，这反而会遮挡视野，带来干扰，降低这类用户的工作效率。\n" +
                    "二是在Android设备上使用键鼠操作的用户，Android其实很早就支持使用键盘和鼠标操作。\n" +
                    "如果开启这一功能，这类用户的“焦点”就会在每次点击“确定回答”的时候，强制回到回答输入框上。\n" +
                    "这就让用户需要用键盘上的方向键反复切换焦点，严重影响工作连贯性和用户体验。\n" +
                    "因此，这个功能建议您按需开启，如果您属于上述两类人群，则我们建议您不要开启此功能。\n" +
                    "2.“伤害值显示”\n" +
                    "开启此功能后，在您和Boss战斗时，每一题结束时，App会在Boss信息栏下方的“--”处短暂显示您本次答题对Boss造成的伤害数值，如果显示有“Critical”，则代表伤害暴击。\n" +
                    "这个功能默认开启，如果您认为这类闪烁的字样对您有负面影响，可以关闭。\n" +
                    "注意：“--”一栏在关闭“伤害值显示”后仍然会常驻Boss信息栏下方，因此无法通过关闭这一功能节约屏幕空间。\n" +
                    "3.“自动清除回答”\n" +
                    "开启此功能后，在点击“确定回答”后，App会自动清除您在答案输入框输入的内容。\n" +
                    "这一功能默认关闭。\n" +
                    "4.“自动大小写”\n" +
                    "这个功能可以将您输入的答案文本当中，所有可被转换大小写的字符（比如：英文字母）统一转换为大写，或是小写。\n" +
                    "注意：这个功能生效时，答案输入框内的文本不会被更改，只有提交了App进行判定的文本会被更改。\n" +
                    "比如，如果您开启了“全部大写”，则您在答案框内输入的“abc”不会改变，而系统会认为您输入的是“ABC”。\n" +
                    "这个功能可以用于回答选择题，避免出现因为大小写不一致导致判定为错误的问题发生。\n" +
                    "注：在您需要输入完整的短语或句子时，请勿开启此功能，它会将短语或句子的全部内容都进行转换，而会忽略语法或位置正确与否。\n" +
                    "上述功能除“伤害值显示”为“主页面”独占外，其余均会在“主页面”和“收藏页面”同时生效。";

    public static final String ACHIEVEMENT_HELP =
            "This Help File is only provided with Chinese edition.\n" +
            "更新日期：2021.7.8\n" +
            "\n" +
            "1.等级成就/达成等级\n" +
            "冒险，启程！2\n" +
            "初心者5\n" +
            "小试牛刀8\n" +
            "初见端倪12\n" +
            "小有成就15\n" +
            "村里最好的剑18\n" +
            "出发，向更远的世界!21\n" +
            "带着希望前行25\n" +
            "新的境遇，挑战，和你。30\n" +
            "强者过招35\n" +
            "未料不敌40\n" +
            "再起45\n" +
            "修行中50\n" +
            "漫漫前路55\n" +
            "跋涉60\n" +
            "回忆着大家的期待65\n" +
            "再遇70\n" +
            "和生活对拼75\n" +
            "不相上下80\n" +
            "和所有人的羁绊85\n" +
            "不惧，直面，出鞘！90\n" +
            "刀落，回望，终焉。95\n" +
            "做自己的勇者100\n" +
            "新增成就：\n" +
            "新世界？ 110\n" +
            "见往昔 120\n" +
            "向上 130\n" +
            "迎面直击 140\n" +
            "面向内心 150\n" +
            "无止无休 160\n" +
            "生平选择 180\n" +
            "现实 200\n" +
            "疲惫 220\n" +
            "丢盔弃甲 240\n" +
            "后退 260\n" +
            "黑幕 280\n" +
            "退场 299\n" +
            "花火，幻觉，和人世 300\n" +
            "\n" +
            "2.积分成就/达成条件\n" +
            "白手起家 10000\n" +
            "勉强维生 30000\n" +
            "第一桶金 100000\n" +
            "小有所成 200000\n" +
            "小康生活 350000\n" +
            "手留余裕 500000\n" +
            "出手阔绰 750000\n" +
            "小富为安 1000000\n" +
            "富甲一方 1500000\n" +
            "地方贵族 2500000\n" +
            "氏族骄傲 4000000\n" +
            "金榜一位 6000000\n" +
            "皇亲国戚 8000000\n" +
            "官爵加身 10000000\n" +
            "锦衣玉食 12500000\n" +
            "众国所倾 15000000\n" +
            "大陆闻名 17500000\n" +
            "尘世尽欲 20000000\n" +
            "财脉系身 30000000\n" +
            "轮回皆知 50000000+\n" +
            "\n" +
            "3.炼金等级成就/达成条件\n" +
            "始习 1\n" +
            "灵根 2\n" +
            "记诵 3\n" +
            "背默 4\n" +
            "有成 5\n" +
            "烂熟 6\n" +
            "品获 7\n" +
            "信己 8\n" +
            "遍籍 9\n" +
            "研古 10\n" +
            "寻真 11\n" +
            "灼见 12\n" +
            "亲思 13\n" +
            "历卷 14\n" +
            "仙眼 15\n" +
            "\n" +
            "4 “比武台”评价成就/达成条件\n" +
            "开幕，1\n" +
            "直击，5000\n" +
            "较量，10000\n" +
            "力战，20000\n" +
            "奋勇，30000\n" +
            "演武，40000\n" +
            "冲撞，50000\n" +
            "搏斗，60000\n" +
            "巧思，70000\n" +
            "艰险，70000\n" +
            "化难，100000\n" +
            "定势，120000\n" +
            "有余，150000\n" +
            "道法，180000\n" +
            "无常，210000\n" +
            "飘摇，240000\n" +
            "刚柔，270000\n" +
            "并济，300000\n" +
            "无双，350000";

    //sentences source, thanks to:
    // https://cn.bing.com/search?q=%E8%8B%B1%E8%AF%AD%E5%90%8D%E8%A8%80&form=ANNTH1&refig=19a61788efd9413b9293c3ae3c505e06&sp=1&qs=AS&pq=%E8%8B%B1%E8%AF%AD%E5%90%8D%E8%A8%80&sc=8-4&cvid=19a61788efd9413b9293c3ae3c505e06 !
    // https://www.lz13.cn/mingrenmingyan/42992.html
    public static final String[] SIGN_SENTENCES_EN = {
            "To be both a speaker of words and a doer of deeds.",
            "Variety is the spice of life.",
            "Bad times make a good man.",
            "There is no royal road to science ,and only those who do not dread the fatiguing climb of gaining its numinous summits.",
            "Doubt is the key to knowledge.",
            "We cannot always build the future for our youth, but we can build our youth for the future.",
            "Sow nothing, reap nothing.",
            "Conceit is the quicksand of success.",
            "The important thing in life is to have a great aim , and the determination to attain it.",
            "The man with a new idea is a crank until the idea succeeds.",
            "Good is good, but better carries it.",
            "When an end is lawful and obligatory, the indispensable means to is are also lawful and obligatory.",
            "Our destiny offers not the cup of despair, but the chalice of opportunity.",
            "The people who get on in this world are the people who get up and look for circumstances they want, and if they cannot find them .they make them.",
            "Love consists in desiring to give what is our own to another and feeling his delight as our own.",
            "It is easy to be wise after the event.",
            "Where there is a will, there is a way.",
            "The first step is as good as half over.",
            "Genius only means hard-working all one's life.",
            "The man who has made up his mind to win will never say \"impossible \".",
            "The only limit to our realization of tomorrow will be our doubts of today ."
    };

    public static final String[] SIGN_SENTENCES_CN = {
            "既当演说家，又做实干家。",
            "变化是生活的调味品。",
            "艰难困苦出能人。",
            "在科学上没有平坦的大道，只有不畏劳苦沿着其崎岖之路攀登的人，才有希望达到它光辉的顶点。  ——马克思",
            "怀疑是知识的钥匙。",
            "我们不能总是为我们的青年造就美好未来，但我们能够为未来造就我们的青年一代。 ——罗斯福",
            "春不播，秋不收。",
            "自负是成功的流沙。",
            "人生重要的事情就是确定一个伟大的目标，并决心实现它。—— 歌德",
            "具有新想法的人在其想法实现之前是个怪人。 ——马克·吐温",
            "精益求精，善益求善。",
            "如果一个目的是正当而必须做的，则达到这个目的的必要手段也是正当而必须采取的。—— 林肯",
            "命运给予我们的不是失望之酒，而是机会之杯。 ——尼克松",
            "在这个世界上，取得成功的人是那些努力寻找他们想要机会的人，如果找不到机会，他们就去创造机会。—— 肖伯纳",
            "爱由付出自己和分享他的快乐组成。",
            "事后诸葛亮容易当。",
            "有志者，事竟成。——爱迪生",
            "第一步是最关键的一步。",
            "天才只意味着终身不懈的努力。——门捷列耶夫",
            "凡是决心取得胜利的人是从来不说“不可能的”。——拿破仑",
            "实现明天理想的唯一障碍是今天的疑虑。 ——罗斯福"
    };
}