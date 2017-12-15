import Game from "../game/game";

export default class GameController {
  constructor(controller) {
    this.controller = controller;
    this.game = new Game(this.controller);
  }

  createPlayer(id) {
    this.game.addPlayer(id);
  }

  removePlayer(id) {
    this.game.removePlayer(id);
  }

  updateMovement(id, newDirection) {
    this.game.updateMovement(id, newDirection);
  }
}
