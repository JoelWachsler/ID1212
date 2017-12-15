const blockS = 25

function Snake() {
  this.head = createVector(0, 0)
  this.body = [this.head, createVector(-25,0), createVector(25,0)]
  this.direction = RIGHT_ARROW

  this.render = function () {
    this.body.forEach(bodyPart => {
      rect(bodyPart.x, bodyPart.y, blockS, blockS)
    })
  }

  this.changeDirection = function (newDirection) {
    switch (newDirection) {
      case UP_ARROW:
        if (this.direction != DOWN_ARROW)
          this.direction = UP_ARROW
        break
      case RIGHT_ARROW:
        if (this.direction != LEFT_ARROW)
          this.direction = RIGHT_ARROW
        break
      case DOWN_ARROW:
        if (this.direction != UP_ARROW)
          this.direction = DOWN_ARROW
        break
      case LEFT_ARROW:
        if (this.direction != RIGHT_ARROW)
          this.direction = LEFT_ARROW
        break
    }
  }

  this.colliding = function (x, y, w, h) {
    const xx = this.head.x
    const yy = this.head.y

    return xx + blockS > x // x-coords
        && xx < x + w
        && yy + blockS > y // y-coords
        && yy < y + h
  }

  this.applyMove = function () {
    for (let i = this.body.length - 1; i > 0; i--) {
      const prev = this.body[i - 1]
      this.body[i] = createVector(prev.x, prev.y)
    }

    // Apply movement to head
    switch (this.direction) {
      case UP_ARROW:
        this.head.add(createVector(0, -blockS))
        break
      case RIGHT_ARROW:
        this.head.add(createVector(blockS, 0))
        break
      case DOWN_ARROW:
        this.head.add(createVector(0, blockS))
        break
      case LEFT_ARROW:
        this.head.add(createVector(-blockS, 0))
        break
    }
  }
}
