/**
 * Visual representation the game area.
 *
 * @param {*} blocks
 */
function GameArea(blocks) {
  console.log(blocks);
  this.blocks = blocks;

  this.render = function() {
    this.blocks.forEach(block => {
      rect(block.x, block.y, blockS, blockS);
    });
  };
}
