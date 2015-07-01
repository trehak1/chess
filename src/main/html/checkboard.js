var checkboard = {
    canvasSize: 400,
    fieldSize: 50,//checkboard.canvasSize / 8,
    canvas: null,
    context2D: null,
    board: null,
    possibleMoves: null,

    translatePixels: function(x) {
        return Math.floor(x / checkboard.fieldSize);
    },

    figure: function(f) {
        switch (f) {
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
    },

    drawCanvas: function() {
        checkboard.drawSquares();
        checkboard.drawPieces();
    },

    drawSquares: function() {
        for (var row = 0; row < 8; row++) {
            for (var column = 0; column < 8; column++) {
                // coordinates of the top-left corner
                var x = column * checkboard.fieldSize;
                var y = row * checkboard.fieldSize;
                var fill;

                if (row % 2 == 1) {
                    if (column % 2 == 0) {
                        fill = "black";
                    } else {
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
                if (checkboard.possibleMoves != null) {
                    for (var i = 0; i < checkboard.possibleMoves.length; i++) {
                        if (coordTranslator.translateRowCol(row, column) == checkboard.possibleMoves[i]) {
                            fill = "red";
                            break;
                        }
                    };
                }
                
                checkboard.context2D.fillStyle = fill;
                checkboard.context2D.fillRect(x, y, 50, 50);
            }
        }
    },
    
    drawPieces: function() {
        for (var x = 0; x < 8; x++) {
            var col = checkboard.board[x];
            for (var y = 7; y > -1; y--) {
                var tx = x * checkboard.fieldSize;
                var ty = (7 - y) * checkboard.fieldSize + checkboard.fieldSize - 10;

                var stroke;

                if (x % 2 == 1) {
                    if (y % 2 == 0) {
                        stroke = "black";
                    } else {
                        stroke = "white";
                    }
                } else {
                    if (y % 2 == 0) {
                        stroke = "white";
                    }
                    else {
                        stroke = "black";
                    }
                }

                checkboard.context2D.font = "50px Arial";
                checkboard.context2D.strokeStyle = stroke;
                checkboard.context2D.strokeText(checkboard.figure(col[y]), tx, ty);

                var fill = "white";
                if (col[y].lastIndexOf("BLACK", 0) == 0) {
                    fill = "black"
                }
                checkboard.context2D.fillStyle = fill;
                checkboard.context2D.fillText(checkboard.figure(col[y]), tx, ty);
            }
        }
    }
}
