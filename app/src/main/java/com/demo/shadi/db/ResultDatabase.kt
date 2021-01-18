//package com.demo.shadi.db
//
//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.demo.shadi.model.Result
//
//@Database(entities = { Result::class.java}, version = 1)
//public abstract class ResultDatabase : RoomDatabase() {
//
//
////    private static final String DB_NAME = "Country_Database.db";
////    private static ResultDatabase INSTANCE;
////
////
////    public static ResultDatabase getResultDatabase(Context context) {
////        if (INSTANCE == null) {
////            INSTANCE = Room.databaseBuilder(context, ResultDatabase.class, DB_NAME).build();
////
////        }
////        return INSTANCE;
////    }
////
////    public static void destroyInstance() {
////        INSTANCE = null;
////    }
////
////    public abstract ResultDao countryDao();countryDao
//}