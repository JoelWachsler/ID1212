// @ts-check
import Colliding from "./colliding";
import Point from "../game/point";

/**
 * Representation of a piece of food on the game board.
 */
export default class Food extends Colliding {
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
}
