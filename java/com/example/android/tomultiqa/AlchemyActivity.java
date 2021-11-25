package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class AlchemyActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alchemy);
        LoadEvent();
        InitializingElementText();
        InitializingMaterial();
        InitializingMasteryData();
        //confirm if user is already have Alchemy still Effective. if user have, then close the Create button.
        Button CreateAlchemyButton = findViewById(R.id.CreateAlchemyButton);
        if(SupportLib.getIntData(this,"AlchemyDataFile","AlchemyTurn",0) > 0){
            CreateAlchemyButton.setClickable(false);
            CreateAlchemyButton.setText(getString(R.string.YouHadOneAvailableTran));
            CreateAlchemyButton.setTextColor(Color.RED);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO: 2021/9/24 save Resource data here in future.
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
            return true;//just forget this for a while.
        }else if(item.getItemId() == R.id.action_ShopEvent){
            LoadEvent();
            if(!EventName.equals("")){
                SupportLib.CreateNoticeDialog(this,
                        getString(R.string.ShopEventTitleTran),
                        EventName + "\n\n" + EventScript,
                        getString(R.string.ConfirmWordTran));
            }else{
                SupportLib.CreateNoticeDialog(this,
                        getString(R.string.ShopEventTitleTran),
                        getString(R.string.NothingWordTran),
                        getString(R.string.ConfirmWordTran));
            }

        }
        return super.onOptionsItemSelected(item);
    }//end of ActionBar Menu.


    //Alchemy system.
    //Mastery part.
    int MasteryLevel = 0;
    int MasteryMaxLevel = 15;
    int MasteryEXP = 0;
    int MasteryUpgradeEXP = 300;

    //main method.
    private void GetMastery(int MasteryNumber){
        //0.preparation.
        boolean IsUpgradeAble = true;
        //1.add Mastery EXP.
        MasteryEXP = MasteryEXP + MasteryNumber;
        //2.check if user have enough EXP to upgrade.
        while(IsUpgradeAble){
            //2.1 try to cost EXP to upgrade, and load new level data.
            if(MasteryEXP >= MasteryUpgradeEXP && MasteryLevel < MasteryMaxLevel){
                MasteryLevel = MasteryLevel + 1;
                MasteryEXP = MasteryEXP - MasteryUpgradeEXP;
                MasteryDataSet();
            }
            //2.2 after loaded next level EXP requirement, check if user have enough EXP to upgrade again.if not, stop the circulation.
            if(MasteryEXP < MasteryUpgradeEXP){
                IsUpgradeAble = false;
            }
        }
        //3.after upgrade, save data.
        SaveMasteryData();
    }

    //main method.
    private void InitializingMasteryData(){
        MasteryLevel = SupportLib.getIntData(this,"AlchemyMasteryFile","MasteryLevel",0);
        MasteryEXP = SupportLib.getIntData(this,"AlchemyMasteryFile","MasteryEXP",0);
        //define the benefit which upgrade Mastery can get.
        MasteryDataSet();
    }

    //sub method of GetMastery() method.
    private void SaveMasteryData(){
        SupportLib.saveIntData(this,"AlchemyMasteryFile","MasteryLevel",MasteryLevel);
        SupportLib.saveIntData(this,"AlchemyMasteryFile","MasteryEXP",MasteryEXP);
    }

    //lv.2 sub method, full collection of Mastery Level.
    @SuppressLint("SetTextI18n")
    private void MasteryDataSet(){
        ProgressBar MasteryEXPView = findViewById(R.id.MasteryEXP);
        TextView MasteryLevelView = findViewById(R.id.MasteryLevel);
        TextView MasteryNumberView = findViewById(R.id.MasteryNumberView);

        SetMasteryLevel(1,300,2,0);
        SetMasteryLevel(2,500,4,0);
        SetMasteryLevel(3,900,7,0);
        SetMasteryLevel(4,1400,7,1);
        SetMasteryLevel(5,1900,10,1);
        SetMasteryLevel(6,2500,12,1);
        SetMasteryLevel(7,3200,14,1);
        SetMasteryLevel(8,3900,16,1);
        SetMasteryLevel(9,4700,18,1);
        SetMasteryLevel(10,5500,20,1);
        SetMasteryLevel(11,6400,22,1);
        SetMasteryLevel(12,7300,22,2);
        SetMasteryLevel(13,8200,24,2);
        SetMasteryLevel(14,9100,27,2);
        SetMasteryLevel(15,10000,30,2);

        MasteryLevelView.setText("Lv." + MasteryLevel);
        MasteryNumberView.setText(MasteryEXP + "/" + MasteryUpgradeEXP);
        MasteryEXPView.setProgress(SupportLib.CalculatePercent(MasteryEXP,MasteryUpgradeEXP));
    }

    //lv.1 sub method of InitializingMasteryData() method.
    private void SetMasteryLevel(int Level, int UpgradeEXP, int SuccessAdd, int RecipeMaxAdd){
        TextView SustainTurnView = findViewById(R.id.SustainTurnView);
        ClearRecipe(SustainTurnView);

        if(MasteryLevel == Level){
            SuccessExtra = SuccessAdd;
            RecipeExtra = RecipeMaxAdd;
            MaxRecipeElementNumber = 6 + RecipeExtra + EventEffect;
            MasteryUpgradeEXP = UpgradeEXP;
        }
    }//end of Mastery part.


    //Material part.
    ResourceIO resourceIO;

    @SuppressLint("SetTextI18n")
    //lv.1 method, main method of Alchemy function.
    private void InitializingMaterial(){
        TextView MaterialNumberView = findViewById(R.id.MaterialNumberView);
        resourceIO = new ResourceIO(this);
        MaterialNumberView.setText(resourceIO.UserMaterial + " " + getString(R.string.MaterialWordTran));
    }//end of Material part.


    //Element Showing part.
    //the Element ID which effect is being displayed currently.
    int ElementShowingID = 1;
    //the Element total type`s number.
    int ElementTotalID = 5;

    //lv.3 method, main method of Alchemy function.
    public void NextElement(View view){
        if(ElementShowingID < ElementTotalID){
            ElementShowingID = ElementShowingID + 1;
        }else{
            ElementShowingID = 1;
        }
        LoadElementText();
    }

    //lv.3 method, main method of Alchemy function.
    public void BeforeElement(View view){
        if(ElementShowingID > 1){
            ElementShowingID = ElementShowingID - 1;
        }else{
            ElementShowingID = ElementTotalID;
        }
        LoadElementText();
    }

    //lv.2 method, total set of Element Text.
    private void LoadElementText(){
        PrintElementEffect(1,getString(R.string.ATKElementNameTran),getString(R.string.ATKElementEffectTran));
        PrintElementEffect(2,getString(R.string.CRElementNameTran),getString(R.string.CDElementEffectTran));
        PrintElementEffect(3,getString(R.string.CDElementNameTran),getString(R.string.CRElementEffectTran));
        PrintElementEffect(4,getString(R.string.AddTurn1NameTran),getString(R.string.AddTurn1EffectTran));
        PrintElementEffect(5,getString(R.string.AddSuccessRateNameTran),getString(R.string.AddSuccessRateEffectTran));
        //PrintElementEffect(6,getString(R.string.AddTurn2NameTran),getString(R.string.AddTurn2EffectTran));
    }

    //lv.1 method, sub method of LoadElementEffect method.
    private void PrintElementEffect(int ElementID, String ElementName, String EffectText){
        TextView ElementNameView = findViewById(R.id.ElementNameView);
        TextView ElementEffectView = findViewById(R.id.ElementEffectView);
        if(ElementShowingID == ElementID){
            CurrentElementName = ElementName;
            CurrentElementEffect = EffectText;
            ElementNameView.setText(CurrentElementName);
            ElementEffectView.setText(CurrentElementEffect);
        }
    }//end of Element showing part.


    //Recipe calculation part.
    int RecipeCost = 0;
    int RecipeTurn = 6;
    int RecipeExtra = 0;

    int SuccessRate = 100;
    int SuccessExtra = 0;
    //the Element`s Name which be showed right now.
    String CurrentElementName = "";
    //the Element`s Effect description which be showed right now.
    String CurrentElementEffect = "";
    //the entire Recipe text, show the Recipe detail to user, including Element types and Number.
    String CurrentRecipeText = "";
    //the entire Recipe Effect text, show the Recipe total effect detail to user.
    String CurrentRecipeEffect = "";
    //the Current Element number which Recipe included right now.
    int RecipeElementNumber = 0;
    //the max Element number which Recipe can received.
    int MaxRecipeElementNumber = 6;
    //Each type of Element Number Counting.
    int ATKElementNumber = 0;
    int CRElementNumber = 0;
    int CDElementNumber = 0;
    int AddTurnElementNumber = 0;
    int AddSuccessRateElementNumber = 0;

    //lv.2 method. main method of Alchemy function.
    @SuppressLint("SetTextI18n")
    public void ClearRecipe(View view){
        //0.preparation.
        TextView MaterialCostView = findViewById(R.id.MaterialCostView);
        TextView SustainTurnView = findViewById(R.id.SustainTurnView);
        TextView SuccessRateView = findViewById(R.id.SuccessRateView);
        EditText RecipeDetailView = findViewById(R.id.RecipeDetailView);
        EditText RecipeEffectView = findViewById(R.id.RecipeEffectView);
        //1.reset the Element record variables.
        RecipeElementNumber = 0;
        ATKElementNumber = 0;
        CRElementNumber = 0;
        CDElementNumber = 0;
        AddTurnElementNumber = 0;
        AddSuccessRateElementNumber = 0;
        SuccessRate = 100;
        //2.show the result to user.
        RecipeDetailView.setText("--");
        RecipeEffectView.setText("--");
        MaterialCostView.setText(0 + "");
        SustainTurnView.setText((MaxRecipeElementNumber + RecipeExtra) + "");
        SuccessRateView.setText((SuccessRate + SuccessExtra) + "%");
    }

    //lv.2 method. main method of Alchemy function.
    @SuppressLint("SetTextI18n")
    public void AddElement(View view){
        //1.judge which Element number should be Added.
        if(RecipeElementNumber < MaxRecipeElementNumber){
            if(CurrentElementName.equals(getString(R.string.ATKElementNameTran))){
                ATKElementNumber = ATKElementNumber + 1;
            }else if(CurrentElementName.equals(getString(R.string.CRElementNameTran))){
                CRElementNumber = CRElementNumber + 1;
            }else if(CurrentElementName.equals(getString(R.string.CDElementNameTran))){
                CDElementNumber = CDElementNumber + 1;
            }else if(CurrentElementName.equals(getString(R.string.AddTurn1NameTran))){
                AddTurnElementNumber = AddTurnElementNumber + 1;
            }else if(CurrentElementName.equals(getString(R.string.AddSuccessRateNameTran))){
                AddSuccessRateElementNumber = AddSuccessRateElementNumber + 1;
            }
            //2.add the specific Element`s number.
            TextView RecipeNumberView = findViewById(R.id.RecipeNumberView);
            RecipeElementNumber = RecipeElementNumber + 1;
            RecipeNumberView.setText(RecipeElementNumber + " / " + MaxRecipeElementNumber);
            //3.change the Recipe information.
            ChangeRecipeText();
            ChangeRecipeReport();
        }else{
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You have touched the Recipe Element Limitation of Current Mastery Level.",
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    //lv.2 method, main method of Alchemy function.
    public void StartAlchemy(View view){
        //1.save data.
        if(SupportLib.CreateRandomNumber(1,100) <= SuccessRate && RecipeTurn > 0 && AddSuccessRateElementNumber + AddTurnElementNumber < MaxRecipeElementNumber){
            //Alchemy effect recording.
            if(ATKElementNumber > 0){
                SupportLib.saveIntData(this,"AlchemyDataFile","ATKup",ATKElementNumber * 10);
            }
            if(CRElementNumber > 0){
                SupportLib.saveIntData(this,"AlchemyDataFile","CRup",CRElementNumber * 5);
            }
            if(CDElementNumber > 0){
                SupportLib.saveIntData(this,"AlchemyDataFile","CDup",CDElementNumber * 15);
            }
            if(AddTurnElementNumber > 0){
                SupportLib.saveIntData(this,"AlchemyDataFile","AlchemyTurn",AddTurnElementNumber * 5);
            }

            int MasteryWillGet = RecipeCost * RecipeElementNumber * SupportLib.CreateRandomNumber(0,10);
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "Alchemy Create Success.\n" +
                            "You get " + MasteryWillGet + " Mastery EXP!",
                    getString(R.string.ConfirmWordTran)
            );
            GetMastery(MasteryWillGet);

        }else if(RecipeTurn <= 0){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "You can`t Create Alchemy result which Sustain Turn is lower than 1!",
                    getString(R.string.ConfirmWordTran)
            );
        }else if(AddSuccessRateElementNumber + AddTurnElementNumber >= MaxRecipeElementNumber){
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    "Why do you want to Create a Alchemy result which contains no Effective Elements? Please change your Recipe.",
                    getString(R.string.ConfirmWordTran)
            );
        }else{
            SupportLib.CreateNoticeDialog(this,
                    "Oh...",
                    "We made it Exploded...Create fail.",
                    getString(R.string.ConfirmWordTran)
            );
        }
