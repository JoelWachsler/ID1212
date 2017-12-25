// @ts-check
/**
 * Representation of a piece of food.
 */
function Food({ x, y }, special = false) {
  this.special = special;
  this.x = x;
  this.y = y;
}
