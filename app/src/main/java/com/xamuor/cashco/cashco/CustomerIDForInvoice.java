package com.xamuor.cashco.cashco;

public class CustomerIDForInvoice {
    private static int customerID;

        public static int getCustomerID() {
            return customerID;
        }

        public static void setCustomerID(int customerID) {
            CustomerIDForInvoice.customerID = customerID;
        }



}
