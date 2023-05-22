package com.jbm.biteburgerv2.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class FoodSummary implements Parcelable {
    private String lineaId;
    private boolean isMenu;
    private String name1;
    private String name2;
    private String name3;
    private double price;
    private int quantity;
    private String imgUrl;

    public FoodSummary() {
    }

    public FoodSummary(String lineaId, boolean isMenu, String name1, double price, int quantity, String imgUrl) {
        this.lineaId = lineaId;
        this.isMenu = isMenu;
        this.name1 = name1;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }

    public FoodSummary(String lineaId, boolean isMenu, String name1, String name2, String name3, double price, int quantity, String imgUrl) {
        this.lineaId = lineaId;
        this.isMenu = isMenu;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.price = price;
        this.quantity = quantity;
        this.imgUrl = imgUrl;
    }

    public String getLineaId() {
        return lineaId;
    }

    public void setLineaId(String lineaId) {
        this.lineaId = lineaId;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean menu) {
        isMenu = menu;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // PARCELABLE (Para poder pasar un Arraylist de tipo Food a un Bundle)
    protected FoodSummary(Parcel in) {
        lineaId = in.readString();
        isMenu = in.readByte() != 0;
        name1 = in.readString();
        name2 = in.readString();
        name3 = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        imgUrl = in.readString();
    }

    public static final Creator<FoodSummary> CREATOR = new Creator<FoodSummary>() {
        @Override
        public FoodSummary createFromParcel(Parcel in) {
            return new FoodSummary(in);
        }

        @Override
        public FoodSummary[] newArray(int size) {
            return new FoodSummary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(lineaId);
        dest.writeByte((byte) (isMenu ? 1 : 0));
        dest.writeString(name1);
        dest.writeString(name2);
        dest.writeString(name3);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeString(imgUrl);
    }
}
