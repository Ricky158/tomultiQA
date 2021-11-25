package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private static final int SHOW_SYSTEM_TIME = 1;
    private static final int UPDATE_TIMER = 2;
    private static final int TODO_LOAD_SUCCESS = 3;
    private static final int TODO_LOAD_EMPTY = 4;
    private static final int TODO_DB_WORK = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //set Title for ActionBar.thanks to: https://stackoverflow.com/questions/3488664/how-to-set-different-label-for-launcher-rather-than-activity-title !
        //2.get system time.
        InitializingSystemTime();
        //3.load List Items data and refresh UI.
        InitializingToDoData();
    }

    //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
    @Override
    protected void onStart() {
        super.onStart();
        //1.loading Timer State.
        //ResetCounterText() will be executed in handler.
        InitializingTimerData();
    }


    //multi-thread UI handler.
    @SuppressLint("SetTextI18n")
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SHOW_SYSTEM_TIME:
                    //if user is first try in Sign(no record), then set SignedDay to -1 to prevent from unavailable Signing.
                    SignedDay = SupportLib.getIntData(ListActivity.this,"TimerSettingProfile","SignDay",-1);
                    TextView ListTimerDateView = findViewById(R.id.ListTimerDateView);
                    ListTimerDateView.setText(AppYear + "-" + AppMonth + "-" + AppDay);
                    break;
                case UPDATE_TIMER:
                    if(SupportLib.getBooleanData(ListActivity.this, "TimerSettingProfile", "TimerEnabled", false)){
                        ResetCounterText();//if Timer is ON.
                    }else{
                        ConstraintLayout ListTimerLayout = findViewById(R.id.ListTimerLayout);
                        ListTimerLayout.setVisibility(View.GONE);
                    }
                    getWindow().setNavigationBarColor(Color.TRANSPARENT);
                    break;
                case TODO_LOAD_SUCCESS:
                    InitializeToDoUI();
                    break;
                case TODO_LOAD_EMPTY:
                    InitializeToDoUI();
                    ImageView DeleteToDoButton = findViewById(R.id.DeleteToDoButton);
                    ImageView EditToDoButton = findViewById(R.id.EditToDoButton);
                    DeleteToDoButton.setEnabled(false);
                    EditToDoButton.setEnabled(false);
                    break;
                case TODO_DB_WORK:
                    ChangeToDoState();
                    if(IsDeleteMode){
                        DeleteToDoItem(null);
                    }
                    if(IsEditMode){
                        EditToDoItem(null);
                    }
                    //2.4 if AutoDelete function is ON(Delete Item when it has been checked.), execute delete process.
                    ToDoAutoDelete();
                default:
                    break;
            }
            return false;//don't mind it.
        }
    });


    //List system.
    //database.
    Cursor ReturnCursor;
    SQLiteDatabase dbRead = null;
    SQLiteDatabase dbWrite = null;
    //RecyclerView.
    TodoAdapter Adapter;
    List <String> ToDoItemList = new ArrayList<>();
    List <Integer> ToDoCheckedList = new ArrayList<>();
    //selected item record.
    /**
     * You can get the To-do text for last selected item in this variable.
     */
    String ItemSelectedText = "";
    /**
     * You can get the To-do selected situation for last selected item in this variable.
     */
    boolean ToDoChecked;
    /**
     * You can get the name of user last edited to-do item, to using in database operation and RecyclerView UI change.<br/>
     * Notice1: [Edit] including Add, Edit and Delete to-do in database.<br/>
     * Notice2: After changes in database and UI are all done, you need to use <code>ClearLastEdit()</code> method in this Activity to change this variable to initial value.<br/>
     * Default Value: "", means no to-do are changed, or change is done.
     */
    String ItemLastEditName = "";
    boolean IsAutoDeleted = false;

    //lv.2 method, main method of Add a ToDoItem to RecyclerView.
    //thanks to: https://stackoverflow.com/questions/27845069/add-a-new-item-to-recyclerview-programmatically !
    public void AddToDoItem(View view){
        //0.View and Layout preparation.
        final EditText ToDoInput = new EditText(this);
        ToDoInput.setTextSize(16.0f);
        ToDoInput.setHint(getString(R.string.AddNewTodoHintTran));
        //1.1 set basic values of dialog, including content text,button text,and title.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.AddToDoTran));
        dialog.setView(ToDoInput);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    ItemLastEditName = ToDoInput.getText().toString();
                    if(!ItemLastEditName.equals("")){
                        //1.if Input is not empty, add a new line in database.
                        ContentValues values = new ContentValues();
                        values.put(TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText,ItemLastEditName);
                        values.put(TodoDbHelper.DataBaseEntry.COLUMN_NAME_IsFinished,0);
                        dbWrite.insert(TodoDbHelper.DataBaseEntry.TABLE_NAME,null,values);
                        //2.insert a new item to RecyclerView through Adapter.
                        AddChange();
                        //3.if Add TodoItem Button is closed when database is empty, then open it again.
                        ImageView DeleteToDoButton = findViewById(R.id.DeleteToDoButton);
                        ImageView EditToDoButton = findViewById(R.id.EditToDoButton);
                        if(!DeleteToDoButton.isEnabled()){
                            DeleteToDoButton.setEnabled(true);
                        }
                        if(!EditToDoButton.isEnabled()){
                            EditToDoButton.setEnabled(true);
                        }
                    }
                    dialog12.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog1, id) -> dialog1.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //lv.3 method, main method of Delete ToDoItem in RecyclerView.
    boolean IsDeleteMode = false;

    public void DeleteToDoItem(View view){
        if(ItemSelectedText.equals("")){//1.if user not selected any TodoItem, tell user to Select one.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    getString(R.string.DeleteModeOnHintTran),
                    getString(R.string.ConfirmWordTran));
            //Delete Mode ON, in this Mode, First Selected TodoItem will be Recorded.
            IsDeleteMode = true;
            IsEditMode = false;
            ItemSelectedText = "";//reset for data security.
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.RemoveTodoTran));
            dialog.setMessage(getShortToDo() + "\n\n" + getString(R.string.DeleteToDoHint2Tran));
            dialog.setCancelable(true);
            dialog.setPositiveButton(getString(R.string.ConfirmWordTran), (dialog1, which) -> {
                //Delete operation.
                String Selection = TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                dbWrite.delete(
                        TodoDbHelper.DataBaseEntry.TABLE_NAME,
                        Selection,
                        SelectionArgs
                );
                //after Delete, change RecyclerView.
                DeleteChange();
                //ReloadToDoList() included in InitializingToDoData().
                dialog1.cancel();
            });
            dialog.setNeutralButton(getString(R.string.CloseWordTran), (dialog12, which) -> dialog12.cancel());
            dialog.setNegativeButton(getString(R.string.CancelWordTran), (dialog13, which) -> {
                IsDeleteMode = false;
                ItemSelectedText = "";
                dialog13.cancel();
            });
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }

    //lv.3 method, main method of Edit ToDoItem in RecyclerView.
    boolean IsEditMode = false;

    public void EditToDoItem(View view){
        if(ItemSelectedText.equals("")){//1.if user not selected any TodoItem, tell user to Select one.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.HintWordTran),
                    getString(R.string.EditToDoModeOnHint),
                    getString(R.string.ConfirmWordTran));
            //Edit Mode ON, in this Mode, First Selected TodoItem will be Recorded.
            IsEditMode = true;
            IsDeleteMode = false;
            ItemSelectedText = "";//reset for data security.
        }else{
            //0.preparation.
            EditText EditInput = new EditText(this);
            EditInput.setTextSize(16.0f);
            EditInput.setMaxLines(6);
            EditInput.setText(ItemSelectedText);//default value.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.EditToDoTitleTran));
            dialog.setMessage(getShortToDo() + "\n\n" + getString(R.string.DeleteToDoHint3Tran));
            dialog.setView(EditInput);
            dialog.setCancelable(true);
            dialog.setPositiveButton(getString(R.string.ConfirmWordTran), (dialog1, which) -> {
                //Edit operation.
                //0.record user input.
                ItemLastEditName = EditInput.getText().toString();
                //1.prepare values which need to be changed to.
                ContentValues values = new ContentValues();
                values.put(TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText,ItemLastEditName);
                values.put(TodoDbHelper.DataBaseEntry.COLUMN_NAME_IsFinished,0);
                //2.Search which TodoItem should be Edited by it's native name.
                String Selection = TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                //3.update data.
                dbWrite.update(
                        TodoDbHelper.DataBaseEntry.TABLE_NAME,
                        values,
                        Selection,
                        SelectionArgs
                );
                //4.after Edit, change RecyclerView.
                EditChange();
                dialog1.cancel();
            });
            dialog.setNeutralButton(getString(R.string.CloseWordTran), (dialog12, which) -> dialog12.cancel());
            dialog.setNegativeButton(getString(R.string.CancelWordTran), (dialog13, which) -> {
                IsEditMode = false;
                ItemSelectedText = "";
                dialog13.cancel();
            });
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }

    //lv.2 method, main method of AutoDeleteTodo Function.
    private void ToDoAutoDelete(){
        handler.postDelayed(() -> {//delay to let user look default animation.
            if(IsAutoDeleted && ToDoChecked && !IsEditMode && !IsDeleteMode){
                //Delete operation.
                String Selection = TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                dbWrite.delete(
                        TodoDbHelper.DataBaseEntry.TABLE_NAME,
                        Selection,
                        SelectionArgs
                );
                //after Delete.
                RefreshToDoView();
                InitializeToDoUI();//Reload in empty situation, DO NOT REMOVE IT!
            }
        },550);
    }

    //lv.2 method, sub method of Add/Edit/Delete TodoItem. which used in after operation, refresh ToDoItem RecyclerView content and variables.
    private void RefreshToDoView(){
        ToDoItemList.clear();
        ToDoCheckedList.clear();
        IsDeleteMode = false;
        IsEditMode = false;
        ItemSelectedText = "";
        InitializingToDoData();
        //ReloadToDoList() included in InitializingToDoData(). EXCEPT FROM ToDoAutoDelete() method calls.
    }

    /**
     * lv.1 method, sub method of EditToDoItem() and DeleteToDoItem(). if the ToDoItem text is too long, then spilt it and with "..." to reduce UI space usage.
     * @return the reasonable length ToDoItem text.
     */
    //thanks to: https://blog.csdn.net/qq_36216193/article/details/90373744 and https://blog.csdn.net/She_lock/article/details/80052542 !
    private String getShortToDo(){
        String TodoShowToUser;
        int TextLength = ItemSelectedText.length();
        int LineNumber = SupportLib.getTextLineNumber(ItemSelectedText);
        if(TextLength > 15 || LineNumber > 3){
            TodoShowToUser = ItemSelectedText.substring( 0, Math.min(15,TextLength) ).trim() + "...";
            //use Math.min() is prevent from texts with 3 line but not have 15 length, which will be all replaced with "..."
        }else{
            TodoShowToUser = ItemSelectedText;
        }
        return TodoShowToUser;
    }

    //lv.1 method, main method of load List data.And sub method of AddToDoItem() and DeleteToDoItem() method.
    private void InitializingToDoData(){
        Thread TodoLoad = new Thread(() -> {
            //0.preparation.
            Message message = new Message();
            //1.get database object.
            TodoDbHelper DBHelper = new TodoDbHelper(this);
            if(dbRead == null){//if database have not loaded yet, or method will passed this part to improve performance.
                dbRead = DBHelper.getReadableDatabase();
                dbWrite = DBHelper.getWritableDatabase();
            }
            ReturnCursor = dbRead.query(
                    TodoDbHelper.DataBaseEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            //after loading, reset the position of Cursor.
            ReturnCursor.moveToFirst();
            //2.fill Cursor with entire database`s data.
            if(ReturnCursor != null && ReturnCursor.getCount() > 0){//prevent from NPE.
                //3.fill entire List data of RecyclerView from Cursor.
                //3.1 get ID of columns in database.
                int ToDoTextColumnId = ReturnCursor.getColumnIndex(TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText);
                int ToDoStateColumnId = ReturnCursor.getColumnIndex(TodoDbHelper.DataBaseEntry.COLUMN_NAME_IsFinished);
                boolean IsMoreData = !ReturnCursor.isLast();//check Is it more data available to query.
                do {//query at first position of Cursor.
                    if(ReturnCursor.isLast()){
                        IsMoreData = false;
                    }
                    ToDoItemList.add(ReturnCursor.getString(ToDoTextColumnId));
                    ToDoCheckedList.add(ReturnCursor.getInt(ToDoStateColumnId));
                    ReturnCursor.moveToNext();
                }while (IsMoreData);//if IsMoreData is false(no more data), stop query, if not, continue query.
                message.what = TODO_LOAD_SUCCESS;
            }else{
                message.what = TODO_LOAD_EMPTY;
            }
            //3.complete loading.
            handler.sendMessage(message);
        });
        TodoLoad.start();
    }

    //lv.1 method, main method of fill RecyclerView with List.
    private void InitializeToDoUI(){
        //1. prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //1.1 Put list data in Adapter.
        Adapter = new TodoAdapter(ToDoItemList);
        //2. prepare RecyclerView object.
        RecyclerView ToDoListView = findViewById(R.id.ToDoListView);
        //2.1 Put Adapter to RecyclerView.
        ToDoListView.setAdapter(Adapter);
        //2.2 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        ToDoListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //2.4 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
        //2.5 refresh load ToDoItem number to user.
        TextView ToDoItemNumberView = findViewById(R.id.ToDoItemNumberView);
        ToDoItemNumberView.setText(String.valueOf(ToDoItemList.size()));
    }

    //copy note text to system clipboard.
    //thanks to: https://blog.csdn.net/asdf717/article/details/52678009 !
    // https://stackoverflow.com/questions/33207809/what-exactly-is-label-parameter-in-clipdata-in-android !
    // https://blog.csdn.net/zhuifengshenku/article/details/19080227 !
    public void BackupTodo(View view){
        //preparation.
        EditText AllTodoView = new EditText(this);
        AllTodoView.setHeight(600);//prevent the EditText too small.
        AllTodoView.setTextSize(16.0f);
        StringBuilder TodoContent = new StringBuilder();//preset value.
        //loading text.
        int Position = 0;
        while(Position < ToDoItemList.size()){
            TodoContent.append(ToDoItemList.get(Position)).append("\n\n\n");
            Position = Position + 1;
        }
        //add text to EditText.
        AllTodoView.setText(TodoContent.toString());
        //create dialog.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.TodoBackupTitleTran));
        dialog.setMessage(getString(R.string.UserCopyHintTran));
        dialog.setView(AllTodoView);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.CopyWordTran),
                (dialog13, id) -> {
                    //paste note to clipboard.
                    ClipboardManager mCM = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Todo from tomultiQA", TodoContent);
                    mCM.setPrimaryClip(mClipData);
                    dialog13.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CloseWordTran),
                (dialog14, id) -> dialog14.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    //RecyclerView support code, thanks to: https://www.cnblogs.com/rustfisher/p/12254732.html !
    //animation: https://www.jianshu.com/p/4f9591291365 !
    //TextView line effect: https://blog.csdn.net/zhuzhiqiang_zhu/article/details/50755980 !
    //lv.2 Class, Adapter, used in load data from ViewHolder and put data into RecyclerView. Sub Class of RecyclerView. Used in ReloadToDoList() method.
    private class TodoAdapter extends RecyclerView.Adapter<VH> {

        private final List<String> dataList;//store DataList in this class only.

        public TodoAdapter(List<String> dataList) {
            this.dataList = dataList;
        }//initializing the class code.(import variable) just like the OnCreate() method in a Activity.

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//load the item appearance xml file of RecyclerView.
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_list, parent, false));
        }

        @Override
        @SuppressLint("RecyclerView")
        public void onBindViewHolder(@NonNull VH holder, int position) {//bind (combine) data and control logics with [each] item in RecyclerView.
            //1.initializing state of ToDoItems.
            //get Item's Text showed in UI, using in searching position of Item information in Activity's List.
            //if search Adapter internal List, then will crash because of Out of Index Exception.
            String NameString = dataList.get(position);
            //calculate the position of Item's Text in Activity's List.
            //"Out" means: out of this class, aka Activity.
            int OutPosition = ToDoItemList.indexOf(NameString);
            //set TextView`s text in the item.
            holder.ToDoItem.setText(NameString);
            //if Checked State in List is 1, check the CheckBox, or uncheck it when is 0.
            holder.ToDoItem.setChecked(ToDoCheckedList.get(OutPosition) == 1);
            //2.set OnClickListener on each ToDoItem.
            holder.ToDoItem.setOnClickListener(v -> {//2.1 do something after Clicked.
                //2.2 report which Item is Clicked.
                ItemSelectedText = NameString;
                //2.3 Branch Action.
                //2.3.1 if in Delete Mode, the Check State will not be changed, instead of only Recording Item`s information.
                //the Mode will be changed in DeleteToDoItem() method.
                //2.3.2 change Item Checked state when Item is Clicked.
                ToDoChecked = holder.ToDoItem.isChecked();
                //2.3.3 change Item Checked state in database.
                Message message = new Message();
                message.what = TODO_DB_WORK;
                handler.sendMessage(message);
            });
        }

        @Override
        public int getItemCount() { return dataList.size(); }//get item number.

        //get real-time item position to fix specific problems.
        //thanks to: https://stackoverflow.com/questions/32771302/recyclerview-items-duplicate-and-constantly-changing !
    }

    //lv.1 Class, ViewHolder, sub Class of onCreateViewHolder() method in [Adapter] Class.
    //control the content of each item in RecyclerView.
    public static class VH extends RecyclerView.ViewHolder {
        public View item; // represent entire item object.(layout)
        public CheckBox ToDoItem;

        public VH(@NonNull View itemView) {//ViewHolder Constructor definition.
            super(itemView);//declare import method from Father Class.
            item = itemView;//store item in RecyclerView to be used by another method in different Class.
            ToDoItem = itemView.findViewById(R.id.ToDoShowView);//import item xml from project file.
        }
    }

    //RecyclerView Refresh methods, thanks to:
    // https://blog.csdn.net/guibao513/article/details/99644931 !
    // https://www.jianshu.com/p/a49e407474bb !
    // https://blog.csdn.net/dianzi314779725/article/details/99829585 !
    // https://blog.csdn.net/iblade/article/details/81810945 !
    // https://blog.csdn.net/shulianghan/article/details/113544538 !

    //Adding bug fix: https://www.jianshu.com/p/2eca433869e9 !
    /*
       Not using getCount() in XxxUiChanges() method, instead of using dataList.size():
       The getCount() method return the size of dataList in the Adapter.
       but if the Adapter data need to update, then you call this method will cause Out of Index Exception. Because the dataList is still not changed.
       So, we need to get the updated dataList's size before change Adapter, that means we can't rely on method in Adapter.
       But in adding method, the Position should rely on Adapter, because of we are getting the initial size of List.
    */

    /**
     * Sub method of <code>AddToDoItem()</code>, using in Refresh RecyclerView UI and its dataset.<br/>
     * After the database operation is done, use this method to finish UI changes in RecyclerView.
     */
    private void AddChange(){
        //Sync with Activity's List and Adapter's List.
        ToDoItemList.add(ItemLastEditName);
        ToDoCheckedList.add(0);//By default, to-do is not finished(0) when created.
        //refresh item position of RecyclerView, from Item which has changed to next element
        //after Adding Item, get the size of List.
        int AddPosition = Adapter.getItemCount();
        //RecyclerView UI, and refresh dataList in Adapter.
        Adapter.notifyItemInserted(AddPosition);
        Adapter.notifyItemRangeChanged(AddPosition, Adapter.getItemCount());
        ClearLastEdit();
    }

    /**
     * Sub method of <code>EditToDoItem()</code>, using in Refresh RecyclerView UI and its dataset.<br/>
     * After the database operation is done, use this method to finish UI changes in RecyclerView.
     */
    private void EditChange(){
        int EditPosition = Adapter.dataList.indexOf(ItemSelectedText);//before changed name.
        if(EditPosition != -1){
            //Sync with Activity's List and Adapter's List.
            ToDoItemList.set(EditPosition,ItemLastEditName);//change the name.
            Adapter.dataList.set(EditPosition,ItemLastEditName);
            //refresh the Position information for Editing Item.
            Adapter.notifyItemChanged(EditPosition);
            ClearLastEdit();
        }else{
            finish();//if the Elements in Activity's List and Adapter's List are not sync, then crash.
        }
    }

    /**
     * Sub method of <code>DeleteToDoItem()</code>, using in Refresh RecyclerView UI and its dataset.<br/>
     * After the database operation is done, use this method to finish UI changes in RecyclerView.
     */
    private void DeleteChange(){
        //find which Element should be Delete and its position.
        int DeletePosition = Adapter.dataList.indexOf(ItemSelectedText);
        if(DeletePosition > -1 && Adapter.dataList.size() > 1){//NPE check.
            //Sync with Activity's List and Adapter's List.
            ToDoItemList.remove(DeletePosition);
            ToDoCheckedList.remove(DeletePosition);
            //RecyclerView UI, and refresh dataList in Adapter.
            Adapter.notifyItemRemoved(DeletePosition);
            Adapter.notifyItemRangeRemoved(DeletePosition, Adapter.getItemCount() - DeletePosition);
            ClearLastEdit();
        }else if(Adapter.dataList.size() <= 1){//delete last Element in two List.
            //Sync with Activity's List and Adapter's List.
            ToDoItemList.clear();
            ToDoCheckedList.clear();
            Adapter.dataList.clear();
            //RecyclerView UI.
            InitializeToDoUI();//reset all data, because of empty list, this is nearly no impact for performance.
            ClearLastEdit();
        }else{
            finish();//if the Elements in Activity's List and Adapter's List are not sync, then crash.
        }
    }

    /**
     * lv.1 sub method, clear the <code>ItemLastEditName</code> record to prevent changes which out of expectation.
     */
    private void ClearLastEdit() {
        ItemSelectedText = "";
        ItemLastEditName = "";
    }

    /**
     * lv.1 method, sub method of <code>onBindViewHolder()</code> method in TodoAdapter Class.
     */
    private void ChangeToDoState(){
        //1.preparation.
        //1.1 Search entire database with ToDoItemText.
        String Selection = TodoDbHelper.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
        String[] SelectionArgs = new String[]{ItemSelectedText};
        //2.check the if ToDoItem is Checked.
        int StateCode;
        if(ToDoChecked){
            StateCode = 1;
        }else{
            StateCode = 0;
        }
        //3.prepare the content which needed to change.
        ContentValues ToDoItemValues = new ContentValues();
        ToDoItemValues.put(TodoDbHelper.DataBaseEntry.COLUMN_NAME_IsFinished,StateCode);
        //4.update Checked State in database.
        dbWrite.update(
                TodoDbHelper.DataBaseEntry.TABLE_NAME,
                ToDoItemValues,
                Selection,
                SelectionArgs
        );
    }//end of RecyclerView support code.

    //List Setting Function.
    @SuppressLint({"SetTextI18n", "UseSwitchCompatOrMaterialCode"})
    public void ListSettingEntry(View view){
        //0.preparation.
        Switch AutoDeleteSwitch = new Switch(this);
        AutoDeleteSwitch.setText(getString(R.string.AutoDeleteTodoTran));
        AutoDeleteSwitch.setTextSize(16.0f);
        AutoDeleteSwitch.setChecked(SupportLib.getBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",false));
        //1.dialog to show to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.SettingTitleTran));
        dialog.setView(AutoDeleteSwitch);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {
                    IsAutoDeleted = AutoDeleteSwitch.isChecked();
                    SupportLib.saveBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",IsAutoDeleted);
                    dialog1.cancel();
                });
        dialog.setNeutralButton(
                getString(R.string.HelpWordTran),
                (dialog13, which) -> SupportLib.CreateNoticeDialog(this,
                        getString(R.string.HelpWordTran),
                        getString(R.string.AutoDeleteTodoHintTran),
                        getString(R.string.ConfirmWordTran)
                ));
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, id) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    public void ShowListSettingHelp(View view){
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                ValueLib.TODO_LIST_HELP,
                getString(R.string.ConfirmWordTran));
    }//end of List Setting Function.
    //end of List system.


    //Timer Layout Management. and sub method of InitializingTimerData() method.
    public void CloseTimerLayout(View view){
        ConstraintLayout ListTimerLayout = findViewById(R.id.ListTimerLayout);
        ListTimerLayout.setVisibility(View.GONE);
    }

    //Floating Button: Go to MainActivity.
    public void GoToMainActivity(View view){
        if(SupportLib.getBooleanData(ListActivity.this, "TimerSettingProfile", "TimerEnabled", false)
                && AppDay != SignedDay//Day check.
        ){
            CreateNotificationChannel();
            ShowTimerNotification();
            //finish signed.
        }
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }//end of Floating Button.


    //timer system.
    int SignedDay;//save recent sign DAY for comparison.
    int TimerProgress = 0;
    int TimerTargetDay = 0;

    //lv.2 method, main method to check Timer State.(and functions)
    private void InitializingTimerData(){
        Thread TimerLoad = new Thread(() -> {
            TimerProgress = SupportLib.getIntData(this,"TimerSettingProfile","TimerProgress", 0);
            TimerTargetDay = SupportLib.getIntData(this,"TimerSettingProfile","TimerTargetDay", 30);
            IsAutoDeleted = SupportLib.getBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",false);//load function.
            Message message = new Message();
            message.what = UPDATE_TIMER;
            handler.sendMessage(message);
        });
        TimerLoad.start();
    }

    //lv.3 method,main method of Timer Sign, now executed in GoToMainActivity() method.
    private void ShowTimerNotification(){
        boolean TimerFortuneState = SupportLib.getBooleanData(this,"TimerSettingProfile","TimerFortune",false);
        String TimerReportText = "";
        //1.Fortune Text preparation.
        if(TimerFortuneState){
            //daily fortune.
            String[] Fortunes = new String[]{
                    getString(R.string.Fortune5WordTran),
                    getString(R.string.Fortune4WordTran),
                    getString(R.string.Fortune3WordTran),
                    getString(R.string.Fortune2WordTran),
                    getString(R.string.Fortune1WordTran)
            };
            //add texts.---" "(tab) and "\n\n" is for beauty reason.
            TimerReportText = TimerReportText + getString(R.string.TodayFortuneTran) + " " + Fortunes[SupportLib.CreateRandomNumber(0,4)] + "\n\n";
        }
        //2.daily sentence.
        int SentenceId = SupportLib.CreateRandomNumber2(0, ValueLib.SIGN_SENTENCES_EN.length - 1);
        if(!Locale.getDefault().getCountry().equals("CN")){//if language is not in Chinese.
            TimerReportText = TimerReportText + ValueLib.SIGN_SENTENCES_EN[SentenceId];
        }else{
            TimerReportText = TimerReportText + ValueLib.SIGN_SENTENCES_CN[SentenceId];
        }
        //3.Sign and notice user.
        //condition: AppDay != SignedDay,which is prevent from repeat notification in single day, now moved to GoToMainActivity() method.
        if(TimerProgress < TimerTargetDay){
            //2.1 add Progress.
            TimerProgress = TimerProgress + 1;
            //thanks to: https://stackoverflow.com/questions/23179795/android-onclicklistener-intent-and-context !
            SupportLib.saveIntData(ListActivity.this,"TimerSettingProfile","TimerProgress",TimerProgress);
            //2.2 Change Timer UI.
            ResetCounterText();
            //2.3 Show progress in the Notification and send to user.
            String TimerUnitText = SupportLib.getStringData(this,"TimerSettingProfile","TimerUnit","Sol.");
            TimerReportText = TimerReportText + "\n" + TimerUnitText + " " + TimerProgress + " / " + TimerTargetDay;
            //2.4 send.
            ShowSignNotification(TimerReportText);
        }else{
            ShowSignNotification(getString(R.string.FinishSignGoalHintTran));
        }
        //for data stable reason, don't use multi-thread here.
        //4.save Today`s DAY to file, to check if user is Signed.
        SignedDay = AppDay;
        SupportLib.saveIntData(this,"TimerSettingProfile","SignDay",SignedDay);
        //4.refresh the Daily Chance data.
        SupportLib.saveIntData(this,"BattleDataProfile","DailyChance",1);
    }

    //lv.1 method, sub method of OnCreate() and ShowTimerDialog() method.
    @SuppressLint("SetTextI18n")
    private void ResetCounterText(){
        TextView ListTimerUnitView = findViewById(R.id.ListTimerUnitView);
        TextView ListTimerProgressView = findViewById(R.id.ListTimerProgressView);
        TextView ListTimerPercentView = findViewById(R.id.ListTimerPercentView);
        ProgressBar ListTimerProgress = findViewById(R.id.ListTimerProgress);
        String TimerUnitText = SupportLib.getStringData(this,"TimerSettingProfile","TimerUnit","Sol.");
        ListTimerUnitView.setText(TimerUnitText);
        ListTimerProgressView.setText(TimerProgress + " / " + TimerTargetDay);
        int Percent = SupportLib.CalculatePercent(TimerProgress,TimerTargetDay);
        ListTimerProgress.setProgress(Percent);
        ListTimerPercentView.setText(Percent + "%");
    }//end of timer system.


    //System Time Function.
    int AppYear;
    int AppMonth;
    int AppDay;

    //lv.1 method, main of get system time method.
    //thanks to: https://blog.csdn.net/huangshenshen_/article/details/57161724 !
    private void InitializingSystemTime(){
        Thread TimeLoad = new Thread(() -> {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Calendar c = Calendar.getInstance();
                AppYear = c.get(Calendar.YEAR);
                AppMonth = c.get(Calendar.MONTH) + 1;//month is based on 0.
                AppDay = c.get(Calendar.DAY_OF_MONTH);
            }else{
                Time time = new Time();
                time.setToNow();
                AppYear = time.year;
                AppMonth = time.month + 1;//month is based on 0.
                AppDay = time.monthDay;
            }
            Message message = new Message();
            message.what = SHOW_SYSTEM_TIME;
            handler.sendMessage(message);
        });
        TimeLoad.start();
    }//end of System Time Function.


    //Notification system, which belongs to Timer system.
    //thanks to: https://developer.android.google.cn/training/notify-user/build-notification?hl=zh-cn#SimpleNotification !
    //lv.2 method, main method of show sign notification, thanks to: https://www.jianshu.com/p/27ef49a0db29 and https://www.jianshu.com/p/6aec3656e274!
    private void ShowSignNotification(String Content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ValueLib.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.OpenNotificationHintTran))
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(getString(R.string.SignSuccessHintTran))
                        .bigText(Content)
                )
                .setChannelId(ValueLib.CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        //id: tell system to send message to user through which channel that App generated.
        notificationManager.notify(ValueLib.NOTIFICATION_SYSTEM_ID, builder.build());
    }

    //lv.1 method, sub method of ShowSignNotification() method, android 8.0+ compatible code.
    private void CreateNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.SignChannelWordTran);
            String description = getString(R.string.NotificationChannelHintTran);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ValueLib.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }//end of Notification system.
}