package com.xamuor.cashco.Utilities;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.xamuor.cashco.Customer;
import com.xamuor.cashco.Inventories;
import com.xamuor.cashco.Product;
import com.xamuor.cashco.Utilities.DAO;

@Database(entities = {Product.class, Customer.class, Inventories.class}, version = 1)
public abstract class PosDatabase extends RoomDatabase {
    public abstract DAO myDao();
}
