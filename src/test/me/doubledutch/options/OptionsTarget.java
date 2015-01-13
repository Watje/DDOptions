package me.doubledutch.options;

public class OptionsTarget {
    public String public_s;

    public void setPublicS(String str) {
        public_s = str;
    }

    public boolean foo = false;

    public int public_i;
    private int private_i;

    public int getPrivateI() {
        return private_i;
    }
}