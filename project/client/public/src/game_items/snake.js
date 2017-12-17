/**
 * Visual representation of a snake.
 *
 * @param {*} body
 */
function Snake(body, id) {
  this.body = body.map(part => createVector(part.x, part.y));
  this.head = this.body[0];
  this.id = id;
  this.body.splice(0, 1);

  this.render = function() {
    fill(104,105,99);
    this.body.forEach(bodyPart => {
      rect(bodyPart.x, bodyPart.y, blockS, blockS);
    });

    fill(138,162,158);
    rect(this.head.x, this.head.y, blockS, blockS);
  };
}
