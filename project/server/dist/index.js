'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _http = require('http');

var _http2 = _interopRequireDefault(_http);

var _socket = require('socket.io');

var _socket2 = _interopRequireDefault(_socket);

var _connection_handler = require('./net/connection_handler');

var _connection_handler2 = _interopRequireDefault(_connection_handler);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

// Load environment variables.
require('dotenv').config();

// Init socket.io
var app = _http2.default.createServer();
var io = (0, _socket2.default)(app);

// Init socket connection handler
(0, _connection_handler2.default)(io);

// Start to listen
var listener = app.listen(process.env.PORT, function () {
	console.log('Listening to port ' + listener.address().port);
});

exports.default = app;
//# sourceMappingURL=index.js.map