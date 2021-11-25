package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
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
        //DO NOT MOVE InitializingOpeningPool() and Initializing ItemIO() `s position.
        InitializingOpeningPool();
        //Initializing Item database.
        itemIO = new ItemIO(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        resourceIO.ApplyChanges(this);
    }

    //Market price system.
    int BoxMarketPrice = 3500;
    int KeyMarketPrice = 6500;

    //lv.2 main method, big number changes in price.
    @SuppressLint("SetTextI18n")
    private void MarketPriceChange(){
        double PriceIndex;
        int BoxPriceChanged;
        int KeyPriceChanged;
        boolean IsUsualDay = true;//In game, some day will have impossible Price.
        boolean IsUserWealthy = true;//by default, we think user are wealthy.
        //1.confirm Is it a Usual Day?
        if(SupportLib.CreateRandomNumber(1,100) < 3){//3% Unusual possibility.
            IsUsualDay = false;
        }
        //2.confirm Is user wealthy?
        if(resourceIO.UserPoint < 1000000){
            IsUserWealthy = false;
        }
        //3.confirm user Point state.and change the market price according to State.
        if(IsUserWealthy && IsUsualDay){
            PriceIndex = (double) SupportLib.CreateRandomNumber(-250,250) / 1000;
        }else if(IsUserWealthy){//IsUsualDay is false.
            PriceIndex = (double) SupportLib.CreateRandomNumber(-1000,1000) / 1000;
        }else if(IsUsualDay){//IsUserWealthy is false.
            PriceIndex = (double) SupportLib.CreateRandomNumber(-600,600) / 1000;
        }else{//IsUsualDay and IsUserWealthy are all false.
            PriceIndex = (double) SupportLib.CreateRandomNumber(-125,125) / 1000;
        }
        //4.calculate Price changed, but not change the actual value yet.
        BoxPriceChanged = Math.max(1, (int) (BoxMarketPrice * (1 + PriceIndex) ) );
        KeyPriceChanged = Math.max(1, (int) (KeyMarketPrice * (1 + PriceIndex) ) ) ;
        //5.show / report Price which after changed to user.
        RefreshPrice(BoxPriceChanged,KeyPriceChanged);
    }

    //lv.2 main method, slight movement of price.
    private void MarketPriceMove(){
        int BoxPriceChanged = BoxMarketPrice + SupportLib.CreateRandomNumber(-200,200);
        int KeyPriceChanged = KeyMarketPrice + SupportLib.CreateRandomNumber(-500,500);
        RefreshPrice(BoxPriceChanged,KeyPriceChanged);
    }

    //lv.1 sub method of MarketPriceChange/Move().
    @SuppressLint("SetTextI18n")
    private void RefreshPrice(int BoxPriceChanged, int KeyPriceChanged){
        TextView BoxMarketPriceView = findViewById(R.id.BoxMarketPrice);
        TextView KeyMarketPriceView = findViewById(R.id.KeyMarketPrice);
        //1.show / report Price which after changed to user.
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        BoxMarketPriceView.setText(SupportLib.ReturnKiloIntString(BoxPriceChanged) + " " + getString(R.string.PointPerBoxTran));
        KeyMarketPriceView.setText(SupportLib.ReturnKiloIntString(KeyPriceChanged) + " " + getString(R.string.PointPerKeyTran));
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.MarketPriceChangedTran) + "\n\n" +
                         "> " + BoxPriceChanged + getString(R.string.PointPerBoxTran) + "\n\n" +
                         "> " + KeyPriceChanged + getString(R.string.PointPerKeyTran),
                getString(R.string.ConfirmWordTran)
        );
        //2.after report, apply calculation to actual Price Value.
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
    ItemIO itemIO;

    //lv.2 method, main method.
    //thanks to: https://developer.android.google.cn/training/data-storage/sqlite#ReadDbRow !
    // https://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list !
    @SuppressLint("Range")
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
        dialog.setMessage(
                getString(R.string.LootBoxTran) + UserBoxNumber + "\n" +
                getString(R.string.MarketKeyWordTran) + ":" + UserKeyNumber);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {
                    //3.0 preparation.
                    //3.1 get user Input Number.
                    int OpenNumber = SupportLib.getInputNumber(NumberView);
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
                        int Random = SupportLib.CreateRandomNumber(1,100);
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
                        //4.2 do Item data insert / update.
                        itemIO.EditOneItem(GotItemName,getString(R.string.ResourceWordTran),1);
                    }
                    //5.finish all work.
                    dialog1.cancel();
                    SupportLib.CreateNoticeDialog(this,getString(R.string.TheItemYouGotTran),GotItemList.toString(),getString(R.string.ConfirmWordTran));
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
        dialog.setTitle(getString(R.string.YouAreBuyingTran) + getMarketItemName() + "].");
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//3.do buying work.
                    int BuyNumber = SupportLib.getInputNumber(NumberView);
                    int BoxTotal = BoxMarketPrice * BuyNumber;
                    int KeyTotal = KeyMarketPrice * BuyNumber;

                    if(BuyNumber > 0){
                        if(ItemType == 0 && resourceIO.UserPoint >= BoxTotal && UserBoxNumber <= ITEM_NUMBER_LIMIT){
                            resourceIO.CostPoint(BoxTotal);
                            GetBox(BuyNumber);
                            MarketPriceMove();
                        }else if(ItemType == 1 && resourceIO.UserPoint >= KeyTotal && UserKeyNumber <= ITEM_NUMBER_LIMIT){
                            resourceIO.CostPoint(KeyTotal);
                            GetKey(BuyNumber);
                            MarketPriceMove();
                        }else if(UserBoxNumber > ITEM_NUMBER_LIMIT || UserKeyNumber > ITEM_NUMBER_LIMIT){
                            SupportLib.CreateNoticeDialog(this,
                                    getString(R.string.ErrorWordTran),
                                    getString(R.string.ItemNumberLimitHintTran),
                                    getString(R.string.ConfirmWordTran)
                            );
                        }else{
                            Toast.makeText(this, getString(R.string.NotEnoughPointTran), Toast.LENGTH_SHORT).show();
                        }
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
        dialog.setTitle("[" +  getMarketItemName() + getString(R.string.HintSellNumberTran));
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        if(ItemType == 0){
            dialog.setMessage(
                    getString(R.string.LootBoxTran) + UserBoxNumber + "\n" +
                    getString(R.string.PointPerBoxTran) + ":" + BoxMarketPrice);
        }else if(ItemType == 1){
            dialog.setMessage(
              getString(R.string.MarketKeyWordTran) + ":" + UserKeyNumber + "\n" +
              getString(R.string.PointPerKeyTran) + ":" + KeyMarketPrice);
        }
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//3.do selling work.
                    int CostNumber = SupportLib.getInputNumber(NumberView);
                    //3.1, 100 items is max limit for once selling.
                    if(CostNumber > 100){
                        CostNumber = 100;
                        Toast.makeText(this, getString(R.string.MarketSellingLimitTran), Toast.LENGTH_SHORT).show();
                    }
                    int BoxSelling = BoxMarketPrice * CostNumber;
                    int KeySelling = KeyMarketPrice * CostNumber;

                    if(CostNumber > 0){
                        if(ItemType == 0 && UserBoxNumber >= CostNumber){
                            CostBox(CostNumber);
                            resourceIO.GetPoint(BoxSelling);
                            MarketPriceMove();
                        }else if(ItemType == 1 && UserKeyNumber >= CostNumber){
                            CostKey(CostNumber);
                            resourceIO.GetPoint(KeySelling);
                            MarketPriceMove();
                        }else{
                            Toast.makeText(this, getString(R.string.NotEnoughItemTran), Toast.LENGTH_SHORT).show();
                        }
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
    @SuppressLint("SetTextI18n")
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
        LinearLayout BoxRewardLayout = findViewById(R.id.BoxRewardLayout);
        RewardShowView.setText(EpicItemList.toString() + "\n" + RareItemList.toString() + "\n" + DecentItemList.toString() + "\n" + NormalItemList.toString());
        BoxRewardLayout.addView(RewardShowView);
    }

    //lv.1 method, sub method of OpenMarketItem() method. randomly return one item of entire list.
    //thanks to: https://stackoverflow.com/questions/12487592/randomly-select-an-item-from-a-list !
    private String ReturnGotItemName(List<String> NeedToGet){
        Random randomizer = new Random();
        return NeedToGet.get(randomizer.nextInt(NeedToGet.size()));
    }//end of Market Item IO system.


    //Item select function.
    /**
     * The Market ItemType Parameter.<br/>
     * "0" represents for "Box".<br/>
     * "1" represents for "Key".
     */
    int ItemType = 0;//not initialized state.
    
    public void ChangeItemType(View view){//change user selected Item Type.
        RadioGroup ItemTypeChoose = findViewById(R.id.ItemTypeChoose);
        RadioButton BoxTypeChoose = findViewById(R.id.BoxTypeChoose);
        RadioButton KeyTypeChoose = findViewById(R.id.KeyTypeChoose);
        int SelectedId = ItemTypeChoose.getCheckedRadioButtonId();
        if(SelectedId == BoxTypeChoose.getId()){
            ItemType = 0;
        }else if(SelectedId == KeyTypeChoose.getId()){
            ItemType = 1;
        }
    }

    private String getMarketItemName(){
        String MarketItemName;
        if(ItemType == 0){
            MarketItemName = getString(R.string.MarketBoxWordTran);
        }else{
            MarketItemName = getString(R.string.MarketKeyWordTran);
        }
        return MarketItemName;
    }//end of Item select function.


    //Detail Button Function.
    public void ShowOpeningDetail(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                ValueLib.OPEN_POSSIBILITY_HELP,
                getString(R.string.ConfirmWordTran));
    }//end of Detail Button Function.


    //resource system.
    int UserBoxNumber = 0;
    int UserKeyNumber = 0;
    ResourceIO resourceIO;

    private void InitializingResourceData(){//main import data method.
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        UserBoxNumber = SupportLib.getIntData(this,"BattleDataProfile","UserBoxNumber",0);
        UserKeyNumber = SupportLib.getIntData(this,"BattleDataProfile","UserKeyNumber",0);
        resourceIO = new ResourceIO(this);
        //make number into financial form.
        BoxCountingView.setText(SupportLib.ReturnKiloIntString(UserBoxNumber));
        PointCountingView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        KeyCountingView.setText(SupportLib.ReturnKiloIntString(UserKeyNumber));
    }

    private void GetBox(int addNumber){//Box IO method.
        UserBoxNumber = UserBoxNumber + addNumber;
        SupportLib.saveIntData(this,"BattleDataProfile","UserBoxNumber",UserBoxNumber);
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        BoxCountingView.setText(SupportLib.ReturnKiloIntString(UserBoxNumber));
    }

    private void CostBox(int CostNumber){//Box IO method.
        UserBoxNumber = UserBoxNumber - CostNumber;
        SupportLib.saveIntData(this,"BattleDataProfile","UserBoxNumber",UserBoxNumber);
        TextView BoxCountingView = findViewById(R.id.BoxCountingView);
        BoxCountingView.setText(SupportLib.ReturnKiloIntString(UserBoxNumber));
    }

    private void GetKey(int addNumber){//Key IO method.
        UserKeyNumber = UserKeyNumber + addNumber;
        SupportLib.saveIntData(this,"BattleDataProfile","UserKeyNumber",UserKeyNumber);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        KeyCountingView.setText(SupportLib.ReturnKiloIntString(UserKeyNumber));
    }

    private void CostKey(int CostNumber){
        UserKeyNumber = UserKeyNumber - CostNumber;
        SupportLib.saveIntData(this,"BattleDataProfile","UserKeyNumber",UserKeyNumber);
        TextView KeyCountingView = findViewById(R.id.KeyCountInMarket);
        KeyCountingView.setText(SupportLib.ReturnKiloIntString(UserKeyNumber));
    }//end of resource system.

    public void GoToBackpack(View view){
        Intent i = new Intent(MarketActivity.this, BackpackActivity.class);
        startActivity(i);
    }
}