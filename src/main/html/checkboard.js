function Checkboard(canvasElementId) {
    this.canvas = $("#"+canvasElementId)[0];
    this.canvasSize = this.canvas.width;
    this.fieldSize = this.canvasSize / 8;
    this.canvas2D = this.canvas.getContext("2d");
    this.black = "#F7EBC3";
    this.white = "#9E7900";
    this.coordTranslator = new CoordTranslator(this);
}

Checkboard.prototype.getFigureChar = function(figureName) {
    switch (figureName) {
        case "WHITE_PAWN":
            return "♙";
        case "BLACK_PAWN":
            return "♟";
        case "BLACK_ROOK":
            return "♜";
        case "WHITE_ROOK":
            return "♖";
        case "BLACK_KNIGHT":
            return "♞";
        case "WHITE_KNIGHT":
            return "♘";
        case "BLACK_BISHOP":
            return "♝";
        case "WHITE_BISHOP":
            return "♗";
        case "BLACK_QUEEN":
            return "♛";
        case "WHITE_QUEEN":
            return "♕";
        case "BLACK_KING":
            return "♚";
        case "WHITE_KING":
            return "♔";
        case "NONE":
            return "";
        default :
            return "?";
    }
}

Checkboard.prototype.highlight = function(coord) {
    var col = coord[0];
    var row = coord[1];
    col = this.coordTranslator.colToIndex(col);
    row = this.coordTranslator.rowToIndex(row);
    var x = col * this.fieldSize;
    var y = row * this.fieldSize;
    this.canvas2D.strokeStyle = "green";
    this.canvas2D.strokeRect(x,y,this.fieldSize,this.fieldSize);
}

Checkboard.prototype.clearHighlights = function() {
    if(this.board) {
        this.drawBoard();
        this.drawFigures(this.board);
    }
}

Checkboard.prototype.drawFigures = function(board) {
    this.board = board;
    var cnt = 0;
    for (var x = 0; x < 8; x++) {
        var col = board[x];
        cnt++;
        for (var y = 7; y > -1; y--) {
            var tx = x * this.fieldSize;
            var ty = (7 - y) * this.fieldSize + this.fieldSize - 10;
            
            var stroke = cnt%2==0 ? this.white : this.black;

            this.canvas2D.font = "50px Arial";
            this.canvas2D.strokeStyle = stroke;
            this.canvas2D.strokeText(this.getFigureChar(board[x][y]), tx, ty);

            var fill = "white";
            if (col[y].lastIndexOf("BLACK", 0) == 0) {
                fill = "black"
            }
            this.canvas2D.fillStyle = fill;
            this.canvas2D.fillText(this.getFigureChar(board[x][y]), tx, ty);
        }
    }
}

Checkboard.prototype.drawBoard = function() {
    var cnt = 0;
    for (var row = 0; row < 8; row++) {
        cnt++;
        for (var column = 0; column < 8; column++) {
            // coordinates of the top-left corner
            var x = column * this.fieldSize;
            var y = row * this.fieldSize;
            var fill = cnt%2==0 ? this.white : this.black;
            cnt++;
            this.canvas2D.fillStyle = fill;
            this.canvas2D.fillRect(x, y, 50, 50);
        }
    }
}