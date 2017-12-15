/**
 * Broadcasts information about a game to all players connected to the provided room.
 */
export function heartbeat(io, players) {
  io.emit('update_snakes', players)
}
