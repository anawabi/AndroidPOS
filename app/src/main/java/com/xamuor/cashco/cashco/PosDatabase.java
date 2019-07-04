package com.xamuor.cashco.cashco;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Product.class, Customer.class, Inventories.class}, version = 1)
public abstract class PosDatabase extends RoomDatabase {
    public abstract DAO myDao();
}
