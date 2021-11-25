package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CraftActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //1.initializing recyclerview.
        //1.1 prepare content which needed to be showed to user.
        //1.2 add content to ArrayList, data from ItemDataBase.
        itemIO = new ItemIO(this);
        InitializingBackpack();
        InitializingRecipePicker();
        ReloadListUI();
    }


    //Craft system.
    String SelectedRecipe;

    //Craft Recipe part.
    //lv.1 method, main method. Initializing Recipe Picker List.
    //thanks to: https://blog.csdn.net/q4878802/article/details/50775002 !
    // https://stackoverflow.com/questions/2652414/how-do-you-get-the-selected-value-of-a-spinner !
    private void InitializingRecipePicker(){
        //0.preparation.
        TextView CraftTargetView = findViewById(R.id.CraftTargetView);
        Spinner RecipePicker = findViewById(R.id.RecipePicker);
        String[] RecipeList = {getString(R.string.DreamFruitTran)};//Preset data.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RecipeList);
        //1.initializing SelectedRecipe variable.
        SelectedRecipe = getString(R.string.DreamFruitTran);
        //2.actions.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RecipePicker.setAdapter(adapter);
        //2.1 set OnClick method.
        RecipePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //report chosen recipe to user from picker return.
                SelectedRecipe = RecipePicker.getItemAtPosition(RecipePicker.getSelectedItemPosition()).toString();
                CraftTargetView.setText(SelectedRecipe);
                ShowRecipeDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SelectedRecipe = getString(R.string.NothingSelectedTran);
                CraftTargetView.setText(SelectedRecipe);
                ShowRecipeDetail();
            }
        });
    }

    //lv.1 method, main method.Find detail in record, and Show it to user via dialog.
    String RecipeDetailText = "--";

    private void ShowRecipeDetail(){
        //1.load Craft Recipe showed to user.
        // TODO: define Recipe Detail which needed to show user here.
        //official sample:
        if(SelectedRecipe.equals(getString(R.string.DreamFruitTran))){
            RecipeDetailText = "1 * " + getString(R.string.MagicPieceTran) + "\n2 * " + getString(R.string.ElementFlowBottleTran);
        }
        //2.show detail to user.
        if(!SelectedRecipe.equals("--")){
            TextView CraftCostShowView = findViewById(R.id.CraftCostShowView);
            CraftCostShowView.setText(RecipeDetailText);
        }else{
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    "Not Expected Recipe Input.",
                    getString(R.string.ConfirmWordTran));
        }
    }//end of Craft Recipe part.

    
    //Craft part.
    //lv.3 method, main method of Craft part.
    public void StartCrafting(View view){
        //0.preparation.
        ArrayList<String> ItemCostList = new ArrayList<>();
        ArrayList<Integer> CostNumberList = new ArrayList<>();
        //1.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.DoYouWantToCraftTran) + SelectedRecipe + "] * " + CraftNumber + "?");
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//2.do work.
                    //2.1 decide recipe and cost.
                    // TODO: define Craft Recipe and Cost here.
                    /*
                     * Warning: do not set the same cost between two or more item.
                     * because of Craft number check method use List.indexOf() to search list.
                     * if you did that, this method can't correctly check all item, which have same number.
                     * so, I am not suggest to do that. Please notice.
                     */
                    //official sample:
                    if(SelectedRecipe.equals(getString(R.string.DreamFruitTran))){
                        //each item's information should have same id in two list.
                        ItemCostList.add(getString(R.string.MagicPieceTran));
                        CostNumberList.add(CraftNumber);
                        //you can split a line to differ each item which included in recipe.
                        ItemCostList.add(getString(R.string.ElementFlowBottleTran));
                        CostNumberList.add(CraftNumber * 2);
                    }
                    //2.2 if recipe and cost not empty, execute Cost and Craft Process.
                    if(!ItemCostList.isEmpty() && !CostNumberList.isEmpty()){
                        boolean[] IsCostCompleted = itemIO.CostItem(ItemCostList,CostNumberList);
                        //3.add Crafted Item to database.
                        if(!IsCostCompleted[0]){
                            SupportLib.CreateNoticeDialog(this,
                                    getString(R.string.ErrorWordTran),
                                    getString(R.string.NotAllUnlockedToCraftTran),
                                    getString(R.string.ConfirmWordTran));
                        }else if(!IsCostCompleted[1]){
                            SupportLib.CreateNoticeDialog(this,
                                    getString(R.string.ErrorWordTran),
                                    getString(R.string.NotEnoughItemTran),
                                    getString(R.string.ConfirmWordTran));
                        }else{
                            itemIO.EditOneItem(SelectedRecipe,getString(R.string.ResourceWordTran),CraftNumber);
                            SupportLib.CreateNoticeDialog(this,
                                    getString(R.string.TheItemYouGotTran),
                                    SelectedRecipe + " * " + CraftNumber,
                                    getString(R.string.ConfirmWordTran));
                        }
                    }
                    //4. Craft finished, Reload RecyclerView.
                    dialog1.cancel();
                    //reload backpack UI.
                    itemIO.RefreshAllList(null);
                    InitializingBackpack();
                    ReloadListUI();
                    //set to default Text to prevent wrong display.
                    TextView BackpackNumberView = findViewById(R.id.BackpackNumberView);
                    BackpackNumberView.setText(getString(R.string.BackpackWordTran));
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of Craft part.


    //Number Input part.
    int CraftNumber = 1;//default value.

    @SuppressLint("SetTextI18n")
    public void OpenCraftNumberInput(View view){
        //1.prepare Input layout.
        final EditText NumberView = new EditText(this);
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        NumberView.setHint(getString(R.string.CraftNumberInputTran));
        //2.show dialog to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.YouAreCraftingTran) + SelectedRecipe + "].");
        dialog.setCancelable(true);
        dialog.setView(NumberView);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {//3.do work.
                    CraftNumber = SupportLib.getInputNumber(NumberView);
                    if(CraftNumber <= 0){
                        CraftNumber = 1;//fix value.
                    }
                    TextView CraftNumberView = findViewById(R.id.CraftNumberView);
                    CraftNumberView.setText(CraftNumber + "");
                    dialog1.cancel();
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of Number Input part.
    //end of Craft system.


    //Backpack import.
    ArrayList <String> ItemNameList = new ArrayList<>();//store all ItemName data.
    ArrayList<Integer> ItemNumberList = new ArrayList<>();//store all ItemNumber data.
    ItemIO itemIO;

    //lv.2 method, main method to load Backpack data to this Activity.
    //thanks to: https://stackoverflow.com/questions/7222873/how-to-test-if-cursor-is-empty-in-a-sqlitedatabase-query ！
    private void InitializingBackpack(){
        ItemNameList = itemIO.getItemNameList();
        ItemNumberList = itemIO.getItemNumberList();
        //prevent from NPE and empty UI.
        if(ItemNameList.isEmpty()){
            SupportLib.CreateNoticeDialog(this,getString(R.string.ErrorWordTran),getString(R.string.NoDataInBackpackTran),getString(R.string.ConfirmWordTran));
            ItemNameList.add(getString(R.string.NothingWordTran));
            ItemNumberList.add(0);
        }
    }


    //RecyclerView support code, thanks to: https://www.cnblogs.com/rustfisher/p/12254732.html !
    //lv.1 method, main method of Reload RecyclerView UI.
    private void ReloadListUI(){
        //1.3 prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //1.3.1 Put list data in Adapter.
        CraftAdapter mLetterAdapter = new CraftAdapter(ItemNameList);
        //1.4 prepare RecyclerView object.
        RecyclerView SelectMaterialView = findViewById(R.id.SelectMaterialView);
        //1.5 Put Adapter to RecyclerView.
        SelectMaterialView.setAdapter(mLetterAdapter);
        //1.6 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        SelectMaterialView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //1.7 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
    }

    //lv.2 Class, Adapter, used in load data from ViewHolder and put data into RecyclerView. Sub Class of RecyclerView.
    private class CraftAdapter extends RecyclerView.Adapter<VH> {

        private final List<String> dataList;

        public CraftAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_backpack, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {//bind (combine) data with [each] item in RecyclerView.
            final String NameString = dataList.get(position);//load data needed to be shown from dataList.
            holder.ItemNameView.setText(NameString);//set TextView`s text in the item.
            //set OnClickListener to each TextView in the item.
            holder.ItemNameView.setOnClickListener(v -> {//do something after Clicked.
                TextView BackpackNumberView = findViewById(R.id.BackpackNumberView);
                BackpackNumberView.setText(getString(R.string.ItemNumberWordTran) + " " + ItemNumberList.get(ItemNameList.indexOf(NameString)));
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
    //end of Backpack import.
}