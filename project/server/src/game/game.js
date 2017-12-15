import Player from './player'

const FPS = 1

/**
 * Keeps track of a game room instance.
 */
export default class Game {
  constructor(controller) {
    this.controller = controller
    this.players = []

    // Server fps
    setInterval(this.heartbeat.bind(this), 1000 / FPS)
  }

  /**
   * Pushes the game state to all clients.
   */
  heartbeat() {
    // Move all players
    this.players.forEach(player => player.applyMovement())
    
    // Broadcast them
    this.controller.heartbeat(this.players.map(player => player.state))
  }

  updateMovement(id, newDirection) {
    const player = this.players.find(player => player.id === id)
    player.updateMovement(newDirection)
  }

  /**
   * Adds a player to the current game instance.
   * 
   * @param {*} socket 
   */
  addPlayer(socket) {
    this.players.push(new Player(socket))
  }

  /**
   * Removes the player with the provided id from the list of players in this game.
   * 
   * @param {*} id 
   */
  removePlayer(id) {
    const index = this.players.findIndex(player => player.id === id)

    this.players.splice(index, 1)
  }
}
