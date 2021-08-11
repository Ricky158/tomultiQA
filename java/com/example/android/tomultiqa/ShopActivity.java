package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //1.initializing recyclerview.
        //1.1 prepare content which needed to be showed to user.
        //1.2 add content to ArrayList, data from ItemDataBase.
        getEXPInformation();
        ReloadResourceData();
        ReLoadAllListData();
    }

    //add a button menu to ActionBar in this Activity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shop_activity_button, menu);
        return true;
    }

    //thanks to: https://blog.csdn.net/z8711042/article/details/28903275 !
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ShopHelp) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    getString(R.string.ShopHelpTextTran),
                    getString(R.string.ConfirmWordTran)
            );
        }else if(item.getItemId() == R.id.action_ShopEvent){
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.ShopEventTitleTran),
                    "To be continued......",
                    getString(R.string.ConfirmWordTran));
        }else if(item.getItemId() == android.R.id.home){
            Intent i = new Intent(this,SquareActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;//keep it here!
        }
        return super.onOptionsItemSelected(item);
    }//end of ActionBar Menu.


    //Shop system.
    //RecyclerView variables.
    ArrayList<String> GoodNameList = new ArrayList<>();
    ArrayList<Integer> GoodPriceList = new ArrayList<>();
    //User Selected variables.
    ArrayList<String> SelectedGoodList = new ArrayList<>();
    ArrayList<Integer> SelectedPriceList = new ArrayList<>();
    //Limit Goods.
//    boolean IsLimit1Gettable = true;
    //Current Selected variables.
    String ItemSelectedName = "";
    int ItemSelectedId = -1;


    //lv.3 method, main method of final phase: buying and good's effect.
    //thanks to: https://www.runoob.com/java/java-loop.html !
    public void BuyAndAffect(View view){
        //1.get total price and selected goods detail.
        int TotalPrice = getCartTotalPrice();
        String AllSelectedGoods = getCartDetail();
        //2.show a dialog to user to start settlement.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.NoticeWordTran));
        dialog.setMessage(getString(R.string.ShopTakeGoodsTran) + "\n" + AllSelectedGoods + "\n" + getString(R.string.TotalPriceHintTran) + "\n" + TotalPrice + getString(R.string.PointWordTran));
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog13, id) -> {
                    //1.NPE and enough point check.
                    if(!SelectedPriceList.isEmpty() && !SelectedGoodList.isEmpty() && UserPoint >= TotalPrice){
                        //2.for any element in Shopping cart, check it's name, cost points, and give it to user.
                        for(String Good: SelectedGoodList){
                            //2.1 cost point according the price of specific good.
                            CostPoint(getGoodPrice(Good));
                            //2.2 settlement process(结算).give effect to user.
                            //2.2.1 Normal Goods, each branch represents a kind of Good.
                            if(Good.equals(GoodNameList.get(0)) && UserHaveEXP <= 1000000){
                                GetEXP(50);
                            }else if(Good.equals(GoodNameList.get(1)) && UserHaveEXP <= 1000000){
                                GetEXP(2500);
                            }
                        }
                        dialog13.cancel();
                    }else if(UserPoint < TotalPrice){
                        dialog13.cancel();//close buying dialog first and open error dialog later to prevent two dialog on same surface.
                        SupportClass.CreateNoticeDialog(this,
                                getString(R.string.ErrorWordTran),
                                getString(R.string.NotEnoughPointTran),
                                getString(R.string.ConfirmWordTran));
                    }
                });
        dialog.setNeutralButton(getString(R.string.ClearCartTran), (dialog12, which) -> {
            ResetWholeCart(null);
            dialog12.cancel();
            Toast.makeText(ShopActivity.this,getString(R.string.CompletedWordTran),Toast.LENGTH_SHORT).show();
        });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.2 method, main method of check details of shopping cart.
    public void CheckCart(View view){
        String Content = getCartDetail();
        //2.show to user.
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.ShoppingCartTran),
                Content,
                getString(R.string.ConfirmWordTran));
    }

    //lv.2 method, main method of add a item to shopping cart.
    //thanks to: https://www.jianshu.com/p/7f2941dbfb17 !
    @SuppressLint("SetTextI18n")
    public void AddGoodToCart(View view){
        //0.preparation.
        LinearLayout CartLayout = new LinearLayout(this);
        TextView NumberCounter = new TextView(this);
        SeekBar NumberSelector = new SeekBar(this);
        CartLayout.setOrientation(LinearLayout.VERTICAL);
        CartLayout.addView(NumberCounter);
        CartLayout.addView(NumberSelector);
        final int[] AddTimes = {0};
        NumberSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AddTimes[0] = NumberSelector.getProgress();
                NumberCounter.setText(AddTimes[0] + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                AddTimes[0] = NumberSelector.getProgress();
                NumberCounter.setText(AddTimes[0] + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                AddTimes[0] = NumberSelector.getProgress();
                NumberCounter.setText(AddTimes[0] + "");
            }
        });
        //1.set SeekBar Max value.
        int GoodMaxNumber;
        if(!ItemSelectedName.equals("") && ItemSelectedId != -1){
            GoodMaxNumber = Math.min(UserPoint / (getGoodPrice(ItemSelectedName) + getCartTotalPrice()),100);
        }else {
            GoodMaxNumber = 0;
        }
        int InitialNumber = Math.min(GoodMaxNumber,1);
        NumberCounter.setText(InitialNumber + "");
        NumberCounter.setPadding(40,4,0,4);
        NumberCounter.setTextSize(16);//16sp
        NumberSelector.setProgress(InitialNumber);
        NumberSelector.setMax(GoodMaxNumber);
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Take Number");
        //2.set basic values of dialog, including content text,button text,and title.
        dialog.setView(CartLayout);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    //1.if selected information is not null, then add selected item information to SelectedList.
                    if(GoodMaxNumber > 0)
                    {
                        while (AddTimes[0] > 0)
                        {
                            SelectedGoodList.add(ItemSelectedName);
                            SelectedPriceList.add(GoodPriceList.get(ItemSelectedId));
                            AddTimes[0] = AddTimes[0] - 1;
                        }
                    }
                    //2.calculate total cost of selected goods and refresh UI.
                    int TotalPrice = getCartTotalPrice();
                    TextView TotalPriceShowView = findViewById(R.id.TotalPriceShowView);
                    TotalPriceShowView.setText(TotalPrice + "");
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.2 method, sub method of BuyAndAffect() method, also main method of Remove all items in shopping cart.
    @SuppressLint("SetTextI18n")
    public void ResetWholeCart(View view){
        SelectedGoodList = new ArrayList<>();
        SelectedPriceList = new ArrayList<>();
        //2.calculate total cost of selected goods and refresh UI.
        int TotalPrice = getCartTotalPrice();
        TextView TotalPriceShowView = findViewById(R.id.TotalPriceShowView);
        TotalPriceShowView.setText(TotalPrice + "");
    }//end of Shop system.(not included support code.)


    //Initializing RecyclerView.
    //lv.2 method, main method of Reload RecyclerView UI and define which good can be purchased.
    private void ReLoadAllListData(){
        DefineGoodInf("50EXP",8500);
        DefineGoodInf("2500EXP",380000);
//        if(IsLimit1Gettable){
//            DefineGoodInf(getString(R.string.MMAchievementGoodTran),1000000);
//        }todo
        //1.3 prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //1.3.1 Put list data in Adapter.
        LetterAdapter mLetterAdapter = new LetterAdapter(GoodNameList);
        //1.4 prepare RecyclerView object.
        RecyclerView BackpackView = findViewById(R.id.GoodRackView);
        //1.5 Put Adapter to RecyclerView.
        BackpackView.setAdapter(mLetterAdapter);
        //1.6 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        BackpackView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //1.7 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
    }

    //lv.1 method, sub method of LoadAllListData() method.
    private void DefineGoodInf(String Name, int Price){
        GoodNameList.add(Name);
        GoodPriceList.add(Price);
    }

