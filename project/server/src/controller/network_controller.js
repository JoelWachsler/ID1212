// @ts-check
import {
  pushSnakes,
  pushFood,
  pushGameArea,
  pushGameOver,
} from "../net/game_broadcast_handler";
import Controller from "./controller";
import Point from "../game/point";
import Food from "../game/food";
import Snake from "../game/snake";

/**
 * All communication to the net package must go through this controller.
 */
export default class NetworkController {
  /**
   * @param {Controller} controller 
   * @param {*} io 
   * @api public
   */
  constructor(controller, io) {
    this.controller = controller;
    this.io = io;
  }

  /**
   * Pushes initial game data to a newly connected client.
   * 
   * @param {*} socket 
   * @api public
   */
  pushInitialData(socket) {
    // Data to push
    const { snakes, food, gameArea } = this.controller.gameController.game;

    pushSnakes(socket, snakes);
    pushFood(socket, food);
    pushGameArea(socket, gameArea);
  }

  /**
   * Update if a player dies.
   * 
   * @param {string} id The id the player who lost.
   * @param {string} reason The reason why they lost.
   * @api public
   */
  pushGameOver(id, reason) {
    pushGameOver(this.io, id, reason);
  }

  /**
   * Updates the game area.
   * 
   * @param {Point[]} gameArea 
   * @api public
   */
  pushGameArea(gameArea) {
    pushGameArea(this.io, gameArea);
  }

  /**
   * Updates the food.
   * 
   * @param {Food[]} food 
   * @api public
   */
  pushFood(food) {
    pushFood(this.io, food);
  }

  /**
   * Update the snakes.
   * 
   * @param {Snake[]} snakes 
   * @api public
   */
  pushSnakes(snakes) {
    pushSnakes(this.io, snakes);
  }
}
