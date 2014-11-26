/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: F:\\android\\workspace\\Taineng\\src\\com\\lichuang\\taineng\\aidl\\IMyAidlInterface.aidl
 */
package com.lichuang.taineng.aidl;
/**
 * Created by Holy on 2014/11/7.
 */
public interface IMyAidlInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.lichuang.taineng.aidl.IMyAidlInterface
{
private static final java.lang.String DESCRIPTOR = "com.lichuang.taineng.aidl.IMyAidlInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.lichuang.taineng.aidl.IMyAidlInterface interface,
 * generating a proxy if needed.
 */
public static com.lichuang.taineng.aidl.IMyAidlInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.lichuang.taineng.aidl.IMyAidlInterface))) {
return ((com.lichuang.taineng.aidl.IMyAidlInterface)iin);
}
return new com.lichuang.taineng.aidl.IMyAidlInterface.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_SendCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.SendCommand(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_SendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _result = this.SendMessage(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.lichuang.taineng.aidl.IMyAidlInterface
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public int SendCommand(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_SendCommand, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int SendMessage(java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_SendMessage, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_SendCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_SendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public int SendCommand(int type) throws android.os.RemoteException;
public int SendMessage(java.lang.String msg) throws android.os.RemoteException;
}
