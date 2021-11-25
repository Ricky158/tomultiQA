package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        //thanks to: https://dev.mi.com/console/doc/detail?pId=2229 !
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        SetLanguageHint();

        final SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        PageMoveBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                LoadStoryText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                LoadStoryText();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                CurrentStoryNumber = PageMoveBar.getProgress() + 1;
                LoadStoryText();
            }
        });

        //initializing SeekBar.
        PageMoveBar.setMax(StoryLib.MainStoryArray.length - 1);
    }

    //add a button menu to ActionBar in MainActivity.
    //thanks to: https://stackoverflow.com/questions/17425742/how-to-add-button-in-actionbarandroid !
    // https://blog.csdn.net/oxiaoxio/article/details/48900763 !
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.story_activity_button, menu);
        return true;
    }

    //LayoutInflater: https://blog.csdn.net/mrkyee/article/details/108094318 and https://blog.csdn.net/harvic880925/article/details/49272285 !
    //Animation: https://blog.csdn.net/shibin1990_/article/details/51603910 and https://www.jianshu.com/p/59c1e8df8735 !
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_StoryPlay && !IsWindowMode) {//if not set !IsWindowMode, then the method will create multi instance.
            //1.create object from xml file.
            ConstraintLayout StoryBaseLayout = findViewById(R.id.StoryBaseLayout);
            @SuppressLint("InflateParams") //the root view of window will be set in PopupWindow.showAtLocation().
            View Window = LayoutInflater.from(this).inflate(R.layout.story_window,null);
            PopupWindow StoryBoard = new PopupWindow(Window, StoryBaseLayout.getWidth() - 60, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView StoryTextView = Window.findViewById(R.id.StoryTextView);
            ImageView StoryNextPin = Window.findViewById(R.id.StoryNextPin);
            ImageView StorySkipButton = Window.findViewById(R.id.StorySkipButton);
            ImageView StoryBackButton = Window.findViewById(R.id.StoryBackButton);
            //2.animation to StoryText in Closing Window.
            AlphaAnimation AlphaStartTran = new AlphaAnimation(0.1f,0.9f);
            AlphaStartTran.setDuration(500);
            StoryTextView.setAnimation(AlphaStartTran);
            StorySkipButton.setAnimation(AlphaStartTran);
            StoryBackButton.setAnimation(AlphaStartTran);
            //3.animation to UI pin. (the triangle)
            TranslateAnimation SizeTran = new TranslateAnimation(0f,0f,0f,12f);//move from the left|top corner of view's (0,0) to (0,12).
            SizeTran.setDuration(1000);
            SizeTran.setRepeatMode(TranslateAnimation.REVERSE);
            SizeTran.setRepeatCount(TranslateAnimation.INFINITE);
            StoryNextPin.setAnimation(SizeTran);
            //4.fill content.
            int ReadMaxLine = EachLineStory.length;
            //4.1 set OnClick method.
            StoryTextView.setOnClickListener(view -> {
                if(ReadProcess < ReadMaxLine){//still has text not read.
                    StoryTextView.setText(EachLineStory[ReadProcess]);//load the next line of Story text.
                    ReadProcess = ReadProcess + 1;
                }else{//all text in this chapter are read.

                    //animation for StoryText in Closing Window.
                    AlphaAnimation AlphaEndTran = new AlphaAnimation(0.9f,0.1f);
                    AlphaEndTran.setDuration(400);
                    StoryTextView.setAnimation(AlphaEndTran);
                    //when the Animation are executed does.
                    AlphaEndTran.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //I don't need this part but I have to override it.
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {//close the Window.
                            IsWindowMode = false;
                            //reset to initial value.
                            ReadProcess = 0;
                            StoryBoard.dismiss();//close the Board.
                            //close the animation to prevent from RAM leak.
                            StoryTextView.clearAnimation();
                            StorySkipButton.clearAnimation();
                            StoryBackButton.clearAnimation();
                            StoryNextPin.clearAnimation();
                            ControlReader(View.VISIBLE);//open the Reader.
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            //I don't need this part but I have to override it.
                        }
                    });
                }
            });
            StorySkipButton.setOnClickListener(view -> {//close the Window.
                IsWindowMode = false;
                //reset to initial value.
                ReadProcess = 0;
                StoryBoard.dismiss();//close the Board.
                //close the animation to prevent from RAM leak.
                StoryTextView.clearAnimation();
                StorySkipButton.clearAnimation();
                StoryNextPin.clearAnimation();
                ControlReader(View.VISIBLE);//open the Reader.
            });
            StoryBackButton.setOnClickListener(v -> {
                StringBuilder ReadStory = new StringBuilder();
                int LoadProcess = 0;
                while (LoadProcess < ReadProcess){
                    ReadStory.append(EachLineStory[LoadProcess]).append("\n\n");
                    LoadProcess = LoadProcess + 1;
                }
                SupportLib.CreateNoticeDialog(this,
                        getString(R.string.ReadHistoryWordTran),
                        ReadStory.toString(),
                        getString(R.string.ConfirmWordTran));
            });
            //4.2 if the reader still displaying, then close it to clear the screen.
            IsWindowMode = true;
            ControlReader(View.GONE);
            //5.show to user.
            StoryBoard.showAtLocation(StoryBaseLayout, Gravity.BOTTOM,0,100);//show the window above the screen's bottom, and lift it up 80px.
        }
        return super.onOptionsItemSelected(item);
    }


    //story system.
    //Story Window variables.
    /**
     * If [IsWindowMode] is true, means the Story text are displayed in the Window.<br/>
     * In this situation, the reader UI can't be visual, or user's screen will be blocked by Reader's text.
      */
    boolean IsWindowMode = false;
    /**
     * the Read Process of Window. (not Reader!), if the Window is ON and user select another chapter to display.<br/>
     * then this number will be reset to 0, to help user to read new chapter from first line.
     */
    int ReadProcess = 0;
    /**
     * the text Array for display by each line in Story Window.
     */
    String[] EachLineStory = new String[]{};
    //end of Story Window variables.
    /**
     * 0 means the "Main." Story.<br/>
     * 1 means the "Dev." Story.<br/>
     * 2 means the "Tale." Story.<br/>
     */
    int StoryType = 0;
    //limit number of story pages.
    int StoryMaxNumber = StoryLib.MainStoryArray.length;//default value.
    int CurrentStoryNumber = 1;

    public void ChangeStoryType(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        RadioGroup StoryTypeChoose = findViewById(R.id.StoryTypeChoose);
        RadioButton MainStoryChoose = findViewById(R.id.MainStoryChoose);
        RadioButton DevelopStoryChoose = findViewById(R.id.DevelopStoryChoose);
        RadioButton TaleStoryChoose = findViewById(R.id.TaleStoryChoose);

        int ModeCheckedId;
        ModeCheckedId = StoryTypeChoose.getCheckedRadioButtonId();
        if (ModeCheckedId == MainStoryChoose.getId()){
            StoryType = 0;
            StoryMaxNumber = StoryLib.MainStoryArray.length;
        }else if(ModeCheckedId == DevelopStoryChoose.getId()){
            StoryType = 1;
            StoryMaxNumber = StoryLib.DevStoryArray.length;
        }else if(ModeCheckedId == TaleStoryChoose.getId()){
            StoryType = 2;
            StoryMaxNumber = StoryLib.TaleStoryArray.length;
        }
        PageMoveBar.setMax(StoryMaxNumber - 1);
        //change the reading progress.
        CurrentStoryNumber = 1;
        PageMoveBar.setProgress(0);//move to first step.
    }

    public void NextPage(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        //if the reader are hidden, then open it to show text.
        ControlReader(View.VISIBLE);
        //decide story type and its limit.
        if(CurrentStoryNumber < StoryMaxNumber){
            CurrentStoryNumber = CurrentStoryNumber + 1;
        }else{
            CurrentStoryNumber = 1;
        }
        //change the ReadProcess, you can check javadoc for ReadProcess variable to know more.
        ReadProcess = 0;
        //load text.
        PageMoveBar.setProgress(CurrentStoryNumber - 1);
        LoadStoryText();
    }

    public void LastPage(View view){
        SeekBar PageMoveBar = findViewById(R.id.PageMoveBar);
        EditText StoryText = findViewById(R.id.StoryText);
        //if the reader are hidden, then open it to show text.
        ControlReader(View.VISIBLE);
        //decide story type and its limit.
        if(CurrentStoryNumber > 1){
            CurrentStoryNumber = CurrentStoryNumber - 1;
        }else if(StoryText.getText().toString().equals("")){
            CurrentStoryNumber = 1;
        }else{
            CurrentStoryNumber = StoryMaxNumber;
        }
        //change the ReadProcess, you can check javadoc for ReadProcess variable to know more.
        ReadProcess = 0;
        //load text.
        PageMoveBar.setProgress(CurrentStoryNumber - 1);
        LoadStoryText();
    }

    //main story showing method.
    //Use Windows NotePad to edit Story Text, and copy it to here.
    @SuppressLint("SetTextI18n")
    private void LoadStoryText(){
        TextView ChapterShowView = findViewById(R.id.ChapterShowView);
        EditText StoryView = findViewById(R.id.StoryText);
        String Story;
        switch (StoryType) {//CurrentStoryNumber - 1: trans number to based on 0.
            case 0:
                Story = StoryLib.MainStoryArray[CurrentStoryNumber - 1];
                break;
            case 1:
                Story = StoryLib.DevStoryArray[CurrentStoryNumber - 1];
                break;
            case 2:
                Story = StoryLib.TaleStoryArray[CurrentStoryNumber - 1];
                break;
            default:
                Story = "";//prevent from null.
                break;
        }
        ChapterShowView.setText(CurrentStoryNumber + " / " +  StoryMaxNumber);
        EachLineStory = Story.split("\n");//save the each line text to RAM to use.
        StoryView.setText(Story);
    }

    private void ControlReader(int ViewState){
        //if the reader are hidden, then open it to show text.
        ConstraintLayout StoryMainLayout = findViewById(R.id.StoryMainLayout);
        //data fix branch.
        if(ViewState == View.VISIBLE && IsWindowMode){//the Window is ON and try to open Reader, the request will be stop.
            ViewState = View.GONE;
        }
        StoryMainLayout.setVisibility(ViewState);
    }//end of story system.

    //hint function.
    private void SetLanguageHint(){
        String Country = Locale.getDefault().getCountry();
        if(!Country.equals("CN")){
            //1.using android api to create a dialog object.
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            //2.set basic values of dialog, including content text,button text,and title.
            dialog.setTitle("Hint");
            dialog.setMessage("--The Story Text has not translated yet, you need to have the language skill to read Chinese.--");
            dialog.setCancelable(true);
            dialog.setPositiveButton(
                    //set left button`s text.
                    getString(R.string.ConfirmWordTran),
                    (dialog1, id) -> dialog1.cancel());
            //3. Use this object to create a actual View in android.
            AlertDialog DialogView = dialog.create();
            DialogView.show();
        }
    }//end of hint function.
}