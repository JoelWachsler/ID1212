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

  const idListener = socket.on("id", id => {
    console.log("Got an id:", id);
    this.id = id;

    // We don't need the listener anymore
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
  });
}

/**
 * Drawing loop.
 */
function draw() {
  background(0); // Background color

  // Find our snake and put it in the middle of the screen
  if (snake !== null) {
    // Make the transition smoother
    translate(-snake.body[0].x + width / 2, -snake.body[0].y + height / 2);
  } else translate(0, 0);

  snakes.forEach(snake => snake.render());
  food.forEach(food => food.render());
  if (gameArea != null) gameArea.render();
}

/**
 * Listens for keys.
 */
function keyPressed() {
  const UP = 0;
  const RIGHT = 1;
  const DOWN = 2;
  const LEFT = 3;

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
