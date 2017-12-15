import { Game } from '../game'

import {
  heartbeat
} from '../net'

export default class GameController {
  constructor(io) {
    this.io = io
    this.game = new Game(this)
  }

  createPlayer(socket) {
    this.game.addPlayer(socket)
  }

  removePlayer(id) {
    this.game.removePlayer(id)
  }

  updateMovement(id, newDirection) {
    this.game.updateMovement(id, newDirection)
  }

  heartbeat(players) {
    heartbeat(this.io, players)
  }
}
