"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _point = require("./point");

var _point2 = _interopRequireDefault(_point);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

// Snake block size
var SIZE = 25;

// Different directions
var UP = 0;
var RIGHT = 1;
var DOWN = 2;
var LEFT = 3;

/**
 * An instance of a snake.
 */

var Snake = function () {
  function Snake(id) {
    _classCallCheck(this, Snake);

    this.id = id;
    this.head = new _point2.default(0, 0);
    this.body = [this.head];
    this.direction = RIGHT;
  }

  /**
   * Moves the snake head and body to the current direction.
   */


  _createClass(Snake, [{
    key: "applyMovement",
    value: function applyMovement() {
      // Move the body parts
      for (var i = this.body.length - 1; i > 0; i--) {
        var _body = this.body[i - 1],
            x = _body.x,
            y = _body.y;

        this.body[i] = new _point2.default(x, y);
      }

      switch (this.direction) {
        case UP:
          this.head.y -= SIZE;
          break;
        case RIGHT:
          this.head.x += SIZE;
          break;
        case DOWN:
          this.head.y += SIZE;
          break;
        case LEFT:
          this.head.x -= SIZE;
          break;
      }
    }

    /**
     * Change the direction of the snake.
     * Checks if the current direction is not in the opposite way of the head.
     *
     * @param {*} newDirection
     */

  }, {
    key: "changeMovement",
    value: function changeMovement(newDirection) {
      var current = this.direction;

      switch (newDirection) {
        case UP:
          if (current != DOWN) this.direction = UP;
          break;
        case RIGHT:
          if (current != LEFT) this.direction = RIGHT;
          break;
        case DOWN:
          if (current != UP) this.direction = DOWN;
          break;
        case LEFT:
          if (current != RIGHT) this.direction = LEFT;
          break;
      }
    }

    /**
     * Checks if the snake head is colliding with the provided point.
     * 
     * @param {*} point The points to check if the head is colliding with.
     */

  }, {
    key: "isColliding",
    value: function isColliding(point) {
      var _head = this.head,
          x = _head.x,
          y = _head.y;


      return x === point.x && y === point.y;
    }
  }, {
    key: "isCollidingWithSelf",
    value: function isCollidingWithSelf() {
      var _head2 = this.head,
          x = _head2.x,
          y = _head2.y;


      for (var i = 1; i < this.body.length; i++) {
        if (this.body[i].x === x && this.body[i].y === y) return true;
      }

      return false;
    }
  }, {
    key: "addBodyPart",
    value: function addBodyPart() {
      this.body.push(new _point2.default(this.head.x, this.head.y));
    }
  }]);

  return Snake;
}();

exports.default = Snake;
//# sourceMappingURL=snake.js.map