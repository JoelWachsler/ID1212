import {
  GameController
} from '../controller'

/**
 * Handles connections and disconnections.
 */
export default function connectionHandler (io) {
  const gameController = new GameController(io)

  io.on('connect', (socket) => {
    console.log('Someone connected!')
    gameController.createPlayer(socket)
    const { id } = socket
    socket.emit('id', id)

    socket.on('update_movement', (newDirection) => {
      gameController.updateMovement(id, newDirection)
    })

    socket.on('disconnect', () => {
      console.log('Someone disconnected!')
      gameController.removePlayer(socket.id)
    })
  })
}
