package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TimerActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //make StatusBar is same color as ActionBar in Activity.
        //thanks to: https://blog.csdn.net/kiven9609/article/details/73162307 !
        // https://www.color-hex.com/color/26c6da ! #26C6DA is our App Primary Color.
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(38,198,218));
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        //main method below.
        CloseAllLayout();

        //calculating UserAnsweringRecord and GatherPointSpeed.
        TextView GatherSpeedView = findViewById(R.id.GatherSpeedView);
        TextView TotalPointView = findViewById(R.id.TotalPointView);
        int RightAnswering = SupportClass.getIntData(this,"RecordDataFile","RightAnswering",0);
        int WrongAnswering = SupportClass.getIntData(this,"RecordDataFile","WrongAnswering",0);

        TotalUserAnswering = RightAnswering + WrongAnswering;
        GatherPointSpeed = 0.1 + TotalUserAnswering / (float) 800;
        UserPoint = SupportClass.getIntData(this,"BattleDataProfile","UserPoint",0);

        GatherSpeedView.setText(SupportClass.ReturnTwoBitText(GatherPointSpeed) + "");
        TotalPointView.setText(SupportClass.ReturnKiloIntString(UserPoint) + "");

        final ProgressBar SquareTimerProgress = findViewById(R.id.SquareTimerProgress);
        final TextView GatherNumberView = findViewById(R.id.GatherNumberView);
        final Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        //timer support.thanks to:
        // https://www.twle.cn/l/yufei/android/android-basic-chronometer.html !
        // https://stackoverflow.com/questions/526524/android-get-time-of-chronometer-widget !
        SquareTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(IsTimerStarted){
                    //1.Timer add 1 sec.
                    AddTimerCount();
                    //2.set ProgressBar value.the value is working time percentage.and show to user.
                        SquareTimerProgress.setProgress(SupportClass.CalculatePercent(TimerCountNumber,TimerCountNumber + TimerStopTime));
                }
                //3.if user is[Working], give user Point Reward.
                if(IsWorking && IsTimerStarted){
                    GatheredNumber = GatheredNumber + GatherPointSpeed;
                    GatherNumberView.setText(SupportClass.ReturnTwoBitText(GatheredNumber) + "");
                }
            }
        });
    }

    //Timer function.
    //recording which Timer is on or off.
    boolean IsTimerStarted = false;//record Timer is Initialized or not.
    boolean IsWorking = false;
    int TimerCountNumber = 0;//the time count is start 1 sec later than Timer, so we put 1 here.
    int TimerStopTime = 0;
    int TotalUserAnswering = 0;
    double GatherPointSpeed = 0.00;
    double GatheredNumber = 0.00;

    int UserPoint = 0;
    long PointRecord = 0;

    //lv.3 method, main method.
    public void ResetTimer(View view){
        //1.report user last time record.
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.ReportWordTran),
                getString(R.string.TimerResetedReportTran) + "\n" +
                getString(R.string.TimerTotalTimeWordTran) + "\n" +
                        SupportClass.ReturnTimerString(TimerCountNumber + TimerStopTime) + "\n" +
                        getString(R.string.TimerWorkingTran) + " " +  SupportClass.CalculatePercent(TimerCountNumber,TimerCountNumber + TimerStopTime) + "%" + "\n" +
                SupportClass.ReturnTimerString(TimerCountNumber) + "\n" +
                        getString(R.string.TimerRestingTran) + " " + SupportClass.CalculatePercent(TimerStopTime,TimerCountNumber + TimerStopTime) + "%" + "\n" +
                        SupportClass.ReturnTimerString(TimerStopTime),
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true
                );
        //2. clear last time record and reset.
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        Switch StartWithSwitch = findViewById(R.id.StartWithSwitch);
        SquareTimer.setBase(SystemClock.elapsedRealtime());
        SquareTimer.start();
        TimerCountNumber = 0;
        TimerStopTime = 0;
        IsWorking = !StartWithSwitch.isChecked();
        //Switch ON is [Working], OFF is [Resting].
        //But considering the IsWorking variable will be changed in ChangeTimerState() method, so we need to set to opposite value manually.
        ChangeTimerState(StartWithSwitch);
    }

    //lv.2 method, main method.
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

    //lv.1 method, main method.
    //thanks to: https://blog.csdn.net/weixin_43564923/article/details/94550630 !
    // https://stackoverflow.com/questions/22545644/how-to-convert-seconds-into-hhmmss !
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void AddTimerCount(){
        final TextView TimerShowView = findViewById(R.id.TimerShowView);
        //0.the first sec will not be loaded automatically.we need to update text manually.
        if(TimerCountNumber == 1 && IsWorking || TimerStopTime == 1 && !IsWorking){
            TimerShowView.setText("00:00:01");
        }
        //1.add 1 sec to total Time number.
        if(IsWorking){
            TimerCountNumber = TimerCountNumber + 1;
            // 2.transform time format.and show to user.
            TimerShowView.setText(SupportClass.ReturnTimerString(TimerCountNumber));
        }else {
            TimerStopTime = TimerStopTime + 1;
            // 2.transform time format.
            TimerShowView.setText(SupportClass.ReturnTimerString(TimerStopTime));
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

    //lv.1 method, sub method of ChangeTimerState() method.
    private void StartTimer(){
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        IsTimerStarted = true;
        IsWorking = true;
        SquareTimer.start();
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
            GatherNumberView.setText(SupportClass.ReturnTwoBitText(GatheredNumber) + "");

            UserPoint = UserPoint + CanBeGathered;
            TotalPointView.setText(SupportClass.ReturnKiloIntString(UserPoint) + "");

            PointRecord = PointRecord + CanBeGathered;
            SupportClass.saveLongData(this, "RecordDataFile", "PointGotten", PointRecord);
            SupportClass.saveIntData(this, "BattleDataProfile", "UserPoint", UserPoint);

            SupportClass.CreateOnlyTextDialog(this,
                    getString(R.string.NoticeWordTran),
                    getString(R.string.GetWordTran) + CanBeGathered + getString(R.string.PointWordTran),
                    getString(R.string.ConfirmWordTran),
                    "Nothing",
                    true);
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