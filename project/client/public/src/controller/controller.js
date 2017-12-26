// @ts-check
/**
 * Controller for communication between different layers and packages.
 */
function Controller(server, port) {
  this.game = new Game(this);
  this.net = new Net(this, server, port);
}

/**
 * Define getters and setters.
 */
Controller.prototype = {
  get gameOverReason() {
    return this.game.gameOverReason;
  },
  get isPlaying() {
    return this.game.isPlaying;
  },
  get scoreBoard() {
    return this.game.scoreBoard;
  },
  set id(id) {
    this.game.id = id;
  },
  get snake() {
    return this.game.snake;
  },
  set snakes(snakes) {
    this.game.snakes = snakes;
  },
  get snakes() {
    return this.game.snakes;
  },
  set food(food) {
    this.game.food = food;
  },
  get food() {
    return this.game.food;
  },
  set gameArea(gameArea) {
    this.game.gameArea = gameArea;
  },
  get gameArea() {
    return this.game.gameArea;
  },
  set gameOver(gameOverObj) {
    this.game.gameOver = gameOverObj;
  },
  get gameOver() {
    return this.game.gameOver;
  },
  set movement(newDirection) {
    this.net.movement = newDirection;
  },
  get connected() {
    return this.net.connected;
  },
  get connectionStatus() {
    return this.net.status;
  },
  newGame() {
    this.net.newGame();
  },
};
