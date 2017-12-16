import Point from "./point";

// Snake block size
const SIZE = 25;

// Different directions
const UP = 0;
const RIGHT = 1;
const DOWN = 2;
const LEFT = 3;

/**
 * An instance of a snake.
 */
export default class Snake {
  constructor(id) {
    this.id = id;
    this.head = new Point(0, 0);
    this.body = [this.head];
    this.direction = RIGHT;

    // Body parts to be added on the next tick.
    this.bodyPartsToAdd = [];
  }

  /**
   * Moves the snake head and body to the current direction.
   */
  applyMovement() {
    // Add new body parts if there are any
    if (this.bodyPartsToAdd.length > 0)
      this.body.push(this.bodyPartsToAdd.pop())

    // Move the body parts
    for (let i = this.body.length - 1; i > 0; i--) {
      const { x, y } = this.body[i - 1];
      this.body[i] = new Point(x, y);
    }

    switch (this.direction) {
      case UP:
        this.head.y -= SIZE;
        break;
      case RIGHT:
        this.head.x += SIZE;
        break;
      case DOWN:
        this.head.y += SIZE;
        break;
      case LEFT:
        this.head.x -= SIZE;
        break;
    }
  }

  /**
   * Change the direction of the snake.
   * Checks if the current direction is not in the opposite way of the head.
   *
   * @param {*} newDirection
   */
  changeMovement(newDirection) {
    const current = this.direction;

    switch (newDirection) {
      case UP:
        if (current != DOWN) this.direction = UP;
        break;
      case RIGHT:
        if (current != LEFT) this.direction = RIGHT;
        break;
      case DOWN:
        if (current != UP) this.direction = DOWN;
        break;
      case LEFT:
        if (current != RIGHT) this.direction = LEFT;
        break;
    }
  }

  /**
   * Checks if the snake head is colliding with the provided point.
   * 
   * @param {*} point The points to check if the head is colliding with.
   */
  isColliding(point) {
    const { x, y } = this.head;

    return x === point.x && y === point.y;
  }

  /**
   * Check if this snake is eating itself.
   */
  isCollidingWithSelf() {
    const { x, y } = this.head;

    for (let i = 1; i < this.body.length; i++) {
      if (this.body[i].x === x && this.body[i].y === y)
        return true
    }

    return false
  }

  addBodyPart() {
    this.bodyPartsToAdd.push(new Point(this.head.x, this.head.y));
  }
}
