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
import android.text.InputType;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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

    private static final int UPDATE_SYSTEM_TIME = 1;
    private static final int UPDATE_TIMER = 2;
    private static final int TODO_LOAD_SUCCESS = 3;
    private static final int TODO_LOAD_EMPTY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //set Title for ActionBar.thanks to: https://stackoverflow.com/questions/3488664/how-to-set-different-label-for-launcher-rather-than-activity-title !
        //2.get system time.
        InitializingSystemTime();
        //3.load List Items.
        InitializingToDoData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //1.loading Timer State.
        //ResetCounterText() will be executed in handler.
        InitializingTimerData();
        //4.load List RecyclerView according to db.
        //ReloadToDoList() will be executed in handler.
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        //postDelayed() is for reduce loading time.
        handler.postDelayed(() -> getWindow().setNavigationBarColor(Color.TRANSPARENT),150);
    }


    //multi-thread UI handler.
    @SuppressLint("SetTextI18n")
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_SYSTEM_TIME:
                    //if user is first try in Sign(no record), then set SignedDay to -1 to prevent from unavailable Signing.
                    SignedDay = SupportClass.getIntData(ListActivity.this,"TimerSettingProfile","SignDay",-1);
                    TextView ListTimerDateView = findViewById(R.id.ListTimerDateView);
                    ListTimerDateView.setText(AppYear + "-" + AppMonth + "-" + AppDay);
                    break;
                case UPDATE_TIMER:
                    ResetCounterText();
                    getWindow().setNavigationBarColor(Color.TRANSPARENT);
                    break;
                case TODO_LOAD_SUCCESS:
                    ReloadToDoList();
                    break;
                case TODO_LOAD_EMPTY:
                    ReloadToDoList();
                    ImageView DeleteToDoButton = findViewById(R.id.DeleteToDoButton);
                    ImageView EditToDoButton = findViewById(R.id.EditToDoButton);
                    DeleteToDoButton.setEnabled(false);
                    EditToDoButton.setEnabled(false);
                    break;
                default:
                    break;
            }
            return false;//don't mind it.
        }
    });

    @Override
    protected void onStop() {
        super.onStop();
        RecyclerView ToDoListView = findViewById(R.id.ToDoListView);
        ToDoListView.clearAnimation();//prevent from RAM leak.
    }


    //timer system.
    int SignedDay;//save recent sign DAY for comparison.
    int TimerProgress = 0;
    int TimerTargetDay = 0;
    int SentenceId = 0;

    //lv.2 method, main method to check Timer State.(and functions)
    private void InitializingTimerData(){
        Thread TimerLoad = new Thread(() -> {
            TimerProgress = SupportClass.getIntData(this,"TimerSettingProfile","TimerProgress", 0);
            TimerTargetDay = SupportClass.getIntData(this,"TimerSettingProfile","TimerTargetDay", 30);
            //load function.
            IsAutoDeleted = SupportClass.getBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",false);
            Message message = new Message();
            message.what = UPDATE_TIMER;
            handler.sendMessage(message);
        });
        TimerLoad.start();
    }

    //lv.3 method,main method of Timer Sign, now executed in GoToMainActivity() method.
    private void ShowTimerNotification(){
        boolean TimerFortuneState = SupportClass.getBooleanData(this,"TimerSettingProfile","TimerFortune",false);
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
            TimerReportText = TimerReportText + getString(R.string.TodayFortuneTran) + " " + Fortunes[SupportClass.CreateRandomNumber(0,4)] + "\n\n";
        }
        //2.daily sentence.
        SentenceId = SupportClass.CreateRandomNumber2(0,ValueClass.SIGN_SENTENCES_EN.length - 1);
        if(!Locale.getDefault().getCountry().equals("CN")){//if language is not in Chinese.
            TimerReportText = TimerReportText + ValueClass.SIGN_SENTENCES_EN[SentenceId];
        }else{
            TimerReportText = TimerReportText + ValueClass.SIGN_SENTENCES_CN[SentenceId];
        }
        //3.Sign and notice user.
        //condition: AppDay != SignedDay,which is prevent from repeat notification in single day, now moved to GoToMainActivity() method.
        if(TimerProgress < TimerTargetDay){
            //2.1 add Progress.
            TimerProgress = TimerProgress + 1;
            //thanks to: https://stackoverflow.com/questions/23179795/android-onclicklistener-intent-and-context !
            SupportClass.saveIntData(ListActivity.this,"TimerSettingProfile","TimerProgress",TimerProgress);
            //2.2 Change Timer UI.
            ResetCounterText();
            //2.3 Show progress in the Notification and send to user.
            String TimerUnitText = SupportClass.getStringData(this,"TimerSettingProfile","TimerUnit","Sol.");
            TimerReportText = TimerReportText + "\n" + TimerUnitText + " " + TimerProgress + " / " + TimerTargetDay;
            //2.4 send.
            ShowSignNotification(TimerReportText);
        }else{
            ShowSignNotification(getString(R.string.FinishSignGoalHintTran));
        }
        //for data stable reason, don't use multi-thread here.
        //4.save Today`s DAY to file, to check if user is Signed.
        SupportClass.saveIntData(this,"TimerSettingProfile","SignDay",AppDay);
        //4.refresh the Daily Chance data.
        SupportClass.saveIntData(this,"BattleDataProfile","DailyChance",1);
    }

    //lv.1 method, sub method of OnCreate() and ShowTimerDialog() method.
    @SuppressLint("SetTextI18n")
    private void ResetCounterText(){
        TextView ListTimerUnitView = findViewById(R.id.ListTimerUnitView);
        TextView ListTimerProgressView = findViewById(R.id.ListTimerProgressView);
        TextView ListTimerPercentView = findViewById(R.id.ListTimerPercentView);
        ProgressBar ListTimerProgress = findViewById(R.id.ListTimerProgress);
        String TimerUnitText = SupportClass.getStringData(this,"TimerSettingProfile","TimerUnit","Sol.");
        ListTimerUnitView.setText(TimerUnitText);
        ListTimerProgressView.setText(TimerProgress + " / " + TimerTargetDay);
        int Percent = SupportClass.CalculatePercent(TimerProgress,TimerTargetDay);
        ListTimerProgress.setProgress(Percent);
        ListTimerPercentView.setText(Percent + "%");
    }//end of timer system.


    //Notification system, which belongs to Timer system.
    //thanks to: https://developer.android.google.cn/training/notify-user/build-notification?hl=zh-cn#SimpleNotification !
    //lv.2 method, main method of show sign notification, thanks to: https://www.jianshu.com/p/27ef49a0db29 and https://www.jianshu.com/p/6aec3656e274!
    private void ShowSignNotification(String Content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ValueClass.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.OpenNotificationHintTran))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle(getString(R.string.SignSuccessHintTran))
                        .bigText(Content)
                )
                .setChannelId(ValueClass.CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(ValueClass.NOTIFICATION_SYSTEM_ID, builder.build());
    }

    //lv.1 method, sub method of ShowSignNotification() method, android 8.0+ compatible code.
    private void CreateNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.SignChannelWordTran);
            String description = getString(R.string.NotificationChannelHintTran);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(ValueClass.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }//end of Notification system.


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
            message.what = UPDATE_SYSTEM_TIME;
            handler.sendMessage(message);
        });
        TimeLoad.start();
    }//end of System Time Function.


    //List system.
    //database.
    Cursor ReturnCursor;
    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;
    List <String> ToDoItemList = new ArrayList<>();
    List <Integer> ToDoCheckedList = new ArrayList<>();
    //selected item record.
    String ItemSelectedText = "";
    int ItemSelectedId = -1;
    boolean ToDoChecked;
    boolean IsAutoDeleted = false;

    //lv.2 method, main method of Add a ToDoItem to RecyclerView.
    //thanks to: https://stackoverflow.com/questions/27845069/add-a-new-item-to-recyclerview-programmatically !
    public void AddToDoItem(View view){
        //0.View and Layout preparation.
        final EditText ToDoInput = new EditText(this);
        ToDoInput.setInputType(InputType.TYPE_CLASS_TEXT);
        ToDoInput.setHint(getString(R.string.AddNewTodoHintTran));
        //1.1 set basic values of dialog, including content text,button text,and title.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.AddToDoTran));
        dialog.setView(ToDoInput);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog12, id) -> {
                    String UserInput = ToDoInput.getText().toString();
                    if(!UserInput.equals("")){
                        //1.if Input is not empty, add a new line in database.
                        ContentValues values = new ContentValues();
                        values.put(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText,UserInput);
                        values.put(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_IsFinished,0);
                        dbWrite.insert(ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,null,values);
                        //2.after add, reload data.
                        RefreshToDoView();
                        //2.1 insert a new item to RecyclerView through Adapter.
                        //ReloadToDoList() included in InitializingToDoData().
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
            SupportClass.CreateNoticeDialog(this,
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
            dialog.setMessage(getString(R.string.DeleteToDoHint1Tran) + "\n\n" +
                    ItemSelectedText +
                    "\n\n" + getString(R.string.DeleteToDoHint2Tran));
            dialog.setCancelable(true);
            dialog.setPositiveButton(getString(R.string.ConfirmWordTran), (dialog1, which) -> {
                //Delete operation.
                String Selection = ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                dbWrite.delete(
                        ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,
                        Selection,
                        SelectionArgs
                );
                //after Delete.
                RefreshToDoView();
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
            SupportClass.CreateNoticeDialog(this,
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
            EditInput.setText(ItemSelectedText);//default value.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.EditToDoTitleTran));
            dialog.setMessage(getString(R.string.DeleteToDoHint1Tran) + "\n\n" +
                    ItemSelectedText +
                    "\n\n" + getString(R.string.DeleteToDoHint3Tran));
            dialog.setView(EditInput);
            dialog.setCancelable(true);
            dialog.setPositiveButton(getString(R.string.ConfirmWordTran), (dialog1, which) -> {
                //Edit operation.
                //1.prepare values which need to be changed to.
                ContentValues values = new ContentValues();
                values.put(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText,EditInput.getText().toString());
                values.put(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_IsFinished,0);
                //2.Search which TodoItem should be Edited.
                String Selection = ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                //3.update data.
                dbWrite.update(
                        ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,
                        values,
                        Selection,
                        SelectionArgs
                );
                //4.after Edit.
                RefreshToDoView();
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
                String Selection = ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
                String[] SelectionArgs = {ItemSelectedText};
                dbWrite.delete(
                        ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,
                        Selection,
                        SelectionArgs
                );
                //after Delete.
                RefreshToDoView();
                ReloadToDoList();//Reload in empty situation, DO NOT REMOVE IT!
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

    //lv.1 method, main method of load List data.And sub method of AddToDoItem() and DeleteToDoItem() method.
    //boolean QuickUI: when you need to update UI quickly, it will not pass message to handler, instead of execute UI reload in main thread immediately.
    private void InitializingToDoData(){
        Thread TodoLoad = new Thread(() -> {
            //0.preparation.
            Message message = new Message();
            //1.get database object.
            ToDoDataBaseBasic DBHelper = new ToDoDataBaseBasic(this);
            dbRead = DBHelper.getReadableDatabase();
            dbWrite = DBHelper.getWritableDatabase();
            ReturnCursor = dbRead.query(
                    ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,
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
                int ToDoTextColumnId = ReturnCursor.getColumnIndex(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText);
                int ToDoStateColumnId = ReturnCursor.getColumnIndex(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_IsFinished);
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

    //lv.1 method, main method of fill RecyclerView with List.
    private void ReloadToDoList(){
        //1. prepare Adapter, which used in put data into recyclerview.
        //this part like Cursor in database IO operation.(following these steps: list~Adapter~RecyclerView)
        //1.1 Put list data in Adapter.
        LetterAdapter mLetterAdapter = new LetterAdapter(ToDoItemList);
        //2. prepare RecyclerView object.
        RecyclerView ToDoListView = findViewById(R.id.ToDoListView);
        //2.1 Put Adapter to RecyclerView.
        ToDoListView.setAdapter(mLetterAdapter);
        //2.2 set Manager and it`s Working Mode to RecyclerView, which used to manage and recycle data, just like a controller of RecyclerView.
        ToDoListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //2.3 enable system default animation for entire RecyclerView.
        ToDoListView.setAnimation(new AlphaAnimation(1.0f, 0.2f));
        //2.4 after import Content(Adapter) and Controller(Manager), finished initializing RecyclerView.
        //2.5 refresh load ToDoItem number to user.
        TextView ToDoItemNumberView = findViewById(R.id.ToDoItemNumberView);
        ToDoItemNumberView.setText(String.valueOf(ToDoItemList.size()));
    }

    //RecyclerView support code, thanks to: https://www.cnblogs.com/rustfisher/p/12254732.html !
    //animation: https://www.jianshu.com/p/4f9591291365 !
    //TextView line effect: https://blog.csdn.net/zhuzhiqiang_zhu/article/details/50755980 !
    //lv.2 Class, Adapter, used in load data from ViewHolder and put data into RecyclerView. Sub Class of RecyclerView. Used in ReloadToDoList() method.
    private class LetterAdapter extends RecyclerView.Adapter<VH> {

        private final List<String> dataList;//store DataList in this class only.

        public LetterAdapter(List<String> dataList) { this.dataList = dataList; }//initializing the class code.(import variable) just like the OnCreate() method in a Activity.

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//load the item appearance xml file of RecyclerView.
            return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_list, parent, false));
        }

        @Override
        @SuppressLint("RecyclerView")
        public void onBindViewHolder(@NonNull VH holder, int position) {//bind (combine) data and control logics with [each] item in RecyclerView.
            //1.initializing state of ToDoItems.
            final String NameString = dataList.get(position);//load data needed to be shown from dataList.
            holder.ToDoItem.setText(NameString);//set TextView`s text in the item.
            holder.ToDoItem.setChecked(ToDoCheckedList.get(position) == 1);//if Checked State in List is 1, check the CheckBox, or uncheck it when is 0.
            //2.set OnClickListener on each ToDoItem.
            holder.ToDoItem.setOnClickListener(v -> {//2.1 do something after Clicked.
                //2.2 report which Item is Clicked.
                ItemSelectedText = NameString;
                ItemSelectedId = position;
                //2.3 Branch Action.
                //2.3.1 if in Delete Mode, the Check State will not be changed, instead of only Recording Item`s information.
                //the Mode will be changed in DeleteToDoItem() method.
                //2.3.2 change Item Checked state when Item is Clicked.
                ToDoChecked = holder.ToDoItem.isChecked();
                //2.3.3 change Item Checked state in database.
                ChangeToDoState();
                if(IsDeleteMode){
                    DeleteToDoItem(null);
                }
                if(IsEditMode){
                    EditToDoItem(null);
                }
                //2.4 if AutoDelete function is ON(Delete Item when it has been checked.), execute delete process.
                ToDoAutoDelete();
            });
        }

        @Override
        public int getItemCount() { return dataList.size(); }//get item number, it actually useless for me, but according the rule, I have to override it.

    }

    //lv.1 Class, ViewHolder, sub Class of onCreateViewHolder() method in [Adapter] Class.
    //control the content of each item in RecyclerView.
    private static class VH extends RecyclerView.ViewHolder {
        View item; // represent entire item object.(layout)
        CheckBox ToDoItem;

        public VH(@NonNull View itemView) {//ViewHolder Constructor definition.
            super(itemView);//declare import method from Father Class.
            item = itemView;//store item in RecyclerView to be used by another method in different Class.
            ToDoItem = itemView.findViewById(R.id.ToDoShowView);//import item xml from project file.
        }
    }

    //lv.1 method, sub method of onBindViewHolder() method in LetterAdapter Class.
    private void ChangeToDoState(){
        //1.preparation.
        //1.1 Search entire database with ToDoItemText.
        String Selection = ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_TodoText + " = ?";
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
        ToDoItemValues.put(ToDoDataBaseBasic.DataBaseEntry.COLUMN_NAME_IsFinished,StateCode);
        //4.update Checked State in database.
        dbWrite.update(
                ToDoDataBaseBasic.DataBaseEntry.TABLE_NAME,
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
        AutoDeleteSwitch.setChecked(SupportClass.getBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",false));
        //1.dialog to show to user.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.SettingTitleTran));
        dialog.setView(AutoDeleteSwitch);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                getString(R.string.ConfirmWordTran),
                (dialog1, id) -> {
                    IsAutoDeleted = AutoDeleteSwitch.isChecked();
                    SupportClass.saveBooleanData(this,"TimerSettingProfile","ToDoAutoDelete",IsAutoDeleted);
                    dialog1.cancel();
                });
        dialog.setNegativeButton(
                getString(R.string.CancelWordTran),
                (dialog12, id) -> dialog12.cancel());
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }

    public void ShowListSettingHelp(View view){
        SupportClass.CreateNoticeDialog(this,
                getString(R.string.HelpWordTran),
                getString(R.string.AutoDeleteTodoHintTran),
                getString(R.string.ConfirmWordTran)
        );
    }//end of List Setting Function.
    //end of List system.


    //Timer Layout Management. and sub method of InitializingTimerData() method.
    public void CloseTimerLayout(View view){
        ConstraintLayout ListTimerLayout = findViewById(R.id.ListTimerLayout);
        ListTimerLayout.setVisibility(View.GONE);
    }

    //Floating Button: Go to MainActivity.
    public void GoToMainActivity(View view){
        if(SupportClass.getBooleanData(ListActivity.this, "TimerSettingProfile", "TimerEnabled", false)
                && AppDay != SignedDay//Day check.
        ){
            CreateNotificationChannel();
            ShowTimerNotification();
            //finish signed.
        }
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}