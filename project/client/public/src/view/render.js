// @ts-check
/**
 * Renders the game.
 */
function Renderer(controller) {
  this.controller = controller;

  createCanvas(width, height); // Create actual game area.
  frameRate(FPS); // Set fps
  rectMode(CENTER); // Draw rectangles from the center
  textSize(FONT_SIZE); // Set font size

  // Variable for smoother screen movement, used by renderer.
  this.lerpingPos = createVector(0, 0);
  this.fps = 0;

  // Resize the window when the browser width or height is changed.
  window.onresize = function(event) {
    width = window.innerWidth;
    height = window.innerHeight;
    createCanvas(width, height);
  };

  // Update the FPS counter every 500 ms.
  setInterval(() => {
    this.fps = Math.round(frameRate());
  }, 500);
}

Renderer.prototype.renderScore = function(scoreBoard) {
  fill(TEXT_COLOR);

  // Draw the score in the top right corner.
  const { x, y } = this.lerpingPos;

  // Keep track of the number of snakes such that we don't draw
  // the scores on top of each other.
  let counter = 0;
  scoreBoard.forEach(({ name, score }) => {
    if (name === "You") {
      text(
        `${name}: ${score}`,
        -x + width - 4 * FONT_SIZE - FONT_SIZE / 3,
        -y + FONT_SIZE + counter++ * FONT_SIZE
      );
    } else {
      text(
        `${name}: ${score}`,
        -x + width - 6 * FONT_SIZE,
        -y + FONT_SIZE + counter++ * FONT_SIZE
      );
    }
  });
}

Renderer.prototype.renderSnakeHead = function (snakeHead) {
  const { R, G, B } = SNAKE_HEAD_COLOR;

  fill(R, G, B);
  rect(snakeHead.x, snakeHead.y, BLOCK_SIZE, BLOCK_SIZE);
}

Renderer.prototype.renderSnakeBody = function (snakeBody) {
  const { R, G, B } = SNAKE_BODY_COLOR;
  fill(R, B, G);

  snakeBody.forEach(bodyPart => {
    rect(bodyPart.x, bodyPart.y, BLOCK_SIZE, BLOCK_SIZE);
  });
}

Renderer.prototype.renderSnake = function(snake) {
  this.renderSnakeBody(snake.body);
  this.renderSnakeHead(snake.head);
}

Renderer.prototype.renderFood = function(food) {
  const { R, G, B } = FOOD_COLOR;
  fill(R, G, B);

  rect(food.x, food.y, BLOCK_SIZE, BLOCK_SIZE);
}

Renderer.prototype.centerSnake = function(snake) {
  if (snake === null) return;

  // Make the transition smoother by using a lerp.
  const vec = createVector(
    -snake.body[0].x + width / 2,
    -snake.body[0].y + height / 2
  );

  this.lerpingPos = p5.Vector.lerp(this.lerpingPos, vec, 0.1);
  const { x, y } = this.lerpingPos;
  translate(x, y);
}

Renderer.prototype.renderFps = function(fps, { x, y }) {
  fill(TEXT_COLOR);

  // Draw the framerate in the top left corner.
  text(`FPS: ${fps}`, -x, -y + FONT_SIZE);
}

/**
 * Renders the provided message over the snake.
 *
 * @param {string} message
 */
Renderer.prototype.renderMessage = function(message) {
  fill(TEXT_COLOR);

  const { x, y } = this.lerpingPos;
  text(message,
    -x + width / 2 - message.length * FONT_SIZE / 4,
    -y + height / 2 - BLOCK_SIZE
  );
}

Renderer.prototype.renderGameArea = function(gameArea) {
  fill(101, 73, 86);

  gameArea.blocks.forEach(block => {
    rect(block.x, block.y, BLOCK_SIZE, BLOCK_SIZE);
  });
}

/**
 * Centers the snake and renders all game components.
 */
Renderer.prototype.render = function() {
  const { R, G, B } = GAME_BACKGROUND_COLOR;
  background(R, G, B); // Background color

  if (!this.controller.connected) {
    this.renderMessage(this.controller.connectionStatus);

    return;
  };

  const {
    snake,
    snakes,
    food,
    gameArea,
    scoreBoard,
    isPlaying
  } = this.controller;

  if (isPlaying) this.centerSnake(snake);

  snakes.forEach(snake => this.renderSnake(snake));
  food.forEach(food => this.renderFood(food));
  if (gameArea != null) this.renderGameArea(gameArea);

  this.renderScore(scoreBoard);
  this.renderFps(this.fps, this.lerpingPos);
}

/**
 * Drawing loop.
 */
function draw() {
  this.renderer.render();
}
