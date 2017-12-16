//@ts-check

/**
 * Broadcasts information about a game to all players connected to the provided room.
 * 
 * @param {*} io 
 * @param {*} snakes
 */
export async function pushSnakes(io, snakes) {
  io.emit("update_snakes", snakes);
}

/**
 * Update food locations.
 * 
 * @param {*} io 
 * @param {*} food 
 */
export async function pushFood(io, food) {
  io.emit("update_food", food);
}

/**
 * Update the game area.
 * 
 * @param {*} io 
 * @param {*} gameArea 
 */
export async function pushGameArea(io, gameArea) {
  io.emit("update_game_area", gameArea);
}

/**
 * Tell a player that their game is over.
 * 
 * @param {*} io 
 * @param {*} reason 
 */
export async function pushGameOver(io, id, reason) {
  io.emit("game_over", {
    id,
    reason
  });
}
