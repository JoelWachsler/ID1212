/**
 * Handles communication to a client.
 */
export default class ClientHandler {
  constructor(controller, socket) {
    this.controller = controller;
    this.socket = socket;
    this.id = socket.id;
    console.log(`${this.id} has connected!`);

    this.controller.gameController.createPlayer(this.id);
    this.sendIdToClient(this.id);
    this.registerSocketEvents();

    // Send initial data to this client only
    this.controller.networkController.pushInitialData(socket);
  }

  registerSocketEvents() {
    this.socket.on("update_movement", newDirection =>
      this.updateMovement(newDirection)
    );

    this.socket.on("disconnect", () => this.disconnect());
  }

  sendIdToClient() {
    this.socket.emit("id", this.id);
  }

  updateMovement(newDirection) {
    this.controller.gameController.updateMovement(this.id, newDirection);
  }

  disconnect() {
    console.log(`${this.id} has disconnected!`);
    this.controller.gameController.removePlayer(this.id);
  }
}
