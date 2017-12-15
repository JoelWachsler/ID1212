import Snake from './snake'

/**
 * Handles a player instance.
 */
export default class Player {
  constructor(socket) {
    this.socket = socket
    this.id = socket.id
    this.snake = new Snake()
  }

  /**
   * @return An object with the body of the snake and id of the player.
   */
  get state() {
    return {
      id: this.id,
      body: this.snake.body
    }
  }

  updateMovement(newDirection) {
    this.snake.changeMovement(newDirection)
  }

  applyMovement() {
    this.snake.applyMovement()
  }
}
