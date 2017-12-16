"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Handles communication to a client.
 */
var ClientHandler = function () {
  function ClientHandler(controller, socket) {
    _classCallCheck(this, ClientHandler);

    this.controller = controller;
    this.socket = socket;
    this.id = socket.id;
    console.log(this.id + " has connected!");

    this.controller.gameController.createPlayer(this.id);
    this.sendIdToClient(this.id);
    this.registerSocketEvents();

    // Send initial data to this client only
    this.controller.networkController.pushInitialData(socket);
  }

  _createClass(ClientHandler, [{
    key: "registerSocketEvents",
    value: function registerSocketEvents() {
      var _this = this;

      this.socket.on("update_movement", function (newDirection) {
        return _this.updateMovement(newDirection);
      });

      this.socket.on("disconnect", function () {
        return _this.disconnect();
      });
    }
  }, {
    key: "sendIdToClient",
    value: function sendIdToClient() {
      this.socket.emit("id", this.id);
    }
  }, {
    key: "updateMovement",
    value: function updateMovement(newDirection) {
      this.controller.gameController.updateMovement(this.id, newDirection);
    }
  }, {
    key: "disconnect",
    value: function disconnect() {
      console.log(this.id + " has disconnected!");
      this.controller.gameController.removePlayer(this.id);
    }
  }]);

  return ClientHandler;
}();

exports.default = ClientHandler;
//# sourceMappingURL=client_handler.js.map