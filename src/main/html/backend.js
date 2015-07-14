var Game = new function(canvas) {
    this.key = "no-key";
    this.canvas = canvas;
    this.host = "http://localhost";
    this.port = "3333";
};

Game.prototype.newGame = new function() {
    $.get(this.host+":"+this.port+"/chess/game/new", function (response) {
        
    });
};

Game.prototype.getGame = new function(gameId) {
    $.get(this.host+":"+this.port+"/chess/game/get/"+gameId, function (response) {
        
    });
};

Game.prototype.getAvailableMoves = new function(coord) {
    $.get(this.host+":"+this.port+"/chess/game/moves/"+gameId+"/"+coord, function (response) {
        
    });
};

Game.prototype.move = new function(from, to) {
    $.get(this.host+":"+this.port+"/chess/game/move/"+gameId+"/"+key+"/"+from+"/"+to, function (response) {
        
    });
};


