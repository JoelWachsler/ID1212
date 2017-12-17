// @ts-check
import Point from "./point";

/**
 * Abstract class which defines common logic for the different game items.
 */
export default class Colliding {
  /**
   * Checks if the two points are colliding.
   * 
   * @param {Point} point1
   * @param {Point} point2
   * @return {boolean} true if point1 is colliding with point2 else false.
   * @api protected
   */
  colliding(point1, point2) {
    return point1.x === point2.x
        && point1.y === point2.y;
  }
}