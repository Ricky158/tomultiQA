package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(38,198,218));
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        getEXPInformation();
        InitializingResourceData();
        CheckLimitedGettable();

        //provide visualize support for listener report.
        //Thanks to:https://stackoverflow.com/questions/41774963/android-seekbar-show-progress-value-along-the-seekbar
        final Button LessGoodButton = findViewById(R.id.LessGoodButton);
        final Button MoreGoodButton = findViewById(R.id.MoreGoodButton);
        final EditText GoodNumberShowView = findViewById(R.id.GoodNumberShowView);
        LessGoodButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(GoodNumber > 1){
                    GoodNumber = GoodNumber - 1;
                    GoodNumberShowView.setText(GoodNumber + "");
                    CalculateGoodPrice();
                }
        }
        });
        MoreGoodButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                GoodNumber = GoodNumber + 1;
                GoodNumberShowView.setText(GoodNumber + "");
                CalculateGoodPrice();
            }
        });
        //thanks to:
        // https://stackoverflow.com/questions/7391891/how-to-check-if-an-edittext-was-changed-or-not !
        // https://stackoverflow.com/questions/5585779/how-do-i-convert-a-string-to-an-int-in-java !
        GoodNumberShowView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(GoodNumberShowView.getText().length() != 0){
                    GoodNumber = Integer.parseInt(GoodNumberShowView.getText().toString());
                    CalculateGoodPrice();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(GoodNumberShowView.getText().length() != 0){
                    GoodNumber = Integer.parseInt(GoodNumberShowView.getText().toString());
                    CalculateGoodPrice();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(GoodNumberShowView.getText().length() != 0){
                    GoodNumber = Integer.parseInt(GoodNumberShowView.getText().toString());
                    CalculateGoodPrice();
                }
            }
        });

        //these declaration are designed for detect the change of Good choice CheckBoxes. And change the total single price number according the status of these CheckBoxes.
        final CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        final CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        final CheckBox TakeLimited1View = findViewById(R.id.TakeLimited1View);

        TakeGood1View.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View v) {
                CloseLimitGoodChoose();
                if(TakeGood1View.isChecked()){
                    AllGoodPrice = AllGoodPrice + Good1Price;
                }else if(!TakeGood1View.isChecked()){
                    AllGoodPrice = AllGoodPrice - Good1Price;
                }
                CalculateGoodPrice();
            }
        });
        TakeGood2View.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseLimitGoodChoose();
                if(TakeGood2View.isChecked()){
                    AllGoodPrice = AllGoodPrice + Good2Price;
                }else if (!TakeGood2View.isChecked()){
                    AllGoodPrice = AllGoodPrice- Good2Price;
                }
                CalculateGoodPrice();
            }
        });
        TakeLimited1View.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                //because Limited good is limited to buy 1 forever, so it can`t calculate with normal Good.
                CloseNormalGoodChoose();
                if(TakeLimited1View.isChecked()){
                    CloseAllOption();
                    AllGoodPrice = AllGoodPrice + Limited1Price;
                }else if (!TakeLimited1View.isChecked()){
                    OpenAllOption();
                    AllGoodPrice = AllGoodPrice- Limited1Price;
                }
                CalculateGoodPrice();
            }
        });
        //end of belongs to Shop system.
    }


    //basic value of whole activity.
    int UserLevel;
    int UserPoint;
    int UserMaterial;
    //Good Price definition.
    private static final int Good1Price = 2500;
    private static final int Good2Price = 225000;
    private static final int Limited1Price = 1000000;

    @SuppressLint("SetTextI18n")
    private void InitializingResourceData(){
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        TextView MaterialCountingView = findViewById(R.id.KeyCountInMarket);
        //!!!
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
        UserMaterial = SupportClass.getIntData(this,"BattleDataProfile","UserMaterial",0);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
        MaterialCountingView.setText(SupportClass.ReturnKiloIntString(UserMaterial));
    }//end of basic value.


    //limited good system.
    private void CloseNormalGoodChoose(){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);

        if(TakeGood1View.isChecked()){
            AllGoodPrice = AllGoodPrice - Good1Price;
            TakeGood1View.setChecked(false);
        }
        if(TakeGood2View.isChecked()){
            AllGoodPrice = AllGoodPrice- Good2Price;
            TakeGood2View.setChecked(false);
        }
        CloseAllOption();
        CalculateGoodPrice();
    }

    private void CloseLimitGoodChoose(){
        CheckBox TakeLimited1View = findViewById(R.id.TakeLimited1View);
        if(TakeLimited1View.isChecked()){
            AllGoodPrice = AllGoodPrice - Limited1Price;
            TakeLimited1View.setEnabled(false);
            TakeLimited1View.setChecked(false);
        }
        CalculateGoodPrice();
    }

    @SuppressLint("SetTextI18n")
    private void CloseAllOption(){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        EditText GoodNumberShowView = findViewById(R.id.GoodNumberShowView);
        Button MoreGoodButton = findViewById(R.id.MoreGoodButton);
        Button LessGoodButton = findViewById(R.id.LessGoodButton);

        if(GoodNumber != 1){
            GoodNumber = 1;
        }
        if(GoodNumberShowView.isEnabled()){
            GoodNumberShowView.setEnabled(false);
            GoodNumberShowView.setText(GoodNumber + "");
        }
        if(TakeGood1View.isEnabled()){
            TakeGood1View.setEnabled(false);
        }
        if(TakeGood2View.isEnabled()){
            TakeGood2View.setEnabled(false);
        }
        if(MoreGoodButton.isEnabled()){
            MoreGoodButton.setEnabled(false);
        }
        if(LessGoodButton.isEnabled()){
            LessGoodButton.setEnabled(false);
        }
    }

    private void OpenAllOption(){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        EditText GoodNumberShowView = findViewById(R.id.GoodNumberShowView);
        Button MoreGoodButton = findViewById(R.id.MoreGoodButton);
        Button LessGoodButton = findViewById(R.id.LessGoodButton);

        if(!TakeGood1View.isEnabled() && UserLevel < LevelLimit){
            TakeGood1View.setEnabled(true);
        }
        if(!TakeGood2View.isEnabled() && UserLevel < LevelLimit){
            TakeGood2View.setEnabled(true);
        }
        if(!GoodNumberShowView.isEnabled()){
            GoodNumberShowView.setEnabled(true);
        }
        if(!MoreGoodButton.isEnabled()){
            MoreGoodButton.setEnabled(true);
        }
        if(!LessGoodButton.isEnabled()){
            LessGoodButton.setEnabled(true);
        }
    }

    private void CloseAllChecked(){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        CheckBox TakeLimited1View = findViewById(R.id.TakeLimited1View);
        TakeGood1View.setChecked(false);
        TakeGood2View.setChecked(false);
        TakeLimited1View.setChecked(false);
    }

    private void CheckLimitedGettable(){
        TextView Limited1NameView = findViewById(R.id.Limited1NameView);
        CheckBox TakeLimited1View = findViewById(R.id.TakeLimited1View);
        if(SupportClass.getBooleanData(this,"LimitedGoodsFile", "MillionMasterGot", false)){
            Limited1NameView.setTextColor(Color.RED);
            TakeLimited1View.setChecked(true);
            TakeLimited1View.setEnabled(false);
        }
    }//end of limited good system.


    //EXP system, belong and refers to MainActivity.
    int LevelLimit = 50;
    int UserHaveEXP;
    private void GetEXP(int AddNumber){
        UserHaveEXP = UserHaveEXP + AddNumber;
        SupportClass.saveIntData(this,"EXPInformationStoreProfile","UserHaveEXP",UserHaveEXP);
    }

    private void getEXPInformation(){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        SharedPreferences EXPInfo = getSharedPreferences("EXPInformationStoreProfile", MODE_PRIVATE);
        UserLevel = EXPInfo.getInt("UserLevel",1);
        UserHaveEXP = EXPInfo.getInt("UserHaveEXP", 0);
        if(UserLevel >= LevelLimit){
            TakeGood1View.setEnabled(false);
            TakeGood2View.setEnabled(false);
        }
    }//end of EXP system.


    //shop system.
    int AllGoodPrice = 0;
    int GoodNumber = 1;

    //sub method.
    @SuppressLint("SetTextI18n")
    private void CostPoints(int CostNumber){
        UserPoint = UserPoint - CostNumber;
        SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
        TextView PointCountingView = findViewById(R.id.PointCountInMarket);
        PointCountingView.setText(SupportClass.ReturnKiloIntString(UserPoint));
    }

    @SuppressLint("SetTextI18n")
    private void CalculateGoodPrice(){
        TextView TotalPriceShowView = findViewById(R.id.TotalPriceShowView);
        TotalPriceShowView.setText(SupportClass.ReturnKiloIntString(GoodNumber * AllGoodPrice));
    }

    @SuppressLint("SetTextI18n")
    public void BuyGoods(View view){
        CheckBox TakeGood1View = findViewById(R.id.TakeGood1View);
        CheckBox TakeGood2View = findViewById(R.id.TakeGood2View);
        CheckBox TakeLimited1View = findViewById(R.id.TakeLimited1View);
        final EditText GoodNumberShowView = findViewById(R.id.GoodNumberShowView);
        int TotalCostOfPoints;
        //AllGoodPrice is price per Good, please take notice!
        //0.preparation.
        if(UserLevel >= 300){
            TakeGood1View.setEnabled(false);
            TakeGood1View.setChecked(false);
            TakeGood2View.setEnabled(false);
            TakeGood2View.setChecked(false);
        }
        //1.calculating total price of good.
        TotalCostOfPoints = (GoodNumber * AllGoodPrice);
        //TotalCostOfPoints > 0 is prevent from int data over limit to be number that below 0.
        if(AllGoodPrice > 0 && GoodNumber > 0 && UserPoint >= TotalCostOfPoints && TotalCostOfPoints <= 200000000 && TotalCostOfPoints > 0){
            //report point situation to user.
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                     getString(R.string.ShopBuyCompletedTran) + "\n" +
                            getString(R.string.PointWordTran) + "\n" +
                            SupportClass.ReturnKiloIntString(UserPoint) + " → " + SupportClass.ReturnKiloIntString(UserPoint - TotalCostOfPoints) + "\n" +
                            getString(R.string.CostWordTran) + "\n" +
                            SupportClass.ReturnKiloIntString(TotalCostOfPoints),
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true
            );
            //1.1 after choose Good.Cost Point to buy it.
            CostPoints(TotalCostOfPoints);
            //because user can buy multi Goods independently, so we need to make their effect independently, too.(it means instead of "if-else if", we need to use multi "if")
            //1.2 get goods after payment.
            if(TakeGood1View.isChecked()){
                GetEXP(GoodNumber * 50 );
            }
            if(TakeGood2View.isChecked()){
                GetEXP(GoodNumber * 5000 );
            }
            if(TakeLimited1View.isChecked()){
                TakeLimited1View.setChecked(false);
                TakeLimited1View.setEnabled(false);
                SupportClass.saveBooleanData(this,"LimitedGoodsFile","MillionMasterGot",true);
            }
            //3.reset Shop system.
            AllGoodPrice = 0;
            GoodNumber = 1;
            GoodNumberShowView.setText(GoodNumber + "");
            OpenAllOption();
            CloseAllChecked();
        }else if(UserPoint < TotalCostOfPoints){//reset Good number function.
            final int MaxNumberCanBuy = UserPoint / AllGoodPrice;
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle(getString(R.string.NoticeWordTran));
            dialog.setMessage(getString(R.string.ShopNotEnoughPointTran) + MaxNumberCanBuy);
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    //set left button`s text.
                    getString(R.string.ConfirmWordTran),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            if(MaxNumberCanBuy > 0){
                dialog.setNegativeButton(
                        //set right button`s text.
                        getString(R.string.ResetGoodNumberTran),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //set the things that pressed right button to do.
                                GoodNumber = MaxNumberCanBuy;
                                GoodNumberShowView.setText(GoodNumber + "");
                                CalculateGoodPrice();
                                //after doing things, close whole dialog.
                                dialog.cancel();
                            }
                        });
            }
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }else if(TotalCostOfPoints > 200000000 || TotalCostOfPoints < 0){
            GoodNumber = 1;
            GoodNumberShowView.setText(GoodNumber + "");
            CalculateGoodPrice();
            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.ShopMaxCostErrorTran),
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
        }
    }

    public void ShowShopHelpDialog(View view){
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.HelpWordTran),
                getString(R.string.ShopHelpTextTran),
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
    }//end of shop system.
}