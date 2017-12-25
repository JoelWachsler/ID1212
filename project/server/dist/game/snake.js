"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _point = _interopRequireDefault(require("./point"));
var _colliding = _interopRequireDefault(require("./colliding"));
var _constants = require("../common/constants");function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

// Different directions
const UP = 0;
const RIGHT = 1;
const DOWN = 2;
const LEFT = 3;

/**
                 * An instance of a snake.
                 */
class Snake extends _colliding.default {
  /**
                                         * @param {string} id 
                                         * @param {Point} point 
                                         * @api public
                                         */
  constructor(id, point) {
    super();

    this.id = id;
    this.head = point;
    this.body = [this.head];
    this.direction = RIGHT;

    // Body parts to be added on the next tick.
    this.bodyPartsToAdd = [];
  }

  /**
     * Moves the snake head and body to the current direction.
     * 
     * @api public
     */
  applyMovement() {
    // Add new body parts if there are any
    if (this.bodyPartsToAdd.length > 0)
    this.body.push(this.bodyPartsToAdd.pop());

    // Move the body parts
    for (let i = this.body.length - 1; i > 0; i--) {
      const { x, y } = this.body[i - 1];
      this.body[i] = new _point.default(x, y);
    }

    switch (this.direction) {
      case UP:
        this.head.y -= _constants.SIZE;
        break;
      case RIGHT:
        this.head.x += _constants.SIZE;
        break;
      case DOWN:
        this.head.y += _constants.SIZE;
        break;
      case LEFT:
        this.head.x -= _constants.SIZE;
        break;}

  }

  /**
     * Change the direction of the snake.
     * Checks if the current direction is not in the opposite way of the head.
     *
     * @param {number} newDirection
     * @api public
     */
  changeMovement(newDirection) {
    const current = this.direction;

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
        break;}

  }

  /**
     * Checks if the snake head is colliding with the provided point.
     * 
     * @param {Point} point The points to check if the head is colliding with.
     * @api public
     */
  isColliding(point) {
    return super.colliding(this.head, point);
  }

  /**
     * Check if this snake is eating itself.
     * 
     * @api public
     */
  isCollidingWithSelf() {
    for (let i = 1; i < this.body.length; i++) {
      if (super.colliding(this.head, this.body[i]))
      return true;
    }

    return false;
  }

  /**
     * Adds a body part the next game tick.
     * 
     * @api public
     */
  addBodyPart() {
    this.bodyPartsToAdd.push(new _point.default(this.head.x, this.head.y));
  }}exports.default = Snake;
//# sourceMappingURL=snake.js.map