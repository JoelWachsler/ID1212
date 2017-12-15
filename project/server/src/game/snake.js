import Point from './point'
import {
  UP,
  RIGHT,
  DOWN,
  LEFT
} from './direction'

// Snake block size
const SIZE = 25

/**
 * An instance of a snake.
 */
export default class Snake {
  constructor() {
    this.head = new Point(0,0)
    this.body = [this.head]
    this.direction = RIGHT
  }

  /**
   * Moves the snake head and body to the current direction.
   */
  applyMovement() {
    switch (this.direction) {
      case UP:
        this.head.x -= SIZE
        break
      case RIGHT:
        this.head.x += SIZE
        break
      case DOWN:
        this.head.y += SIZE
        break
      case LEFT:
        this.head.x -= SIZE
        break
    }
  }

  /**
   * Change the direction of the snake.
   * Checks if the current direction is not in the opposite way of the head.
   * 
   * @param {*} newDirection 
   */
  changeMovement(newDirection) {
    const current = this.direction

    switch (newDirection) {
      case UP:
        if (current != DOWN) this.direction = UP
        break
      case RIGHT:
        if (current != LEFT) this.direction = RIGHT
        break
      case DOWN:
        if (current != UP) this.direction = DOWN
        break
      case LEFT:
        if (current != RIGHT) this.direction = LEFT
        break
    }
  }
}