//    //lv.1 method, sub method of BuyAndAffect() method, Remove specific good in the Good(Name/Price)List by good's name.
//    private void RemoveGoodInf(String Name){todo
//        GoodNameList.remove(Name);
//        GoodPriceList.remove(SelectedGoodList.indexOf(Name));
//    }//end of Initializing RecyclerView.


    //resource system.
    int UserPoint;
    int UserMaterial;

    @SuppressLint("SetTextI18n")
    private void ReloadResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
        MaterialCountingView.setText(SupportClass.ReturnKiloIntString(UserMaterial));
    }

    //lv.1 method, sub method of BuyAndAffect() method.Basic operation of resource, just do it automatically.
    private void CostPoint(int Cost){
        UserPoint = UserPoint - Cost;
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
    }//end of resource system.


    //EXP system, belong and refers to MainActivity.
    int UserLevel;
    int LevelLimit = 50;
    int UserHaveEXP;
    private void GetEXP(int AddNumber){
        UserHaveEXP = UserHaveEXP + AddNumber;
        SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",UserHaveEXP);
    }

    private void getEXPInformation(){
        SharedPreferences EXPInfo = getSharedPreferences("EXPInformationStoreProfile", MODE_PRIVATE);
        LevelLimit = SupportClass.getIntData(this,"ExcessDataFile","LevelLimit",50);
        UserLevel = EXPInfo.getInt("UserLevel",1);
        UserHaveEXP = EXPInfo.getInt("UserHaveEXP", 0);
    }//end of EXP system.


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
        @SuppressLint("RecyclerView")
        public void onBindViewHolder(@NonNull VH holder, int position) {//bind (combine) data with [each] item in RecyclerView.
            final String NameString = dataList.get(position);//load data needed to be shown from dataList.
            holder.ItemNameView.setText(NameString);//set TextView`s text in the item.
            //set OnClickListener to each TextView in the item.
            holder.ItemNameView.setOnClickListener(v -> {//do something after Clicked.
                ItemSelectedName = NameString;//return selected item name to String field.
                TextView SelectedGoodNameView = findViewById(R.id.SelectedGoodNameView);
                SelectedGoodNameView.setText(NameString);
                ItemSelectedId = position;//return selected item Id to int field.
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


    //Shop support code.
    //lv.1 method, sub method of BuyAndAffect() and AddGoodToCart() method, provide total price of selected goods.
    private int getCartTotalPrice(){
        //1.calculate total cost of selected goods and refresh UI.
        int Total = 0;
        if(!SelectedPriceList.isEmpty()){
            for(int EachPrice: SelectedPriceList){
                Total = Total + EachPrice;
            }
        }
        return Total;
    }

    //lv.1 method, sub method of BuyAndAffect() method, return good's price according to its Name.
    private int getGoodPrice(String Name){
        return GoodPriceList.get(GoodNameList.indexOf(Name));
    }

    //lv.1 method, sub method of CheckCart() and BuyAndAffect() method. return the detail of shopping cart.
    //thanks to: https://www.cnblogs.com/DreamingFishZIHao/p/12982988.html !
    private String getCartDetail(){
        StringBuilder CartContent = new StringBuilder();
        //1.read entire Selected List.
        if(!SelectedGoodList.isEmpty()){
            for(String Good: SelectedGoodList){
                CartContent.append(Good).append("\n");//equals "String CartContent = CartContent + Good + "\n".
            }
        }
        return CartContent.toString();
    }//end of Shop support code.
    //end of shop system.
}