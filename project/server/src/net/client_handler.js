// @ts-check
import Controller from "../controller/controller";

/**
 * Handles communication to a client.
 */
export default class ClientHandler {
  /**
   * @param {Controller} controller 
   * @param {*} socket 
   * @api public
   */
  constructor(controller, socket) {
    this.controller = controller;
    this.socket = socket;
    this.id = socket.id;
    console.log(`${this.id} has connected!`);

    this.sendIdToClient();
    this.registerSocketEvents();

    // Send initial data to this client only
    this.controller.networkController.pushInitialData(socket);
  }

  /**
   * Registers various events to listen to from the client.
   * 
   * @api private
   */
  registerSocketEvents() {
    this.socket.on("update_movement", newDirection => this.updateMovement(newDirection));
    this.socket.on("game_start", () => this.controller.gameController.createPlayer(this.id));
    this.socket.on("disconnect", () => this.disconnect());
  }

  /**
   * Sends the clients id to them.
   * 
   * @api private
   */
  sendIdToClient() {
    this.socket.emit("id", this.id);
  }

  /**
   * Updates the movement of the current player.
   * 
   * @param {number} newDirection 
   * @api private
   */
  updateMovement(newDirection) {
    this.controller.gameController.updateMovement(this.id, newDirection);
  }

  /**
   * Removes the player from the game if they disconnect.
   * 
   * @api private
   */
  disconnect() {
    console.log(`${this.id} has disconnected!`);
    this.controller.gameController.removePlayer(this.id);
  }
}
