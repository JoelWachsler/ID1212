// Load environment variables.
require('dotenv').config()

import http from 'http'
import socketio from 'socket.io'
import { init } from './net'

// Init socket.io
const app = http.createServer()
const io = socketio(app)

// Init socket listeners
init(io)

// Start to listen
const listener = app.listen(process.env.PORT, () => {
	console.log(`Listening to port ${listener.address().port}`)
})

export default app
