package com.lichuang.taineng.aidl;
/**
 * Created by Holy on 2014/11/7.
 */
interface IMyAidlInterface {
    int SendCommand(int type);
    int SendMessage(String msg);
}