//        //2.go to MainActivity.
//        Intent i = new Intent(AlchemyActivity.this, MainActivity.class);
//        startActivity(i);
//        finish();
    }

    //lv.1 method, sub method of onCreate method.
    private void InitializingElementText(){
        Thread thread = new Thread(() -> {
            CurrentElementName = getString(R.string.ATKElementNameTran);
            CurrentElementEffect = getString(R.string.ATKElementEffectTran);
        });
        thread.start();
    }

    //lv.1 method, sub method of AddElement method.
    private void ChangeRecipeText() {
        TextView RecipeDetailView = findViewById(R.id.RecipeDetailView);
        TextView RecipeEffectView = findViewById(R.id.RecipeEffectView);
        //1.Clear Recipe Text Record to edit easily.
//        CurrentRecipeText = "";
//        CurrentRecipeEffect = "";
        //2.calculate the Element Effect value.
        int ATKEffect = 10 * ATKElementNumber;
        int CREffect = 5 * CRElementNumber;
        int CDEffect = 15 * CDElementNumber;
        int AddTurnEffect = 5 * AddTurnElementNumber;
        int AddSuccessEffect = 5 * AddSuccessRateElementNumber;
        //3. add text according to Element Number and Effect value.
        CurrentRecipeText =
                getString(R.string.ATKElementNameTran) + "*" + ATKElementNumber + " / " +
                        getString(R.string.CRElementNameTran) + "*" + CRElementNumber + " / " +
                        getString(R.string.CDElementNameTran) + "*" + CDElementNumber + " / " +
                        getString(R.string.AddTurn1NameTran) + " *" + AddTurnElementNumber + " / " +
                        getString(R.string.AddSuccessRateNameTran) + "*" + AddSuccessRateElementNumber + ".";
        CurrentRecipeEffect =
                getString(R.string.ATKWordTran) + "+" + ATKEffect + "% / " +
                        getString(R.string.CritialRateWordTran) + "+" + CREffect + "% / " +
                        getString(R.string.CritialDamageWordTran) + "+" + CDEffect + "% / " +
                        getString(R.string.SustainTurnWordTran) + "+" + AddTurnEffect + " / " +
                        getString(R.string.SuccessRateWordTran) + "+" + AddSuccessEffect + "%.";
        //4. show the Recipe Effect result to user.
        RecipeDetailView.setText(CurrentRecipeText);
        RecipeEffectView.setText(CurrentRecipeEffect);
    }

    //lv.1 method, change the Recipe cost, Success Rate and Sustain Turn according to Element number changed
    @SuppressLint("SetTextI18n")
    private void ChangeRecipeReport () {
        TextView MaterialCostView = findViewById(R.id.MaterialCostView);
        TextView SustainTurnView = findViewById(R.id.SustainTurnView);
        TextView SuccessRateView = findViewById(R.id.SuccessRateView);
        if (RecipeElementNumber > 0) {
            RecipeCost = RecipeElementNumber * 3;
            RecipeTurn = 6 - RecipeElementNumber + 5 * AddTurnElementNumber;
            SuccessRate = 100 - RecipeElementNumber * 3 + 5 * AddSuccessRateElementNumber + SuccessExtra;
            MaterialCostView.setText(RecipeCost + "");
            SustainTurnView.setText(RecipeTurn + "");
            SuccessRateView.setText(SuccessRate + "%");
        }
    }//end of Alchemy system.


    //Event system.
    //Event system.
    String EventName = "";
    String EventScript = "";
    int EventEffect = 0;

    private void LoadEvent(){
        Thread thread = new Thread(() -> {
            if(getIntent().getStringExtra("EventName").equals(getString(R.string.AlchemyBottleEventTran))){
                EventName = getString(R.string.AlchemyBottleEventTran);
                EventScript = getIntent().getStringExtra("EventScript");
                EventEffect = 2;//Extra 2 number of Element Limit.
            }
        });
        thread.start();
    }//end of Event system.
}