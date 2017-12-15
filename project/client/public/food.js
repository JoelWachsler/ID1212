/**
 * Visual representation of a piece of food.
 */
function Food({ x, y }) {
  this.x = x;
  this.y = y;

  this.render = function() {
    rect(this.x, this.y, blockS, blockS);
  };
}
