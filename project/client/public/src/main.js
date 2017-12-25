/**
 * Main setup function.
 */
function setup() {
  this.controller = new Controller(SERVER, PORT);
  this.renderer = new Renderer(this.controller);
}
