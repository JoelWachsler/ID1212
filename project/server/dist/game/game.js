"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _snake = _interopRequireDefault(require("./snake"));
var _point = _interopRequireDefault(require("./point"));
var _food = _interopRequireDefault(require("./food"));
var _constants = require("../common/constants");
var _controller = _interopRequireDefault(require("../controller/controller"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * Keeps track of a game room instance.
 */
class Game {
  /**
             * @param {Controller} controller 
             * @api public
             */
  constructor(controller) {
    this.controller = controller;
    this.snakes = [];
    this.food = [];
    this.gameArea = [];

    // Define the game area.
    this.gameAreaWidth = 500;
    this.gameAreaHeight = 500;

    this.createGameArea();

    // Rate of which to update the game
    this.fps = 20;

    // Server fps
    setInterval(this.updateSnakes.bind(this), 1000 / this.fps);

    // Let's spawn some random food if there's less than 100 on the board.
    // Check every 5 seconds.
    setInterval(() => {
      if (this.food.length < 1000) this.spawnFood(100);
    }, 1000);

    // Make a piece of food special
    setInterval(() => {
      for (let i = 0; i < 3; i++)
      this.food[Math.floor(Math.random() * this.food.length) + 0].special = true;

      this.updateFood();
    }, 10000);

    // Initial spawn
    this.spawnFood();
  }

  /**
     * Creates points for the game area.
     * 
     * @api private
     */
  createGameArea() {
    // Let's create the game area
    for (let i = -this.gameAreaWidth; i <= this.gameAreaHeight; i += 25) {
      // Top
      this.gameArea.push(new _point.default(i, -this.gameAreaHeight));
      // Right
      this.gameArea.push(new _point.default(this.gameAreaWidth, i));
      // Bottom
      this.gameArea.push(new _point.default(i, this.gameAreaHeight));
      // Left
      this.gameArea.push(new _point.default(-this.gameAreaWidth, i));
    }
  }

  /**
     * Generates a random point depending on the input.
     * 
     * @param {number} minX 
     * @param {number} maxX 
     * @param {number} minY 
     * @param {number} maxY 
     * @return {Point} point
     * @api private
     */
  randomPoint(minX = -this.gameAreaWidth + 25,
  maxX = this.gameAreaWidth - 25,
  minY = -this.gameAreaHeight + 25,
  maxY = this.gameAreaHeight - 25) {

    let x = Math.floor(Math.random() * (maxX - minX) + minX);
    let y = Math.floor(Math.random() * (maxY - minY) + minY);

    // Only multiples of 25.
    x -= x % _constants.SIZE;
    y -= y % _constants.SIZE;

    return new _point.default(x, y);
  }

  /**
     * @api private
     */
  spawnFood(maxFoodToSpawn) {
    // Spawn some random food
    // 200 attempts
    for (let i = 0; i < maxFoodToSpawn; i++) {
      const newFood = new _food.default(this.randomPoint());

      // Do not spawn food on another piece of food.
      if (newFood.isCollidingWithPoints(this.food.map(food => food.point))) continue;

      // Do not spawn food on a snake.
      const snakePoints = this.snakes.map(snake => snake.body);
      const snakePointsArray = [].concat.apply([], snakePoints);
      if (newFood.isCollidingWithPoints(snakePointsArray)) continue;

      this.food.push(newFood);
    }

    // Push the changes to all clients.
    this.updateFood();
  }

  /**
     * @api private
     */
  updateGameArea() {
    this.controller.networkController.pushGameArea(this.gameArea);
  }

  /**
     * @api private
     */
  updateFood() {
    this.controller.networkController.pushFood(this.food);
  }

  /**
     * Updates the game state for all players.
     * 
     * @api private
     */
  updateSnakes() {
    const foodLen = this.food.length;

    // Move all players
    this.snakes.forEach(snake => snake.applyMovement());

    // Check if this player is colliding with a piece of food.
    this.snakes.forEach(snake => snake.isCollidingWithFood(this.food));

    // Check if a snake is eating itself.
    this.snakes = this.snakes.filter(snake => {
      const colliding = snake.isCollidingWithPoints(snake.body.slice(1));

      if (colliding) this.controller.networkController.pushGameOver(snake.id, "ate yourself!");

      return !colliding;
    });

    // Check if a snake is colliding a wall.
    this.snakes = this.snakes.filter(snake => {
      const colliding = snake.isCollidingWithPoints(this.gameArea);

      if (colliding) this.controller.networkController.pushGameOver(snake.id, "crashed into a wall!");

      return !colliding;
    });

    // Check if a snake is crashing into another snake.
    // Going from the back so we don't mess anything up if a snake is removed.
    for (let i = this.snakes.length - 1; i >= 0; i--) {
      const snake = this.snakes[i];
      let match = false;
      for (let j = 0; j < this.snakes.length && !match; j++) {
        if (i === j) continue;

        const cmpSnake = this.snakes[j].body;

        if (snake.isCollidingWithPoints(cmpSnake)) {
          match = true;
          this.controller.networkController.pushGameOver(snake.id, "crashed into another snake!");
        }
      }

      // Removing the snake who collided.
      if (match) this.snakes.splice(i, 1);
    }

    // Only update the food if something happened to them
    if (this.food.length != foodLen) this.updateFood();

    // Broadcast them
    this.controller.networkController.pushSnakes(this.snakes);
  }

  /**
     * @param {string} id 
     * @param {number} newDirection 
     * @api private
     */
  updateMovement(id, newDirection) {
    const snake = this.snakes.find(snake => snake.id === id);

    if (snake !== void 0) snake.changeMovement(newDirection);
  }

  /**
     * Adds a player to the current game instance.
     *
     * @param {string} id
     * @api public
     */
  addPlayer(id) {
    // Check if the player is already playing.
    if (this.snakes.find(snake => snake.id === id) !== void 0) return;

    // Spawn a new snake on a free point.
    let okSpawn = false;
    while (!okSpawn) {
      const spawnPoint = this.randomPoint(
      -this.gameAreaWidth + 25,
      this.gameAreaWidth - 500,
      -this.gameAreaHeight + 25,
      this.gameAreaHeight - 25);
      okSpawn = true;

      for (let i = 0; i < this.snakes.length; i++) {
        if (this.snakes[i].isColliding(spawnPoint)) {
          okSpawn = false;
          break;
        }
      }

      if (okSpawn) this.snakes.push(new _snake.default(id, spawnPoint));
    }
  }

  /**
     * Removes the player with the provided id from the list of players in this game.
     *
     * @param {string} id
     * @api public
     */
  removePlayer(id) {
    const index = this.snakes.findIndex(snake => snake.id === id);

    if (index === -1) return;

    this.snakes.splice(index, 1);
  }}exports.default = Game;
//# sourceMappingURL=game.js.map