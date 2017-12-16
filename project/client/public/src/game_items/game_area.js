/**
 * Visual representation the game area.
 *
 * @param {*} blocks
 */
function GameArea(blocks) {
  this.blocks = blocks;

  this.render = function() {
    fill(101,73,86)
    this.blocks.forEach(block => {
      rect(block.x, block.y, blockS, blockS);
    });
  };
}
