function Game(checkboard, gameIdInputElementId) {
    this.gameIdElem = $("#"+gameIdInputElementId);
    this.key = "no-key";
    this.checkboard = checkboard;
    this.host = "http://localhost";
    this.port = "3333";
    this.coordTranslator = new CoordTranslator(this.checkboard);
    this.addCheckBoardListener();
};

Game.prototype.addCheckBoardListener = function () {
    var thisGame = this;
    $("#"+this.checkboard.canvas.id).click(function (e) { 
        var posX = e.pageX - $(this).position().left;
        var posY = e.pageY - $(this).position().top;
        var coords = thisGame.coordTranslator.clickToCoords(posX, posY);
        thisGame.checkboard.clearHighlights();
        thisGame.checkboard.highlight(coords);
    });
}

Game.prototype.isOk = function(response) {
    if(response.items.errorMsg) {
        console.log(response.items.errorMsg);
        return false;
    }
    return true;
}

Game.prototype.newGame = function () {
    var dis = this;
    $.get(this.host + ":" + this.port + "/chess/game/new", function (response) {
        if(dis.isOk(response)) {
            dis.session = response.items.session;
            dis.checkboard.drawBoard();
            dis.checkboard.drawFigures(dis.session.game.currentBoard.bitBoard.board)
        }
    });
};

Game.prototype.getGame = function () {
    var dis = this;
    var gameId = this.gameIdElem.val();
    $.get(this.host + ":" + this.port + "/chess/game/get/" + gameId, function (response) {
        if(dis.isOk(response)) {
            dis.session = response.items.session;
            dis.checkboard.drawBoard();
            dis.checkboard.drawFigures(dis.session.game.currentBoard.bitBoard.board)
        }
    });
};

Game.prototype.getAvailableMoves = function (coord) {
    $.get(this.host + ":" + this.port + "/chess/game/moves/" + gameId + "/" + coord, function (response) {
        console.log("response "+response);
    });
};

Game.prototype.move = function (from, to) {
    $.get(this.host + ":" + this.port + "/chess/game/move/" + gameId + "/" + key + "/" + from + "/" + to, function (response) {
        console.log("response "+response);
    });
};


