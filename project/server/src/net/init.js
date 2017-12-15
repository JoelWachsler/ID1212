export default (io) => {
  io.on('connection', (socket) => {
    console.log('Someone is connecting!')
  })
}
