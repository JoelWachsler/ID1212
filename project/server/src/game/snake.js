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
  }

  /**
   * Moves the snake head and body to the current direction.
   */
  applyMovement() {
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

  isColliding(point) {
    return (
      this.body.findIndex(part => part.x == point.x && part.y == point.y) !== -1
    );
  }

  addBodyPart() {
    this.body.push(new Point(this.head.x, this.head.y));
  }
}
