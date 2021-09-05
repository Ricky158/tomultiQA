package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarketActivity extends AppCompatActivity {

    private static final int ITEM_NUMBER_LIMIT = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        InitializingResourceData();
        MarketPriceChange();
        //DO NOT MOVE InitializingOpeningPool() and InitializingDataBase() `s position.
        InitializingOpeningPool();
        InitializingDataBase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //these methods may create some complicate crash issues, so we disabled them for a while.
//        if(dbRead.isOpen()){
//            dbRead.close();
//        }
//        if(dbWrite.isOpen()){
//            dbWrite.close();
//        }
        if(!ResultCursor.isClosed()){
            ResultCursor.close();
        }
    }

    //Market price system.
    int BoxMarketPrice = 3500;
    int KeyMarketPrice = 6500;

    @SuppressLint("SetTextI18n")
    private void MarketPriceChange(){
        TextView BoxMarketPriceView = findViewById(R.id.BoxMarketPrice);
        TextView KeyMarketPriceView = findViewById(R.id.KeyMarketPrice);
        double PriceIndex;
        int BoxPriceChanged;
        int KeyPriceChanged;
        boolean IsUsualDay = true;//In game, some day will have impossible Price.
        boolean IsUserWealthy = true;//by default, we think user are wealthy.
        //1.confirm Is it a Usual Day?
        if(SupportClass.CreateRandomNumber(1,100) < 3){//3% Unusual possibility.
            IsUsualDay = false;
        }
        //2.confirm Is user wealthy?
        if(UserPoint < 1000000){
            IsUserWealthy = false;
        }
        //3.confirm user Point state.and change the market price according to State.
        if(IsUserWealthy && IsUsualDay){
            PriceIndex = (double) SupportClass.CreateRandomNumber(-500,500) / 1000;
        }else if(IsUserWealthy){//IsUsualDay is false.
            PriceIndex = (double) SupportClass.CreateRandomNumber(-2000,2000) / 1000;
        }else if(IsUsualDay){//IsUserWealthy is false.
            PriceIndex = (double) SupportClass.CreateRandomNumber(-1200,1200) / 1000;
        }else{//IsUsualDay and IsUserWealthy are all false.
            PriceIndex = (double) SupportClass.CreateRandomNumber(-250,250) / 1000;
        }
        //4.calculate Price changed, but not change the actual value yet.
        BoxPriceChanged = Math.max(1, (int) (BoxMarketPrice * (1 + PriceIndex) ) );
        KeyPriceChanged = Math.max(1, (int) (KeyMarketPrice * (1 + PriceIndex) ) ) ;
        //5.show / report Price which after changed to user.
        BoxMarketPriceView.setText(SupportClass.ReturnKiloIntString(BoxPriceChanged) + " " + getString(R.string.PointPerBoxTran));
        KeyMarketPriceView.setText(SupportClass.ReturnKiloIntString(KeyPriceChanged) + " " + getString(R.string.PointPerKeyTran));
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.MarketPriceChangedTran) + "\n" +
                        getString(R.string.PointPerBoxTran) + "\n" +
                        BoxMarketPrice + " → "+ BoxPriceChanged + "\n" +
                        getString(R.string.PointPerKeyTran) + "\n" +
                        KeyMarketPrice + " → " + KeyPriceChanged,
                getString(R.string.ConfirmWordTran)
                );
        //6.after report, apply calculation to actual Price Value.
        BoxMarketPrice = BoxPriceChanged;
        KeyMarketPrice = KeyPriceChanged;
    }//Market price system.


    //Market Item IO system.
    //record all get-able items in game.(OpeningPool)
    List<String> EpicItemList = new ArrayList<>();
    List<String> RareItemList = new ArrayList<>();
    List<String> DecentItemList = new ArrayList<>();
    List<String> NormalItemList = new ArrayList<>();

    //database object in RAM, initialized in InitializingDataBase().
    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;
    Cursor ResultCursor;

    //lv.3 method, main method.
    private void InitializingDataBase(){
        ItemDataBaseBasic DBHelper = new ItemDataBaseBasic(this);
        dbRead = DBHelper.getReadableDatabase();
        dbWrite = DBHelper.getWritableDatabase();
        //get data from database to fill Cursor, and save it to field. initializing done.
        SearchDataBase(null,null);
    }

    //lv.2 method, main method.
    //thanks to: https://developer.android.google.cn/training/data-storage/sqlite#ReadDbRow !
    // https://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list !
    public void OpenMarketItem(View view){
        //0.preparation.
        final EditText NumberView = new EditText(this);
        //1.let user input box number needed to be opened.
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.OpenNumberInputTran));
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {
                    //3.0 preparation.
                    //3.1 get user Input Number.
                    int OpenNumber = SupportClass.getInputNumber(NumberView);
                    //3.1.1 check OpenNumber, Box, Key Number.
                    if(OpenNumber >= UserBoxNumber || OpenNumber >= UserKeyNumber){
                        OpenNumber = Math.min(UserBoxNumber,UserKeyNumber);
                    }
                    //prevent from too large number.
                    OpenNumber = Math.min(OpenNumber,500);
                    String GotItemName;//variable used in record Got Item Name.
                    List<String> GotItemList = new ArrayList<>();
                    //3.2 Cost Box and Key.
                    CostBox(OpenNumber);
                    CostKey(OpenNumber);
                    //3.3 start multi Opening.
                    int OpenTime = OpenNumber;//variable used in while circulation.
                    while (OpenTime > 0){
                        //3.4 start 1 time Opening.
                        OpenTime = OpenTime - 1;
                        //3.5 confirm random number.
                        int Random = SupportClass.CreateRandomNumber(1,100);
                        //3.6 check result and give reward to user.
                        if(Random == 1){//get Epic Item.(1/100, 1%)
                            GotItemName = ReturnGotItemName(EpicItemList);
                        }else if(Random < 15){//get Rare Item.(2~14, 12%)
                            GotItemName = ReturnGotItemName(RareItemList);
                        }else if(Random < 41){//get Decent item.(16~40, 24%)
                            GotItemName = ReturnGotItemName(DecentItemList);
                        }else{//get Normal Item.(42~100, 58%)
                            GotItemName = ReturnGotItemName(NormalItemList);
                        }
                        //3.7 keep the reward name to list, which used to show the detail to user.
                        GotItemList.add(GotItemName);
                        //4.according to GotItemName variable, save data to database.
                        //4.0 prepare method parameter.
                        String Selection = ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
                        String[] SelectionArgs = {GotItemName};
                        ContentValues ItemNumberValues = new ContentValues();
                        //4.1 get data from database, and fill Cursor.
                        SearchDataBase(Selection,SelectionArgs);
                        //4.1.1 get if Item Exist situation from Cursor.
                        boolean IsItemExist = ResultCursor.moveToFirst();
                        //4.2 do Item data insert / update branch.
                        if(IsItemExist){//4.2.1 if item is in database.
                            int ItemNumberInDb = ResultCursor.getInt(ResultCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber));
                            //4.2.1.1 change the number and save changes.
                            ItemNumberValues.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,ItemNumberInDb + 1);
                            dbWrite.update(
                                    ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                                    ItemNumberValues,
                                    Selection,
                                    SelectionArgs
                            );
                        }else{//4.2.2 if item is not in database.
                            //4.2.2.1 initializing item column in database, and the number will be set to 1.
                            ItemNumberValues.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,1);
                            NewItemColumn(GotItemName,getString(R.string.ResourceWordTran),getItemText(GotItemName));
                        }
                    }
                    //5.finish all work.
                    dialog1.cancel();
                    SupportClass.CreateNoticeDialog(this,getString(R.string.TheItemYouGotTran),GotItemList.toString(),getString(R.string.ConfirmWordTran));
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.2 method, main method, provide buying function.
    @SuppressLint("SetTextI18n")
    public void BuyMarketItem(View view){
        //1.prepare Input layout.
        final EditText NumberView = new EditText(this);
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        NumberView.setHint(getString(R.string.InputBuyNumberHintTran));
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.YouAreBuyingTran) + ItemType + "].");
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//3.do buying work.
                    int BuyNumber = SupportClass.getInputNumber(NumberView);
                    int BoxTotal = BoxMarketPrice * BuyNumber;
                    int KeyTotal = KeyMarketPrice * BuyNumber;

                    if(ItemType.equals("Box") && UserPoint >= BoxTotal && UserBoxNumber <= ITEM_NUMBER_LIMIT){
                        CostPoint(BoxTotal);
                        GetBox(BuyNumber);
                    }else if(ItemType.equals("Key") && UserPoint >= KeyTotal && UserKeyNumber <= ITEM_NUMBER_LIMIT){
                        CostPoint(KeyTotal);
                        GetKey(BuyNumber);
                    }else if(UserBoxNumber > ITEM_NUMBER_LIMIT || UserKeyNumber > ITEM_NUMBER_LIMIT){
                       SupportClass.CreateNoticeDialog(this,
                               getString(R.string.ErrorWordTran),
                               getString(R.string.ItemNumberLimitHintTran),
                               getString(R.string.ConfirmWordTran)
                       );
                    }else{
                        Toast.makeText(this, getString(R.string.NotEnoughPointTran), Toast.LENGTH_SHORT).show();
                    }
                    dialog1.cancel();
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.2 method, main method.
    public void SellMarketItem(View view){
        //1.prepare Input layout.
        final EditText NumberView = new EditText(this);
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        NumberView.setHint(getString(R.string.InputSellNumberHintTran));
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("[" +  ItemType + getString(R.string.HintSellNumberTran));
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//3.do selling work.
                    int CostNumber = SupportClass.getInputNumber(NumberView);
                    //3.1, 100 items is max limit for once selling.
                    if(CostNumber > 100){
                        CostNumber = 100;
                        Toast.makeText(this, getString(R.string.MarketSellingLimitTran), Toast.LENGTH_SHORT).show();
                    }
                    int BoxSelling = BoxMarketPrice * CostNumber;
                    int KeySelling = KeyMarketPrice * CostNumber;

                    if(ItemType.equals("Box") && UserBoxNumber >= CostNumber){
                        CostBox(CostNumber);
                        GetPoint(BoxSelling);
                        MarketPriceChange();
                    }else if(ItemType.equals("Key") && UserKeyNumber >= CostNumber){
                        CostKey(CostNumber);
                        GetPoint(KeySelling);
                        MarketPriceChange();
                    }else{
                        Toast.makeText(this, getString(R.string.NotEnoughItemTran), Toast.LENGTH_SHORT).show();
                    }
                    dialog1.cancel();
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.1 method, main method.
    // TODO: Define Each Rare rank 's item list here.
    private void InitializingOpeningPool(){
        //Epic Rank.
        EpicItemList.add(getString(R.string.MagicPieceTran));
        //Rare Rank.
        RareItemList.add(getString(R.string.ElfTreeWoodTran));
        RareItemList.add(getString(R.string.ElementFlowBottleTran));
        //Decent Rank.
        DecentItemList.add(getString(R.string.SpiritStoneTran));
        DecentItemList.add(getString(R.string.PureStringTran));
        //Normal Rank.
        NormalItemList.add(getString(R.string.SoildMetalTran));
        NormalItemList.add(getString(R.string.CurvedGoldTran));
        //show get-able item list to user.
        TextView RewardShowView = new TextView(this);
        List<String> AllItemList = new ArrayList<>();
        AllItemList.add(EpicItemList.toString() + "\n" + RareItemList.toString() + "\n" + DecentItemList.toString() + "\n" + NormalItemList.toString());
        LinearLayout BoxRewardLayout = findViewById(R.id.BoxRewardLayout);
        RewardShowView.setText(AllItemList.get(0));
        BoxRewardLayout.addView(RewardShowView);
    }

    //lv.1 method, sub method of BuyItem() method.
    private String getItemText(String ItemName){
        String Text = "--";
        if(ItemName.equals(getString(R.string.MagicPieceTran))){
            Text = "这是“世界”经历大战后，古人遗留下来的魔力凝结的物质。是极佳的素材。被各国的商人们高价收购。据说法术学院当中的那些“帽子”们，在它身上发现了重大的秘密。";
        }else if(ItemName.equals(getString(R.string.ElfTreeWoodTran))){
            Text = "自精灵一族居住的森林当中获得的树木。就产量而言，这一材料并不稀有。真正让它具备价值的原因，在于精灵族对森林的守护。能从精灵森林的重重陷阱当中逃脱的伐木工可为数不多。";
        }else if(ItemName.equals(getString(R.string.ElementFlowBottleTran))){
            Text = "装有流动元素的玻璃瓶，是缺乏魔力者和初学者使用魔法的重要道具。只有熟练的炼金术师和法师们联合才能制作。且其市场流通被严格监管，供不应求。";
        }else if(ItemName.equals(getString(R.string.DreamFruitTran))){
            Text = "虽然“清梦果”的外形酷似果实，但需要澄清的是，尽管名字里有“果”，但清梦果实际上并不能食用。曾经有人品尝过它，但当人们询问他滋味时，他却无法表达出来。也因此，它被巫师们用作忘记人间疾苦的道具，也就有了“清梦”的名号。（合成获得）";
        }else if(ItemName.equals(getString(R.string.SpiritStoneTran))){
            Text = "蕴含灵气的矿石，可以从中提取出编制魔法术式的能量，或是提炼过后，作为机械的动力源。是一种广泛使用的资源。不过，矿石本身其实价值有限，其珍贵实际上体现在被提炼的纯度上。";
        }else if(ItemName.equals(getString(R.string.PureStringTran))){
            Text = "通过精心清洁和祝福的绳子，相较于它“弱不禁风”的外观，它的束缚力其实很大。一些贵族也因为其纯白无暇的外观而将这种绳子用于装饰。是一种兼具实用和装饰性的资源。";
        }else if(ItemName.equals(getString(R.string.SoildMetalTran))){
            Text = "经过铁匠锻造，塑性的优质金属。是被用于制作武器，防具等用途的素材。注意，虽然名字叫“韧铁”，但不一定是金属铁，也可能是其他物质的混合体。打造这些金属的细节，是各个地区的铁匠们不约而同要保守的秘密。";
        }else if(ItemName.equals(getString(R.string.CurvedGoldTran))){
            Text = "金银等贵金属具备良好的魔法适应性，是用于制作法器，魔法制品的好素材。“蚀金”就是通过炼金术师之手刻蚀的黄金。其上的刻痕可以是魔力的流动更为畅通，但有市井传言道：真正优良的“蚀金”是另有制作方法的。这可能就是普通的“蚀金”明明含有不少黄金，价值却平平无奇的原因吧。";
        }
        return Text;
    }

    //lv.1 method, sub method of InitializingDataBase() method.
    private void NewItemColumn(String ItemName, String ItemType, String ItemText){
        ContentValues values = new ContentValues();
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName,ItemName);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemType,ItemType);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemText,ItemText);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,0);
        dbWrite.insert(ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,null,values);
    }

    //lv.1 method, sub method of InitializingDataBase() method.
    private void SearchDataBase(String Selection, String[] SelectionArgs){
        ResultCursor = dbRead.query(//let Cursor return the ItemName and ItemNumber data in this name line.
                ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                null,
                Selection,
                SelectionArgs,
                null,
                null,
                null
        );
    }

    //lv.1 method, sub method of OpenMarketItem() method. randomly return one item of entire list.
    //thanks to: https://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list !
    private String ReturnGotItemName(List<String> NeedToGet){
        Random randomizer = new Random();
        return NeedToGet.get(randomizer.nextInt(NeedToGet.size()));
    }//end of Market Item IO system.


    //Item select function.
    String ItemType = "Box";//not initialized state.
    
    public void ChangeItemType(View view){//change user selected Item Type.
        RadioGroup ItemTypeChoose = findViewById(R.id.ItemTypeChoose);
        RadioButton BoxTypeChoose = findViewById(R.id.BoxTypeChoose);
        RadioButton KeyTypeChoose = findViewById(R.id.KeyTypeChoose);
        int SelectedId = ItemTypeChoose.getCheckedRadioButtonId();
        if(SelectedId == BoxTypeChoose.getId()){
            ItemType = "Box";
        }else if(SelectedId == KeyTypeChoose.getId()){
            ItemType = "Key";
        }
    }//end of Item select function.


    //Detail Button Function.
    public void ShowOpeningDetail(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                ValueClass.OPEN_POSSIBILITY_HELP,
                getString(R.string.ConfirmWordTran));
    }//end of Detail Button Function.


    //resource system.
    int UserBoxNumber = 0;
    int UserKeyNumber = 0;
    int UserPoint = 0;
    long PointRecord = 0;

    private void InitializingResourceData(){//main import data method.
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        UserBoxNumber = SupportClass.getIntData(this,"BattleDataProfile","UserBoxNumber",0);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserKeyNumber = SupportClass.getIntData(this,"BattleDataProfile","UserKeyNumber",0);
        PointRecord = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
        //make number into financial form.
        BoxCountingView.setText(SupportClass.ReturnKiloIntString(UserBoxNumber));
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
        KeyCountingView.setText(SupportClass.ReturnKiloIntString(UserKeyNumber));
    }

    private void GetBox(int addNumber){//Box IO method.
        UserBoxNumber = UserBoxNumber + addNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserBoxNumber",UserBoxNumber);
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        BoxCountingView.setText(SupportClass.ReturnKiloIntString(UserBoxNumber));
    }

    private void CostBox(int CostNumber){//Box IO method.
        UserBoxNumber = UserBoxNumber - CostNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserBoxNumber",UserBoxNumber);
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        BoxCountingView.setText(SupportClass.ReturnKiloIntString(UserBoxNumber));
    }

    private void GetKey(int addNumber){//Key IO method.
        UserKeyNumber = UserKeyNumber + addNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserKeyNumber",UserKeyNumber);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        KeyCountingView.setText(SupportClass.ReturnKiloIntString(UserKeyNumber));
    }

    private void CostKey(int CostNumber){
        UserKeyNumber = UserKeyNumber - CostNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserKeyNumber",UserKeyNumber);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        KeyCountingView.setText(SupportClass.ReturnKiloIntString(UserKeyNumber));
    }

    private void GetPoint(int addNumber){//Point IO method.
        UserPoint = UserPoint + addNumber;
        PointRecord = PointRecord + addNumber;
        SupportClass.saveLongData(this,"RecordDataFile","PointGotten",PointRecord);
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
    }

    private void CostPoint(int CostNumber){//Point IO method.
        UserPoint = UserPoint - CostNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
    }//end of resource system.

    public void GoToBackpack(View view){
        Intent i = new Intent(MarketActivity.this, BackpackActivity.class);
        startActivity(i);
    }
}