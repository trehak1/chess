var move = {
    from: null,
    to: null,
    clear: function() {
        move.from = null;
        move.to = null;
    },
    addCoord: function(coord) {
        if (move.from == null) {
            move.from = coord;
        } else if (move.to == null) {
            move.to = coord;
        } else {
            throw "wtf";
        }
    },
    isFull: function() {
        return move.from != null && move.to != null;
    }
}


function processCoord(coord) {
    move.addCoord(coord);
    if (move.isFull()) {
        var gameId = $("#game-id").html();
        var movement = move.from + "-" + move.to;

        $.get("http://localhost:3333/chess/game/move/" + gameId + "/" + movement, function (data) {
            drawCanvas(data);
        });
        
        move.clear();
    }
}