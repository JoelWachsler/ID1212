// @ts-check
import GameController from "./game_controller";
import NetworkController from "./network_controller";

/**
 * Main controller which initializes the other controllers.
 */
export default class Controller {
  /**
   * @param {*} io 
   * @api public
   */
  constructor(io) {
    this.networkController = new NetworkController(this, io);
    this.gameController = new GameController(this);
  }
}
