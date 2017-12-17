/**
 * Project constructor.
 */
function setup() {
  createCanvas(width, height);
  frameRate(60);
  rectMode(CENTER); // Draw rectangles from the center

  // Connect to the server
  socket = io(`${SERVER}:${PORT}`);

  // Container for all snakes to be rendered.
  snakes = [];
  // Our snake
  snake = null;
  // Container for all pieces of food to be rendered.
  food = [];
  // Our game area
  gameArea = null;
  // Variable for smoother screen movement.
  lerpingPos = createVector(0,0);
  // Update fps every half a second
  fps = 0;
  setInterval(() => {
    this.fps = Math.round(frameRate())
  }, 500);

  const idListener = socket.on("id", id => {
    console.log("Got an id:", id);
    this.id = id;

    // We don't need the id listener anymore
    socket.removeListener("id", idListener);

    // Listen for snake changes
    socket.on("update_snakes", snakes => {
      // Can optimize this if needed
      this.snake = snakes.find(snake => snake.id === this.id);
      this.snakes = snakes.map(snake => new Snake(snake.body));
    });

    // Listen for food changes
    socket.on("update_food", food => {
      this.food = food.map(food => new Food(food.point));
    });

    // Listen for game area changes
    socket.on("update_game_area", gameArea => {
      this.gameArea = new GameArea(gameArea);
    });

    // Listen for when we die
    socket.on("game_over", ({ id, reason }) => {
      if (id == this.id) {
        alert(`You died: ${reason}`);
        location.reload();
      }
    });
  });
}

function centerOurSnake() {
  // Put our snake in the middle of the screen if it exists.
  if (snake !== null && typeof snake !== 'undefined') {
    // Make the transition smoother
    const vec = createVector(-snake.body[0].x + width / 2, -snake.body[0].y + height / 2);
    lerpingPos = p5.Vector.lerp(lerpingPos, vec, 0.1);
    translate(lerpingPos.x, lerpingPos.y);
  }
}

function renderFps() {
  if (snake !== null) {
    textSize(fontSize);
    fill(255);

    const { x, y } = lerpingPos;
    // Draw the framerate in the top left corner.
    text(fps, -x, -y+fontSize);
  }
}

function renderGame() {
  snakes.forEach(snake => snake.render());
  food.forEach(food => food.render());
  if (gameArea != null) gameArea.render();
}

/**
 * Drawing loop.
 */
function draw() {
  background(61,84,103); // Background color

  centerOurSnake();
  renderGame();
  renderFps();
}

/**
 * Listens for keys.
 */
function keyPressed() {
  switch (keyCode) {
    case UP_ARROW:
      socket.emit("update_movement", UP);
      break;
    case RIGHT_ARROW:
      socket.emit("update_movement", RIGHT);
      break;
    case DOWN_ARROW:
      socket.emit("update_movement", DOWN);
      break;
    case LEFT_ARROW:
      socket.emit("update_movement", LEFT);
      break;
  }
}
