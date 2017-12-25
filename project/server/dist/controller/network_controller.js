"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _game_broadcast_handler = require("../net/game_broadcast_handler");





var _controller = _interopRequireDefault(require("./controller"));
var _point = _interopRequireDefault(require("../game/point"));
var _food = _interopRequireDefault(require("../game/food"));
var _snake = _interopRequireDefault(require("../game/snake"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * All communication to the net package must go through this controller.
 */
class NetworkController {
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

    (0, _game_broadcast_handler.pushSnakes)(socket, snakes);
    (0, _game_broadcast_handler.pushFood)(socket, food);
    (0, _game_broadcast_handler.pushGameArea)(socket, gameArea);
  }

  /**
     * Update if a player dies.
     * 
     * @param {string} id The id the player who lost.
     * @param {string} reason The reason why they lost.
     * @api public
     */
  pushGameOver(id, reason) {
    (0, _game_broadcast_handler.pushGameOver)(this.io, id, reason);
  }

  /**
     * Updates the game area.
     * 
     * @param {Point[]} gameArea 
     * @api public
     */
  pushGameArea(gameArea) {
    (0, _game_broadcast_handler.pushGameArea)(this.io, gameArea);
  }

  /**
     * Updates the food.
     * 
     * @param {Food[]} food 
     * @api public
     */
  pushFood(food) {
    (0, _game_broadcast_handler.pushFood)(this.io, food);
  }

  /**
     * Update the snakes.
     * 
     * @param {Snake[]} snakes 
     * @api public
     */
  pushSnakes(snakes) {
    (0, _game_broadcast_handler.pushSnakes)(this.io, snakes);
  }}exports.default = NetworkController;
//# sourceMappingURL=network_controller.js.map