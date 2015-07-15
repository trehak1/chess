/**
 * Created by matej.ferenc on 01/07/2015.
 */

function CoordTranslator(checkboard) {
    this.checkboard = checkboard;
}

CoordTranslator.prototype.translatePixels = function(x) {
    return Math.floor(x/this.checkboard.fieldSize);
};

CoordTranslator.prototype.clickToCoords = function(x,y) {
    var res = this.indexToCol(this.translatePixels(x))+this.indexToRow(this.translatePixels(y));
    console.log("click "+x+","+y+" translated to "+res);
    return res;
}

CoordTranslator.prototype.rowToIndex = function(row) {
    return 8 - row;
}

CoordTranslator.prototype.colToIndex = function(col) {
    return "ABCDEFGH".lastIndexOf(col);
}

CoordTranslator.prototype.indexToRow = function(row) {
    return 8 - row;
}

CoordTranslator.prototype.indexToCol = function(col) {
    return "ABCDEFGH"[col];
}
