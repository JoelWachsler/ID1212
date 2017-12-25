/**
 * Handles the server communication.
 */
function Net(controller, host, port) {
  this.controller = controller;
  this.connect(host, port);
  this.connected = false;
  this.status = "Connecting...";
}

Net.prototype = {
  set movement(newDirection) {
    this.socket.emit("update_movement", newDirection);
  },
  newGame() {
    this.socket.emit("game_start");
  }
};

/**
 * Connects to the server and then waits for a player id.
 * 
 * @param {string} host The host to connect to.
 * @param {number} port The port to connect to on the host.
 */
Net.prototype.connect = function(server, port) {
  this.socket = io(`${server}:${port}`);
  this.errorHandling();

  this.socket.on("connect", () => {
    this.status = "Connected!";
    this.connected = true;

    this.idListener();
  });
}

/**
 * Updates statuses depending on what went wrong.
 */
Net.prototype.errorHandling = function() {
  this.socket.on("connect_error", e => {
    this.connected = false;
    this.status = "Connection error, trying to reconnect...";
  });

  this.socket.on("reconnect", (attempts) => {
    this.connected = true;
    this.status = "Reconnected!";
  });
}

/**
 * Listens for an id and then starts all the other listeners.
 */
Net.prototype.idListener = function() {
  this.socket.on("id", id => {
    this.controller.id = id;

    this.gameAreaListener();
    this.snakeListener();
    this.gameAreaListener();
    this.gameOverListener();
    this.foodListener();
    this.powerUp();

    this.connected = true;
  });
}

Net.prototype.powerUp = function() {
  this.socket.on("update_power_up", powerUp => {
    this.controller.powerUp = powerUp;
  })
}

Net.prototype.snakeListener = function() {
  this.socket.on("update_snakes", snakes => {
    this.controller.snakes = snakes;
  })
}

Net.prototype.foodListener = function() {
  this.socket.on("update_food", food => {
    this.controller.food = food;
  });
}

Net.prototype.gameAreaListener = function() {
  this.socket.on("update_game_area", gameArea => {
    this.controller.gameArea = gameArea;
  });
}

Net.prototype.gameOverListener = function() {
  this.socket.on("game_over", gameOverObj => {
    this.controller.gameOver = gameOverObj;
  });
}
