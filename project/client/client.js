// Load environment variables
require('dotenv').config()

const express = require('express')
const app = express()
const path = require('path')
var server = require('http').Server(app)
const io = require('socket.io')(server)

app.use(express.static(path.join(__dirname, 'public')))
app.use(express.static(path.join(__dirname, 'libs')))
app.use(express.static(path.join(__dirname, 'node_modules')))

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'))
})

app.listen(process.env.PORT)
