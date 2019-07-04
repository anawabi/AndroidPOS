package com.xamuor.cashco.cashco;

public class Routes {
//    Remote-server...
//    private static String IMG_PATH = "http://pos.xamuor.com/uploads/";
//    Virtual-device IP
    private static String IMG_PATH = "http://192.168.56.1:8000/uploads/";
//    private static String IMG_PATH = "http://192.168.133.2:8000/uploads/";
    public static String setUrl(String route) {
        String url = "http://192.168.56.1:8000/api/" + route;
//        Virtual-device IP
        return url;

        //    Remote-server...
//        return "http://pos.xamuor.com/api/" + url;
//        return "http://192.168.133.2:8000/api/" + url;
    }
    // To load images though their path
    public static String onLoadImage(String img) {
        return IMG_PATH.concat(img);
    }
}
