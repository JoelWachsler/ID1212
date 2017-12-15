import GameController from "./game_controller";
import NetworkController from "./network_controller";

export default class Controller {
  constructor(io) {
    this.gameController = new GameController(this);
    this.networkController = new NetworkController(this, io);
  }
}
