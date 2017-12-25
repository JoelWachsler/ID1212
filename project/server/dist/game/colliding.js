"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _point = _interopRequireDefault(require("./point"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * Abstract class which defines common logic for the different game items.
 */
class Colliding {
  /**
                  * Checks if the two points are colliding.
                  * 
                  * @param {Point} point1
                  * @param {Point} point2
                  * @return {boolean} true if point1 is colliding with point2 else false.
                  * @api protected
                  */
  colliding(point1, point2) {
    return point1.x === point2.x &&
    point1.y === point2.y;
  }}exports.default = Colliding;
//# sourceMappingURL=colliding.js.map