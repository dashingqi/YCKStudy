package com.example.binderdemo.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * 自定义Binder接口
 */
public interface IBookManager extends IInterface {

    public static final String Description = "com.example.binddemo";

    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION+1;
    static final int TRANSACTION_addBookList = IBinder.FIRST_CALL_TRANSACTION+2;

    public List<Book> getBookList()throws RemoteException;
    public void addBookList(Book book) throws RemoteException;




}
