//@ts-check
import Food from "../game/food";
import Snake from "../game/snake";
import Point from "../game/point";

/**
 * Broadcasts information about a game to all players connected to the provided room.
 * 
 * @param {*} io 
 * @param {Snake[]} snakes
 * @api public
 */
export async function pushSnakes(io, snakes) {
  io.emit("update_snakes", snakes);
}

/**
 * Update food locations.
 * 
 * @param {*} io 
 * @param {Food[]} food 
 * @api public
 */
export async function pushFood(io, food) {
  io.emit("update_food", food);
}

/**
 * Update the game area.
 * 
 * @param {*} io 
 * @param {Point[]} gameArea 
 * @api public
 */
export async function pushGameArea(io, gameArea) {
  io.emit("update_game_area", gameArea);
}

/**
 * Tell a player that their game is over.
 * 
 * @param {*} io 
 * @param {String} reason Reason for game over.
 * @api public
 */
export async function pushGameOver(io, id, reason) {
  io.emit("game_over", {
    id,
    reason
  });
}
