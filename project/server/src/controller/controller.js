import GameController from "./game_controller";
import NetworkController from "./network_controller";

/**
 * Main controller which initializes the other controllers.
 */
export default class Controller {
  constructor(io) {
    this.networkController = new NetworkController(this, io);
    this.gameController = new GameController(this);
  }
}
