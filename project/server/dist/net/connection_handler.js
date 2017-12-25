"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = connectionHandler;
var _client_handler = _interopRequireDefault(require("./client_handler"));
var _controller = _interopRequireDefault(require("../controller/controller"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * Handles connections and disconnections.
 * 
 * @param {*} io 
 * @api public
 */
function connectionHandler(io) {
  const controller = new _controller.default(io);

  // Create a separate client handler for each client.
  io.on("connect", socket => new _client_handler.default(controller, socket));
}
//# sourceMappingURL=connection_handler.js.map