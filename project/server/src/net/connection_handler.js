import ClientHandler from "./client_handler";
import Controller from "../controller/controller";

/**
 * Handles connections and disconnections.
 */
export default function connectionHandler(io) {
  const controller = new Controller(io);

  // Create a separate client handler for each client.
  io.on("connect", socket => new ClientHandler(controller, socket));
}
