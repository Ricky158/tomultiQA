package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
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
        TotalPointView.setText(UserPoint + "");

        final ProgressBar SquareTimerProgress = findViewById(R.id.SquareTimerProgress);
        final TextView GatherNumberView = findViewById(R.id.GatherNumberView);
        //timer support.thanks to:
        // https://www.twle.cn/l/yufei/android/android-basic-chronometer.html !
        // https://stackoverflow.com/questions/526524/android-get-time-of-chronometer-widget !
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        SquareTimer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if(IsTimerOn){
                    if(SquareTimerProgress.getProgress() < 9){
                        SquareTimerProgress.setProgress(SquareTimerProgress.getProgress() + 1);
                    }else{
                        SquareTimerProgress.setProgress(0);
                    }
                    GatheredNumber = GatheredNumber + GatherPointSpeed;
                    GatherNumberView.setText(SupportClass.ReturnTwoBitText(GatheredNumber) + "");
                }
            }
        });
    }

    //recording which Timer is on or off.
    boolean IsTimerOn = false;
    int TotalUserAnswering = 0;
    double GatherPointSpeed = 0.00;
    double GatheredNumber = 0.00;

    int UserPoint = 0;
    long PointRecord = 0;

    public void ResetTimer(View view){
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        SquareTimer.setBase(SystemClock.elapsedRealtime());
        SquareTimer.start();
    }

    public void ChangeTimerState(View view){
        Chronometer SquareTimer = findViewById(R.id.SquareTimerView);
        TextView WorkStateView = findViewById(R.id.WorkStateView);
        //change button image by code.thanks to:
        //https://stackoverflow.com/questions/35121147/change-android-drawable-button-icon-programmatically !
        //https://stackoverflow.com/questions/30455765/change-image-floating-action-button-android !
        FloatingActionButton SquareTimerStartButton = findViewById(R.id.SquareTimerStartButton);

        //after entered Activity, timer is off by default.
        if(IsTimerOn){
            SquareTimer.stop();
            WorkStateView.setText(R.string.TimerRestingTran);
            SquareTimerStartButton.setImageResource(android.R.drawable.ic_media_play);

        }else{
            SquareTimer.start();
            WorkStateView.setText(R.string.TimerWorkingTran);
            SquareTimerStartButton.setImageResource(android.R.drawable.ic_media_pause);
        }
        //change state record which timer has been Changed.
        IsTimerOn = !IsTimerOn;
    }

    @SuppressLint("SetTextI18n")
    public void CollectReward(View view) {
        TextView GatherNumberView = findViewById(R.id.GatherNumberView);
        TextView TotalPointView = findViewById(R.id.TotalPointView);

        //thanks to: https://blog.csdn.net/geniushorse/article/details/83182241 !
        int CanBeGathered = 0;

        if (GatheredNumber > 1) {
            CanBeGathered = (int) (Math.floor(GatheredNumber));
            GatheredNumber = GatheredNumber - CanBeGathered;
            GatherNumberView.setText(GatheredNumber + "");

            UserPoint = UserPoint + CanBeGathered;
            TotalPointView.setText(UserPoint + "");

            PointRecord = PointRecord + CanBeGathered;
            SupportClass.saveLongData(this, "RecordDataFile", "PointGotten", PointRecord);
            SupportClass.saveIntData(this, "BattleDataProfile", "UserPoint", UserPoint);
        }
        SupportClass.CreateOnlyTextDialog(this,
                getString(R.string.NoticeWordTran),
                "Get " + CanBeGathered + " Point as Reward.",
                getString(R.string.ConfirmWordTran),
                "Nothing",
                true);
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
    }
}