package com.xamuor.cashco.pos;

public class CustomerIDForInvoice {
    private static Object customerID;

        public static Object getCustomerID() {
            return customerID;
        }

        public static void setCustomerID(Object customerID) {
            CustomerIDForInvoice.customerID = customerID;
        }



}
