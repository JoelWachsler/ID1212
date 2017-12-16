"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _game_broadcast_handler = require("../net/game_broadcast_handler");

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var NetworkController = function () {
  function NetworkController(controller, io) {
    _classCallCheck(this, NetworkController);

    this.controller = controller;
    this.io = io;
  }

  _createClass(NetworkController, [{
    key: "pushInitialData",
    value: function pushInitialData(socket) {
      // Data to push
      var _controller$gameContr = this.controller.gameController.game,
          snakes = _controller$gameContr.snakes,
          food = _controller$gameContr.food,
          gameArea = _controller$gameContr.gameArea;


      (0, _game_broadcast_handler.pushSnakes)(socket, snakes);
      (0, _game_broadcast_handler.pushFood)(socket, food);
      (0, _game_broadcast_handler.pushGameArea)(socket, gameArea);
    }
  }, {
    key: "pushGameArea",
    value: function pushGameArea(gameArea) {
      (0, _game_broadcast_handler.pushGameArea)(this.io, gameArea);
    }
  }, {
    key: "pushFood",
    value: function pushFood(food) {
      (0, _game_broadcast_handler.pushFood)(this.io, food);
    }
  }, {
    key: "pushSnakes",
    value: function pushSnakes(snakes) {
      (0, _game_broadcast_handler.pushSnakes)(this.io, snakes);
    }
  }]);

  return NetworkController;
}();

exports.default = NetworkController;
//# sourceMappingURL=network_controller.js.map