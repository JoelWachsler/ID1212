// @ts-check
import Game from "../game/game";
import Controller from "./controller";

/**
 * Communication to the game must go through this controller.
 */
export default class GameController {
  /**
   * @param {Controller} controller 
   */
  constructor(controller) {
    this.controller = controller;
    this.game = new Game(this.controller);
  }

  /**
   * Add the player to a game.
   * 
   * @param {string} id The id of the player to add.
   * @api public
   */
  createPlayer(id) {
    this.game.addPlayer(id);
  }

  /**
   * Remove the player from a game.
   * 
   * @param {string} id The player with the id to remove.
   * @api public
   */
  removePlayer(id) {
    this.game.removePlayer(id);
  }

  /**
   * Update the movement of a player.
   * 
   * @param {string} id The player to update the movement for.
   * @param {number} newDirection The new direction of the player
   * @api public
   */
  updateMovement(id, newDirection) {
    this.game.updateMovement(id, newDirection);
  }
}
