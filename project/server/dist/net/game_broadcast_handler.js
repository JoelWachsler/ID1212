"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.pushSnakes = pushSnakes;exports.pushFood = pushFood;exports.pushGameArea = pushGameArea;exports.pushGameOver = pushGameOver;
var _food = _interopRequireDefault(require("../game/food"));
var _snake = _interopRequireDefault(require("../game/snake"));
var _point = _interopRequireDefault(require("../game/point"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} //@ts-check

/**
 * Broadcasts information about a game to all players connected to the provided room.
 * 
 * @param {*} io 
 * @param {Snake[]} snakes
 * @api public
 */
async function pushSnakes(io, snakes) {
  io.emit("update_snakes", snakes);
}

/**
   * Update food locations.
   * 
   * @param {*} io 
   * @param {Food[]} food 
   * @api public
   */
async function pushFood(io, food) {
  io.emit("update_food", food);
}

/**
   * Update the game area.
   * 
   * @param {*} io 
   * @param {Point[]} gameArea 
   * @api public
   */
async function pushGameArea(io, gameArea) {
  io.emit("update_game_area", gameArea);
}

/**
   * Tell a player that their game is over.
   * 
   * @param {*} io 
   * @param {String} reason Reason for game over.
   * @api public
   */
async function pushGameOver(io, id, reason) {
  io.emit("game_over", {
    id,
    reason });

}
//# sourceMappingURL=game_broadcast_handler.js.map