const width = window.innerWidth
const height = window.innerHeight
const PORT = 3000

/**
 * Project constructor.
 */
function setup() {
  createCanvas(width, height)
  frameRate(60)
  rectMode(CENTER) // Draw rectangles from the center

  socket = io('localhost:3000')

  socket.on('news', function (data) {
    console.log(data)
    socket.emit('my other event', { my: 'data' })
  })

  scribble = new Scribble()
  snake = new Snake()
  frame = 0
}

/**
 * Drawing loop.
 */
function draw() {
  background(0) // Background color 

  translate(-snake.head.x+width/2, -snake.head.y+height/2)

  snake.render()
  // scribble.scribbleRect( 50, 50, 25, 25 );
  rect(50, 50, 25, 25)

  if (frame++ % 60 == 0) {
    snake.applyMove()

    console.log(snake.colliding(50, 50, 25, 25))
  }
}

/**
 * Listens for keys.
 */
function keyPressed() {
  snake.changeDirection(keyCode)
}