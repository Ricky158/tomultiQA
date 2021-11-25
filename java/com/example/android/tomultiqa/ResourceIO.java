package com.example.android.tomultiqa;

import android.content.Context;

/**
 * Main Class for Resource Operation, you can use methods below this class to manage Resource system in Game.<br/>
 * [Resource]: means Point and Material data.<br/>
 * You need to construct a Instance to call methods in this class.<br/>
 * You can refer examples in MainActivity and SquareActivity method.<br/>
 */

public class ResourceIO {

    /**
     * Reset the Variables in this class from SharedPreference.<br/>
     * So, when you want to excess these variables, please call constructor to refresh data first.
     * @param Import context, which used in refresh data.
     */
    public ResourceIO(Context Import){
        InitializingResource(Import);
        InitializingRecord(Import);
    }


    //Game Resource system. Including Point and Materials.
    /*
    QuestCombo and BossDeadTime variable are handled in MainActivity or MemoryActivity.
     */
    //static variable, thanks to: https://www.cnblogs.com/dotgua/p/6354151.html !

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Resource change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int UserPoint = 0;
    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Resource change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected int UserMaterial = 0;

    /**
     * You can use this method to add Point to total number of User having Point.<br/>
     * Notice: <br/>
     * 1.User can't get any Point when they having more than 2,000,000,000 Point.<br/>
     * @param AddNumber the Number of Point that you want to give to user.<br/>
     */
    private static final int AddLimit = 10000000;

    protected void GetPoint(int AddNumber){
        if(AddNumber > AddLimit){
            AddNumber = AddLimit;
        }
        if(UserPoint <= 2000000000){
            UserPoint = UserPoint + AddNumber;
            PointRecord = PointRecord + AddNumber;
        }
    }

    /**
     * You can use this method to Minus Point to total number of User having Point.<br/>
     * @param CostNumber the Number of Point that you want to minus to user.<br/>
     */
    protected void CostPoint(int CostNumber){
        UserPoint = UserPoint - CostNumber;
    }

    /**
     * You can use this method to add Material to total number of User having Material.<br/>
     * @param AddNumber the Number of Material that you want to give to user.<br/>
     */
    protected void GetMaterial(int AddNumber){
        UserMaterial = UserMaterial + AddNumber;
        MaterialRecord = MaterialRecord + AddNumber;
    }

    /**
     * You can use this method to Minus Material to total number of User having Material.<br/>
     * @param CostNumber the Number of Material that you want to minus to user.<br/>
     */
    protected void CostMaterial(int CostNumber){
        UserMaterial = UserMaterial - CostNumber;
    }

    /**
     * You need to call this method to save All Point and Material data variation (including adding, costing, changes, ane etc.) after all usage done.<br/>
     * We suggest you call it when the current Context is [done], like OnPause() or OnStop() in Activity, to cut down unnecessary cost.<br/>
     * @param context Using in SharedPreferences, which used in saving data.<br/>
     */
    protected void ApplyChanges(Context context){
        SupportLib.saveMultiInt(
                context,
                "BattleDataProfile",
                new String[]{"UserPoint","UserMaterial"},
                new int[]{UserPoint,UserMaterial}
        );
        SupportLib.saveLongData(context,"RecordDataFile","PointGotten",PointRecord);
        SupportLib.saveIntData(context,"RecordDataFile","MaterialGotten",MaterialRecord);
    }

    private void InitializingResource(Context context){
        int[] ResourceData = SupportLib.getMultiInt(
                context,
                "BattleDataProfile",
                new String[]{"UserPoint","UserMaterial"},
                new int[]{0,0}
        );
        UserPoint = ResourceData[0];
        UserMaterial = ResourceData[1];
    }//end of Game Resource system.


    //Record part of Point and Material, sub of Record system.
    //Exp Record is loaded in ExpIO class, you need to create a Instance to call it.
    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Resource change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected static long PointRecord = 0;

    /**
     * you can direct calling this variable to get value, but you need to use methods in this class to handle all Resource change operations.<br/>
     * Or it will cause number error.<br/>
     */
    protected static int MaterialRecord = 0;

    private void InitializingRecord(Context context){
        PointRecord = SupportLib.getLongData(context,"RecordDataFile","PointGotten",0);
        MaterialRecord = SupportLib.getIntData(context,"RecordDataFile","MaterialGotten",0);
    }//end of Record part.

}
