"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _colliding = _interopRequireDefault(require("./colliding"));
var _point = _interopRequireDefault(require("../game/point"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * Representation of a piece of food on the game board.
 */
class Food extends _colliding.default {
  /**
                                        * @param {Point} point 
                                        * @api public
                                        */
  constructor(point) {
    super();

    this.point = point;
  }

  /**
     * Checks if this piece of food is colliding with the provided one.
     * 
     * @param {Point} point 
     * @return {boolean} true if the food is colliding with the provided point else false.
     * @api public
     */
  isColliding(point) {
    return super.colliding(this.point, point);
  }

  /**
     * Check if this object is colliding with other points.
     * 
     * @param {Point[]} points The points to check this point with.
     * @api public
     */
  isCollidingWithPoints(points) {
    return super.collidingMany(this.point, points);
  }}exports.default = Food;
//# sourceMappingURL=food.js.map