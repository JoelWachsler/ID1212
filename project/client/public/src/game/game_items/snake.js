/**
 * Representation of a snake.
 * 
 * @param {Point[]} body
 * @param {string} id
 */
function Snake(body, id) {
  this.body = body.map(part => createVector(part.x, part.y));
  this.head = this.body[0];
  this.id = id;
  this.body.splice(0, 1);
}
