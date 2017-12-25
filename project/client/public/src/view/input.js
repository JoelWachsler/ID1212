/**
 * Listen for key input.
 */
function keyPressed() {
  switch (keyCode) {
    case UP_ARROW:
      this.controller.movement = UP;
      break;
    case RIGHT_ARROW:
      this.controller.movement = RIGHT;
      break;
    case DOWN_ARROW:
      this.controller.movement = DOWN;
      break;
    case LEFT_ARROW:
      this.controller.movement = LEFT;
      break;
  }
}
