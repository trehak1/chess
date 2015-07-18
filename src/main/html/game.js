function Game(checkboard, gameIdInputElementId) {
    this.gameIdElem = $("#" + gameIdInputElementId);
    this.key = "no-key";
    this.checkboard = checkboard;
    this.host = "http://localhost";
    this.port = "3333";
    this.coordTranslator = new CoordTranslator(this.checkboard);
    this.addCheckBoardListener();
    this.gameId = null;
    this.selectedFigure = null;
    this.key = "0123456789abcdef";
};

Game.prototype.addCheckBoardListener = function () {
    var thisGame = this;
    $("#" + this.checkboard.canvas.id).click(function (e) {
        var posX = e.pageX - $(this).position().left;
        var posY = e.pageY - $(this).position().top;
        var coords = thisGame.coordTranslator.clickToCoords(posX, posY);
        thisGame.processClick(coords);
    });
}

Game.prototype.processClick = function (coords) {
    console.log("processing click to " + coords);
    this.checkboard.clearHighlights();
    if (this.isMyPieceClicked(coords)) {
        this.selectedFigure = coords;
        this.checkboard.highlight(coords);
        this.highlightPossibleMoves(coords);
    } else {
        if (this.selectedFigure != null) {
            if (coords == this.selectedFigure) {
                this.selectedFigure = null;
            } else {
                this.move(this.selectedFigure, coords);
            }
        }
        this.selectedFigure = null;
    }
}

Game.prototype.isMyPieceClicked = function (coords) {
    if (this.session) {
        var figure = this.checkboard.getFigureOn(coords);
        return figure.lastIndexOf(this.session.game.currentBoard.playerOnTurn) > -1;
    }
    return false;
}

Game.prototype.isOk = function (response) {
    if (response.items.errorMsg) {
        console.log(response.items.errorMsg);
        return false;
    }
    return true;
}

Game.prototype.newGame = function () {
    var dis = this;
    $.get(this.host + ":" + this.port + "/chess/game/new", function (response) {
        if (dis.isOk(response)) {
            dis.gameId = response.items.session.id;
            dis.gameIdElem.val(dis.gameId);
            dis.session = response.items.session;
            dis.refresh();
        }
    });
};

Game.prototype.refresh = function () {
    if (this.session) {
        this.checkboard.drawBoard();
        this.checkboard.drawFigures(this.session.game.currentBoard.bitBoard.board)
    }
}

Game.prototype.getGame = function () {
    var dis = this;
    this.gameId = this.gameIdElem.val();
    $.get(this.host + ":" + this.port + "/chess/game/get/" + this.gameId, function (response) {
        if (dis.isOk(response)) {
            dis.session = response.items.session;
            dis.refresh();
        }
    });
};

Game.prototype.highlightPossibleMoves = function (coord) {
    var dis = this;
    $.get(this.host + ":" + this.port + "/chess/game/moves/" + this.gameId + "/" + coord, function (response) {
        if (dis.isOk(response)) {
            for (var i = 0; i < response.items.availableMoves.length; i++) {
                var move = response.items.availableMoves[i];
                dis.checkboard.highlight(move.to);
            }
        }
    });
};

Game.prototype.move = function (from, to) {
    var dis = this;
    $.get(this.host + ":" + this.port + "/chess/game/move/" + this.gameId + "/" + this.key + "/" + from + "/" + to, function (response) {
        if (dis.isOk(response)) {
            dis.session = response.items.session;
            dis.refresh();
        }
    });
};


