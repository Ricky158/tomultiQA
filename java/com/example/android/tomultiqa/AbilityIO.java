package com.example.android.tomultiqa;

import android.content.Context;

import java.util.ArrayList;


/**
 *  This Class can get Abilities Information in Game, you can use methods below this class to access Ability detail.<br/>
 *  Each Ability has its own Name, Effect, Tourney Index.<br/>
 *  [Name] is the Ability label, which shown for user. user can watch Name to remember the effect of Ability.<br/>
 *  [Effect] is the Ability's Effect, which activated in Battle, it can affect Boss or User's State, Values, and etc.<br/>
 *  [Tourney Index] is the Ability's Evaluation Index of Tourney, the Abilities can be selected by User and equip to Boss, to change its Evaluation.<br/>
 *  As Developer, you need to edit all Abilities Information here, including all variables above, and it changed automatically in all codes.<br/>
 *  Oh, you need to know that, this class not included Operation for Battle definition, you need to do it in MainActivity.<br/>
 */

public class AbilityIO {

    //Constructor of Class, just like OnCreate() method for Activity.
    public AbilityIO(Context Import) {//initialize Ability List.
        SetAbilityName();
        SetAbilityEffect(Import);//you can't store Context as global variable, it can only passed in methods.
        SetAbilityTourney();
        //import Context from Android.
    }


    //Ability.
    //"protected" means it can only called by Class Instance.
    protected final ArrayList<String> NameList = new ArrayList<>();
    protected final ArrayList<Integer> TourneyList = new ArrayList<>();
    protected final ArrayList<String> EffectList = new ArrayList<>();

    protected int ReturnAbilityNumber(){
        return NameList.size();
    }

    //developer helper, I don't recommend you delete it, you can add log() method to print full list in android studio Run log.
    protected String PrintAllAbilitiesId(){
        int Position = 0;
        StringBuilder ReturnValue = new StringBuilder();
        ReturnValue.append("ID      Ability\n");
        while(Position < NameList.size()){
            //Position start with 0.
            ReturnValue.append(Position).append("   ").append( NameList.get(Position) ).append("\n");
            Position = Position + 1;
        }
        return ReturnValue.toString();
    }

    //called this method, it will return a Name and Effect manual String for Abilities.
    protected String PrintFullAbilityString(){//getter for other class.
        int Position = 0;
        StringBuilder ReturnString = new StringBuilder();
        String NumberWrite;
        while(Position < NameList.size()){//Print each Ability's Name and Effect in List by loop.
            NumberWrite  = TourneyList.get(Position).toString();
            if(TourneyList.get(Position) > 0){
                NumberWrite = "+" + NumberWrite;
            }
            ReturnString.append( NameList.get(Position) ).append("\n").append(NumberWrite).append("\n");
            ReturnString.append( EffectList.get(Position) ).append("\n\n");
            Position = Position + 1;
        }
        return ReturnString.toString();
    }

    //the number hint here is their Id to call.
    private void SetAbilityName(){
        NameList.add("?????? Trial");//0
        NameList.add("?????? Rush");//1
        NameList.add("?????? Fragile");//2
        NameList.add("?????? WeakSpot");//3
        NameList.add("?????? Slow");//4
        NameList.add("?????? Corrosion");//5
        NameList.add("?????? Curse");//6
        NameList.add("?????? Diversion");//7
        NameList.add("?????? Treasure");//8
        NameList.add("?????? Recover");//9
        NameList.add("?????? LastHurt");//10
        NameList.add("?????? Shield");//11
        NameList.add("?????? Hopeless");//12
        NameList.add("?????? Duel");//13
        NameList.add("?????? FastStep");//14
        NameList.add("?????? Proud");//15
        NameList.add("?????? Fear");//16
        NameList.add("?????? ReTime");//17
        NameList.add("?????? Active");//18
        NameList.add("?????? Wake");//19
        NameList.add("?????? Destruct");//20
    }

    private void SetAbilityEffect(Context context){
        EffectList.add(context.getString(R.string.TrialEffectTran));
        EffectList.add(context.getString(R.string.RushEffectTran));
        EffectList.add(context.getString(R.string.FragileEffectTran));
        EffectList.add(context.getString(R.string.WeakSpotEffectTran));
        EffectList.add(context.getString(R.string.SlowEffectTran));
        EffectList.add(context.getString(R.string.CorrosionEffectTran));
        EffectList.add(context.getString(R.string.CurseEffectTran));
        EffectList.add(context.getString(R.string.DiversionEffectTran));
        EffectList.add(context.getString(R.string.TreasureEffectTran));
        EffectList.add(context.getString(R.string.RecoverEffectTran));
        EffectList.add(context.getString(R.string.LastHurtEffectTran));
        EffectList.add(context.getString(R.string.ShieldEffectTran));
        EffectList.add(context.getString(R.string.HopelessEffectTran));
        EffectList.add(context.getString(R.string.DuelEffectTran));
        EffectList.add(context.getString(R.string.FastStepEffectTran));
        EffectList.add(context.getString(R.string.ProudEffectTran));
        EffectList.add(context.getString(R.string.FearEffectTran));
        EffectList.add(context.getString(R.string.RetimeEffectTran));
        EffectList.add(context.getString(R.string.ActiveEffectTran));
        EffectList.add(context.getString(R.string.WakeEffectTran));
        EffectList.add(context.getString(R.string.DestructEffectTran));
    }

    private void SetAbilityTourney(){
        TourneyList.add(30);
        TourneyList.add(17);
        TourneyList.add(-70);
        TourneyList.add(-25);
        TourneyList.add(-17);
        TourneyList.add(25);
        TourneyList.add(60);
        TourneyList.add(25);
        TourneyList.add(0);
        TourneyList.add(85);
        TourneyList.add(-105);
        TourneyList.add(35);
        TourneyList.add(0);
        TourneyList.add(0);
        TourneyList.add(100);
        TourneyList.add(36);
        TourneyList.add(10);
        TourneyList.add(-33);
        TourneyList.add(9);
        TourneyList.add(34);
        TourneyList.add(10);
    }//end of Ability.
}
