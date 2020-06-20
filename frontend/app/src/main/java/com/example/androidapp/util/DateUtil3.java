package com.example.androidapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil3 {

  private static ThreadLocal<DateFormat> sdfThreadLocal =  new ThreadLocal<DateFormat>(){
    @Override
    public SimpleDateFormat initialValue(){
      return  new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
  };

  public static  String formatDate(Date date)throws ParseException {
    return sdfThreadLocal.get().format(date);
  }

  public static Date parse(String strDate) throws ParseException{

    return sdfThreadLocal.get().parse(strDate);
  }
}
