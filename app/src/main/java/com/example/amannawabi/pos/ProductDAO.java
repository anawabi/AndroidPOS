package com.example.amannawabi.pos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDAO {
    //    Insert into Room
    @Insert
    public void insert(Product product);

    // Fetch all products based on company-id
    @Query("SELECT * FROM invoices WHERE comp_id = (:cid)")
    public List<Product> getProducts(int cid);

    // Check if item already exists
    @Query("SELECT * FROM invoices WHERE product_name IN (:item)")
    public List<Product> getItem(String item);

    // Delete products
    @Query("DELETE FROM invoices")
    public void delete();

    // Edit Item
    @Query("UPDATE invoices SET product_qty = :qty WHERE comp_id = :cid AND product_name = :item")
    public void updateItem(int qty, int cid, String item);

/*
//    select sum of price
    @Query("SELECT SUM( product_price) FROM products WHERE comp_id = :cid;")
    public List<Product> getTotal(int cid);
*/

    /* =============== CUSTOMERS =============== */
//    Insert customers from server into ROOM
    @Insert
    public void insertCustomer(Customer customer);
//    Fetch customers based on company-id
    @Query("SELECT * FROM customers WHERE companyId = :cid")
    public List<Customer> getCustomers(int cid);
//    Delete customers
    @Query("DELETE FROM customers")
    public void deleteCustomer();

}
