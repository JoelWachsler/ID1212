"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _game = require("../game/game");

var _game2 = _interopRequireDefault(_game);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var GameController = function () {
  function GameController(controller) {
    _classCallCheck(this, GameController);

    this.controller = controller;
    this.game = new _game2.default(this.controller);
  }

  _createClass(GameController, [{
    key: "createPlayer",
    value: function createPlayer(id) {
      this.game.addPlayer(id);
    }
  }, {
    key: "removePlayer",
    value: function removePlayer(id) {
      this.game.removePlayer(id);
    }
  }, {
    key: "updateMovement",
    value: function updateMovement(id, newDirection) {
      this.game.updateMovement(id, newDirection);
    }
  }]);

  return GameController;
}();

exports.default = GameController;
//# sourceMappingURL=game_controller.js.map