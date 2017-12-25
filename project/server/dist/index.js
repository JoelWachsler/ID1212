"use strict";Object.defineProperty(exports, "__esModule", { value: true });exports.default = void 0;



var _http = _interopRequireDefault(require("http"));
var _socket = _interopRequireDefault(require("socket.io"));
var _connection_handler = _interopRequireDefault(require("./net/connection_handler"));function _interopRequireDefault(obj) {return obj && obj.__esModule ? obj : { default: obj };} // @ts-check
// Load environment variables.
require("dotenv").config(); // Init socket.io
const app = _http.default.createServer();
const io = (0, _socket.default)(app);

// Init socket connection handler
(0, _connection_handler.default)(io);

// Start to listen
const listener = app.listen(process.env.PORT, () => {
  console.log(`Listening to port ${listener.address().port}`);
});var _default =

app;exports.default = _default;
//# sourceMappingURL=index.js.map