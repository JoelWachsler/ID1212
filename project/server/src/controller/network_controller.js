import {
  pushSnakes,
  pushFood,
  pushGameArea
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
