// @ts-check

import Snake from "./snake";
import Point from "./point";
import Food from "./food";
import { SIZE } from "../common/constants";

/**
 * Keeps track of a game room instance.
 */
export default class Game {
  constructor(controller) {
    this.controller = controller;
    this.snakes = [];
    this.food = [];
    this.gameArea = [];

    this.gameAreaWidth = 1000;
    this.gameAreaHeight = 1000;

    this.createGameArea();

    // Rate of which to update the game
    this.fps = 5;

    // Server fps
    setInterval(this.updateSnakes.bind(this), 1000 / this.fps);

    // Let's spawn some random food if there's less than 100 on the board.
    // Check every 5 seconds.
    setInterval(() => {
      if (this.food.length < 100) this.spawnFood();
    }, 5000);

    // Initial spawn
    this.spawnFood();
  }

  createGameArea() {
    // Let's create the game area
    for (let i = -this.gameAreaWidth; i <= this.gameAreaHeight; i += 25) {
      // Top
      this.gameArea.push(new Point(i, -this.gameAreaHeight));
      // Right
      this.gameArea.push(new Point(this.gameAreaWidth, i));
      // Bottom
      this.gameArea.push(new Point(i, this.gameAreaHeight));
      // Left
      this.gameArea.push(new Point(-this.gameAreaWidth, i));
    }
  }

  randomPoint(minX=-this.gameAreaWidth+25,
              maxX=this.gameAreaWidth-25,
              minY=-this.gameAreaHeight+25,
              maxY=this.gameAreaHeight-25) {

    let x = Math.floor(Math.random() * (maxX - minX) + minX);
    let y = Math.floor(Math.random() * (maxY - minY) + minY);

    // Only multiples of 25.
    x -= x % SIZE;
    y -= y % SIZE;

    return new Point(x, y);
  }

  spawnFood() {
    // Spawn some random food
    // 200 attempts
    for (let i = 0; i < 200; i++) {
      const newFood = new Food(this.randomPoint());


      // Do not spawn food on another piece of food.
      const isCollidingFood = this.food.findIndex(food => food.isColliding(newFood.point));
      if (isCollidingFood !== -1) continue;

      // Do not spawn food on a snake.
      let isCollidingSnake = false;
      for (let j = 0; j < this.snakes.length && !isCollidingSnake; j++) {
        const snake = this.snakes[j];
        if (newFood.isColliding(snake.head)) {
          isCollidingSnake = true;
          break;
        }

        for (let k = 0; k < snake.body.length; k++) {
          const body = snake.body[k];
          if (newFood.isColliding(body)) {
            isCollidingSnake = true;
            break;
          }
        }
      }

      if (isCollidingSnake) continue;

      this.food.push(newFood);
    }

    // Push the changes to all clients.
    this.updateFood();
  }

  updateGameArea() {
    this.controller.networkController.pushGameArea(this.gameArea);
  }

  updateFood() {
    this.controller.networkController.pushFood(this.food);
  }

  /**
   * Updates the game state for all players and pushes them.
   */
  updateSnakes() {
    const foodLen = this.food.length;

    // Move all players
    this.snakes.forEach(snake => {
      snake.applyMovement();

      // Check if this player is colliding with a piece of food.
      this.food = this.food.filter(food => {
        const colliding = snake.isColliding(food.point);

        if (colliding) snake.addBodyPart();

        return !colliding;
      });
    })

    // Check if a snake is colliding with itself or a wall.
    this.snakes = this.snakes.filter(snake => {
      const isEatingThemselves = snake.isCollidingWithSelf();
      if (isEatingThemselves) {
        this.controller.networkController.pushGameOver(snake.id, "ate yourself!");
        return false;
      }

      for (let i = 0; i < this.gameArea.length; i++) {
        if (snake.isColliding(this.gameArea[i])) {
          this.controller.networkController.pushGameOver(snake.id, "crashed into a wall!");

          return false;
        }
      }

      return true;
    });

    // Check if a snake is eating another snake
    for (let i = 0; i < this.snakes.length; i++) {
      const snake = this.snakes[i];

      for (let j = 0; j < this.snakes.length; j++) {
        if (i == j) continue;

        const anotherSnake = this.snakes[j];
        for (let k = 1; k < anotherSnake.body.length; k++) {
          if (snake.isColliding(anotherSnake.body[k])) {
            const bodyPartsEaten = anotherSnake.body.splice(k);

            // Add the pieces to the other snake
            bodyPartsEaten.forEach(bodyPart => snake.addBodyPart());
          }
        }
      }
    }

    // Only update the food if something happened to them
    if (this.food.length != foodLen) this.updateFood();

    // Broadcast them
    this.controller.networkController.pushSnakes(this.snakes);
  }

  /**
   * @param {number} id 
   * @param {number} newDirection 
   */
  updateMovement(id, newDirection) {
    const snake = this.snakes.find(snake => snake.id === id);
    snake.changeMovement(newDirection);
  }

  /**
   * Adds a player to the current game instance.
   *
   * @param {string} id
   */
  addPlayer(id) {
    // Spawn a new snake on a free position.
    let okSpawn = false;
    while (!okSpawn) {
      const spawnPoint = this.randomPoint(
        -this.gameAreaWidth+25,
        this.gameAreaWidth-500,
        -this.gameAreaHeight+25,
        this.gameAreaHeight-25);
      okSpawn = true;

      for (let i = 0; i < this.snakes.length; i++) {
        if (this.snakes[i].isColliding(spawnPoint)) {
          okSpawn = false;
          break;
        }
      }

      if (okSpawn)
        this.snakes.push(new Snake(id, spawnPoint));
    }
  }

  /**
   * Removes the player with the provided id from the list of players in this game.
   *
   * @param {*} id
   */
  removePlayer(id) {
    const index = this.snakes.findIndex(snake => snake.id === id);

    if (index === -1) return;

    this.snakes.splice(index, 1);
  }
}
