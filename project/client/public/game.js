const width = window.innerWidth
const height = window.innerHeight
const PORT = 3000
const SERVER = 'localhost'

/**
 * Project constructor.
 */
function setup() {
  createCanvas(width, height)
  frameRate(60)
  rectMode(CENTER) // Draw rectangles from the center

  // Connect to the server
  socket = io(`${SERVER}:${PORT}`)

  // Container for all snakes which will be rendered
  snakes = []
  // Our snake
  snake = null

  const idListener = socket.on('id', (id) => {
    console.log('Got an id:', id)
    this.id = id

    // We don't need the listener anymore
    socket.removeListener('id', idListener)

    // Start to listen for game updates instead
    socket.on('update_snakes', (snakes) => {
      // Can optimize this if needed
      this.snake = snakes.find(snake => snake.id === this.id)
      this.snakes = snakes.map(snake => new Snake(snake.body))
    })
  })
}

/**
 * Drawing loop.
 */
function draw() {
  background(0) // Background color 

  // Find our snake and put it in the middle of the screen
  if (snake !== null)
    translate(-snake.body[0].x+width/2, -snake.body[0].y+height/2)
  else
    translate(0, 0)

  snakes.forEach(snake => snake.render())
  rect(50, 50, 25, 25)
}

/**
 * Listens for keys.
 */
function keyPressed() {
  const UP = 0
  const RIGHT = 1
  const DOWN = 2
  const LEFT = 3

  switch (keyCode) {
    case UP_ARROW:
      socket.emit('update_movement', UP)
      break
    case RIGHT_ARROW:
      socket.emit('update_movement', RIGHT)
      break
    case DOWN_ARROW:
      socket.emit('update_movement', DOWN)
      break
    case LEFT_ARROW:
      socket.emit('update_movement', LEFT)
      break
  }
}