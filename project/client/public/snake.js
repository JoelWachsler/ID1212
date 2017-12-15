const blockS = 25

function Snake(body) {
  this.body = body.map(part => createVector(part.x, part.y))

  this.render = function () {
    this.body.forEach(bodyPart => {
      rect(bodyPart.x, bodyPart.y, blockS, blockS)
    })
  }

  this.colliding = function (x, y, w, h) {
    const xx = this.head.x
    const yy = this.head.y

    return xx + blockS > x // x-coords
        && xx < x + w
        && yy + blockS > y // y-coords
        && yy < y + h
  }
}
