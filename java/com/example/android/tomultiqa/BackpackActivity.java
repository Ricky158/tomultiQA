package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BackpackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpack);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        //1.initializing recyclerview.
        //1.1 prepare content which needed to be showed to user.
        //1.2 add content to ArrayList, data from ItemDataBase.
        InitializingBackpack();
        //1.3 prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //1.3.1 Put list data in Adapter.
        LetterAdapter mLetterAdapter = new LetterAdapter(ItemNameList);
        //1.4 prepare RecyclerView object.
        RecyclerView BackpackView = findViewById(R.id.BackpackView);
        //1.5 Put Adapter to RecyclerView.
        BackpackView.setAdapter(mLetterAdapter);
        //1.6 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        BackpackView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //1.7 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backpack_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_BackPackHelp) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    "This Help File is only provided with Chinese edition.\n" +
                            "更新日期：2021/6/5\n" +
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
                            "5.2 稀有（Rare）品质\n" +
                            " 5.2.1 精灵树之木（Elf Tree Wood）/资源/稀有；\n" +
                            "自精灵一族居住的森林当中获得的树木。就产量而言，这一材料并不稀有。真正让它具备价值的原因，在于精灵族对森林的守护。能从精灵森林的重重陷阱当中逃脱的伐木工可为数不多。（行商集获取）\n" +
                            " 5.2.2 元素流瓶（Element Flow Bottle）/资源/稀有；\n" +
                            "装有流动元素的玻璃瓶，是缺乏魔力者和初学者使用魔法的重要道具。只有熟练的炼金术师和法师们联合才能制作。且其市场流通被严格监管，供不应求。（行商集获取）\n" +
                            "5.3 精制（Decent）品质\n" +
                            " 5.3.1 灵气石（Spirit Stone）/资源/精制；\n" +
                            "蕴含灵气的矿石，可以从中提取出编制魔法术式的能量，或是提炼过后，作为机械的动力源。是一种广泛使用的资源。不过，矿石本身其实价值有限，其珍贵实际上体现在被提炼的纯度上。（行商集获取）\n" +
                            " 5.3.2 纯洁束绳（Pure String）/资源/精制；\n" +
                            "通过精心清洁和祝福的绳子，相较于它“弱不禁风”的外观，它的束缚力其实很大。一些贵族也因为其纯白无暇的外观而将这种绳子用于装饰。是一种兼具实用和装饰性的资源。（行商集获取）\n" +
                            "5.4 普通（Normal）品质\n" +
                            " 5.4.1 韧铁（Solid Metal）/资源/普通；\n" +
                            "经过铁匠锻造，塑性的优质金属。是被用于制作武器，防具等用途的素材。注意，虽然名字叫“韧铁”，但不一定是金属铁，也可能是其他物质的混合体。打造这些金属的细节，是各个地区的铁匠们不约而同要保守的秘密。（行商集获取）\n" +
                            "5.4.2 蚀金（Curved Gold）/资源/普通；\n" +
                            "金银等贵金属具备良好的魔法适应性，是用于制作法器，魔法制品的好素材。“蚀金”就是通过炼金术师之手刻蚀的黄金。其上的刻痕可以是魔力的流动更为畅通，但有市井传言道：真正优良的“蚀金”是另有制作方法的。这可能就是普通的“蚀金”明明含有不少黄金，价值却平平无奇的原因吧。（行商集获取）",
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        ReturnCursor.close();//Stop Using Cursor.
    }

    //RecyclerView support code, thanks to: https://www.cnblogs.com/rustfisher/p/12254732.html !
    //lv.2 Class, Adapter, used in load data from ViewHolder and put data into RecyclerView. Sub Class of RecyclerView.
    private class LetterAdapter extends RecyclerView.Adapter<VH> {

        private final List<String> dataList;

        public LetterAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_backpack, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {//bind (combine) data with [each] item in RecyclerView.
            final String NameString = dataList.get(position);//load data needed to be shown from dataList.
            holder.ItemNameView.setText(NameString);//set TextView`s text in the item.
            //set OnClickListener to each TextView in the item.
            holder.ItemNameView.setOnClickListener(v -> {//do something after Clicked.
                ItemSelectedId = position;//return selected item Id to int field.
                LoadSelectedItem();//load data from Cursor and show to user.
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    //lv.1 Class, ViewHolder, sub Class of onCreateViewHolder() method in [Adapter] Class.
    //control the content of each item in RecyclerView.
    private static class VH extends RecyclerView.ViewHolder {
        View item; // 我们希望拿到整个item的view
        TextView ItemNameView;

        public VH(@NonNull View itemView) {//ViewHolder Constructor definition.
            super(itemView);//declare import method from Father Class.
            item = itemView;//store item in RecyclerView to be used by another method in different Class.
            ItemNameView = itemView.findViewById(R.id.ItemNameView);//import item xml from project file.
        }
    }//end of RecyclerView support code.


    //Backpack main part.
    //database part.
    Cursor ReturnCursor;//store all data from database.
    List <String> ItemNameList = new ArrayList<>();//store all ItemName data.
    //which any Item in RecyclerView was selected, its Name and Text(Description) to be showed will be wrote in here.
    int ItemSelectedId = -1;
    String ItemTextToShow = "--";
    String ItemTypeToShow = "--";
    int ItemNumberToShow = 0;

    //lv.2 method, main method.
    //thanks to: https://stackoverflow.com/questions/7222873/how-to-test-if-cursor-is-empty-in-a-sqlitedatabase-query ！
    private void InitializingBackpack(){
        ItemDataBaseBasic DBHelper = new ItemDataBaseBasic(this);
        SQLiteDatabase db = DBHelper.getReadableDatabase();
        //1.query entire backpack database, store all query result.
        ReturnCursor = db.query(
                ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        //2.load data to variables.
        if(ReturnCursor != null && ReturnCursor.getCount() > 0){//prevent from NPE.
            ReturnCursor.moveToFirst();//prevent from -1 position.
            //calculate variables and keep them to improve efficiency.
            int ItemNameColumnId = ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName);
            boolean IsMoreData = !ReturnCursor.isLast();//check Is it more data available to query.
            do {//query at first position of Cursor.
                if(ReturnCursor.isLast()){
                    IsMoreData = false;
                }
                ItemNameList.add(ReturnCursor.getString(ItemNameColumnId));
                ReturnCursor.moveToNext();
            }while (IsMoreData);//if IsMoreData is false(no more data), stop query, if not, continue query.
        }else{//no data in Cursor.
           SupportClass.CreateNoticeDialog(this,getString(R.string.ErrorWordTran),"No Data in backpack.",getString(R.string.ConfirmWordTran));
           //prevent from NPE and empty UI.
           ItemNameList.add(getString(R.string.NothingWordTran));
           ItemTextToShow = getString(R.string.NothingSelectedTran);
           ItemTypeToShow = getString(R.string.NoTypeTran);
           ItemNumberToShow = 0;
        }
        //3.show empty information to user.
        ShowItemInformation();
    }

    //lv.2 method, sub method of onBindViewHolder() method in [Adapter] Class.
    public void LoadSelectedItem(){
        if(ReturnCursor != null && ReturnCursor.getCount() > 0 && ItemSelectedId != -1){
            ReturnCursor.moveToPosition(ItemSelectedId);
            ItemTypeToShow = ReturnCursor.getString(ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemType));
            ItemTextToShow = ReturnCursor.getString(ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemText));
            ItemNumberToShow = ReturnCursor.getInt(ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber));
            ShowItemInformation();
        }
    }

    //lv.1 method, sub method of LoadSelectedItem() and InitializingBackpack() method.
    @SuppressLint("SetTextI18n")
    private void ShowItemInformation(){
        TextView ItemTypeView = findViewById(R.id.ItemTypeView);
        TextView ItemTextView = findViewById(R.id.ItemTextView);
        TextView ItemNumberView = findViewById(R.id.ItemNumberView);
        ItemTypeView.setText(getString(R.string.ItemTypeWordTran) + " " +ItemTypeToShow);
        ItemTextView.setText(ItemTextToShow);
        ItemNumberView.setText(getString(R.string.ItemNumberWordTran) + " " + ItemNumberToShow);
    }//end of Backpack main part.
// TODO: 2021/6/3 working on button operation. 

    //Item Type Select Function.
//    public void ResourceItemType(View view){
//        TabItem ResourceTypeItem = findViewById(R.id.ResourceTypeItem);
//        if(ResourceTypeItem.isSelected()){
//            //......
//        }
//    }
//
//    public void ItemTypeSelect(View view){
//        TabItem ItemTypeItem = findViewById(R.id.ItemTypeItem);
//        if(ItemTypeItem.isSelected()){
//            //......
//        }
//    }
//
//    public void PreTypeSelect(View view){//"Pre" means "Precious".
//        TabItem PreciousTypeItem = findViewById(R.id.PreciousTypeItem);
//        if(PreciousTypeItem.isSelected()){
//            //......
//        }
//    }//end of Item Type Select Function.
}
