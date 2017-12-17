/**
 * Abstract class which defines common logic for the different game items.
 */
export default class Colliding {
  /**
   * Checks if the two points are colliding.
   * 
   * @param {Point} point1
   * @param {Point} point2
   */
  colliding(point1, point2) {
    return point1.x === point2.x
        && point1.y === point2.y;
  }
}