package com.example.ipcdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelUser implements Parcelable {

    private String name;
    private int age;
    private String address;

    public ParcelUser(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 从序列化后的对象中，转化成原对象
     * @param in
     */
    protected ParcelUser(Parcel in) {
       name = in.readString();
       age = in.readInt();
       address = in.readString();

    }

    public static final Creator<ParcelUser> CREATOR = new Creator<ParcelUser>() {
        /**
         * 从序列化后的对象中，创建原始对象
         * @param in
         * @return
         */
        @Override
        public ParcelUser createFromParcel(Parcel in) {
            return new ParcelUser(in);
        }

        @Override
        public ParcelUser[] newArray(int size) {
            return new ParcelUser[size];
        }
    };

    /**
     * 返回当前对象的描述性文件，一般都是返回0，如果当前对象有描述性文件，就返回当前的文件
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将现有的对象，写入到序列化机构中
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(address);
    }
}
