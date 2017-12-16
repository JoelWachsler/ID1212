"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _snake = require("./snake");

var _snake2 = _interopRequireDefault(_snake);

var _point = require("./point");

var _point2 = _interopRequireDefault(_point);

var _food = require("./food");

var _food2 = _interopRequireDefault(_food);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Keeps track of a game room instance.
 */
var Game = function () {
  function Game(controller) {
    _classCallCheck(this, Game);

    this.controller = controller;
    this.snakes = [];
    this.food = [];
    this.gameArea = [];

    // Let's create the game area
    for (var i = -1000; i <= 1000; i += 25) {
      // Top
      this.gameArea.push(new _point2.default(i, -1000));
      // Right
      this.gameArea.push(new _point2.default(1000, i));
      // Bottom
      this.gameArea.push(new _point2.default(i, 1000));
      // Left
      this.gameArea.push(new _point2.default(-1000, i));
    }

    // Spawn some random food
    for (var _i = 0; _i < 200; _i++) {
      var x = Math.floor(Math.random() * 80 - 50) * 25;
      var y = Math.floor(Math.random() * 80 - 50) * 25;
      this.food.push(new _food2.default(new _point2.default(x, y)));
    }

    // Rate of which to update the game
    this.fps = 1;

    // Server fps
    setInterval(this.updateSnakes.bind(this), 1000 / this.fps);
  }

  _createClass(Game, [{
    key: "updateGameArea",
    value: function updateGameArea() {
      this.controller.networkController.pushGameArea(this.gameArea);
    }
  }, {
    key: "updateFood",
    value: function updateFood() {
      this.controller.networkController.pushFood(this.food);
    }

    /**
     * Updates the game state for all players and pushes them.
     */

  }, {
    key: "updateSnakes",
    value: function updateSnakes() {
      var _this = this;

      var foodLen = this.food.length;

      // Move all players
      this.snakes.filter(function (snake) {
        snake.applyMovement();

        // Check if this player is colliding with a piece of food.
        _this.food = _this.food.filter(function (food) {
          var colliding = snake.isColliding(food.point);

          if (colliding) snake.addBodyPart();

          return !colliding;
        });

        // Check if the current snake is colliding with itself
        if (snake.isCollidingWithSelf()) {
          console.log('Colliding!');
          _this.controller.networkController.pushGameOver();

          return false;
        }

        return true;
      });

      if (this.food.length != foodLen) this.updateFood();

      // Broadcast them
      this.controller.networkController.pushSnakes(this.snakes);
    }
  }, {
    key: "updateMovement",
    value: function updateMovement(id, newDirection) {
      var snake = this.snakes.find(function (snake) {
        return snake.id === id;
      });
      snake.changeMovement(newDirection);
    }

    /**
     * Adds a player to the current game instance.
     *
     * @param {*} socket
     */

  }, {
    key: "addPlayer",
    value: function addPlayer(id) {
      this.snakes.push(new _snake2.default(id));
    }

    /**
     * Removes the player with the provided id from the list of players in this game.
     *
     * @param {*} id
     */

  }, {
    key: "removePlayer",
    value: function removePlayer(id) {
      var index = this.snakes.findIndex(function (snake) {
        return snake.id === id;
      });

      this.snakes.splice(index, 1);
    }
  }]);

  return Game;
}();

exports.default = Game;
//# sourceMappingURL=game.js.map