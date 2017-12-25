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

  this.socket.on("connect", () => {
    this.status = "Connected!";
    this.connected = true;

    this.errorHandling();
    this.idListener();
  });
}

/**
 * Updates statuses depending on what went wrong.
 */
Net.prototype.errorHandling = function() {
  this.socket.on("connect_error", e => {
    this.connected = false;
    this.status = `Connection error: ${e.message}`;
  });

  this.socket.on("reconnect_error", (e) => {
    this.connected = false;
    this.status = `Failed to reconnect: ${e.message}`;
  });

  this.socket.on("reconnect_failed", (e) => {
    this.connected = false;
    this.status = `Failed to reconnect. Please try again later...`;
  });

  this.socket.on("reconnecting", (attempts) => {
    this.connected = false;
    this.status = `Attempting to reconnect: ${attempts} left`;
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
    this.pingServer();
    this.pongListener();

    this.connected = true;
  });
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
