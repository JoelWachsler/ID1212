// @ts-check

import Colliding from "./colliding";

/**
 * Representation of a piece of food on the game board.
 */
export default class Food extends Colliding {
  constructor(point) {
    super();

    this.point = point;
  }

  /**
   * Checks if this piece of food is colliding with the provided one.
   * 
   * @param {Point} point 
   */
  isColliding(point) {
    return super.colliding(this.point, point);
  }
}
