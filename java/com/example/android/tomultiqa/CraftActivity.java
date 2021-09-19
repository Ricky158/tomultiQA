package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        InitializingBackpack();
        InitializingRecipePicker();
        ReloadListUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbRead.isOpen()){
            dbRead.close();
        }
        if(dbWrite.isOpen()){
            dbWrite.close();
        }
        if(!ReturnCursor.isClosed()){
            ReturnCursor.close();
        }
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
                SelectedRecipe = "Nothing is selected.";
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
            SupportClass.CreateNoticeDialog(this,
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
                        boolean IsCostCompleted = CostItem(ItemCostList,CostNumberList);
                        //3.add Crafted Item to database.
                        if(IsCostCompleted){
                            AddItemToDb(SelectedRecipe,CraftNumber);
                            SupportClass.CreateNoticeDialog(this,
                                    getString(R.string.TheItemYouGotTran),
                                    SelectedRecipe + " * " + CraftNumber,
                                    getString(R.string.ConfirmWordTran));
                        }
                    }
                    //4. Craft finished, Reload RecyclerView.
                    dialog1.cancel();
                    //reload backpack UI.
                    ItemNameList = new ArrayList<>();
                    ItemNumberToShow = new ArrayList<>();
                    InitializingBackpack();
                    ReloadListUI();
                });
        dialog.setNegativeButton(getString(R.string.CancelWordTran),
                (dialog12, which) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.2 method, sub method of StartCrafting() method. Add a item to database, you need to input item's name and AddNumber to use method.
    @SuppressLint("Range")
    private void AddItemToDb(String ItemName, int AddNumber){
        //3.according to got list, save Crafting items data to database.
        //3.0 prepare method parameter.
        String Selection = ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
        String[] SelectionArgs = {ItemName};
        ContentValues ItemNumberValues = new ContentValues();
        //3.1 get data from database, and fill Cursor.
        SearchDataBase(Selection,SelectionArgs);
        //3.1.1 get if any Item Exist in Cursor.
        boolean IsItemExist = ReturnCursor.moveToFirst();
        //3.2 do Item data insert / update branch.
        if(IsItemExist){//3.2.1 if item is in database.
            int ItemNumberInDb = ReturnCursor.getInt(ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber));
            //3.2.1.1 change the number and save changes.
            ItemNumberValues.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,ItemNumberInDb + AddNumber);
            dbWrite.update(
                    ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                    ItemNumberValues,
                    Selection,
                    SelectionArgs
            );
        }else{//3.2.2 if item is not in database.
            //3.2.2.1 initializing item column in database, and the number will be set to "CraftNumber".
            //ItemNumberValues.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,AddNumber);
            NewItemColumn(ItemName,getString(R.string.ResourceWordTran),getItemText(ItemName),AddNumber);
        }
    }

    //lv.2 method, sub method of StartCrafting() method. Cost multi items by List, these two list record which item and their cost numbers in database.
    @SuppressLint("Range")
    private boolean CostItem(ArrayList<String> ItemCostList, ArrayList<Integer> CostNumberList){
        //0. preparation.
        String Selection = ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
        ArrayList<Integer> ItemNumberInDb = new ArrayList<>();
        //1.for each item should be cost, get its number in database.
        for(String Item : ItemCostList){
            //1.1 prepare data.
            String[] NeedToSearch ={Item};
            //1.2 get items data from database, and fill Cursor.
            SearchDataBase(Selection,NeedToSearch);
            //1.3 get if Item Exist situation from Cursor.
            boolean IsItemExist = ReturnCursor.moveToFirst();
            if(IsItemExist){
                //1.4 get Item Number in database.
                ItemNumberInDb.add(ReturnCursor.getInt(ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber) ) );
            }
        }
        //2.after get item number list, for each number in list, do check process.
        boolean IsAllItemHaveEnough = true;
        for(int NeedToCheck : ItemNumberInDb){
            if(NeedToCheck < CostNumberList.get(ItemNumberInDb.indexOf(NeedToCheck))){
                IsAllItemHaveEnough = false;//if any number in list smaller than which need to cost, then false.
            }
        }
        //3.if all Item have enough number to cost, then do Craft process, or quit Craft.
        if(IsAllItemHaveEnough){
            for(int EachNumber : CostNumberList){
                int Position = CostNumberList.indexOf(EachNumber);//the Id of each item in CostNumberList,used in calculate item's number which after cost.
                int UpdatedNumber = ItemNumberInDb.get(Position) - CostNumberList.get(Position);//calculate each item's number which after cost.
                ContentValues ChangeValue = new ContentValues();
                ChangeValue.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,UpdatedNumber);//update number data in db.
                dbWrite.update(
                        ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                        ChangeValue,
                        Selection,
                        new String[]{ItemCostList.get(CostNumberList.indexOf(EachNumber))}
                );
            }
            return true;//cost success.
        }else{
            SupportClass.CreateNoticeDialog(
                    this,
                    getString(R.string.ErrorWordTran),
                    getString(R.string.NotEnoughToCraftTran),
                    getString(R.string.ConfirmWordTran)
            );
            return false;//cost fail.
        }
    }

    //lv.1 method, sub method. Provide create new column in database support.
    private void NewItemColumn(String ItemName, String ItemType, String ItemText, int ItemNumber){
        ContentValues values = new ContentValues();
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName,ItemName);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemType,ItemType);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemText,ItemText);
        values.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,ItemNumber);
        dbWrite.insert(ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,null,values);
    }

    //lv.1 method, sub method of search entire Item database.
    private void SearchDataBase(String Selection, String[] SelectionArgs){
        ReturnCursor = dbRead.query(//let Cursor return the ItemName and ItemNumber data in this name line.
                ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                null,
                Selection,
                SelectionArgs,
                null,
                null,
                null
        );
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
                    CraftNumber = SupportClass.getInputNumber(NumberView);
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
    List <String> ItemNameList = new ArrayList<>();//store all ItemName data.
    Cursor ReturnCursor;
    ArrayList<Integer> ItemNumberToShow = new ArrayList<>();//store all ItemNumber data.
    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;

    //lv.2 method, main method to load Backpack data to this Activity.
    //thanks to: https://stackoverflow.com/questions/7222873/how-to-test-if-cursor-is-empty-in-a-sqlitedatabase-query ！
    private void InitializingBackpack(){
        ItemDataBaseBasic DBHelper = new ItemDataBaseBasic(this);
        dbRead = DBHelper.getReadableDatabase();
        dbWrite = DBHelper.getWritableDatabase();
        //1.query entire backpack database, store all query result.
        SearchDataBase(null,null);
        //2.load data to variables.
        if(ReturnCursor != null && ReturnCursor.getCount() > 0){//prevent from NPE.
            ReturnCursor.moveToFirst();//prevent from -1 position.
            //calculate variables and keep them to improve efficiency.
            int ItemNameColumnId = ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName);
            int ItemNumberColumnId = ReturnCursor.getColumnIndex(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber);
            boolean IsMoreData = !ReturnCursor.isLast();//check Is it more data available to query.
            do {//query at first position of Cursor.
                if(ReturnCursor.isLast()){
                    IsMoreData = false;
                }
                ItemNameList.add(ReturnCursor.getString(ItemNameColumnId));
                ItemNumberToShow.add(ReturnCursor.getInt(ItemNumberColumnId));
                ReturnCursor.moveToNext();
            }while (IsMoreData);//if IsMoreData is false(no more data), stop query, if not, continue query.
        }else{//no data in Cursor.
            SupportClass.CreateNoticeDialog(this,getString(R.string.ErrorWordTran),getString(R.string.NoDataInBackpackTran),getString(R.string.ConfirmWordTran));
            //prevent from NPE and empty UI.
            ItemNameList.add(getString(R.string.NothingWordTran));
            ItemNumberToShow.add(0);
        }
    }

    //lv.1 method, sub method of loading ItemText.
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
                BackpackNumberView.setText(getString(R.string.ItemNumberWordTran) + " " + ItemNumberToShow.get(ItemNameList.indexOf(NameString)));
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