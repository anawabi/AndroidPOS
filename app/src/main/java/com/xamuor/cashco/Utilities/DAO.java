package com.xamuor.cashco.Utilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.xamuor.cashco.Customer;
import com.xamuor.cashco.Inventories;
import com.xamuor.cashco.Product;

import java.util.List;

@Dao
public interface DAO {
    /* ================ Inventory ================*/
    //    Insert products from server into inventories of ROOM
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertIntoInventories(Inventories inventory);
    @Query("SELECT * FROM inventories WHERE compId = :compId")
    public List<Inventories> getInventories(int compId);

//    fetch qty of a specific product
    @Query("SELECT qty FROM inventories WHERE compId = :compId AND productId = :pId")
    public int getQty(int compId, int pId);
//    update qty of inventory
    @Query("UPDATE inventories SET qty = :q WHERE compId = :compId AND productId = :pid")
    public void updateInventory(int q, int compId, int pid);

//    ِDelete products from Room
    @Query("DELETE FROM inventories WHERE compId = :compId")
    public void deleteProducts(int compId);

//    Delete inventories

    /* ==================== /. Inventory ===================*/
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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCustomer(Customer customer);
//    Fetch customers based on company-id
    @Query("SELECT * FROM customers WHERE companyId = :cid")
    public List<Customer> getCustomers(int cid);
//    Delete customers
    @Query("DELETE FROM customers")
    public void deleteCustomer();

//    Fetch customer-id of a specific customer
//   * is set to return all columns only to avoid giving error otherwise only we need customer-id here
    @Query("SELECT * FROM customers WHERE customerName = :custName")
    public int getCustId(String custName);
}
