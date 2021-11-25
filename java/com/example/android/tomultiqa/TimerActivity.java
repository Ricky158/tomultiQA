package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class TimerActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        CloseAllLayout();
        //calculating UserAnsweringRecord and GatherPointSpeed.
        TextView GatherSpeedView = findViewById(R.id.GatherSpeedView);
        TextView TotalPointView = findViewById(R.id.TotalPointView);
        Switch TimerShowSwitch = findViewById(R.id.TimerShowSwitch);
        int RightAnswering = SupportLib.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportLib.getIntData(this,"RecordDataFile","WrongAnswering",0);
        IsShowTimeNumber = SupportLib.getBooleanData(this,"TimerSettingProfile","ChronometerShowCount",true);
        GatherPointSpeed = 0.1 + (RightAnswering + WrongAnswering) / 800.0;
        //initializing ResourceIO Instance.
        resourceIO = new ResourceIO(this);

        GatherSpeedView.setText(SupportLib.ReturnTwoBitText(GatherPointSpeed));
        TotalPointView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
        TimerShowSwitch.setChecked(IsShowTimeNumber);
        //ChronometerTickListener.
        final ProgressBar SquareTimerProgress = findViewById(R.id.SquareTimerProgress);
        final TextView GatherNumberView = findViewById(R.id.GatherNumberView);
        final Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        //timer support.thanks to:
        // https://www.twle.cn/l/yufei/android/android-basic-chronometer.html !
        // https://stackoverflow.com/questions/526524/android-get-time-of-chronometer-widget !
        SquareTimer.setOnChronometerTickListener(chronometer -> {
            if(IsTimerStarted){
                //1.Timer add 1 sec.
                AddTimerCount();
                //2.set ProgressBar value.the value is working time percentage.and show to user.
                    SquareTimerProgress.setProgress(SupportLib.CalculatePercent(WorkDuration, WorkDuration + RestDuration));
            }
            //3.if user is[Working], give user Point Reward.
            if(IsWorking && IsTimerStarted){
                GatheredNumber = GatheredNumber + GatherPointSpeed;
                GatherNumberView.setText(SupportLib.ReturnTwoBitText(GatheredNumber));
            }
        });
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_activity_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_TimerHistory){//if setting icon in Menu be touched.
            ShowHistory();
        }
        return super.onOptionsItemSelected(item);
    }//end of ActionBar Menu.


    //Background Timer.
    /**
     * If TimerActivity has been put into background, then this variable will be filled with the Time Stamp of user leave Activity.<br/>
     * then, when user back to this Activity. You can use <code>BackTimeEnd</code> minus <code>BackTimeStart</code> to get the background time duration.<br/>
     * Value: If the value equal to -1, it means user never put Activity to background, then no record.<br/>
     * Notice: The unit of <code>BackTime</code> is <code>Second</code>.
     */
    long BackTimeStart = -1;
    long BackTimeEnd = -1;

    //when user put this Activity to background.
    @Override
    protected void onPause() {
        super.onPause();
        BackTimeStart = System.currentTimeMillis() / 1000;
    }

    //when user pull this Activity from background.
    @Override
    protected void onResume() {
        super.onResume();
        BackTimeEnd = System.currentTimeMillis() / 1000;
        //write time number to foreground timer.
        int BackDuration = (int) (BackTimeEnd - BackTimeStart);
        if(IsTimerStarted && IsWorking){
            WorkDuration = WorkDuration + BackDuration;
            ReloadTimerUI(WorkDuration);
        }else if(IsTimerStarted){//REST state of Timer.
            RestDuration = RestDuration + BackDuration;
            ReloadTimerUI(RestDuration);
        }
    }//end of Background Timer.

    //History Function.
    StringBuilder History = new StringBuilder();//prevent from empty error, the accumulate record for Timer.
    String ThisRecord = "";//prevent from empty error, record for this time Activity access.

    @Override
    protected void onDestroy() {//not using onStop() method is that if App be placed between front and background multi-time, the method will execute multi-time.
        super.onDestroy();
        if(IsWorking){
            ResetTimer(null);
        }
        //1.update and save history.(Recording in ResetTimer() method.)
        Thread thread = new Thread(() -> {
            String UpdatedHistory = ThisRecord + History.toString();
            if(UpdatedHistory.length() > 50000){
                UpdatedHistory = "";//if History is too long, system will reset it to default value, to save storage.
            }
            SupportLib.saveStringData(TimerActivity.this,"TimerSettingProfile","TimerHistory", UpdatedHistory);
        });
        thread.start();
    }

    private void ShowHistory(){
        //it can't be placed in another thread.
        //1.import String to Buffer to reverse it to show to user.(initialize)
        History = new StringBuilder(SupportLib.getStringData(this,"TimerSettingProfile","TimerHistory","") );//default value.
        //2.do process.
        if(History.length() > 40000){//hint user.
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.ErrorWordTran),
                    "The History is too long for loading, please delete or backup it manually, or App will do these soon.",
                    getString(R.string.ConfirmWordTran));
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.TimerHistoryTitleTran));
        dialog.setMessage(History.toString());
        dialog.setPositiveButton(
                getString(R.string.CloseWordTran),
                (dialog1, which) -> dialog1.cancel()
        );
        dialog.setNeutralButton(
                getString(R.string.DeleteWordTran),
                (dialog13, which) -> {
                    Thread thread = new Thread(() -> {
                        History = new StringBuilder();
                        ThisRecord = "";
                        SupportLib.saveStringData(TimerActivity.this,"TimerSettingProfile","TimerHistory","");//default value.
                    });
                    thread.start();
                    dialog13.cancel();
                }
        );
        dialog.setNegativeButton(
                getString(R.string.CopyWordTran),
                (dialog12, which) -> {
                    //paste note to clipboard.
                    //thanks to: https://blog.csdn.net/asdf717/article/details/52678009 !
                    // https://stackoverflow.com/questions/33207809/what-exactly-is-label-parameter-in-clipdata-in-android !
                    // https://blog.csdn.net/zhuifengshenku/article/details/19080227 !
                    ClipboardManager mCM = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("TimerHistory from tomultiQA", History);
                    mCM.setPrimaryClip(mClipData);
                    dialog12.cancel();
                }
        );
        AlertDialog DialogView = dialog.create();
        DialogView.show();
    }//end of History Function.(not included ResetTimer() method.)


    //Timer function.
    /**
     * Record Timer is Initialized or not.<br/>
     * When the Timer is Counting (no matter in WORK or REST state), it will be <code>true</code>.<br/>
     * If it is <code>false</code>, that means Timer isn't start counting at all.
     */
    boolean IsTimerStarted = false;
    /**
     * Value:<br/>
     * <code>true</code>: Timer is in WORK state.<br/>
     * <code>false</code>: Timer is in REST state.<br/>
     * Notice: This variable can't show Timer is ON or OFF.
     */
    boolean IsWorking = false;
    boolean IsShowTimeNumber = true;
    //WORK state secs.
    int WorkDuration = 0;
    //REST state secs.
    int RestDuration = 0;
    double GatherPointSpeed = 0.00;
    double GatheredNumber = 0.00;

    ResourceIO resourceIO;

    //lv.3 method, main method.
    public void ResetTimer(View view){
        //1.report user last time record, add to history.
        //"+ "\n----\n" + CurrentDate + "\n" +" String is spilt line of each Record.
        String Report =
                getString(R.string.TimerTotalTimeWordTran) + "\n" +
                SupportLib.ReturnTimerString(WorkDuration + RestDuration) + "\n" +
                getString(R.string.TimerWorkingTran) + " " +  SupportLib.CalculatePercent(WorkDuration, WorkDuration + RestDuration) + "%\n" +
                SupportLib.ReturnTimerString(WorkDuration) + "\n" +
                getString(R.string.TimerRestingTran) + " " + SupportLib.CalculatePercent(RestDuration, WorkDuration + RestDuration) + "%\n" +
                SupportLib.ReturnTimerString(RestDuration);
        if(WorkDuration + RestDuration > 0){//we can't record empty History.
            ThisRecord = SupportLib.getSystemTime() + "\n" + Report + "\n----\n" + ThisRecord;
        }
        //2.show to user with format.
        SupportLib.CreateNoticeDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.TimerResetedReportTran) + "\n" + Report,
                getString(R.string.ConfirmWordTran)
        );
        //2. clear last time record and stop.
        ClearTimer();
        //Switch ON is [Working], OFF is [Resting].
        //But considering the IsWorking variable will be changed in ChangeTimerState() method, so we need to set to opposite value manually.
    }

    //lv.2 method, main method.
    @SuppressLint("SetTextI18n")
    public void ChangeTimerState(View view){
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        TextView WorkStateView = findViewById(R.id.WorkStateView);
        //change button image by code.thanks to:
        //https://stackoverflow.com/questions/35121147/change-android-drawable-button-icon-programmatically !
        //https://stackoverflow.com/questions/30455765/change-image-floating-action-button-android !
        FloatingActionButton SquareTimerStartButton = findViewById(R.id.SquareTimerStartButton);
        Switch StartWithSwitch = findViewById(R.id.StartWithSwitch);
        //0.if Timer is not initialized, start it.
        if(!IsTimerStarted){
            StartTimer();
            IsWorking = !StartWithSwitch.isChecked();
            //Switch ON is [Working], OFF is [Resting].
            //But considering the IsWorking variable will be changed in next code, so we need to set to opposite value manually.
        }
        //1.change Timer State. (after entered Activity, timer is off by default)
        if(IsWorking){
            WorkStateView.setText(R.string.TimerRestingTran);
            SquareTimerStartButton.setImageResource(android.R.drawable.ic_media_play);
        }else{
            SquareTimer.start();
            WorkStateView.setText(R.string.TimerWorkingTran);
            SquareTimerStartButton.setImageResource(android.R.drawable.ic_media_pause);
        }
        //change state record which timer has been Changed.
        IsWorking = !IsWorking;
    }

    //thanks to: https://blog.csdn.net/weixin_43564923/article/details/94550630 !
    // https://stackoverflow.com/questions/22545644/how-to-convert-seconds-into-hhmmss !
    /**
     * lv.1 main method, which can add 1 sec to Counting Number depending which Mode Timer is using.
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void AddTimerCount(){
        TextView TimerShowView = findViewById(R.id.TimerShowView);
        //0.the first sec will not be loaded automatically.we need to update text manually.
        if(WorkDuration == 1 && IsWorking || RestDuration == 1 && !IsWorking){
            TimerShowView.setText("00:00:01");
        }
        //1.add 1 sec to total Time number.
        if(IsWorking){
            WorkDuration = WorkDuration + 1;
            //2.transform time format.and show to user.
            ReloadTimerUI(WorkDuration);
        }else {
            RestDuration = RestDuration + 1;
            //2.transform time format.and show to user.
            ReloadTimerUI(RestDuration);
        }
    }

    /**
     * Transform Sec number to Time format, and show the Time Number to user (refresh UI).
     * @param ShowedNumber Because of Timer have two mode Counting Number, you can put which you want to show here.
     */
    @SuppressLint("SetTextI18n")
    private void ReloadTimerUI(int ShowedNumber){
        TextView TimerShowView = findViewById(R.id.TimerShowView);
        if(IsShowTimeNumber){
            TimerShowView.setText(SupportLib.ReturnTimerString(ShowedNumber));
        }else{
            TimerShowView.setText("00:00:00");
        }
    }

    @SuppressLint("SetTextI18n")
    public void SetSwitchText(View view){
        Switch StartWithSwitch = findViewById(R.id.StartWithSwitch);
        if(StartWithSwitch.isChecked()){
            StartWithSwitch.setText(getString(R.string.TimerStartWithTran) + getString(R.string.TimerWorkingTran));
        }else{
            StartWithSwitch.setText(getString(R.string.TimerStartWithTran) + getString(R.string.TimerRestingTran));
        }
    }

    public void ChangeShowTimeState(View view){
        Switch TimerShowSwitch = findViewById(R.id.TimerShowSwitch);
        IsShowTimeNumber = TimerShowSwitch.isChecked();
        SupportLib.saveBooleanData(this,"TimerSettingProfile","ChronometerShowCount",IsShowTimeNumber);
    }

    //lv.1 method, sub method of ChangeTimerState() method.
    private void StartTimer(){
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        IsTimerStarted = true;
        IsWorking = true;
        SquareTimer.start();
    }

    /**
     * lv.1 method, sub method of <code>ResetTimer()</code> method.<br/>
     * Call this method to let TimerActivity UI back to Initial State.
     */
    @SuppressLint("SetTextI18n")
    private void ClearTimer(){
        //1.stop progress immediately.
        IsTimerStarted = false;
        //2.preparation.
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        Switch StartWithSwitch = findViewById(R.id.StartWithSwitch);
        FloatingActionButton SquareTimerStartButton = findViewById(R.id.SquareTimerStartButton);
        TextView TimerShowView = findViewById(R.id.TimerShowView);
        TextView WorkStateView = findViewById(R.id.WorkStateView);
        //3.reset UI.
        TimerShowView.setText("00:00:00");
        WorkStateView.setText(getString(R.string.TimerReadyTran));
        SquareTimerStartButton.setImageResource(android.R.drawable.ic_media_play);
        ProgressBar SquareTimerProgress = findViewById(R.id.SquareTimerProgress);
        SquareTimerProgress.setProgress(0);
        //4.reset Chronometer counting.
        SquareTimer.setBase(SystemClock.elapsedRealtime());
        SquareTimer.stop();
        //5.clear counting variables.
        WorkDuration = 0;
        RestDuration = 0;
        IsWorking = !StartWithSwitch.isChecked();
    }//end of Timer Function.


    //Timer Reward function.
    @SuppressLint("SetTextI18n")
    public void CollectReward(View view) {
        TextView GatherNumberView = findViewById(R.id.GatherNumberView);
        TextView TotalPointView = findViewById(R.id.TotalPointView);

        //thanks to: https://blog.csdn.net/geniushorse/article/details/83182241 !
        int CanBeGathered;

        if (GatheredNumber > 1) {
            CanBeGathered = (int) (Math.floor(GatheredNumber));
            GatheredNumber = GatheredNumber - CanBeGathered;
            GatherNumberView.setText(SupportLib.ReturnTwoBitText(GatheredNumber));

            resourceIO.GetPoint(CanBeGathered);
            resourceIO.ApplyChanges(this);
            TotalPointView.setText(SupportLib.ReturnKiloIntString(resourceIO.UserPoint));
            SupportLib.CreateNoticeDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.GetWordTran) + CanBeGathered + getString(R.string.PointWordTran),
                    getString(R.string.ConfirmWordTran)
            );
        }
    }

    public void OpenRewardLayout(View view){
        ConstraintLayout TimerRewardLayout = findViewById(R.id.TimerRewardLayout);
        if(TimerRewardLayout.getVisibility() == View.GONE){
            TimerRewardLayout.setVisibility(View.VISIBLE);
        }else{
            TimerRewardLayout.setVisibility(View.GONE);
        }
    }

    private void CloseAllLayout(){
        ConstraintLayout TimerRewardLayout = findViewById(R.id.TimerRewardLayout);
        TimerRewardLayout.setVisibility(View.GONE);
    }//end of Timer Reward function.
}