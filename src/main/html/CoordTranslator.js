/**
 * Created by matej.ferenc on 01/07/2015.
 */

var coordTranslator = {
    translateRowCol: function(row, col) {
        return coordTranslator.translateCol(col) + coordTranslator.translateRow(row);
    },
    translateRow: function(row) {
        return 8 - row;
    },
    translateCol: function(col) {
        switch (col) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
            case 5:
                return "F";
            case 6:
                return "G";
            case 7:
                return "H";
        }
    }
}