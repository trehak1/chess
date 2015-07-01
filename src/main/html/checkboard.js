var canvasSize = 400;
var fieldSize = canvasSize/8;

function translatePixels(x) {
    return x/fieldSize;
}

function translateRowCol(row,col) {
    function translateRow(row) {
        return 8-row;
    };

    function translateCol(col) {
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

    var translated = translateRow(row)+"-"+translateCol(col);

}

function drawCanvas(game) {
    var canvas = document.getElementById("checkerboard");
    var context2D = canvas.getContext("2d");
    var board = game.currentBoard.board;

    for (var row = 0; row < 8; row++) {
        for (var column = 0; column < 8; column++) {
            // coordinates of the top-left corner
            var x = column * fieldSize;
            var y = row * fieldSize;
            var fill;

            if (row % 2 == 1) {
                if (column % 2 == 0) {
                    fill = "black";
                }  else {
                    fill = "white";
                }
            } else {
                if (column % 2 == 0) {
                    fill = "white";
                }
                else {
                    fill = "black";
                }
            }
            context2D.fillStyle = fill;
            context2D.fillRect(x, y, 50, 50);
        }
    }

    var figure = function (f) {
        switch(f) {
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
    };

    for(var x =0; x < 8; x++) {
        var col = board[x];
        for(var y = 7; y > -1; y--) {
            var tx = x*fieldSize;
            var ty = (7-y)*fieldSize + fieldSize;
            context2D.font="20px Arial";
            context2D.fillText(figure(col[y]),tx,ty);
        }
    }

}