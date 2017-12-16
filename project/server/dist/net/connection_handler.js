"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = connectionHandler;

var _client_handler = require("./client_handler");

var _client_handler2 = _interopRequireDefault(_client_handler);

var _controller = require("../controller/controller");

var _controller2 = _interopRequireDefault(_controller);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Handles connections and disconnections.
 */
function connectionHandler(io) {
  var controller = new _controller2.default(io);

  // Create a separate client handler for each client.
  io.on("connect", function (socket) {
    return new _client_handler2.default(controller, socket);
  });
}
//# sourceMappingURL=connection_handler.js.map