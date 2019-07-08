package com.xamuor.cashco;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.xamuor.cashco.Utilities.PosDatabase;

public class ProductRepository {
    private String DB_NAME = "newpos_db";
    private PosDatabase posDatabase;
    public ProductRepository(Context context) {
        posDatabase = Room.databaseBuilder(context, PosDatabase.class, DB_NAME).build();
    }
}
