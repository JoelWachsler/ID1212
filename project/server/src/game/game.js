import Snake from "./snake";
import Point from "./point";
import Food from "./food";

/**
 * Keeps track of a game room instance.
 */
export default class Game {
  constructor(controller) {
    this.controller = controller;
    this.snakes = [];
    this.food = [];
    this.gameArea = [];

    // Let's create the game area
    for (let i = -1000; i <= 1000; i += 25) {
      // Top
      this.gameArea.push(new Point(i, -1000));
      // Right
      this.gameArea.push(new Point(1000, i));
      // Bottom
      this.gameArea.push(new Point(i, 1000));
      // Left
      this.gameArea.push(new Point(-1000, i));
    }

    // Spawn some random food
    for (let i = 0; i < 200; i++) {
      const x = Math.floor(Math.random() * 80 - 50) * 25;
      const y = Math.floor(Math.random() * 80 - 50) * 25;
      this.food.push(new Food(new Point(x, y)));
    }

    // Rate of which to update the game
    this.fps = 10;

    // Server fps
    setInterval(this.updateSnakes.bind(this), 1000 / this.fps);
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
    // Go from the back so we don't miss a player if we remove them.
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

      this.gameArea.forEach(wall => {
        const isCrashingIntoAWall = snake.isColliding(wall);

        if (isCrashingIntoAWall) {
          this.controller.networkController.pushGameOver(snake.id, "crashed into a wall!");
          return false;
        }
      });

      return true;
    });

    // Only update the food if something happened to them
    if (this.food.length != foodLen) this.updateFood();

    // Broadcast them
    this.controller.networkController.pushSnakes(this.snakes);
  }

  updateMovement(id, newDirection) {
    const snake = this.snakes.find(snake => snake.id === id);
    snake.changeMovement(newDirection);
  }

  /**
   * Adds a player to the current game instance.
   *
   * @param {*} socket
   */
  addPlayer(id) {
    this.snakes.push(new Snake(id));
  }

  /**
   * Removes the player with the provided id from the list of players in this game.
   *
   * @param {*} id
   */
  removePlayer(id) {
    const index = this.snakes.findIndex(snake => snake.id === id);

    this.snakes.splice(index, 1);
  }
}
