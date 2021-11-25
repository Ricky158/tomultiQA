package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Main Class for Exp Operation, you can use methods below this class to manage Exp system in Game.<br/>
 * You need to construct a Instance to call methods in this class.<br/>
 * You can refer examples in ShopActivity - BuyAndAffect(), MainActivity and SquareActivity - ShowResourceCodeInput() method.<br/>
 */

public class ExpIO{

    //EXP system.

    /**
     * Reset the Variables in this class from SharedPreference.<br/>
     * So, when you want to excess these variables, please call constructor to refresh data first.
     * @param Import context, which used in refresh data.
     */
    public ExpIO(Context Import){
        InitializingExpData(Import);//import raw data.
        CheckLevel();//initialize data.
    }


    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Exp change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int UserLevel;

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Exp change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int LevelLimit = 50;

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Exp change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int UserHaveEXP;

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Exp change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int EXPRecord = 0;

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Exp change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int UserUpgradeEXP = 1;

    /**
     * You can use this method to add Exp to total number of User having Exp.<br/>
     * And the IO will calculate the current Level, HaveExp, Upgrade Exp.<br/>
     * Notice: <br/>
     * 1.Once Adding can't over 1,000,000.<br/>
     * 2.User can't get any Exp when they having more than 2,000,000 Exp.<br/>
     * @param AddNumber the Number of Exp that you want to give to user.<br/>
     */
    static final int AddLimit = 1000000;

    protected void GetEXP(int AddNumber){
        if(AddNumber > AddLimit){
            AddNumber = AddLimit;
        }
        if(UserHaveEXP <= AddLimit * 2){
            UserHaveEXP = UserHaveEXP + AddNumber;
        }
        CheckLevel();
    }

    /**
     * You can use this method to Minus Exp to total number of User having Exp.<br/>
     * And the IO will calculate the current Level, HaveExp, Upgrade Exp.<br/>
     * @param MinusNumber the Number of Exp that you want to minus to user.<br/>
     */
    protected void LostEXP(int MinusNumber){
        UserHaveEXP = UserHaveEXP - MinusNumber;
        CheckLevel();
    }

    /**
     * You need to call this method to save All Exp data variation (including adding, costing, changes, ane etc.) after all usage done.<br/>
     * We suggest you call it when the current Context is [done], like OnPause() or OnStop() in Activity, to cut down unnecessary cost.<br/>
     * @param context Using in SharedPreferences, which used in saving data.<br/>
     */
    protected void ApplyChanges(Context context){
        SupportLib.saveMultiInt(
                context,
                "EXPInformationStoreProfile",
                new String[]{"UserLevel","UserHaveEXP"},
                new int[]{UserLevel,UserHaveEXP}
        );
        SupportLib.saveIntData(context,"ExcessDataFile","LevelLimit",LevelLimit);
        SupportLib.saveIntData(context,"RecordDataFile","EXPGotten",EXPRecord);
    }

    //check the EXP number, and make level up or down calculation.
    //Thanks to:https://www.jianshu.com/p/59b266c644f3!
    private void CheckLevel(){
        //show current level upgrade needs.
        CalculateUpgradeEXP();
        //1.upgrade.
        while(UserHaveEXP >= UserUpgradeEXP && UserLevel < LevelLimit){
            UserLevel = UserLevel + 1 ;
            UserHaveEXP = UserHaveEXP - UserUpgradeEXP;
            //1.1 after upgrade / downgrade,show lower needs to upgrade and show the EXP data to user, including level, upgrade EXP, and User haveEXP.
            CalculateUpgradeEXP();
        }
        while(UserHaveEXP < 0 && UserLevel > 1){
            UserLevel = UserLevel - 1 ;
            UserHaveEXP = UserHaveEXP + UserUpgradeEXP;
            //1.1 after upgrade / downgrade,show lower needs to upgrade and show the EXP data to user, including level, upgrade EXP, and User haveEXP.
            CalculateUpgradeEXP();
        }
    }

    @SuppressLint("SetTextI18n")
    private void CalculateUpgradeEXP(){
        //EXPLimit stand for EXP which user need to collect to upgrade.
        // it just use for double form number calculating, it is not used in actual EXP system code.
        double EXPLimit;
        //it means the number: level^1.6 + 15 - level^1.1.
        EXPLimit = Math.pow(UserLevel,1.6) + 150 - Math.pow(UserLevel,1.2) ;
        //lost the number under 0`s part,or "FLOOR".
        UserUpgradeEXP = (int) EXPLimit;
        //UpgradeEXP data will be showed in PrintEXPData() method.
    }

    //read EXP data method.
    private void InitializingExpData(Context context){
        int[] ExpData = SupportLib.getMultiInt(
                context,
                "EXPInformationStoreProfile",
                new String[]{"UserLevel","UserHaveEXP"},
                new int[]{1,0}
        );
        UserLevel = ExpData[0];
        UserHaveEXP = ExpData[1];
        LevelLimit = SupportLib.getIntData(context,"ExcessDataFile","LevelLimit",50);
        EXPRecord = SupportLib.getIntData(context,"RecordDataFile","EXPGotten",0);
    }//the end of EXP system.
}
