package com.example.android.tomultiqa;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemIO {
    //database part.
    //store all data from database.
    protected SQLiteDatabase dbRead;
    protected SQLiteDatabase dbWrite;
    private ArrayList<String> ItemNameList = null;
    private ArrayList<Integer> ItemNumberList = null;

    /**
     * the ItemName - ItemText Map, because of the access of string.xml need context. So we need to initialize them in constructor method.
     */
    private final HashMap<String,String> ItemNameToText = new HashMap<>();

    /**
     * Constructor method for ItemIO class, used in Initializing.
     * @param context the Position that using in <code>getString()</code> method and database loading.
     */
    public ItemIO(Context context){
        if(dbRead == null){
            ItemDbHelper DBHelper = new ItemDbHelper(context);
            dbRead = DBHelper.getReadableDatabase();
            dbWrite = DBHelper.getWritableDatabase();
        }
        LoadItemData(null);
        InitializeItemText(context);
    }

    /**
     * Assess method for ItemNameList.
     * @return The ArrayList of selected type ItemName data.
     */
    protected ArrayList<String> getItemNameList(){
        return ItemNameList;
    }

    /**
     * Assess method for ItemNumberList.
     * @return The ArrayList of selected type ItemNumber data.
     */
    protected ArrayList<Integer> getItemNumberList(){
        return ItemNumberList;
    }

    /**
     * Refresh All List in this class, to prevent from useless refresh and more flexibility to combine API.<br/>
     * You need to call this method after any changes to List.<br/>
     * Notice: You need to call <code>getItemNameList()</code> and <code>getItemNumberList()</code> method AGAIN to get refreshed data.<br/>
     * @param ItemType You can use this parameter to get certain part of Item Number data. Pass <code>null</code> to get all.
     */
    protected void RefreshAllList(String ItemType) {
        LoadItemData(ItemType);
    }

    /**
     * Main method for Item IO operation, you can use this method to add / cost single kind of Item to database.<br/>
     * Notice: You need to call <code>RefreshAllList()</code> and <code>get...List()</code> method to get refreshed data.<br/>
     * @param ItemName The Name of Item.
     * @param ItemType The Type of Item.
     * @param Number The number of this adding operation, if passed a number > 0, then do adding; or passed < 0 to do costing.
     * @return If return the number larger than 0, means Adding Success; if return <code>-1</code>, means Adding Fail.
     */
    @SuppressLint("Range")
    protected int EditOneItem(String ItemName, String ItemType, int Number){
        //1.according to GotItemName variable, save data to database.
        //1.0 prepare method parameter, and check if item exist.
        String Selection = ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
        String[] SelectionArgs = {ItemName};
        ContentValues ItemNumberValues = new ContentValues();
        //1.1 get data from database, and fill Cursor.
        //let Cursor return the ItemName and ItemNumber data in this name line.
        Cursor cursor = SearchDatabase(Selection,SelectionArgs);
        boolean IsItemExist = cursor.moveToFirst();
        if(IsItemExist){
            //1.2 get this Item's number.
            int ItemNumberInDb = cursor.getInt(cursor.getColumnIndex(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber));
            //1.3 check if operation is reasonable.
            if(Number < 0 && ItemNumberInDb < Math.abs(Number)){
                return -1;//error: Item in database are not enough to cost.
            }else if(Number == 0){
                return -1;//error: useless operation.
            }
            //1.4 change the number and save changes.
            ItemNumberValues.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber,ItemNumberInDb + Number);
            int UpdateLine = dbWrite.update(
                    ItemDbHelper.DataBaseEntry.TABLE_NAME,
                    ItemNumberValues,
                    Selection,
                    SelectionArgs
            );
            cursor.close();
            if(UpdateLine < 1){
                UpdateLine = -1;
            }
            return UpdateLine;
        }else{
            //2. if item is not in database, add a new kind of item in database.
            //2.1 initializing item column in database, and the number will be set to "CraftNumber".
            ContentValues values = new ContentValues();
            values.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName,ItemName);
            values.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemType,ItemType);
            values.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemText,getItemText(ItemName));
            values.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber,Number);
            long InsertRow = dbWrite.insert(ItemDbHelper.DataBaseEntry.TABLE_NAME,null,values);
            return (int) InsertRow;
        }
    }

    /**
     * Main method for Item IO operation, you can use this method to Cost single or multi kind of Item to database.<br/>
     * Notice1: You need to call <code>getItemNameList()</code> and <code>getItemNumberList()</code> method to get refreshed data.<br/>
     * Notice2: You need to make sure each Item's Name and CostNumber have same Position in two List.<br/>
     * @param ItemCostList The List Obtains which Item(s) needed to Cost.
     * @param CostNumberList The List Obtains Each Item's CostNumber.
     * @return
     * The execute result of this method:<br/>
     * If All Elements in return Array are <code>true</code>, it means success.<br/>
     * <code>Array[0]</code> is <code>false</code>, means "Not All Item have unlocked"<br/>
     * <code>Array[1]</code> is <code>false</code>, means "Not All Item have enough number to craft."
     */
    @SuppressLint("Range")
    protected boolean[] CostItem(ArrayList<String> ItemCostList, ArrayList<Integer> CostNumberList){
        //0. preparation.
        LoadItemData(null);//load all item data in List.
        String Selection = ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName + " = ?";
        boolean[] Report = new boolean[]{true,true};
        Cursor cursor;
        //Not using ItemNumberList is that we need to sync CostNumberList and ItemNumberInDb List, to do database operation.
        ArrayList<Integer> ItemNumberInDb = new ArrayList<>();
        //Used in database operation.
        int ColumnId;
        //1.for each item should be cost, get its number in database.
        for(String EachItem : ItemCostList){
            //1.1 prepare data.
            String[] NeedToSearch ={EachItem};
            //1.2 get items data from database, and fill Cursor.
            cursor = SearchDatabase(Selection,NeedToSearch);
            //1.3 get if Item Exist situation from Cursor.
            boolean IsItemExist = cursor.moveToFirst();
            ColumnId = cursor.getColumnIndex(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber);
            if(IsItemExist){
                //1.4 get Item Number in database.
                ItemNumberInDb.add(cursor.getInt(ColumnId));
            }
        }
        //2.1 check if the Item are unlocked in the Item system, if not, stop the process.
        if(ItemNumberInDb.size() != CostNumberList.size()){//not all items in current recipe are unlocked in Item system.
            Report[0] = false;
        }
        //2.2 after get item number list, for each cost number in list, do check process.
        for(int NeedToCheck : ItemNumberInDb){
            if(NeedToCheck < CostNumberList.get(ItemNumberInDb.indexOf(NeedToCheck))){
                Report[1] = false;//Not all of number in list smaller than which need to cost, then false.
            }
        }
        //3.if all Item are unlocked and have enough number to cost, then do Craft process, or quit Craft.
        if(Report[0] && Report[1]){
            for(int EachCost : CostNumberList){
                int Position = CostNumberList.indexOf(EachCost);//the Id of each item in CostNumberList,used in calculate item's number which after cost.
                int UpdatedNumber = ItemNumberInDb.get(Position) - CostNumberList.get(Position);//calculate each item's number which after cost.
                ContentValues ChangeValue = new ContentValues();
                ChangeValue.put(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber,UpdatedNumber);//update number data in db.
                dbWrite.update(
                        ItemDbHelper.DataBaseEntry.TABLE_NAME,
                        ChangeValue,
                        Selection,
                        new String[]{ItemCostList.get(CostNumberList.indexOf(EachCost))}
                );
            }
        }
        return Report;
    }

    /**
     * Make database's data to ArrayList form.
     * @param ItemType Pass this parameter to get certain type of item data, pass <code>null</code> to get all item's data.
     */
    private void LoadItemData(String ItemType){
        //0.preparation.
        ItemNameList = new ArrayList<>();
        ItemNumberList = new ArrayList<>();
        //1.query entire backpack database, store all query result.
        String Selection;
        String[] SelectionArgs;
        if(ItemType != null){
            Selection = ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemType + " = ?";
            SelectionArgs = new String[]{ItemType};
        }else{
            Selection = null;
            SelectionArgs = null;
        }
        Cursor ReturnCursor = SearchDatabase(Selection,SelectionArgs);
        //2.load data to variables.
        if(ReturnCursor != null && ReturnCursor.getCount() > 0){//prevent from NPE.
            ReturnCursor.moveToFirst();//prevent from -1 position.
            //calculate variables and keep them to improve efficiency.
            final int ItemNameColumnId = ReturnCursor.getColumnIndex(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemName);
            final int ItemNumberColumnId = ReturnCursor.getColumnIndex(ItemDbHelper.DataBaseEntry.COLUMN_NAME_ItemNumber);
            boolean IsMoreData = !ReturnCursor.isLast();//check Is it more data available to query.
            do {//query at first position of Cursor.
                if(ReturnCursor.isLast()){
                    IsMoreData = false;
                }
                ItemNameList.add(ReturnCursor.getString(ItemNameColumnId));
                ItemNumberList.add(ReturnCursor.getInt(ItemNumberColumnId));
                ReturnCursor.moveToNext();
            }while (IsMoreData);//if IsMoreData is false(no more data), stop query, if not, continue query.
        }
        if(ReturnCursor != null){
            ReturnCursor.close();
        }
    }

    // TODO: 2021/10/10 DataSet.
    private void InitializeItemText(Context context){
        //DataSet.
        String[] AllItemName = new String[]{
                context.getString(R.string.MagicPieceTran),
                context.getString(R.string.ElfTreeWoodTran),
                context.getString(R.string.ElementFlowBottleTran),
                context.getString(R.string.DreamFruitTran),
                context.getString(R.string.SpiritStoneTran),
                context.getString(R.string.PureStringTran),
                context.getString(R.string.SoildMetalTran),
                context.getString(R.string.CurvedGoldTran)
        };
        String[] AllItemText = new String[]{
                "这是“世界”经历大战后，古人遗留下来的魔力凝结的物质。是极佳的素材。被各国的商人们高价收购。据说法术学院当中的那些“帽子”们，在它身上发现了重大的秘密。",
                "自精灵一族居住的森林当中获得的树木。就产量而言，这一材料并不稀有。真正让它具备价值的原因，在于精灵族对森林的守护。能从精灵森林的重重陷阱当中逃脱的伐木工可为数不多。",
                "装有流动元素的玻璃瓶，是缺乏魔力者和初学者使用魔法的重要道具。只有熟练的炼金术师和法师们联合才能制作。且其市场流通被严格监管，供不应求。",
                "虽然“清梦果”的外形酷似果实，但需要澄清的是，尽管名字里有“果”，但清梦果实际上并不能食用。曾经有人品尝过它，但当人们询问他滋味时，他却无法表达出来。也因此，它被巫师们用作忘记人间疾苦的道具，也就有了“清梦”的名号。（合成获得）",
                "蕴含灵气的矿石，可以从中提取出编制魔法术式的能量，或是提炼过后，作为机械的动力源。是一种广泛使用的资源。不过，矿石本身其实价值有限，其珍贵实际上体现在被提炼的纯度上。",
                "通过精心清洁和祝福的绳子，相较于它“弱不禁风”的外观，它的束缚力其实很大。一些贵族也因为其纯白无暇的外观而将这种绳子用于装饰。是一种兼具实用和装饰性的资源。",
                "经过铁匠锻造，塑性的优质金属。是被用于制作武器，防具等用途的素材。注意，虽然名字叫“韧铁”，但不一定是金属铁，也可能是其他物质的混合体。打造这些金属的细节，是各个地区的铁匠们不约而同要保守的秘密。",
                "金银等贵金属具备良好的魔法适应性，是用于制作法器，魔法制品的好素材。“蚀金”就是通过炼金术师之手刻蚀的黄金。其上的刻痕可以是魔力的流动更为畅通，但有市井传言道：真正优良的“蚀金”是另有制作方法的。这可能就是普通的“蚀金”明明含有不少黄金，价值却平平无奇的原因吧。"
        };
        int MaxLimit = AllItemName.length;
        //Initializing.
        int Process = 0;
        while (Process < MaxLimit){
            ItemNameToText.put(AllItemName[Process], AllItemText[Process]);
            Process = Process + 1;
        }
    }

    /**
     * Search Item database, and return the result as Cursor form.
     * @param Selection Which column you need to search.
     * @param SelectionArgs Which Attribute(s) used in searching.
     * @return The search result in Cursor form.
     */
    protected Cursor SearchDatabase(String Selection, String[] SelectionArgs){
        return dbRead.query(//let Cursor return the ItemName and ItemNumber data in this name line.
                ItemDbHelper.DataBaseEntry.TABLE_NAME,
                null,
                Selection,
                SelectionArgs,
                null,
                null,
                null
        );
    }

    /**
     * According to Item's name to return it's ItemText (Description of a kind of Item).
     * @param ItemName Item's Name.
     * @return ItemText that represent this Item's Description.
     */
    protected String getItemText(String ItemName){
        return ItemNameToText.get(ItemName);
    }

}
