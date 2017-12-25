"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;
var _game_controller = _interopRequireDefault(require("./game_controller"));
var _network_controller = _interopRequireDefault(require("./network_controller"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check

/**
 * Main controller which initializes the other controllers.
 */
class Controller {
  /**
                   * @param {*} io 
                   * @api public
                   */
  constructor(io) {
    this.networkController = new _network_controller.default(this, io);
    this.gameController = new _game_controller.default(this);
  }}exports.default = Controller;
//# sourceMappingURL=controller.js.map