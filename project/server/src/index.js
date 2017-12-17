// @ts-check
// Load environment variables.
require('dotenv').config()

import http from 'http'
import socketIO from 'socket.io'
import connectionHandler from './net/connection_handler'

// Init socket.io
const app = http.createServer()
const io = socketIO(app)

// Init socket connection handler
connectionHandler(io)

// Start to listen
const listener = app.listen(process.env.PORT, () => {
	console.log(`Listening to port ${listener.address().port}`)
})

export default app
