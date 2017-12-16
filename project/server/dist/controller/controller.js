"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _game_controller = require("./game_controller");

var _game_controller2 = _interopRequireDefault(_game_controller);

var _network_controller = require("./network_controller");

var _network_controller2 = _interopRequireDefault(_network_controller);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

var Controller = function Controller(io) {
  _classCallCheck(this, Controller);

  this.gameController = new _game_controller2.default(this);
  this.networkController = new _network_controller2.default(this, io);
};

exports.default = Controller;
//# sourceMappingURL=controller.js.map