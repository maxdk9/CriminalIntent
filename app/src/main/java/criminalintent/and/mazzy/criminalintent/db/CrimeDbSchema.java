package criminalintent.and.mazzy.criminalintent.db;

public class CrimeDbSchema {

    public static final String NAME = "Crimes";

    public static final class CrimeTable{


        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE="date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";


            public static final String SUSPECTID ="suspectid" ;
        }
    }

}
