import {
  pushSnakes,
  pushFood,
  pushGameArea,
  pushGameOver
} from "../net/game_broadcast_handler";

export default class NetworkController {
  constructor(controller, io) {
    this.controller = controller;
    this.io = io;
  }

  pushInitialData(socket) {
    // Data to push
    const { snakes, food, gameArea } = this.controller.gameController.game;

    pushSnakes(socket, snakes);
    pushFood(socket, food);
    pushGameArea(socket, gameArea);
  }

  pushGameOver(id, reason) {
    pushGameOver(this.io, id, reason);
  }

  pushGameArea(gameArea) {
    pushGameArea(this.io, gameArea);
  }

  pushFood(food) {
    pushFood(this.io, food);
  }

  pushSnakes(snakes) {
    pushSnakes(this.io, snakes);
  }
}
