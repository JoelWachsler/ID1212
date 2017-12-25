// @ts-check
/**
 * Handles the game instance.
 *
 * @param {Controller} controller
 */
function Game(controller) {
  this.controller = controller;
}

/**
 * Define setters and getters.
 */
Game.prototype = {
  get gameOverReason() {
    if (this.gameOver === void 0) {
      return "Press \"p\" to play!";
    } else {
      const { reason } = this.gameOver;
      return `You ${reason}\nPress "p" to play again!`;
    }
  },
  get isPlaying() {
    return this.snake !== void(0);
  },
  set snakes(snakes) {
    this.snake = snakes.find(snake => snake.id === this.id);
    this._snakes = snakes.map(snake => new Snake(snake.body, snake.id));
  },
  get snakes() {
    return this._snakes || [];
  },
  set food(food) {
    this._food = food.map(food => new Food(food.point, food.special));
  },
  get food() {
    return this._food || [];
  },
  set gameArea(gameArea) {
    this._gameArea = new GameArea(gameArea);
  },
  get gameArea() {
    return this._gameArea || new GameArea([]);
  },
  set gameOver(gameOver) {
    if (gameOver.id === this.id) this._gameOver = gameOver;
  },
  get gameOver() {
    return this._gameOver;
  },
  set powerUp(powerUp) {
    this._powerUp = powerUp;
  },
  get powerUp() {
    return this._powerUp || [];
  },
  /**
   * Calculates the score of each player.
   */
  get scoreBoard() {
    let snakeCounter = 1;
    return this.snakes
      .map(snake => {
        return {
          name: snake.id === this.id ? "You" : `Snake${snakeCounter++}`,
          score: snake.body.length
        };
      })
      .sort((snake1, snake2) => snake2.score - snake1.score);
  }
};
