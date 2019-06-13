package com.xamuor.cashco.pos;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Product.class, Customer.class}, version = 1)
public abstract class PosDatabase extends RoomDatabase {
    public abstract ProductDAO myDao();
}
