"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

/**
 * Broadcasts information about a game to all players connected to the provided room.
 * 
 * @param {*} io 
 * @param {*} snakes
 */
var pushSnakes = exports.pushSnakes = function () {
  var _ref = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee(io, snakes) {
    return regeneratorRuntime.wrap(function _callee$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            io.emit("update_snakes", snakes);

          case 1:
          case "end":
            return _context.stop();
        }
      }
    }, _callee, this);
  }));

  return function pushSnakes(_x, _x2) {
    return _ref.apply(this, arguments);
  };
}();

/**
 * Update food locations.
 * 
 * @param {*} io 
 * @param {*} food 
 */


var pushFood = exports.pushFood = function () {
  var _ref2 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee2(io, food) {
    return regeneratorRuntime.wrap(function _callee2$(_context2) {
      while (1) {
        switch (_context2.prev = _context2.next) {
          case 0:
            io.emit("update_food", food);

          case 1:
          case "end":
            return _context2.stop();
        }
      }
    }, _callee2, this);
  }));

  return function pushFood(_x3, _x4) {
    return _ref2.apply(this, arguments);
  };
}();

/**
 * Update the game area.
 * 
 * @param {*} io 
 * @param {*} gameArea 
 */


var pushGameArea = exports.pushGameArea = function () {
  var _ref3 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee3(io, gameArea) {
    return regeneratorRuntime.wrap(function _callee3$(_context3) {
      while (1) {
        switch (_context3.prev = _context3.next) {
          case 0:
            io.emit("update_game_area", gameArea);

          case 1:
          case "end":
            return _context3.stop();
        }
      }
    }, _callee3, this);
  }));

  return function pushGameArea(_x5, _x6) {
    return _ref3.apply(this, arguments);
  };
}();

/**
 * Tell a player that their game is over.
 * 
 * @param {*} io 
 * @param {*} reason 
 */


var pushGameOver = exports.pushGameOver = function () {
  var _ref4 = _asyncToGenerator( /*#__PURE__*/regeneratorRuntime.mark(function _callee4(io, reason) {
    return regeneratorRuntime.wrap(function _callee4$(_context4) {
      while (1) {
        switch (_context4.prev = _context4.next) {
          case 0:
            io.emit("game_over", reason);

          case 1:
          case "end":
            return _context4.stop();
        }
      }
    }, _callee4, this);
  }));

  return function pushGameOver(_x7, _x8) {
    return _ref4.apply(this, arguments);
  };
}();

function _asyncToGenerator(fn) { return function () { var gen = fn.apply(this, arguments); return new Promise(function (resolve, reject) { function step(key, arg) { try { var info = gen[key](arg); var value = info.value; } catch (error) { reject(error); return; } if (info.done) { resolve(value); } else { return Promise.resolve(value).then(function (value) { step("next", value); }, function (err) { step("throw", err); }); } } return step("next"); }); }; }
//# sourceMappingURL=game_broadcast_handler.js.map