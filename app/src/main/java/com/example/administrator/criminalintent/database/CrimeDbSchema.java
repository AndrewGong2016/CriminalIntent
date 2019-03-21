package com.example.administrator.criminalintent.database;

public class CrimeDbSchema {



    public static final class CrimeTable {
        //part 1: name
        public static final String NAME = "crimes";

        //part 2: columes
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";

        }

    }

}
