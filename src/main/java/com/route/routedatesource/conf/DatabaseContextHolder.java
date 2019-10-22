package com.route.routedatesource.conf;

public class DatabaseContextHolder {
    private static ThreadLocal<String> contextHolder=new ThreadLocal<String>();
      public static void setDbType(String dbType){
                 contextHolder.set(dbType);
             }
      public static String getDbType(){
                 return contextHolder.get();
             }

              public static void clearDbType(){
                 contextHolder.remove();
             }
}
