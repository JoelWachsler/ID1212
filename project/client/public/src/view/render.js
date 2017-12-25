// @ts-check

// fill overload
function fillColor(color) {
  if (typeof color === "number") {
    fill(color);
  } else {
    const { R, G, B } = color;
    fill(R, G, B);
  }
}

/**
 * Renders the game.
 */
function Renderer(controller) {
  this.controller = controller;
  this.toggleLerp = false;

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
    textSize(FONT_SIZE);
  };

  // Update the FPS counter every 500 ms.
  setInterval(() => {
    this.fps = Math.round(frameRate());
  }, 500);
}

Renderer.prototype.renderScore = function(scoreBoard) {
  fillColor(TEXT_COLOR);

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
  fillColor(SNAKE_HEAD_COLOR);
  rect(snakeHead.x, snakeHead.y, BLOCK_SIZE, BLOCK_SIZE);
}

Renderer.prototype.renderSnakeBody = function (snakeBody) {
  fillColor(SNAKE_BODY_COLOR);

  snakeBody.forEach(bodyPart => {
    rect(bodyPart.x, bodyPart.y, BLOCK_SIZE, BLOCK_SIZE);
  });
}

Renderer.prototype.renderSnake = function(snake) {
  this.renderSnakeBody(snake.body);
  this.renderSnakeHead(snake.head);
}

Renderer.prototype.renderFood = function(food) {
  fillColor(food.special ? SPECIAL_FOOD_COLOR : FOOD_COLOR);

  rect(food.x, food.y, BLOCK_SIZE, BLOCK_SIZE);
}

Renderer.prototype.center = function({ x, y } = { x: 0, y: 0 }) {
  // Make the transition smoother by using a lerp.
  const vec = createVector(
    -x + width / 2,
    -y + height / 2
  );

  this.lerpingPos = p5.Vector.lerp(this.lerpingPos, vec, 0.2);
  translate(this.lerpingPos.x, this.lerpingPos.y);
}

Renderer.prototype.renderFps = function(fps, { x, y }) {
  fillColor(TEXT_COLOR);

  // Draw the framerate in the top left corner.
  text(`FPS: ${fps}`, -x, -y + FONT_SIZE);
}

/**
 * Renders the provided message over the snake.
 *
 * @param {string} message
 */
Renderer.prototype.renderMessage = function(message) {
  fillColor(TEXT_COLOR);

  const splitMsg = message.split("\n");
  let counter = -splitMsg.length / 2; // Vertical center
  splitMsg.forEach(msg => {
    text(msg, -msg.length * FONT_SIZE / 4, counter++ * FONT_SIZE);
  });
}

Renderer.prototype.renderGameArea = function(gameArea) {
  fillColor(GAME_AREA_COLOR);

  gameArea.blocks.forEach(block => {
    rect(block.x, block.y, BLOCK_SIZE, BLOCK_SIZE);
  });
}

Renderer.prototype.renderPowerUps = function(powerUps) {
  powerUps.forEach(power => {
    switch (power) {
      case "random_colors":
        SNAKE_BODY_COLOR = {
          R: Math.floor(Math.random() * 255) + 0,
          G: Math.floor(Math.random() * 255) + 0,
          B: Math.floor(Math.random() * 255) + 0,
        }
        break;
    }
  });
}

/**
 * Centers the snake and renders all game components.
 */
Renderer.prototype.render = function() {
  const { R, G, B } = GAME_BACKGROUND_COLOR;
  background(R, G, B); // Background color

  if (!this.controller.connected) {
    this.center();
    this.renderMessage(this.controller.connectionStatus);

    return;
  };

  const {
    snake,
    snakes,
    food,
    gameArea,
    scoreBoard,
    isPlaying,
    gameOverReason
  } = this.controller;

  if (isPlaying)  this.center(snake.head);
  else            this.center();

  snakes.forEach(snake => this.renderSnake(snake));
  food.forEach(food => this.renderFood(food));
  this.renderGameArea(gameArea);

  this.renderScore(scoreBoard);
  this.renderFps(this.fps, this.lerpingPos);
  this.renderPowerUps(this.controller.powerUp);

  if (!isPlaying) this.renderMessage(gameOverReason);
}

/**
 * Drawing loop.
 */
function draw() {
  this.renderer.render();
}
