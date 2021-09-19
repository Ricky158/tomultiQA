package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BackpackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpack);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //1.initializing recyclerview.
        //1.1 prepare content which needed to be showed to user.
        //1.2 add content to ArrayList, data from ItemDataBase.
        InitializingBackpack();
        InitializingSellData();
        //1.3 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
        //2.initialize TabLayout function.
        //thanks to: https://segmentfault.com/a/1190000008753052 !
        TabLayout ItemTypeLayout = findViewById(R.id.ItemTypeLayout);
        //set default selected Tab, to prevent from NPE.
        Objects.requireNonNull(ItemTypeLayout.getTabAt(0)).select();
        SelectedTabName = getString(R.string.ResourceWordTran);
        //initializing UI.
        InitializingBackpack();
        //set Tab selected listener.
        ItemTypeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//get Current Selected Tab in TabLayout.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SelectedTabName = Objects.requireNonNull(tab.getText()).toString();
                InitializingBackpack();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                SelectedTabName = Objects.requireNonNull(tab.getText()).toString();
                InitializingBackpack();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SelectedTabName = Objects.requireNonNull(tab.getText()).toString();
                InitializingBackpack();
            }
        });
    }

    //add a button menu to ActionBar in Activity.
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
        if (item.getItemId() == R.id.action_TimerHistory) {//if setting icon in Menu be touched.
            SupportClass.CreateNoticeDialog(this,
                    getString(R.string.HelpWordTran),
                    ValueClass.ITEM_SYSTEM_HELP,
                    getString(R.string.ConfirmWordTran));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbRead.close();
        dbWrite.close();
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
        @SuppressLint("RecyclerView")
        public void onBindViewHolder(@NonNull VH holder,  int position) {//bind (combine) data with [each] item in RecyclerView.
            final String NameString = dataList.get(position);//load data needed to be shown from dataList.
            holder.ItemNameView.setText(NameString);//set TextView`s text in the item.
            //set OnClickListener to each TextView in the item.
            holder.ItemNameView.setOnClickListener(v -> {//do something after Clicked.
                ItemSelectedName = NameString;//return selected item name to String field.
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
    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;
    List <String> ItemNameList = new ArrayList<>();//store all ItemName data.
    //which any Item in RecyclerView was selected, its Name and Text(Description) to be showed will be wrote in here.
    int ItemSelectedId = -1;
    String ItemSelectedName = "";
    String ItemTextToShow = "--";
    String ItemTypeToShow = "--";
    int ItemNumberToShow = 0;
    String SelectedTabName = "";

    //lv.2 method, main method.
    //thanks to: https://stackoverflow.com/questions/7222873/how-to-test-if-cursor-is-empty-in-a-sqlitedatabase-query ！
    private void InitializingBackpack(){
        ItemDataBaseBasic DBHelper = new ItemDataBaseBasic(this);
        if(dbRead == null){//if db were not loaded, do load, or skip loading to cut down process time.
            dbRead = DBHelper.getReadableDatabase();
            dbWrite = DBHelper.getWritableDatabase();
        }
        //1.query entire backpack database, store all query result.
        String Selection = ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemType + " = ?";
        String[] SelectionArgs = {SelectedTabName};
        SearchDataBase(Selection,SelectionArgs);
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
            // prevent from NPE and empty UI.
            ItemNameList.clear();//prevent from repeat add item.
            ItemSelectedName = "--";//repeat load RecyclerView should clear selected Item inf.
            ItemTextToShow = getString(R.string.NothingSelectedTran);
            ItemTypeToShow = getString(R.string.NoTypeTran);
            ItemNumberToShow = 0;
        }
        //3.show empty information to user.
        ShowItemInformation();
        //4.1 prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //4.2 Put list data in Adapter.
        LetterAdapter mLetterAdapter = new LetterAdapter(ItemNameList);
        //4.3 prepare RecyclerView object.
        RecyclerView BackpackView = findViewById(R.id.BackpackView);
        //4.4 Put Adapter to RecyclerView.
        BackpackView.setAdapter(mLetterAdapter);
        //4.5 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        BackpackView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    //lv.2 method, sub method of onBindViewHolder() method in [Adapter] Class.
    @SuppressLint("Range")
    public void LoadSelectedItem(){
        if(ReturnCursor != null && ReturnCursor.getCount() > 0 && ItemSelectedId != -1){
            try {
                ReturnCursor.moveToPosition(ItemSelectedId);
            }catch (android.database.CursorIndexOutOfBoundsException e){
                ReturnCursor.moveToFirst();
            }
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


    //Craft Function Entry.
    public void OpenCraftActivity(View view){
        Intent i = new Intent(this,CraftActivity.class);
        startActivity(i);
    }

    //Sell Function Entry.
    int UserPoint;
    long PointRecord;

    //lv.2 method, main method of Sell Function.
    @SuppressLint("SetTextI18n")
    public void StartSellItem(View view){
        //1.prepare Input layout.
        final EditText NumberView = new EditText(this);
        NumberView.setInputType(InputType.TYPE_CLASS_NUMBER);
        NumberView.setHint(getString(R.string.InputSellNumberHintTran));
        //2.show user the dialog.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("[" + ItemSelectedName + getString(R.string.HintSellNumberTran));
        dialog.setView(NumberView);
        dialog.setMessage(getString(R.string.ItemNumberWordTran) + " " + ItemNumberToShow);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    //3.1 get user want to sell number.
                    int SellNumber = SupportClass.getInputNumber(NumberView);
                    //3.2 start sell process.
                    if(!ItemSelectedName.equals("") && SellNumber <= ItemNumberToShow && SellNumber > 0){
                        //3.2.0 preparation.
                        TextView ItemNumberView = findViewById(R.id.ItemNumberView);
                        String Selection = ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
                        String[] SelectionArgs = {ItemSelectedName};
                        ContentValues ItemNumberValues = new ContentValues();
                        //3.2.1 get data from database, and fill Cursor.
                        SearchDataBase(Selection,SelectionArgs);
                        //3.2.2 get if Item Exist situation from Cursor.
                        boolean IsItemExist = ReturnCursor.moveToFirst();
                        //3.3 do Item data insert / update branch.
                        if(IsItemExist){//3.3.1 if item is in database.
                            //3.3.1.0 preparation.
                            int AfterSold = ItemNumberToShow - SellNumber;//record after item are sold, item's number.
                            int SellReward = SellNumber * 1000;
                            //3.3.1.1 change the number and save changes.
                            if(AfterSold >= 0){
                                ItemNumberValues.put(ItemDataBaseBasic.DataBaseEntry.COLUMN_NAME_ItemNumber,AfterSold);
                                dbWrite.update(
                                        ItemDataBaseBasic.DataBaseEntry.TABLE_NAME,
                                        ItemNumberValues,
                                        Selection,
                                        SelectionArgs
                                );
                                //3.3.2 if item was successfully sold some, then give user reward and save data.
                                //3.3.2.1 refresh data when selling is done, to prevent from data loading error.
                                ItemNameList.clear();//prevent from repeat add item.
                                InitializingBackpack();
                                //3.3.2.2 get Points and show result after selling.
                                UserPoint = UserPoint + SellReward;
                                PointRecord = PointRecord + SellReward;
                                SupportClass.saveLongData(this,"RecordDataFile","PointGotten",PointRecord);
                                SupportClass.saveIntData(this,"BattleDataProfile","UserPoint",UserPoint);
                                //3.3.2.3 after checked user have enough item to sell, then actually cost item and show changes to user.
                                ItemNumberToShow = AfterSold;
                                ItemNumberView.setText(getString(R.string.ItemNumberWordTran) + " " + ItemNumberToShow);
                            }else{//3.3.1.2 if user doesn't have enough item at all.(AfterSold < 0)
                                SupportClass.CreateNoticeDialog(
                                        this,
                                        getString(R.string.ErrorWordTran),
                                        "Number is larger than you have.",
                                        getString(R.string.ConfirmWordTran)
                                        );
                            }
                        }else if(ItemSelectedName.equals("")){//3.3.4 if item is not in database.(or no item is selected.)
                            SupportClass.CreateNoticeDialog(
                                    this,
                                    getString(R.string.ErrorWordTran),
                                    "No item selected yet.",
                                    getString(R.string.ConfirmWordTran));
                        }
                    }
                    dialog12.cancel();
                });
            dialog.setNegativeButton(
                    getString(R.string.CancelWordTran),
                    (dialog1, id) -> dialog1.cancel());
        //3. Use this object to create a actual View in android.
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.1 method, main method.
    private void InitializingSellData(){
        Thread thread = new Thread(() -> {
            UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);
            PointRecord = SupportClass.getLongData(this,"RecordDataFile","PointGotten",0);
        });
        thread.start();
    }

    //lv.1 method, sub method.
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
    }
}
