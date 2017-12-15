/**
 * Broadcasts information about a game to all players connected to the provided room.
 */
export async function pushSnakes(io, snakes) {
  io.emit("update_snakes", snakes);
}

export async function pushFood(io, food) {
  io.emit("update_food", food);
}

export async function pushGameArea(io, gameArea) {
  io.emit("update_game_area", gameArea);
}
