function Game(checkboard) {
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

Game.prototype.newGame = function () {
    $.get(this.host + ":" + this.port + "/chess/game/new", function (response) {

    });
};

Game.prototype.getGame = function (gameId) {
    $.get(this.host + ":" + this.port + "/chess/game/get/" + gameId, function (response) {

    });
};

Game.prototype.getAvailableMoves = function (coord) {
    $.get(this.host + ":" + this.port + "/chess/game/moves/" + gameId + "/" + coord, function (response) {

    });
};

Game.prototype.move = function (from, to) {
    $.get(this.host + ":" + this.port + "/chess/game/move/" + gameId + "/" + key + "/" + from + "/" + to, function (response) {

    });
};


