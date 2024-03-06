package com.alinakravckenkodev.crm.data;

import android.provider.BaseColumns;

public final class Main {

    private Main() {
    };

    public static final class CustomerForms implements BaseColumns {
        public final static String TABLE_NAME = "CustomerForms";
        public final static String _ID = BaseColumns._ID;
        public final static String full_name = "full_name";
        public final static String date_create = "date_create";
        public final static String date_finish = "date_finish";
        public final static String geo_create = "geo_create";
        public final static String geo_finish = "geo_finish";
        public final static String tsa_name = "tsa_name";
        public final static String tsa_address = "tsa_address";
        public final static String photo_base64 = "photo_base64";

    }


}
